package com.github.wolfiewaffle.hardcore_torches.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

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

    @Comment("The amount of ticks the lantern can last. Default is 144000. 20 ticks per second, 144000 = 60 minutes")
    public int defaultLanternFuel = 144000;

    @Comment("The amount a fuel item adds to the lantern by default. default 72000")
    public int defLanternFuelItem = 72000;

    @Comment("A lantern must have at least this much fuel (min 1) to be ignited from unlit. Once lit it will continue to burn to 0. default 1")
    public int minLanternIgnitionFuel = 1;

    @Comment("Are torches crafted unlit. default false")
    public boolean craftUnlit = false;

    @Comment("Do torches become unlit when placed in storage. default false")
    public boolean unlightInChest = false;

    @Comment("Do torches lose fuel while the player has then in their inventory. default false")
    public boolean tickInInventory = false;

    @Comment("How many torches are crafted. default 4")
    public int craftAmount = 4;

    @Comment("Right click torch or lantern to see fuel value. default false")
    public boolean fuelMessage = false;

    @Comment("Max fuel an oil can holds. default 576000")
    public int maxCanFuel = 576000;

    @Comment("Do lanterns have to be filled using an oil can. default true")
    public boolean lanternsNeedCan = true;

    @Comment("Can you refuel a torch using an oil can. default false")
    public boolean torchesUseCan = false;

    @Comment("Multiplies the fuel value of all oil can recipes. 0.5 makes all fuel recipes return half as much fuel. default 1")
    public float oilRecipeMultiplier = 1;

    @Comment("Overrides the oil can fuel recipe if set. default -1")
    public int oilRecipeOverride = -1;

    @Comment("If true, you can craft animal fat with an oil can to fill it. You can also add custom fill recipes with a datapack, open the mod jar to see the JSON format. default true")
    public boolean enableFatOil = true;

    @Comment("If true, you can craft coal with an oil can to fill it. You can also add custom fill recipes with a datapack, open the mod jar to see the JSON format. default false")
    public boolean enableCoalOil = false;

    @Comment("If true, you can right click torches to extinguish them while not holding fuel or a torch to light. default false")
    public boolean handUnlightTorch = false;

    @Comment("If true, you can right click lanterns to extinguish them while not holding fuel or a torch to light. default false")
    public boolean handUnlightLantern = false;

    @Comment("0: When going underwater, torches in your inventory will be unaffected\n1: When going underwater, torches in mainhand or offhand will be extinguished\n2: When going underwater, torches in inventory will be extinguished. default 2")
    public int invExtinguishInWater = 2;

    @Comment("0: When in rain, torches in your inventory will be unaffected\n1: When in rain, torches in mainhand or offhand will be extinguished or smolder\n2: When in rain, torches in inventory will be extinguished or smolder. default 2")
    public int invExtinguishInRain = 2;

    @Comment("Can the fire starter light campfires. default true")
    public boolean starterLightCampfires = true;

    @Comment("Can the fire starter light torches. default true")
    public boolean starterLightTorches = true;

    @Comment("Can the fire starter create full-block fires. default true")
    public boolean starterStartFires = true;

    @Comment("Can the fire starter light lanterns. default true")
    public boolean starterLightLanterns = false;

    @Comment("Percentage chance that the fire starter works. default 0.33")
    public float starterSuccessChance = 0.33f;

    @Comment("Default fuel that a lantern starts with when crafted. default 0")
    public int startingLanternFuel = 0;

    @Comment("Allow the player to pick up lanterns with sneak-clicking. default true")
    public boolean pickUpLanterns = true;

    @Comment("Max campfire fuel. default 24000")
    public int maxCampfireFuel = 24000;

    @Comment("Duration for fuel items in campfire is furnace burn time, times this number. default 8.0")
    public double campfireFuelFactor = 8.0;

    @Comment("Should hardcore campfires be crafted? If false, this essentially disables the campfire part of this mod.")
    public boolean craftHardcoreCampfire = true;
}