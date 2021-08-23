package lol.bai.datagen.impl.mixin;

import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockLootTableGenerator.class)
public class MixinBlockLootTableGenerator {

    @ModifyVariable(
        method = "accept",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/Block;getLootTableId()Lnet/minecraft/util/Identifier;"))
    private Identifier disableErrorForModdedBlocks(Identifier original) {
        return original.getNamespace().equals("minecraft") ? original : LootTables.EMPTY;
    }

}
