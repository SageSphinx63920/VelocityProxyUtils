package de.sage.utils.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import de.sage.utils.ProxyUtils;

import java.io.IOException;

public class TabResetEvent {

    @Subscribe(order = PostOrder.EARLY)
    public void onPlayerChat(ServerConnectedEvent event) throws IOException {
        Player player = event.getPlayer();
        if((Boolean) ProxyUtils.getInstance().getConfig().getOption(true, "functions", "resetTablistOnServerChange")){
            player.getTabList().clearHeaderAndFooter();
        }
    }
}
