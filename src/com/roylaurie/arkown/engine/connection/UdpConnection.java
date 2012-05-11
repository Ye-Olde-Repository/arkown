/**
 * 
 */
package com.roylaurie.arkown.engine.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.roylaurie.arkown.engine.Connection;


/**
 * @author rlaurie
 *
 */
public abstract class UdpConnection extends Connection {   
    private DatagramSocket mSocket = null;
    private InetSocketAddress mSocketAddress = null;
    private int mResponsePacketSize = 0;

    public static class UdpTransmission extends Transmission {
        protected UdpTransmission(Connection connection) {
            super(connection);
        };
        
        public void send(byte[] request) throws ConnectionException {
            if (!getConnection().isOpen()) {
                getConnection().open();
            }
            
            try {
                ((UdpConnection)getConnection()).getSocket().send(((UdpConnection)getConnection()).createRequestPacket(request));
            } catch (IOException e) {
                throw new ConnectionException(e.getMessage());
            }
        }
        
        public byte[] receive() throws ConnectionException {
            DatagramPacket packet = ((UdpConnection)getConnection()).createResponsePacket();
            byte[] response = null;
            
            try {
                ((UdpConnection)getConnection()).getSocket().receive(packet);
            } catch (IOException e) {
                throw new ConnectionException(e.getMessage());
            }
            
            response = packet.getData();            
            return response;
        }
    }
    
    protected DatagramPacket createRequestPacket(byte[] request) throws ConnectionException {
        DatagramPacket packet = null;
        
        try {
            packet = new DatagramPacket(request, request.length, getSocketAddress());
        } catch (SocketException e) {
            throw new ConnectionException(e.getMessage());
        }        
        
        return packet;
    }
    
    protected DatagramPacket createResponsePacket() {
        int packetSize = getResponsePacketSize();
        return new DatagramPacket(new byte[packetSize], packetSize);
    }    
    
    protected UdpConnection(String hostname, int port, int transmissionTimeoutMs, int responsePacketSizeBytes) {
        super(hostname, port, transmissionTimeoutMs);
        setResponsePacketSize(responsePacketSizeBytes);
        initializeTransmission();
    }
    
    private void initializeTransmission() {
        setTransmission(new UdpTransmission(this));
        return;
    }
    
    protected UdpTransmission getUdpTransmission() {
        return (UdpTransmission)getTransmission();
    }
    
    private InetSocketAddress getSocketAddress() throws ConnectionException {
        if (mSocketAddress == null) {
            try {
                mSocketAddress = new InetSocketAddress(InetAddress.getByName(getHostname()), getPort());
            } catch (UnknownHostException e) {
                throw new ConnectionException(e.getMessage());
            }            
        }

        return mSocketAddress;
    }
    
    protected DatagramSocket getSocket() {
        return mSocket;
    }
    
    protected int getResponsePacketSize() {
        return mResponsePacketSize;
    }
    
    protected void setResponsePacketSize(int packetSizeBytes) {
        mResponsePacketSize = packetSizeBytes;
    }
    
    /* 
     * 
     */
    @Override
    public void open() throws ConnectionException {
        if (isOpen()) {
            return;
        }
        
        try {
            mSocket = new DatagramSocket();
            mSocket.connect(getSocketAddress());
            mSocket.setSoTimeout(getTransmissionTimeout());
        } catch (SocketException e) {
            throw new ConnectionException(e.getMessage());
        }
        
        setOpen(true);
    }       
    
    /* (non-Javadoc)
     * @see com.roylaurie.arkown.server.query.Connection#close()
     */
    @Override
    public void close() {
        if (mSocket != null) {
            mSocket.close();
        }
        
        mSocket = null;
        mSocketAddress = null;
        
        setOpen(false);
    }    
}
