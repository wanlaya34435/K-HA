package com.zti.kha.model.User;

import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

public class RoleAdmin {
    public Boolean isSuperAdmin=false;
    public List<String> adminGroups = new ArrayList<>();
    public List<String> technicianGroups = new ArrayList<>();


    @Transient
    public List<String> groupsName = new ArrayList<>();
    @Transient
    public List<String> technicianName = new ArrayList<>();
    public List<String> getTechnicianGroups() {
        return technicianGroups;
    }

    public void setTechnicianGroups(List<String> technicianGroups) {
        this.technicianGroups = technicianGroups;
    }

    public List<String> getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(List<String> technicianName) {
        this.technicianName = technicianName;
    }

    public Boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public List<String> getAdminGroups() {
        return adminGroups;
    }

    public void setAdminGroups(List<String> adminGroups) {
        this.adminGroups = adminGroups;
    }

    public List<String> getGroupsName() {
        return groupsName;
    }

    public void setGroupsName(List<String> groupsName) {
        this.groupsName = groupsName;
    }
}
