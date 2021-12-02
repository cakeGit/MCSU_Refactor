package com.cloud.mcsu_rf.Game_Classes;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Schematic_Loader {

    public static void Load_Schematic(File Schem_File, BlockVector3 Paste_To, World World) {

        ClipboardFormat Format = ClipboardFormats.findByFile(Schem_File);
        ClipboardReader Reader = null;
        try {
            Reader = Format.getReader(new FileInputStream(Schem_File));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Clipboard clipboard = null;
        try {
            clipboard = Reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(World, -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(Paste_To)
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        }


    }

}
