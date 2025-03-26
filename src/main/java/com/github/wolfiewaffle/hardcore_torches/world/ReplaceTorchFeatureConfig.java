//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.wolfiewaffle.hardcore_torches.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record ReplaceTorchFeatureConfig(int number, Identifier blockId) implements FeatureConfig {
  public static final Codec<ReplaceTorchFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codecs.POSITIVE_INT.fieldOf("number").forGetter(ReplaceTorchFeatureConfig::number), Identifier.CODEC.fieldOf("blockID").forGetter(ReplaceTorchFeatureConfig::blockId)).apply(instance, ReplaceTorchFeatureConfig::new));

  public ReplaceTorchFeatureConfig(int number, Identifier blockId) {
    this.number = number;
    this.blockId = blockId;
  }

  public int number() {
    return this.number;
  }

  public Identifier blockId() {
    return this.blockId;
  }
}
