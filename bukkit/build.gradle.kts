group = "com.nectavox.nxcore.bukkit"
version = rootProject.version

dependencies {
    implementation(project(":api"))
    implementation("net.kyori:adventure-api:5.2.0")
    implementation("dev.triumphteam:triumph-gui:3.1.13")

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.12.3")
}