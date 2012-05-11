/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.server.json;

import java.util.List;

import com.roylaurie.arkown.json.ServerDeleteRequestJsonEnvelope;
import com.roylaurie.arkown.json.ServerDeleteResponseJsonEnvelope;
import com.roylaurie.arkown.json.ServerListRequestJsonEnvelope;
import com.roylaurie.arkown.json.ServerListResponseJsonEnvelope;
import com.roylaurie.arkown.json.ServerUpdateRequestJsonEnvelope;
import com.roylaurie.arkown.json.ServerUpdateResponseJsonEnvelope;
import com.roylaurie.arkown.json.ServerListRequestJsonEnvelope.Host;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.modeljson.JsonEnvelope;
import com.roylaurie.modeljson.JsonUriAction;
import com.roylaurie.modeljson.ModelJson;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;
import com.roylaurie.modeljson.exception.JsonDuplicateException;
import com.roylaurie.modeljson.exception.JsonException;
import com.roylaurie.modeljson.exception.JsonNotFoundException;
import com.roylaurie.modeljson.exception.JsonPermissionException;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class ServerJson extends ModelJson<Server> {
    public static final String BASE_URI = "http://api.arkown.roylaurie.com/server";

    @Override
    public Server read(long id) throws JsonException {
        ServerListRequestJsonEnvelope request = new ServerListRequestJsonEnvelope(); 
        String json = null;
        ServerListResponseJsonEnvelope response = null;
        
        request.addHost(new Host(id));
        json = request(HttpMethod.POST, BASE_URI + JsonUriAction.FIND, request);
        response = new ServerListResponseJsonEnvelope(json, ExclusionStrategyType.PUBLIC);
               
        if (response.getServerList().size() == 0) {
            throw new JsonNotFoundException();
        }
        
        return response.getServerList().get(0);
    }

    @Override
    public void write(Server model) throws JsonException {
        ServerUpdateRequestJsonEnvelope request = new ServerUpdateRequestJsonEnvelope();
        String json = null;
        ServerUpdateResponseJsonEnvelope response = null;
        
        request.setServer(model);
        json = request(HttpMethod.POST, BASE_URI + JsonUriAction.WRITE, request);
        response = new ServerUpdateResponseJsonEnvelope(json);
        
        switch (response.getResult()) {
        case OK:
            model.setDatabaseId(response.getServer().getDatabaseId());
            break;
            
        case ERROR_CREATE_DUPLICATE:
            throw new JsonDuplicateException(response.getServer());
            
        case ERROR_NOT_FOUND:
            throw new JsonNotFoundException();
            
        case ERROR_PERMISSION:
            throw new JsonPermissionException();
            
        default:
            throw new JsonException("Unable to store server.");
        }
        
        model.setDatabaseId(response.getServer().getDatabaseId());
    }

    @Override
    public void delete(Server model) throws JsonException {
        ServerDeleteRequestJsonEnvelope request = new ServerDeleteRequestJsonEnvelope();
        String json = null;
        ServerDeleteResponseJsonEnvelope response = null;
        
        request.setServer(model);
        json = request(HttpMethod.POST, BASE_URI + JsonUriAction.DELETE, request);
        response = new ServerDeleteResponseJsonEnvelope(json);
        
        switch (response.getResult()) {
        case OK:
            break;
        
        case ERROR_NOT_FOUND:
            throw new JsonNotFoundException();
            
        case ERROR_PERMISSION:
            throw new JsonPermissionException();    
            
        default:
            throw new JsonException();
        } 
    }

    @Override
    public List<Server> find(JsonEnvelope request) throws JsonException {
        String json = null;
        ServerListResponseJsonEnvelope response = null;

        json = request(HttpMethod.POST, BASE_URI + JsonUriAction.FIND, request);
        response = new ServerListResponseJsonEnvelope(json, ExclusionStrategyType.PUBLIC);
        
        return response.getServerList();
    }
    
    /**
     * Helper function to find all servers within a given list.
     *
     * @param serverList
     * @return
     * @throws JsonException 
     */
    public List<Server> find(List<Server> serverList) throws JsonException {
        ServerListRequestJsonEnvelope request = new ServerListRequestJsonEnvelope(); 
        
        for (Server server : serverList) {
            request.addHost(new Host(server));
        }
        
        return find(request);
    }
    
    /**
     * Helper function to find a server hostname/port
     *
     * @param serverList
     * @return
     * @throws JsonException 
     */
    public Server find(Host host) throws JsonException {
        ServerListRequestJsonEnvelope request = new ServerListRequestJsonEnvelope(); 
        List<Server> serverList = null;
        
        request.addHost(host);      
        serverList = find(request);
        
        if (serverList.size() == 0) {
            throw new JsonNotFoundException();
        }
        
        return serverList.get(0);
    }    
}
