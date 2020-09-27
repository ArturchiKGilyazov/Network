import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class App {
        private MulticastSocket socket;
        private SocketAddress multicastAddr;
        private int TIMEOUT = 2000;
        private int UPDATE_TIMEOUT = 5000;
        private long lastSendtime;
        private long lastRecvtime;

        private byte[] id;
        //private UUID uid;

    public byte[] getUUID() {

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        id = bb.array();

        return  id;
    }

    App(String IP_, int port_) throws IOException {

        this.multicastAddr = new InetSocketAddress(IP_, port_);
        socket = new MulticastSocket(port_);
        //uid = UUID.randomUUID();
        id = getUUID();
        //System.out.println();
        NetworkInterface NF = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        socket.setSoTimeout(TIMEOUT);
        socket.joinGroup(this.multicastAddr, NF);
        run();
    }

    private void run() throws IOException {
        lastSendtime = System.currentTimeMillis();
        Map<String, Long> clone = new HashMap<String, Long>();
        LinkedList<String> abort = new LinkedList<>();
        //System.out.println(id);

        while (true){
            if (System.currentTimeMillis() - lastSendtime > TIMEOUT){
                //System.out.println(id);
                send(id);
            }

            var lip = receive();
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
            for (Map.Entry<String, Long> entry: clone.entrySet()){
                System.out.println(entry.getKey());
            }

        }

    }

    public void send(byte[] uuid) throws IOException {
        byte[] messByte = uuid;
        //System.out.println(uuid);
        DatagramPacket sendPacket = new DatagramPacket(messByte, messByte.length, multicastAddr);
        socket.send(sendPacket);
        lastSendtime = System.currentTimeMillis();
    }

    public String receive() {
        byte[] messByte = new byte[255];
        DatagramPacket recvPacket = new DatagramPacket(messByte, messByte.length);
        try {
            socket.receive(recvPacket);
            lastRecvtime = System.currentTimeMillis();
        } catch (IOException e) {
            return null;
        }

        byte[] recvData = recvPacket.getData();
        //System.out.println(recvData);
        ByteBuffer bb = ByteBuffer.wrap(recvData);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        UUID uuid = new UUID(firstLong, secondLong);

        //System.out.println(uuid);

        String retRecv = "ip: " + recvPacket.getAddress().toString() + "\nid: " + uuid + "\n";// re
        return retRecv;

    }
}