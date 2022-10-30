package de.sage.utils.commds;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.sage.utils.ProxyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SendUserCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length >= 2) {
            switch (args[0].toLowerCase()) {
                case "player" -> {
                    if (doPlayerValidation(args[1])) {
                        if (doPlayerValidation(args[2])) {
                            Player player = ProxyUtils.getInstance().getProxy().getPlayer(args[1]).get();
                            Player target = ProxyUtils.getInstance().getProxy().getPlayer(args[2]).get();
                            player.createConnectionRequest(target.getCurrentServer().get().getServer()).fireAndForget();
                            source.sendMessage(Component.text("Successfully sent player " + args[1] + " to " + args[2] + "."));
                        } else if (doServerValidation(args[2])) {
                            Player player = ProxyUtils.getInstance().getProxy().getPlayer(args[1]).get();
                            player.createConnectionRequest(ProxyUtils.getInstance().getProxy().getServer(args[2]).get()).fireAndForget();
                            source.sendMessage(Component.text("Successfully sent player " + args[1] + " to " + args[2] + "."));
                        } else {
                            source.sendMessage(Component.text("The server or player " + args[2] + " does not exist.").color(NamedTextColor.RED));
                        }
                    } else
                        source.sendMessage(Component.text("The player " + args[1] + " does not exist.").color(NamedTextColor.RED));
                }
                case "all" -> {
                    if (doServerValidation(args[1])) {
                        ProxyUtils.getInstance().getProxy().getAllPlayers().forEach(player -> player.createConnectionRequest(ProxyUtils.getInstance().getProxy().getServer(args[1]).get()).fireAndForget());
                        source.sendMessage(Component.text("Successfully sent all players to " + args[1] + "."));
                    } else
                        source.sendMessage(Component.text("The server " + args[1] + " does not exist.").color(NamedTextColor.RED));
                }

                case "server" -> {
                    if (args.length == 3) {
                        if (doServerValidation(args[1])) {
                            if (doServerValidation(args[2])) {
                                ProxyUtils.getInstance().getProxy().getServer(args[1]).get().getPlayersConnected().forEach(player -> player.createConnectionRequest(ProxyUtils.getInstance().getProxy().getServer(args[2]).get()).fireAndForget());
                                source.sendMessage(Component.text("Successfully moved all players from " + args[1] + " to " + args[2] + "!").color(NamedTextColor.GREEN));
                            } else
                                source.sendMessage(Component.text("The server " + args[2] + " does not exist.").color(NamedTextColor.RED));
                        } else
                            source.sendMessage(Component.text("The server " + args[1] + " does not exist.").color(NamedTextColor.RED));
                    } else
                        source.sendMessage(Component.text("Please use /send server <server> <target>").color(NamedTextColor.RED));
                }

                case "current" -> {
                    if (source instanceof Player player) {
                        if (doServerValidation(args[1])) {
                            player.getCurrentServer().get().getServer().getPlayersConnected().forEach(online -> online.createConnectionRequest(ProxyUtils.getInstance().getProxy().getServer(args[1]).get()).fireAndForget());
                            source.sendMessage(Component.text("Successfully moved all players from " + args[1] + " to " + args[2] + "!").color(NamedTextColor.GREEN));
                        } else
                            source.sendMessage(Component.text("The server " + args[1] + " does not exist.").color(NamedTextColor.RED));
                    } else
                        source.sendMessage(Component.text("You must be a player to use this command!").color(NamedTextColor.RED));
                }

                default -> source.sendMessage(Component.text("Unknown sub-command!").color(NamedTextColor.RED));
            }
        } else
            source.sendMessage(Component.text("Usage: /send sub-command", NamedTextColor.RED));
    }

    // This method allows you to control who can execute the command.
    // If the executor does not have the required permission,
    // the execution of the command and the control of its autocompletion
    // will be sent directly to the server on which the sender is located
    @Override
    public boolean hasPermission(final Invocation invocation) {
        if(invocation.arguments().length >= 1){
            if (invocation.arguments()[0].equalsIgnoreCase("all")) {
                return invocation.source().hasPermission("utils.command.send.all");
            } else if (invocation.arguments()[0].equalsIgnoreCase("player")) {
                return invocation.source().hasPermission("utils.command.send.player");
            } else if (invocation.arguments()[0].equalsIgnoreCase("current")) {
                return invocation.source().hasPermission("utils.command.send.current");
            } else if (invocation.arguments()[0].equalsIgnoreCase("server")) {
                return invocation.source().hasPermission("utils.command.send.server");
            }
        }

        return invocation.source().hasPermission("utils.command.send");
    }

    // Here you can offer argument suggestions in the same way as the previous method,
    // but asynchronously. It is recommended to use this method instead of the previous one
    // especially in cases where you make a more extensive logic to provide the suggestions
    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        if (invocation.arguments().length == 0) {
            return CompletableFuture.completedFuture(Arrays.asList("player", "all", "current", "server"));

        } else if (invocation.arguments().length == 1) {
            if (invocation.arguments()[0].equalsIgnoreCase("player")) {
                return CompletableFuture.completedFuture(ProxyUtils.getInstance().getProxy().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList()));
            } else if (invocation.arguments()[0].equalsIgnoreCase("server") || invocation.arguments()[0].equalsIgnoreCase("all")) {
                return CompletableFuture.completedFuture(ProxyUtils.getInstance().getProxy().getAllServers().stream().map(server -> server.getServerInfo().getName()).collect(Collectors.toList()));
            } else if (invocation.arguments()[0].equalsIgnoreCase("current")) {
                return CompletableFuture.completedFuture(ProxyUtils.getInstance().getProxy().getAllServers().stream().map(server -> server.getServerInfo().getName()).collect(Collectors.toList()));
            }
        } else if (invocation.arguments().length == 2) {
            if (invocation.arguments()[0].equalsIgnoreCase("server")) {
                return CompletableFuture.completedFuture(ProxyUtils.getInstance().getProxy().getAllServers().stream().map(server -> server.getServerInfo().getName()).collect(Collectors.toList()));
            } else if (invocation.arguments()[0].equalsIgnoreCase("player")) {
                List<String> options = new ArrayList<>();
                ProxyUtils.getInstance().getProxy().getAllPlayers().stream().map(Player::getUsername).forEach(name -> options.add(name));
                ProxyUtils.getInstance().getProxy().getAllServers().stream().map(server -> server.getServerInfo().getName()).forEach(name -> options.add(name));
                return CompletableFuture.completedFuture(options);
            }
        }

        return CompletableFuture.completedFuture(Collections.emptyList());
    }


    /**
     * Checks if the server exists
     *
     * @param serverName The name of the server to check
     * @return true if the server exists
     */
    private boolean doServerValidation(String serverName) {
        return ProxyUtils.getInstance().getProxy().getAllServers().stream().anyMatch(server -> server.getServerInfo().getName().equalsIgnoreCase(serverName));
    }

    /**
     * Checks if the player in online on the proxy
     *
     * @param playerName The name of the player to check
     * @return true if the player is online
     */
    private boolean doPlayerValidation(String playerName) {
        return ProxyUtils.getInstance().getProxy().getAllPlayers().stream().anyMatch(player -> player.getUsername().equalsIgnoreCase(playerName));
    }
}
