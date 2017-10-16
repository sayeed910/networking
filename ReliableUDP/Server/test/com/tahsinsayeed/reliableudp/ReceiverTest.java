package com.tahsinsayeed.reliableudp;

import org.junit.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;

public class ReceiverTest {

    private final InetAddress inetAddress = InetAddress.getByName("localhost");
    private Receiver receiver;
    private DatagramSocket sender;
    private final DatagramPacket sentPacket = new DatagramPacket(new byte[1024], 0, 1024, inetAddress, 1234);

    public ReceiverTest() throws UnknownHostException {
    }

    @Before
    public void setUp() throws Exception {
        receiver = new Receiver(1234);
        sender = new DatagramSocket(1235);
    }
    @After
    public void tearDown() throws Exception {
        receiver.close();
        sender.close();
    }

    private void startReceiver() {
        new Thread(()->receiver.start()).start();
    }



    private void sendData(String data) throws IOException {
        sentPacket.setData(data.getBytes());
        startReceiver();
        sender.send(sentPacket);
    }

    private String receiveReply() throws IOException {
        DatagramPacket replyPacket = new DatagramPacket(new byte[100], 100);
        sender.receive(replyPacket);
        return new String(replyPacket.getData());
    }

    @Test
    public void receiveAPacket() throws Exception {
        sendData("1one");
        receiver.stop();

        Thread.sleep(100);

        assertThat(receiver.getReceivedData(),equalTo("one"));
    }

    @Test
    public void askNextPacketOnPacketReceive() throws Exception {
        sendData("0one");
        receiver.stop();

        String reply = receiveReply();

        assertThat(reply.charAt(0),equalTo('1'));
    }

    @Test
    public void notTheRightFrame_ResendPreviousAck() throws Exception {
        sendData("5file");
        receiver.stop();

        String reply = receiveReply();

        assertThat(reply.charAt(0), equalTo('0'));
    }

    @Test
    public void stopIfFirstCharIsMinus() throws Exception {
        sendData("-ajfsdlsjal");
        assertFalse(receiver.isRunning());
    }
}
