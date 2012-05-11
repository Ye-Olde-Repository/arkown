/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.engine.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.modeljson.JsonAdapter;
import com.roylaurie.modeljson.JsonFactory;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class CredentialsJsonAdapter extends JsonAdapter<Credentials> {
    public CredentialsJsonAdapter(JsonFactory factory) {
        super(factory);
    }

    @Override
    public JsonElement serialize(Credentials src, Type typeOfSrc, JsonSerializationContext context) {
        ExclusionStrategyType strategyType = getJsonFactory().getCurrentStrategyType();
        JsonObject obj = new JsonObject();
        
        obj.addProperty("username", src.getUsername());
        
        if (strategyType == ExclusionStrategyType.PRIVATE) {
            obj.addProperty("password", src.getPassword());
        }
        
        return obj;
    }

    @Override
    public Credentials deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Credentials credentials = new Credentials();
        
        if (obj.has("username")) {
            credentials.setUsername(obj.get("username").getAsString());
        }
        
        if (obj.has("password")) {
            credentials.setPassword(obj.get("password").getAsString());
        }
        
        return credentials;
    }
}
