/**
 * 
 */
package com.roylaurie.arkown.engine.callofduty7;

import com.roylaurie.arkown.engine.connection.UdpConnection;
import com.roylaurie.arkown.engine.Connection.CommandConnection;


/**
 * @author rlaurie
 *
 */
public final class CallOfDuty7CommandConnection extends UdpConnection implements CommandConnection {   
    private static final int PACKET_SIZE_BYTES = 1400;
    private static final int SOCKET_TIMEOUT_MS = 2000;
    
    protected CallOfDuty7CommandConnection(String hostname, int port, Credentials credentials) {
        super(hostname, port, SOCKET_TIMEOUT_MS, PACKET_SIZE_BYTES);
        setCredentials(credentials);
    }

    @Override
    public String sendCommand(String command) throws CommandException,ConnectionException {
        byte[] payload = (getCredentials().getPassword() + ' ' + command).getBytes();
        int requestLength = payload.length + 6; // payload + prefix + suffix
        byte[] requestBytes = new byte[requestLength];
        UdpTransmission transmission = getUdpTransmission();
        byte[] response = null;
        StringBuffer buffer = null;
        
        // prefix
        requestBytes[0] = (byte)0xFF;
        requestBytes[1] = (byte)0xFF;
        requestBytes[2] = (byte)0xFF;
        requestBytes[3] = (byte)0xFF;
        requestBytes[4] = (byte)0x00;
        
        System.arraycopy(payload, 0, requestBytes, 5, payload.length);
        requestBytes[requestLength - 1] = (byte)0x00;
        
        transmission.send(requestBytes);
        response = transmission.receive();
        
        buffer = new StringBuffer(response.length);
        for (int i = 0; i < response.length && response[i] != 0; ++i) {
            buffer.append((char)(response[i] & 0xFF));
        }
        
        return buffer.toString();
    }
}
