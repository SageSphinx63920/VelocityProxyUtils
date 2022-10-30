package de.sage.utils.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;

public class TabResetEvent {

    @Subscribe(order = PostOrder.EARLY)
    public void onPlayerChat(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        player.getTabList().clearHeaderAndFooter();
    }

}
