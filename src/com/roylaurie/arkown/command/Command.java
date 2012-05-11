package com.roylaurie.arkown.command;

import java.util.ArrayList;
import java.util.HashMap;

public class Command implements Comparable<Command> {
    public static final String MAP_NAME = "name";
    public static final String MAP_RAW_COMMAND = "raw_command";
    public static final String MAP_TARGET = "target";
    public static final String MAP_DATABASE_ID = "_id";
    public static final String MAP_CATEGORY_DATABASE_ID = "_category_id";
    
    
    private Target mTarget = null;
    private String mName = "";
    private String mRawCommandString = "";
    private Category mCategory = null;
    private long mApplicationDatabaseId = 0;
    private long mDatabaseId = 0;
    private long mCategoryApplicationDatabaseId = 0;
    private long mCategoryDatabaseId = 0;
    private OptionType mOptionType = OptionType.NONE;
    private ArrayList<String> mOptionList = new ArrayList<String>();    
    
    public enum Target {
        CLIENT,
        SERVER;
        
        private static HashMap<Target, String> sNameMap = new HashMap<Target, String>();
        static {
            sNameMap.put(CLIENT, "Client");
            sNameMap.put(SERVER, "Server");
        }
        
        public String toName() {
            return sNameMap.get(this);
        }
        
        public static Target fromName(String name) {
            for (Target v : Target.values()) {
                if (sNameMap.get(v) == name) {
                    return v;
                }
            }
            
            throw new IllegalArgumentException("No target exists by the name `" + name + "`.");
        }
        
        
    }
    
    public enum OptionType {
        NONE,
        CUSTOM_LIST,
        MAP_LIST;
        
        private static HashMap<OptionType, String> sNameMap = new HashMap<OptionType, String>();
        static {
            sNameMap.put(NONE, "None");
            sNameMap.put(CUSTOM_LIST, "Custom list");
            sNameMap.put(MAP_LIST, "Map list");
        }
        
        public String toName() {
            return sNameMap.get(this);
        }
        
        public static OptionType fromName(String name) {
            for (OptionType v : OptionType.values()) {
                if (sNameMap.get(v) == name) {
                    return v;
                }
            }
            
            throw new IllegalArgumentException("No option type exists by the name `" + name + "`.");
        }        
    }
    
    public class ParserException extends Exception {
        private static final long serialVersionUID = 1L;

        public ParserException(String message) {
            super(message);
        }
    }
    
    
    public Command() {}
    
    public Command(String name, String rawCommand, Target target) {
        setName(name);
        setRawCommandString(rawCommand);
        setTarget(target);
    }    
    
    @Override
    public boolean equals(Object rh) {
        if (rh == null || rh.getClass() != getClass()) {
            return false;
        }
        
        return ( hashCode() == rh.hashCode() );
    }
    
    @Override
    public int hashCode() {
        int code = 1;
        code = code * 7 + ( mName == null ? 0 : mName.hashCode() );
        code = code * 7 + ( mCategory == null ? 0 : mCategory.hashCode() );
        
        return code;
    } 

    @Override
    public int compareTo(Command rh) {
        return mName.compareToIgnoreCase(rh.mName);
    }    
    
    public String getCommandString() throws ParserException {
        return getCommandString("");
    }
    
    public String getCommandString(String option) throws ParserException {
        String cmd = getRawCommandString();
        
        if (option == null || option.length() == 0) {
            cmd = cmd.replace("{%option%}", option);
        }
            
        return cmd;
    }

	public String getRawCommandString() {
		return mRawCommandString;
	}

	public void setRawCommandString(String rawCommandString) {
		mRawCommandString = rawCommandString;
	}

    /**
     * @return the category
     */
    public Category getCategory() {
        return mCategory;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        mCategory = category;
        
        if (category != null) {
            setCategoryApplicationDatabaseId(category.getApplicationDatabaseId());
            setCategoryDatabaseId(category.getDatabaseId());
        }
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

    /**
     * @return the databaseId
     */
    public long getDatabaseId() {
        return mDatabaseId;
    }

    /**
     * @param databaseId the databaseId to set
     */
    public void setDatabaseId(long id) {
        mDatabaseId = id;
    }

    /**
     * @return the databaseId
     */
    public long getApplicationDatabaseId() {
        return mApplicationDatabaseId;
    }

    /**
     * @param databaseId the databaseId to set
     */
    public void setApplicationDatabaseId(long id) {
        mApplicationDatabaseId = id;
    }    
    
    /**
     * @return the categoryDatabaseId
     */
    public long getCategoryDatabaseId() {
        return mCategoryDatabaseId;
    }
    
    /**
     * @param id
     */
    public void setCategoryDatabaseId(long id) {
        mCategoryDatabaseId = id;
    }    
    
    /**
     * @param categoryDatabaseId the categoryDatabaseId to set
     */
    public void setCategoryApplicationDatabaseId(long id) {
        mCategoryApplicationDatabaseId = id;
    }    
    
    /**
     * @return the categoryDatabaseId
     */
    public long getCategoryApplicationDatabaseId() {
        return mCategoryApplicationDatabaseId;
    }


    public HashMap<String, String> getHashMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(MAP_NAME, getName());
        map.put(MAP_RAW_COMMAND, getRawCommandString());
        map.put(MAP_DATABASE_ID, Long.toString(getDatabaseId()));
        map.put(MAP_CATEGORY_DATABASE_ID, Long.toString(getCategoryDatabaseId()));
        map.put(MAP_TARGET, mTarget.toName());
        
        return map;
    }

    /**
     * @return the target
     */
    public Target getTarget() {
        return mTarget;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(Target target) {
        mTarget = target;
    }

    /**
     * @return the options
     */
    public ArrayList<String> getOptions() {
        return mOptionList;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(ArrayList<String> options) {
        mOptionList = options;
    }
    
    public void addOption(String option) {
        mOptionList.add(option);
    }

    /**
     * @return the optionType
     */
    public OptionType getOptionType() {
        return mOptionType;
    }

    /**
     * @param optionType the optionType to set
     */
    public void setOptionType(OptionType optionType) {
        mOptionType = optionType;
    }
}
