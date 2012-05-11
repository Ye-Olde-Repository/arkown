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
import com.roylaurie.modeljson.JsonAdapter;
import com.roylaurie.modeljson.JsonFactory;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public abstract class ClientJsonAdapter extends JsonAdapter<Client> {
    public ClientJsonAdapter(JsonFactory factory) {
        super(factory);
    }

    @Override
    public JsonElement serialize(Client src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        
        obj.addProperty("name", src.getName());
        
        return obj;
    }

    protected Client deserializeClient(Client client, JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        
        client.setName(obj.getAsJsonPrimitive("name").getAsString());
        
        return client;
    }  
}
