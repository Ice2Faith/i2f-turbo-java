plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "i2f.turbo"
version = "1.0"

repositories {
//    mavenCentral()
    // TODO 切换中央仓库
    maven{
        url = uri("https://maven.aliyun.com/repository/public")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-parameters"))
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
//    version.set("2023.2.5")
//    type.set("IC") // Target IDE Platform
     // TODO 不使用下载，直接使用本地安装目录
//    localPath.set("C:\\Program Files\\JetBrains\\IntelliJ IDEA 2024.1")
    localPath.set("D:\\Program Files\\JetBrains\\IntelliJ IDEA 2024.2.1")

    plugins.set(listOf(/* Plugin Dependencies */
        "com.intellij.java",
        "com.intellij.database",
        "org.intellij.intelliLang",
//        "com.intellij.modules.xml",
        "com.intellij.velocity"
        )
    )

}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
