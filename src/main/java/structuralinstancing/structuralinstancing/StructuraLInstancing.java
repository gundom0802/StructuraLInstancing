package structuralinstancing.structuralinstancing;

import org.bukkit.NamespacedKey;
import org.bukkit.structure.Structure;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import structuralinstancing.structuralinstancing.commands.StructureCommands;
import structuralinstancing.structuralinstancing.util.StructureUtil;

import java.io.File;
import java.util.Set;

public final class StructuraLInstancing extends JavaPlugin {

    @Override
    public void onEnable() {

        load();

    }

    @Override
    public void onDisable() {
    }

    public void load() {
        getDataFolder().mkdir();
        new File(getDataFolder(), "data").mkdir();
        if (!new File(getDataFolder(), "general.yml").exists()) saveResource("general.yml", false);
        StructureUtil.initializeStructures(this, getServer().getStructureManager());

        getCommand("instance").setExecutor(new StructureCommands(this));
    }
}
