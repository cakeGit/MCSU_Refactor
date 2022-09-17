package com.cloud.mcsu_rf.Devtools;

import com.cak.what.CommandAPI.CommandHandler;
import com.cak.what.ItemAPI.WhItemStack;
import com.cak.what.MenuAPI.InventoryMenu;
import com.cak.what.Util.ChCol;
import com.cloud.mcsu_rf.Definitions.Map.MapMetadata;
import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MapMetaEditor {

    public static void init() {
        CommandHandler DevHandler = new CommandHandler(MCSU_Main.Mcsu_Plugin);

        DevHandler.registerCommandListener("deveditmapmeta", (sender, command, label, args) -> {

            InventoryMenu metaMenu = new InventoryMenu("[DEV] Meta Editor");

            genPage(metaMenu, MetaEditorPage.SELECT, (Player) sender, 0);

            return true;
        });
    }

    static void genPage(InventoryMenu menu, MetaEditorPage page, Player player, int MapIndex) {
        menu.clear();

        switch(page) {
            case SELECT -> {
                int index = 0;
                for (MapMetadata meta : MapMetadata.RegisteredMapMetadata) {
                    menu.setIndex(index, new WhItemStack(Material.PAPER)
                            .setName(ChCol.GREEN + meta.getName() + " - " + meta.getGame())
                            .setLore("Click to edit!"));

                    index += 1;
                }
            }
            case MAP -> {
                menu.setIndex(0,
                        new WhItemStack(Material.RED_BED)
                                .setName(ChCol.GREEN + "Edit Game Spawns")
                                .setLore("Click to edit!")
                );
            }
        }

        menu.open(player, event -> onClick(event, menu, page, player, MapIndex));
    }

    static void onClick(InventoryClickEvent event, InventoryMenu menu, MetaEditorPage page, Player player,  int MapIndex) {

        switch (page) {
            case SELECT -> {
                genPage(menu, MetaEditorPage.MAP, player, event.getRawSlot());
            }
            case MAP -> {
                switch (event.getRawSlot()) {
                    case 0 -> {
                        player.closeInventory();

                    }
                }
            }
        }

    }

}
