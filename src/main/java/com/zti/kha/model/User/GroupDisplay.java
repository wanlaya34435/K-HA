package com.zti.kha.model.User;


import com.zti.kha.model.Base.BaseModel;
import org.springframework.data.annotation.Transient;

/**
 * Created by up on 3/13/17.
 */
public class GroupDisplay  {
    private String name="";
    private String icon="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
