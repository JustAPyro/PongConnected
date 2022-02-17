package com.pyredevelopment.pongonline;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PongServer extends Thread{

    // The main game object
    private final PongGame game;

    // Socket for network communication
    private DatagramSocket socket;

    // Creating a datagram packet for incoming data and create a byte buffer
    private final byte[] incomingBuffer = new byte[1];
    private final DatagramPacket incomingPacket;

    // Driver method
    public static void main(String[] args) {
        new PongServer().start();
    }

    public PongServer() {
        game = new PongGame();
        incomingPacket = new DatagramPacket(incomingBuffer, 1);
        try {
            socket = new DatagramSocket(9875);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {

                socket.receive(incomingPacket);
                InetAddress address = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                String clientID = address.toString() + ":" + port;

                System.out.println(clientID);
                String inputs = String.format("%07d", Integer.parseInt(Integer.toBinaryString(incomingPacket.getData()[0])));
                game.updateState(clientID, inputs);
                DatagramPacket packetOut = new DatagramPacket(game.getState(), 8, address, port);
                socket.send(packetOut);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
