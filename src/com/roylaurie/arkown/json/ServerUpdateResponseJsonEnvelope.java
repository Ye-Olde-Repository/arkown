/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.json;

import com.roylaurie.arkown.server.Server;
import com.roylaurie.modeljson.JsonEnvelope;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;
import com.roylaurie.modeljson.exception.JsonException;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class ServerUpdateResponseJsonEnvelope extends JsonEnvelope {
    public static final String PROTOTYPE_RESULT = "result";
    public static final String PROTOTYPE_SERVER = "server";
    
    public static enum Result {
        OK,
        ERROR_CREATE_DUPLICATE,
        ERROR_PERMISSION,
        ERROR_NOT_FOUND,
        ERROR_QUERY,
        ERROR_UNKNOWN;
    }
    
    public ServerUpdateResponseJsonEnvelope() {
        super(ArkownJsonFactory.getInstance());

        addPrototype(new Prototype(
            PROTOTYPE_RESULT,
            ExclusionStrategyType.PUBLIC,
            Result.class
        ));
        
        addPrototype(new Prototype(
            PROTOTYPE_SERVER,
            ExclusionStrategyType.PUBLIC,
            Server.class
        ));        
        
        putObject(PROTOTYPE_RESULT, Result.ERROR_UNKNOWN);
        putObject(PROTOTYPE_SERVER, null);        
    }
    
    public ServerUpdateResponseJsonEnvelope(String json) throws JsonException {
        super(ArkownJsonFactory.getInstance());
        
        addPrototype(new Prototype(
            PROTOTYPE_RESULT,
            ExclusionStrategyType.PUBLIC,
            Result.class
        ));        
        
        addPrototype(new Prototype(
            PROTOTYPE_SERVER,
            ExclusionStrategyType.PUBLIC,
            Server.class
        ));
        
        fromJson(json);
    }
    
    /**
     * Retrieves server.
     *
     * @return Server
     */
    public Server getServer() {
        return (Server)getObject(PROTOTYPE_SERVER);
    }
    
    public void setServer(Server server) {
        putObject(PROTOTYPE_SERVER, server);
        setNeedsJsonRefresh();
    } 
    
    /**
     * Retrieves the API result.
     *
     * @return Result
     */
    public Result getResult() {
        return (Result)getObject(PROTOTYPE_RESULT);
    }
    
    public void setResult(Result result) {
        putObject(PROTOTYPE_RESULT, result);
        setNeedsJsonRefresh();
    }     
}
