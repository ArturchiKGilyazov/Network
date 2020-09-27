package ru.nsu.ccfit.lukyanova.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public class multicastSenderReceiver implements Runnable {

    private DatagramSocket socket = null;
    // генерация id
    private byte[] id;

    public multicastSenderReceiver(DatagramSocket socket) {

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        id = bb.array();

        System.out.println(uuid);
        if (null != socket) {
            this.socket = socket;
        } else {
            System.out.println("Error in socket object");
        }

    }

    private void send() {
        try {
            socket.send(new DatagramPacket(id, id.length,
                    InetAddress.getByName(Settings.getNet()), Settings.getIncomingPort()));
  //          System.out.println("Send: " + id.length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(id);
        programsDataSingleton.getInstance().cleanUp();
    }

    private void receive() {
        DatagramPacket recvPacket = new DatagramPacket(new byte[Settings.getDatagramSize()], Settings.getDatagramSize());

        try {
            socket.receive(recvPacket);
        } catch (IOException e) {
            return;
        }

        byte[] recvData = recvPacket.getData(); // достать id и добавить ip
        //System.out.println(recvData);
//        System.out.println("Received " + recvData.length);
        ByteBuffer bb = ByteBuffer.wrap(recvData);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        UUID id = new UUID(firstLong, secondLong);
        //System.out.println(id);
        programsDataSingleton.getInstance().addProgram(
                recvPacket.getAddress().toString().substring(1)+ ": " + id.toString());
    }

    public void run() {
        send();
        long lastSendTime = System.currentTimeMillis();

        for (;;) {

            if (System.currentTimeMillis() - lastSendTime >= Settings.getWaitTime()) {
                send();
                lastSendTime = System.currentTimeMillis();
            }

            receive();
        }
    }
}
