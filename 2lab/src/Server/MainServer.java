package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private ServerSocket serverSocket;
    private int port;

    MainServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        waiting();
    }

    public void waiting() throws IOException {
        while (true) {
            System.out.println("1");
            Socket userSocket = null;
            userSocket = serverSocket.accept();
            if(userSocket != null) {
                System.out.println("2");
                Thread thread = new Thread(String.valueOf(Server.class));           //Создаем тред для работы с отдельным юзером
            }
        }
    }
}
