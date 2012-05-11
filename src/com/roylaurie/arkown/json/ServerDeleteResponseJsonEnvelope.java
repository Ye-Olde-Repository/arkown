/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.json;

import com.roylaurie.modeljson.JsonEnvelope;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;
import com.roylaurie.modeljson.exception.JsonException;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class ServerDeleteResponseJsonEnvelope extends JsonEnvelope {
    public static final String PROTOTYPE_RESULT = "result";
    
    public static enum Result {
        OK,
        ERROR_PERMISSION,
        ERROR_NOT_FOUND,
        ERROR_UNKNOWN;
    }
    
    public ServerDeleteResponseJsonEnvelope() {
        super(ArkownJsonFactory.getInstance());

        addPrototype(new Prototype(
            PROTOTYPE_RESULT,
            ExclusionStrategyType.PUBLIC,
            Result.class
        ));
        
        putObject(PROTOTYPE_RESULT, null);        
    }
    
    public ServerDeleteResponseJsonEnvelope(String json) throws JsonException {
        super(ArkownJsonFactory.getInstance());
        
        addPrototype(new Prototype(
            PROTOTYPE_RESULT,
            ExclusionStrategyType.PUBLIC,
            Result.class
        ));
        
        fromJson(json);
    }
    
    /**
     * Retrieves the API result.
     *
     * @return Result
     */
    public Result getResult() {
        return (Result)getObject(PROTOTYPE_RESULT);
    }
    
    public void setResult(Result result) {
        putObject(PROTOTYPE_RESULT, result);
        setNeedsJsonRefresh();
    }    
}
