package com.ekek.tfthobmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HobData {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "cooker_id")
    private int cookerID;
    @Property(nameInDb = "cooker_mode")
    private int cookerMode;
    @Property(nameInDb = "cooker_gear")
    private int cookerGear;
    @Property(nameInDb = "cooker_temp")
    private int cookerTemp;
    @Property(nameInDb = "reserve")
    private int reserve;
    @Generated(hash = 1344222941)
    public HobData(Long id, int cookerID, int cookerMode, int cookerGear,
            int cookerTemp, int reserve) {
        this.id = id;
        this.cookerID = cookerID;
        this.cookerMode = cookerMode;
        this.cookerGear = cookerGear;
        this.cookerTemp = cookerTemp;
        this.reserve = reserve;
    }
    @Generated(hash = 972721403)
    public HobData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getCookerID() {
        return this.cookerID;
    }
    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }
    public int getCookerMode() {
        return this.cookerMode;
    }
    public void setCookerMode(int cookerMode) {
        this.cookerMode = cookerMode;
    }
    public int getCookerGear() {
        return this.cookerGear;
    }
    public void setCookerGear(int cookerGear) {
        this.cookerGear = cookerGear;
    }
    public int getCookerTemp() {
        return this.cookerTemp;
    }
    public void setCookerTemp(int cookerTemp) {
        this.cookerTemp = cookerTemp;
    }
    public int getReserve() {
        return this.reserve;
    }
    public void setReserve(int reserve) {
        this.reserve = reserve;
    }
}
