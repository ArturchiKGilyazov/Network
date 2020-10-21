package Server;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Server implements Runnable {
    final public Socket socket;                                      //Клиент
    private Charset charset = StandardCharsets.UTF_8;               //Клиент передаёт серверу имя файла в кодировке UTF-8
    private int TIMEOUT = 3000;
    private int BUFFER_SIZE = 1024*1024;
    private int MAXBYTE = 1024*1024;

    Server(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(TIMEOUT * 10);
    }

    //проверка правильности директория
    private boolean checkDirecktory(File dir){
        if (!dir.exists()){
            return dir.mkdir();
        }
        return true;
    }

    private boolean readFile(File file, long fileSize, InputStream socketInputStream) throws
            IOException {
        System.out.println("readfile");
        try (FileOutputStream writerToFile = new FileOutputStream(file)) {
            long readySize = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            long period_read = 0;
            long startTime = System.currentTimeMillis();
            long time = startTime;
            long receiveTime = startTime;

            while (readySize < fileSize) {
                //System.out.println("1");      //For debug
                int actuallyRead = socketInputStream.readNBytes(buffer, 0, buffer.length);
                if (actuallyRead == 0) {
                    break;
                }
                period_read += actuallyRead;
                readySize += actuallyRead;

                writerToFile.write(buffer, 0, actuallyRead);
                receiveTime = System.currentTimeMillis();

                //В процессе приёма данных от клиента, сервер должен раз в 3 секунды выводить в консоль мгновенную
                //скорость приёма и среднюю скорость за сеанс.
                if (receiveTime - time > TIMEOUT) {

                    //вывод данных(скорости)
                    System.out.println(file);
                    System.out.println("current speed: " + String.format("%.2f",
                            period_read / MAXBYTE / ((double) (receiveTime - time) / 1000)) + " mb/s");
                    System.out.println("average speed: " + String.format("%.2f",
                            (double) (readySize) / MAXBYTE / ((double) (receiveTime - startTime) / 1000)) + " mb/s");

                    period_read = 0;
                    time = System.currentTimeMillis();
                }



            }

            System.out.println(file);
            System.out.println("current speed: " + String.format("%.2f",
                    period_read / MAXBYTE / ((double) (receiveTime - time) / 1000)) + " mb/s");
            System.out.println("average speed: " + String.format("%.2f",
                    (double) (readySize) / MAXBYTE / ((double) (receiveTime - startTime) / 1000)) + " mb/s");
            return true;
        }
    }


    private String checkUploadedFile(File file, long fileSize, boolean read) {
        if (file.length() == fileSize) {
            return " loaded";
        } else if (!read) {
            return null;
        } else {
            return " incorrect size ";
        }
    }

    @Override
    public void run() {
        //Получили буффер, с которого можем считывать файл Клиента
        try (this.socket) {
            InputStream socketInputStream = this.socket.getInputStream();
            DataInputStream inputFromClient = new DataInputStream(socketInputStream);
            BufferedWriter writerToClient = new BufferedWriter(new OutputStreamWriter( this.socket.getOutputStream(), charset));
            File dir = new File("./upload/");               //TODO Arthur rename directory
            if (!this.checkDirecktory(dir)) {
                writerToClient.flush();
                return;
            }

            int nameLength = inputFromClient.readInt();

            byte[] nameBuffer = new byte[nameLength];
            int actualNameLength = inputFromClient.readNBytes(nameBuffer, 0, nameLength);
            String fileName = new String(nameBuffer, 0, actualNameLength, charset);

            if (fileName.contains(File.separator) || fileName.contains("/") || fileName.contains("\\")) {
                writerToClient.flush();
                return;
            }
            long fileSize = inputFromClient.readLong();
            System.out.println("got file_name: " + fileName + "\n" + "got file_Size: " + fileSize);
            File file = new File(dir.getAbsoluteFile() + File.separator + fileName);
            boolean read = this.readFile(file, fileSize, socketInputStream);
            String answer = this.checkUploadedFile(file, fileSize, read);

            writerToClient.write("server: " + fileName + answer + "\n");
            writerToClient.flush();
        } catch (IOException e) {
            System.err.println("connection: " + e.getMessage());
        }

    }
}
