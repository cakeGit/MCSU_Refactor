package com.cloud.mcsu_rf.Definitions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * <h3>A <b>wrapper class</b> for ItemStack to allow chaining and some other extra functionality.
 * Extends Bukkit ItemStack so it has all preexisting methods,
 * should be able to let ItemStack cast to itself and should be interchangeable with ItemStack</h3>
 * Most custom functions will return the McsuItemStack to allow chaining.<br><br>
 * Already existing functions may have a wrapper in here, ending with <code>RI</code> (Returns Item),<br>
 * For example, <code>setDisplayName()</code> has a copy called <code>setDisplayNameRI()</code>.<br><br>
 * Constructor will take a Material and a quantity (optional) - same as ItemStack.<br>
 * If for any reason you need a true ItemStack, use <code>getItemStack()</code>.
 *
 *<br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
 *
 * @author Cake
 * @see ItemStack
 */
public class McsuItemStack extends ItemStack {

    private final Material material;

    /**
     * <h3>A <b>wrapper class</b> for ItemStack to allow chaining and some other extra functionality.
     * Extends Bukkit ItemStack so it has all preexisting methods,
     * should be able to let ItemStack cast to itself and should be interchangeable with ItemStack</h3>
     * Most custom functions will return the McsuItemStack to allow chaining.<br><br>
     * Already existing functions may have a wrapper in here, ending with <code>RI</code> (Returns Item),<br>
     * For example, <code>setDisplayName()</code> has a copy called <code>setDisplayNameRI()</code>.
     *
     *<br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     *
     * @param material Material of the McsuItemStack
     * @param amount Quantity items in the of the McsuItemStack
     * @see ItemStack
     */
    public McsuItemStack(Material material, int amount) { super(material, amount); this.material = material; }

    /**
     * <h3>A <b>wrapper class</b> for ItemStack to allow chaining and some other extra functionality.
     * Extends Bukkit ItemStack so it has all preexisting methods,
     * should be able to let ItemStack cast to itself and should be interchangeable with ItemStack</h3>
     * Most custom functions will return the McsuItemStack to allow chaining.<br><br>
     * Already existing functions may have a wrapper in here, ending with <code>RI</code> (Returns Item),<br>
     * For example, <code>setDisplayName()</code> has a copy called <code>setDisplayNameRI()</code>.
     *
     *<br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     *
     * @param material Material of the McsuItemStack
     * @see ItemStack
     */
    public McsuItemStack(Material material) { super(material); this.material = material; }

    /**
     * <h3>Wrapper function that makes the item unbreakable via the <code>ItemMeta.addEnchant()</code> method.</h3>
     *
     * This makes the item unable to take damage to its durability<br><br>
     *
     * <span style="font-style: italic;">Returns the McsuItemStack so that you can chain methods</span>
     *
     * <br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     *
     * @return McsuItemStack with unbreakable set to true
     */
    public ItemStack setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * <h3>Wrapper function that forcefully sets enchantment via the <code>ItemMeta.addEnchant()</code> method.</h3>
     *
     * This <b>bypasses the Bukkit's enchantment rules</b> and allows you to set any enchantment, i.e. a knock-back enchantment on a stick.<br><br>
     *
     * <span style="font-style: italic;">Returns the McsuItemStack so that you can chain methods</span>
     *
     * <br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     * @param enchantment Enchantment to set
     * @param level Level of enchantment to set
     * @return McsuItemStack with set enchantment
     */
    public McsuItemStack forceAddEnchantment(Enchantment enchantment, int level) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * <h3>Wrapper function that sets the display name of an item via the <code>ItemMeta.addEnchant()</code> method.</h3>
     *
     * This is a convenience method for setting the display name of an item without getting and setting meta.<br><br>
     *
     * <span style="font-style: italic;">Returns the McsuItemStack so that you can chain methods</span>
     *
     * <br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     * @param name Name to set
     * @return McsuItemStack with set name
     */
    public McsuItemStack setName(String name) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * <h3>Wrapper function to set a single line of lore for an item that runs <code>getItemMeta().setLore(List.of(lore))</code>.</h3>
     *
     * <b color="#ff8800;">Warning: Please note that this will overwrite any existing lore!</b><br><br>
     *
     * This is a convenience method for setting the lore of an item without getting and setting meta.<br><br>
     *
     * <span style="font-style: italic;">Returns the McsuItemStack so that you can chain methods</span>
     *
     * <br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     * @param lore Lore to set
     * @return McsuItemStack with set lore
     */
    public McsuItemStack setBasicLore(String lore) {
        getItemMeta().setLore(List.of(lore));
        return this;
    }

    /**
     * <h3>Returns a copy of the McsuItemStack with the same material, MetaData and amount.</h3>
     *
     * This is done to prevent accidentally modifying the original McsuItemStack if you have a public var and want to modiify params.<br><br>
     *
     * <span style="font-style: italic;">Will not interrupt chaining as the return is still a McsuItemStack</span>
     *
     * <br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     * @return Copy of McsuItemStack called on
     */
    public McsuItemStack clone() {
        return new McsuItemStack(material, getAmount())
                .setItemMetaRI(getItemMeta());
    }

    /**
     * <h3>Wrapper function to set ItemMeta and allow chaining with other <code>RI</code> methods,
     * runs <code>setItemMeta()</code>.</h3>
     *
     * <span style="font-style: italic;">Returns the McsuItemStack so that you can chain methods</span>
     *
     * <br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     *
     * @param meta ItemMeta to set
     * @return McsuItemStack with set meta
     * @see ItemStack#setItemMeta(ItemMeta)
     */
    public McsuItemStack setItemMetaRI(ItemMeta meta) {
        setItemMeta(meta);
        return this;
    }

    /**
     * <h3>Wrapper function to set the amount of the items in the ItemStack and allow chaining with other <code>RI</code> methods,
     * runs <code>setAmount()</code>.</h3>
     *
     * <span style="font-style: italic;">Returns the McsuItemStack so that you can chain methods</span>
     *
     * <br><br><img style=" text-align : center;" src="https://i.ibb.co/bRK3GN4/MCSU-Small.png"></img><br><samp>MCSU Javadocs by Cak</samp><br><br>
     *
     * @param amount Item amount to set
     * @return McsuItemStack with set meta
     * @see ItemStack#setAmount(int)
     */
    public McsuItemStack setAmountRI(int amount) {
        setAmount(amount);
        return this;
    }

}
