import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class App {
    private String message;
    private String IP;
    private int port;
    private SocketAddress address;
    private MulticastSocket socket = new MulticastSocket(port);
    private int timeout = 3000;
    private HashMap<String, Long> clone;


    App(String message_, String IP_, int port_) throws IOException {
        this.message = message_;
        this.IP = IP_;
        this.port = port_;
        address = new InetSocketAddress(IP, port);
        socket.setSoTimeout(timeout);
        socket.joinGroup(address, NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        clone.clear();
        findClone();

    }

    public void findClone(){
        while(){

        }
    }

}
