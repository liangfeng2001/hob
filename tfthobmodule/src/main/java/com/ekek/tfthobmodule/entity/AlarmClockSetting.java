package com.ekek.tfthobmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AlarmClockSetting {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "alarm_clock_hour")
    private int hour;
    @Property(nameInDb = "alarm_clock_minute")
    private int minute;
    @Property(nameInDb = "reserve")
    private int reserve;
    @Generated(hash = 1610481980)
    public AlarmClockSetting(Long id, int hour, int minute, int reserve) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.reserve = reserve;
    }
    @Generated(hash = 217907514)
    public AlarmClockSetting() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getHour() {
        return this.hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return this.minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public int getReserve() {
        return this.reserve;
    }
    public void setReserve(int reserve) {
        this.reserve = reserve;
    }


}
