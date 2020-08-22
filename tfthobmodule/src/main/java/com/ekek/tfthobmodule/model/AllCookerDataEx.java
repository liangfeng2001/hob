package com.ekek.tfthobmodule.model;

import com.ekek.hardwaremodule.entity.AllCookerData;

public class AllCookerDataEx extends AllCookerData {

    private static final String FMT_RAW_DATA = "" +
            "A %02x %02x %02x B %02x %02x %02x C %02x %02x %02x " +
            "D %02x %02x %02x E %02x %02x %02x F %02x %02x %02x " +
            "P %02x H %02x L %02x"
            ;

    public AllCookerDataEx() {
        super();
    }
    public AllCookerDataEx(
            int aMode, int aFireValue, int aTempValue,
            int bMode, int bFireValue, int bTempValue,
            int cMode, int cFireValue, int cTempValue,
            int dMode, int dFireValue, int dTempValue,
            int eMode, int eFireValue, int eTempValue,
            int fMode, int fFireValue, int fTempValue,
            int bluetoothTempValue, int hoodValue, int lightValue) {
        super(
                aMode, aFireValue, aTempValue,
                bMode, bFireValue, bTempValue,
                cMode, cFireValue, cTempValue,
                dMode, dFireValue, dTempValue,
                eMode, eFireValue, eTempValue,
                fMode, fFireValue, fTempValue,
                bluetoothTempValue, hoodValue, lightValue);
    }

    public String toRawDataString() {
        return String.format(
                FMT_RAW_DATA,
                getaMode() & 0xFF, getaFireValue() & 0xFF, getaTempValue() & 0xFF,
                getbMode() & 0xFF, getbFireValue() & 0xFF, getbTempValue() & 0xFF,
                getcMode() & 0xFF, getcFireValue() & 0xFF, getcTempValue() & 0xFF,
                getdMode() & 0xFF, getdFireValue() & 0xFF, getdTempValue() & 0xFF,
                geteMode() & 0xFF, geteFireValue() & 0xFF, geteTempValue() & 0xFF,
                getfMode() & 0xFF, getfFireValue() & 0xFF, getfTempValue() & 0xFF,
                getBluetoothTempValue() & 0xFF, getHoodValue() & 0xFF, getLightValue() & 0xFF
        ).toUpperCase();
    }




    public AllCookerDataEx clone() {
        AllCookerDataEx r = new AllCookerDataEx();
        r.setaMode(getaMode());
        r.setbMode(getbMode());
        r.setcMode(getcMode());
        r.setdMode(getdMode());
        r.seteMode(geteMode());
        r.setfMode(getfMode());
        r.setaFireValue(getaFireValue());
        r.setbFireValue(getbFireValue());
        r.setcFireValue(getcFireValue());
        r.setdFireValue(getdFireValue());
        r.seteFireValue(geteFireValue());
        r.setfFireValue(getfFireValue());
        r.setaTempValue(getaTempValue());
        r.setbTempValue(getbTempValue());
        r.setcTempValue(getcTempValue());
        r.setdTempValue(getdTempValue());
        r.seteTempValue(geteTempValue());
        r.setfTempValue(getfTempValue());
        r.setLightValue(getLightValue());
        r.setHoodValue(getHoodValue());
        r.setBluetoothTempValue(getBluetoothTempValue());
        return r;
    }
    public void loadFrom(AllCookerData value) {
        if (value == null) return;

        setaMode(value.getaMode());
        setbMode(value.getbMode());
        setcMode(value.getcMode());
        setdMode(value.getdMode());
        seteMode(value.geteMode());
        setfMode(value.getfMode());
        setaFireValue(value.getaFireValue());
        setbFireValue(value.getbFireValue());
        setcFireValue(value.getcFireValue());
        setdFireValue(value.getdFireValue());
        seteFireValue(value.geteFireValue());
        setfFireValue(value.getfFireValue());
        setaTempValue(value.getaTempValue());
        setbTempValue(value.getbTempValue());
        setcTempValue(value.getcTempValue());
        setdTempValue(value.getdTempValue());
        seteTempValue(value.geteTempValue());
        setfTempValue(value.getfTempValue());
        setLightValue(value.getLightValue());
        setHoodValue(value.getHoodValue());
        setBluetoothTempValue(value.getBluetoothTempValue());
    }
}
