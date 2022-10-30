package de.sage.utils.commds;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.sage.utils.ProxyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AlertCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length >= 2) {
            switch (args[0].toLowerCase()) {
                case "chat" -> {
                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        message.append(args[i]).append(" ");
                    }
                    ProxyUtils.getInstance().getProxy().getAllPlayers().forEach(player -> player.sendMessage(ProxyUtils.getInstance().getMiniMessage().deserialize(message.toString().trim())));
                }
                case "title" -> {
                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    ProxyUtils.getInstance().getProxy().getAllPlayers().forEach(player -> player.showTitle(Title.title(ProxyUtils.getInstance().getMiniMessage().deserialize(message.toString().trim()), Component.empty())));
                }

                case "actionbar" -> {
                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    ProxyUtils.getInstance().getProxy().getAllPlayers().forEach(player -> player.sendActionBar(ProxyUtils.getInstance().getMiniMessage().deserialize(message.toString().trim())));
                }

                default -> source.sendMessage(Component.text("Unknown sub-command!").color(NamedTextColor.RED));
            }
        } else
            source.sendMessage(Component.text("Usage: /alert sub-command <Message>", NamedTextColor.RED));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        if(invocation.arguments().length >= 1){
            if (invocation.arguments()[0].equalsIgnoreCase("chat")) {
                return invocation.source().hasPermission("utils.command.alert.chat");
            } else if (invocation.arguments()[0].equalsIgnoreCase("title")) {
                return invocation.source().hasPermission("utils.command.alert.title");
            } else if (invocation.arguments()[0].equalsIgnoreCase("actionbar")) {
                return invocation.source().hasPermission("utils.command.alert.actionbar");
            }
        }

        return invocation.source().hasPermission("utils.command.alert");
    }


    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        if(invocation.arguments().length == 0){
            return CompletableFuture.completedFuture(Arrays.asList("chat", "title", "actionbar"));
        }else
            return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
