package com.roylaurie.arkown.server;

import java.util.HashMap;

public abstract class Client implements Comparable<Client> {
    private long mApplicationDatabaseId = 0;
    private long mDatabaseId = 0;
	private String mName = "";
    private Server mServer = null;

	/**
	 * @param name
	 */
	public Client(String name) {
		setName(name);
		
		return;
	}
	
	public Client() {
	    
	}
	
    @Override
    public boolean equals(Object rh) {
        if (rh == null || rh.getClass() != getClass()) {
            return false;
        }
        
        return (mName == ((Client)rh).mName);
    }
    
    @Override
    public int hashCode() {
        return mName.hashCode();       
    }        

    @Override
    public int compareTo(Client rh) {
        return mName.compareToIgnoreCase(rh.mName);
    }       
    
    /**
     * Retrieves the databaseId.
     *
     * @return long
     */
    public final long getDatabaseId() {
        return mDatabaseId;
    }

    /**
     * Sets the databaseId.
     *
     * @param long databaseId
     */
    public final void setDatabaseId(long databaseId) {
        mDatabaseId = databaseId;
    }    
    
	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		mName = name;
	}

	public Server getServer() {
		return mServer;
	}

	public void setServer(Server server) {
		this.mServer = server;
	}
	
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        
        map.put("name", getName());
        
        return map;
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
}
