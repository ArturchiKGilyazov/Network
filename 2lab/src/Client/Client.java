package Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client {
    private static int BUFFER_SIZE = 1024 * 1024;
    private int port;
//    private int ip;
    private Socket socket;
    private String ip;
    private Charset charset = StandardCharsets.UTF_8;

    Client(String ip, int port) throws IOException {
        this.port = port;
        this.ip = ip;
        this.socket = new Socket(ip, port);
//        socket = new Socket();
//        socket.connect(ip);
    }

    void sendFile(String fileName) throws IOException {
        System.out.println("Send");
        File file = new File(fileName);
        if ((!file.exists()) || (!file.isFile()) || (!file.canRead())) {
            System.err.println("Bad input file");
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(file);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        BufferedReader readerFromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),charset));

        byte[] byteName = file.getName().getBytes();
        dataOutputStream.writeInt(byteName.length);
        dataOutputStream.write(byteName, 0 ,byteName.length);
        dataOutputStream.writeLong(file.length());
        writeFile(fileInputStream, dataOutputStream);
        socket.shutdownOutput();
        String str = readerFromServer.readLine();
        System.out.println(str);

    }

    private void writeFile(FileInputStream fileInputStream, OutputStream outputStream) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int read;
        while (true){
            read = fileInputStream.read(buf);
            if (read == -1){
                break;
            }
            outputStream.write(buf, 0 , read);
        }
        outputStream.flush();
    }

}
