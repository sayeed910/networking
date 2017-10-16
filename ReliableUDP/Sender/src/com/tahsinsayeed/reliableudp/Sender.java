package com.tahsinsayeed.reliableudp;

import java.io.*;
import java.net.*;

public class Sender {
    private final DatagramSocket socket;
    private DatagramPacket packetToSend;
    private final Frame frame;
    private int packetToSendNext = 0;

    public Sender(File dataSource, int port, InetAddress receiverAddress, int receiverPort) {
        try {
            this.socket = new DatagramSocket(port);
            packetToSend = new DatagramPacket(new byte[100], 100, receiverAddress, receiverPort);
            this.frame = new Frame(dataSource);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send(){
        Thread responseReceiver = new Thread(this::receiveResponse);
        responseReceiver.start();

        frame.slide(packetToSendNext);
        while(frame.hasNext()){
            for (String data : frame.next()){
                packetToSend.setData(data.getBytes());
                sendPacket();
            }
            delay(1000);
            frame.slide(packetToSendNext);
        }

        responseReceiver.interrupt();
        packetToSend.setData("-end".getBytes());
        sendPacket();
    }

    public void receiveResponse(){
        while(!Thread.interrupted()) {
            DatagramPacket packet = new DatagramPacket(new byte[100], 100);
            receivePacket(packet);
            packetToSendNext = new String(packet.getData()).charAt(0) - '0';
        }
    }

    private void receivePacket(DatagramPacket packet){
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendPacket()  {
        try {
            socket.send(packetToSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void close() {
        socket.close();
    }


}
