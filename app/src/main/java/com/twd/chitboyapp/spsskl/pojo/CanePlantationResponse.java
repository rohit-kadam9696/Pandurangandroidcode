package com.twd.chitboyapp.spsskl.pojo;

import java.util.ArrayList;

public class CanePlantationResponse extends MainResponse {
    private ArrayList<String> removeEntry = new ArrayList<>();
    private String successMessage;

    public ArrayList<String> getRemoveEntry() {
        return removeEntry;
    }

    public void setRemoveEntry(ArrayList<String> removeEntry) {
        this.removeEntry = removeEntry;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
