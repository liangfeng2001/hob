package com.ekek.commonmodule;

import android.graphics.Typeface;
import android.os.SystemClock;

import com.ekek.commonmodule.utils.Logger;

import java.util.Locale;

public class GlobalVars {

    // Singleton
    private static GlobalVars instance;
    private GlobalVars() {

    }
    public static GlobalVars getInstance() {
        if (instance == null) {
            instance = new GlobalVars();
        }
        return instance;
    }

    // Fields
    private boolean initializeProcessComplete = false;
    private int appStartTag = GlobalCons.APP_START_NONE;
    private boolean hibernationModeEnabled;
    private boolean totalTurnOffModeEnabled;
    private boolean inDemoMode;
    private long latestTouchTime = SystemClock.elapsedRealtime();
    private boolean allCookersIsClose=true;
    private boolean inDebugMode;
    private int debugModeExtra;
    private boolean isFirstTimeForHoodPanelFragment=true;
    private Locale currentLocale;
    private Typeface helveticaFontRegular;
    private Typeface helveticaFontBold;
    private Typeface robotoFontRegular;
    private Typeface robotoFontBold;
    private Typeface defaultFontBold;
    private Typeface defaultFontRegular;
    private boolean playSplashVideo;
    private int powerSwitchState;
    private boolean isPause=false;
    private boolean isClickPowerOffButton=false;
    private boolean showingPauseHobDialog;
    private boolean okToSendE2;
    private boolean waitingToUpdate;
    private boolean configuringCooker;
    private int[][] tempIndicatorStrings;
    private int hobType;
    private boolean setTurboToNextPower=false;
    private boolean isHandlePowerOffAllCooker=true;
    private boolean powerOffEventHandled =true;
    private boolean abUnited;
    private boolean efUnited;
    private int hoodLevel=0;

    public int getHoodLevel() {
        return hoodLevel;
    }

    public void setHoodLevel(int hoodLevel) {
        this.hoodLevel = hoodLevel;
    }

    public boolean isHandlePowerOffAllCooker() {
        return isHandlePowerOffAllCooker;
    }

    public void setHandlePowerOffAllCooker(boolean handlePowerOffAllCooker) {
        isHandlePowerOffAllCooker = handlePowerOffAllCooker;
    }

    public boolean isSetTurboToNextPower() {
        return setTurboToNextPower;
    }

    public void setSetTurboToNextPower(boolean setTurboToNextPower) {
        this.setTurboToNextPower = setTurboToNextPower;
    }

    public int getHobType() {
        return hobType;
    }

    public void setHobType(int hobType) {
        this.hobType = hobType;
    }

    public boolean isClickPowerOffButton() {
        return isClickPowerOffButton;
    }

    public void setClickPowerOffButton(boolean clickPowerOffButton) {
        isClickPowerOffButton = clickPowerOffButton;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isFirstTimeForHoodPanelFragment() {
        return isFirstTimeForHoodPanelFragment;
    }

    public void setFirstTimeForHoodPanelFragment(boolean firstTimeForHoodPanelFragment) {
        isFirstTimeForHoodPanelFragment = firstTimeForHoodPanelFragment;
    }

    public boolean isAllCookersIsClose() {
        return allCookersIsClose;
    }

    public void setAllCookersIsClose(boolean allCookersIsClose) {
        this.allCookersIsClose = allCookersIsClose;
    }

    // Public functions
    public void updateLatestTouchTime() {
        latestTouchTime = SystemClock.elapsedRealtime();
    }
    public boolean checkNoTouch(long duration) {
        long elapsed = SystemClock.elapsedRealtime() - latestTouchTime;
        return elapsed >= duration;
    }
    public void initTempIndicatorStringList(int[][] lst) {
        tempIndicatorStrings = lst;
    }
    public int getTempIndicatorString(int temperature) {
        for (int i = 0; i < tempIndicatorStrings.length ;i++) {
            if (tempIndicatorStrings[i][0] == temperature) {
                return tempIndicatorStrings[i][1];
            }
        }

        return -1;
    }

    // Properties
    public boolean isInitializeProcessComplete() {
        return initializeProcessComplete;
    }
    public void setInitializeProcessComplete(boolean initializeProcessComplete) {
        this.initializeProcessComplete = initializeProcessComplete;
    }
    public int getAppStartTag() {
        return appStartTag;
    }
    public void setAppStartTag(int appStartTag) {
        this.appStartTag = appStartTag;
    }
    public boolean isHibernationModeEnabled() {
        return hibernationModeEnabled;
    }
    public void setHibernationModeEnabled(boolean hibernationModeEnabled) {
        this.hibernationModeEnabled = hibernationModeEnabled;
    }

    /**
     * Discarded
     * @return
     */
    public boolean isTotalTurnOffModeEnabled() {
        return totalTurnOffModeEnabled;
    }

    /**
     * Discarded
     * @param totalTurnOffModeEnabled
     */
    public void setTotalTurnOffModeEnabled(boolean totalTurnOffModeEnabled) {
        this.totalTurnOffModeEnabled = totalTurnOffModeEnabled;
    }
    public boolean isInDemoMode() {
        return inDemoMode;
    }
    public void setInDemoMode(boolean inDemoMode) {
        this.inDemoMode = inDemoMode;
    }
    public long getLatestTouchTime() {
        return latestTouchTime;
    }
    public void setLatestTouchTime(long latestTouchTime) {
        this.latestTouchTime = latestTouchTime;
    }
    public boolean isInDebugMode() {
        return inDebugMode;
    }
    public void setInDebugMode(boolean inDebugMode) {
        this.inDebugMode = inDebugMode;
    }
    public int getDebugModeExtra() {
        return debugModeExtra;
    }
    public void setDebugModeExtra(int debugModeExtra) {
        this.debugModeExtra = debugModeExtra;
    }
    public Locale getCurrentLocale() {
        return currentLocale;
    }
    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }
    public Typeface getHelveticaFontRegular() {
        return helveticaFontRegular;
    }
    public void setHelveticaFontRegular(Typeface helveticaFontRegular) {
        this.helveticaFontRegular = helveticaFontRegular;
    }
    public Typeface getHelveticaFontBold() {
        return helveticaFontBold;
    }
    public void setHelveticaFontBold(Typeface helveticaFontBold) {
        this.helveticaFontBold = helveticaFontBold;
    }
    public Typeface getRobotoFontRegular() {
        return robotoFontRegular;
    }
    public void setRobotoFontRegular(Typeface robotoFontRegular) {
        this.robotoFontRegular = robotoFontRegular;
    }
    public Typeface getRobotoFontBold() {
        return robotoFontBold;
    }
    public void setRobotoFontBold(Typeface robotoFontBold) {
        this.robotoFontBold = robotoFontBold;
    }
    public Typeface getDefaultFontBold() {
        return defaultFontBold;
    }
    public void setDefaultFontBold(Typeface defaultFontBold) {
        this.defaultFontBold = defaultFontBold;
    }
    public Typeface getDefaultFontRegular() {
        return defaultFontRegular;
    }
    public void setDefaultFontRegular(Typeface defaultFontRegular) {
        this.defaultFontRegular = defaultFontRegular;
    }
    public boolean isPlaySplashVideo() {
        return playSplashVideo;
    }
    public void setPlaySplashVideo(boolean playSplashVideo) {
        this.playSplashVideo = playSplashVideo;
    }
    public int getPowerSwitchState() {
        return powerSwitchState;
    }
    public synchronized void setPowerSwitchState(int powerSwitchState) {
        this.powerSwitchState = powerSwitchState;
    }

    public boolean isShowingPauseHobDialog() {
        return showingPauseHobDialog;
    }

    public void setShowingPauseHobDialog(boolean showingPauseHobDialog) {
        this.showingPauseHobDialog = showingPauseHobDialog;
    }

    public boolean isOkToSendE2() {
        return okToSendE2;
    }

    public void setOkToSendE2(boolean okToSendE2) {
        this.okToSendE2 = okToSendE2;
    }

    public boolean isWaitingToUpdate() {
        return waitingToUpdate;
    }

    public void setWaitingToUpdate(boolean waitingToUpdate) {
        this.waitingToUpdate = waitingToUpdate;
    }

    public boolean isConfiguringCooker() {
        return configuringCooker;
    }

    public void setConfiguringCooker(boolean configuringCooker) {
        this.configuringCooker = configuringCooker;
    }

    public boolean isPowerOffEventHandled() {
        return powerOffEventHandled;
    }

    public void setPowerOffEventHandled(boolean powerOffEventHandled) {
        this.powerOffEventHandled = powerOffEventHandled;
        Logger.getInstance().i("setPowerOffEventHandled(" + powerOffEventHandled + ")");
    }

    public boolean isAbUnited() {
        return abUnited;
    }

    public void setAbUnited(boolean abUnited) {
        this.abUnited = abUnited;
    }

    public boolean isEfUnited() {
        return efUnited;
    }

    public void setEfUnited(boolean efUnited) {
        this.efUnited = efUnited;
    }
}
