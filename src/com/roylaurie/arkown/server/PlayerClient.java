/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.server;

import java.util.HashMap;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public class PlayerClient extends Client {
    private int mScore = 0;


    public PlayerClient(String name) {
        super(name);
    }    
    
    public PlayerClient() {
    }

    /**
     * Retrieves the mScore.
     *
     * @return int
     */
    public int getScore() {
        return mScore;
    }
    
    /**
     * Sets the mScore
     * @param int score
     */
    public void setScore(int score) {
        mScore = score;
    }
    
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> map = super.getHashMap();
        
        map.put("score", Integer.toString(getScore()));
        
        return map;
    }    
}
