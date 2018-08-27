package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class SimpleStairsBlock extends BlockStairs {
    public SimpleStairsBlock(final Block material) {
        super(material.getDefaultState(), Builder.from(material));
    }
}
