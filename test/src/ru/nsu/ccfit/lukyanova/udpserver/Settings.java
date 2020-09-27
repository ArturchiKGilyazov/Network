package ru.nsu.ccfit.lukyanova.udpserver;

/**
 *
 * @author lewbor
 */
public class Settings {

    private static final int incomingPort = 5055;
    private static final int datagramSize = 16;
    // broadcast address
    private static final String net = "228.5.6.7";

    //delay time to dead message
    private static final int waitTime = 3000;

    public static String getNet() {
        return net;
    }

    public static int getIncomingPort() {
        return incomingPort;
    }

    public static int getDatagramSize() {
        return datagramSize;
    }

    public static int getWaitTime() {
        return waitTime;
    }

    



}
