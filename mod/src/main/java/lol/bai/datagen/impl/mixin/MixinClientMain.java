package lol.bai.datagen.impl.mixin;

import java.io.IOException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MixinClientMain {

    @Inject(method = "main", at = @At("HEAD"), cancellable = true)
    private static void main(String[] args, CallbackInfo ci) throws IOException {
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        OptionSpec<Void> enabledSpec = optionParser.accepts("datagen");
        OptionSet optionSet = optionParser.parse(args);
        if (optionSet.has(enabledSpec)) {
            net.minecraft.data.Main.main(args);
            ci.cancel();
        }
    }

}
