package lol.bai.datagen.impl.mixin;

import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateDefinitionProvider;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateDefinitionProvider.class)
public class MixinBlockStateDefinitionProvider {

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "method_25738", at = @At("HEAD"), cancellable = true)
    private static void disableErrorForModdedBlocks(Map<?, ?> map, Block block, CallbackInfoReturnable<Boolean> cir) {
        if (!Registry.BLOCK.getId(block).getNamespace().equals("minecraft")) {
            cir.setReturnValue(false);
        }
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "method_25741", at = @At("HEAD"), cancellable = true)
    private static void disableModdedBlockItemModelsGeneration(Set<?> set, Map<?, ?> map, Block block, CallbackInfo ci) {
        if (!Registry.BLOCK.getId(block).getNamespace().equals("minecraft")) {
            ci.cancel();
        }
    }

}
