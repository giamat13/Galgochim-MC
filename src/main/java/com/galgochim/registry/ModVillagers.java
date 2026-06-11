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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.VillagerTrades;

/**
 * Doron Fishler (דורון פישלר): a villager who lives by his microphone and sells
 * Shmamba, Shmilki, the book "I can explain", and the odd Fishland Anthem disc.
 */
public final class ModVillagers {
    private ModVillagers() {
    }

    public static final ResourceKey<PoiType> DORON_POI_KEY =
            ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Galgochim.id("doron_fishler"));
    public static final ResourceKey<VillagerProfession> DORON_KEY =
            ResourceKey.create(Registries.VILLAGER_PROFESSION, Galgochim.id("doron_fishler"));

    public static void register() {
        // The microphone block is Doron's job site (point of interest).
        PoiType poi = new PoiType(
                ImmutableSet.copyOf(ModBlocks.MICROPHONE.getStateDefinition().getPossibleStates()), 1, 1);
        Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, DORON_POI_KEY, poi);

        Int2ObjectMap<VillagerTrades.ItemListing[]> trades = new Int2ObjectOpenHashMap<>();
        trades.put(1, new VillagerTrades.ItemListing[]{
                sell(ModItems.SHMAMBA, 1, 1, 16, 2),
                sell(ModItems.SHMILKI, 1, 2, 16, 2),
                sell(ModItems.BOOK_ANI_YACHOL_LEHASBIR, 1, 5, 8, 5),
        });
        trades.put(2, new VillagerTrades.ItemListing[]{
                sell(ModItems.MUSIC_DISC_FISHLAND_ANTHEM, 1, 8, 4, 10),
        });

        VillagerProfession doron = new VillagerProfession(
                Component.translatable("entity.galgochim.villager.doron_fishler"),
                (Holder<PoiType> holder) -> holder.is(DORON_POI_KEY),
                (Holder<PoiType> holder) -> holder.is(DORON_POI_KEY),
                ImmutableSet.of(),
                ImmutableSet.of(),
                SoundEvents.VILLAGER_WORK_CARTOGRAPHER,
                trades);
        Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, DORON_KEY, doron);
    }

    /** A simple "N emeralds -> item" trade listing. */
    private static VillagerTrades.ItemListing sell(Item result, int count, int emeralds, int maxUses, int xp) {
        return (entity, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, emeralds), new ItemStack(result, count), maxUses, xp, 0.05f);
    }
}
