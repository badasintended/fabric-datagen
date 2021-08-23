# fabric-datagen

A _hacky_ extension to [Fabric Loom](https://github.com/FabricMC/fabric-loom) that adds support for generating data at build time.

## Setup
1. Add badasintended maven repository to `settings.gradle`
   ```diff
   pluginManagement {
       repositories {
           maven {
               name = 'Fabric'
               url = 'https://maven.fabricmc.net/'
           }
   ++      maven {
   ++          name = 'badasintended'
   ++          url = 'https://maven.bai.lol/'
   ++      }
           gradlePluginPortal()
       }
   }
   ```
2. Add fabric-datagen into the plugins block on your `build.gradle`
   ```diff
   plugins {
   ++  id 'lol.bai.fabric-datagen' version %VERSION%
       id 'fabric-loom' version '0.9-SNAPSHOT'
       id 'maven-publish'
   }
   ```
3. On `datagen` source set, create a class that implements `DataInitializer`, install your providers there
   ```java
   public class ExampleData implements DataInitializer {
       @Override
       public void onInitializeData(DataGenerator generator) {
           generator.install(new BlockTagsProvider(generator));
       }
   }
   ```
4. Create a `fabric.mod.json` **in the `datagen` source set**, add your class to `bai:datagen` entrypoint
   ```json
   {
     "schemaVersion": 1,
     "id"           : "example-data",
     "version"      : "1",
     "entrypoints"  : {
       "bai:datagen": ["com.example.data.ExampleData"]
     }
   }
   ```
5. Add your data mod id to the `datagen` block on your `build.gradle`
   ```gradle
   datagen {
       mods("example-data")
   }
   ```
6. Run `runData` gradle task
7. Build your mod jar

## `.gitignore`
Most of the time you don't really need to include the generated files to the git history, 
so you can add it to the `.gitignore` file.
```gitignore
src/generated/
```
But, if you want to include those files, I recommend only ignoring the cache folder instead.
```gitignore
src/generated/resources/.cache/
```

## Configurations
You can specify the source set used and the output directory to your liking. 
To do that, add a `datagen` block to your `build.gradle`.
```gradle
datagen {
    sourceSet = sourceSets.main
    output = file('src/main/resources')
}
```
Though keep in mind that you need to add the main classpath if you use custom source set,
also the output won't be automatically included in the jar.
