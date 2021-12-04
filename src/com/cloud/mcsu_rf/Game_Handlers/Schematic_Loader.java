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
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Schematic_Loader {

    public static void Load_Schematic(File Schem_File, BlockVector3 Paste_To, World World) {

        Bukkit.getLogger().info("Loading schem " + Schem_File.getAbsoluteFile());

        World adaptedWorld = (World) BukkitAdapter.adapt(World);
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File sgschem = new File(MCSU_Main.Mcsu_Plugin.getDataFolder() + File.separator + "/schematics/sg.schem");

        Bukkit.getScheduler().runTaskAsynchronously(
                MCSU_Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(sgschem);
                            ClipboardReader reader = format.getReader(new FileInputStream(sgschem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(Paste_To).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }

}
