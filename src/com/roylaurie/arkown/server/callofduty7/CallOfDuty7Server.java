/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.server.callofduty7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.roylaurie.arkown.engine.Connection.CommandException;
import com.roylaurie.arkown.engine.Connection.ConnectionException;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.arkown.engine.Query.QueryException;
import com.roylaurie.arkown.engine.callofduty7.CallOfDuty7CommandConnection;
import com.roylaurie.arkown.engine.callofduty7.CallOfDuty7Engine;
import com.roylaurie.arkown.engine.callofduty7.CallOfDuty7Engine.CallOfDuty7Product;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.arkown.server.Server.ServerMaps;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class CallOfDuty7Server extends Server implements ServerMaps {
    private String mMapToken = null;
    private ArrayList<String> mMapTokenList = null;
    
    public CallOfDuty7Server() {
        super(CallOfDuty7Engine.getInstance());
        setProduct(CallOfDuty7Product.BLACK_OPS);
    }
    
    public CallOfDuty7Server(String hostname, int port, Credentials credentials) {
        super(hostname, port, CallOfDuty7Engine.getInstance(), credentials);
        setProduct(CallOfDuty7Product.BLACK_OPS);
    }    

    /**
     *
     * @throws ConnectionException
     * @throws QueryException
     * @throws  
     */
    @Override
    public void pull() throws ConnectionException, QueryException {
        if (getCredentials().getPassword() == null || getCredentials().getPassword().length() == 0) {
            if (!isQueryProxyAllowed()) {
                throw new QueryException("RCON password must be set or server registered for proxied queries.");
            }
            
            proxyPull();
        } else {
            commandPull();
        }
        
        return;
    }
    
    private void commandPull() throws ConnectionException, QueryException {
        CallOfDuty7CommandConnection connection = (CallOfDuty7CommandConnection)getEngine().factoryCommandConnection(
            getHostname(),
            getPort(),
            getCredentials()
        );
        
        String response = null;
        String key = null;
        String value = null;
        String[] lines = null;
        long timestamp = 0;
        int padding = 0;
        HashMap<String, String> rules = new HashMap<String, String>();
        
        setResponseTime(999);
        
        try {
            timestamp = System.currentTimeMillis();
            response = connection.sendCommand("serverinfo");
            setResponseTime((int)(System.currentTimeMillis() - timestamp));
            markLastPullTime();
        } catch (CommandException e) {
            throw new QueryException(e.getMessage());
        }
        
        lines = response.substring(33).split("\n");
        padding = lines[0].lastIndexOf(' ') + 1;
        for (String line : response.substring(33).split("\n")) {
            if (line.startsWith("sv_disableClientConsole")) { // bug in their formatting, doesn't pad correctly
                key = line.substring(0, 23);
                value = line.substring(23);
                rules.put(key, value);
                continue;
            }
            
            key = line.substring(0, padding).trim();
            value = line.substring(padding).trim();
            rules.put(key, value);
        }

        setMaxClients(Integer.parseInt(rules.get("com_maxclients")) - 1);
        setMapToken(rules.get("mapname"));
        setName(rules.get("sv_hostname"));
    }

    /**
     *
     * @param command
     * @return
     * @throws ConnectionException
     * @throws CommandException
     */
    @Override
    public String sendCommand(String command) throws ConnectionException, CommandException {
        CallOfDuty7CommandConnection connection = (CallOfDuty7CommandConnection)getEngine().factoryCommandConnection(
            getHostname(),
            getPort(),
            getCredentials()
        );
            
        String response = connection.sendCommand("serverinfo");
        return response;
    }

    @Override
    public ArrayList<String> getAvailableMapTokens() {
        if (mMapTokenList != null) {
            return mMapTokenList;
        }
        
        mMapTokenList = new ArrayList<String>();
        String response = null;
        
        try {
            response = sendCommand("maps *");
        } catch (Exception e) {
            return mMapTokenList;
        }
        
        String[] lines = response.split("\n");
        String token = null;
        
        for (int i = 1; i < lines.length; ++i) {
            token = lines[i].trim();
            token = token.substring(token.lastIndexOf(' ') + 1, token.length() - 4); // subtract .bsp
            mMapTokenList.add(token);
        }
        
        Collections.sort(mMapTokenList);
        
        return mMapTokenList;
    }
    
    public String getMapToken() {
        return mMapToken;
    }

    public void setMapToken(String mapToken) {
        mMapToken = mapToken;
    }

    @Override
    public boolean validateCredentials() {
        try {
            sendCommand("serverinfo");
        } catch (ConnectionException e) {
            getCredentials().setPassword(null);
            return false;
        } catch (CommandException e) {
            getCredentials().setPassword(null);
            return false;
        }
        
        return true;
    }
}
