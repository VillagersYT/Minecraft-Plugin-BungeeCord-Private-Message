package fr.villagersyt.privatemessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.ProxyServer;


public class MsgCommand extends Command {
	
    private final Map<ProxiedPlayer, ProxiedPlayer> lastMessaged = new HashMap<>();
    private final Set<ProxiedPlayer> spyPlayers = new HashSet<>();

        public MsgCommand() {
            super("msg");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;

                if (args.length >= 2) {
                    String recipientName = args[0];
                    ProxiedPlayer recipient = findOnlinePlayer(recipientName);

                    if (recipient != null) {
                        // Ajoutez une vérification pour s'assurer que le joueur n'envoie pas de message à lui-même
                        if (player.equals(recipient)) {
                            player.sendMessage("§cVous ne pouvez pas vous envoyer des messages à vous - même.");
                            return; // Arrêtez le traitement si le destinataire est le joueur émetteur.
                        }

                        StringBuilder message = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            message.append(args[i]).append(" ");
                        }

                        String msg = message.toString().trim();
                        recipient.sendMessage("§a§lMessage privés §7» §e" + player.getName() + " §7: §f" + msg);
                        player.sendMessage("§a§lMessage privés §7» §e" + recipientName + " §7: §f" + msg);

                        // Enregistrez la dernière conversation pour la commande /r
                        lastMessaged.put(recipient, player);

                        // Si un administrateur est en mode espion, enregistrez la conversation
                        if (spyPlayers.contains(player)) {
                            ProxyServer.getInstance().getConsole().sendMessage("§8[§eSpyMsg§8] §e" + player.getName() + " §7-> §e" + recipientName + "§7 : §f" + msg);
                        }
                    } else {
                        player.sendMessage("§cLe joueur n' est pas en ligne ou n' existe pas.");
                    }
                } else {
                    player.sendMessage("§cUtilisation correcte : /msg <destinataire> <message>");
                }
            } else {
                // Si la commande est exécutée depuis la console
                sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            }
        }

        private ProxiedPlayer findOnlinePlayer(String playerName) {
            // Cherchez le joueur en ligne en fonction du nom
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().equalsIgnoreCase(playerName)) {
                    return player;
                }
            }
            return null; // Retourne null si le joueur n'est pas trouvé
        }

    public void toggleSpy(ProxiedPlayer player) {
        if (spyPlayers.contains(player)) {
            spyPlayers.remove(player);
            player.sendMessage("§fMode espion des messages désactivé.");
        } else {
            spyPlayers.add(player);
            player.sendMessage("§fMode espion des messages activé.");
        }
    }

    public void replyToLastMessage(ProxiedPlayer player, String[] args) {
        if (lastMessaged.containsKey(player)) {
            ProxiedPlayer lastSender = lastMessaged.get(player);
            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg).append(" ");
            }
            String msg = message.toString().trim();
            lastSender.sendMessage("Message from " + player.getName() + " (reply): " + msg);
            player.sendMessage("Message sent to " + lastSender.getName() + ": " + msg);
        } else {
            player.sendMessage("§cAucune conversation récente à laquelle répondre.");
        }
    }
}
