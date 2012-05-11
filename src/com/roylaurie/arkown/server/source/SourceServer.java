package com.roylaurie.arkown.server.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.roylaurie.arkown.engine.Connection.CommandException;
import com.roylaurie.arkown.engine.Connection.ConnectionException;
import com.roylaurie.arkown.engine.Query.QueryException;
import com.roylaurie.arkown.engine.source.SourceEngine;
import com.roylaurie.arkown.engine.source.SourceQuery;
import com.roylaurie.arkown.engine.source.SourceQueryConnection;
import com.roylaurie.arkown.engine.source.SourceEngine.SourceProduct;
import com.roylaurie.arkown.engine.source.SourceQuery.InfoQueryResult;
import com.roylaurie.arkown.engine.source.SourceQuery.PlayersQueryResult;
import com.roylaurie.arkown.engine.source.SourceQuery.SourceQueryResource;
import com.roylaurie.arkown.engine.source.SourceQuery.PlayersQueryResult.PlayerDetails;
import com.roylaurie.arkown.server.PlayerClient;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.arkown.server.Server.ServerMaps;

public final class SourceServer extends Server implements ServerMaps {
    private String mMapToken = null;
    private ArrayList<String> mMapTokenList = null;

    public SourceServer() {
        super(SourceEngine.getInstance());
    }
    
    public SourceServer(String hostname, int port) {
        super(hostname, port, SourceEngine.getInstance());
    }
    
    /**
    *
    * @return String
    */
    @Override
   public SourceProduct getProduct() {
       return (SourceProduct)super.getProduct();
   }
    
    @Override
    public void pull() throws ConnectionException, QueryException {
        SourceQueryConnection connection = (SourceQueryConnection)getEngine().factoryQueryConnection(
            getHostname(),
            getPort()
        );
        
        setResponseTime(999);
        
        connection.open();
        
        try {
            long timestamp = System.currentTimeMillis();
            InfoQueryResult infoResult = (InfoQueryResult)connection.createQuery(SourceQueryResource.INFO).query();
            setResponseTime((int)(System.currentTimeMillis() - timestamp));
            
            setName(infoResult.getName());
            setMaxClients(infoResult.getMaxPlayers());
            setNumClients(infoResult.getNumPlayers());
            setMapToken(infoResult.getMapToken());

            setProduct(((SourceEngine)getEngine()).parseProductFromApplicationId(infoResult.getApplicationId()));
            
            pullPlayers(connection);
        } catch (ConnectionException e) {
            connection.close();
            throw e;
        } catch (QueryException e) {
            connection.close();
            throw e;
        }
        
        connection.close();
        markLastPullTime();
    }
    
    private void pullPlayers(SourceQueryConnection connection) throws ConnectionException, QueryException {
        SourceQuery query = (SourceQuery)connection.createQuery(SourceQueryResource.PLAYERS);        
        PlayersQueryResult playersResult = (PlayersQueryResult)query.query();
        PlayerClient player = null;
        
        setClients(null);
        
        for (PlayerDetails playerDetails : playersResult.getPlayers()) {
            if (playerDetails.getName() == null || playerDetails.getName().length() < 1) {
                continue;
            }
            
            player = new PlayerClient(playerDetails.getName());
            player.setScore((int)playerDetails.getKills());
            addClient(player);
        }
    }

    @Override
    public String sendCommand(String command) throws ConnectionException, CommandException {
        return command;
/*        SourceQueryConnection connection = (SourceCommandConnection)getEngine().factoryCommandConnection(
            getHostname(),
            getPort(),
            getCredentials()
        );
        
        return connection.sendCommand(command);
*/        
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
    
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> map = super.getHashMap();
        map.put("mapToken", getMapToken());
        return map;
    }

    @Override
    public boolean validateCredentials() {
        return true;
    }
}
