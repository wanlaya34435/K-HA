package com.zti.kha.model;


import com.zti.kha.model.Base.BaseModel;
public class Category extends BaseModel {
    private String categoryName="";
    private String categoryType="";
    private String categoryImage="";
    private String categoryCode="";
    private int sequenceNo;
    private String parentCategory="";


    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }


    public int getSequenceNo() {
        return sequenceNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }



    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }
}
