package com.github.wolfiewaffle.hardcore_torches.util;

import com.github.wolfiewaffle.hardcore_torches.block.HardcoreFloorTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreWallTorchBlock;
import net.minecraft.block.Block;

import java.util.HashMap;

public class TorchGroup {

    private HashMap<ETorchState, HardcoreFloorTorchBlock> standingTorches = new HashMap<ETorchState, HardcoreFloorTorchBlock>();
    private HashMap<ETorchState, HardcoreWallTorchBlock> wallTorches = new HashMap<ETorchState, HardcoreWallTorchBlock>();

    public TorchGroup() {

    }

    public void add(Block block) {
        if (block instanceof HardcoreFloorTorchBlock) {
            add((HardcoreFloorTorchBlock) block);
        } else if (block instanceof  HardcoreWallTorchBlock) {
            add((HardcoreWallTorchBlock) block);
        }
    }

    public void add(HardcoreFloorTorchBlock block) {
        standingTorches.put(block.burnState, block);
        block.group = this;
    }

    public void add(HardcoreWallTorchBlock block) {
        wallTorches.put(block.burnState, block);
        block.group = this;
    }

    public HardcoreFloorTorchBlock getStandingTorch(ETorchState state) {
        return standingTorches.get(state);
    }

    public HardcoreWallTorchBlock getWallTorch(ETorchState state) {
        return wallTorches.get(state);
    }
}
