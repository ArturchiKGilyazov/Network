package Client;

import java.net.Socket;

public class Client {
    private int port;
//    private int ip;
    private Socket socket;
    private String ip;

    Client(String ip, int port){
        this.port = port;
        socket = new Socket();
        socket.connect(ip);
    }
}
