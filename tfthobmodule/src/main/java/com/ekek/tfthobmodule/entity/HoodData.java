package com.ekek.tfthobmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HoodData {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "fan_gear")
    private int fanGear;
    @Property(nameInDb = "light_gear")
    private int lightGear;
    @Property(nameInDb = "reserve")
    private int reserve;
    @Generated(hash = 760608046)
    public HoodData(Long id, int fanGear, int lightGear, int reserve) {
        this.id = id;
        this.fanGear = fanGear;
        this.lightGear = lightGear;
        this.reserve = reserve;
    }
    @Generated(hash = 2045637753)
    public HoodData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getFanGear() {
        return this.fanGear;
    }
    public void setFanGear(int fanGear) {
        this.fanGear = fanGear;
    }
    public int getLightGear() {
        return this.lightGear;
    }
    public void setLightGear(int lightGear) {
        this.lightGear = lightGear;
    }
    public int getReserve() {
        return this.reserve;
    }
    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

}
