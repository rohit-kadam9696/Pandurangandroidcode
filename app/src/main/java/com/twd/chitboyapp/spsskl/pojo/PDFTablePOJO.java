package com.twd.chitboyapp.spsskl.pojo;

import android.view.View;
import android.widget.TableLayout;

public class PDFTablePOJO {
    private View HeadView;
    private String headText;
    private TableLayout tbl;

    public View getHeadView() {
        return HeadView;
    }

    public void setHeadView(View headView) {
        HeadView = headView;
    }

    public TableLayout getTbl() {
        return tbl;
    }

    public void setTbl(TableLayout tbl) {
        this.tbl = tbl;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }
}
