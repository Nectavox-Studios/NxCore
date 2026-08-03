group = "com.nectavox.nxcore.bukkit"
version = rootProject.version

dependencies {
    api(project(":api")){
        exclude(module = "net.kyori")
    }
    implementation("net.kyori:adventure-api:4.22.0")
    implementation("net.kyori:adventure-platform-bukkit:4.4.1")
    implementation("dev.triumphteam:triumph-gui:3.1.13")

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.12.3")

    compileOnly("com.github.retrooper:packetevents-spigot:2.13.0")
}