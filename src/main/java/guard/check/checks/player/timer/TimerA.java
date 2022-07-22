package guard.check.checks.player.timer;

import guard.check.GuardCategory;
import guard.check.GuardCheck;
import guard.check.GuardCheckInfo;
import guard.check.GuardCheckState;
import guard.utils.SampleList;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

@GuardCheckInfo(name = "Timer A", category = GuardCategory.Player, state = GuardCheckState.Testing, addBuffer = 0, removeBuffer = 0, maxBuffer = 0)
public class TimerA extends GuardCheck {
    double bal;
    double lastBal;
    long lastMS;
    long joinTime;
    boolean wasFirst;
    SampleList<Long> balls = new SampleList<>(3);

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        //if(packet.getPacketId() == PacketType.Play.Client.POSITION || packet.getPacketId() == PacketType.Play.Client.POSITION_LOOK || packet.getPacketId() == PacketType.Play.Client.LOOK || packet.getPacketId() == PacketType.Play.Client.FLYING) {
        long now = System.currentTimeMillis();
        long rate = (System.currentTimeMillis() - lastMS);
        bal += 50 - rate;
        if(!wasFirst) {
            wasFirst = true;
            joinTime = now;
        }
        if(now - joinTime < 3000) {
            bal = -40;
        }
        if(String.valueOf(bal).contains("E")) {
            bal = 0;
        }
        if(Math.abs(rate) >= 48 && Math.abs(rate) <= 52 && bal < -10) {
            balls.add(rate);
            if(balls.isCollected()) {
                if (Math.abs(balls.getAverageLong(balls) - Math.abs(rate)) < 20) {
                    bal = -10;
                }
            }
        }
        if(bal > 12) {
            fail(packet, "Sent too many Move Packets", bal);
            bal = 0;
        } else removeBuffer();
        lastMS = now;
        lastBal = bal;
        //}
    }

    @Override
    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.FLYING || packet.getPacketId() == PacketType.Play.Client.LOOK) {
            long now = System.currentTimeMillis();
            double rate = (System.currentTimeMillis() - lastMS);
            bal += 50 - rate;
            if(!wasFirst) {
                wasFirst = true;
                joinTime = now;
            }
            if(now - joinTime < 6000) {
                bal = -40;
            }
            if(bal > 12) {
                fail(packet, "Sent too many Move Packets", bal);
                bal = 0;
            }else removeBuffer();
            lastMS = now;
        }
    }


    public void onPacketSend(PacketPlaySendEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Server.POSITION) {
            bal -= 50;
        }
    }
}