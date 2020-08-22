package com.ekek.tfthobmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Samhung on 2017/1/16.
 */
@Entity
public class Setting {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "Item")
    private String item;

    @Property(nameInDb = "Parameter")
    private String parameter;

    @Property(nameInDb = "IsEnable")
    private String isEnable;

    @Generated(hash = 1145451553)
    public Setting(Long id, String item, String parameter, String isEnable) {
        this.id = id;
        this.item = item;
        this.parameter = parameter;
        this.isEnable = isEnable;
    }

    @Generated(hash = 909716735)
    public Setting() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getParameter() {
        return this.parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getIsEnable() {
        return this.isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }


}
