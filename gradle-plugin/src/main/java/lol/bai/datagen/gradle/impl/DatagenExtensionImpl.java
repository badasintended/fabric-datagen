package lol.bai.datagen.gradle.impl;

import java.io.File;
import lol.bai.datagen.gradle.api.DatagenExtension;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

public class DatagenExtensionImpl implements DatagenExtension {

    private final Project project;
    private SourceSet sourceSet;
    private File output;
    private boolean useDefaultSourceSet;

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
