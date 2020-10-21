package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private ServerSocket serverSocket;
    private int port;

    MainServer(int port) throws IOException {//на вход приходит номер порта
        this.port = port;
        serverSocket = new ServerSocket(port);
        waiting();
    }

    public void waiting() throws IOException {
        System.out.println("SERVER UP");

        while (true) {
            Socket userSocket = serverSocket.accept();     //проверяет, на подключился ли к нам новый пользователь
            if(userSocket != null) {                //если есть новый пользователь, то мы создаём новую нитку,чтобы работать с этим пользователем
                System.out.println("CONNECT USER WITH IP:" + userSocket.getInetAddress());
                Thread thread;
                new Thread(new Server(userSocket)).start();
//                Thread thread = new Thread(String.valueOf(Server.class));           //Создаем тред для работы с отдельным юзером
            }
        }
    }
}
