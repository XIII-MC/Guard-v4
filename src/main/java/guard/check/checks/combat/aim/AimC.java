package guard.check.checks.combat.aim;

import guard.check.GuardCategory;
import guard.check.GuardCheck;
import guard.check.GuardCheckInfo;
import guard.check.GuardCheckState;
import guard.utils.MathUtils;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.bukkit.Bukkit;

@GuardCheckInfo(name = "Aim C", category = GuardCategory.Combat, state = GuardCheckState.Testing, addBuffer = 1, removeBuffer = 1, maxBuffer = 4)
public class AimC extends GuardCheck {

    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.USE_ENTITY) {

            if(gp.deltaYaw != 0) {
                if (gp.deltaPitch == gp.lastDeltaPitch) fail(packet, "Repeated deltaPitch", "rpP=" + gp.deltaPitch);
                else removeBuffer();
            }
        }
    }
}
