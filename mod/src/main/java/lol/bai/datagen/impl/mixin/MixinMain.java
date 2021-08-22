package lol.bai.datagen.impl.mixin;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import lol.bai.datagen.api.DataInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.main.Main;
import net.minecraft.data.DataGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MixinMain {

    @Inject(method = "main", at = @At("HEAD"), cancellable = true)
    private static void main(String[] args, CallbackInfo ci) {
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        OptionSpec<Void> enabledSpec = optionParser.accepts("datagen");
        OptionSpec<String> dirSpec = optionParser.accepts("dir").withRequiredArg();
        OptionSet optionSet = optionParser.parse(args);
        if (!optionSet.has(enabledSpec)) {
            return;
        }

        SharedConstants.createGameVersion();

        Path dirPath = Paths.get(dirSpec.value(optionSet));

        DataGenerator generator = new DataGenerator(dirPath, Collections.singleton(dirPath));
        FabricLoader.getInstance()
            .getEntrypoints("bai:datagen", DataInitializer.class)
            .forEach(i -> i.onInitializeData(generator));

        try {
            generator.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ci.cancel();
    }

}
