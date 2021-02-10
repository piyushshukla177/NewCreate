package com.otpl.newcreate.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HunarInterest {
    @SerializedName("hunarId")
    @Expose
    private Integer hunarId;
    @SerializedName("isSelected")
    @Expose
    private boolean selected;
    @SerializedName("isDeleted")
    @Expose
    private boolean deleted;

    public Integer getHunarId() {
        return hunarId;
    }

    public void setHunarId(Integer hunarId) {
        this.hunarId = hunarId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
