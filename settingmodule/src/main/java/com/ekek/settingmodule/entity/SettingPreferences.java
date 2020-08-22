package com.ekek.settingmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Samhung on 2018/2/1.
 */
@Entity
public class SettingPreferences {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "Item")
    private String item;
    @Property(nameInDb = "Parameter")
    private String parameter;
    @Generated(hash = 1971632996)
    public SettingPreferences(Long id, String item, String parameter) {
        this.id = id;
        this.item = item;
        this.parameter = parameter;
    }
    @Generated(hash = 862590032)
    public SettingPreferences() {
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

}
