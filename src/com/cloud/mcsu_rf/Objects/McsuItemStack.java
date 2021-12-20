package com.cloud.mcsu_rf.Objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class McsuItemStack extends ItemStack {

    private Material material;

    public McsuItemStack(Material material, int stackSize) { super(material, stackSize); this.material = material; }
    public McsuItemStack(Material material) { super(material); this.material = material; }

    public ItemStack setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        setItemMeta(itemMeta);
        return this;
    }

    public McsuItemStack forceAddEnchantment(Enchantment enchantment, int level) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        setItemMeta(itemMeta);
        return this;
    }

    public McsuItemStack setBasicLore(String lore) {
        getItemMeta().setLore(Arrays.asList(lore));
        return this;
    }

    public boolean isConcrete() {
        List<Material> concretes = Arrays.asList(
                Material.WHITE_CONCRETE,
                Material.ORANGE_CONCRETE,
                Material.MAGENTA_CONCRETE,
                Material.LIGHT_BLUE_CONCRETE,
                Material.YELLOW_CONCRETE,
                Material.LIME_CONCRETE,
                Material.PINK_CONCRETE,
                Material.GRAY_CONCRETE,
                Material.LIGHT_GRAY_CONCRETE,
                Material.CYAN_CONCRETE,
                Material.PURPLE_CONCRETE,
                Material.BLUE_CONCRETE,
                Material.BROWN_CONCRETE,
                Material.GREEN_CONCRETE,
                Material.RED_CONCRETE,
                Material.BLACK_CONCRETE
        );
        return  concretes.contains(material);
    }

}
