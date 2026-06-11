package com.galgochim.block;

import com.galgochim.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A washing machine. When it is full, right-clicking it pops out 64 laundry
 * powder. The {@code FILLED} state is what the alien ship looks at: a full
 * machine standing under open sky is "bait", and a machine with a roof over
 * it is safe from being emptied.
 */
public class WashingMachineBlock extends Block {

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    public WashingMachineBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FILLED, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (state.getValue(FILLED)) {
            if (!level.isClientSide()) {
                player.addItem(new ItemStack(ModItems.LAUNDRY_POWDER, 64));
                level.setBlock(pos, state.setValue(FILLED, false), Block.UPDATE_ALL);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
