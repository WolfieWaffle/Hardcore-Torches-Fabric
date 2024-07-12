package com.github.wolfiewaffle.hardcore_torches.util;

import com.github.wolfiewaffle.hardcore_torches.block.AbstractLanternBlock;
import net.minecraft.block.Block;

import java.util.HashMap;

public class LanternGroup {

    private HashMap<Boolean, AbstractLanternBlock> lanterns = new HashMap<>();
    public final String name;

    public LanternGroup(String name) {
        this.name = name;
    }

    public void add(Block block) {
        if (block instanceof AbstractLanternBlock) {
            add((AbstractLanternBlock) block);
        }
    }

    public void add(AbstractLanternBlock block) {
        lanterns.put(block.isLit, block);
        block.group = this;
    }

    public AbstractLanternBlock getLanternBlock(boolean lit) {
        return lanterns.get(lit);
    }
}
