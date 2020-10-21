package Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Client client = new Client(args[0], Integer.parseInt(args[1]));
    if (args.length == 3){
        String Ip = args[0];
        int port = Integer.parseInt(args[1]);
        String namePath = args[2];
        Client client = new Client(Ip, port);
        client.sendFile(namePath);
    } else
        System.out.println("False parametr");
    }
}
