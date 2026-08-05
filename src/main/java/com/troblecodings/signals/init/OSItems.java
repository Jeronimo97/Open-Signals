package com.troblecodings.signals.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.troblecodings.core.NBTWrapper;
import com.troblecodings.opensignals.linkableapi.Linkingtool;
import com.troblecodings.opensignals.linkableapi.MultiLinkingTool;
import com.troblecodings.signals.OpenSignalsMain;
import com.troblecodings.signals.blocks.Signal;
import com.troblecodings.signals.items.ItemArmorTemplate;
import com.troblecodings.signals.items.Placementtool;
import com.troblecodings.signals.items.SignalBridgeItem;
import com.troblecodings.signals.items.ToolParser;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public final class OSItems {

    private OSItems() {
    }

    public static final Linkingtool LINKING_TOOL = new Linkingtool((world, pos) -> {
        final BlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();
        final boolean isRedstoneBlock = block == OSBlocks.REDSTONE_IN
                || block == OSBlocks.REDSTONE_OUT || block == OSBlocks.COMBI_REDSTONE_INPUT;
        return isRedstoneBlock || (block instanceof Signal && ((Signal) block).canBeLinked())
                || block == OSBlocks.SIGNAL_BOX;
    }, _u -> true, (level, pos, tag) -> {
        final BlockState state = level.getBlockState(pos);
        final NBTWrapper wrapper = new NBTWrapper(tag);
        wrapper.putString(pos.toShortString(),
                ForgeRegistries.BLOCKS.getKey(state.getBlock()).getPath());
    });
    public static final MultiLinkingTool MULTI_LINKING_TOOL = new MultiLinkingTool(
            (world, pos) -> {
                final BlockState state = world.getBlockState(pos);
                final Block block = state.getBlock();
                final boolean isRedstoneBlock = block == OSBlocks.REDSTONE_IN
                        || block == OSBlocks.REDSTONE_OUT || block == OSBlocks.COMBI_REDSTONE_INPUT;
                return isRedstoneBlock
                        || (block instanceof Signal && ((Signal) block).canBeLinked())
                        || block == OSBlocks.SIGNAL_BOX;
            }, _u -> true, (level, pos, tag) -> {
                final BlockState state = level.getBlockState(pos);
                final NBTWrapper wrapper = new NBTWrapper(tag);
                wrapper.putString(pos.toShortString(),
                        ForgeRegistries.BLOCKS.getKey(state.getBlock()).getPath());
            });
    public static final Item CONDUCTOR_TROWEL_GREEN = new Item(
            new Properties());
    public static final Item CONDUCTOR_TROWEL_RED = new Item(
            new Properties());
    public static final Item WARNING_FLAG = new Item(
            new Properties());
    public static final Item K_BOARD = new Item(new Properties());
    public static final Item L_BOARD = new Item(new Properties());
    public static final ItemArmorTemplate REFLECTIVE_HEAD = new ItemArmorTemplate(
            ItemArmorTemplate.REFLECTIVE_ARMOR_MATERIAL, ArmorItem.Type.HELMET);
    public static final ItemArmorTemplate REFLECTIVE_CHESTPLATE = new ItemArmorTemplate(
            ItemArmorTemplate.REFLECTIVE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE);
    public static final ItemArmorTemplate REFLECTIVE_PANTS = new ItemArmorTemplate(
            ItemArmorTemplate.REFLECTIVE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS);
    public static final ItemArmorTemplate REFLECTIVE_SHOES = new ItemArmorTemplate(
            ItemArmorTemplate.REFLECTIVE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS);
    public static final ItemArmorTemplate DISPATCHER_HEAD = new ItemArmorTemplate(
            ItemArmorTemplate.DISPATCHER_ARMOR_MATERIAL, ArmorItem.Type.HELMET);
    public static final ItemArmorTemplate DISPATCHER_CHESTPLATE = new ItemArmorTemplate(
            ItemArmorTemplate.DISPATCHER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE);
    public static final ItemArmorTemplate DISPATCHER_PANTS = new ItemArmorTemplate(
            ItemArmorTemplate.DISPATCHER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS);
    public static final ItemArmorTemplate DISPATCHER_SHOES = new ItemArmorTemplate(
            ItemArmorTemplate.DISPATCHER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS);
    public static final ItemArmorTemplate STATION_MANAGER_HEAD = new ItemArmorTemplate(
            ItemArmorTemplate.STATIONMANAGER_ARMOR_MATERIAL, ArmorItem.Type.HELMET);
    public static final ItemArmorTemplate STATION_MANAGER_CHESTPLATE = new ItemArmorTemplate(
            ItemArmorTemplate.STATIONMANAGER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE);
    public static final ItemArmorTemplate STATION_MANAGER_PANTS = new ItemArmorTemplate(
            ItemArmorTemplate.STATIONMANAGER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS);
    public static final ItemArmorTemplate STATION_MANAGER_SHOES = new ItemArmorTemplate(
            ItemArmorTemplate.STATIONMANAGER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS);
    public static final ItemArmorTemplate TRAIN_DRIVER_HEAD = new ItemArmorTemplate(
            ItemArmorTemplate.TRAINDRIVER_ARMOR_MATERIAL, ArmorItem.Type.HELMET);
    public static final ItemArmorTemplate TRAIN_DRIVER_CHESTPLATE = new ItemArmorTemplate(
            ItemArmorTemplate.TRAINDRIVER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE);
    public static final ItemArmorTemplate TRAIN_DRIVER_PANTS = new ItemArmorTemplate(
            ItemArmorTemplate.TRAINDRIVER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS);
    public static final ItemArmorTemplate TRAIN_DRIVER_SHOES = new ItemArmorTemplate(
            ItemArmorTemplate.TRAINDRIVER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS);
    public static final ItemArmorTemplate CONDUCTOR_HEAD = new ItemArmorTemplate(
            ItemArmorTemplate.CONDUCTOR_ARMOR_MATERIAL, ArmorItem.Type.HELMET);
    public static final ItemArmorTemplate CONDUCTOR_CHESTPLATE = new ItemArmorTemplate(
            ItemArmorTemplate.CONDUCTOR_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE);
    public static final ItemArmorTemplate CONDUCTOR_PANTS = new ItemArmorTemplate(
            ItemArmorTemplate.CONDUCTOR_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS);
    public static final ItemArmorTemplate CONDUCTOR_SHOES = new ItemArmorTemplate(
            ItemArmorTemplate.CONDUCTOR_ARMOR_MATERIAL, ArmorItem.Type.BOOTS);
    public static final Item SIGNAL_PLATE = new Item(
            new Properties());
    public static final Item SIGNAL_SHIELD = new Item(
            new Properties());
    public static final Item LAMPS = new Item(new Properties());
    public static final Item ELECTRIC_PARTS = new Item(
            new Properties());
    public static final Item MANIPULATOR = new Item(new Properties());
    public static final SignalBridgeItem SIGNAL_BRIDGE_ITEM = new SignalBridgeItem();

    public static ArrayList<Item> registeredItems = new ArrayList<>();

    public static ArrayList<Placementtool> placementtools = new ArrayList<>();

    /**
     * Registry names used to live on the item itself; {@code setRegistryName} was removed in 1.19,
     * so they are held here until {@link RegisterEvent} fires.
     */
    public static final Map<Item, ResourceLocation> ITEM_NAMES = new LinkedHashMap<>();

    /**
     * Items that sat in a vanilla creative tab through {@code Properties.tab(...)} on 1.18. 1.20.1
     * removed that, so {@link OSTabs} puts them back via BuildCreativeModeTabContentsEvent.
     */
    public static final Map<Item, ResourceKey<CreativeModeTab>> VANILLA_TAB_ITEMS =
            new LinkedHashMap<>();

    private static void assignVanillaTabs() {
        for (final Item item : new Item[] {
                CONDUCTOR_TROWEL_GREEN, CONDUCTOR_TROWEL_RED, WARNING_FLAG, K_BOARD, L_BOARD,
                REFLECTIVE_HEAD, REFLECTIVE_CHESTPLATE, REFLECTIVE_PANTS, REFLECTIVE_SHOES,
                DISPATCHER_HEAD, DISPATCHER_CHESTPLATE, DISPATCHER_PANTS, DISPATCHER_SHOES,
                STATION_MANAGER_HEAD, STATION_MANAGER_CHESTPLATE, STATION_MANAGER_PANTS,
                STATION_MANAGER_SHOES, TRAIN_DRIVER_HEAD, TRAIN_DRIVER_CHESTPLATE,
                TRAIN_DRIVER_PANTS, TRAIN_DRIVER_SHOES, CONDUCTOR_HEAD, CONDUCTOR_CHESTPLATE,
                CONDUCTOR_PANTS, CONDUCTOR_SHOES
        })
            VANILLA_TAB_ITEMS.put(item, CreativeModeTabs.COMBAT);
        for (final Item item : new Item[] {
                SIGNAL_PLATE, SIGNAL_SHIELD, LAMPS, ELECTRIC_PARTS
        })
            VANILLA_TAB_ITEMS.put(item, CreativeModeTabs.INGREDIENTS);
    }

    public static void init() {
        synchronized (LINKING_TOOL) {
            if (!registeredItems.isEmpty())
                return;
            final Field[] fields = OSItems.class.getFields();
            for (final Field field : fields) {
                final int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                        && Modifier.isPublic(modifiers)) {
                    final String name = field.getName().toLowerCase().replace("_", "");
                    try {
                        final Object object = field.get(null);
                        if (!(object instanceof Item))
                            continue;
                        final Item item = (Item) object;
                        ITEM_NAMES.put(item,
                                new ResourceLocation(OpenSignalsMain.MODID, name));
                        registeredItems.add(item);
                    } catch (final IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            assignVanillaTabs();
            loadTools();
        }
    }

    private static final Gson GSON = new Gson();

    private static void loadTools() {
        OpenSignalsMain.contentPacks.getFiles("tools").forEach(entry -> {
            final ToolParser tools = GSON.fromJson(entry.getValue(), ToolParser.class);
            tools.getPlacementTools().forEach(placementtool -> {
                final Placementtool tool = new Placementtool();
                final String name = placementtool.toLowerCase().replace("_", "").trim();
                ITEM_NAMES.put(tool, new ResourceLocation(OpenSignalsMain.MODID, name));
                placementtools.add(tool);
                registeredItems.add(tool);
            });
        });
    }

    @SubscribeEvent
    public static void registerItem(final RegisterEvent event) {
        event.register(Registries.ITEM, helper -> {
            OSItems.init();
            registeredItems.forEach(item -> helper.register(ITEM_NAMES.get(item), item));
        });
    }
}