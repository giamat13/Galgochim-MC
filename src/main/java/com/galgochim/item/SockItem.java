package com.galgochim.item;

import java.util.Objects;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyedItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * A single sock (גרב). Two socks of the same colour can be turned back into a
 * pair of leather boots: hold one and use it while a matching sock is somewhere
 * in your inventory.
 */
public class SockItem extends Item {

    public SockItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }
        DyedItemColor color = held.get(DataComponents.DYED_COLOR);

        // Two socks in the same stack make a pair directly.
        if (held.getCount() >= 2) {
            held.shrink(2);
            giveBoots(player, color);
            return InteractionResult.SUCCESS;
        }

        // Otherwise look for a matching sock elsewhere in the inventory.
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack other = inv.getItem(i);
            if (other != held && other.is(this)
                    && Objects.equals(color, other.get(DataComponents.DYED_COLOR))) {
                held.shrink(1);
                other.shrink(1);
                giveBoots(player, color);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private static void giveBoots(Player player, DyedItemColor color) {
        ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
        if (color != null) {
            boots.set(DataComponents.DYED_COLOR, color);
        }
        player.addItem(boots);
    }
}
