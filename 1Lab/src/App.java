import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;

public class App {
        private MulticastSocket socket;
        private SocketAddress multicastAddr;
        private int TIMEOUT = 5000;
        private int UPDATE_TIMEOUT = 3000;
        private long lastSendtime;
        private long lastRecvtime;
//    private String message;
//    private String IP;
//    private int port;
//    private SocketAddress address;
//    private MulticastSocket socket = new MulticastSocket(port);
//    private int timeout = 3000;
//    private HashMap<String, Long> clone = new HashMap<String, Long>();
//    long LastTimeOut = 3000;
//    long secondTimeout = 2000;

    //List<Long> DIMA = new LinkedList<Long>();


    App(String IP_, int port_) throws IOException {
        //this.message = message_;
        //this.IP = IP_;
        //this.port = port_;
        //socket.setSoTimeout(timeout);
        //address = new InetSocketAddress(IP, port);

        //socket.joinGroup(this.address, NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        //clone.clear();
        //findClone();

        this.multicastAddr = new InetSocketAddress(IP_, port_);
        socket = new MulticastSocket(port_);
        NetworkInterface NF = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        socket.setSoTimeout(TIMEOUT);
        socket.joinGroup(this.multicastAddr, NF);
        run();
    }

    private void run() throws IOException {
        lastSendtime = System.currentTimeMillis();
        Map<String, Long> clone = new HashMap<String, Long>();
        LinkedList<String> abort = new LinkedList<>();

        while (true){
            if (System.currentTimeMillis() - lastSendtime > TIMEOUT){
                send("F");
            }

            var lip = recieve();
            if (lip != null){
                clone.put(lip, lastRecvtime);
            }

            for (Map.Entry<String, Long> entry : clone.entrySet()){
                if (System.currentTimeMillis() - entry.getValue() > UPDATE_TIMEOUT){
                    abort.add(entry.getKey());
                }
            }

            for (var value: abort){
                clone.remove(value);
            }

            abort.clear();
            System.out.println(clone.size());
//            for (Map.Entry<String, Long> entry: clone.entrySet()){
//                System.out.println(entry.getKey() + new Date(entry.getValue()));
//            }

        }

    }

    public void send(String message) throws IOException {
        byte[] messByte = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(messByte, messByte.length, multicastAddr);
        socket.send(sendPacket);
        lastSendtime = System.currentTimeMillis();
    }

    public String recieve(){
        byte[] messByte = new byte[255];
        DatagramPacket recvPacket = new DatagramPacket(messByte, messByte.length);
        try {
            socket.receive(recvPacket);
            lastRecvtime = System.currentTimeMillis();

        } catch (IOException e) {
            return null;
        }
        return recvPacket.getAddress().toString();
    }
}
/*
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
/*
            for (HashMap.Entry<String, Long> entry: clone.entrySet()){
                if ((System.currentTimeMillis() - entry.getValue()) > timeout){
                    DIMA.add(entry.getValue());

                }
            }

            System.out.println(clone.size());
            for (Long entyty: DIMA){
                clone.remove(entyty.longValue());
            }
            DIMA.clear();*/
/*
            for (Map.Entry entry: Map.of().entrySet()){
                if ((System.currentTimeMillis() - entry.getValue()) > timeout)
            }*/

            /*for (Object entry: clone) {
                if (System.currentTimeMillis() - i.value) clone.remove()
         */