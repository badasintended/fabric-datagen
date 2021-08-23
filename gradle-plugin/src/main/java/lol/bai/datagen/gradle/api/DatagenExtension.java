package lol.bai.datagen.gradle.api;

import java.io.File;
import java.util.Collection;
import org.gradle.api.tasks.SourceSet;

public interface DatagenExtension {

    Collection<String> getMods();

    void mods(String... mods);

    void all();

    void setServer(boolean server);

    boolean isServer();

    void setClient(boolean server);

    boolean isClient();

    void setDev(boolean server);

    boolean isDev();

    void setReports(boolean server);

    boolean isReports();

    void setValidate(boolean server);

    boolean isValidate();

    SourceSet getSourceSet();

    void setSourceSet(SourceSet sourceSet);

    File getOutput();

    void setOutput(File output);

}
