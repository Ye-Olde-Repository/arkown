/**
 * 
 */
package com.roylaurie.arkown.engine.source;

import com.roylaurie.arkown.engine.Query;
import com.roylaurie.arkown.engine.connection.UdpConnection;
import com.roylaurie.arkown.engine.Connection.QueryConnection;
import com.roylaurie.arkown.engine.Query.QueryException;
import com.roylaurie.arkown.engine.source.SourceQuery;
import com.roylaurie.arkown.engine.source.SourceQuery.ChallengeQueryResult;
import com.roylaurie.arkown.engine.source.SourceQuery.SourceQueryResource;


/**
 * @author rlaurie
 *
 */
public final class SourceQueryConnection extends UdpConnection implements QueryConnection {   
    public static final int PACKET_SIZE_BYTES = 1400;
    private static final int SOCKET_TIMEOUT_MS = 2000;

    private byte[] mChallengeBytes = null;

    protected SourceQueryConnection(String hostname, int port) {
        super(hostname, port, SOCKET_TIMEOUT_MS, PACKET_SIZE_BYTES);
    } 
    
    @Override
    public Query createQuery(Enum<?> resource) {
        SourceQuery query = new SourceQuery((SourceQueryResource)resource, getTransmission());
        return query;
    }
    
    protected byte[] getChallenge() throws ConnectionException, QueryException {
        if (mChallengeBytes != null) {
            return mChallengeBytes;
        }
        
        ChallengeQueryResult result = (ChallengeQueryResult)createQuery(SourceQueryResource.CHALLENGE).query();
        mChallengeBytes = result.getChallengeBytes();
        
        return mChallengeBytes;
    }
    
    @Override
    public void close() {
        super.close();
        
        mChallengeBytes = null;
    }
}
