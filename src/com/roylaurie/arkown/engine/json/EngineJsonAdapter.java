/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.engine.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.engine.EngineType;
import com.roylaurie.modeljson.JsonAdapter;
import com.roylaurie.modeljson.JsonFactory;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class EngineJsonAdapter extends JsonAdapter<Engine> {
    public EngineJsonAdapter(JsonFactory factory) {
        super(factory);
    }

    @Override
    public JsonElement serialize(Engine src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getType().toString());
    }

    @Override
    public Engine deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        Engine engine = EngineType.valueOf(json.getAsJsonPrimitive().getAsString()).getEngine();
        return engine;
    }
}
