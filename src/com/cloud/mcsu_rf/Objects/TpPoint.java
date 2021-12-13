package com.cloud.mcsu_rf.Objects;

import com.sk89q.worldedit.math.BlockVector3;

public class TpPoint {
    
    BlockVector3 blockVector3;
    String Name;
    double x;
    double y;
    double z;
    
    public TpPoint(String Name, double x, double y, double z) {
        
        this.x = x; this.y = y; this.z = z;

        this.Name = Name;
        
        this.blockVector3 = BlockVector3.at(x, y, z);
        
    }

    public double[] getCoordinates() {
        return new double[] {this.x , this.y, this.x};
    }

    public BlockVector3 getBlockVector3() { return blockVector3; }
    public String getName() { return Name; }
}
