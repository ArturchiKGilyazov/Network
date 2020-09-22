import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class App {
    private String message;
    private String IP;
    private int port;
    private SocketAddress address;
    private MulticastSocket socket = new MulticastSocket(port);
    private int timeout = 3000;
    private HashMap<String, Long> clone = new HashMap<String, Long>();
    long LastTimeOut;
    long secondTimeout;

    List<Long> DIMA = new LinkedList<Long>();


    App(String message_, String IP_, int port_) throws IOException {
        this.message = message_;
        this.IP = IP_;
        this.port = port_;
        socket.setSoTimeout(timeout);
        address = new InetSocketAddress(IP, port);

        socket.joinGroup(this.address, NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        clone.clear();
        findClone();

    }

    public void findClone() throws IOException {
        LastTimeOut = System.currentTimeMillis();
        while(true){
            if ((System.currentTimeMillis() - LastTimeOut) > timeout){
                sendMessage("F");
            }
            String message = reciveMessage();

            if (!message.isEmpty()){
                clone.put(message, secondTimeout);
            }

            //for(HashMap<String, Long> entry: )
            //for (int i = 0; i < clone.size(); i++){
            /*for (HashMap<String, Long> i: clone){

            }*/

            for (HashMap.Entry<String, Long> entry: clone.entrySet()){
                if ((System.currentTimeMillis() - entry.getValue()) > timeout){
                    DIMA.add(entry.getValue());

                }
            }
            for (Long entyty: DIMA){
                clone.remove(entyty.longValue());
            }
            DIMA.clear();
/*
            for (Map.Entry entry: Map.of().entrySet()){
                if ((System.currentTimeMillis() - entry.getValue()) > timeout)
            }*/

            /*for (Object entry: clone) {
                if (System.currentTimeMillis() - i.value) clone.remove()
            }*/
            System.out.println(clone.size());
        }
    }

    public String reciveMessage() throws IOException {
        //Array BA = new Array();
        byte[] BA = new byte[255];
        DatagramPacket datagramPacket = new DatagramPacket(BA, BA.length);
        try {
            socket.receive(datagramPacket);
        } catch (SocketTimeoutException ex) {
            //e.printStackTrace();
            return "";
        }

        secondTimeout = System.currentTimeMillis();
        return datagramPacket.getAddress().toString();
    }
    public void sendMessage(String string) throws IOException {
        socket.send(new DatagramPacket(string.getBytes(), string.length(), address));
        LastTimeOut = System.currentTimeMillis();s

    }
}
