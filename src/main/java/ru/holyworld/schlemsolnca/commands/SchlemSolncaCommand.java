package ru.holyworld.schlemsolnca.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.holyworld.schlemsolnca.SchlemSolnca;

public class SchlemSolncaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        // Проверка прав (хотя plugin.yml уже фильтрует, но дополнительная проверка не помешает)
        if (!sender.hasPermission("schlemsolnca.give")) {
            sender.sendMessage("§cУ вас нет прав на использование этой команды!");
            return true;
        }
        
        Player target;
        
        if (args.length > 0) {
            // Выдаём указанному игроку
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cИгрок " + args[0] + " не найден или не в сети!");
                return true;
            }
        } else {
            // Выдаём себе (должен быть игроком)
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cКонсоль должна указать игрока: /schlemsolnca <игрок>");
                return true;
            }
            target = (Player) sender;
        }
        
        // Создаём шлем солнца
        ItemStack schlem = SchlemSolnca.createSchlemSolnca();
        
        // Пытаемся положить в инвентарь
        if (target.getInventory().firstEmpty() == -1) {
            // Инвентарь полон — кидаем под ноги
            target.getWorld().dropItemNaturally(target.getLocation(), schlem);
            target.sendMessage("§6Ваш инвентарь полон! Шлем Солнца упал рядом.");
        } else {
            target.getInventory().addItem(schlem);
            target.sendMessage("§6Вы получили Шлем Солнца!");
        }
        
        // Оповещаем отправителя, если он выдал другому игроку
        if (sender != target) {
            sender.sendMessage("§aВы выдали Шлем Солнца игроку " + target.getName());
        }
        
        return true;
    }
}
