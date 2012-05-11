/**
 * 
 */
package com.roylaurie.arkown.command;

import com.roylaurie.arkown.server.Server;

/**
 * @author rlaurie
 *
 */
public class ServerCommand extends Command {
    protected Target mTarget = Target.SERVER;
    
    protected Server mServer = null;
    
    public ServerCommand() {}
    
    public ServerCommand(Command command, Server server) {
        setName(command.getName());
        setRawCommandString(command.getRawCommandString());
        setServer(server);
        setOptionType(command.getOptionType());
        setOptions(command.getOptions());
    }        
    
    public ServerCommand(String name, String rawCommand, Server server) {
        setName(name);
        setRawCommandString(rawCommand);
        setServer(server);
    }

    /**
     * @return the server
     */
    public Server getServer() {
        return mServer;
    }

    /**
     * @param server the server to set
     */
    public void setServer(Server server) {
        mServer = server;
    }

    public void setTarget(Target target) {
        throw new IllegalStateException("Cannot change target.");
    }
    
    @Override
    public String getCommandString() throws ParserException {
        return getCommandString("");
    }    
    
    @Override
    public String getCommandString(String option) throws ParserException {
        String cmd = super.getCommandString(option);
        Server server = getServer();
        
        cmd = cmd.replace("{%server.name%}", server.getName());
            
        return cmd;
    }    
}
