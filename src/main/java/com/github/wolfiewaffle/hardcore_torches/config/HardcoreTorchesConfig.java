package com.github.wolfiewaffle.hardcore_torches.config;

import com.github.wolfiewaffle.hardcore_torches.Mod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collection;

@Config(name = "hardcore_torches")
public class HardcoreTorchesConfig implements ConfigData {
    @Comment("Torches will extinguish if broken. default true")
    public boolean torchesExtinguishWhenBroken = true;

    @Comment("Torches are fully expended when broken. Overrides torchesExtinguishWhenBroken. default true")
    public boolean torchesBurnWhenDropped = true;

    @Comment("Torches become unlit in rain. If torchesSmolder is true, they will instead smolder in rain. default true")
    public boolean torchesRain = true;

    @Comment("Only matters if torchesRain is true. In rain, torches will extinguish but emit smoke, and consume fuel at 1/3 the rate until fully extinguished or re-lit. default true")
    public boolean torchesSmolder = true;

    @Comment("Burnt torches drop as vanilla stick when broken instead of a burnt torch. default true")
    public boolean burntStick = true;

    @Comment("The amount of ticks the torch lasts. Default is 48000. 20 ticks per second, 48000 = 20 minutes")
    public int defaultTorchFuel = 48000;

    @Comment("Are torches crafted unlit. default false")
    public boolean craftUnlit = false;

    @Comment("Are torches crafted unlit. default false")
    public boolean unlightInChest = false;

//    @Comment("How many torches are crafted. default 4")
//    public int craftAmount = 4;

    @Comment("Items which can be used to light a torch for free")
    public String[] freeLightItems = new String[] {
            "minecraft:torch",
            "minecraft:lava_bucket",
            "hardcore_torches:lit_torch"
    };

    @Comment("Items which can be used to light a torch but take a durability point")
    public String[] damageLightItems = new String[] {
            "minecraft:flint_and_steel"
    };

    @Comment("Items which consume one to light a torch")
    public String[] consumeLightItems = new String[] {
            "minecraft:fire_charge"
    };

    @Comment("Items that can extinguish a torch for free. the fuel is not expended")
    public String[] freeExtinguishItems = new String[] {
            "minecraft:water_bucket"
    };

    @Comment("Blocks that can be right clicked with a torch to light the torch")
    public String[] worldLightBlocks = new String[] {
            "minecraft:torch",
            "minecraft:soul_torch",
            "minecraft:fire",
            "minecraft:soul_fire",
            "minecraft:lantern",
            "minecraft:soul_lantern",
            "minecraft:jack_o_lantern",
            "minecraft:campfire",
            "minecraft:soul_campfire",
            "minecraft:magma_block",
            "hardcore_torches:lit_torch",
            "hardcore_torches:smoldering_torch"
    };

    public static ArrayList<Item> getItems (String[] list) {
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < list.length; i++) {
            Item item = Registry.ITEM.get(new Identifier(list[i]));

            // If it was not recognized, print an error
            if (item == Items.AIR && !list[i].equals("minecraft:air") && !list[i].equals("air")) {
                Mod.LOGGER.error("an invalid item was detected in config file: " + list[i]);
            }

            items.add(item);
        }

        return items;
    }

    public static ArrayList<Block> getBlocks (String[] list) {
        ArrayList<Block> blocks = new ArrayList<>();

        for (int i = 0; i < list.length; i++) {
            Block block = Registry.BLOCK.get(new Identifier(list[i]));

            // If it was not recognized, print an error
            if (block == Blocks.AIR && !list[i].equals("minecraft:air") && !list[i].equals("air")) {
                Mod.LOGGER.error("an invalid item was detected in config file: " + list[i]);
            }

            blocks.add(block);
        }

        return blocks;
    }
}