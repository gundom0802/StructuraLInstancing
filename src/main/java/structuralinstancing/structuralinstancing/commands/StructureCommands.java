package structuralinstancing.structuralinstancing.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import structuralinstancing.structuralinstancing.StructuraLInstancing;
import structuralinstancing.structuralinstancing.util.StructureUtil;

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
				Structure structure = StructureUtil.getStructure(args[1], plugin.getServer().getStructureManager());
				if (structure == null) {
					sender.sendMessage("Structure not found");
				}
				else {
					structure.place(StructureUtil.getLocation(args[1], plugin.getServer().getStructureManager()), false, StructureRotation.NONE, Mirror.NONE, -1, 1, ThreadLocalRandom.current());
					StructureUtil.setLocation(args[1], plugin.getServer().getStructureManager(), plugin);
				}
		}
		return false;
	}
}
