package com.cloud.mcsu_rf.Definitions.Map;

import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;

public class MapPoint {

    Double[] Coordinates;
    Double[] Rotation;
    String Id;

    public MapPoint(String Coordinates, String Rotation) {
        this.Coordinates = ParseArr.toDouble(Coordinates.split(" "));
        this.Rotation = ParseArr.toDouble(Rotation.split(" "));

    }

    public Double[] getCoordinates() { return Coordinates; }
    public Double[] getRotation() { return Rotation; }
    public String getId() { return Id; }

    public MapPoint setId(String Id) {
        this.Id = Id;
        return this;
    }

}
