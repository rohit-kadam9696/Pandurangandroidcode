package com.twd.chitboyapp.spsskl.pojo;

public class SavePrintResponse extends ActionResponse {

    private String htmlContent;
    private String printHead;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getPrintHead() {
        return printHead;
    }

    public void setPrintHead(String printHead) {
        this.printHead = printHead;
    }
}
