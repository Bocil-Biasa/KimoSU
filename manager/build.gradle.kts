// Mod by Nixz • Dev
import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.api.AndroidBasePlugin
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.agp.lib) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.lsplugin.cmaker)
}

cmaker {
    default {
        arguments.addAll(
            arrayOf(
                "-DANDROID_STL=none",
            )
        )
        abiFilters("arm64-v8a", "x86_64", "riscv64")
    }
    buildTypes {
        if (it.name == "release") {
            arguments += "-DDEBUG_SYMBOLS_PATH=${layout.buildDirectory.asFile.get().absolutePath}/symbols"
        }
    }
}

val androidMinSdkVersion = 26
val androidTargetSdkVersion = 36
val androidCompileSdkVersion = 36
val androidBuildToolsVersion = "36.1.0"
val androidCompileNdkVersion by extra(libs.versions.ndk.get())
val androidSourceCompatibility = JavaVersion.VERSION_21
val androidTargetCompatibility = JavaVersion.VERSION_21
val managerVersionCode by extra(getVersionCode())
val managerVersionName by extra(getVersionName())

fun findGitBinary(): String {
    val candidates = listOf(
        "/data/data/com.termux/files/usr/bin/git",
        "/usr/bin/git",
        "/usr/local/bin/git",
        "git"
    )
    return candidates.firstOrNull { java.io.File(it).canExecute() } ?: "git"
}

fun getGitCommitCount(): Int {
    return try {
        val git = findGitBinary()
        val process = ProcessBuilder(git, "rev-list", "--count", "HEAD")
            .directory(rootDir)
            .start()
        val count = process.inputStream.bufferedReader().use { it.readText().trim() }.toIntOrNull()
        process.waitFor()
        count ?: 3452
    } catch (_: Exception) {
        3452
    }
}

fun getGitDescribe(): String {
    return try {
        val git = findGitBinary()
        val process = ProcessBuilder(git, "describe", "--tags", "--always")
            .directory(rootDir)
            .start()
        val desc = process.inputStream.bufferedReader().use { it.readText().trim() }
        process.waitFor()
        if (desc.isNotBlank() && !desc.startsWith("fatal")) desc else "v3.4.2"
    } catch (_: Exception) {
        "v3.4.2"
    }
}

fun getVersionCode(): Int {
    val commitCount = getGitCommitCount()
    val major = 1
    return major * 30000 + commitCount
}

fun getVersionName(): String {
    return getGitDescribe()
}

subprojects {
    plugins.withType(AndroidBasePlugin::class.java) {
        extensions.configure(CommonExtension::class.java) {
            compileSdk = androidCompileSdkVersion
            ndkVersion = androidCompileNdkVersion

            defaultConfig {
                minSdk = androidMinSdkVersion
                if (this is ApplicationDefaultConfig) {
                    targetSdk = androidTargetSdkVersion
                    versionCode = managerVersionCode
                    versionName = managerVersionName
                }
                ndk {
                    abiFilters += listOf("arm64-v8a", "x86_64", "riscv64")
                }
            }

            lint {
                abortOnError = true
                checkReleaseBuilds = false
            }

            compileOptions {
                sourceCompatibility = androidSourceCompatibility
                targetCompatibility = androidTargetCompatibility
            }
        }
    }
}
