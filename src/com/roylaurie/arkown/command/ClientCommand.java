/**
 * 
 */
package com.roylaurie.arkown.command;

import com.roylaurie.arkown.server.Client;
import com.roylaurie.arkown.server.Server;

/**
 * @author rlaurie
 *
 */
public final class ClientCommand extends ServerCommand {
    protected Target mTarget = Target.CLIENT;
    
    private Client mClient = null;
    
    public ClientCommand() {
    }
    
    public ClientCommand(Command command, Server server, Client client) {
        setName(command.getName());
        setRawCommandString(command.getRawCommandString());
        setServer(server);
        setClient(client);
        setOptionType(command.getOptionType());
        setOptions(command.getOptions());
    }    
    
    public ClientCommand(String name, String rawCommand, Server server, Client client) {
        setName(name);
        setRawCommandString(rawCommand);
        setServer(server);
        setClient(client);
    }
    
    /**
     * @return the client
     */
    public Client getClient() {
        return mClient;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        mClient = client;
    }
    
    @Override
    public String getCommandString() throws ParserException {
        return getCommandString("");
    }       
    
    @Override
    public String getCommandString(String option) throws ParserException {
        String cmd = super.getCommandString(option);
        Client client = getClient();
        
        cmd = cmd.replace("{%client.name%}", client.getName());
            
        return cmd;
    }        
}
