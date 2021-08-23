package lol.bai.datagen.impl.mixin;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import lol.bai.datagen.api.DataInitializer;
import lol.bai.datagen.api.GeneratorOptions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Main.class)
public class MixinDataMain {

    private static OptionSpec<String> modSpec;
    private static List<String> mods;

    @Inject(
        method = "main",
        locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(value = "INVOKE", target = "Ljoptsimple/OptionParser;parse([Ljava/lang/String;)Ljoptsimple/OptionSet;"))
    private static void addSpecs(String[] args, CallbackInfo ci, OptionParser optionParser) {
        optionParser.allowsUnrecognizedOptions();
        optionParser.accepts("datagen");
        modSpec = optionParser.accepts("mod", "Target mod").withRequiredArg().withValuesSeparatedBy(';');
    }

    @Inject(
        method = "main",
        locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(value = "INVOKE_ASSIGN", target = "Ljoptsimple/OptionParser;parse([Ljava/lang/String;)Ljoptsimple/OptionSet;"))
    private static void parse(String[] args, CallbackInfo ci, OptionParser optionParser, OptionSpec<Void> optionSpec, OptionSpec<Void> optionSpec2, OptionSpec<Void> optionSpec3, OptionSpec<Void> optionSpec4, OptionSpec<Void> optionSpec5, OptionSpec<Void> optionSpec6, OptionSpec<Void> optionSpec7, OptionSpec<String> optionSpec8, OptionSpec<String> optionSpec9, OptionSet optionSet) {
        mods = optionSet.valuesOf(modSpec);
    }

    @Inject(method = "create", at = @At("TAIL"), cancellable = true)
    private static void create(Path output, Collection<Path> inputs, boolean includeClient, boolean includeServer, boolean includeDev, boolean includeReports, boolean validate, CallbackInfoReturnable<DataGenerator> cir) {
        FabricLoader loader = FabricLoader.getInstance();

        loader.getEntrypoints("main", ModInitializer.class)
            .forEach(ModInitializer::onInitialize);

        DataGenerator generator = mods.contains("minecraft")
            ? cir.getReturnValue()
            : new DataGenerator(output, inputs);
        GeneratorOptions options = new GeneratorOptions(output, inputs, mods, includeClient, includeServer, includeDev, includeReports, validate);

        loader.getEntrypointContainers("bai:datagen", DataInitializer.class)
            .stream()
            .filter(it -> mods.contains(it.getProvider().getMetadata().getId()))
            .forEach(it -> it.getEntrypoint().onInitializeData(generator, options));

        cir.setReturnValue(generator);
    }

}
