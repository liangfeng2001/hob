package com.ekek.tfthobmodule.database;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.constants.HardwareConstant;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.entity.CookerDataTable;
import com.ekek.tfthobmodule.entity.HobData;
import com.ekek.tfthobmodule.entity.HobDataDao;
import com.ekek.tfthobmodule.entity.HoodData;
import com.ekek.tfthobmodule.entity.HoodDataDao;

import java.util.List;

public class DatabaseHelper {

    public static void saveHobData(int cookerID,int cookerMode,int cookerGear,int cookerTemp) {
        HobDataDao dao = CataTFTHobApplication.getDaoSession().getHobDataDao();
        List<HobData> datas =  dao.queryBuilder().where(HobDataDao.Properties.CookerID.eq(cookerID)).limit(1).list();
        if (datas.size() == 0) {
            HobData data = new HobData(null,cookerID,cookerMode,cookerGear,cookerTemp,0);
            dao.insertOrReplace(data);
        }else {
            HobData data = datas.get(0);
            data.setCookerMode(cookerMode);
            data.setCookerGear(cookerGear);
            data.setCookerTemp(cookerTemp);
            dao.update(data);
        }
    }

    public static List<HobData> getHobData() {
        HobDataDao dao = CataTFTHobApplication.getDaoSession().getHobDataDao();
        List<HobData> datas =  dao.loadAll();
        return datas;
    }

    public static List<CookerDataTable> getCookerData() {
        return CataTFTHobApplication.getDaoSession().getCookerDataTableDao().loadAll();
    }

    public static void resetAllHobData() {
        HobDataDao dao = CataTFTHobApplication.getDaoSession().getHobDataDao();
        List<HobData> datas =  dao.loadAll();
        if (datas.size() == 0) {
            for (int i = 0; i < HardwareConstant.COOKER_QUANTITY; i++) {
                HobData data = new HobData(null,i, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING,0,0,0);
                dao.insert(data);
            }
//InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING
        }else if(datas.size() == HardwareConstant.COOKER_QUANTITY) {
            for (int i = 0; i < datas.size() ;i++) {
                HobData data = datas.get(i);
                data.setCookerMode(InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING);
                data.setCookerGear(0);
                data.setCookerTemp(0);
                dao.update(data);
            }
        }
    }

    public static void saveHoodData(int fanGear,int lightGear) {
        HoodDataDao dao = CataTFTHobApplication.getDaoSession().getHoodDataDao();
        List<HoodData> datas = dao.loadAll();
        if (datas.size() == 0) {
            HoodData data = new HoodData(null,fanGear,lightGear,0);
            dao.insertOrReplace(data);
        }else {
            HoodData data = datas.get(0);
            data.setFanGear(fanGear);
            data.setLightGear(lightGear);
            dao.update(data);
        }
      //  LogUtil.d("liang the fangear is "+fanGear+" ;the light gear is "+lightGear);
    }

    public static HoodData getHoodData() {
        HoodDataDao dao = CataTFTHobApplication.getDaoSession().getHoodDataDao();
        List<HoodData> datas =  dao.loadAll();
        if (datas.size() == 1) return datas.get(0);
        else return null;
    }

    public static void resetAllHoodData() {
        HoodDataDao dao = CataTFTHobApplication.getDaoSession().getHoodDataDao();
        List<HoodData> datas =  dao.loadAll();
        if (datas.size() == 0) {
            HoodData data = new HoodData(null,0,0,0);
            dao.insertOrReplace(data);

        }else if(datas.size() == 1) {
            HoodData data = datas.get(0);
            data.setFanGear(0);
            data.setLightGear(0);
            dao.update(data);
        }
    }

    public static void resetHobAndHoodData() {
        resetAllHoodData();
        resetAllHobData();
    }

    public static void powerOffHobAndHoodData() {
        resetAllHobData();
       // resetAllHoodData();
    }

    public static void resetAllCookerData() {
        CataTFTHobApplication.getDaoSession().getCookerDataTableDao().deleteAll();
    }

    public static void resetHoodData(){
        resetAllHoodData();
    }

}
