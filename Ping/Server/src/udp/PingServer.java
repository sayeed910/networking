package udp;

import java.io.*;
import java.net.*;
import java.util.Random;


public class PingServer {

    public static void main(String[] args) throws Exception
    {
        int port = 8080;

        DatagramSocket socket = new DatagramSocket(port);

        while (true) {
            DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
            socket.receive(request);
            printData(request);
            //simulate delay
            Thread.sleep( ((int)(Math.random() * 100)));

            InetAddress clientHost = request.getAddress();
            int clientPort = request.getPort();
            byte[] buf = request.getData();
            DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
            socket.send(reply);

        }
    }

    private static void printData(DatagramPacket request) throws Exception
    {
        byte[] buf = request.getData();

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

        System.out.println(
                "Received from " +
                        request.getAddress().getHostAddress() +
                        ": " +
                        br.readLine() );
    }
}