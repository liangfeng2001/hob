package com.ekek.hardwaremodule.power;

import com.ekek.hardwaremodule.constants.CookerConstant;

public class KSO90CookerPowerData extends BaseCookerPowerData {
    private static final int TOTAL_POWER = 9000;
    private static final int TOTAL_SINGLE_LEFT_POWER = 3600;
    private static final int TOTAL_SINGLE_RIGHT_POWER = 3600;
    private static final int[] COOKER_A_POWER_TABLE = {0,100,150,200,250,350,600,1000,1500,2000,2800,2000,2000};
    private static final int[] COOKER_B_POWER_TABLE = {0,100,150,200,250,350,600,1000,1300,1500,2000,1500,1500};
    private static final int[] COOKER_C_POWER_TABLE = {0,100,150,250,350,600,1000,1300,1800,2300,3000,2300,2300};
    private static final int[] COOKER_D_POWER_TABLE = {0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static final int[] COOKER_E_POWER_TABLE = {0,100,150,200,250,350,600,1000,1300,1500,2000,1500,1500};
    private static final int[] COOKER_F_POWER_TABLE = {0,100,150,200,250,350,600,1000,1500,2000,2800,2000,2000};
    private static final int[] COOKER_UNION_LEFT_POWER_TABLE = {0,200,300,400,500,700,1200,2000,2600,3000,3600,3000,3000};
    private static final int[] COOKER_UNION_RIGHT_POWER_TABLE = {0,200,300,400,500,700,1200,2000,2600,3000,3600,3000,3000};

    @Override
    protected int getCookerType() {
        return CookerConstant.COOKER_TYPE_KSO_90;
    }

    @Override
    protected int[] getCookerAPowerTable() {
        return COOKER_A_POWER_TABLE;
    }

    @Override
    protected int[] getCookerBPowerTable() {
        return COOKER_B_POWER_TABLE;
    }

    @Override
    protected int[] getCookerCPowerTable() {
        return COOKER_C_POWER_TABLE;
    }

    @Override
    protected int[] getCookerDPowerTable() {
        return COOKER_D_POWER_TABLE;
    }

    @Override
    protected int[] getCookerEPowerTable() {
        return COOKER_E_POWER_TABLE;
    }

    @Override
    protected int[] getCookerFPowerTable() {
        return COOKER_F_POWER_TABLE;
    }

    @Override
    protected int[] getCookerUnionLeftPowerTable() {
        return COOKER_UNION_LEFT_POWER_TABLE;
    }

    @Override
    protected int[] getCookerUnionRightPowerTable() {
        return COOKER_UNION_RIGHT_POWER_TABLE;
    }

    @Override
    protected int getTotalPower() {
        return TOTAL_POWER;
    }

    @Override
    protected void setTotalPower(int power) {

    }

    @Override
    protected int getTotalSingleLeftPower() {
        return TOTAL_SINGLE_LEFT_POWER;
    }

    @Override
    protected int getTotalSingleRightPower() {
        return TOTAL_SINGLE_RIGHT_POWER;
    }
}
