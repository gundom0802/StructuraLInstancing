package structuralinstancing.structuralinstancing;

import org.bukkit.plugin.java.JavaPlugin;
import structuralinstancing.structuralinstancing.commands.StructureCommands;
import structuralinstancing.structuralinstancing.util.StructureUtil;

import java.io.File;

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
        if (!new File(getDataFolder(), "instances.yml").exists()) saveResource("instances.yml", false);
        StructureUtil.initializeStructures(this, getServer().getStructureManager());

        getCommand("instance").setExecutor(new StructureCommands(this));
    }
}
