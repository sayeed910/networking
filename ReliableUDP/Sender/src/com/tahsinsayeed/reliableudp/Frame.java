package com.tahsinsayeed.reliableudp;

import java.io.*;
import java.util.*;

public class Frame {
    private static final int PACKETS_PER_WINDOW = 3;
    private static final int PACKETS_PER_FRAME = 8;


    private final BufferedReader reader;
    private int packetNo = 0;
    private Queue<String> frame;


    public Frame(File datasource) {
        try {
            reader = new BufferedReader(new FileReader(datasource));
            frame = new ArrayDeque<>();
//            loadPackets();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private void loadPackets() {
//        try {
//            for (int i = 0; i < 8; i++) {
//                frame.add(loadPacket());
//            }
//        } catch (EndOfFile e){
//
//        }
//    }
//
//    private String loadPacket() throws EndOfFile {
//        try{
//            char[] buf = new char[10];
//            int bytesRead = reader.read(buf);
//
//            if (bytesRead == -1) throw new EndOfFile();
//            else return new String(buf).trim();
//
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//    }


    public boolean hasNext() {
        return false;
    }

    public void next() {
        if (!hasNext()) throw new NoMoreFrame();
    }

    private class EndOfFile extends Exception {
    }

    public class NoMoreFrame extends RuntimeException {
    }
}