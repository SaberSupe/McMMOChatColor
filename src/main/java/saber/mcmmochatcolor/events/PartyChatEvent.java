package saber.mcmmochatcolor.events;

import com.gmail.nossr50.chat.mailer.PartyChatMailer;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.datatypes.chat.ChatChannel;
import com.gmail.nossr50.events.chat.McMMOPartyChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import saber.mcmmochatcolor.McMMOChatColor;
import saber.mcmmochatcolor.utils.DisplayNameUtil;

public class PartyChatEvent implements Listener {

    private McMMOChatColor plugin;
    private NamespacedKey ColorKey;
    private PartyChatMailer PartyMailer;
    public PartyChatEvent(McMMOChatColor p1){
        plugin = p1;
        ColorKey = new NamespacedKey(plugin,"PartyChatColor");
        PartyMailer = new PartyChatMailer(JavaPlugin.getPlugin(mcMMO.class));
    }

    @EventHandler
    public void onPartyChat (McMMOPartyChatEvent event){

        // Get the display name of the chat sender in color
        String disname = ChatColor.translateAlternateColorCodes('&', event.getDisplayName(ChatChannel.PARTY));

        // Use the display name to get the player. This is not ideal but McMMO only provides display name of sender
        Player play = DisplayNameUtil.getPlayerByDisplayName(disname);
        if (play == null) return;

        // Check Perms
        if (!play.hasPermission("mcmmochatcolor.color")) return;

        // Check for then retrieve player's saved color
        if (!play.getPersistentDataContainer().has(ColorKey, PersistentDataType.STRING)) return;
        String playercolor = play.getPersistentDataContainer().get(ColorKey,PersistentDataType.STRING);

        // Confirm the player has perms to use colors in party chat, also needed for addStyle later
        boolean canColor = play.hasPermission("mcmmo.chat.colors");
        if (!canColor) return;

        // Add the color code to the front of the raw message and get McMMO PartyChatMailer to do the formatting according to McMMO settings. Then update the message
        boolean isLeader = event.getAuthorParty().getLeader().getPlayerName().equalsIgnoreCase(play.getName());
        event.setMessagePayload(PartyMailer.addStyle(event.getAuthor(), playercolor + event.getRawMessage(), canColor, isLeader));
    }

}
