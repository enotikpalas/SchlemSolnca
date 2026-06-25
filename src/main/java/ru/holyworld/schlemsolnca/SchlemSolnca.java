package ru.holyworld.schlemsolnca;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import ru.holyworld.schlemsolnca.commands.SchlemSolncaCommand;

import java.util.UUID;

public class SchlemSolnca extends JavaPlugin {

    private static SchlemSolnca instance;
    private NamespacedKey schlemKey;

    @Override
    public void onEnable() {
        instance = this;
        schlemKey = new NamespacedKey(this, "schlem_solnca");
        
        // Регистрируем команду
        getCommand("schlemsolnca").setExecutor(new SchlemSolncaCommand());
        
        getLogger().info("SchlemSolnca плагин успешно включён!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SchlemSolnca плагин выключен.");
    }

    public static SchlemSolnca getInstance() {
        return instance;
    }

    public NamespacedKey getSchlemKey() {
        return schlemKey;
    }

    /**
     * Создаёт шлем солнца со всеми нужными характеристиками
     */
    public static ItemStack createSchlemSolnca() {
        ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        
        // Название шлема (можно менять под стиль HolyWorld)
        meta.setDisplayName("§6§lШлем Солнца");
        meta.setLore(java.util.Arrays.asList(
                "§7Священный шлем, наполненный",
                "§7энергией самого солнца"
        ));
        
        // Делаем шлем неразрушаемым
        meta.setUnbreakable(true);
        
        // Скрываем флаг неразрушаемости для красоты
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        
        // Помечаем предмет как шлем солнца через PersistentDataContainer
        meta.getPersistentDataContainer().set(
                getInstance().getSchlemKey(),
                PersistentDataType.BOOLEAN,
                true
        );
        
        helmet.setItemMeta(meta);
        
        // Добавляем защиту как у незеритового шлема
        // Базовая броня золотого шлема = 2 (половина иконки)
        // Незеритовый шлем = 3 брони + 3 toughness
        // Делаем: золотой шлем получает такие же статы
        
        // Удаляем стандартные модификаторы и добавляем новые
        // Для 1.21.1 используем EquipmentSlotGroup.HEAD
        
        // Основная броня (Armor) — как у незеритового шлема: 3
        AttributeModifier armorModifier = new AttributeModifier(
                UUID.randomUUID(),
                "schlem_solnca_armor",
                3.0, // Значение как у незеритового шлема
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.HEAD
        );
        
        // Toughness (прочность брони) — как у незеритового шлема: 3
        AttributeModifier toughnessModifier = new AttributeModifier(
                UUID.randomUUID(),
                "schlem_solnca_toughness",
                3.0, // Как у незеритового шлема
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.HEAD
        );
        
        // Knockback Resistance — как у незеритового шлема: 0 (у шлема нет сопротивления отбрасыванию)
        // но можно добавить для усиления:
        AttributeModifier knockbackModifier = new AttributeModifier(
                UUID.randomUUID(),
                "schlem_solnca_knockback",
                0.1, // Лёгкое сопротивление отбрасыванию
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.HEAD
        );
        
        meta = helmet.getItemMeta();
        meta.addAttributeModifier(Attribute.ARMOR, armorModifier);
        meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, toughnessModifier);
        meta.addAttributeModifier(Attribute.KNOCKBACK_RESISTANCE, knockbackModifier);
        
        // Разрешаем любые зачарования — убираем ограничения
        // Золотой шлем по умолчанию может принимать все зачарования для брони,
        // но мы явно разрешаем конфликтующие зачарования
        
        helmet.setItemMeta(meta);
        
        // Добавляем свечение как у зачарованного предмета
        helmet.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        meta = helmet.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Скрываем само зачарование свечения
        helmet.setItemMeta(meta);
        
        return helmet;
    }

    /**
     * Проверяет, является ли предмет шлемом солнца
     */
    public static boolean isSchlemSolnca(ItemStack item) {
        if (item == null || item.getType() != Material.GOLDEN_HELMET) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(
                getInstance().getSchlemKey(),
                PersistentDataType.BOOLEAN
        );
    }
}
