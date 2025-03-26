//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.util;

import com.github.wolfiewaffle.hardcore_torches.block.HardcoreFloorTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.block.HardcoreWallTorchBlock;
import java.util.HashMap;
import net.minecraft.block.Block;

public class TorchGroup {
  private HashMap<ETorchState, HardcoreFloorTorchBlock> standingTorches = new HashMap();
  private HashMap<ETorchState, HardcoreWallTorchBlock> wallTorches = new HashMap();
  public final String name;

  public TorchGroup(String name) {
    this.name = name;
  }

  public void add(Block block) {
    if (block instanceof HardcoreFloorTorchBlock) {
      this.add((HardcoreFloorTorchBlock)block);
    } else if (block instanceof HardcoreWallTorchBlock) {
      this.add((HardcoreWallTorchBlock)block);
    }

  }

  public void add(HardcoreFloorTorchBlock block) {
    this.standingTorches.put(block.burnState, block);
    block.group = this;
  }

  public void add(HardcoreWallTorchBlock block) {
    this.wallTorches.put(block.burnState, block);
    block.group = this;
  }

  public HardcoreFloorTorchBlock getStandingTorch(ETorchState state) {
    return (HardcoreFloorTorchBlock)this.standingTorches.get(state);
  }

  public HardcoreWallTorchBlock getWallTorch(ETorchState state) {
    return (HardcoreWallTorchBlock)this.wallTorches.get(state);
  }
}
