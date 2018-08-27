package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;

public class SimplePillarBlock extends BlockRotatedPillar {
    public SimplePillarBlock(final Block material) {
        super(Builder.from(material));
    }
}
