package com.ekek.settingmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SecurityTable {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "Type")
    private String type;
    @Property(nameInDb = "Password")
    private String password;
    @Property(nameInDb = "Enable")
    private boolean enable;
    @Property(nameInDb = "Reserve")
    private String reserve;
    @Generated(hash = 1149660121)
    public SecurityTable(Long id, String type, String password, boolean enable,
            String reserve) {
        this.id = id;
        this.type = type;
        this.password = password;
        this.enable = enable;
        this.reserve = reserve;
    }
    @Generated(hash = 1611970254)
    public SecurityTable() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getEnable() {
        return this.enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public String getReserve() {
        return this.reserve;
    }
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}

