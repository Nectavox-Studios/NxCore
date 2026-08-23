package com.nectavox.nxcore.sign;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import com.nectavox.nxcore.NxPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SignInputPacketListener extends PacketListenerAbstract {
    private final NxPlugin plugin;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.UPDATE_SIGN) return;

        Object rawPlayer = event.getPlayer();
        if (!(rawPlayer instanceof Player player)) return;

        WrapperPlayClientUpdateSign wrapper = new WrapperPlayClientUpdateSign(event);
        Vector3i pos = wrapper.getBlockPosition();
        Location location = new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ());

        SignUtil.PendingInput input = SignUtil.getInstance().findAndRemove(location);
        if (input == null) return;

        event.setCancelled(true);

        String[] lines = wrapper.getTextLines();
        String value = (lines != null && lines.length > 0 && lines[0] != null) ? lines[0] : "";

        input.restore();

        player.getScheduler().run(plugin, task -> input.callback().accept(value), null);
    }
}