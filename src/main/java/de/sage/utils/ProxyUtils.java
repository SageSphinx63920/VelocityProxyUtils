package de.sage.utils;

import com.google.gson.reflect.TypeToken;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import de.sage.utils.commds.*;
import de.sage.utils.util.ConfigUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

import de.sage.utils.events.TabResetEvent;

import java.io.IOException;
import java.nio.file.Path;

import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;
import com.google.inject.Inject;
import org.yaml.snakeyaml.DumperOptions;

@Plugin(id = "proxy-utils", name = "VelocityProxyUtils", version = "0.0.1-SNAPSHOT",
        url = "https://sagesphinx63920.dev", description = "Simple proxy command everybody needs.", authors = {"SageSphinx63920"})
public class ProxyUtils {

    private final @Getter ProxyServer proxy;
    private final @Getter Logger logger;
    private final Path dataDirectory;
    private static ProxyUtils instance;
    private @Getter ConfigUtil config;

    private final @Getter MiniMessage miniMessage = MiniMessage.builder().tags(
            TagResolver.builder()
                    .resolver(StandardTags.color())
                    .resolver(StandardTags.decorations())
                    .resolver(StandardTags.gradient())
                    .resolver(StandardTags.font())
                    .resolver(StandardTags.reset())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.translatable())
                    .build()
    ).build();

    @Inject
    public ProxyUtils(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) throws IOException {
        this.proxy = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        instance = this;

        config = new ConfigUtil(dataDirectory, "config.yml");


        if((Boolean) config.getOption(true, "functions", "maintenanceMode")) {
            logger.info("Maintenance mode is enabled!");
        }

        logger.info("Hello there! I made my first plugin with Velocity.");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        EventManager eventManager = proxy.getEventManager();
        CommandManager commandManager = proxy.getCommandManager();

        commandManager.register(commandManager.metaBuilder("send").aliases("senduser").plugin(this).build(), new SendUserCommand());
        commandManager.register(commandManager.metaBuilder("find").aliases("finduser").plugin(this).build(), new FindUserCommand());
        commandManager.register(commandManager.metaBuilder("ip").plugin(this).build(), new IpCommand());
        commandManager.register(commandManager.metaBuilder("jump").aliases("jumpto").plugin(this).build(), new JumpCommand());
        commandManager.register(commandManager.metaBuilder("alert").plugin(this).build(), new AlertCommand());

        eventManager.register(this, new TabResetEvent());
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        logger.info("Goodbye!");
    }

    public static ProxyUtils getInstance() {
        return instance;
    }

}
