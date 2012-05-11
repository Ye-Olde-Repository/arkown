package com.roylaurie.arkown.server.sql;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.roylaurie.arkown.engine.EngineType;
import com.roylaurie.modelsql.ConnectionParameters;
import com.roylaurie.modelsql.ModelSql;
import com.roylaurie.modelsql.ModelSql.ColumnQueryList;
import com.roylaurie.arkown.server.Client;
import com.roylaurie.arkown.server.PlayerClient;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.arkown.server.Server.ServerMaps;

public class ServerSql extends ModelSql<Server> {
    public static final class Column {
        public static final String ID = "id";
        public static final String ENGINE_TYPE = "engine_type";
        public static final String HOSTNAME = "hostname";
        public static final String HOST_ADDRESS = "host_address";
        public static final String PORT = "port";
        public static final String PRODUCT_TYPE = "product_type";
        public static final String NAME = "name";
        public static final String MAX_CLIENTS = "max_clients";
        public static final String NUM_CLIENTS = "num_clients";
        public static final String RESPONSE_TIME = "response_time";
        public static final String LAST_PULL_TIME = "last_pull_time";
        public static final String CREDENTIAL_USERNAME = "credential_username";
        public static final String CREDENTIAL_PASSWORD = "credential_password";
        public static final String IS_QUERY_PROXY_ALLOWED = "is_query_proxy_allowed";
        public static final String MAP_TOKEN = "map_token";
        
        public static final ColumnTypeMap COLUMN_TYPE_MAP = new ColumnTypeMap(); 
        static {
            COLUMN_TYPE_MAP.put(ID, Long.class);
            COLUMN_TYPE_MAP.put(ENGINE_TYPE, String.class);
            COLUMN_TYPE_MAP.put(HOSTNAME, String.class);
            COLUMN_TYPE_MAP.put(HOST_ADDRESS, String.class);
            COLUMN_TYPE_MAP.put(PORT, Integer.class);
            COLUMN_TYPE_MAP.put(NAME, String.class);
            COLUMN_TYPE_MAP.put(PRODUCT_TYPE, String.class);
            COLUMN_TYPE_MAP.put(MAX_CLIENTS, Integer.class);
            COLUMN_TYPE_MAP.put(NUM_CLIENTS, Integer.class);
            COLUMN_TYPE_MAP.put(RESPONSE_TIME, Integer.class);
            COLUMN_TYPE_MAP.put(LAST_PULL_TIME, Long.class);
            COLUMN_TYPE_MAP.put(CREDENTIAL_USERNAME, String.class);
            COLUMN_TYPE_MAP.put(CREDENTIAL_PASSWORD, String.class);
            COLUMN_TYPE_MAP.put(IS_QUERY_PROXY_ALLOWED, Boolean.class);
            COLUMN_TYPE_MAP.put(MAP_TOKEN, String.class);
        }
    }
    
    // static default construction
    static {
        ModelSql.loadConnectionParameters(Server.class);
    }    
    
    public ServerSql(String context) {
        super(context, Server.class);
    }
    
    public ServerSql(ConnectionParameters connectionParameters) {
        super(connectionParameters);
    }    
    
    public ServerSql(ConnectionParameters connectionParameters, Connection connection) {
        super(connectionParameters, connection);
    }

    @Override
    public Server read(long id) throws SQLException {
        ColumnValueMap valueMap = read(id, new ArrayList<String>(Column.COLUMN_TYPE_MAP.keySet()));
        Server server = Server.factory(EngineType.valueOf((String)valueMap.get(Column.ENGINE_TYPE)).getEngine());

        server.setDatabaseId(id);
        server.setHostname((String)valueMap.get(Column.HOSTNAME));
        server.setPort((Integer)valueMap.get(Column.PORT));
        server.setProduct(server.getEngine().productValueOf(((String)valueMap.get(Column.PRODUCT_TYPE))));
        server.setName((String)valueMap.get(Column.NAME));
        server.setMaxClients((Integer)valueMap.get(Column.MAX_CLIENTS));
        server.setNumClients((Integer)valueMap.get(Column.NUM_CLIENTS));
        server.setResponseTime((Integer)valueMap.get(Column.RESPONSE_TIME));
        server.setLastPullTime((Long)valueMap.get(Column.LAST_PULL_TIME));
        server.setQueryProxyAllowed((Boolean)valueMap.get(Column.IS_QUERY_PROXY_ALLOWED));
        server.getCredentials().setUsername((String)valueMap.get(Column.CREDENTIAL_USERNAME));
        server.getCredentials().setPassword((String)valueMap.get(Column.CREDENTIAL_PASSWORD));

        if (server instanceof ServerMaps) {
            ((ServerMaps)server).setMapToken((String)valueMap.get(Column.MAP_TOKEN));
        }
        
        // read players
        PlayerClientSql clientSql = new PlayerClientSql(ModelSql.CONTEXT_READ);
        ColumnQueryList queryList = new ColumnQueryList();
        queryList.and(PlayerClientSql.Column.SERVER_ID, id);
        List<PlayerClient> clientList = clientSql.find(queryList);
        server.setClients(clientList);
        
        return server;
    }

    /**
     * Ensures that servers that WOULD be inserted are not duplicates in the current database, thus preventing
     * unnecessary inserts on the ID sequence table. 
     *
     * @param Server server
     * @throws ModelSqlDuplicateException With SQLSTATE_DUPLICATE as the SQLState
     * @throws IllegalStateException With UnknownHostException as the cause if host address doesn't resolve.
     */
    protected void validateOriginal(Server server) throws ModelSqlDuplicateException, SQLException {
        if (server.getDatabaseId() > 0) {
            return;
        }
        
        ServerSql readServerSql = new ServerSql(ModelSql.CONTEXT_READ);
        ColumnQueryList queryList = new ColumnQueryList();
        String address = null;
        
        try {
            address = InetAddress.getByName(server.getHostname()).getHostAddress();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Hostname `" + server.getHostname() + "` does not resolve to an address.", e);
        }

        queryList.and(new ColumnQueryList()
            .and(ServerSql.Column.HOSTNAME, server.getHostname())
            .or(ServerSql.Column.HOST_ADDRESS, address)
        )
        .and(ServerSql.Column.PORT, server.getPort());
        
        List<Server> serverList = readServerSql.find(queryList, 1);
        if (!serverList.isEmpty()) {
            throw new ModelSqlDuplicateException("Server " + server.getHost() + " already exists.", serverList.get(0));
        }
    }
    
    /**
     * Inserts / updates a server object in the database.
     *
     * @param Server server
     * @throws SQLException
     * @throws IllegalStateException With UnknownHostException as the cause if host address doesn't resolve.
     */
    @Override
    public void write(Server server) throws SQLException {
        ColumnValueMap valueMap = new ColumnValueMap();
        
        valueMap.put(Column.ENGINE_TYPE, server.getEngine().getType().toString());
        valueMap.put(Column.HOSTNAME, server.getHostname());
        
        try {
            valueMap.put(Column.HOST_ADDRESS, server.getHostAddress());
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Hostname `" + server.getHostname() + "` does not resolve to an address.", e);
        }        
        
        valueMap.put(Column.PORT, server.getPort());
        valueMap.put(Column.PRODUCT_TYPE, server.getProduct().toString());
        valueMap.put(Column.NAME, server.getName());
        valueMap.put(Column.MAX_CLIENTS, server.getMaxClients());
        valueMap.put(Column.NUM_CLIENTS, server.getNumClients());
        valueMap.put(Column.RESPONSE_TIME, server.getResponseTime());
        valueMap.put(Column.LAST_PULL_TIME, server.getLastPullTime());
        valueMap.put(Column.IS_QUERY_PROXY_ALLOWED, server.isQueryProxyAllowed());
        valueMap.put(Column.CREDENTIAL_USERNAME, server.getCredentials().getUsername());
        valueMap.put(Column.CREDENTIAL_PASSWORD, server.getCredentials().getPassword());
        
        if (server instanceof ServerMaps) {
            valueMap.put(Column.MAP_TOKEN,((ServerMaps)server).getMapToken());
        }        
        
        long databaseId = write(server, valueMap);
        server.setDatabaseId(databaseId);
        
        // write players
        PlayerClientSql playerClientSql = new PlayerClientSql(ModelSql.CONTEXT_WRITE);
        for (Client client : server.getClients()) {
            playerClientSql.write((PlayerClient)client);
        }
        
        return;
    }

    @Override
    public long getDatabaseId(Server server) {
        return server.getDatabaseId();
    }

    @Override
    protected ColumnTypeMap getColumnTypeMap() {
        return Column.COLUMN_TYPE_MAP;
    }
}
