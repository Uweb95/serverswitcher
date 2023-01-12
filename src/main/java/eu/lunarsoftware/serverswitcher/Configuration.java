package eu.lunarsoftware.serverswitcher;

import com.moandjiezana.toml.Toml;

import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Configuration {
    List<Server> serverList = new ArrayList<>();
    private static File configurationFile;

    Configuration(Toml config) {
        loadConfig(config);
    }

    public static Configuration loadConfig(Path path) {
        Path p = createConfig(path);

        if (p != null) {
            Configuration.configurationFile = p.toFile();
            Toml config = new Toml().read(configurationFile);
            return new Configuration(config);
        }

        return null;
    }

    public void reload() {
        loadConfig(new Toml().read(configurationFile));
    }

    private void loadConfig(Toml config) {
        serverList = new ArrayList<>();

        List<Toml> serverListRaw = config.getTables("server");

        for (Toml entry : serverListRaw) {
            serverList.add(new Server(entry.getString("name"), entry.getString("command")));
        }
    }

    /**
     * Create the configuration file if it does not exist and return its path
     *
     * @param dataDir
     * @return Path
     */
    private static Path createConfig(Path dataDir) {
        try {
            if (Files.notExists(dataDir)) Files.createDirectory(dataDir);
            Path path = dataDir.resolve("config.toml");

            if (Files.notExists(path)) {
                try (InputStream stream = Configuration.class.getResourceAsStream("/config.toml")) {
                    Files.copy(Objects.requireNonNull(stream), path);
                }
            }

            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Server> getServerList() {
        return serverList;
    }
}
