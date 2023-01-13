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
        version = ServerSwitcher.VERSION,
        description = "Map commands to servers",
        url = "https://mc.lunar-software.eu",
        authors = {"Uweb95"}
)
public class ServerSwitcher {
    private static ProxyServer server;
    private static Logger logger;
    private final Path dataDirectory;
    private static Configuration config;

    public static final String VERSION = "1.1";

    @Inject
    public ServerSwitcher(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        ServerSwitcher.server = server;
        ServerSwitcher.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();
        ServerSwitcher.config = Configuration.loadConfig(dataDirectory);
        commandManager.register("serswi", new MainCommand());
        registerCommands();
    }

    public static void registerCommands() {
        CommandManager commandManager = server.getCommandManager();

        if (config != null) {
            int commands = 0;

            for (Server server : config.getServerList()) {
                try {
                    commandManager.register(server.command(), new SwitchCommand(server.servername()));
                    commands++;
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            }

            logger.info("Server Switcher initialized. " + commands + " commands registered.");
        } else {
            logger.warn("Server Switcher configuration could not be loaded.");
        }
    }

    public static void unregisterCommands() {
        CommandManager commandManager = server.getCommandManager();

        if (config != null) {
            for (Server server : config.getServerList()) {
                try {
                    commandManager.unregister(server.command());
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            }
        }
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void updateConfiguration() {
        config.reload();
    }
}
