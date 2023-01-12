package eu.lunarsoftware.serverswitcher;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import eu.lunarsoftware.serverswitcher.ServerSwitcher;

public class SwitchCommand implements SimpleCommand {
    private final RegisteredServer destination;

    public SwitchCommand(String destinationName) throws Exception {
        RegisteredServer dest = ServerSwitcher.getServer().getServer(destinationName).orElse(null);
        if (dest == null) throw new Exception("Server not found: " + destinationName);
        this.destination = dest;
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            ServerSwitcher.getLogger().info("Teleport " + player.getUsername() + " to " + destination.getServerInfo().getName() + ".");
            player.createConnectionRequest(destination).connect();
        } else {
            ServerSwitcher.getLogger().info("Source is not a player.");
        }
    }
}
