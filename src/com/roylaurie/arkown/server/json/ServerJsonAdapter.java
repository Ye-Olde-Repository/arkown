/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.server.json;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.arkown.server.Client;
import com.roylaurie.arkown.server.PlayerClient;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.arkown.server.Server.ServerMaps;
import com.roylaurie.modeljson.JsonAdapter;
import com.roylaurie.modeljson.JsonFactory;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public class ServerJsonAdapter extends JsonAdapter<Server> {
    public ServerJsonAdapter(JsonFactory factory) {
        super(factory);
    }

    /**
     *
     * @param src
     * @param typeOfSrc
     * @param context
     * @return
     */
    @Override
    public JsonElement serialize(Server src, Type typeOfSrc,
            JsonSerializationContext context) {
        ExclusionStrategyType strategyType = getJsonFactory().getCurrentStrategyType();
        Gson gson = getJsonFactory().getCurrentGson();
        JsonObject obj = new JsonObject();

        obj.addProperty("id", src.getDatabaseId());
        obj.add("engine", gson.toJsonTree(src.getEngine()));
        obj.addProperty("product", src.getProduct().toString());
        obj.addProperty("hostname", src.getHostname());
        obj.addProperty("port", src.getPort());
        obj.addProperty("name", src.getName());
        obj.addProperty("maxClients", src.getMaxClients());
        obj.addProperty("responseTimeMs", src.getResponseTime());
        obj.addProperty("isQueryProxyAllowed", src.isQueryProxyAllowed());
        obj.add("clients", gson.toJsonTree(src.getClients()));
        
        if (src instanceof ServerMaps) {
            obj.addProperty("mapToken", ((ServerMaps)src).getMapToken());
        }        
        
        if (strategyType == ExclusionStrategyType.PRIVATE) {
            obj.add("credentials", gson.toJsonTree(src.getCredentials()));
        }

        return obj;
    }

    /**
     *
     * @param JsonElement json
     * @param Type typeOfT
     * @param JsonDeserializationContext context
     * @return Server
     * @throws JsonParseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public Server deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Gson gson = getJsonFactory().getCurrentGson();
        JsonObject jsonObject = json.getAsJsonObject();
        Engine engine = gson.fromJson(jsonObject.get("engine"), Engine.class);
        Server server = Server.factory(engine); 
        Type clientListType = null;
        
        //TODO: figure out a better way to do this
        switch (engine.getType()) {
        case SOURCE:
        case CALL_OF_DUTY_7:
            clientListType = new TypeToken<List<PlayerClient>>(){}.getType();
        }

        server.setDatabaseId(jsonObject.getAsJsonPrimitive("id").getAsLong());
        server.setProduct(engine.productValueOf(jsonObject.getAsJsonPrimitive("product").getAsString()));
        server.setHostname(jsonObject.getAsJsonPrimitive("hostname").getAsString());
        server.setPort(jsonObject.getAsJsonPrimitive("port").getAsInt());
        server.setName(jsonObject.getAsJsonPrimitive("name").getAsString());
        server.setMaxClients(jsonObject.getAsJsonPrimitive("maxClients").getAsInt());
        server.setResponseTime(jsonObject.getAsJsonPrimitive("responseTimeMs").getAsInt());
        server.setQueryProxyAllowed(jsonObject.getAsJsonPrimitive("isQueryProxyAllowed").getAsBoolean());
        server.setClients((List<? extends Client>)gson.fromJson(jsonObject.get("clients"), clientListType));

        if (server instanceof ServerMaps) {
            ((ServerMaps)server).setMapToken(jsonObject.getAsJsonPrimitive("mapToken").getAsString());
        }
        
        if (jsonObject.has("credentials")) {
            server.setCredentials(gson.fromJson(jsonObject.get("credentials"), Credentials.class));
        }
        
        return server;
    }
}
