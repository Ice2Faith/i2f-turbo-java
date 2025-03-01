pluginManagement {
    repositories {
//        mavenCentral()
        // TODO 切换中央仓库
        maven{
            url = uri("https://maven.aliyun.com/repository/public")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "jdbc-procedure-plugin"