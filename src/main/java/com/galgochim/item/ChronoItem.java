package com.galgochim.item;

import com.galgochim.registry.ModEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * The "chrono-thing" (כרונומשהו) dropped by the Hagit spaceship.
 *
 * Using it always costs 5 health, with a 25% chance to lose an extra 3-20
 * health on top of that, and a 10% chance to die on the spot. It also hands you
 * a Shmilki that fizzles away a second later.
 */
public class ChronoItem extends Item {

    public ChronoItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            RandomSource random = player.getRandom();
            DamageSource source = player.damageSources().magic();
            if (random.nextFloat() < 0.10f) {
                // 10%: instant death.
                player.hurt(source, Float.MAX_VALUE);
            } else {
                float damage = 5.0f; // base 5 health points (2.5 hearts)
                if (random.nextFloat() < 0.25f) {
                    damage += 3.0f + random.nextInt(18); // +3..20 extra
                }
                player.hurt(source, damage);
            }
            // You also get a Shmilki that fizzles away ~1 second later.
            if (player instanceof ServerPlayer serverPlayer) {
                ModEvents.giveVanishingShmilki(serverPlayer);
            }
        }
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
