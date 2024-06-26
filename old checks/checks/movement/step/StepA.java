package guard.check.checks.movement.step;

import guard.check.GuardCategory;
import guard.check.GuardCheck;
import guard.check.GuardCheckInfo;
import guard.check.GuardCheckState;
import guard.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@GuardCheckInfo(name = "Step A", category = GuardCategory.Movement, state = GuardCheckState.STABLE, addBuffer = 1, removeBuffer = 0, maxBuffer = 1)
public class StepA extends GuardCheck {
    int airTicks;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastMotionX, double lastMotionY, double lastMotionZ, float deltaYaw, float deltaPitch, float lastDeltaYaw, float lastDeltaPitch) {
        boolean exempt = isExempt(ExemptType.TELEPORT, ExemptType.BLOCK_ABOVE, ExemptType.CLIMBABLE, ExemptType.FLYING, ExemptType.GLIDE, ExemptType.PLACE, ExemptType.NEAR_VEHICLE, ExemptType.INSIDE_VEHICLE, ExemptType.FLYING, ExemptType.PISTON, ExemptType.STAIRS, ExemptType.SLAB, ExemptType.WEB, ExemptType.VELOCITY);
        if(!exempt) {
            if (motionY > 0) {
                if (airTicks < 4) {
                    airTicks++;
                }
            } else {
                if (motionY == 0 && gp.playerGround) {
                    if (airTicks < 4 && airTicks != 0 && airTicks > 0) {
                        if(!gp.onLowBlock && !isExempt(ExemptType.STAIRS) && !isExempt(ExemptType.SLAB)) fail(packet, "Stepped up a block wrongly", "aT §9" + airTicks);
                    } else {
                        if (airTicks != 0 && airTicks > 0)
                            removeBuffer();
                    }
                    airTicks = 0;
                }
            }
        }
        if(exempt || gp.onLowBlock) {
            airTicks -= 2;
            removeBuffer();
        }

    }

}
