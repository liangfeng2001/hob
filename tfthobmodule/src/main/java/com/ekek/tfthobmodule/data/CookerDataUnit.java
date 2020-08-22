package com.ekek.tfthobmodule.data;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.entity.CookerDataTable;
import com.ekek.tfthobmodule.entity.CookerDataTableDao;

public class CookerDataUnit {
    private int cookerID;
    private int workMode = TFTHobConstant.HOB_WORK_MODE_POWER_OFF;
    private int hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
    private int fireValue,tempValue,tempMode;
    private int remainHourValue,remainMinuteValue,remainSecondValue;
    private int tempIndicatorResID,recipesResID;
    private String errorMessage = "";
    private boolean highTempFlag = false;
    private boolean recoverUIForErrorFlag = false;
    private boolean powerOnFlag = false;
    private boolean havePanFlag = true;

    //save
    private int saveWorkMode = TFTHobConstant.HOB_WORK_MODE_POWER_OFF;
    private int saveHardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
    private int saveFireValue,saveTempValue,saveTempMode;
    private int saveRemainHourValue,saveRemainMinuteValue,saveRemainSecondValue;
    private int saveTempIndicatorResID,saveRecipesResID;
    private String saveeErorMessage = "";
    private boolean saveHighTempFlag = false;
    private boolean saveRecoverUIForErrorFlag = false;
    private boolean savePowerOnFlag = false;
    private boolean saveHavePanFlag = true;


    //setting
    private int settingWorkMode = TFTHobConstant.HOB_WORK_MODE_POWER_OFF;
    private int settingHardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
    private int settingFireValue,settingTempValue,settingTempMode;
    private int settingHourValue,settingMinuteValue,settingSecondValue;
    private int settingTempIndicatorResID,settingRecipesResID;
    private int settingTempShowOrder;
    private String settingErrorMessage = "";
    private boolean settingHighTempFlag = false;
    private boolean settingRecoverUIForErrorFlag = false;
    private boolean settingPowerOnFlag = false;
    private boolean settingHavePanFlag = true;

    private int remainFiveMinuteHourValue = 5;

    public void setCookerSettingData(CookerSettingData data) {

        fireValue = data.getFireValue();
        tempValue = data.getTempValue();
        tempMode = data.getTempMode();
        tempIndicatorResID = data.getTempIdentifyDrawableResourceID();
        recipesResID = data.getTempRecipesResID();
        powerOnFlag = true;
        havePanFlag = true;
        settingTempShowOrder = data.getTempShowOrder();
        copyDataToSaveData();
        copyDataToSettingData();

        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            if (cookerData.getSettingHourValue() != data.getTimerHourValue() || cookerData.getSettingMinuteValue() != data.getTimerMinuteValue()) {
                remainHourValue = data.getTimerHourValue();
                remainMinuteValue = data.getTimerMinuteValue();
                remainSecondValue = data.getTimerSecondValue();

                settingHourValue = data.getTimerHourValue();
                settingMinuteValue = data.getTimerMinuteValue();
                settingSecondValue = data.getTimerSecondValue();

                saveRemainHourValue = remainHourValue;
                saveRemainMinuteValue = remainMinuteValue;
                saveRemainSecondValue = remainSecondValue;
            }

        }else {
            remainHourValue = data.getTimerHourValue();
            remainMinuteValue = data.getTimerMinuteValue();
            remainSecondValue = data.getTimerSecondValue();

            settingHourValue = data.getTimerHourValue();
            settingMinuteValue = data.getTimerMinuteValue();
            settingSecondValue = data.getTimerSecondValue();

            saveRemainHourValue = remainHourValue;
            saveRemainMinuteValue = remainMinuteValue;
            saveRemainSecondValue = remainSecondValue;
        }



    }

    private void copyDataToSaveData() {
        saveFireValue = fireValue;
        saveTempValue = tempValue;
        saveTempMode = tempMode;
        saveTempIndicatorResID = tempIndicatorResID;
        saveRecipesResID = recipesResID;
        savePowerOnFlag = powerOnFlag;
        saveHavePanFlag = powerOnFlag;

    }

    private void copyDataToSettingData() {
        settingFireValue = fireValue;
        settingTempValue = tempValue;
        settingTempMode = tempMode;
        settingTempIndicatorResID = tempIndicatorResID;
        settingRecipesResID = recipesResID;
        settingPowerOnFlag = powerOnFlag;
        settingHavePanFlag = powerOnFlag;
    }

    public void saveDataToDatabase() {
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData == null) {
            cookerData = new CookerDataTable(null,
                    cookerID,workMode,hardwareWorkMode,tempMode,fireValue,tempValue,0,remainHourValue,remainMinuteValue,remainSecondValue,tempIndicatorResID,recipesResID,highTempFlag,havePanFlag,errorMessage,powerOnFlag,recoverUIForErrorFlag,
                    saveWorkMode,saveHardwareWorkMode,saveTempMode,saveFireValue,saveTempValue,saveRemainHourValue,saveRemainMinuteValue,saveRemainSecondValue,saveTempIndicatorResID,saveRecipesResID,saveHighTempFlag,saveHavePanFlag,saveeErorMessage,savePowerOnFlag,saveRecoverUIForErrorFlag,
                    settingTempMode,settingFireValue,settingTempValue,settingHourValue,settingMinuteValue,settingSecondValue,settingTempIndicatorResID,settingRecipesResID,""
            );
            dao.insertOrReplace(cookerData);
        }else {
            cookerData.setFireValue(fireValue);
            cookerData.setTempValue(tempValue);
            cookerData.setTempMode(tempMode);
            cookerData.setRemainHourValue(remainHourValue);
            cookerData.setRemainMinuteValue(remainMinuteValue);
            cookerData.setRemainSecondValue(remainSecondValue);
            cookerData.setWorkMode(workMode);
            cookerData.setHardwareWorkMode(hardwareWorkMode);
            cookerData.setTempIndicatorResID(tempIndicatorResID);
            cookerData.setRecipesResID(recipesResID);
            cookerData.setHighTempFlag(highTempFlag);
            cookerData.setPanFlag(havePanFlag);
            cookerData.setErrorMessage(errorMessage);
            cookerData.setPowerOnFlag(powerOnFlag);
            cookerData.setRecoverFlag(recoverUIForErrorFlag);

            cookerData.setSaveFireValue(saveFireValue);
            cookerData.setSaveTempValue(saveTempValue);
            cookerData.setSaveTempMode(saveTempMode);
            cookerData.setSaveRemainHourValue(saveRemainHourValue);
            cookerData.setSaveRemainMinuteValue(saveRemainMinuteValue);
            cookerData.setSaveRemainSecondValue(saveRemainSecondValue);
            cookerData.setSaveWorkMode(saveWorkMode);
            cookerData.setSaveTempIndicatorResID(saveTempIndicatorResID);
            cookerData.setSaveRecipesResID(saveRecipesResID);
            cookerData.setSaveHighTempFlag(saveHighTempFlag);
            cookerData.setSavePanFlag(saveHavePanFlag);
            cookerData.setSaveErrorMessage(saveeErorMessage);
            cookerData.setSavePowerOnFlag(savePowerOnFlag);
            cookerData.setSaveRecoverFlag(saveRecoverUIForErrorFlag);

            cookerData.setSettingFireValue(settingFireValue);
            cookerData.setSettingTempMode(settingTempMode);
            cookerData.setSettingtempValue(settingTempValue);
            cookerData.setSettingHourValue(settingHourValue);
            cookerData.setSettingMinuteValue(settingMinuteValue);
            cookerData.setSettingSecondValue(settingSecondValue);
            cookerData.setSettingrecipesResID(settingRecipesResID);
            cookerData.setSettingtempIndicatorResID(settingTempIndicatorResID);
            dao.update(cookerData);


        }

    }

    public void saveFireGearUIForBGearTimeIsUp() {
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            cookerData.setFireValue(fireValue);
            cookerData.setSettingFireValue(settingFireValue);
            cookerData.setSaveFireValue(saveFireValue);
            cookerDataTableDao.update(cookerData);
        }
    }

    public void saveDataForTempTimerIsUp() {
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            LogUtil.d("cooker data  !=       null");
            cookerData.setWorkMode(workMode);
            cookerData.setTempValue(tempValue);
            cookerDataTableDao.update(cookerData);
        }else {
            LogUtil.d("cooker data  ==       null");
        }
    }

    public int getCookerID() {
        return cookerID;
    }

    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }

    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public int getHardwareWorkMode() {
        return hardwareWorkMode;
    }

    public void setHardwareWorkMode(int hardwareWorkMode) {
        this.hardwareWorkMode = hardwareWorkMode;
    }

    public int getFireValue() {
        return fireValue;
    }

    public void setFireValue(int fireValue) {
        this.fireValue = fireValue;
    }

    public int getTempValue() {
        return tempValue;
    }

    public void setTempValue(int tempValue) {
        this.tempValue = tempValue;
    }

    public int getTempMode() {
        return tempMode;
    }

    public void setTempMode(int tempMode) {
        this.tempMode = tempMode;
    }

    public int getRemainHourValue() {
        return remainHourValue;
    }

    public void setRemainHourValue(int remainHourValue) {
        this.remainHourValue = remainHourValue;
    }

    public int getRemainMinuteValue() {
        return remainMinuteValue;
    }

    public void setRemainMinuteValue(int remainMinuteValue) {
        this.remainMinuteValue = remainMinuteValue;
    }

    public int getRemainSecondValue() {
        return remainSecondValue;
    }

    public void setRemainSecondValue(int remainSecondValue) {
        this.remainSecondValue = remainSecondValue;
    }

    public int getTempIndicatorResID() {
        return tempIndicatorResID;
    }

    public void setTempIndicatorResID(int tempIndicatorResID) {
        this.tempIndicatorResID = tempIndicatorResID;
    }

    public int getRecipesResID() {
        return recipesResID;
    }

    public void setRecipesResID(int recipesResID) {
        this.recipesResID = recipesResID;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isHighTempFlag() {
        return highTempFlag;
    }

    public void setHighTempFlag(boolean highTempFlag) {
        this.highTempFlag = highTempFlag;
    }

    public boolean isRecoverUIForErrorFlag() {
        return recoverUIForErrorFlag;
    }

    public void setRecoverUIForErrorFlag(boolean recoverUIForErrorFlag) {
        this.recoverUIForErrorFlag = recoverUIForErrorFlag;
    }

    public boolean isPowerOnFlag() {
        return powerOnFlag;
    }

    public void setPowerOnFlag(boolean powerOnFlag) {
        this.powerOnFlag = powerOnFlag;
    }

    public boolean isHavePanFlag() {
        return havePanFlag;
    }

    public void setHavePanFlag(boolean havePanFlag) {
        this.havePanFlag = havePanFlag;
    }

    public int getSaveWorkMode() {
        return saveWorkMode;
    }

    public void setSaveWorkMode(int saveWorkMode) {
        this.saveWorkMode = saveWorkMode;
    }

    public int getSaveHardwareWorkMode() {
        return saveHardwareWorkMode;
    }

    public void setSaveHardwareWorkMode(int saveHardwareWorkMode) {
        this.saveHardwareWorkMode = saveHardwareWorkMode;
    }

    public int getSaveFireValue() {
        return saveFireValue;
    }

    public void setSaveFireValue(int saveFireValue) {
        this.saveFireValue = saveFireValue;
    }

    public int getSaveTempValue() {
        return saveTempValue;
    }

    public void setSaveTempValue(int saveTempValue) {
        this.saveTempValue = saveTempValue;
    }

    public int getSaveTempMode() {
        return saveTempMode;
    }

    public void setSaveTempMode(int saveTempMode) {
        this.saveTempMode = saveTempMode;
    }

    public int getSaveRemainHourValue() {
        return saveRemainHourValue;
    }

    public void setSaveRemainHourValue(int saveRemainHourValue) {
        this.saveRemainHourValue = saveRemainHourValue;
    }

    public int getSaveRemainMinuteValue() {
        return saveRemainMinuteValue;
    }

    public void setSaveRemainMinuteValue(int saveRemainMinuteValue) {
        this.saveRemainMinuteValue = saveRemainMinuteValue;
    }

    public int getSaveRemainSecondValue() {
        return saveRemainSecondValue;
    }

    public void setSaveRemainSecondValue(int saveRemainSecondValue) {
        this.saveRemainSecondValue = saveRemainSecondValue;
    }

    public int getSaveTempIndicatorResID() {
        return saveTempIndicatorResID;
    }

    public void setSaveTempIndicatorResID(int saveTempIndicatorResID) {
        this.saveTempIndicatorResID = saveTempIndicatorResID;
    }

    public int getSaveRecipesResID() {
        return saveRecipesResID;
    }

    public void setSaveRecipesResID(int saveRecipesResID) {
        this.saveRecipesResID = saveRecipesResID;
    }

    public String getSaveeErorMessage() {
        return saveeErorMessage;
    }

    public void setSaveeErorMessage(String saveeErorMessage) {
        this.saveeErorMessage = saveeErorMessage;
    }

    public boolean isSaveHighTempFlag() {
        return saveHighTempFlag;
    }

    public void setSaveHighTempFlag(boolean saveHighTempFlag) {
        this.saveHighTempFlag = saveHighTempFlag;
    }

    public boolean isSaveRecoverUIForErrorFlag() {
        return saveRecoverUIForErrorFlag;
    }

    public void setSaveRecoverUIForErrorFlag(boolean saveRecoverUIForErrorFlag) {
        this.saveRecoverUIForErrorFlag = saveRecoverUIForErrorFlag;
    }

    public boolean isSavePowerOnFlag() {
        return savePowerOnFlag;
    }

    public void setSavePowerOnFlag(boolean savePowerOnFlag) {
        this.savePowerOnFlag = savePowerOnFlag;
    }

    public boolean isSaveHavePanFlag() {
        return saveHavePanFlag;
    }

    public void setSaveHavePanFlag(boolean saveHavePanFlag) {
        this.saveHavePanFlag = saveHavePanFlag;
    }

    public int getSettingWorkMode() {
        return settingWorkMode;
    }

    public void setSettingWorkMode(int settingWorkMode) {
        this.settingWorkMode = settingWorkMode;
    }

    public int getSettingHardwareWorkMode() {
        return settingHardwareWorkMode;
    }

    public void setSettingHardwareWorkMode(int settingHardwareWorkMode) {
        this.settingHardwareWorkMode = settingHardwareWorkMode;
    }

    public int getSettingFireValue() {
        return settingFireValue;
    }

    public void setSettingFireValue(int settingFireValue) {
        this.settingFireValue = settingFireValue;
    }

    public int getSettingTempValue() {
        return settingTempValue;
    }

    public void setSettingTempValue(int settingTempValue) {
        this.settingTempValue = settingTempValue;
    }

    public int getSettingTempMode() {
        return settingTempMode;
    }

    public void setSettingTempMode(int settingTempMode) {
        this.settingTempMode = settingTempMode;
    }

    public int getSettingHourValue() {
        return settingHourValue;
    }

    public void setSettingHourValue(int settingHourValue) {
        this.settingHourValue = settingHourValue;
    }

    public int getSettingMinuteValue() {
        return settingMinuteValue;
    }

    public void setSettingMinuteValue(int settingMinuteValue) {
        this.settingMinuteValue = settingMinuteValue;
    }

    public int getSettingSecondValue() {
        return settingSecondValue;
    }

    public void setSettingSecondValue(int settingSecondValue) {
        this.settingSecondValue = settingSecondValue;
    }

    public int getSettingTempIndicatorResID() {
        return settingTempIndicatorResID;
    }

    public void setSettingTempIndicatorResID(int settingTempIndicatorResID) {
        this.settingTempIndicatorResID = settingTempIndicatorResID;
    }

    public int getSettingRecipesResID() {
        return settingRecipesResID;
    }

    public void setSettingRecipesResID(int settingRecipesResID) {
        this.settingRecipesResID = settingRecipesResID;
    }

    public String getSettingErrorMessage() {
        return settingErrorMessage;
    }

    public void setSettingErrorMessage(String settingErrorMessage) {
        this.settingErrorMessage = settingErrorMessage;
    }

    public boolean isSettingHighTempFlag() {
        return settingHighTempFlag;
    }

    public void setSettingHighTempFlag(boolean settingHighTempFlag) {
        this.settingHighTempFlag = settingHighTempFlag;
    }

    public boolean isSettingRecoverUIForErrorFlag() {
        return settingRecoverUIForErrorFlag;
    }

    public void setSettingRecoverUIForErrorFlag(boolean settingRecoverUIForErrorFlag) {
        this.settingRecoverUIForErrorFlag = settingRecoverUIForErrorFlag;
    }

    public boolean isSettingPowerOnFlag() {
        return settingPowerOnFlag;
    }

    public void setSettingPowerOnFlag(boolean settingPowerOnFlag) {
        this.settingPowerOnFlag = settingPowerOnFlag;
    }

    public boolean isSettingHavePanFlag() {
        return settingHavePanFlag;
    }

    public void setSettingHavePanFlag(boolean settingHavePanFlag) {
        this.settingHavePanFlag = settingHavePanFlag;
    }

    public int getRemainFiveMinuteHourValue() {
        return remainFiveMinuteHourValue;
    }

    public void setRemainFiveMinuteHourValue(int remainFiveMinuteHourValue) {
        this.remainFiveMinuteHourValue = remainFiveMinuteHourValue;
    }

    public int getSettingTempShowOrder() {
        return settingTempShowOrder;
    }

    public void setSettingTempShowOrder(int settingTempShowOrder) {
        this.settingTempShowOrder = settingTempShowOrder;
    }

}
