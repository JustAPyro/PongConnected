package com.pyredevelopment.pongonline.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {

    private InetAddress ipAddress;
    private int portNumber;

    public Client(String ipAddress, int portNumber) {

        this.ipAddress = parseAddress(ipAddress);
        this.portNumber = portNumber;

    }

    private InetAddress parseAddress(String ipAddress) {
        try {
            if (ipAddress.toLowerCase().contains("localhost"))
                return InetAddress.getLocalHost();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Couldn't find IP Address");

    }
}
