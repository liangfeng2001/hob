package com.ekek.hardwaremodule.entity;

public class AllCookerData {
    private int aMode, aFireValue, aTempValue; //A炉 ： 模式， 档位值，温度值
    private int bMode, bFireValue, bTempValue; //B炉 ： 模式， 档位值，温度值
    private int cMode, cFireValue, cTempValue; //C炉 ： 模式， 档位值，温度值
    private int dMode, dFireValue, dTempValue; //D炉 ： 模式， 档位值，温度值
    private int eMode, eFireValue, eTempValue; //E炉 ： 模式， 档位值，温度值
    private int fMode, fFireValue, fTempValue; //F炉 ： 模式， 档位值，温度值
    private int bluetoothTempValue;//蓝牙温度值
    private int hoodValue;//烟机档位模式值
    private int lightValue;//烟机灯光档位值

    /**
     * 初始化炉头数据
     */
    public AllCookerData() {

    }


    /**
     * 初始化炉头数据
     *
     * @param aMode              A炉模式
     * @param aFireValue         A炉档位值
     * @param aTempValue         A炉温度值
     * @param bMode              B炉模式
     * @param bFireValue         B炉档位值
     * @param bTempValue         B炉温度值
     * @param cMode              C炉模式
     * @param cFireValue         C炉档位值
     * @param cTempValue         C炉温度值
     * @param dMode              D炉模式
     * @param dFireValue         D炉档位值
     * @param dTempValue         D炉温度值
     * @param eMode              E炉模式
     * @param eFireValue         E炉档位值
     * @param eTempValue         E炉温度值
     * @param fMode              F炉模式
     * @param fFireValue         F炉档位值
     * @param fTempValue         F炉温度值
     * @param bluetoothTempValue 蓝牙温度值
     * @param hoodValue          烟机模式档位值
     * @param lightValue         烟机灯光值
     */
    public AllCookerData(int aMode, int aFireValue, int aTempValue,
                         int bMode, int bFireValue, int bTempValue,
                         int cMode, int cFireValue, int cTempValue,
                         int dMode, int dFireValue, int dTempValue,
                         int eMode, int eFireValue, int eTempValue,
                         int fMode, int fFireValue, int fTempValue,
                         int bluetoothTempValue, int hoodValue, int lightValue
    ) {

        this.aMode = aMode;
        this.aFireValue = aFireValue;
        this.aTempValue = aTempValue;

        this.bMode = bMode;
        this.bFireValue = bFireValue;
        this.bTempValue = bTempValue;

        this.cMode = cMode;
        this.cFireValue = cFireValue;
        this.cTempValue = cTempValue;

        this.dMode = dMode;
        this.dFireValue = dFireValue;
        this.dTempValue = dTempValue;

        this.eMode = eMode;
        this.eFireValue = eFireValue;
        this.eTempValue = eTempValue;

        this.fMode = fMode;
        this.fFireValue = fFireValue;
        this.fTempValue = fTempValue;

        this.bluetoothTempValue = bluetoothTempValue;
        this.hoodValue = hoodValue;
        this.lightValue = lightValue;

    }

    public int getaMode() {
        return aMode;
    }

    public void setaMode(int aMode) {
        this.aMode = aMode;
    }

    public int getaFireValue() {
        return aFireValue;
    }

    public void setaFireValue(int aFireValue) {
        this.aFireValue = aFireValue;
    }

    public int getaTempValue() {
        return aTempValue;
    }

    public void setaTempValue(int aTempValue) {
        this.aTempValue = aTempValue;
    }

    public int getbMode() {
        return bMode;
    }

    public void setbMode(int bMode) {
        this.bMode = bMode;
    }

    public int getbFireValue() {
        return bFireValue;
    }

    public void setbFireValue(int bFireValue) {
        this.bFireValue = bFireValue;
    }

    public int getbTempValue() {
        return bTempValue;
    }

    public void setbTempValue(int bTempValue) {
        this.bTempValue = bTempValue;
    }

    public int getcMode() {
        return cMode;
    }

    public void setcMode(int cMode) {
        this.cMode = cMode;
    }

    public int getcFireValue() {
        return cFireValue;
    }

    public void setcFireValue(int cFireValue) {
        this.cFireValue = cFireValue;
    }

    public int getcTempValue() {
        return cTempValue;
    }

    public void setcTempValue(int cTempValue) {
        this.cTempValue = cTempValue;
    }

    public int getdMode() {
        return dMode;
    }

    public void setdMode(int dMode) {
        this.dMode = dMode;
    }

    public int getdFireValue() {
        return dFireValue;
    }

    public void setdFireValue(int dFireValue) {
        this.dFireValue = dFireValue;
    }

    public int getdTempValue() {
        return dTempValue;
    }

    public void setdTempValue(int dTempValue) {
        this.dTempValue = dTempValue;
    }

    public int geteMode() {
        return eMode;
    }

    public void seteMode(int eMode) {
        this.eMode = eMode;
    }

    public int geteFireValue() {
        return eFireValue;
    }

    public void seteFireValue(int eFireValue) {
        this.eFireValue = eFireValue;
    }

    public int geteTempValue() {
        return eTempValue;
    }

    public void seteTempValue(int eTempValue) {
        this.eTempValue = eTempValue;
    }

    public int getfMode() {
        return fMode;
    }

    public void setfMode(int fMode) {
        this.fMode = fMode;
    }

    public int getfFireValue() {
        return fFireValue;
    }

    public void setfFireValue(int fFireValue) {
        this.fFireValue = fFireValue;
    }

    public int getfTempValue() {
        return fTempValue;
    }

    public void setfTempValue(int fTempValue) {
        this.fTempValue = fTempValue;
    }

    public int getBluetoothTempValue() {
        return bluetoothTempValue;
    }

    public void setBluetoothTempValue(int bluetoothTempValue) {
        this.bluetoothTempValue = bluetoothTempValue;
    }

    public int getHoodValue() {
        return hoodValue;
    }

    public void setHoodValue(int hoodValue) {
        this.hoodValue = hoodValue;
    }

    public int getLightValue() {
        return lightValue;
    }

    public void setLightValue(int lightValue) {
        this.lightValue = lightValue;
    }

    @Override
    public String toString() {
       return  "A:Mode-->" + aMode + "--aFire--->" + aFireValue + "--aTemp--->" + aTempValue + "\r\n" +
                "B:Mode-->" + bMode + "--bFire--->" + bFireValue + "--bTemp--->" + bTempValue + "\r\n" +
                "C:Mode-->" + cMode + "--cFire--->" + cFireValue + "--cTemp--->" + cTempValue + "\r\n" +
                "D:Mode-->" + dMode + "--dFire--->" + dFireValue + "--dTemp--->" + dTempValue + "\r\n" +
                "E:Mode-->" + eMode + "--eFire--->" + eFireValue + "--eTemp--->" + eTempValue + "\r\n" +
                "F:Mode-->" + fMode + "--fFire--->" + fFireValue + "--fTemp--->" + fTempValue + "\r\n" +
                "BlueTempValue--->" + bluetoothTempValue + "---HoodValue--->" + hoodValue + "---LightValue--->" + lightValue + "\r\n";

    }
}
