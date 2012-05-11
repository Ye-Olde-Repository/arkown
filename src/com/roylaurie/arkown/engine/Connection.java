/**
 * 
 */
package com.roylaurie.arkown.engine;

/**
 * @author rlaurie
 *
 */
public abstract class Connection {  
    private static final int TIMEOUT_MS = 2000;
    
    private String mHostname = null;
    private int mPort = 0;
    private boolean mOpen = false;
    private int mTransmissionTimeoutMs = TIMEOUT_MS;
    private Transmission mTransmission = null;
    private Credentials mCredentials = new Credentials();
    
    private static interface Connectable {
        public String getHostname();
        public void setHostname(String hostname);
        public int getPort();
        public void setPort(int port);
        public void setTransmissionTimeout(int tranmssionTimeoutMs);
        public int getTransmissionTimeout();
    }
    
    public static final class Credentials {
        private String mUsername = null;
        private String mPassword = null;
        
        public Credentials() { }
        
        public Credentials(String username, String password) {
            setUsername(username);
            setPassword(password);
        }
        
        public Credentials(String password) {
            setPassword(password);
        }
        
        @Override
        public boolean equals(Object rh) {
            if (rh == null || !(rh instanceof Credentials)) {
                return false;
            }

            return (hashCode() == rh.hashCode());
        }
        
        @Override
        public int hashCode() {
            int hash = 1;
            hash = hash * 7 + ( mUsername == null ? 0 : mUsername.hashCode());
            hash = hash * 7 + ( mPassword == null ? 0 : mPassword.hashCode());
            return hash;
        }
        /**
         *
         * @return String
         */
        public String getUsername() {
            return mUsername;
        }
        /**
         *
         * @param String username
         */
        public void setUsername(String username) {
            mUsername = username;
        }
        /**
         *
         * @return String
         */
        public String getPassword() {
            return mPassword;
        }
        /**
         *
         * @param String password
         */
        public void setPassword(String password) {
            mPassword = password;
        }
        
        /**
         * Determines whether these credentials are valid or not.
         *
         * @return boolean
         */
        public boolean valid() {
            return ( mPassword != null && mPassword.length() > 0);
        }
    }
    
    public static interface QueryConnection extends Connectable {
        public Query createQuery(Enum<?> resource);
    }
    
    public static interface CommandConnection extends Connectable {
        public String sendCommand(String command) throws CommandException, ConnectionException;
    }    
    
    @SuppressWarnings("serial")
    public static final class ConnectionException extends Exception {
        public ConnectionException(String message) {
            super(message);
        }
        
        public ConnectionException(Throwable cause) {
            super(cause);
        }                  
    }
    
    @SuppressWarnings("serial")
    public static final class CommandException extends Exception {   
        public CommandException(String message) {
            super(message);
        }
        
        public CommandException(Throwable cause) {
            super(cause);
        }        
    }    
    
    public abstract static class Transmission {
        private Connection mConnection = null;
        
        protected Transmission(Connection connection) {
            mConnection = connection;
        };
        
        public final Connection getConnection() {
            return mConnection;
        }
    }    
    
    protected Connection(String hostname, int port, int transmissionTimeoutMs) {
        setHostname(hostname);
        setPort(port);
        setTransmissionTimeout(transmissionTimeoutMs);
    }
    
    protected void setTransmission(Transmission transmission) {
        mTransmission = transmission;
    }
    
    protected Transmission getTransmission() {
        return mTransmission;
    }      
    
    /**
     * @return the hostname
     */
    public String getHostname() {
        return mHostname;
    }
    
    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        mHostname = hostname;
    }
    
    /**
     * @return the port
     */
    public int getPort() {
        return mPort;
    }
    
    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        mPort = port;
    }
    
    /**
     * @return the connected
     */
    public boolean isOpen() {
        return mOpen;
    }

    /**
     * @param connected the connected to set
     */
    protected void setOpen(boolean opened) {
        mOpen = opened;
    }

    /**
     *
     * @return int
     */
    public int getTransmissionTimeout() {
        return mTransmissionTimeoutMs;
    }

    /**
     *
     * @param transmissionTimeoutMs transmissionTimeoutMs
     */
    public void setTransmissionTimeout(int transmissionTimeoutMs) {
        mTransmissionTimeoutMs = transmissionTimeoutMs;
    }          
    
    public abstract void open() throws ConnectionException;
    public abstract void close();

    /**
     *
     * @return Credentials
     */
    public final Credentials getCredentials() {
        return mCredentials;
    }
    
    protected final void setCredentials(Credentials credentials) {
        mCredentials = credentials;
    }

}
