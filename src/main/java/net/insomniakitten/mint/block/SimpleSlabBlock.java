package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

public class SimpleSlabBlock extends BlockSlab {
    public SimpleSlabBlock(final Block material) {
        super(Builder.from(material));
    }
}
