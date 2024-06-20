package com.twd.chitboyapp.spsskl.pojo;

public class ActionResponse extends MainResponse {
    private boolean actionComplete;
    private String successMsg;
    private String failMsg;
    private String newStatus;

    public boolean isActionComplete() {
        return actionComplete;
    }

    public void setActionComplete(boolean actionComplete) {
        this.actionComplete = actionComplete;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
