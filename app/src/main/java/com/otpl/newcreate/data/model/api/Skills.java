package com.otpl.newcreate.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Skills {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("otherQualification")
    @Expose
    private String otherQualification;
    @SerializedName("isSkilledLabour")
    @Expose
    private Boolean isSkilledLabour;
    @SerializedName("skill")
    @Expose
    private String skill;
    @SerializedName("otherSkill")
    @Expose
    private String otherSkill;
    @SerializedName("hasCertificate")
    @Expose
    private Boolean hasCertificate;
    @SerializedName("certificate")
    @Expose
    private String certificate;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getOtherQualification() {
        return otherQualification;
    }

    public void setOtherQualification(String otherQualification) {
        this.otherQualification = otherQualification;
    }

    public Boolean getIsSkilledLabour() {
        return isSkilledLabour;
    }

    public void setIsSkilledLabour(Boolean isSkilledLabour) {
        this.isSkilledLabour = isSkilledLabour;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getOtherSkill() {
        return otherSkill;
    }

    public void setOtherSkill(String otherSkill) {
        this.otherSkill = otherSkill;
    }

    public Boolean getHasCertificate() {
        return hasCertificate;
    }

    public void setHasCertificate(Boolean hasCertificate) {
        this.hasCertificate = hasCertificate;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
