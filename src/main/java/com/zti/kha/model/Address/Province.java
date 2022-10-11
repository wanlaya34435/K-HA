package com.zti.kha.model.Address;

import org.springframework.data.annotation.Id;

public class Province {
    @Id
    private String id;
    private int province_code;
    private String province_th;
    private String province_en;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProvince_code() {
        return province_code;
    }

    public void setProvince_code(int province_code) {
        this.province_code = province_code;
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
}
