package com.pyredevelopment.pongonline;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PongServer extends Thread{

    PongGame game;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    DatagramPacket incomingPacket;

    public static void main(String[] args) {
        new PongServer().start();
    }

    public PongServer() {
        game = new PongGame();
        incomingPacket = new DatagramPacket(buf, buf.length);
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
                DatagramPacket packetOut = new DatagramPacket(game.getState(), 4, address, port);
                socket.send(packetOut);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
