package com.pyredevelopment.pongonline;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PongServer extends Thread{

    // The main game object
    private PongGame game;

    // Socket for network communication
    private DatagramSocket socket;

    // Creating a datagram packet for incoming data and create a byte buffer
    private final byte[] incomingBuffer = new byte[1];
    private final DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, 1);;

    // Driver method
    public static void main(String[] args) {
        new PongServer().start();
    }

    @Override
    public void run() {
        try {

            // Initialize a new game
            game = new PongGame();

            // Create a new socket for network communication
            socket = new DatagramSocket(9875);

            // Receiving loop
            while (true) {

                // Recieve incoming data
                socket.receive(incomingPacket);

                // Collect the address and port of the packet
                InetAddress address = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                // Construct the client ID using address:port (Should be unique)
                String clientID = address.toString() + ":" + port;

                // Format the incoming packet
                String inputs = String.format("%07d", Integer.parseInt(Integer.toBinaryString(incomingPacket.getData()[0])));

                // Update the gamestate
                game.updateState(clientID, inputs);
                DatagramPacket packetOut = new DatagramPacket(game.getState(), 8, address, port);
                socket.send(packetOut);


            }
        }
        catch (Exception e) {

        }
    }

}
