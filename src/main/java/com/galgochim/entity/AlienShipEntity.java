package com.galgochim.entity;

import java.util.List;

import com.galgochim.block.WashingMachineBlock;
import com.galgochim.registry.ModBlocks;
import com.galgochim.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * The alien ship. It drifts over at night and vacuums up your laundry powder
 * (only if you are standing under open sky). There is a 25% chance it also
 * snatches half of a pair of leather boots, leaving you with a single sock.
 */
public class AlienShipEntity extends Mob {

    private Vec3 heading = new Vec3(0.4, 0.0, 0.0);
    private int lifetime = 2400;

    public AlienShipEntity(EntityType<? extends AlienShipEntity> type, Level level) {
        super(type, level);
        this.setNoAi(true);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    protected void registerGoals() {
        // No AI - it drifts in a straight line (see tick).
    }

    public void setHeading(Vec3 dir) {
        this.heading = dir;
    }

    @Override
    public void tick() {
        super.tick();
        this.setPos(this.getX() + this.heading.x, this.getY() + this.heading.y, this.getZ() + this.heading.z);

        if (this.level() instanceof ServerLevel server) {
            if (this.tickCount % 40 == 0) {
                this.abduct(server);
            }
            if (--this.lifetime <= 0) {
                this.discard();
            }
        }
    }

    /** Steal laundry powder (and maybe a boot) from nearby players under open sky. */
    private void abduct(ServerLevel server) {
        AABB area = this.getBoundingBox().inflate(24.0, 64.0, 24.0);
        List<Player> players = server.getEntitiesOfClass(Player.class, area);
        for (Player player : players) {
            if (player.isCreative() || player.isSpectator()) {
                continue;
            }
            // The roof protects you: only abducts powder when you stand under sky.
            if (!server.canSeeSky(player.blockPosition())) {
                continue;
            }
            boolean tookSomething = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.is(ModItems.LAUNDRY_POWDER)) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                    tookSomething = true;
                }
            }
            // 25% chance: turn half of a pair of leather boots into a single sock.
            if (this.random.nextFloat() < 0.25f) {
                tookSomething |= this.bootToSock(player);
            }
            // Empty any unroofed washing machines standing near the player.
            tookSomething |= this.emptyMachines(server, player.blockPosition());
            if (tookSomething) {
                server.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE, 1.0f, 2.0f);
            }
        }
    }

    /** Empty nearby full washing machines, unless they are protected by a roof. */
    private boolean emptyMachines(ServerLevel server, BlockPos center) {
        boolean any = false;
        int r = 8;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-r, -r, -r), center.offset(r, r, r))) {
            BlockState state = server.getBlockState(pos);
            if (state.is(ModBlocks.WASHING_MACHINE) && state.getValue(WashingMachineBlock.FILLED)
                    && server.canSeeSky(pos.above())) {
                server.setBlock(pos.immutable(), state.setValue(WashingMachineBlock.FILLED, false),
                        Block.UPDATE_ALL);
                any = true;
            }
        }
        return any;
    }

    private boolean bootToSock(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(Items.LEATHER_BOOTS)) {
                ItemStack sock = new ItemStack(ModItems.SOCK);
                DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
                if (color != null) {
                    sock.set(DataComponents.DYED_COLOR, color);
                }
                stack.shrink(1);
                player.getInventory().setItem(i, stack);
                player.addItem(sock);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
