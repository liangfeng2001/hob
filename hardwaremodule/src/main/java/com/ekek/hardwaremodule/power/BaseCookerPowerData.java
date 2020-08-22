package com.ekek.hardwaremodule.power;


import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.constants.CookerConstant;
import com.ekek.hardwaremodule.entity.AllCookerData;
import com.ekek.hardwaremodule.entity.CookerData;

public abstract class BaseCookerPowerData {
    private static int currentTotalPower;
    private static int currentTotalSingleLeftPower;
    private static int currentTotalSingleRightPower;
    private static int aCookerPower,bCookerPower,cCookerPower,dCookerPower,eCookerPower,fCookerPower;

    public int getCurrentTotalCookerPower() {
        return currentTotalPower;
    }

    public int getCurrentTotalSingleLeftCookerPower() {
        return currentTotalSingleLeftPower;
    }

    public int getCurrentTotalSingleRightCookerPower() {
        return currentTotalSingleRightPower;
    }

    protected int updateCookerPower(AllCookerData data) {
        if (checkTotalPowerAvailable(data)) {
            if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                aCookerPower = getCookerUnionLeftPowerTable()[data.getaFireValue()] / 2;
                bCookerPower = getCookerUnionLeftPowerTable()[data.getbFireValue()] / 2;

            }else {
                bCookerPower = getCookerBPowerTable()[data.getbFireValue()];
                aCookerPower = getCookerAPowerTable()[data.getaFireValue()];
            }

            if (data.getcMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                cCookerPower = getCookerUnionRightPowerTable()[data.getcFireValue()] / 2;
                dCookerPower = getCookerUnionRightPowerTable()[data.getdFireValue()] / 2;
            }else {
                cCookerPower = getCookerCPowerTable()[data.getcFireValue()];
                dCookerPower = getCookerDPowerTable()[data.getdFireValue()];
            }

            if (data.geteMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                eCookerPower = getCookerUnionRightPowerTable()[data.geteFireValue()] / 2;
                fCookerPower = getCookerUnionRightPowerTable()[data.getfFireValue()] / 2;
            }else {
                eCookerPower = getCookerEPowerTable()[data.geteFireValue()];
                fCookerPower = getCookerFPowerTable()[data.getfFireValue()];
            }

            currentTotalPower = aCookerPower + bCookerPower + cCookerPower + dCookerPower + eCookerPower + fCookerPower;
            currentTotalSingleLeftPower = aCookerPower + bCookerPower;
            if (checkCDCookerLocation ()) {//CD炉在中间
                currentTotalSingleRightPower = eCookerPower + fCookerPower;
            }else {//CD炉在旁边
                currentTotalSingleRightPower = cCookerPower + dCookerPower;
            }

            return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_SUCCESS;
        }else {

            return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_FAIL;
        }


    }

    private boolean checkCDCookerLocation () {
        if (getCookerType() == CookerConstant.COOKER_TYPE_CATA_60) {
            return false;
        }else {
            if(getCookerDPowerTable()[1] == 0){
                return true;
            }else {
                return false;
            }
        }
    }

    private long start;
    protected int checkCookerPower(int value, CookerData... datas) {
        start = System.currentTimeMillis();
        int testCurrentTotalPower = currentTotalPower;
        int testCurrentTotalSingleLeftPower = currentTotalSingleLeftPower;
        int testCurrentTotalSinglRightPower = currentTotalSingleRightPower;
        boolean hasLeftUnionMode = false;
        boolean hasRightUnionMode = false;
        while (value >= 0) {
            hasLeftUnionMode = false;
            hasRightUnionMode = false;
            testCurrentTotalPower = currentTotalPower;
            testCurrentTotalSingleLeftPower = currentTotalSingleLeftPower;
            testCurrentTotalSinglRightPower = currentTotalSingleRightPower;
            for (CookerData data:datas) {

                switch (data.getId()) {
                    case CookerConstant.COOKER_ID_A_COOKDER:
                        if (data.getMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                            testCurrentTotalPower += getCookerUnionLeftPowerTable()[value] / 2 - aCookerPower ;
                            testCurrentTotalSingleLeftPower += getCookerUnionLeftPowerTable()[value] / 2 - aCookerPower;
                            hasLeftUnionMode = true;
                        }else {
                            testCurrentTotalPower += getCookerAPowerTable()[value] - aCookerPower;
                            testCurrentTotalSingleLeftPower += getCookerAPowerTable()[value] - aCookerPower;
                        }


                        break;
                    case CookerConstant.COOKER_ID_B_COOKDER:
                        if (data.getMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                            testCurrentTotalPower += getCookerUnionLeftPowerTable()[value] / 2 - bCookerPower;
                            testCurrentTotalSingleLeftPower += getCookerUnionLeftPowerTable()[value] / 2 - bCookerPower;
                            hasLeftUnionMode = true;
                        }else {
                            testCurrentTotalPower += getCookerBPowerTable()[value] - bCookerPower;
                            testCurrentTotalSingleLeftPower += getCookerBPowerTable()[value] - bCookerPower;
                        }
                        break;
                    case CookerConstant.COOKER_ID_C_COOKDER:
                        if (data.getMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                            testCurrentTotalPower += getCookerUnionRightPowerTable()[value] / 2 - cCookerPower;
                            testCurrentTotalSinglRightPower += getCookerUnionRightPowerTable()[value] / 2 - cCookerPower;
                            hasRightUnionMode = true;
                        }else {
                            testCurrentTotalPower += getCookerCPowerTable()[value] - cCookerPower;
                            if (checkCDCookerLocation()) {//CD在中间

                            }else {//CD在旁边
                                testCurrentTotalSinglRightPower += getCookerCPowerTable()[value] - cCookerPower;
                            }

                        }
                        break;
                    case CookerConstant.COOKER_ID_D_COOKDER:
                        if (data.getMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                            testCurrentTotalPower += getCookerUnionRightPowerTable()[value] / 2 - dCookerPower;
                            testCurrentTotalSinglRightPower += getCookerUnionRightPowerTable()[value] / 2 - dCookerPower;
                            hasRightUnionMode = true;
                        }else {
                            testCurrentTotalPower += getCookerDPowerTable()[value] - dCookerPower;


                            if (checkCDCookerLocation()) {//CD在中间

                            }else {//CD在旁边
                                testCurrentTotalSinglRightPower += getCookerDPowerTable()[value] - dCookerPower;
                            }
                        }
                        break;
                    case CookerConstant.COOKER_ID_E_COOKDER:
                        if (data.getMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                            testCurrentTotalPower += getCookerUnionRightPowerTable()[value] / 2 - eCookerPower;
                            testCurrentTotalSinglRightPower += getCookerUnionRightPowerTable()[value] / 2 - eCookerPower;
                            hasRightUnionMode = true;
                        }else {
                            testCurrentTotalPower += getCookerEPowerTable()[value] - eCookerPower;
                            if (checkCDCookerLocation()) {//CD在中间
                                testCurrentTotalSinglRightPower += getCookerEPowerTable()[value] - eCookerPower;
                            }else {//CD在旁边

                            }

                        }
                        break;

                    case CookerConstant.COOKER_ID_F_COOKDER:
                        if (data.getMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                            testCurrentTotalPower += getCookerUnionRightPowerTable()[value] / 2 - fCookerPower;
                            testCurrentTotalSinglRightPower += getCookerUnionRightPowerTable()[value] / 2 - fCookerPower;
                            hasRightUnionMode = true;
                        }else {
                            testCurrentTotalPower += getCookerFPowerTable()[value] - fCookerPower;

                            if (checkCDCookerLocation()) {//CD在中间
                                testCurrentTotalSinglRightPower += getCookerFPowerTable()[value] - fCookerPower;
                            }else {//CD在旁边

                            }
                        }
                        break;
                    case CookerConstant.COOKER_ID_AB_COOKDER:
                        testCurrentTotalPower += getCookerUnionLeftPowerTable()[value] - aCookerPower - bCookerPower;
                        testCurrentTotalSinglRightPower += getCookerUnionLeftPowerTable()[value] - aCookerPower - bCookerPower;
                        hasRightUnionMode = true;
                        break;
                    case CookerConstant.COOKER_ID_EF_COOKDER:
                        testCurrentTotalPower += getCookerUnionRightPowerTable()[value] - eCookerPower - fCookerPower;
                        testCurrentTotalSinglRightPower += getCookerUnionRightPowerTable()[value] - eCookerPower - fCookerPower;
                        hasRightUnionMode = true;
                        break;
                }


            }
          /*  LogUtil.d("Enter::checkCookerPower time--1-->" + (System.currentTimeMillis() - start));
            LogUtil.d("hasLeftUnionMode-->" + hasLeftUnionMode + "---hasRightUnionMode-->" + hasRightUnionMode);
            LogUtil.d("Enter::checkCookerPower time--2----1->" + (System.currentTimeMillis() - start));
            LogUtil.d("value---->" + value);
            LogUtil.d("Enter::checkCookerPower time--2----2-->" + (System.currentTimeMillis() - start));
            LogUtil.d("Test: signle left--->" + testCurrentTotalSingleLeftPower + "---singleRight--->" + testCurrentTotalSinglRightPower + "----Total--->" + testCurrentTotalPower);
            */
          //LogUtil.d("Enter::checkCookerPower time--2-->" + (System.currentTimeMillis() - start));
           // LogUtil.d("Test: signle left--->" + testCurrentTotalSingleLeftPower + "---singleRight--->" + testCurrentTotalSinglRightPower + "----Total--->" + testCurrentTotalPower);
            if (hasLeftUnionMode == false && hasRightUnionMode == false) {
                if (testCurrentTotalSingleLeftPower <= getTotalSingleLeftPower() && testCurrentTotalSinglRightPower <= getTotalSingleRightPower() && testCurrentTotalPower <= getTotalPower() ) {
                    //LogUtil.d("Enter::checkCookerPower time--3-->" + (System.currentTimeMillis() - start));
                    return value;
                }

            }else if (hasLeftUnionMode == true && hasRightUnionMode == false) {
                if (testCurrentTotalSinglRightPower <= getTotalSingleRightPower() && testCurrentTotalPower <= getTotalPower() ) {
                   //LogUtil.d("Enter::checkCookerPower time--4-->" + (System.currentTimeMillis() - start));
                    return value;
                }
            }else if (hasLeftUnionMode == false && hasRightUnionMode == true) {
                if (testCurrentTotalSingleLeftPower <= getTotalSingleLeftPower() && testCurrentTotalPower <= getTotalPower() ) {
                    //LogUtil.d("Enter::checkCookerPower time--5-->" + (System.currentTimeMillis() - start));
                    return value;
                }
            }else if (hasLeftUnionMode == true && hasRightUnionMode == true) {
                if (testCurrentTotalPower <= getTotalPower()) {
                    //LogUtil.d("Enter::checkCookerPower time--5-->" + (System.currentTimeMillis() - start));
                    return value;
                }
            }


            value--;


        }


        return 0;
    }

    protected boolean checkTotalPowerAvailable(AllCookerData data) {

        int apower = 0;
        int bpower = 0;
        int cpower = 0;
        int dpower = 0;
        int epower = 0;
        int fpower = 0;
        if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
            apower = getCookerUnionLeftPowerTable()[data.getaFireValue()] / 2;
            bpower = getCookerUnionLeftPowerTable()[data.getbFireValue()] / 2;

        }else {
            bpower = getCookerBPowerTable()[data.getbFireValue()];
            apower = getCookerAPowerTable()[data.getaFireValue()];
        }

        if (data.getcMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
            cpower = getCookerUnionRightPowerTable()[data.getcFireValue()] / 2;
            dpower = getCookerUnionRightPowerTable()[data.getdFireValue()] / 2;
        }else {
            cpower = getCookerCPowerTable()[data.getcFireValue()];
            dpower = getCookerDPowerTable()[data.getdFireValue()];
        }

        if (data.geteMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
            epower = getCookerUnionRightPowerTable()[data.geteFireValue()] / 2;
            fpower = getCookerUnionRightPowerTable()[data.getfFireValue()] / 2;
        }else {
            epower = getCookerEPowerTable()[data.geteFireValue()];
            fpower = getCookerFPowerTable()[data.getfFireValue()];
        }

        if (checkCDCookerLocation()) {//CD炉在中间
            if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION && data.geteMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                return true;
            }else if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION && data.geteMode() != CookerConstant.COOKER_WORK_MODE_UNION) {
                if ((epower + fpower) >  getTotalSingleRightPower()) return false;
                if ((epower + fpower) <=  getTotalSingleRightPower() && (cpower + dpower + apower + bpower + epower + fpower) <= getTotalPower()) return true;
                if ((epower + fpower) <=  getTotalSingleRightPower() && (cpower + dpower + apower + bpower + epower + fpower) > getTotalPower()) return false;

            }else if (data.getbMode() != CookerConstant.COOKER_WORK_MODE_UNION && data.geteMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                if ((apower + bpower) >  getTotalSingleLeftPower()) return false;
                if ((apower + bpower) <=  getTotalSingleLeftPower() && (cpower + dpower + apower + bpower + epower + fpower) <= getTotalPower()) return true;
                if ((apower + bpower) <=  getTotalSingleLeftPower() && (cpower + dpower + apower + bpower + epower + fpower) > getTotalPower()) return false;
            }else if  (data.getbMode() != CookerConstant.COOKER_WORK_MODE_UNION && data.geteMode() != CookerConstant.COOKER_WORK_MODE_UNION) {
                if ((apower + bpower) >  getTotalSingleLeftPower() || (epower + fpower) >  getTotalSingleRightPower() || (cpower + dpower + apower + bpower + epower + fpower) > getTotalPower()) return false;
                else return true;
            }else if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION && data.geteMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                if ((cpower + dpower + apower + bpower + epower + fpower) > getTotalPower()) return false;
                else return true;
            }


        }else {//CD炉在旁边
            if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION && data.getcMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                return true;
            }else if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION && data.getcMode() != CookerConstant.COOKER_WORK_MODE_UNION) {
                if ((cpower + dpower) >  getTotalSingleRightPower()) return false;
                if ((cpower + dpower) <=  getTotalSingleRightPower() && (cpower + dpower + apower + bpower + epower + fpower) <= getTotalPower()) return true;
                if ((cpower + dpower) <=  getTotalSingleRightPower() && (cpower + dpower + apower + bpower + epower + fpower) > getTotalPower()) return false;

            }else if (data.getbMode() != CookerConstant.COOKER_WORK_MODE_UNION && data.getcMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                if ((apower + bpower) >  getTotalSingleLeftPower()) return false;
                if ((apower + bpower) <=  getTotalSingleLeftPower() && (cpower + dpower + apower + bpower + epower + fpower) <= getTotalPower()) return true;
                if ((apower + bpower) <=  getTotalSingleLeftPower() && (cpower + dpower + apower + bpower + epower + fpower) > getTotalPower()) return false;
            }else if  (data.getbMode() != CookerConstant.COOKER_WORK_MODE_UNION && data.getcMode() != CookerConstant.COOKER_WORK_MODE_UNION) {
                if ((apower + bpower) >  getTotalSingleLeftPower() || (cpower + dpower) >  getTotalSingleRightPower() || (cpower + dpower + apower + bpower + epower + fpower) > getTotalPower()) return false;
                else return true;
            }else if (data.getbMode() == CookerConstant.COOKER_WORK_MODE_UNION && data.getcMode() == CookerConstant.COOKER_WORK_MODE_UNION) {
                return true;
            }
        }


        return true;
    }

    protected boolean setCookerTotalPower(int power) {
        boolean result = false;
        LogUtil.d("Enter:: setCookerTotalPower---->" + currentTotalPower);
        if (currentTotalPower == 0) {
            setTotalPower(power);
            result = true;
        }else {
            result = false;
        }
        return result;
    }

    protected abstract int getCookerType();
    protected abstract int[] getCookerAPowerTable();
    protected abstract int[] getCookerBPowerTable();
    protected abstract int[] getCookerCPowerTable();
    protected abstract int[] getCookerDPowerTable();
    protected abstract int[] getCookerEPowerTable();
    protected abstract int[] getCookerFPowerTable();
    protected abstract int[] getCookerUnionLeftPowerTable();
    protected abstract int[] getCookerUnionRightPowerTable();
    protected abstract int getTotalPower();
    protected abstract void setTotalPower(int power);
    protected abstract int getTotalSingleLeftPower();
    protected abstract int getTotalSingleRightPower();

}
