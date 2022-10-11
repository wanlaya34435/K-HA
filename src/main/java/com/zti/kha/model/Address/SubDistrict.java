package com.zti.kha.model.Address;

import org.springframework.data.annotation.Id;

public class SubDistrict {
    @Id
    private String id;
    private int subDistrict_code;
    private String subDistrict_th;
    private String subDistrict_en;
    private String district_th;
    private String district_en;
    private String province_th;
    private String province_en;
    private String postal_code;
    private int district_code;
    private int province_code;

    public String getId() {
        return id;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSubDistrict_code() {
        return subDistrict_code;
    }

    public void setSubDistrict_code(int subDistrict_code) {
        this.subDistrict_code = subDistrict_code;
    }

    public String getSubDistrict_th() {
        return subDistrict_th;
    }

    public void setSubDistrict_th(String subDistrict_th) {
        this.subDistrict_th = subDistrict_th;
    }

    public String getSubDistrict_en() {
        return subDistrict_en;
    }

    public void setSubDistrict_en(String subDistrict_en) {
        this.subDistrict_en = subDistrict_en;
    }

    public String getDistrict_th() {
        return district_th;
    }

    public void setDistrict_th(String district_th) {
        this.district_th = district_th;
    }

    public String getDistrict_en() {
        return district_en;
    }

    public void setDistrict_en(String district_en) {
        this.district_en = district_en;
    }

    public String getProvince_th() {
        return province_th;
    }

    public void setProvince_th(String province_th) {
        this.province_th = province_th;
    }

    public String getProvince_en() {
        return province_en;
    }

    public void setProvince_en(String province_en) {
        this.province_en = province_en;
    }

    public int getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(int district_code) {
        this.district_code = district_code;
    }

    public int getProvince_code() {
        return province_code;
    }

    public void setProvince_code(int province_code) {
        this.province_code = province_code;
    }
}
