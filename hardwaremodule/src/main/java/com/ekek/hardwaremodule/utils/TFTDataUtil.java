package com.ekek.hardwaremodule.utils;

import com.ekek.hardwaremodule.entity.FTFDataModel;

public class TFTDataUtil {

    public static FTFDataModel makeFTFDataModel(byte aMode, byte aFireGear, byte aTempGear, byte bMode, byte bFireGear, byte bTempGear, byte cMode, byte cFireGear, byte cTempGear, byte dMode, byte dFireGear, byte dTempGear, byte eMode, byte eFireGear, byte eTempGear, byte fMode, byte fFireGear, byte fTempGear, byte tempSensorValue, byte fanGear, byte lightGear) {
        FTFDataModel ftfDataModel = new FTFDataModel(aMode, aFireGear, aTempGear, bMode, bFireGear, bTempGear, cMode, cFireGear, cTempGear, dMode, dFireGear, dTempGear, eMode, eFireGear, eTempGear, fMode, fFireGear, fTempGear, tempSensorValue, fanGear, lightGear);
        return ftfDataModel;
    }
}
