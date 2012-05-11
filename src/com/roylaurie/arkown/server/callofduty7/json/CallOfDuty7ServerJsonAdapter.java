/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.server.callofduty7.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.roylaurie.arkown.server.callofduty7.CallOfDuty7Server;
import com.roylaurie.arkown.server.json.ServerJsonAdapter;
import com.roylaurie.modeljson.JsonFactory;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class CallOfDuty7ServerJsonAdapter extends ServerJsonAdapter {
   public CallOfDuty7ServerJsonAdapter(JsonFactory factory) {
        super(factory);
    }

   /**
    *
    * @param json
    * @param typeOfT
    * @param context
    * @return
    * @throws JsonParseException
    */
   @Override
   public CallOfDuty7Server deserialize(JsonElement json, Type typeOfT,
           JsonDeserializationContext context) throws JsonParseException {
       CallOfDuty7Server server = (CallOfDuty7Server)super.deserialize(json, typeOfT, context);
       JsonObject jsonObject = json.getAsJsonObject();
       
       server.setMapToken(jsonObject.getAsJsonPrimitive("mapToken").getAsString());
       
       return server;
   }
}
