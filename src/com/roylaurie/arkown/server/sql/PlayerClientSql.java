package com.roylaurie.arkown.server.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.roylaurie.modelsql.ConnectionParameters;
import com.roylaurie.modelsql.ModelSql;
import com.roylaurie.arkown.server.PlayerClient;

public final class PlayerClientSql extends ModelSql<PlayerClient> {
    public static final class Column {
        public static final String ID = "id";
        public static final String SERVER_ID = "server_id";
        public static final String NAME = "name";
        public static final String PLAYER_SCORE = "player_score";
        
        public static final ColumnTypeMap COLUMN_TYPE_MAP = new ColumnTypeMap(); 
        static {
            COLUMN_TYPE_MAP.put(ID, Long.class);
            COLUMN_TYPE_MAP.put(SERVER_ID, Long.class);
            COLUMN_TYPE_MAP.put(NAME, String.class);
            COLUMN_TYPE_MAP.put(PLAYER_SCORE, Integer.class);
        }
    }

    // static default construction
    static {
        ModelSql.loadConnectionParameters(PlayerClient.class);
    }
        
    
    public PlayerClientSql(String context) {
        super(context, PlayerClient.class);
    }
    
    public PlayerClientSql(ConnectionParameters connectionParameters) {
        super(connectionParameters);
    }    
    
    public PlayerClientSql(ConnectionParameters connectionParameters, Connection connection) {
        super(connectionParameters, connection);
    }

    @Override
    public PlayerClient read(long id) throws SQLException {
        ColumnValueMap valueMap = read(id, new ArrayList<String>(Column.COLUMN_TYPE_MAP.keySet()));
        PlayerClient client = new PlayerClient();

        client.setDatabaseId(id);
        client.setName((String)valueMap.get(Column.NAME));
        client.setScore((Integer)valueMap.get(Column.PLAYER_SCORE));
        
        return client;
    }

    @Override
    public void write(PlayerClient client) throws SQLException {
        ColumnValueMap valueMap = new ColumnValueMap();

        valueMap.put(Column.NAME, client.getName());
        valueMap.put(Column.SERVER_ID, client.getServer().getDatabaseId());
        valueMap.put(Column.PLAYER_SCORE, client.getScore());      
        
        long databaseId = write(client, valueMap);
        client.setDatabaseId(databaseId);
        return;
    }

    @Override
    public long getDatabaseId(PlayerClient client) {
        return client.getDatabaseId();
    }

    @Override
    protected ColumnTypeMap getColumnTypeMap() {
        return Column.COLUMN_TYPE_MAP;
    }

    @Override
    protected void validateOriginal(PlayerClient model) throws ModelSqlDuplicateException, SQLException {
        return;
    }
}
