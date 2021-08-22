package lol.bai.datagen.gradle.api;

import java.io.File;
import org.gradle.api.tasks.SourceSet;

public interface DatagenExtension {

    SourceSet getSourceSet();

    void setSourceSet(SourceSet sourceSet);

    File getOutput();

    void setOutput(File output);

}
