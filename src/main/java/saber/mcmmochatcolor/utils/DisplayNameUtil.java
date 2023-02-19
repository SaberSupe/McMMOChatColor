package saber.mcmmochatcolor.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DisplayNameUtil {

    /* Retrieves a player object using the display name
        Returns Player object if found, null if not
     */
    public static Player getPlayerByDisplayName(String DisplayName){

        // Remove any colors
        DisplayName = ChatColor.stripColor(DisplayName);
        Player temp = null;

        // Loop through online players looking for matching display name
        for (Player x : Bukkit.getOnlinePlayers()){
            if (ChatColor.stripColor(x.getDisplayName()).equalsIgnoreCase(DisplayName)) {
                temp = x;
                break;
            }
        }
        return temp;
    }
}
