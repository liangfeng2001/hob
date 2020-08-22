package com.ekek.tfthobmodule.event;

import com.ekek.tfthobmodule.constants.TFTHobConstant;

public class DetectCookerEvent {

    boolean detectCookerStatusA = true;
    boolean detectCookerStatusB = true;
    boolean detectCookerStatusC = true;
    boolean detectCookerStatusD = true;
    boolean detectCookerStatusE = true;
    boolean detectCookerStatusF = true;

    public DetectCookerEvent(boolean detectCookerStatusA, boolean detectCookerStatusB, boolean detectCookerStatusC, boolean detectCookerStatusD, boolean detectCookerStatusE, boolean detectCookerStatusF) {
        this.detectCookerStatusA = detectCookerStatusA;
        this.detectCookerStatusB = detectCookerStatusB;
        this.detectCookerStatusC = detectCookerStatusC;
        this.detectCookerStatusD = detectCookerStatusD;
        this.detectCookerStatusE = detectCookerStatusE;
        this.detectCookerStatusF = detectCookerStatusF;
    }

    public boolean getDetectCookerResult(int cookerID) {
        boolean result = true;
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                result = detectCookerStatusA;
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                result = detectCookerStatusB;
                break;
            case TFTHobConstant.COOKER_TYPE_AB_COOKER:
                result = detectCookerStatusA || detectCookerStatusB;
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                result = detectCookerStatusC;
                break;
            case TFTHobConstant.COOKER_TYPE_D_COOKER:
                result = detectCookerStatusD;
                break;
            case TFTHobConstant.COOKER_TYPE_E_COOKER:
                result = detectCookerStatusE;
                break;
            case TFTHobConstant.COOKER_TYPE_F_COOKER:
                result = detectCookerStatusF;
                break;
            case TFTHobConstant.COOKER_TYPE_EF_COOKER:
                result = detectCookerStatusE || detectCookerStatusF;
                break;

        }

        return result;
    }


}
