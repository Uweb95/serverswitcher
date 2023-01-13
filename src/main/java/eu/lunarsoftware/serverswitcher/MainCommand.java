package eu.lunarsoftware.serverswitcher;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class MainCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        Player player = (Player) invocation.source();

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload" -> reload(player);
            }
        } else {
            if (player != null) player.sendMessage(Component.text("Server Switcher version " + ServerSwitcher.VERSION));
        }
    }

    private void reload(Player player) {
        ServerSwitcher.unregisterCommands();
        ServerSwitcher.updateConfiguration();
        ServerSwitcher.registerCommands();
        ServerSwitcher.getLogger().info("Refresh completed");
        if (player != null) player.sendMessage(Component.text("Refresh completed"));
    }
}
