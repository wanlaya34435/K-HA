package com.zti.kha.model.Address;

import org.springframework.data.annotation.Id;

public class District {
    @Id
    private String id;
    private int district_code;
    private String district_th;
    private String district_en;
    private String province_th;
    private String province_en;
    private int province_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(int district_code) {
        this.district_code = district_code;
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

    public int getProvince_code() {
        return province_code;
    }

    public void setProvince_code(int province_code) {
        this.province_code = province_code;
    }
}
