package datagramclient;
import java.io.*;
import java.net.*;
import java.util.Date;

public class PingClient {


    private static final int MAX_TIMEOUT = 1000;
    private static final int LOOP_COUNTER = 10;


    public static void main(String[] args) throws Exception {

        int port = 8080;
        InetAddress serverAddress = InetAddress.getByName("localhost");
        DatagramSocket socket = new DatagramSocket(9090);

        int sequenceNumber = 0;
        while (sequenceNumber++ < LOOP_COUNTER) {
            long msSend = System.currentTimeMillis();
            String str = "PING " + sequenceNumber + " " + msSend + " \n";
            byte[] buf =  str.getBytes();

            DatagramPacket pingPacket = new DatagramPacket(buf, buf.length, serverAddress, port);

            socket.send(pingPacket);
            try {
                socket.setSoTimeout(MAX_TIMEOUT);
                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                socket.receive(response);
                long msReceived = System.currentTimeMillis();

                printData(response, msReceived - msSend);
            } catch (IOException e) {
                System.out.println("Timeout for packet " + sequenceNumber);
            }
        }
    }


    private static void printData(DatagramPacket request, long delayTime) throws Exception
    {
        byte[] buf = request.getData();

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
        System.out.println(
                "Received from " +
                        request.getAddress().getHostAddress() +
                        ": " +
                        br.readLine()+ " Delay: " + delayTime );
    }
}