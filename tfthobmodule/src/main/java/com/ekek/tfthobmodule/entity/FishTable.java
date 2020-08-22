package com.ekek.tfthobmodule.entity;

import com.ekek.tfthobmodule.utils.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;

@Entity
public class FishTable {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "weight")
    private String weight;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> cookDatas;
    @Generated(hash = 1683708418)
    public FishTable(Long id, String name, String weight, List<String> cookDatas) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.cookDatas = cookDatas;
    }
    @Generated(hash = 1536556266)
    public FishTable() {
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
    public String getWeight() {
        return this.weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public List<String> getCookDatas() {
        return this.cookDatas;
    }
    public void setCookDatas(List<String> cookDatas) {
        this.cookDatas = cookDatas;
    }
}
