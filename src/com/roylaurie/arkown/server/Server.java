/**
 * 
 */
package com.roylaurie.arkown.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonParseException;
import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.engine.Connection.CommandException;
import com.roylaurie.arkown.engine.Connection.ConnectionException;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.arkown.engine.Engine.Capability;
import com.roylaurie.arkown.engine.Engine.Product;
import com.roylaurie.arkown.engine.Query.QueryException;
import com.roylaurie.arkown.json.ServerListRequestJsonEnvelope.Host;
import com.roylaurie.arkown.server.callofduty7.CallOfDuty7Server;
import com.roylaurie.arkown.server.json.ServerJson;
import com.roylaurie.arkown.server.source.SourceServer;
import com.roylaurie.modeljson.exception.JsonApiVersionException;
import com.roylaurie.modeljson.exception.JsonConnectionException;
import com.roylaurie.modeljson.exception.JsonException;

/**
 * Live game server model.
 *
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 * 
 */
public abstract class Server implements Comparable<Server> {        
    private long mDatabaseId = 0;
    private long mApplicationDatabaseId = 0;
    private String mHostname = "";
	private String mHostAddress = null;
	private int mPort = 0;
	private Engine mEngine = null;
	private Enum<? extends Product> mProduct = null;
	private Credentials mCredentials = new Credentials();
	private String mName = "";
	private int mNumClients = 0;
	private int mMaxClients = 0;
	private int mResponseTimeMs = 999;
	private long mLastPullTime = 0;
	private ArrayList<Client> mClientList = new ArrayList<Client>();
	private boolean mQueryProxyAllowed = false;

    public static interface ServerMaps {
        public String getMapToken();
        public void setMapToken(String mapToken);
        public ArrayList<String> getAvailableMapTokens();
    };    
    
    public Server(Engine engine) {
        setEngine(engine);
    }
    
	/**
     * Explicit constructor.
     * 
	 * @param String hostname The server hostname to connect to.
	 * @param int port The server port to connect to.
	 */	
	public Server(String hostname, int port, Engine engine) {
		setHostname(hostname);
		setPort(port);
		setEngine(engine);
		
		return;
	}
	
	/**
	 * Implicit constructor. Engine needs to be set first.
	 *
	 * @param engine
	 */
	public Server() {
    }

	/**
     * Explicit constructor. Takes credentials.
     *
	 * @param String hostname The server hostname to connect to.
	 * @param int port The server port to connect to.
     * @param String rconPassowrd
	 */	
	public Server(String hostname, int port, Engine engine, Credentials credentials) {
		setHostname(hostname);
		setPort(port);
		setEngine(engine);
		setCredentials(credentials);
		
		return;
	}	
	
	public static Server factory(Engine engine) {
	    if (engine == null) {
	        throw new IllegalArgumentException("Invalid engine.");	        
	    }
	    
	    switch (engine.getType()) {
	    case SOURCE:
	        return new SourceServer();
	    case CALL_OF_DUTY_7:
            return new CallOfDuty7Server();
	    }
	    
	    throw new IllegalArgumentException("Invalid engine `" + engine.getName() + "`.");
	}
	
    @Override
    public boolean equals(Object rh) {
        if (rh == null || rh.getClass() != getClass()) {
            return false;
        } 
        
        return hashCode() == rh.hashCode();
    }
    
    @Override
    public int hashCode() {
        int code = 1;
        code = 7 * code + ( mEngine == null ? 0 : mEngine.getType().toString().hashCode() );
        code = 7 * code + ( mHostname == null ? 0 : mHostname.hashCode() );
        code = 7 * code + mPort;
        return code;        
    }

    @Override
    public int compareTo(Server rh) {
        return getTitle().compareToIgnoreCase(rh.getTitle());
    }    
    
    public void addClient(Client client) {
    	mClientList.add(client);
    	client.setServer(this);
    }
    
    public void setClients(List<? extends Client> clients) {
        mClientList.clear();
        if (clients == null) {
            return;
        }
        
        mClientList.addAll(clients);
        
        for (Client client : clients) {
            client.setServer(this);
        }
    }
    
	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return mHostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public final void setHostname(String hostname) {
		this.mHostname = hostname;
	}

	/**
	 * @return the port
	 */
	public final int getPort() {
		return mPort;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public final void setPort(int port) {
		this.mPort = port;
	}

	public final String getHostAddress() throws UnknownHostException {
	    if (mHostAddress != null) {
	        return mHostAddress;
	    }
	    
	    mHostAddress = InetAddress.getByName(getHostname()).getHostAddress();
	    return mHostAddress;
	}
	
	/**
	 * @return the rconPassword
	 */
	public final Credentials getCredentials() {
		return mCredentials;
	}

	/**
	 * @param rconPassword
	 *            the rconPassword to set
	 */
	public final void setCredentials(Credentials credentials) {
		mCredentials = credentials;
	}

	public final String getName() {
		return mName;
	}

	public final void setName(String name) {
		mName = name;
	}

	public int getNumClients() {
		if (mClientList.size() > 0) {
			return mClientList.size();
		}
		
		return mNumClients;
	}

	public void setNumClients(int numClients) {
		mNumClients = numClients;
	}

	public int getResponseTime() {
		return mResponseTimeMs;
	}

	public void setResponseTime(int responseTime) {
		mResponseTimeMs = responseTime;
	}

	public final ArrayList<Client> getClients() {
		return mClientList;
	}

	public final Engine getEngine() {
		return mEngine;
	}

	public final void setEngine(Engine engine) {
		mEngine = engine;
	}
	
	public final String getTitle() {
	    if (mName != "") {
	        return mName;
	    }
	    
	    return getHost() + getPort();
	}
	
	public final String getHost() {
	    return ( mHostname + ":" + mPort );
	}
	
	public HashMap<String, String> getHashMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("name", getName());
		map.put("title", getTitle());
		map.put("engine", getEngine().getName());
		map.put("product", ((Product)getProduct()).getName());
		map.put("numClients", Integer.toString(getNumClients()));
		map.put("maxClients", Integer.toString(getMaxClients()));
		map.put("clientRatio", Integer.toString(getNumClients()) + " / " + Integer.toString(getMaxClients()));
		map.put("hostname", getHostname());
		map.put("port", Integer.toString(getPort()));
		map.put("host", getHost());
		map.put("responseTimeMs", Long.toString(getResponseTime()));
		map.put("responseTime", Long.toString(getResponseTime()) + "ms");
		
		if (this instanceof ServerMaps) {
		    map.put("mapToken", ((ServerMaps)this).getMapToken());
		}
		
		return map;
	}

	/**
	 * @return the maxClients
	 */
	public final int getMaxClients() {
		return mMaxClients;
	}

	/**
	 * @param maxClients the maxClients to set
	 */
	public final void setMaxClients(int maxClients) {
		mMaxClients = maxClients;
	}

    /**
     * Retrieves the last time that this server called pull() in milliseconds.
     * Uses System.currentTimeMillis()
     * 
     * @return long
     */
    public final long getLastPullTime() {
        return mLastPullTime;
    }

    /**
     * Sets the the last pull time.
     * 
     */
    public final void setLastPullTime(long lastPullTimeMs) {
        mLastPullTime = lastPullTimeMs;
    }    
    
    /**
     * Marks the current time as the last pull time.
     * 
     */
    public final void markLastPullTime() {
        mLastPullTime = System.currentTimeMillis();
    }

    /**
     * @return the databaseId
     */
    public final long getDatabaseId() {
        return mDatabaseId;
    }

    /**
     * Sets the applicationDatabaseId.
     *
     * @param long applicationDatabaseId
     */
    public void setApplicationDatabaseId(long applicationDatabaseId) {
        mApplicationDatabaseId = applicationDatabaseId;
    }

    /**
     * Retrieves the applicationDatabaseId.
     *
     * @return long
     */
    public long getApplicationDatabaseId() {
        return mApplicationDatabaseId;
    }

    /**
     * @param databaseId the databaseId to set
     */
    public final void setDatabaseId(long databaseId) {
        mDatabaseId = databaseId;
    }

    /**
     *
     * @return String
     */
    public Enum<? extends Product> getProduct() {
        return mProduct;
    }

    /**
     * Retrieves the product.
     * 
     * @param Enum<?> product
     */
    public void setProduct(Enum<? extends Product> product) {
        mProduct = product;
    }

    /**
     * Determines whether commands are sent directly by the local application or proxied by the ArkOwn server.
     * 
     * @return boolean
     */
    public final boolean isQueryProxyAllowed() {
        return mQueryProxyAllowed;
    }

    /**
     *
     * @param boolean queryProxyAllowed
     */
    public final void setQueryProxyAllowed(boolean queryProxyAllowed) {
        mQueryProxyAllowed = queryProxyAllowed;
    }    

    /**
     * Determines whether this server must use the API server to update status or not.
     *
     * @return boolean
     */
    public final boolean needsProxyPull() {
        return ( !getEngine().hasCapability(Capability.QUERY) && !getCredentials().valid() );
    }
    
    /**
     * Performs a proxied pull via the Arkown API service.
     *
     * @throws ConnectionException
     * @throws QueryException
     */
    protected Server proxyPull() throws ConnectionException, QueryException {
        ServerJson json = new ServerJson();
        Server server = null;
        
        try {
            if (getDatabaseId() != 0) {
                server = json.read(getDatabaseId());
            } else {
                server = json.find(new Host(this));
            }
        } catch (JsonConnectionException e) {
            throw new ConnectionException(e);
        } catch (JsonParseException e) {
            throw new ConnectionException(e);
        } catch (JsonApiVersionException e) {
            throw new ConnectionException(e);
        } catch (JsonException e) {
            throw new QueryException(e);
        }
        
        setName(server.getName());
        setDatabaseId(server.getDatabaseId());
        setMaxClients(server.getMaxClients());
        server.setNumClients(server.getNumClients());
        setResponseTime(server.getResponseTime());
        setProduct(server.getProduct());
        
        if (this instanceof ServerMaps) {
            ((ServerMaps)this).setMapToken(((ServerMaps)server).getMapToken());
        }
        
        markLastPullTime();
        
        return server;
    }
    
    /**
     * Queries the server for as much information as possible and self-updates.
     *
     * @throws ConnectionException
     * @throws QueryException
     */
    public abstract void pull() throws ConnectionException, QueryException;
    
    /**
     * Sends an RCON command.
     *
     * @param String command
     * @return String
     * @throws ConnectionException
     * @throws CommandException
     */
    public abstract String sendCommand(String command) throws ConnectionException, CommandException;  
    
    /**
     * Attempts to connect to the server using the current credentials.
     * If the connection (and command, if needed) fail, the credentials are reset.
     *
     * @return boolean TRUE if valid, FALSE if not.
     */
    public abstract boolean validateCredentials();
}
