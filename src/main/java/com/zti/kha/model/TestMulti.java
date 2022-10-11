package com.zti.kha.model;

import java.util.ArrayList;
import java.util.List;

public class TestMulti {
    List<String> fileName= new ArrayList<>();


    public List<String> getFileName() {
        return fileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public TestMulti(List<String> fileName) {
        this.fileName = fileName;
    }
}
