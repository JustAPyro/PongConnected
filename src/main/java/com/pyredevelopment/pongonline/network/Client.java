package com.pyredevelopment.pongonline.network;

import java.io.IOException;
import java.net.*;

public class Client extends Thread {

    private InetAddress ipAddress;
    private int portNumber;

    private DatagramSocket socket;

    private byte[] receivedData;

    public Client(String ipAddress, int portNumber) {
        try {

            this.ipAddress = parseAddress(ipAddress);
            this.portNumber = portNumber;
            socket = new DatagramSocket();

        }
        catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public boolean hasData() {
        return receivedData != null;
    }

    public byte[] getReceivedData() {
        return receivedData;
    }

    public void push(byte[] packet) {
        try {
            DatagramPacket wrappedPacket = new DatagramPacket(packet, packet.length, ipAddress, portNumber);
            socket.send(wrappedPacket);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            final byte[] incomingBuffer = new byte[8];
            final DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, 8);

            while (true) {
                socket.receive(incomingPacket);
                receivedData = incomingPacket.getData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InetAddress parseAddress(String ipAddress) throws UnknownHostException {

            if (ipAddress.toLowerCase().contains("localhost"))
                return InetAddress.getLocalHost();


        throw new UnknownHostException("Failed to parse Host in Client class");

    }
}
