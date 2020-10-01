package Server;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    Socket socket;                                      //Клиент

    Server(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        //Получили буффер, с которого можем считывать файл Клиента
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            File file = new File("");           //TODO Arthur rename path please
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
