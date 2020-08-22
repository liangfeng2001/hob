package com.ekek.hardwaremodule.pid;

import com.ekek.commonmodule.utils.LogUtil;

public class TempControlManager {
    private PIDData mPIDData;
    private long lastCalTime ,currentTime,lastKTime;
    private int lastKValue = 0;
    private int kOut = 0;
    private float lastOut = 0;
    private boolean isOil = false;
    public void init(int settingValue,int currentValue) {
        mPIDData = new PIDData(settingValue);
        lastCalTime = System.currentTimeMillis();
        lastKTime = lastCalTime;
        lastKValue = currentValue;
        kOut = 0;
    }

 /*   public float calPID(int currentValue) {

        float out = 0;
        float ki,kd;
        float iOut,dOut,pOut,kOut;
        currentTime = System.currentTimeMillis();
        if (currentTime - lastCalTime < 1000) return -10000;
        lastCalTime = currentTime;
        lastKTime = lastCalTime;


        mPIDData.setPv(currentValue);

        pOut = mPIDData.getKp() * mPIDData.getError();


        ki = (mPIDData.getT() / mPIDData.getTi()) * mPIDData.getKp();
        iOut = ki * mPIDData.getSumError();


        kd = (mPIDData.getTd() / mPIDData.getT()) * mPIDData.getKp();
        dOut = kd * mPIDData.getDelError();

        if (currentTime - lastKTime >1000 * 30) {
            lastKTime = currentTime;

        }

        //out = pOut + iOut + dOut;
        out = pOut  + dOut;
        LogUtil.e("PIDTEST--EK---->" + mPIDData.getError() + "---EK-1---->" + mPIDData.getLastError() + "----SUMEk---->" + mPIDData.getSumError() + "-----DelEk---->" + mPIDData.getDelError());
        LogUtil.i("PIDTEST--POut---->" + pOut + "----iOut--->" + iOut + "----dOut---->" + dOut + "----PIDOut---->" + out);
        LogUtil.e("samhung-----DelEk---->" + mPIDData.getDelError());

        // LogUtil.d("PIDOut---->" + out);


        return out;

    }*/

    public float calPID(int settingValue,int currentValue ,int mode) {

        currentTime = System.currentTimeMillis();
        //LogUtil.d("Enter:: calPID-------------1------currentTime--->" + currentTime + "----lastTime---->" + lastCalTime);
        if (currentTime - lastCalTime < 1000) return -10000;
        //LogUtil.d("Enter:: calPID-------------2------");
        lastCalTime = currentTime;
        if (lastKValue == -1) lastKValue = currentValue;

        float out = 0;
        float ki,kd;
        float iOut,dOut,pOut;
        mPIDData.setSvPv(settingValue,currentValue);

        pOut = mPIDData.getKp() * mPIDData.getError();


        ki = (mPIDData.getT() / mPIDData.getTi()) * mPIDData.getKp();
        iOut = ki * mPIDData.getSumError();


        kd = (mPIDData.getTd() / mPIDData.getT()) * mPIDData.getKp();
        dOut = kd * mPIDData.getDelError();

        if (mPIDData.getError() < 20) {

            if (mPIDData.getError() <= 1) {
                out = 0;
            }else {
                if (currentTime - lastKTime > 1000 * 10) {//1000 * 15
                    lastKTime = currentTime;
                    LogUtil.d("PIDTEST---KValue---->" + (currentValue - lastKValue));
                    int deltaValue = currentValue - lastKValue;

                    if (deltaValue >= 5) {
                        LogUtil.d("PIDTEST---KValue---out---1---->" + deltaValue);
                        out = 0;
                    }else if (deltaValue < 5 && deltaValue >=3) {
                        LogUtil.d("PIDTEST---KValue---out---2---->" + deltaValue);
                        kOut = 100 * (currentValue - lastKValue);
                        out = pOut  + dOut - kOut;
                    }else {

                        kOut = 50 * (currentValue - lastKValue);
                        out = pOut  + dOut - kOut;
                        LogUtil.d("PIDTEST---KValue---out---3---->" + out);
                    }


                    lastKValue = currentValue;


                }else {
                    out = lastOut;
                }
            }



        }else {

            out = pOut  + dOut;

        }
      /*
        if (currentTime - lastKTime > 1000 * 15) {//1000 * 15
            lastKTime = currentTime;
            LogUtil.d("PIDTEST---KValue---->" + (currentValue - lastKValue));
            if (currentValue - lastKValue >= 3) kOut = 60 * (currentValue - lastKValue);// 3    50
            else kOut = 0;

            lastKValue = currentValue;


        }else {
            out = 0;
        }*/



       // LogUtil.e("PIDTEST--EK---->" + mPIDData.getError() + "---EK-1---->" + mPIDData.getLastError() + "----SUMEk---->" + mPIDData.getSumError() + "-----DelEk---->" + mPIDData.getDelError());
        LogUtil.i("PIDTEST--POut---->" + pOut + "----iOut--->" + iOut + "----dOut---->" + dOut + "---kOut--->" + kOut + "----PIDOut---->" + out);
      //  LogUtil.e("samhung-----DelEk---->" + mPIDData.getDelError());

        // LogUtil.d("PIDOut---->" + out);

        lastOut = out;
        return out;

    }

    public float calPID(int settingValue,int currentValue) {
        float out = 0;
        currentTime = System.currentTimeMillis();
        if (currentTime - lastCalTime < 1000) return -10000;
        if (lastKValue == -1) lastKValue = currentValue;
        lastCalTime = currentTime;
        mPIDData.setSvPv(settingValue,currentValue);
        int deltaT = mPIDData.getError();
        if (deltaT <= 20 && deltaT >12) {//12--20
            if (currentTime - lastKTime > 1000 * 17) {
                lastKTime = currentTime;
                int deltaValue = currentValue - lastKValue;
                LogUtil.d("PIDTEST---KValue---out---1---->" + deltaValue);
                if (deltaValue > 5) {
                    isOil = true;
                    out = 0;

                }else if (deltaValue <= 5 && deltaValue > 3) {
                    isOil = true;
                    if (isOil) out = 0;
                    else out = 200;

                }else if (deltaValue <= 3 && deltaValue > 0) {
                    if (isOil) out = 0;
                    else out = 500;

                }else {

                    if (isOil) out = 0;
                    else out = 1000;
                }

                lastKValue = currentValue;
                lastOut = out;
            }else {
                out = lastOut;
            }

        }else if (deltaT <= 12 && deltaT > 7) {
            if (currentTime - lastKTime > 1000 * 15) {
                lastKTime = currentTime;
                int deltaValue = currentValue - lastKValue;

                LogUtil.d("PIDTEST---KValue---out---2---->" + deltaValue);
                if (deltaValue > 5) {
                    isOil = true;
                    out = 0;

                }else if (deltaValue <= 5 && deltaValue > 3) {
                    isOil = true;
                    out = 0;

                }else if (deltaValue <= 3 && deltaValue > 0) {
                    if (isOil) out = 0;
                    else out = 500;

                }else {
                    if (isOil) out = 100;
                    else out = 800;


                }

                lastKValue = currentValue;
                lastOut = out;
            }else {
                out = lastOut;
            }



        }else if (deltaT <= 7 && deltaT > 2) {
            if (currentTime - lastKTime > 1000 * 5) {
                lastKTime = currentTime;
                int deltaValue = currentValue - lastKValue;
                LogUtil.d("PIDTEST---KValue---out---3---->" + deltaValue);
                if (deltaValue > 5) {
                    isOil = true;
                    out = 0;

                }else if (deltaValue <= 5 && deltaValue > 3) {
                    isOil = true;
                    out = 50;

                }else if (deltaValue <= 3 && deltaValue > 0) {
                    //out = 50;
                    if (isOil) out = 0;
                    else out = 500;

                }else {
                    out = 100;
                    if (isOil) out = 0;
                    else out = 800;

                }

                lastKValue = currentValue;
                lastOut = out;
            }else {
                out = lastOut;
            }

        }else if (deltaT <= 2) {

            if (isOil) out =0;
            else {
                if (deltaT <= 0) out = 0;
                else out = 200;
            }

        }else if (deltaT > 20) {
            if (currentValue - lastKValue >= 2) isOil = true;
            lastKValue = currentValue;
            out = 1000;
        }

        LogUtil.d("PIDTEST---KValue---isOil---->" + isOil);
        return out;

    }


    public float calPIDForFastMode(int settingValue,int currentValue) {
        float out = 0;
        currentTime = System.currentTimeMillis();
        if (currentTime - lastCalTime < 1000) return -10000;
        if (lastKValue == -1) lastKValue = currentValue;
        lastCalTime = currentTime;
        mPIDData.setSvPv(settingValue,currentValue);
        int deltaT = mPIDData.getError();
        if (deltaT <= 20 && deltaT >12) {//12--20
            if (currentTime - lastKTime > 1000 * 17) {
                lastKTime = currentTime;
                int deltaValue = currentValue - lastKValue;
                LogUtil.d("PIDTEST---KValue---out---1---->" + deltaValue);
                if (deltaValue > 5) {
                    //isOil = true;
                    out = 0;

                }else if (deltaValue <= 5 && deltaValue > 3) {
                    //isOil = true;
                    if (isOil) out = 0;
                    else out = 200;

                }else if (deltaValue <= 3 && deltaValue > 0) {
                    if (isOil) out = 0;
                    else out = 500;

                }else {

                    if (isOil) out = 0;
                    else out = 1000;
                }

                lastKValue = currentValue;
                lastOut = out;
            }else {
                out = lastOut;
            }

        }else if (deltaT <= 12 && deltaT >7) {
            if (currentTime - lastKTime > 1000 * 15) {
                lastKTime = currentTime;
                int deltaValue = currentValue - lastKValue;

                LogUtil.d("PIDTEST---KValue---out---2---->" + deltaValue);
                if (deltaValue > 5) {
                    //isOil = true;
                    out = 0;

                }else if (deltaValue <= 5 && deltaValue > 3) {
                    //isOil = true;
                    out = 0;

                }else if (deltaValue <= 3 && deltaValue > 0) {
                    if (isOil) out = 0;
                    else out = 500;

                }else {
                    if (isOil) out = 100;
                    else out = 800;


                }

                lastKValue = currentValue;
                lastOut = out;
            }else {
                out = lastOut;
            }



        }else if (deltaT <= 7 && deltaT > 2) {
            if (currentTime - lastKTime > 1000 * 5) {
                lastKTime = currentTime;
                int deltaValue = currentValue - lastKValue;
                LogUtil.d("PIDTEST---KValue---out---3---->" + deltaValue);
                if (deltaValue > 5) {
                   // isOil = true;
                    out = 0;

                }else if (deltaValue <= 5 && deltaValue > 3) {
                   // isOil = true;
                    out = 50;

                }else if (deltaValue <= 3 && deltaValue > 0) {
                    //out = 50;
                    if (isOil) out = 0;
                    else out = 500;

                }else {
                    out = 100;
                    if (isOil) out = 0;
                    else out = 800;

                }

                lastKValue = currentValue;
                lastOut = out;
            }else {
                out = lastOut;
            }

        }else if (deltaT <= 2) {

            if (isOil) out =0;
            else {
                if (deltaT <= 0) out = 0;
                else out = 200;
            }

        }else if (deltaT > 20) {
            //if (currentValue - lastKValue >= 2) isOil = true;
            lastKValue = currentValue;
            out = 1000;
        }

        LogUtil.d("PIDTEST---KValue---isOil---->" + isOil);
        return out;

    }

}
