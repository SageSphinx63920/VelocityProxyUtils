package de.sage.utils.commds;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.sage.utils.ProxyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class IpCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 1) {
            if (ProxyUtils.getInstance().getProxy().getPlayer(args[0]).isPresent()) {
                Player player = ProxyUtils.getInstance().getProxy().getPlayer(args[0]).get();
                source.sendMessage(Component.text("The player " + args[0] + " has online with the IP " + player.getRemoteAddress().getAddress().getHostAddress() + " and has a Ping of " + player.getPing() + ".").color(NamedTextColor.GREEN));
            } else
                source.sendMessage(Component.text("The player " + args[0] + " is not online.").color(NamedTextColor.RED));
        } else
            source.sendMessage(Component.text("Usage: /ip <Player>", NamedTextColor.RED));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("utils.command.ip");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        if (invocation.arguments().length == 1) {
            return CompletableFuture.completedFuture(ProxyUtils.getInstance().getProxy().getAllPlayers().stream().map(Player::getUsername).toList());
        } else
            return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
