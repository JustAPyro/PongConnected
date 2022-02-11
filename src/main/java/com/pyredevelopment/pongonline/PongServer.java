package com.pyredevelopment.pongonline;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.BitSet;

public class PongServer extends Thread{

    PongGame game;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public static void main(String[] args) {
        new PongServer().start();
    }

    public PongServer() {
        game = new PongGame();
        try {
            socket = new DatagramSocket(4445);
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();


                String inputs = String.format("%07d", Integer.parseInt(Integer.toBinaryString(packet.getData()[0])));
                game.updateState(inputs);
                DatagramPacket packetOut = new DatagramPacket(game.getState(), 4, address, port);
                socket.send(packetOut);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
