package com.twd.chitboyapp.spsskl.pojo;

public class LotGenResponse extends TableResponse {

    private boolean actionComplete;
    private String successMsg;
    private String failMsg;
    private String vhead;

    private String vprintHead;

    private String htmlContent;

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

    public String getVhead() {
        return vhead;
    }

    public void setVhead(String vhead) {
        this.vhead = vhead;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getVprintHead() {
        return vprintHead;
    }

    public void setVprintHead(String vprintHead) {
        this.vprintHead = vprintHead;
    }
}
