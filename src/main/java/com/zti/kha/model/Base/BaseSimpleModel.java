package com.zti.kha.model.Base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * Created by up on 3/6/17.
 */
public class BaseSimpleModel {

    @JsonProperty(value = "id",access = JsonProperty.Access.READ_ONLY)
    @Id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
