package lol.bai.datagen.gradle.impl;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lol.bai.datagen.gradle.api.DatagenExtension;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

public class DatagenExtensionImpl implements DatagenExtension {

    private final Project project;
    private final Set<String> mods = new HashSet<>();

    private SourceSet sourceSet;
    private File output;
    private boolean useDefaultSourceSet;
    private boolean server;
    private boolean client;
    private boolean dev;
    private boolean reports;
    private boolean validate;

    public DatagenExtensionImpl(Project project) {
        this.project = project;
        this.useDefaultSourceSet = true;
        project.getExtensions().configure(SourceSetContainer.class, it -> {
            SourceSet main = it.getByName("main");
            sourceSet = it.maybeCreate("datagen");
            sourceSet.setCompileClasspath(sourceSet.getCompileClasspath()
                .plus(main.getCompileClasspath())
                .plus(main.getOutput()));
            sourceSet.setRuntimeClasspath(sourceSet.getRuntimeClasspath()
                .plus(main.getRuntimeClasspath())
                .plus(main.getOutput()));
        });

        setServer(true);
        setClient(true);
    }

    @Override
    public Set<String> getMods() {
        return mods;
    }

    @Override
    public void mods(String... mods) {
        this.mods.addAll(Arrays.asList(mods));
    }

    @Override
    public void all() {
        setServer(true);
        setClient(true);
        setDev(true);
        setReports(true);
        setValidate(true);
    }

    @Override
    public void setServer(boolean server) {
        this.server = server;
    }

    @Override
    public boolean isServer() {
        return server;
    }

    @Override
    public void setClient(boolean client) {
        this.client = client;
    }

    @Override
    public boolean isClient() {
        return client;
    }

    @Override
    public void setDev(boolean dev) {
        this.dev = dev;
    }

    @Override
    public boolean isDev() {
        return dev;
    }

    @Override
    public void setReports(boolean reports) {
        this.reports = reports;
    }

    @Override
    public boolean isReports() {
        return reports;
    }

    @Override
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Override
    public boolean isValidate() {
        return validate;
    }

    @Override
    public SourceSet getSourceSet() {
        return sourceSet;
    }

    @Override
    public void setSourceSet(SourceSet sourceSet) {
        if (useDefaultSourceSet) {
            useDefaultSourceSet = false;
            project.getExtensions().configure(SourceSetContainer.class, it ->
                it.remove(this.sourceSet));
        }
        this.sourceSet = sourceSet;
    }

    @Override
    public File getOutput() {
        if (output == null) {
            project.getExtensions().configure(SourceSetContainer.class, it ->
                it.getByName("main").getResources().srcDir("src/generated/resources"));
            this.output = project.file("src/generated/resources");
        }
        return output;
    }

    @Override
    public void setOutput(File output) {
        this.output = output;
    }

}
