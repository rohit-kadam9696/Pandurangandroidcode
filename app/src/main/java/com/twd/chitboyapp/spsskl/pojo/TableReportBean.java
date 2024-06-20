package com.twd.chitboyapp.spsskl.pojo;

import java.util.ArrayList;
import java.util.HashMap;

public class TableReportBean {
    private HashMap<String, Integer> rowColSpan;
    private Integer noofHeads;
    private boolean footer;
    private boolean marathi;
    private ArrayList<ArrayList<String>> tableData = new ArrayList<>();
    private HashMap<String, Boolean> boldIndicator;

    private HashMap<String, Integer> rowSpan; // 1 : full 2 : ltr 3 : lr : 4 : lbr
    private HashMap<String, String> floatings;

    private HashMap<String, Integer> textAlign; // 1 : left 2 : center 3 :right
    private Integer[] colWidth;

    private HashMap<String, Boolean> visibility;

    public HashMap<String, Integer> getRowColSpan() {
        return rowColSpan;
    }

    public void setRowColSpan(HashMap<String, Integer> rowColSpan) {
        this.rowColSpan = rowColSpan;
    }

    public Integer getNoofHeads() {
        return noofHeads;
    }

    public void setNoofHeads(Integer noofHeads) {
        this.noofHeads = noofHeads;
    }

    public ArrayList<ArrayList<String>> getTableData() {
        return tableData;
    }

    public void setTableData(ArrayList<ArrayList<String>> tableData) {
        this.tableData = tableData;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public HashMap<String, Boolean> getBoldIndicator() {
        return boldIndicator;
    }

    public void setBoldIndicator(HashMap<String, Boolean> boldIndicator) {
        this.boldIndicator = boldIndicator;
    }

    public HashMap<String, String> getFloatings() {
        return floatings;
    }

    public void setFloatings(HashMap<String, String> floatings) {
        this.floatings = floatings;
    }

    public boolean isMarathi() {
        return marathi;
    }

    public void setMarathi(boolean marathi) {
        this.marathi = marathi;
    }

    public HashMap<String, Integer> getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(HashMap<String, Integer> rowSpan) {
        this.rowSpan = rowSpan;
    }

    public HashMap<String, Integer> getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(HashMap<String, Integer> textAlign) {
        this.textAlign = textAlign;
    }

    public Integer[] getColWidth() {
        return colWidth;
    }

    public void setColWidth(Integer[] colWidth) {
        this.colWidth = colWidth;
    }

    public HashMap<String, Boolean> getVisibility() {
        return visibility;
    }

    public void setVisibility(HashMap<String, Boolean> visibility) {
        this.visibility = visibility;
    }
}
