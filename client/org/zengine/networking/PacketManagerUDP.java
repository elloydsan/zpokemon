package org.zengine.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 
 * @author Troy
 * 
 * NOTE: We will not use this unless we have to.
 * 
 * This Packet Manager uses the DatagramSocket to
 * send and receive packets over UDP.
 * 
 * More information can be found here:
 * http://docs.oracle.com/javase/1.4.2/docs/api/java/net/DatagramSocket.html
 *
 */
public class PacketManagerUDP {
	private Thread receiveThread;
	private DatagramSocket socket;
	
	private int port;
	private InetAddress ipAddress;
	
	private boolean running = false;
	
	/**
	 * This will create a new PacketManager
	 * which will listen to all incoming packets
	 * on the specified IP address and port.
	 * 
	 * @param ipAddress
	 */
	public PacketManagerUDP(String ipAddress, int port){
		this.port = port;
		
		try {
			this.ipAddress = InetAddress.getByName(ipAddress);
		}catch(UnknownHostException e1) {
			e1.printStackTrace();
			return;
		}
		
		try {
			socket = new DatagramSocket();
			socket.connect(this.ipAddress, port);
		}catch(SocketException e) {
			e.printStackTrace();
			return;
		}
		
		receiveThread = new Thread() {
			@Override
			public void run(){
				running = true;
				listen();
			}
		};
		receiveThread.start();
	}
	
	/**
	 * This method will keep on listening
	 * for incoming packet's until the thread
	 * has been stopped.
	 */
	public void listen(){
		while(running){
			try{
		        //receive server packet
		        byte[] buffer = new byte[256];
		        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		        socket.receive(packet);
				
				//translate packet
		        String received = new String(packet.getData(), 0, packet.getLength());
		        // TODO pass this packet on.
		        System.out.println(received);
		        
			}catch(IOException e){
				e.printStackTrace();
				System.out.println("Failed to receive packet from the server, closing connection.");
				running = false;
				socket.disconnect();
				socket.close();
			}catch(Exception e){
				e.printStackTrace();
				running = false;
				socket.disconnect();
				socket.close();
			}
		}
	}
	
	/**
	 * Method to send a packet to the server.
	 * 
	 * @param packetString
	 */
	public void sendPacket(String packetString){
		try{
			byte[] buf = packetString.getBytes();
	        DatagramPacket packet = new DatagramPacket(buf, buf.length, ipAddress, port);
	        socket.send(packet);
		}catch (IOException e){
			e.printStackTrace();
			System.out.println("Failed to send packet to server.");
		}
	}
	
	/**
	 * Close the connection.
	 */
	public void disconnect(){
		while(!receiveThread.isInterrupted())
			receiveThread.interrupt();
	}

}