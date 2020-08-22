package com.ekek.tfthobmodule.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CookerDataTable {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "CookerID")
    private int cookerID;
    @Property(nameInDb = "WorkMode")
    private int workMode;
    @Property(nameInDb = "HardwareWorkMode")
    private int hardwareWorkMode;
    @Property(nameInDb = "TempMode")
    private int tempMode;
    @Property(nameInDb = "FireValue")
    private int fireValue;
    @Property(nameInDb = "TempValue")
    private int tempValue;
    @Property(nameInDb = "NTCTempValue")
    private int ntcTempValue;
    @Property(nameInDb = "RemainHourValue")
    private int remainHourValue;
    @Property(nameInDb = "RemainMinuteValue")
    private int remainMinuteValue;
    @Property(nameInDb = "RemainSecondValue")
    private int remainSecondValue;
    @Property(nameInDb = "TempIndicatorResID")
    private int tempIndicatorResID;
    @Property(nameInDb = "RecipesResID")
    private int recipesResID;
    @Property(nameInDb = "HighTempFlag")
    private boolean highTempFlag;
    @Property(nameInDb = "PanFlag")
    private boolean panFlag;
    @Property(nameInDb = "ErrorMessage")
    private String errorMessage;
    @Property(nameInDb = "PowerOnFlag")
    private boolean powerOnFlag;
    @Property(nameInDb = "RecoverFlag")
    private boolean recoverFlag;

    @Property(nameInDb = "SaveWorkMode")
    private int saveWorkMode;
    @Property(nameInDb = "SaveHardwareWorkMode")
    private int saveHardwareWorkMode;
    @Property(nameInDb = "SaveTempMode")
    private int saveTempMode;
    @Property(nameInDb = "SaveFireValue")
    private int saveFireValue;
    @Property(nameInDb = "SaveTempValue")
    private int saveTempValue;
    @Property(nameInDb = "SaveRemainHourValue")
    private int saveRemainHourValue;
    @Property(nameInDb = "SaveRemainMinuteValue")
    private int saveRemainMinuteValue;
    @Property(nameInDb = "SaveRemainSecondValue")
    private int saveRemainSecondValue;
    @Property(nameInDb = "SaveTempIndicatorResID")
    private int saveTempIndicatorResID;
    @Property(nameInDb = "SaveRecipesResID")
    private int saveRecipesResID;
    @Property(nameInDb = "SaveHighTempFlag")
    private boolean saveHighTempFlag;
    @Property(nameInDb = "SavePanFlag")
    private boolean savePanFlag;
    @Property(nameInDb = "SaveErrorMessage")
    private String saveErrorMessage;
    @Property(nameInDb = "SavePowerOnFlag")
    private boolean savePowerOnFlag;
    @Property(nameInDb = "SaveRecoverFlag")
    private boolean saveRecoverFlag;

    @Property(nameInDb = "SettingTempMode")
    private int settingTempMode;
    @Property(nameInDb = "SettingFireValue")
    private int settingFireValue;
    @Property(nameInDb = "SettingTempValue")
    private int settingtempValue;
    @Property(nameInDb = "SettingHourValue")
    private int settingHourValue;
    @Property(nameInDb = "SettingMinuteValue")
    private int settingMinuteValue;
    @Property(nameInDb = "SettingSecondValue")
    private int settingSecondValue;
    @Property(nameInDb = "SettingTempIndicatorResID")
    private int settingtempIndicatorResID;
    @Property(nameInDb = "SettingRecipesResID")
    private int settingrecipesResID;
    @Property(nameInDb = "Reserve")
    private String reserve;
    @Generated(hash = 1695006165)
    public CookerDataTable(Long id, int cookerID, int workMode,
            int hardwareWorkMode, int tempMode, int fireValue, int tempValue,
            int ntcTempValue, int remainHourValue, int remainMinuteValue,
            int remainSecondValue, int tempIndicatorResID, int recipesResID,
            boolean highTempFlag, boolean panFlag, String errorMessage,
            boolean powerOnFlag, boolean recoverFlag, int saveWorkMode,
            int saveHardwareWorkMode, int saveTempMode, int saveFireValue,
            int saveTempValue, int saveRemainHourValue, int saveRemainMinuteValue,
            int saveRemainSecondValue, int saveTempIndicatorResID,
            int saveRecipesResID, boolean saveHighTempFlag, boolean savePanFlag,
            String saveErrorMessage, boolean savePowerOnFlag,
            boolean saveRecoverFlag, int settingTempMode, int settingFireValue,
            int settingtempValue, int settingHourValue, int settingMinuteValue,
            int settingSecondValue, int settingtempIndicatorResID,
            int settingrecipesResID, String reserve) {
        this.id = id;
        this.cookerID = cookerID;
        this.workMode = workMode;
        this.hardwareWorkMode = hardwareWorkMode;
        this.tempMode = tempMode;
        this.fireValue = fireValue;
        this.tempValue = tempValue;
        this.ntcTempValue = ntcTempValue;
        this.remainHourValue = remainHourValue;
        this.remainMinuteValue = remainMinuteValue;
        this.remainSecondValue = remainSecondValue;
        this.tempIndicatorResID = tempIndicatorResID;
        this.recipesResID = recipesResID;
        this.highTempFlag = highTempFlag;
        this.panFlag = panFlag;
        this.errorMessage = errorMessage;
        this.powerOnFlag = powerOnFlag;
        this.recoverFlag = recoverFlag;
        this.saveWorkMode = saveWorkMode;
        this.saveHardwareWorkMode = saveHardwareWorkMode;
        this.saveTempMode = saveTempMode;
        this.saveFireValue = saveFireValue;
        this.saveTempValue = saveTempValue;
        this.saveRemainHourValue = saveRemainHourValue;
        this.saveRemainMinuteValue = saveRemainMinuteValue;
        this.saveRemainSecondValue = saveRemainSecondValue;
        this.saveTempIndicatorResID = saveTempIndicatorResID;
        this.saveRecipesResID = saveRecipesResID;
        this.saveHighTempFlag = saveHighTempFlag;
        this.savePanFlag = savePanFlag;
        this.saveErrorMessage = saveErrorMessage;
        this.savePowerOnFlag = savePowerOnFlag;
        this.saveRecoverFlag = saveRecoverFlag;
        this.settingTempMode = settingTempMode;
        this.settingFireValue = settingFireValue;
        this.settingtempValue = settingtempValue;
        this.settingHourValue = settingHourValue;
        this.settingMinuteValue = settingMinuteValue;
        this.settingSecondValue = settingSecondValue;
        this.settingtempIndicatorResID = settingtempIndicatorResID;
        this.settingrecipesResID = settingrecipesResID;
        this.reserve = reserve;
    }
    @Generated(hash = 532621125)
    public CookerDataTable() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getCookerID() {
        return this.cookerID;
    }
    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }
    public int getWorkMode() {
        return this.workMode;
    }
    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }
    public int getHardwareWorkMode() {
        return this.hardwareWorkMode;
    }
    public void setHardwareWorkMode(int hardwareWorkMode) {
        this.hardwareWorkMode = hardwareWorkMode;
    }
    public int getTempMode() {
        return this.tempMode;
    }
    public void setTempMode(int tempMode) {
        this.tempMode = tempMode;
    }
    public int getFireValue() {
        return this.fireValue;
    }
    public void setFireValue(int fireValue) {
        this.fireValue = fireValue;
    }
    public int getTempValue() {
        return this.tempValue;
    }
    public void setTempValue(int tempValue) {
        this.tempValue = tempValue;
    }
    public int getNtcTempValue() {
        return this.ntcTempValue;
    }
    public void setNtcTempValue(int ntcTempValue) {
        this.ntcTempValue = ntcTempValue;
    }
    public int getRemainHourValue() {
        return this.remainHourValue;
    }
    public void setRemainHourValue(int remainHourValue) {
        this.remainHourValue = remainHourValue;
    }
    public int getRemainMinuteValue() {
        return this.remainMinuteValue;
    }
    public void setRemainMinuteValue(int remainMinuteValue) {
        this.remainMinuteValue = remainMinuteValue;
    }
    public int getRemainSecondValue() {
        return this.remainSecondValue;
    }
    public void setRemainSecondValue(int remainSecondValue) {
        this.remainSecondValue = remainSecondValue;
    }
    public int getTempIndicatorResID() {
        return this.tempIndicatorResID;
    }
    public void setTempIndicatorResID(int tempIndicatorResID) {
        this.tempIndicatorResID = tempIndicatorResID;
    }
    public int getRecipesResID() {
        return this.recipesResID;
    }
    public void setRecipesResID(int recipesResID) {
        this.recipesResID = recipesResID;
    }
    public boolean getHighTempFlag() {
        return this.highTempFlag;
    }
    public void setHighTempFlag(boolean highTempFlag) {
        this.highTempFlag = highTempFlag;
    }
    public boolean getPanFlag() {
        return this.panFlag;
    }
    public void setPanFlag(boolean panFlag) {
        this.panFlag = panFlag;
    }
    public String getErrorMessage() {
        return this.errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public boolean getPowerOnFlag() {
        return this.powerOnFlag;
    }
    public void setPowerOnFlag(boolean powerOnFlag) {
        this.powerOnFlag = powerOnFlag;
    }
    public boolean getRecoverFlag() {
        return this.recoverFlag;
    }
    public void setRecoverFlag(boolean recoverFlag) {
        this.recoverFlag = recoverFlag;
    }
    public int getSaveWorkMode() {
        return this.saveWorkMode;
    }
    public void setSaveWorkMode(int saveWorkMode) {
        this.saveWorkMode = saveWorkMode;
    }
    public int getSaveHardwareWorkMode() {
        return this.saveHardwareWorkMode;
    }
    public void setSaveHardwareWorkMode(int saveHardwareWorkMode) {
        this.saveHardwareWorkMode = saveHardwareWorkMode;
    }
    public int getSaveTempMode() {
        return this.saveTempMode;
    }
    public void setSaveTempMode(int saveTempMode) {
        this.saveTempMode = saveTempMode;
    }
    public int getSaveFireValue() {
        return this.saveFireValue;
    }
    public void setSaveFireValue(int saveFireValue) {
        this.saveFireValue = saveFireValue;
    }
    public int getSaveTempValue() {
        return this.saveTempValue;
    }
    public void setSaveTempValue(int saveTempValue) {
        this.saveTempValue = saveTempValue;
    }
    public int getSaveRemainHourValue() {
        return this.saveRemainHourValue;
    }
    public void setSaveRemainHourValue(int saveRemainHourValue) {
        this.saveRemainHourValue = saveRemainHourValue;
    }
    public int getSaveRemainMinuteValue() {
        return this.saveRemainMinuteValue;
    }
    public void setSaveRemainMinuteValue(int saveRemainMinuteValue) {
        this.saveRemainMinuteValue = saveRemainMinuteValue;
    }
    public int getSaveRemainSecondValue() {
        return this.saveRemainSecondValue;
    }
    public void setSaveRemainSecondValue(int saveRemainSecondValue) {
        this.saveRemainSecondValue = saveRemainSecondValue;
    }
    public int getSaveTempIndicatorResID() {
        return this.saveTempIndicatorResID;
    }
    public void setSaveTempIndicatorResID(int saveTempIndicatorResID) {
        this.saveTempIndicatorResID = saveTempIndicatorResID;
    }
    public int getSaveRecipesResID() {
        return this.saveRecipesResID;
    }
    public void setSaveRecipesResID(int saveRecipesResID) {
        this.saveRecipesResID = saveRecipesResID;
    }
    public boolean getSaveHighTempFlag() {
        return this.saveHighTempFlag;
    }
    public void setSaveHighTempFlag(boolean saveHighTempFlag) {
        this.saveHighTempFlag = saveHighTempFlag;
    }
    public boolean getSavePanFlag() {
        return this.savePanFlag;
    }
    public void setSavePanFlag(boolean savePanFlag) {
        this.savePanFlag = savePanFlag;
    }
    public String getSaveErrorMessage() {
        return this.saveErrorMessage;
    }
    public void setSaveErrorMessage(String saveErrorMessage) {
        this.saveErrorMessage = saveErrorMessage;
    }
    public boolean getSavePowerOnFlag() {
        return this.savePowerOnFlag;
    }
    public void setSavePowerOnFlag(boolean savePowerOnFlag) {
        this.savePowerOnFlag = savePowerOnFlag;
    }
    public boolean getSaveRecoverFlag() {
        return this.saveRecoverFlag;
    }
    public void setSaveRecoverFlag(boolean saveRecoverFlag) {
        this.saveRecoverFlag = saveRecoverFlag;
    }
    public int getSettingTempMode() {
        return this.settingTempMode;
    }
    public void setSettingTempMode(int settingTempMode) {
        this.settingTempMode = settingTempMode;
    }
    public int getSettingFireValue() {
        return this.settingFireValue;
    }
    public void setSettingFireValue(int settingFireValue) {
        this.settingFireValue = settingFireValue;
    }
    public int getSettingtempValue() {
        return this.settingtempValue;
    }
    public void setSettingtempValue(int settingtempValue) {
        this.settingtempValue = settingtempValue;
    }
    public int getSettingHourValue() {
        return this.settingHourValue;
    }
    public void setSettingHourValue(int settingHourValue) {
        this.settingHourValue = settingHourValue;
    }
    public int getSettingMinuteValue() {
        return this.settingMinuteValue;
    }
    public void setSettingMinuteValue(int settingMinuteValue) {
        this.settingMinuteValue = settingMinuteValue;
    }
    public int getSettingSecondValue() {
        return this.settingSecondValue;
    }
    public void setSettingSecondValue(int settingSecondValue) {
        this.settingSecondValue = settingSecondValue;
    }
    public int getSettingtempIndicatorResID() {
        return this.settingtempIndicatorResID;
    }
    public void setSettingtempIndicatorResID(int settingtempIndicatorResID) {
        this.settingtempIndicatorResID = settingtempIndicatorResID;
    }
    public int getSettingrecipesResID() {
        return this.settingrecipesResID;
    }
    public void setSettingrecipesResID(int settingrecipesResID) {
        this.settingrecipesResID = settingrecipesResID;
    }
    public String getReserve() {
        return this.reserve;
    }
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
   

}
