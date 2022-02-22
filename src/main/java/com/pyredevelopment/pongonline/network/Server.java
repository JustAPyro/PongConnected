package com.pyredevelopment.pongonline.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Server extends Thread{

    private static final Logger logger = LogManager.getLogger(Server.class);

    private final int port;
    private DatagramSocket socket;

    private Set<String> clients;

    public Server(int port) {
        this.port = port;
    }

    public void push(byte[] packet) {

        try {
            for (String clientAddress : clients) {
                System.out.println("Pushing!");
                String[] information = clientAddress.split(":");
                InetAddress address = InetAddress.getByName(information[0].replace("/", ""));
                int port = Integer.parseInt(information[1]);
                DatagramPacket packetout = new DatagramPacket(packet, 8,address, port);
                socket.send(packetout);

            }
        }
        catch (Exception e) {
            System.out.println("Couldn't find address");
        }

    }

    @Override
    public void run() {
        try {

            // Creating a datagram packet for incoming data and create a byte buffer
            final byte[] incomingBuffer = new byte[1];
            final DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, 1);

            socket = new DatagramSocket(port);

            // Synchronize hashset
            clients = Collections.synchronizedSet(new HashSet<>());

            while (true) {


                // Recieve incoming data
                socket.receive(incomingPacket);

                // Collect the address and port of the packet
                InetAddress address = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                // Construct the client ID using address:port (Should be unique)
                String clientID = address.toString() + ":" + port;

                clients.add(clientID);
                System.out.println("New client! At " + clientID);

            }

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }




}
