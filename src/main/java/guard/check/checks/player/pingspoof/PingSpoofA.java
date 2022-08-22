package guard.check.checks.player.pingspoof;

import guard.check.GuardCategory;
import guard.check.GuardCheck;
import guard.check.GuardCheckInfo;
import guard.check.GuardCheckState;
import guard.utils.SampleList;
import guard.utils.packet.TransactionPacketClient;
import guard.utils.packet.TransactionPacketServer;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

@GuardCheckInfo(name = "PingSpoof A", category = GuardCategory.Player, state = GuardCheckState.Testing, addBuffer = 1, removeBuffer = 1, maxBuffer = 1)
public class PingSpoofA extends GuardCheck {

    long lastTransaction;
    double bal;
    int id;
    boolean checked;
    boolean wasKeepalive;
    SampleList<Integer> transactionPing = new SampleList<>(10, true);

    public void onTransactionSend(TransactionPacketServer packet) {
        if(!checked) {
            lastTransaction = packet.getTimeStamp();
            id = packet.getId();
            checked = true;
        }

    }

    public void onTransaction(TransactionPacketClient packet, boolean found) {
       if(id == packet.getId() && checked) {
           transactionPing.add((int) (packet.getTimeStamp() - lastTransaction));
           if(transactionPing.isCollected()) {
               int diff = gp.ping - transactionPing.getAverageInt(transactionPing);
                if(diff > 70 && wasKeepalive) {
                    fail(null, "Spoofed Ping", "transaction=" + transactionPing.getAverageInt(transactionPing) + " keepalive=" + gp.ping);
                    wasKeepalive = false;
                }
                if(wasKeepalive) {
                    wasKeepalive = false;
                    removeBuffer();
                }
               debug("transactionPing=" + transactionPing.getAverageInt(transactionPing) + " keepalivePing=" + gp.ping + " buffer=" + buffer);
           }
           checked = false;

       }

    }


    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.KEEP_ALIVE) {
            wasKeepalive = true;
        }
    }

    /*
    if(gp.getPing() != 0)
            bal += gp.getPing() - (System.currentTimeMillis() - lastTransaction);
        if(gp.getPing() > (System.currentTimeMillis() - lastTransaction)) {
            debug("bal=" + bal + " ping=" + gp.getPing() + " transactionPing=" + (System.currentTimeMillis() - lastTransaction));
            if(bal > 20) {

                //bal = 0;
            }
        }
     */
}
