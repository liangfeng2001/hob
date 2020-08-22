package com.ekek.tfthobmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MyRecipesTable {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "Name")
    private String name;
    @Property(nameInDb = "TempValue")
    private int tempValue;
    @Property(nameInDb = "Decription")
    private String decription;
    @Property(nameInDb = "HourValue")
    private int hourValue;
    @Property(nameInDb = "MinuteValue")
    private int minuteValue;
    @Property(nameInDb = "SecondValue")
    private int secondValue;
    @Property(nameInDb = "Reserve")
    private String reserve;
    @Generated(hash = 687323038)
    public MyRecipesTable(Long id, String name, int tempValue, String decription,
            int hourValue, int minuteValue, int secondValue, String reserve) {
        this.id = id;
        this.name = name;
        this.tempValue = tempValue;
        this.decription = decription;
        this.hourValue = hourValue;
        this.minuteValue = minuteValue;
        this.secondValue = secondValue;
        this.reserve = reserve;
    }
    @Generated(hash = 914163131)
    public MyRecipesTable() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTempValue() {
        return this.tempValue;
    }
    public void setTempValue(int tempValue) {
        this.tempValue = tempValue;
    }
    public String getDecription() {
        return this.decription;
    }
    public void setDecription(String decription) {
        this.decription = decription;
    }
    public int getHourValue() {
        return this.hourValue;
    }
    public void setHourValue(int hourValue) {
        this.hourValue = hourValue;
    }
    public int getMinuteValue() {
        return this.minuteValue;
    }
    public void setMinuteValue(int minuteValue) {
        this.minuteValue = minuteValue;
    }
    public int getSecondValue() {
        return this.secondValue;
    }
    public void setSecondValue(int secondValue) {
        this.secondValue = secondValue;
    }
    public String getReserve() {
        return this.reserve;
    }
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}
