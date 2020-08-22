package com.ekek.tfthobmodule;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.constants.CookerConstant;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SecurityDBUtil;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.tfthobmodule.constants.TFTHobConfiguration;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.constants.TFTHobDatabaseConstant;
import com.ekek.tfthobmodule.database.DatabaseHelper;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.entity.DaoMaster;
import com.ekek.tfthobmodule.entity.DaoSession;
import com.ekek.tfthobmodule.entity.FishTable;
import com.ekek.tfthobmodule.entity.FruitTable;
import com.ekek.tfthobmodule.entity.LegumesAndCerealsTable;
import com.ekek.tfthobmodule.entity.MeatTable;
import com.ekek.tfthobmodule.entity.ShellfishTable;
import com.ekek.tfthobmodule.entity.VegetablesTable;
import com.ekek.tfthobmodule.utils.JsonUtil;
import com.ekek.tfthobmodule.utils.SoundUtil;
import com.ekek.viewmodule.product.ProductManager;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.reactivex.Observer;
import solid.ren.skinlibrary.loader.SkinManager;

public class CataTFTHobApplication extends MultiDexApplication {

    private static CataTFTHobApplication instance;
    private static MediaPlayer mediaPlayer;
    private static  DaoMaster.DevOpenHelper helper;
    private PowerManager.WakeLock wakeLock;
    private static DaoSession daoSession;
    private static Stack<Activity> activityStack;
    private long latestTouchTime = SystemClock.elapsedRealtime();
    private long activationTime;
    private Typeface helveticaFontRegular;
    private Typeface helveticaFontBold;
    private Typeface robotoFontRegular;
    private Typeface robotoFontBold;

    private List<MeatTable> meatTables = new ArrayList<>();
    private String meatTablesDesciption;
    private List<LegumesAndCerealsTable> legumesAndCerealsTables = new ArrayList<>();
    private String legumesAndCerealsTablesDesciption;
    private List<FruitTable> fruitTables = new ArrayList<>();
    private String fruitTablesDesciption;
    private List<ShellfishTable> shellfishTables = new ArrayList<>();
    private String shellfishTablesDesciption;
    private List<VegetablesTable> vegetablesTables = new ArrayList<>();
    private String vegetablesTablesDesciption;
    private List<FishTable> fishTables = new ArrayList<>();
    private String fishTablesDesciption;
    private boolean showingErrER03 = false;
    private MyUncaughtExceptionHandler myUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLog();
        initDatabase();
        initFonts();
       // initSound();
        //requestWakeLock();
        SkinManager.getInstance().init(this);
    }

    public static CataTFTHobApplication getInstance() {
        return instance;
    }

    private void init() {
        Logger.initializeLoggingSystem(getFilesDir().getParentFile().getAbsolutePath());
        myUncaughtExceptionHandler = new MyUncaughtExceptionHandler(
                this.getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(myUncaughtExceptionHandler);
        ProductManager.PRODUCT_TYPE = TFTHobConfiguration.TFT_HOB_TYPE;
        ProductManager.IS_TI = TFTHobConfiguration.IS_TI;
        switch (TFTHobConfiguration.TFT_HOB_TYPE) {
            case TFTHobConfiguration.TFT_HOB_TYPE_60:
                CookerConstant.COOKER_TYPE_CURRENT_TYPE = CookerConstant.COOKER_TYPE_CATA_60;
                break;
            case TFTHobConfiguration.TFT_HOB_TYPE_80:
                CookerConstant.COOKER_TYPE_CURRENT_TYPE = CookerConstant.COOKER_TYPE_CATA_80;
                break;
            case TFTHobConfiguration.TFT_HOB_TYPE_90:
                CookerConstant.COOKER_TYPE_CURRENT_TYPE = CookerConstant.COOKER_TYPE_CATA_90;
                break;
        }

        GlobalVars.getInstance().initTempIndicatorStringList(TFTHobConstant.TEMP_INDENTIFY_STRING_LIST);
    }
    private void initFonts() {
        helveticaFontRegular = Typeface.createFromAsset(getResources().getAssets(), "font/HelveticaNeueLTStd-Cn.otf");
        GlobalVars.getInstance().setHelveticaFontRegular(helveticaFontRegular);
        helveticaFontBold = Typeface.createFromAsset(getResources().getAssets(), "font/HelveticaNeueLTStd-BdCn.otf");
        GlobalVars.getInstance().setHelveticaFontBold(helveticaFontBold);
        robotoFontRegular = Typeface.createFromAsset(getResources().getAssets(), "font/RobotoCondensed-Regular.ttf");
        GlobalVars.getInstance().setRobotoFontRegular(robotoFontRegular);
        robotoFontBold = Typeface.createFromAsset(getResources().getAssets(), "font/RobotoCondensed-Bold.ttf");
        GlobalVars.getInstance().setRobotoFontBold(robotoFontBold);
    }
    public boolean isCookingTablesEmpty() {
        return meatTables.isEmpty() ||
                legumesAndCerealsTables.isEmpty() ||
                fruitTables.isEmpty() ||
                shellfishTables.isEmpty() ||
                vegetablesTables.isEmpty() ||
                fishTables.isEmpty();
    }
    public void refreshCookingTables(Observer<Boolean> observer) {
        meatTables.clear();
        legumesAndCerealsTables.clear();
        fruitTables.clear();
        shellfishTables.clear();
        vegetablesTables.clear();
        fishTables.clear();

        JsonUtil.parseCookingTableJson(getApplicationContext()).subscribe(observer);
    }

    private void initLog() {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(getApplicationContext());
        userStrategy.setCrashHandleCallback(new AppCrashHandleCallback());
        CrashReport.initCrashReport(getApplicationContext(), "4dc580bc75", true,userStrategy);
       // LogcatHelper.getInstance(this).start();
    }

    private class AppCrashHandleCallback extends CrashReport.CrashHandleCallback {
        @Override
        public synchronized Map<String, String> onCrashHandleStart(int crashType, String errorType, String s1, String errorMessage) {
           // FileUtils.saveCrashLogFile(getApplicationContext() , errorType + "\n" + errorMessage);
            Log.d("samhung","Enter:: AppCrashHandleCallback---->" + errorType + "\n" + errorMessage);
            return super.onCrashHandleStart(crashType, errorType, s1, s1);
        }


    }




    private void requestWakeLock() {
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , "CataTFTHobApplication:GTermWakelock");
        wakeLock.acquire();
    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    private void initSound() {
        SoundUtil.init(this);
    }

    private void initDatabase() {
        helper = new DaoMaster.DevOpenHelper(this, TFTHobDatabaseConstant.DATABASE_ENCRYPTED ? TFTHobDatabaseConstant.DATABASE_NAME_ENCRYPTED : TFTHobDatabaseConstant.DATABASE_NAME);
        Database db = TFTHobDatabaseConstant.DATABASE_ENCRYPTED  ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        SettingDbHelper.saveTemperatureSensorStatus(0);
        DatabaseHelper.resetHobAndHoodData();
        DatabaseHelper.resetAllCookerData();
        //SecurityTableDao dao = SettingDatabaseHelper.getInstance(getApplicationContext()).getDaoSession().getSecurityTableDao();
        //dao.deleteAll();
        SecurityDBUtil.initSecurityDatabase(getApplicationContext());
        SettingPreferencesUtil.getHibernationModeSwitchStatus(getApplicationContext());
        SettingPreferencesUtil.getTotalTurnOffSwitchStatus(getApplicationContext());
        SettingPreferencesUtil.saveEnterPowerOffMode(getApplicationContext(), CataSettingConstant.EnterNone);
        SettingPreferencesUtil.saveDemoSwitchStatus(getApplicationContext(), CataSettingConstant.DEMO_SWITCH_STATUS_CLOSE);
        GlobalVars.getInstance().setInDebugMode(SettingPreferencesUtil.getDebugMode(getApplicationContext()));
        GlobalVars.getInstance().setDebugModeExtra(SettingPreferencesUtil.getDebugModeExtra(getApplicationContext()));

        boolean soundEnabled = CataSettingConstant.SOUND_SWITCH_STATUS_OPEN.equals(SettingPreferencesUtil.getSoundSwitchStatus(getApplicationContext()));
        if (soundEnabled) {
            int volume = Integer.valueOf(SettingPreferencesUtil.getDefaultSound(getApplicationContext()));
            if(volume <= 0){
                volume = 1;
            }
            com.ekek.settingmodule.utils.SoundUtil.setSystemVolume(getApplicationContext(), volume);

            boolean clickSoundEnabled = CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_OPEN.equals(SettingPreferencesUtil.getClickSoundSwitchStatus(getApplicationContext()));
            com.ekek.settingmodule.utils.SoundUtil.setSoundEffect(getApplicationContext(), clickSoundEnabled);
        } else {
            com.ekek.settingmodule.utils.SoundUtil.setSystemVolume(getApplicationContext(), 1);
            com.ekek.settingmodule.utils.SoundUtil.setSoundEffect(getApplicationContext(), false);
        }
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public synchronized void updateLatestTouchTime() {
        latestTouchTime = SystemClock.elapsedRealtime();
    }

    public long getLatestTouchTime(){
        return  latestTouchTime;
    }

    public boolean checkNoTouch(long duration) {//no touch : true , have touch : false
        long elapsed = SystemClock.elapsedRealtime() - latestTouchTime;
//        LogUtil.d("checkNoTouch(" + DateTimeUtil.millisecondToString(duration) + ") elapsed=" + DateTimeUtil.millisecondToString(elapsed));
        return elapsed >= duration;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity){
        if(activityStack ==null){
            activityStack =new Stack<Activity>();
        }
        activityStack.add(activity);

    }
    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }
    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            releaseWakeLock();
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } catch (Exception e) {
        }
    }

    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler defaultHandler;
        private Context context;

        public MyUncaughtExceptionHandler(Context context) {
            this.context = context;
            this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        }
        @Override
        public void uncaughtException(Thread thread, Throwable e) {
            Logger.getInstance().e(e);
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, e);
            }
        }
    }

    public long getActivationTime() {
        return activationTime;
    }
    public void setActivationTime(long activationTime) {
        this.activationTime = activationTime;
    }
    public Typeface getHelveticaFontRegular() {
        return helveticaFontRegular;
    }
    public Typeface getHelveticaFontBold() {
        return helveticaFontBold;
    }
    public Typeface getRobotoFontRegular() {
        return robotoFontRegular;
    }
    public Typeface getRobotoFontBold() {
        return robotoFontBold;
    }
    public List<MeatTable> getMeatTables() {
        return meatTables;
    }
    public List<LegumesAndCerealsTable> getLegumesAndCerealsTables() {
        return legumesAndCerealsTables;
    }
    public List<FruitTable> getFruitTables() {
        return fruitTables;
    }
    public List<ShellfishTable> getShellfishTables() {
        return shellfishTables;
    }
    public List<VegetablesTable> getVegetablesTables() {
        return vegetablesTables;
    }
    public List<FishTable> getFishTables() {
        return fishTables;
    }
    public boolean isShowingErrER03() {
        return showingErrER03;
    }
    public void setShowingErrER03(boolean showingErrER03) {
        this.showingErrER03 = showingErrER03;
    }

    public String getMeatTablesDesciption() {
        return meatTablesDesciption;
    }

    public void setMeatTablesDesciption(String meatTablesDesciption) {
        this.meatTablesDesciption = meatTablesDesciption;
    }

    public String getLegumesAndCerealsTablesDesciption() {
        return legumesAndCerealsTablesDesciption;
    }

    public void setLegumesAndCerealsTablesDesciption(String legumesAndCerealsTablesDesciption) {
        this.legumesAndCerealsTablesDesciption = legumesAndCerealsTablesDesciption;
    }

    public String getFruitTablesDesciption() {
        return fruitTablesDesciption;
    }

    public void setFruitTablesDesciption(String fruitTablesDesciption) {
        this.fruitTablesDesciption = fruitTablesDesciption;
    }

    public String getShellfishTablesDesciption() {
        return shellfishTablesDesciption;
    }

    public void setShellfishTablesDesciption(String shellfishTablesDesciption) {
        this.shellfishTablesDesciption = shellfishTablesDesciption;
    }

    public String getVegetablesTablesDesciption() {
        return vegetablesTablesDesciption;
    }

    public void setVegetablesTablesDesciption(String vegetablesTablesDesciption) {
        this.vegetablesTablesDesciption = vegetablesTablesDesciption;
    }

    public String getFishTablesDesciption() {
        return fishTablesDesciption;
    }

    public void setFishTablesDesciption(String fishTablesDesciption) {
        this.fishTablesDesciption = fishTablesDesciption;
    }
}
