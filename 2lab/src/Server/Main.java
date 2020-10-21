package Server;

import java.io.IOException;

public class Main {
    //на вход подаётся порт
    public static void main(String[] args) throws IOException {
        MainServer mainServer = new MainServer(Integer.parseInt(args[0]));
    }
}
