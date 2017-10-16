package com.tahsinsayeed.reliableudp;

import java.io.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Frame {
    private static final int PACKETS_PER_FRAME = 7;


    private final BufferedReader reader;
    private int packetNo = 0;
    private Queue<Packet> frame;


    public Frame(File datasource) {
        try {
            reader = new BufferedReader(new FileReader(datasource));
            frame = new ArrayDeque<>();
            loadPackets(PACKETS_PER_FRAME);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadPackets(int n) {
        try {
            for (int i = 0; i < n; i++) {
                frame.add(loadPacket());
            }
        } catch (EndOfFile e){

        }
    }

    public void slide(int uptoPacket){

        while(!frame.isEmpty() && uptoPacket != frame.peek().packetNo){
            frame.remove();
            loadPackets(1);
        }

    }

    private Packet loadPacket() throws EndOfFile {
        try{
            char[] buf = new char[10];
            int bytesRead = reader.read(buf);

            if (bytesRead == -1) throw new EndOfFile();
            else return new Packet(packetNo++ % (PACKETS_PER_FRAME + 1), new String(buf).trim());

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    public boolean hasNext() {

        return frame.size() > 0;
    }

    public List<String> next() {
        if (!hasNext()) throw new NoMoreFrame();

        return frame.stream().map(Packet::toString).collect(Collectors.toList());


    }

    private class EndOfFile extends Exception {
    }

    public class NoMoreFrame extends RuntimeException {
    }


    public class Packet{
        public final int packetNo;
        public final String data;

        public Packet(int packetNo, String data) {
            this.packetNo = packetNo;
            this.data = data;
        }

        @Override
        public String toString() {
            return packetNo + data;
        }
    }
}