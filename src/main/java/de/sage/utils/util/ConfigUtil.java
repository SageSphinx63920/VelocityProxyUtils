package de.sage.utils.util;

import de.sage.utils.ProxyUtils;
import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigUtil {

    private @Getter YAMLConfigurationLoader configLoader = null;

    public ConfigUtil(Path directory, String fileName) {
        configLoader = YAMLConfigurationLoader.builder().setPath(directory.resolve(fileName)).build();

        try {
            if (!directory.toFile().exists()) {
                directory.toFile().mkdirs();
                directory.resolve(fileName).toFile().createNewFile();
            }

            this.configLoader = YAMLConfigurationLoader.builder().setPath(directory.resolve("config.yml")).setFlowStyle(DumperOptions.FlowStyle.BLOCK).build();

            generateConfig();
        } catch (IOException exception) {
            exception.printStackTrace();
            ProxyUtils.getInstance().getLogger().error("Error while creating config file! Please report this to the developer!");
        }
    }

    public Object getOption(Object defaultValue, String... path) throws IOException {

        return configLoader.load().getNode(path).getValue()!=null?configLoader.load().getNode(path).getValue():defaultValue;
    }

    public void setValue(Object value, String... path) throws IOException {
        ConfigurationNode node = configLoader.load();
        node.getNode(path).setValue(value);
        configLoader.save(node);
    }

    private void generateConfig() throws IOException {
        ProxyUtils.getInstance().getLogger().info("Generating config...");

        final ConfigurationNode conf = configLoader.load();

        conf.getNode("functions").getNode("resetTablistOnServerChange").setValue(true);
        conf.getNode("functions").getNode("maintenanceMode").setValue(false);
        conf.getNode("functions").getNode("maintenanceModeKickMessage").setValue("<red>The server is currently in maintenance mode. \n Please try again later.");
        conf.getNode("security").getNode("disableProxyCommandTab").setValue(false);
        configLoader.save(conf);

        ProxyUtils.getInstance().getLogger().info("Config generated!");
    }

}
