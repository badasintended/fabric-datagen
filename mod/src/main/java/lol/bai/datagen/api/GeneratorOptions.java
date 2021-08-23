package lol.bai.datagen.api;

import java.nio.file.Path;
import java.util.Collection;

public final class GeneratorOptions {

    public final Path output;
    public final Collection<Path> inputs;
    public final Collection<String> mods;
    public final boolean includeClient;
    public final boolean includeServer;
    public final boolean includeDev;
    public final boolean includeReports;
    public final boolean validate;

    public GeneratorOptions(Path output, Collection<Path> inputs, Collection<String> mods, boolean includeClient, boolean includeServer, boolean includeDev, boolean includeReports, boolean validate) {
        this.output = output;
        this.inputs = inputs;
        this.mods = mods;
        this.includeClient = includeClient;
        this.includeServer = includeServer;
        this.includeDev = includeDev;
        this.includeReports = includeReports;
        this.validate = validate;
    }

}
