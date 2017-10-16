package com.tahsinsayeed.reliableudp;

/**
 * Created by Tahsin Sayeed on 16/10/2017.
 */
public class Main {

    public static void main(String[] args) {
        Receiver receiver = new Receiver(1235);
        receiver.start();
        System.out.println(receiver.getReceivedData());
    }
}
