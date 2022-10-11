package com.zti.kha.model.User;
import com.zti.kha.model.Base.BaseModel;
import org.springframework.data.annotation.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by S on 9/22/2016.
 */
//TODO need to add setter getter for binding request parameter
public class Profile extends BaseModel implements Serializable {
    public String gcmToken;
    public String imageProfile;
    public String secret;
    public String userName;
    public String email;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String permissionMenu;
    public String permissionButton;
    public String idCard;
    public int amountResidents;
    public int type;
    public String job;
    public String khaId="";
    public String address;
    public Date endContract;
    public String provinceCode="";
    @Transient
    public String provinceName="";
    @Transient
    public String zipcode="";
    public String districtCode="";
    @Transient
    public String districtName="";
    public String subDistrictCode="";
    @Transient
    public String subDistrictName="";
    @Transient
    public String token;
    @Transient
    public Group khaProfile;
    public RoleAdmin role;
    public List<ReadGroup> readGroups = new ArrayList<>();
    public List<ReadGroup> pendingGroups = new ArrayList<>();

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public Date getEndContract() {
        return endContract;
    }
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Group getKhaProfile() {
        return khaProfile;
    }

    public void setKhaProfile(Group khaProfile) {
        this.khaProfile = khaProfile;
    }

    public void setEndContract(Date endContract) {
        this.endContract = endContract;
    }

    public int getAmountResidents() {
        return amountResidents;
    }

    public void setAmountResidents(int amountResidents) {
        this.amountResidents = amountResidents;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getKhaId() {
        return khaId;
    }

    public void setKhaId(String khaId) {
        this.khaId = khaId;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getSubDistrictCode() {
        return subDistrictCode;
    }

    public void setSubDistrictCode(String subDistrictCode) {
        this.subDistrictCode = subDistrictCode;
    }

    public String getSubDistrictName() {
        return subDistrictName;
    }

    public void setSubDistrictName(String subDistrictName) {
        this.subDistrictName = subDistrictName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPermissionMenu() {
        return permissionMenu;
    }

    public void setPermissionMenu(String permissionMenu) {
        this.permissionMenu = permissionMenu;
    }

    public String getPermissionButton() {
        return permissionButton;
    }

    public void setPermissionButton(String permissionButton) {
        this.permissionButton = permissionButton;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RoleAdmin getRole() {
        return role;
    }

    public void setRole(RoleAdmin role) {
        this.role = role;
    }

    public List<ReadGroup> getReadGroups() {
        return readGroups;
    }

    public void setReadGroups(List<ReadGroup> readGroups) {
        this.readGroups = readGroups;
    }

    public List<ReadGroup> getPendingGroups() {
        return pendingGroups;
    }

    public void setPendingGroups(List<ReadGroup> pendingGroups) {
        this.pendingGroups = pendingGroups;
    }

    public String getGcmToken() {
        return gcmToken;
    }

}
