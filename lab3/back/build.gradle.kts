import org.gradle.process.internal.ExecException
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    application
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

val altDir = layout.projectDirectory.dir("alt-src")
val functionalTestsDir = layout.projectDirectory.dir("src/functionals")

sourceSets {
    val alt by creating {
        kotlin.srcDir(altDir)
        java.srcDir(altDir)
        resources.srcDir(sourceSets.main.get().resources.srcDirs)
    }

    val functionalTestSource by creating {
        kotlin.srcDir(functionalTestsDir.dir("kotlin"))
        resources.srcDir("src/functionalTest/resources")
        compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath.get()
        runtimeClasspath += output + compileClasspath

    }
    // Расширяем зависимости
    configurations[functionalTestSource.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
    configurations[functionalTestSource.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())
}

group = "com.maxim"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.hibernate:hibernate-core:6.1.7.Final")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-security")
//    Swagger enable in http://localhost:8080/swagger-ui.html
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    testImplementation("org.seleniumhq.selenium:selenium-java:4.20.0")

}

val teamDirectory = "${buildDir.absolutePath}/team"

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}


var md5: String = ""
var sha1: String = ""
tasks.register("generateChecksums") {
    val outputDir = layout.buildDirectory.file("checksums")

    inputs.files(sourceSets.main.get().allSource)
    outputs.dir(outputDir)

    doLast {
        val md5Digest = MessageDigest.getInstance("MD5")
        val sha1Digest = MessageDigest.getInstance("SHA-1")

        sourceSets.main.get().allSource.forEach { file ->
            if (file.isFile) {
                file.readBytes().let { bytes ->
                    md5Digest.update(bytes)
                    sha1Digest.update(bytes)
                }
            }
        }

        md5 = md5Digest.digest().joinToString("") { "%02x".format(it) }
        sha1 = sha1Digest.digest().joinToString("") { "%02x".format(it) }

        println(md5)

    }
}

tasks.jar {
    dependsOn("generateChecksums")
    manifest {
        attributes["Main-Class"] = "com.maxim.back.BackendKt"

        attributes (
            "Main-Class" to "com.maxim.back.BackendKt",
            "SHA1-Checksum" to sha1,
            "MD5-Checksum" to md5
        )


    }
    // Копирует зависимости в JAR (опционально, для fat JAR)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.register("music") {
    group = "laboratory"
    dependsOn(tasks.classes)

    doLast {
        try {
            val audioInputStream: AudioInputStream = AudioSystem.getAudioInputStream(File("directedBy.wav"))
            val clip: Clip = AudioSystem.getClip()

            clip.open(audioInputStream)
            clip.start()

            Thread.sleep(clip.microsecondLength / 1000)

            clip.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

tasks.register("xml") {
    dependsOn(tasks.build)

    doLast {
        var invalidFiles: Int = 0
        val factory = DocumentBuilderFactory.newInstance()
        Files.walk(project.rootDir.toPath())
            .filter { Files.isRegularFile(it) }
            .filter { it.toString().endsWith(".xml") }
            .forEach {
                try {
                    val builder = factory.newDocumentBuilder()
                    val doc = builder.parse(it.toFile())
                    println("File $it is valid")
                } catch (e: Exception) {
                    invalidFiles++;
                    println("File $it is not valid")
                }
            }

        if (invalidFiles > 0) {
            throw GradleException("Found $invalidFiles  invalid .xml files")
        }
    }
}

tasks.register("scp") {
    group = "laboratory"
    dependsOn(tasks.jar)

    doLast {

        println("Executing scp")
        println("sshpass -p YPWB+9433 scp -P 2222 ${tasks.jar.get().archiveFile.get().asFile.absolutePath} s409664@helios.cs.ifmo.ru:/home/studs/s409664/scp_files")

        project.exec {
            commandLine(
                "sshpass",
                "-p",
                "YPWB+9433",
                "scp",
                "-P",
                "2222",
                tasks.jar.get().archiveFile.get().asFile.absolutePath,
                "s409664@helios.cs.ifmo.ru:/home/studs/s409664/scp_files"
            )
        }
    }
}


var branch: String = "master"
tasks.register("team") {
    group = "laboratory"
    dependsOn(tasks.build)


    val directory = File(teamDirectory)


    fun parseCommits(): List<String> {
        val output = ByteArrayOutputStream();
        project.exec {
            commandLine("git", "log", "--oneline")
            standardOutput = output
        }
        return output.toString().split("\n")
            .map { it.substringBefore(" ") }
            .take(4)
    }

    fun parseHead(): String {
        val output = ByteArrayOutputStream();
        project.exec {
            commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
            standardOutput = output
        }
        return output.toString().trim()
    }

    doLast {

        if (!directory.exists()) {
            directory.mkdirs()
        }

        branch = parseHead()
        parseCommits().forEach { commit ->
            println("Checkout to commit:$commit")
            project.exec {
                commandLine("git", "checkout", commit)
            }

            println("Executing jar")
            project.exec {
                commandLine("./gradlew", "jar")
            }

            println("Copying files to ")
            Files.copy(
                tasks.jar.get().archivePath.toPath(),
                Paths.get(teamDirectory, "${branch}-${commit}.jar"),
                StandardCopyOption.REPLACE_EXISTING
            )
        }
    }
}

tasks.register<Zip>("teamZip") {
    group = "laboratory"

    from(teamDirectory)
    into(teamDirectory)
    archiveFileName.set("team.zip")

    doLast {
        println("Created zip")
        println(archiveFile.get().asFile.toPath())
    }

}

tasks.register("restoreBranch") {
    group = "laboratory"

    doLast {
        project.exec {
            commandLine("git", "checkout", branch)
        }
    }
}

tasks.named("team").get().finalizedBy(tasks.named("teamZip"))
tasks.named("teamZip").get().finalizedBy(tasks.named("restoreBranch"))


tasks.register<Copy>("renameAndCopySources") {
    group = "helping"
    description = "Renames identifiers..."

    val renamingMap: Map<String, String> = mapOf(Pair("Backend", "Back"))

    from(sourceSets.main.get().allSource.srcDirs) {
        include("**/*.java", "**/*.kt")
    }

    val dest = layout.projectDirectory.dir("alt-src")
    into(dest)


    filter { line: String ->
        var result = line
        renamingMap.forEach { (old, newOne) ->
            result = result.replace(old, newOne)
        }
        result
    }

    rename { filename ->
        var result = filename
        renamingMap.forEach { (old, newOne) ->
            result = result.replace(old, newOne)
        }
        println("Renamed $filename to $result")
        result
    }

    doLast {
        println("[DEBUG] Task finished. Check output in ${destinationDir}")
    }
}

tasks.named("compileAltKotlin").get().dependsOn(tasks.named("renameAndCopySources"))

tasks.register<Jar>("alt") {
    group = "laboratory"
    dependsOn("compileAltKotlin")

    archiveBaseName.set("alternative-version")
    archiveVersion.set("1.0")

    from(tasks.named("compileAltKotlin").get())

    from(sourceSets.getByName("alt").resources) {
        into("resources")
    }

    manifest {
        attributes(
            "Implementation-Title" to "Alternative Version",
            "Implementation-Version" to archiveVersion.get(),
            "Main-Class" to "com.max.BackendKt"
        )
    }

    doLast {
        println("Alternative Kotlin JAR created at: ${archiveFile.get()}")
    }
}

tasks.register("history") {
    group = "laboratory"

    fun gitCommand(command: String): String? {
        val output = ByteArrayOutputStream()
        try {
            project.exec {
                commandLine("git", *command.split(" ").toTypedArray())
                standardOutput = output
            }
        } catch (e: Exception) {
            logger.error("Failed to execute git command: $command", e)
            return null
        }
        return output.toString()
    }

    fun tryBuild(): Boolean {
        return try {
            val baos = ByteArrayOutputStream()
            project.exec {
                commandLine("./gradlew", "clean", "build")
                standardOutput = baos
                errorOutput = baos
            }
            true
        } catch (e: ExecException) {
            false
        }
    }

    fun rollbackAndBuild() {
        val commits = gitCommand("rev-list HEAD")?.lines() ?: return
        var lastSuccessfulCommit: String? = null
        val currentlyCheckedOutCommit = gitCommand("rev-parse HEAD")!!.trim()

        gitCommand("add .")
        gitCommand("commit -m temp")

        for (commit in commits) {
            gitCommand("checkout $commit")
            if (tryBuild()) {
                lastSuccessfulCommit = commit
                println("Successful build at commit: $commit")
                break
            } else {
                println("Failed build at commit: $commit")
            }
        }

        if (lastSuccessfulCommit == null) {
            println("No successful build found.")
        } else {
            val diff = gitCommand("diff $currentlyCheckedOutCommit $lastSuccessfulCommit")
            val diffFile = project.file("build/successful_build_diff.txt")
            diffFile.writeText(diff ?: "No diff available.")
            println("Diff between $currentlyCheckedOutCommit and $lastSuccessfulCommit has been saved to ${diffFile.path}")
        }
    }

    doLast {
        try {
            println("Running build task...")
            val baos = ByteArrayOutputStream()
            project.exec {
                commandLine("./gradlew", "build")
                standardOutput = baos
                errorOutput = baos
            }

            println("Build was successful or already up to date. No rollback needed.")
        } catch (e: Exception) {
            println("Build failed. Attempting to rollback...")
            rollbackAndBuild()
        } finally {

            println("Task 'history' completed successfully in reporting.")
        }
    }
}

tasks.register("testEnvironmentUp") {
    group = "helping"
    dependsOn(tasks.bootJar)

    doLast {
        println("creating functional tests environment")

        val npmCommand = listOf("npm", "--prefix", "/home/maxim/IdeaProjects/opi-3/front", "run", "dev")
        val npmProcess = ProcessBuilder(npmCommand)
            .directory(project.projectDir)
            .start()


        println("Creating docker container")
        val dockerCommand = listOf("docker", "compose", "up", "-d")
        val dockerProcess = ProcessBuilder(dockerCommand)
            .directory(project.projectDir)
            .start()

        println("Starting docker with code ${dockerProcess.info()}")

        println("Starting java")
        val cmd = listOf(
            "bash", "-c",
            "nohup java -jar ${tasks.bootJar.get().archiveFile.get().asFile.absolutePath} > output.log 2>&1 &"
        )
        val process = ProcessBuilder(cmd)
            .directory(project.projectDir)
            .start().waitFor()
    }
}

tasks.register("testEnvironmentDown") {
    group = "helping"

    doLast {
        val dockerDown = listOf("docker", "compose", "down")
        val dockerProcess = ProcessBuilder(dockerDown)
            .directory(project.projectDir)
            .start().waitFor()

        val killJava = listOf("pkill", "-f", tasks.jar.get().archiveFile.get().asFile.name)
        val killJavaProccess = ProcessBuilder(killJava)
            .directory(project.projectDir)
            .start().waitFor()

        val killNode = listOf("pkill", "-f", "node")
        val killNodeProccess = ProcessBuilder(killJava)
            .directory(project.projectDir)
            .start().waitFor()

        val killChromium = listOf("pkill", "-f", "snap/chromium")
        val killChromiumProccess = ProcessBuilder(killJava)
            .directory(project.projectDir)
            .start().waitFor()
    }
}

tasks.register<Test>("functionalTests") {
    group = "laboratory"
    dependsOn("testEnvironmentUp")

    testClassesDirs = sourceSets["functionalTestSource"].output.classesDirs
    classpath = sourceSets["functionalTestSource"].runtimeClasspath
}

tasks.named("functionalTests").get().finalizedBy("testEnvironmentDown")
