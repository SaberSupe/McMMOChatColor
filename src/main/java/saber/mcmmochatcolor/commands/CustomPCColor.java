package saber.mcmmochatcolor.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import saber.mcmmochatcolor.McMMOChatColor;
import saber.mcmmochatcolor.utils.HexColors;

import java.util.List;

public class CustomPCColor implements CommandExecutor {

    private final McMMOChatColor plugin;
    private final NamespacedKey ColorKey;

    public CustomPCColor(McMMOChatColor p1){
        plugin = p1;
        ColorKey = new NamespacedKey(plugin,"PartyChatColor");

    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        // Check that given command is correct, probably not necessary
        if (!cmd.getName().equalsIgnoreCase("custompccolor")) return true;

        if (args.length == 0){
            sendColorizedMessage(sender, "msgTooFewArgs");
            return true;
        }

        // Force reseting another players party chat color
        if (args.length >= 2 && args[0].equalsIgnoreCase("reset") && sender.hasPermission("mcmmochatcolor.forcereset")) {
            Player removee = Bukkit.getPlayer(args[1]);
            if (removee == null){
                sendColorizedMessage(sender, "msgPlayerNotFound");
                return true;
            }
            // Remove party chat color of player
            removee.getPersistentDataContainer().remove(ColorKey);
            sendColorizedMessage(removee, "msgResetForced");
            sendColorizedMessage(sender, "msgForceResetSuccess");
            return true;
        }

        // Don't let non-players set party chat color
        if (!(sender instanceof Player)) {
            sendColorizedMessage(sender, "msgNotAPlayer");
            return true;
        }

        // Check for perms
        if (!sender.hasPermission("mcmmochatcolor.color")) {
            sendColorizedMessage(sender, "msgNoPerms");
            return true;
        }

        Player play = (Player) sender;

        // Remove party chat color of sender if reset is selected
        if (args[0].equalsIgnoreCase("reset")){
            play.getPersistentDataContainer().remove(ColorKey);
            sendColorizedMessage(sender, "msgResetSuccess");
            return true;
        }

        String color = args[0].toLowerCase();

        // Check for any blacklisted formats
        List<String> blacklist = plugin.getConfig().getStringList("formatblacklist");
        for (String x : blacklist) {
            if (color.contains(x)) {
                sendColorizedMessage(sender, "msgFormatNotAllowed");
                return true;
            }
        }

        // Convert any valid short codes then remove them to leave behind only hex codes
        String stripped = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',color));

        // Ensure remaining string only contains hex codes
        if (!HexColors.validHexColorCodes(stripped)){
            sendColorizedMessage(sender, "msgInvalidCode");
            return true;
        }

        // if (hex codes used && hex codes not allowed)
        if (stripped.length()>0 && !plugin.getConfig().getBoolean("allowhex")){
            sendColorizedMessage(sender,"msgHexNotAllowed");
            return true;
        }

        // Update player custom party chat color
        play.getPersistentDataContainer().set(ColorKey, PersistentDataType.STRING, args[0]);
        sendColorizedMessage(sender, "msgSetSuccessfully");

        return true;
    }

    // Adds color and sends the message from config
    private void sendColorizedMessage(CommandSender sender, String configmsg){
        String message = plugin.getConfig().getString(configmsg);

        // Shouldn't be null but will check anyway
        if (message == null) return;

        message = ChatColor.translateAlternateColorCodes('&', message);
        sender.sendMessage(message);
    }
}
