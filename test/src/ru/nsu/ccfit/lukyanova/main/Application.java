package ru.nsu.ccfit.lukyanova.main;

import ru.nsu.ccfit.lukyanova.udpserver.Settings;
import ru.nsu.ccfit.lukyanova.udpserver.multicastSenderReceiver;
import ru.nsu.ccfit.lukyanova.udpserver.programsDataSingleton;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Set;

public class Application {

	public static void main(String[] argv) throws IOException {

		MulticastSocket socket;

		try {
			InetAddress group = InetAddress.getByName(Settings.getNet());
			socket = new MulticastSocket(Settings.getIncomingPort());
			socket.joinGroup(new InetSocketAddress(group, Settings.getIncomingPort()), NetworkInterface.getByName(""));
			//socket.joinGroup(group);
			socket.setSoTimeout(500);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}

		multicastSenderReceiver senderReceiver = new multicastSenderReceiver(socket);
		Thread thread = new Thread(senderReceiver);
		thread.start();

		for(;;) {
			System.out.println("Info:");
			Set<String> users = programsDataSingleton.getInstance().getData();
			for(Iterator<String> it = users.iterator(); it.hasNext();) {
				System.out.println(it.next());
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
