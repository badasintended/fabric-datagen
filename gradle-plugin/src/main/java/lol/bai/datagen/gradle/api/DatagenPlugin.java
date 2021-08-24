package lol.bai.datagen.gradle.api;

import java.util.Objects;
import java.util.Scanner;
import lol.bai.datagen.gradle.impl.DatagenExtensionImpl;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.tasks.Jar;

public class DatagenPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        DatagenExtension extension = project.getExtensions()
            .create(DatagenExtension.class, "datagen", DatagenExtensionImpl.class, project);

        Scanner scanner = new Scanner(Objects.requireNonNull(DatagenPlugin.class.getClassLoader().getResourceAsStream("modversion")), "UTF-8");
        String modVersion = scanner.nextLine();
        scanner.close();

        project.getRepositories().maven(maven -> {
            maven.setName("badasintended");
            maven.setUrl(project.uri("https://maven.bai.lol"));
        });

        project.getTasks().named("jar", Jar.class, jar ->
            jar.exclude(".cache/*")
        );

        project.getPluginManager().withPlugin("fabric-loom", a -> {
            String mod = "lol.bai.fabric-datagen:mod:" + modVersion;
            project.getDependencies().add("modCompileOnly", mod);
            project.getDependencies().add("modRuntime", mod);
            project.afterEvaluate(b ->
                project.getExtensions().configure(LoomGradleExtensionAPI.class, loom ->
                    loom.runs(runs ->
                        runs.create("data", data -> {
                            data.client();
                            data.ideConfigGenerated(false);
                            data.source(extension.getSourceSet());
                            data.getProgramArgs().clear();

                            String dir = "\"" + extension.getOutput().getAbsolutePath() + "\"";
                            data.programArgs("--datagen", "--input=" + dir, "--output=" + dir);

                            StringBuilder mods = new StringBuilder();
                            for (String dataMod : extension.getMods()) {
                                mods.append(dataMod);
                                mods.append(';');
                            }
                            data.programArg("--mod=" + mods);
                            if (extension.isServer()) data.programArg("--server");
                            if (extension.isClient()) data.programArg("--client");
                            if (extension.isDev()) data.programArg("--dev");
                            if (extension.isReports()) data.programArg("--reports");
                            if (extension.isValidate()) data.programArg("--validate");
                        })
                    )
                )
            );
        });
    }

}
