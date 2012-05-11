/**
 * Copyright (C) 2010 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.engine.source;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;


import com.roylaurie.arkown.engine.Query;
import com.roylaurie.arkown.engine.Connection.ConnectionException;
import com.roylaurie.arkown.engine.Connection.Transmission;
import com.roylaurie.arkown.engine.connection.UdpConnection.UdpTransmission;
import com.roylaurie.arkown.engine.source.SourceQuery.PlayersQueryResult.PlayerDetails;

/**
 * @author rlaurie
 *
 */
public final class SourceQuery extends Query {
    private static final String CHARSET_LATIN = "ISO-8859-1";
    private static final String CHARSET_UTF = "UTF-8";
    
    public static enum SourceQueryResource {
        //TODO: PING,
        INFO,
        PLAYERS,
        RULES,
        CHALLENGE;
    }
    
    public static enum OperatingSystem {
        LINUX,
        WINDOWS;
    }       
    
    public static enum ListenType {
        LISTEN,
        DEDICATED,
        SOURCE_TV;
    }

    public static final class ChallengeQueryResult extends QueryResult {
        private byte[] mChallengeBytes = null;

        /**
         *
         * @return byte[]
         */
        public byte[] getChallengeBytes() {
            return mChallengeBytes;
        }

        /**
         *
         * @param challengeBytes challengeBytes
         */
        protected void setChallengeBytes(byte[] challengeBytes) {
            mChallengeBytes = challengeBytes;
        }
        
    }
    
    public static final class PingQueryResult extends QueryResult {
        private String mPing = null;

        /**
         * @return the ping
         */
        public String getPing() {
            return mPing;
        }

        /**
         * @param ping the ping to set
         */
        protected void setPing(String ping) {
            mPing = ping;
        }
    }
    
    public static final class InfoQueryResult extends QueryResult {
        private int mProtocolVersion = 0;
        private String mName = null;
        private String mMapToken = null;
        private String mGameDirectory = null;
        private String mGameName = null;
        private int mApplicationId = 0;
        private int mNumPlayers = 0;
        private int mMaxPlayers = 0;
        private int mNumBots = 0;
        private ListenType mListenType = null;
        private OperatingSystem mOperatingSystem = null;
        private boolean mIsPrivate = false;
        private boolean mSecure = false;
        private String mGameVersion = null;
        private int mPort = 0;
        private String mSteamId = null;
        private String mTag = null;
        private int mSpectatorPort = 0;
        private String mSpectatorServerName = null;
        
        /**
         * @return the protocolVersion
         */
        public int getProtocolVersion() {
            return mProtocolVersion;
        }
        
        /**
         * @param protocolVersion the protocolVersion to set
         */
        protected void setProtocolVersion(int protocolVersion) {
            mProtocolVersion = protocolVersion;
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
        protected void setName(String name) {
            mName = name;
        }
        
        /**
         * @return the mapToken
         */
        public String getMapToken() {
            return mMapToken;
        }
        
        /**
         * @param mapToken the mapToken to set
         */
        protected void setMapToken(String mapToken) {
            mMapToken = mapToken;
        }
        
        /**
         * @return the gameDirectory
         */
        public String getGameDirectory() {
            return mGameDirectory;
        }
        
        /**
         * @param gameDirectory the gameDirectory to set
         */
        protected void setGameDirectory(String gameDirectory) {
            mGameDirectory = gameDirectory;
        }
        
        /**
         * @return the gameName
         */
        public String getGameName() {
            return mGameName;
        }
        
        /**
         * @param gameName the gameName to set
         */
        protected void setGameName(String gameName) {
            mGameName = gameName;
        }
        
        /**
         * @return the applicationId
         */
        public int getApplicationId() {
            return mApplicationId;
        }
        
        /**
         * @param applicationId the applicationId to set
         */
        protected void setApplicationId(int applicationId) {
            mApplicationId = applicationId;
        }
        
        /**
         * @return the numPlayers
         */
        public int getNumPlayers() {
            return mNumPlayers;
        }
        
        /**
         * @param numPlayers the numPlayers to set
         */
        protected void setNumPlayers(int numPlayers) {
            mNumPlayers = numPlayers;
        }
        
        /**
         * @return the maxPlayers
         */
        public int getMaxPlayers() {
            return mMaxPlayers;
        }
        
        /**
         * @param maxPlayers the maxPlayers to set
         */
        protected void setMaxPlayers(int maxPlayers) {
            mMaxPlayers = maxPlayers;
        }
        
        /**
         * @return the numBots
         */
        public int getNumBots() {
            return mNumBots;
        }
        
        /**
         * @param numBots the numBots to set
         */
        protected void setNumBots(int numBots) {
            mNumBots = numBots;
        }
        
        /**
         * @return the dedicated
         */
        public ListenType getListenType() {
            return mListenType;
        }
        
        /**
         * @param dedicated the dedicated to set
         */
        protected void setListenType(ListenType listenType) {
            mListenType = listenType;
        }
        
        /**
         * @return the operatingSystem
         */
        public OperatingSystem getOperatingSystem() {
            return mOperatingSystem;
        }
        
        /**
         * @param operatingSystem the operatingSystem to set
         */
        protected void setOperatingSystem(OperatingSystem operatingSystem) {
            mOperatingSystem = operatingSystem;
        }
        
        /**
         * @return the password
         */
        public boolean isPrivate() {
            return mIsPrivate;
        }
        
        /**
         * @param password the password to set
         */
        protected void setPrivate(boolean isPrivate) {
            mIsPrivate = isPrivate;
        }
        
        /**
         * @return the secure
         */
        public boolean isSecure() {
            return mSecure;
        }
        
        /**
         * @param secure the secure to set
         */
        protected void setSecure(boolean secure) {
            mSecure = secure;
        }
        
        /**
         * @return the gameVersion
         */
        public String getGameVersion() {
            return mGameVersion;
        }
        
        /**
         * @param gameVersion the gameVersion to set
         */
        protected void setGameVersion(String gameVersion) {
            mGameVersion = gameVersion;
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
        protected void setPort(int port) {
            mPort = port;
        }
        
        /**
         * @return the serverSteamId
         */
        public String getSteamId() {
            return mSteamId;
        }
        
        /**
         * @param serverSteamId the serverSteamId to set
         */
        protected void setSteamId(String steamId) {
            mSteamId = steamId;
        }

        /**
         * @return the tag
         */
        protected final String getTag() {
            return mTag;
        }

        /**
         * @param tag the tag to set
         */
        protected final void setTag(String tag) {
            mTag = tag;
        }

        /**
         * @return the spectatorPort
         */
        protected final int getSpectatorPort() {
            return mSpectatorPort;
        }

        /**
         * @param spectatorPort the spectatorPort to set
         */
        protected final void setSpectatorPort(int spectatorPort) {
            mSpectatorPort = spectatorPort;
        }

        /**
         * @return the spectatorServerName
         */
        protected final String getSpectatorServerName() {
            return mSpectatorServerName;
        }

        /**
         * @param spectatorServerName the spectatorServerName to set
         */
        protected final void setSpectatorServerName(String spectatorServerName) {
            mSpectatorServerName = spectatorServerName;
        }
        
        
    }
    
    public static final class PlayersQueryResult extends QueryResult {
        private int mNumPlayers = 0;
        
        public static class PlayerDetails {
            private int mIndex = 0;
            private String mName = null;
            private long mKills = 0;
            private float mTimeConnectedSeconds = 0;
            
            /**
             * @return the index
             */
            public int getIndex() {
                return mIndex;
            }
            
            /**
             * @param index the index to set
             */
            protected void setIndex(int index) {
                mIndex = index;
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
            protected void setName(String name) {
                mName = name;
            }
            
            /**
             * @return the kills
             */
            public long getKills() {
                return mKills;
            }
            
            /**
             * @param kills the kills to set
             */
            protected void setKills(long kills) {
                mKills = kills;
            }
            
            /**
             * @return the timeConnectedSeconds
             */
            public float getTimeConnectedSeconds() {
                return mTimeConnectedSeconds;
            }
            
            /**
             * @param timeConnectedSeconds the timeConnectedSeconds to set
             */
            protected void setTimeConnectedSeconds(float timeConnectedSeconds) {
                mTimeConnectedSeconds = timeConnectedSeconds;
            }
        }
        
        ArrayList<PlayerDetails> mPlayerList = null;

        /**
         * @return the playerList
         */
        public ArrayList<PlayerDetails> getPlayers() {
            return mPlayerList;
        }

        /**
         * @param playerList the playerList to set
         */
        protected void setPlayers(ArrayList<PlayerDetails> playerList) {
            mPlayerList = playerList;
        }

        /**
         *
         * @return int
         */
        public int getNumPlayers() {
            return mNumPlayers;
        }

        /**
         *
         * @param numPlayers numPlayers
         */
        protected void setNumPlayers(int numPlayers) {
            mNumPlayers = numPlayers;
        }
    }
    
    public static final class RulesQueryResult extends QueryResult {
        private int mNumRules = 0;
        private HashMap<String, String> mRuleMap = null;
        
        /**
         * @return the numRules
         */
        public int getNumRules() {
            return mNumRules;
        }
        
        /**
         * @param numRules the numRules to set
         */
        public void setNumRules(int numRules) {
            mNumRules = numRules;
        }
        
        /**
         * @return the ruleMap
         */
        public HashMap<String, String> getRules() {
            return mRuleMap;
        }
        
        /**
         * @param ruleMap the ruleMap to set
         */
        public void setRules(HashMap<String, String> ruleMap) {
            mRuleMap = ruleMap;
        }
    }

    protected SourceQuery(SourceQueryResource resource, Transmission transmission) {
        super(resource, transmission);
    }
    
    private UdpTransmission getUdpTransmission() {
        return (UdpTransmission)getTransmission();
    }
    
    private SourceQueryConnection getSourceQueryConnection() {
        return (SourceQueryConnection)getConnection();
    }
    
    @Override
    public void setResource(Enum<?> resource) {
        if (resource.getClass() != SourceQueryResource.class) {
            throw new IllegalArgumentException("Resource must be of type `SourceQueryResource`.");
        }
        
        super.setResource(resource);
    }
    
    /**
     * Retrieves the query resource.
     *
     * @return SourceQueryResource
     */
    public SourceQueryResource getSourceQueryResource() {
        return (SourceQueryResource)getResource();
    }
    
    private void send(byte[] request) throws ConnectionException {      
        final byte[] prefix = {
            (byte)0xFF,
            (byte)0xFF,
            (byte)0xFF,
            (byte)0xFF
        };
        
        byte[] buffer = new byte[prefix.length + request.length];

        System.arraycopy(prefix, 0, buffer, 0, prefix.length);
        System.arraycopy(request, 0, buffer, prefix.length, request.length);
        
        getUdpTransmission().send(buffer);
    }
    
    private byte[] receive() throws ConnectionException, QueryException {
        byte[] response = getUdpTransmission().receive();
        
        if (response.length == 0
            || response[0] != (byte)0xFF
            || response[1] != (byte)0xFF
            || response[2] != (byte)0xFF
            || response[3] != (byte)0xFF) {
                throw new QueryException("Invalid server response header.");
        }
            
        return response;
    }
    
    @Override
    public QueryResult query() throws ConnectionException, QueryException {
        boolean wasOpen = getConnection().isOpen();
        SourceQueryResource resource = getSourceQueryResource();
        byte[] response = null;
        
        send(getRequest());
        response = receive();
        
        if (!wasOpen) { // maintain previous manual state
            getConnection().close();
        }
        
        switch (resource) {
        case INFO:
            return parseInfoResponse(response);
        case PLAYERS:
            return parsePlayersResponse(response);
        case RULES:
            return parseRulesResponse(response);    
        case CHALLENGE:
            return parseChallengeResponse(response);
        }        
        
        throw new IllegalStateException("Unknown resource type `" + getResource().toString() + "`.");        
    }
    
    private byte[] getRequest() throws ConnectionException, QueryException { 
        SourceQueryResource resource = getSourceQueryResource();
        byte[] request = null;
        
        switch (resource) {
        case INFO:
            try {
                request = "TSource Engine Query\0".getBytes(CHARSET_LATIN);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Latin charset does not exist.");
            }
            break;
            
        case PLAYERS:
        case RULES:
            byte[] challenge = getSourceQueryConnection().getChallenge();
            request = new byte[] {
                (byte)( resource == SourceQueryResource.PLAYERS ? 0x55 : 0x56 ),
                challenge[0],
                challenge[1],
                challenge[2],
                challenge[3]
            };
            break;
            
        case CHALLENGE:
            request = new byte[] { (byte)0x56, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };           
            break;       
            
        default:
            throw new IllegalArgumentException("Unknown resource type `" + getResource().toString() + "`.");
        }
        
        return request;
    }
    
    private String toUtf(StringBuffer buffer) {
        byte[] latin1 = null;
        String utf8 = null;
        
        try {
            latin1 = buffer.toString().trim().getBytes(CHARSET_LATIN);
            utf8 = new String(latin1, CHARSET_UTF);
        } catch (UnsupportedEncodingException e) {
            utf8 = buffer.toString();
        }

        return utf8;
    }    
    
    private ChallengeQueryResult parseChallengeResponse(byte[] response) throws QueryException {
        ChallengeQueryResult result = new ChallengeQueryResult();
        
        if (response[4] != 0x41) {
            throw new QueryException("Invalid server response.");
        }
        
        result.setChallengeBytes(new byte[] { response[5], response[6], response[7], response[8] });
        return result;
    }
    
    private InfoQueryResult parseInfoResponse(byte[] response) throws QueryException {
        InfoQueryResult result = new InfoQueryResult();
        StringBuffer buffer = null;
        byte extraDataFlag = 0;
        long[] steamId = new long[2];
        int offset = 4;

        if (response[offset] != 0x49) { 
            throw new QueryException("Invalid server response.");
        }

        // protocol version
        ++offset;
        result.setProtocolVersion(response[offset]);
        
        // server name
        ++offset;
        buffer = new StringBuffer(20);
        while(response[offset] != 0) {
            buffer.append((char)(response[offset++] & 0xFF));
        }
        result.setName(toUtf(buffer));
        
        // map token
        ++offset;
        buffer = new StringBuffer(20);
        while(response[offset] != 0) {
            buffer.append((char)(response[offset++] & 0xFF));
        }
        result.setMapToken(toUtf(buffer));

        //game directory
        ++offset;
        buffer = new StringBuffer(20);
        while(response[offset] != 0) {
            buffer.append((char)(response[offset++] & 0xFF));
        }
        result.setGameDirectory(buffer.toString());
        
        // game name
        buffer = new StringBuffer(20);
        ++offset;
        while(response[offset] != 0) {
            buffer.append((char)(response[offset++] & 0xFF));
        }
        result.setGameName(toUtf(buffer));
        
        // application id
        ++offset;
        result.setApplicationId((response[offset] & 0xFF) | (response[offset + 1] & 0xFF) << 8);
        ++offset;
        
        // number of players
        ++offset;
        result.setNumPlayers(((int)response[offset] & 0xFF));
        
        // maximum players
        ++offset;
        result.setMaxPlayers(((int)response[offset] & 0xFF));        
        
        // number of bots
        ++offset;
        result.setNumBots(((int)response[offset] & 0xFF));
        
        // dedicated or listen
        ++offset;
        switch(response[offset] & 0xFF) {
        case 'l':
            result.setListenType(ListenType.LISTEN);
            break;
        case 'd':
            result.setListenType(ListenType.DEDICATED);
            break;
        case 'p':
            result.setListenType(ListenType.SOURCE_TV);
            break;       
        default:
            throw new QueryException("Invalid listen type in server response.");
        }
        
        // operating system
        ++offset;
        switch(response[offset] & 0xFF) {
        case 'l':
            result.setOperatingSystem(OperatingSystem.LINUX);
            break;
        case 'w':
            result.setOperatingSystem(OperatingSystem.WINDOWS);
            break;        
        default:
            throw new QueryException("Invalid operating system in server response.");            
        }
        
        // has password
        ++offset;
        result.setPrivate((response[offset] & 0xFF) == 0x01);
        
        // vac secure
        ++offset;
        result.setSecure((response[offset] & 0xFF) == 0x01);
        
        // game version
        ++offset;
        buffer = new StringBuffer(20);
        while(response[offset] != 0) {
            buffer.append((char)(response[offset++] & 0xFF));
        }
        result.setGameVersion(toUtf(buffer));        
        
        // extra data flag (edf)
        ++offset;
        extraDataFlag = response[offset];
        ++offset;
        
        if ((extraDataFlag & 0x80) != 0) { // // game port
            result.setPort((response[offset] & 0xFF) | (response[offset + 1] & 0xFF) << 8);
            offset += 2;
        }
        if ((extraDataFlag & 0x10) != 0) { // // steam id
            steamId[0] =
                (response[offset] & 0xFF)
                | (response[offset + 1] & 0xFF) << 8
                | (response[offset + 2] & 0xFF) << 16
                | (response[offset + 3] & 0xFF) << 32;
            offset += 4;
            steamId[1] =
                (response[offset] & 0xFF)
                | (response[offset + 1] & 0xFF) << 8
                | (response[offset + 2] & 0xFF) << 16
                | (response[offset + 3] & 0xFF) << 32;
            offset += 4;     
            result.setSteamId(Long.toString(steamId[0]) + Long.toString(steamId[1]));
        }
        if ((extraDataFlag & 0x40) != 0) { // // spectator port and spectator server name
            // spectator port
            result.setSpectatorPort((response[offset] & 0xFF) | (response[offset + 1] & 0xFF) << 8);
            offset += 2;
            
            // spectator server name
            buffer = new StringBuffer(20);
            while(response[offset] != 0) {
                buffer.append((char)(response[offset++] & 0xFF));
            }
            result.setSpectatorServerName(toUtf(buffer));
            ++offset;            
        }
        if ((extraDataFlag & 0x20) != 0) { // // game tag
            buffer = new StringBuffer(20);
            while(response[offset] != 0) {
                buffer.append((char)(response[offset++] & 0xFF));
            }
            result.setTag(toUtf(buffer));
            ++offset;
        }
        if ((extraDataFlag & 0x01) != 0) { // // application id + several 0 bytes
            offset += 2; // short app id (already retrieved)
        }
        
        return result;
    } 
    

    /**
     * 
     *
     * @param response
     * @return
     * @throws QueryException
     */
    private PlayersQueryResult parsePlayersResponse(byte[] response) throws QueryException {
        PlayersQueryResult result = new PlayersQueryResult();
        StringBuffer buffer = null;
        int offset = 4;
        PlayerDetails player = null;
        ArrayList<PlayerDetails> playerList = new ArrayList<PlayerDetails>();
        
        if (response[offset] != 0x44) { 
            throw new QueryException("Invalid server response.");
        }    
        
        // number of players
        ++offset;
        result.setNumPlayers(response[offset++]);
        
        for (int i = 0, n = result.getNumPlayers(); i < n; ++i) {
            player = new PlayerDetails();
            
            // index
            player.setIndex(response[offset++]);
            
            // name
            buffer = new StringBuffer(20);
            while(response[offset] != 0) {
                buffer.append((char)(response[offset++] & 0xFF));
            }
            player.setName(toUtf(buffer));
            ++offset;
            
            // kills
            player.setKills(
                (response[offset] & 0xFF)
                | (response[offset + 1] & 0xFF) << 8
                | (response[offset + 2] & 0xFF) << 16
                | (response[offset + 3] & 0xFF) << 32
            );
            offset += 4;
            
            //TODO: time connected
            ByteBuffer bb = ByteBuffer.wrap(response, offset, 4);
            player.setTimeConnectedSeconds(bb.getFloat());
            offset += 4;
            
            playerList.add(player);
        }
        
        result.setPlayers(playerList);
        
        return result;
    }
    
    /**
     * 
     *
     * @param response
     * @return
     * @throws QueryException
     */
    private RulesQueryResult parseRulesResponse(byte[] response) throws QueryException {
        RulesQueryResult result = new RulesQueryResult();
        StringBuffer buffer = null;
        int offset = 4;
        String key = null, value = null;
        HashMap<String,String> rules = new HashMap<String, String>();
        
        if (response[offset] != 0x45) { 
            throw new QueryException("Invalid server response.");
        }    
        
        // number of players
        ++offset;
        result.setNumRules((response[offset] & 0xFF) | (response[offset + 1] & 0xFF) << 8);
        offset += 2;
        
        for (int i = 0, n = result.getNumRules(); i < n; ++i) {
            // key
            buffer = new StringBuffer(20);
            while(response[offset] != 0) {
                buffer.append((char)(response[offset++] & 0xFF));
            }
            key = (toUtf(buffer));
            ++offset;

            // value
            buffer = new StringBuffer(20);
            while(response[offset] != 0) {
                buffer.append((char)(response[offset++] & 0xFF));
            }
            value = (toUtf(buffer));
            ++offset;
            
            rules.put(key, value);
        }
        
        result.setRules(rules);
        
        return result;
    }    
}
