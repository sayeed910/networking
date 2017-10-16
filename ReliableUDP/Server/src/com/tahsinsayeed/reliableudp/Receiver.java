package com.tahsinsayeed.reliableudp;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

public class Receiver {
    public static final int BUFFER_SIZE = 1024;
    private final DatagramSocket socket;
    private static final int PACKETS_PER_FRAME = 8;
    private String receivedData = "";
    private boolean running;
    private final DatagramPacket receivedPacket;
    private DatagramPacket sentPacket;
    private int requiredFrameNo = 0;

    public Receiver(int port) {
        socket = createSocket(port);
        receivedPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        sentPacket = new DatagramPacket(new byte[30], 30);
    }

    private DatagramSocket createSocket(int port) {
        try {
            return new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    public void start() {
        running = true;

        while(running){
            receiveDataAndSendReply();
        }

    }

    private void receiveDataAndSendReply() {
        receivePacket(receivedPacket);
        String packetData = new String(receivedPacket.getData()).trim();
        char c = packetData.charAt(0);
        int receivedFrameNo = c - '0';

        if (isEndOfTransmission(c)){
            stop();
            return;
        }

        if (isRequiredFrame(receivedFrameNo)) {
            sendAck(++requiredFrameNo % (PACKETS_PER_FRAME + 1));
            receivedData += packetData.substring(1);
        }
        else
            sendAck(requiredFrameNo);
    }

    private boolean isEndOfTransmission(char c) {
        return c == '-';
    }

    private boolean isRequiredFrame(int receivedFrameNo) {
        return receivedFrameNo == requiredFrameNo;
    }

    private void sendAck(int frameNo) {
        String reply = frameNo + "ACK";
        sentPacket.setData(reply.getBytes());
        sentPacket.setAddress(receivedPacket.getAddress());
        sentPacket.setPort(receivedPacket.getPort());

        sendPacket(sentPacket);
    }

    private void sendPacket(DatagramPacket sentPacket) {
        try {
            socket.send(sentPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receivePacket(DatagramPacket receivedPacket){
        try {
            socket.receive(receivedPacket);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            Logger.getGlobal().info("socket closed");
        }
    }

    public void close() {
        socket.close();
    }

    public String getReceivedData() {
        return receivedData;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }


}
