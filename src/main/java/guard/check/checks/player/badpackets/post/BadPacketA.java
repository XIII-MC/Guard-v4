package guard.check.checks.player.badpackets.post;

import guard.check.GuardCategory;
import guard.check.GuardCheck;
import guard.check.GuardCheckInfo;
import guard.check.GuardCheckState;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

@GuardCheckInfo(name = "BadPacket A", category = GuardCategory.Player, state = GuardCheckState.Testing, addBuffer = 0, removeBuffer = 0, maxBuffer = 0)
public class BadPacketA extends GuardCheck {

    public void onPacket(PacketPlayReceiveEvent packet) {
        if (packet.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) {
            boolean isPost = isPost(packet.getPacketId(), (byte) -100);
            if (isPost) fail(packet, "Post packet", "ARM_ANIMATION");
            if (isPost) packet.setCancelled(true);
        }
    }

}