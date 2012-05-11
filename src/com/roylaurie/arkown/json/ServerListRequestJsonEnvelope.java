/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.modeljson.JsonAdapter;
import com.roylaurie.modeljson.JsonEnvelope;
import com.roylaurie.modeljson.JsonFactory;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;
import com.roylaurie.modeljson.exception.JsonException;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class ServerListRequestJsonEnvelope extends JsonEnvelope {
    public static final String PROTOTYPE_HOSTS = "hosts";

    public static class Host {
        private long mServerId = 0;
        private Engine mEngine = null;
        private String mHostname = null;
        private int mPort = 0;
        
        public static class HostJsonAdapter extends JsonAdapter<Host> {
            public HostJsonAdapter(JsonFactory factory) {
                super(factory);
            }

            @Override
            public JsonElement serialize(Host src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject obj = new JsonObject();
                Gson gson = getJsonFactory().getGson(ExclusionStrategyType.PUBLIC);
                
                obj.add("engine", gson.toJsonTree(src.getEngine()));
                obj.addProperty("serverId", src.getServerId());
                obj.addProperty("hostname", src.getHostname());
                
                if (src.getPort() != 0) {
                    obj.addProperty("port", src.getPort());
                }
                
                return obj;
            }

            @Override
            public Host deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                JsonObject obj = json.getAsJsonObject();
                Host host = new Host();
                
                if (obj.has("engine")) {
                    host.setEngine(getJsonFactory().getGson(ExclusionStrategyType.PUBLIC).fromJson(
                        obj.get("engine"),
                        Engine.class
                    ));
                }
                if (obj.has("serverId")) {
                    host.setServerId(obj.getAsJsonPrimitive("serverId").getAsLong());
                }
                if (obj.has("hostname")) {
                    host.setHostname(obj.getAsJsonPrimitive("hostname").getAsString());
                }
                if (obj.has("port")) {
                    host.setPort(obj.getAsJsonPrimitive("port").getAsInt());
                }
                
                return host;
            }
        }
        
        public Host(Engine engine, long serverId, String hostname, int port) {
            setEngine(engine);
            setServerId(serverId);
            setHostname(hostname);
            setPort(port);
        }              
        
        public Host(Engine engine, String hostname, int port) {
            setEngine(engine);
            setHostname(hostname);
            setPort(port);
        }
        
        public Host(long serverId) {
            setServerId(serverId);
        }         
        
        public Host(Server server) {
            setEngine(server.getEngine());
            setServerId(server.getDatabaseId());
            
            if (server.getHostname() != null) {
                setHostname(server.getHostname());
            }
            
            setPort(server.getPort());            
        }
        
        public Host() {
        }
        
        /**
         * Sets the engine.
         *
         * @param Engine engine
         */
        public void setEngine(Engine engine) {
            mEngine = engine;
        }

        /**
         * Retrieves the engine.
         *
         * @return Engine
         */
        public Engine getEngine() {
            return mEngine;
        }

        /**
         * Retrieves the hostname.
         *
         * @return String
         */
        public String getHostname() {
            return mHostname;
        }
        
        /**
         * Sets the hostname.
         *
         * @param String hostname
         */
        public void setHostname(String hostname) {
            mHostname = hostname;
        }
        
        /**
         * Retrieves the port.
         *
         * @return int
         */
        public int getPort() {
            return mPort;
        }
        
        /**
         * Sets the port.
         *
         * @param int port
         */
        public void setPort(int port) {
            mPort = port;
        }

        /**
         * Sets the serverId.
         *
         * @param long serverId
         */
        public void setServerId(long serverId) {
            mServerId = serverId;
        }

        /**
         * Retrieves the serverId.
         *
         * @return long
         */
        public long getServerId() {
            return mServerId;
        }
    }
    
    public ServerListRequestJsonEnvelope() {
        super(ArkownJsonFactory.getInstance());

        addPrototype(new Prototype(
            PROTOTYPE_HOSTS,
            ExclusionStrategyType.PUBLIC,
            new TypeToken<List<Host>>(){}.getType()
        ));
        
        putObject(PROTOTYPE_HOSTS, new ArrayList<Host>());        
    }
    
    public ServerListRequestJsonEnvelope(String json) throws JsonException {
        super(ArkownJsonFactory.getInstance());
        
        addPrototype(new Prototype(
            PROTOTYPE_HOSTS,
            ExclusionStrategyType.PUBLIC,
            new TypeToken<List<Host>>(){}.getType()
        ));
        
        fromJson(json);
    }
    
    @Override
    protected void configureGsonBuilder(GsonBuilder gsonBuilder) {
        super.configureGsonBuilder(gsonBuilder);
        gsonBuilder.registerTypeAdapter(Host.class, new Host.HostJsonAdapter(getJsonFactory()));        
    }    
    
    /**
     * Retrieves the immutable host list.
     *
     * @return ArrayList<Host>
     */
    @SuppressWarnings("unchecked")
    public List<Host> getHostList() {
        return Collections.unmodifiableList(((List<Host>)getObject(PROTOTYPE_HOSTS)));
    }
    
    @SuppressWarnings("unchecked")
    public void addHost(Host host) {
        ((List<Host>)getObject(PROTOTYPE_HOSTS)).add(host);
        setNeedsJsonRefresh();
    }
   
    @SuppressWarnings("unchecked")
    public void removeHost(Host host) {
        ((List<Host>)getObject(PROTOTYPE_HOSTS)).remove(host);
        setNeedsJsonRefresh();
    }    
}
