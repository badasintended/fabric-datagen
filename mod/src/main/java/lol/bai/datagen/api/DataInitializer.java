package lol.bai.datagen.api;

import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.NotNull;

public interface DataInitializer {

    void onInitializeData(@NotNull DataGenerator generator, @NotNull GeneratorOptions options);

}
