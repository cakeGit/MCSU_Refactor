package com.cloud.mcsu_rf.Objects.Map;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;

public class MapPoint {

    Double[] Coordinates;
    Double[] Rotation;
    String Id;

    public MapPoint(String Coordinates, String Rotation) {
        Bukkit.getLogger().info("(MapPoint) Parsing coordinates '" + Coordinates + "'");
        String[] CrdArr = Coordinates.split(" ");

        ArrayList<Double> CrdArrList = new ArrayList<>();
        for (String str : CrdArr) {
            CrdArrList.add(Double.parseDouble(str));
        }

        this.Coordinates = CrdArrList.toArray(new Double[0]);

        Bukkit.getLogger().info("(MapPoint) Parsing rotation '" + Rotation + "'");
        String[] rotArr = Rotation.split(" ");

        ArrayList<Double> rotArrList = new ArrayList<>();
        for (String str : rotArr) {
            rotArrList.add(Double.parseDouble(str));
        }

        this.Rotation = rotArrList.toArray(new Double[0]);
    }

    public Double[] getCoordinates() { return Coordinates; }
    public Double[] getRotation() { return Rotation; }
    public String getId() { return Id; }

    public MapPoint setId(String Id) {
        this.Id = Id;
        return this;
    }

}
