package guard.check.checks.player.badpackets;

import guard.check.GuardCategory;
import guard.check.GuardCheck;
import guard.check.GuardCheckInfo;
import guard.check.GuardCheckState;
import guard.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;
import org.bukkit.GameMode;

@GuardCheckInfo(name = "BadPacket J", category = GuardCategory.Player, state = GuardCheckState.STABLE, addBuffer = 1, removeBuffer = 0, maxBuffer = 1)
public class BadPacketJ extends GuardCheck {

    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.ABILITIES) {
            WrappedPacketInAbilities wrapper = new WrappedPacketInAbilities(packet.getNMSPacket());
            boolean exempt = isExempt(ExemptType.FLYING);
            if(((wrapper.isFlying() && !gp.getPlayer().isFlying() && !gp.getPlayer().getAllowFlight()) || (!gp.getPlayer().getAllowFlight() && wrapper.isFlightAllowed().isPresent() ? wrapper.isFlightAllowed().get() : true)) && !exempt && gp.player.getGameMode() != GameMode.CREATIVE && gp.player.getGameMode() != GameMode.SPECTATOR) fail(packet, "Impossible packet", "§9ABILITIES");
            if(wrapper.isFlying() || gp.getPlayer().isFlying() || gp.getPlayer().getAllowFlight()) removeBuffer();
        }
    }

}
