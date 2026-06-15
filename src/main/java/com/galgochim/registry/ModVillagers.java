package com.galgochim.registry;

import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.galgochim.Galgochim;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;

/**
 * Doron Fishler (דורון פישלר): a villager whose job site is the microphone block.
 *
 * In MC 26.1.2 villager trades are fully data-driven: a profession maps each
 * level to a {@code ResourceKey<TradeSet>}, and the trade sets themselves are
 * defined as datapack JSON (registries {@code trade_set} / {@code villager_trade}).
 * The trade-set JSON lives under {@code data/galgochim/trade_set/} and
 * {@code data/galgochim/villager_trade/} (+ a tag in {@code tags/villager_trade/}).
 */
public final class ModVillagers {
    private ModVillagers() {
    }

    public static final ResourceKey<PoiType> DORON_POI_KEY =
            ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Galgochim.id("doron_fishler"));
    public static final ResourceKey<VillagerProfession> DORON_KEY =
            ResourceKey.create(Registries.VILLAGER_PROFESSION, Galgochim.id("doron_fishler"));

    private static final ResourceKey<TradeSet> DORON_TRADES_1 =
            ResourceKey.create(Registries.TRADE_SET, Galgochim.id("doron_level_1"));
    private static final ResourceKey<TradeSet> DORON_TRADES_2 =
            ResourceKey.create(Registries.TRADE_SET, Galgochim.id("doron_level_2"));

    public static void register() {
        // The microphone block is Doron's job site (point of interest).
        PoiType poi = new PoiType(
                ImmutableSet.copyOf(ModBlocks.MICROPHONE.getStateDefinition().getPossibleStates()), 1, 1);
        Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, DORON_POI_KEY, poi);

        // level -> trade set (defined as datapack JSON, see data/galgochim/trade_set).
        Int2ObjectMap<ResourceKey<TradeSet>> trades = new Int2ObjectOpenHashMap<>();
        trades.put(1, DORON_TRADES_1);
        trades.put(2, DORON_TRADES_2);

        VillagerProfession doron = new VillagerProfession(
                Component.translatable("entity.galgochim.villager.doron_fishler"),
                (Holder<PoiType> holder) -> holder.is(DORON_POI_KEY),
                (Holder<PoiType> holder) -> holder.is(DORON_POI_KEY),
                ImmutableSet.of(),
                ImmutableSet.of(),
                // No work sound - Doron should be quiet (no repetitive noise).
                (SoundEvent) null,
                trades);
        Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, DORON_KEY, doron);
    }
}
