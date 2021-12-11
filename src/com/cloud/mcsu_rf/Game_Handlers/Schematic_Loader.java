package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.MCSU_Main;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Schematic_Loader {

    public static void loadSchematic(String Schem_File, BlockVector3 Paste_To, World world) {


        File schem = new File(MCSU_Main.FileDir + File.separator + "schematics/"+Schem_File+".schem");
        Bukkit.getLogger().info("Loading schem '" + MCSU_Main.FileDir + File.separator + "schematics/"+Schem_File+".schem" + "'");

        if (schem.exists()) {
            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(world);
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
            Bukkit.getScheduler().runTaskAsynchronously(
                    MCSU_Main.instance, () -> {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schem);
                            ClipboardReader reader = format.getReader(new FileInputStream(schem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(Paste_To).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } else  {
            Bukkit.getLogger().info("Couldn't find file '" + MCSU_Main.FileDir + File.separator + "schematics/"+Schem_File+".schem" + "'");
        }



    }

}
