package structuralinstancing.structuralinstancing.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import structuralinstancing.structuralinstancing.StructuraLInstancing;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StructureUtil {

	final static Map<String, Location> LOCATION_MAP = new HashMap<>();

	final static Set<Location> SAVED_LOCATIONS = new HashSet<>();


	public static void initializeStructures(StructuraLInstancing plugin, StructureManager strmanager) {

		String world;
		double startlocationx;
		double startlocationy;
		double startlocationz;

		try {
			File instanceconfigfile = new File(plugin.getDataFolder(), "instances.yml");
			YamlConfiguration general_config = new YamlConfiguration();
			general_config.load(instanceconfigfile);
			for (String key : general_config.getKeys(false)) {
				ConfigurationSection config_section = general_config.getConfigurationSection(key);

				if (config_section == null) continue;
				if (!config_section.contains("start-x") || !config_section.contains("start-y") || !config_section.contains("start-z") || !config_section.contains("world") || !config_section.contains("spacing")) continue;
				world = config_section.getString("world");
				if (world == null || Bukkit.getServer().getWorld(world) == null) continue;

				startlocationx = config_section.getDouble("start-x");
				startlocationy = config_section.getDouble("start-y");
				startlocationz = config_section.getDouble("start-z");
				Location loc = new Location(plugin.getServer().getWorld(world), startlocationx, startlocationy, startlocationz);

				Structure structure = strmanager.loadStructure(new NamespacedKey("test", key));
				Structure emptystructure = strmanager.loadStructure(new NamespacedKey("test", key + "empty"));


				if (structure != null && emptystructure != null) {
					strmanager.registerStructure(new NamespacedKey("test", key), structure);
					strmanager.registerStructure(new NamespacedKey("test", key + "empty"), emptystructure);

					LOCATION_MAP.put(key, loc);

					}
				else {
					System.out.println("Structure " + key + "not found. Could not register structure");
				}
			}
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	public static Map<NamespacedKey, Structure> getStructureSet(StructureManager strmanager) {
		return strmanager.getStructures();
	}

	public static Structure getStructure(String structurename, StructureManager strmanager) {
		return strmanager.getStructure(new NamespacedKey("test", structurename));
	}

	public static Location getLocation(String structurename, StructureManager strmanager) {
		return LOCATION_MAP.get(structurename);
	}

	public static boolean setLocationConfig(String structurename, StructureManager strmanager, StructuraLInstancing plugin) {
		try {
			File instanceconfigfile = new File(plugin.getDataFolder(), "instances.yml");
			YamlConfiguration general_config = new YamlConfiguration();
			general_config.load(instanceconfigfile);

			ConfigurationSection config_section = general_config.getConfigurationSection(structurename);
			if (config_section == null) return false;

			double spacing = config_section.getDouble("spacing");
			int iteration = config_section.getInt("iteration");

			Location loc = LOCATION_MAP.get(structurename);
			loc.add(spacing, 0, 0);
			if (iteration == 5) {
				loc.add((-1)*(spacing*5), 0, spacing);
				iteration = 0;
			}
			iteration += 1;

			if (!config_section.contains("iteration")) {
				config_section.createSection("iteration");
				config_section.set("iteration", 0);
				Bukkit.getLogger().info("check");
			}

			LOCATION_MAP.replace(structurename, loc);

			config_section.set("iteration", iteration);
			config_section.set("start-x", loc.getX());
			config_section.set("start-y", loc.getY());
			config_section.set("start-z", loc.getZ());
			general_config.save(instanceconfigfile);

		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	public static String serializeLocation(Location loc) {
		return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getUID();


	}

	public static Location deserializeLocation(String s) {
		String[] s_parts = s.split(";");
		double x = Double.parseDouble(s_parts[0]);
		double y = Double.parseDouble(s_parts[1]);
		double z = Double.parseDouble(s_parts[2]);
		UUID u = UUID.fromString(s_parts[3]);
		World w = Bukkit.getServer().getWorld(u);
		if (w == null) {
			return null;
		}
		return new Location(w, x, y, z);
	}
}
