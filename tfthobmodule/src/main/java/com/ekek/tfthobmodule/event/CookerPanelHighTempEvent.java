package com.ekek.tfthobmodule.event;

public class CookerPanelHighTempEvent {
    private boolean isPanelHighTemp;

    public CookerPanelHighTempEvent(boolean isPanelHighTemp) {
        this.isPanelHighTemp = isPanelHighTemp;
    }

    public boolean isPanelHighTemp() {
        return isPanelHighTemp;
    }

    public void setPanelHighTemp(boolean panelHighTemp) {
        isPanelHighTemp = panelHighTemp;
    }
}
