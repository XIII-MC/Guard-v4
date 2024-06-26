package guard.exempt;

import guard.data.GuardPlayer;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.function.Function;

@Getter
public enum ExemptType {
    TELEPORT(gp -> gp.isTeleporting || System.currentTimeMillis() - gp.joined < 2000L),
    AIR(gp -> gp.inAir),
    GROUND(gp -> gp.onSolidGround && gp.to.getY() % 0.015625 == 0.0),
    PACKET_GROUND(gp -> gp.playerGround),
    TPS(gp -> gp.getTPS() > 18.5D),
    JOINED(gp -> System.currentTimeMillis() - gp.joined < 5000L),
    TRAPDOOR(gp -> gp.nearTrapdoor),
    CHUNK(gp -> !gp.getPlayer().getWorld().isChunkLoaded(gp.getPlayer().getLocation().getBlockX() << 4, gp.getPlayer().getLocation().getBlockZ() << 4)),
    STEPPED(gp -> gp.playerGround && gp.motionY > 0),
    SLAB(gp -> gp.isOnSlab),
    STAIRS(gp -> gp.isOnStair),
    WEB(gp -> gp.inWeb),
    CLIMBABLE(gp -> gp.onClimbable),
    SLIME(gp -> gp.sinceSlimeTicks < 30),
    NEAR_VEHICLE(gp -> gp.nearBoat),
    INSIDE_VEHICLE(gp -> System.currentTimeMillis() - gp.lastNearBoat < 20),
    LIQUID(gp -> gp.isInLiquid),
    FULL_LIQUID(gp -> gp.isInFullLiquid),
    BLOCK_ABOVE(gp -> gp.blockAbove),
    PISTON(gp -> gp.nearPiston),
    VOID(gp -> gp.getPlayer().getLocation().getY() < 4),
    DEPTH_STRIDER(gp -> gp.getDepthStriderLevel() > 0),
    SPECTATE(gp -> gp.player.getGameMode().equals(GameMode.SPECTATOR)),
    FLYING(gp -> System.currentTimeMillis() - gp.lastFlyingTime < 3000L),
    VELOCITY(gp -> System.currentTimeMillis() - gp.entityHit < 800L || System.currentTimeMillis() - gp.lastVelocity < 800L),
    HEALTH_CHANGE(gp -> gp.lastHealth > gp.getPlayer().getHealth()),
    GLIDE(gp -> System.currentTimeMillis() - gp.lastGlide < 4000),
    ICE(gp -> System.currentTimeMillis() - gp.lastIce < 2000),
    RESPAWN(gp -> System.currentTimeMillis() - gp.wasDead < 300L),
    SWIMMING(gp -> PacketEvents.get().getPlayerUtils().getClientVersion(gp.player).isNewerThanOrEquals(ClientVersion.v_1_13) && gp.getPlayer().isSwimming()),
    SOULSAND(gp -> gp.onSoulSand),
    PLACE(gp -> System.currentTimeMillis() - gp.lastBlockPlaced < 600L);

    private final Function<GuardPlayer, Boolean> exception;

    ExemptType(final Function<GuardPlayer, Boolean> exception) {
        this.exception = exception;
    }

}
