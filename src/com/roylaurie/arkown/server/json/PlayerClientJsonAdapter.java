/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.server.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.roylaurie.arkown.server.Client;
import com.roylaurie.arkown.server.PlayerClient;
import com.roylaurie.modeljson.JsonFactory;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public class PlayerClientJsonAdapter extends ClientJsonAdapter {
    public PlayerClientJsonAdapter(JsonFactory factory) {
        super(factory);
    }

    @Override
    public JsonElement serialize(Client src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = super.serialize(src, typeOfSrc, context).getAsJsonObject();
        PlayerClient playerClient = (PlayerClient)src;
        
        obj.addProperty("score", playerClient.getScore());
        
        return obj;
    }
    
    @Override
    public PlayerClient deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        PlayerClient playerClient = (PlayerClient)super.deserializeClient(new PlayerClient(), json, typeOfT, context);
        
        playerClient.setScore(obj.getAsJsonPrimitive("score").getAsInt());
        
        return playerClient;
    }
}
