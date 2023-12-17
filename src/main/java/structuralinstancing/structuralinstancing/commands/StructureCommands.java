package structuralinstancing.structuralinstancing.commands;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.structure.Structure;
import org.jetbrains.annotations.NotNull;
import structuralinstancing.structuralinstancing.StructuraLInstancing;
import structuralinstancing.structuralinstancing.util.StructureUtil;

import javax.naming.Name;
import java.util.concurrent.ThreadLocalRandom;

public class StructureCommands implements CommandExecutor {

	private StructuraLInstancing plugin;

	public StructureCommands(StructuraLInstancing plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}

		Player pl = (Player) sender;
		PersistentDataContainer player_pdc = pl.getPersistentDataContainer();
		Structure structure;

		if (args.length == 0) {
			sender.sendMessage("Invalid command usage. Must use /instance <action>");
		}

		switch (args[0]) {
			case "list":
				for (NamespacedKey key : StructureUtil.getStructureSet(plugin.getServer().getStructureManager()).keySet()) {
					sender.sendMessage(key.getKey());
					return true;
				}
			case "place":
				if (player_pdc.has(new NamespacedKey("test", args[1]))) {
					pl.sendMessage("Player already has instance. Can not place another one.");
					return true;
				}
				structure = StructureUtil.getStructure(args[1], plugin.getServer().getStructureManager());
				if (structure == null) {
					sender.sendMessage("Structure not found");
				}
				else {
					Location loc = StructureUtil.getLocation(args[1], plugin.getServer().getStructureManager());
					structure.place(loc, false, StructureRotation.NONE, Mirror.NONE, -1, 1, ThreadLocalRandom.current());
					player_pdc.set(new NamespacedKey("test", args[1]), PersistentDataType.STRING, StructureUtil.serializeLocation(loc));
					StructureUtil.setLocationConfig(args[1], plugin.getServer().getStructureManager(), plugin);
				}
				return true;
			case "remove":
				String locationstring = player_pdc.get(new NamespacedKey("test", args[1]), PersistentDataType.STRING);
				if (locationstring == null){
					pl.sendMessage("Player does not have an instance");
					return true;
				}
				structure = plugin.getServer().getStructureManager().getStructure(new NamespacedKey("test", args[1] + "empty"));
				if (structure == null) {
					pl.sendMessage("Structure not found.");
					return true;
				}

				Location loc = StructureUtil.deserializeLocation(locationstring);
				if (loc == null) return true;
				structure.place(loc, false, StructureRotation.NONE, Mirror.NONE, -1, 1, ThreadLocalRandom.current());
				player_pdc.remove(new NamespacedKey("test", args[1]));
				return true;
		}
		return false;
	}
}
