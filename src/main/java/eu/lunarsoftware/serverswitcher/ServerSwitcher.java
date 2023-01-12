package eu.lunarsoftware.serverswitcher;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "serverswitcher",
        name = "ServerSwitcher",
        version = "1.0-SNAPSHOT",
        description = "Map commands to servers",
        url = "https://mc.lunar-software.eu",
        authors = {"Uweb95"}
)
public class ServerSwitcher {
    private static ProxyServer server;
    private static Logger logger;
    private final Path dataDirectory;

    @Inject
    public ServerSwitcher(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        ServerSwitcher.server = server;
        ServerSwitcher.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();

        Configuration config = Configuration.loadConfig(dataDirectory);

        int commands = 0;

        if (config != null) {
            for (Server server : config.getServerList()) {
                try {
                    commandManager.register(server.command(), new SwitchCommand(server.servername()));
                    commands++;
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            }
        } else {
            logger.warn("Server Switcher configuration could not be loaded.");
        }

        logger.info("Server Switcher initialized. " + commands + " commands registered.");
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static Logger getLogger() {
        return logger;
    }
}
