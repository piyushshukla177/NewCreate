package com.otpl.newcreate.data.model.api;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.otpl.newcreate.R;


import java.io.Serializable;


public class Sector implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sectorId")
    @Expose
    private Integer sectorId;
    @SerializedName("sector")
    @Expose
    private String sector;
    @SerializedName("jobRoles")
    @Expose
    private String jobRoles;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("isSelected")
    @Expose
    private int isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getJobRoles() {
        return jobRoles;
    }

    public void setJobRoles(String jobRoles) {
        this.jobRoles = jobRoles;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    @BindingAdapter({"sector_image"})
    public static void loadSectorImage(ImageView imageView, String imageUrl) {
        Context context = imageView.getContext();
       /* Drawable mDefaultImage =
                context.getResources().getDrawable(R.drawable.ic_image_loading);
        Glide.with(context)
                .load("http://demo.otpl.in/abhaapi/HunarIcons/" + imageUrl)
                .placeholder(R.drawable.ic_image_loading)
                .error(mDefaultImage)
                .into(imageView);*/
    }

    @BindingAdapter("favourite_image")
    public static void loadImage(ImageView view, int selected) {
        Context mContext = view.getContext();
        if (selected == 1) {
            view.setImageDrawable(ContextCompat.getDrawable(mContext,
                    R.drawable.ic_baseline_add_circle_orange_24));
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(mContext,
                    R.drawable.ic_baseline_add_circle_24));
        }
    }

}
