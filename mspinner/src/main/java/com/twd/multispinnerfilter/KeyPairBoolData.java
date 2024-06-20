package com.twd.multispinnerfilter;

import java.io.Serializable;

public class KeyPairBoolData implements Serializable {
    private long id;
    private String name;
    private boolean selected;
    private Object object;

    public KeyPairBoolData() {
    }

    public KeyPairBoolData(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }
}
