package com.tahsinsayeed.reliableudp;


import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        Sender sender = new Sender(new File("data.txt"), 1234,
                InetAddress.getByName("localhost"), 1235);
        sender.send();
        sender.close();
    }
}
