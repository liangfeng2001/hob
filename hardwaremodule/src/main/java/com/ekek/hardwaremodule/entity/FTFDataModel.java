package com.ekek.hardwaremodule.entity;

/**
 * Created by Samhung on 2018/1/10.
 */

public class FTFDataModel {
    private byte aMode,aFireGear,aTempGear;
    private byte bMode,bFireGear,bTempGear;
    private byte cMode,cFireGear,cTempGear;
    private byte dMode,dFireGear,dTempGear;
    private byte eMode,eFireGear,eTempGear;
    private byte fMode,fFireGear,fTempGear;
    private byte tempSensorValue;
    private byte fanGear;
    private byte lightGear;

    public FTFDataModel(byte aMode, byte aFireGear, byte aTempGear, byte bMode, byte bFireGear, byte bTempGear, byte cMode, byte cFireGear, byte cTempGear, byte dMode, byte dFireGear, byte dTempGear, byte eMode, byte eFireGear, byte eTempGear, byte fMode, byte fFireGear, byte fTempGear, byte tempSensorValue, byte fanGear, byte lightGear) {
        this.aMode = aMode;
        this.aFireGear = aFireGear;
        this.aTempGear = aTempGear;
        this.bMode = bMode;
        this.bFireGear = bFireGear;
        this.bTempGear = bTempGear;
        this.cMode = cMode;
        this.cFireGear = cFireGear;
        this.cTempGear = cTempGear;
        this.dMode = dMode;
        this.dFireGear = dFireGear;
        this.dTempGear = dTempGear;
        this.eMode = eMode;
        this.eFireGear = eFireGear;
        this.eTempGear = eTempGear;
        this.fMode = fMode;
        this.fFireGear = fFireGear;
        this.fTempGear = fTempGear;
        this.tempSensorValue = tempSensorValue;
        this.fanGear = fanGear;
        this.lightGear = lightGear;
    }

    public byte getaMode() {
        return aMode;
    }

    public void setaMode(byte aMode) {
        this.aMode = aMode;
    }

    public byte getaFireGear() {
        return aFireGear;
    }

    public void setaFireGear(byte aFireGear) {
        this.aFireGear = aFireGear;
    }

    public byte getaTempGear() {
        return aTempGear;
    }

    public void setaTempGear(byte aTempGear) {
        this.aTempGear = aTempGear;
    }

    public byte getbMode() {
        return bMode;
    }

    public void setbMode(byte bMode) {
        this.bMode = bMode;
    }

    public byte getbFireGear() {
        return bFireGear;
    }

    public void setbFireGear(byte bFireGear) {
        this.bFireGear = bFireGear;
    }

    public byte getbTempGear() {
        return bTempGear;
    }

    public void setbTempGear(byte bTempGear) {
        this.bTempGear = bTempGear;
    }

    public byte getcMode() {
        return cMode;
    }

    public void setcMode(byte cMode) {
        this.cMode = cMode;
    }

    public byte getcFireGear() {
        return cFireGear;
    }

    public void setcFireGear(byte cFireGear) {
        this.cFireGear = cFireGear;
    }

    public byte getcTempGear() {
        return cTempGear;
    }

    public void setcTempGear(byte cTempGear) {
        this.cTempGear = cTempGear;
    }

    public byte getdMode() {
        return dMode;
    }

    public void setdMode(byte dMode) {
        this.dMode = dMode;
    }

    public byte getdFireGear() {
        return dFireGear;
    }

    public void setdFireGear(byte dFireGear) {
        this.dFireGear = dFireGear;
    }

    public byte getdTempGear() {
        return dTempGear;
    }

    public void setdTempGear(byte dTempGear) {
        this.dTempGear = dTempGear;
    }

    public byte geteMode() {
        return eMode;
    }

    public void seteMode(byte eMode) {
        this.eMode = eMode;
    }

    public byte geteFireGear() {
        return eFireGear;
    }

    public void seteFireGear(byte eFireGear) {
        this.eFireGear = eFireGear;
    }

    public byte geteTempGear() {
        return eTempGear;
    }

    public void seteTempGear(byte eTempGear) {
        this.eTempGear = eTempGear;
    }

    public byte getfMode() {
        return fMode;
    }

    public void setfMode(byte fMode) {
        this.fMode = fMode;
    }

    public byte getfFireGear() {
        return fFireGear;
    }

    public void setfFireGear(byte fFireGear) {
        this.fFireGear = fFireGear;
    }

    public byte getfTempGear() {
        return fTempGear;
    }

    public void setfTempGear(byte fTempGear) {
        this.fTempGear = fTempGear;
    }

    public byte getTempSensorValue() {
        return tempSensorValue;
    }

    public void setTempSensorValue(byte tempSensorValue) {
        this.tempSensorValue = tempSensorValue;
    }

    public byte getFanGear() {
        return fanGear;
    }

    public void setFanGear(byte fanGear) {
        this.fanGear = fanGear;
    }

    public byte getLightGear() {
        return lightGear;
    }

    public void setLightGear(byte lightGear) {
        this.lightGear = lightGear;
    }
}
