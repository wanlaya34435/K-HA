package com.zti.kha.model.Common;

import com.zti.kha.model.Base.BaseModel;
import org.springframework.data.annotation.Transient;

/**
 * Created by S on 10/5/2016.
 */
public class LocalizeText  extends BaseModel {

    @Transient
    public static final String LANGUAGE_CODE_DEFAULT="th";


    String  duplicateEmail="ไม่สามารถลงทะเบียนได้เนื่องจากมีอีเมลนี้ในระบบแล้ว";
    String  duplicateFacebook="มีเฟสบุ๊คนี้อยู่ในระบบแล้ว";
    String  duplicateIdCard="รหัสบัตรประชาชนซ้ำ";
    String  duplicateGroup="ชื่อกลุ่มซ้ำ";
    String  duplicateSubscribe="ติดตามกลุ่มซ้ำ";
    String  duplicatePending="มีรายชื่ออยู่ในกลุ่มกำลังรออนุมัติแล้ว";

    String  wrongIdCard="รหัสบัตรประชาชนต้องมี 13 หลัก";
    String  duplicateGoogle="googleIdนี้อยู่ในระบบแล้ว";
    String  duplicateLine="lineIdนี้อยู่ในระบบแล้ว";
    String  duplicateApple="appleIdนี้อยู่ในระบบแล้ว";

    String  duplicate="ข้อมูลซ้ำ";
    String  duplicateUserName="ชื่อผู้ใช้งานซ้ำ";
    String  noUserFound ="ไม่พบผู้ใช้งานนี้";
    String  noContent  ="ไม่พบข้อมูล";
    String  noReadGroups ="บัญชีของท่านอยู่ระหว่างการตรวจสอบ หากผลการอนุมัติสำเร็จแล้วจะมีข้อความแจ้งเตือนไปยังอีเมลของท่าน";
    String  noGroup="ไม่พบข้อมูลกลุ่ม";
    String  noUnScribeGroup="ท่านไม่สามารถเลิกติดตามกลุ่มหลักได้";

    String duplicateCategoryCode ="ชื่อหมวดหมู่ซ้ำ";
    String duplicateInstitutionCode ="ชื่อสำนักงานซ้ำ";
    String duplicateGeneration ="ชื่อรุ่นซ้ำ";
    String  invalidID="หมายเลขรหัสประชาชนไม่ถูกต้อง";
    String  invalidEmail="อีเมลไม่ถูกต้อง";
    String  deleted="ลบ";
    String  showed="โชว์";
    String  hidden="ซ่อน";
    String userDisable="บัญชีนี้ถูกปิดการใช้งาน";

    String permissionDenied="ไม่มีสิทธิ์การเข้าถึงข้อมูล";
    String duplicateVillage="ชื่อหมู่บ้านซ้ำ";
    String newFacebookAccount="ยังไม่ได้สมัครสมาชิก";
    String tokenExpire="หมดอายุการใช้งาน";
    String  updatedForget="ส่งคำขอสำเร็จ";
    String  noExcel="ไม่ใช่ไฟล์ Excel";
    String  dataUpdated="อัพเดทข้อมูลสำเร็จ";
    String  statusUpdated="อัพเดทสถานะสำเร็จ";
    String  postUploaded="อัพโหลดสำเร็จ";
    String  postUploadFail="อัพโหลดไม่สำเร็จ";
    String  loginSucceed="เข้าสู่ระบบ";
    String  loginFailed="เข้าสู่ระบบไม่สำเร็จ" ;
    String  fileUploaded="อัพโหลดไฟล์สำเร็จ";
    String  fileUploadFailed="อัพโหลดไฟล์ไม่สำเร็จ";
    String  registerSucceed="สมัครสมาชิกสำเร็จ";
    String  wrongPassword ="รหัสผ่านไม่ถูกต้อง";
    String  wrongPhoneNumber ="หมายเลขโทรศัพท์ไม่ถูกต้อง";
    String  noUpdate="ไม่อัพเดท";
    String  failed="ไม่สำเร็จ";
    String  failedLine="Line Error";
    String  failedActivateUser="ไม่สามารถเปิดการใช้งานไอดีนี้ได้";
    String  passwordNotMatch="ยืนยันรหัสผ่านใหม่อีกครั้ง";

    public void setDuplicateLine(String duplicateLine) {
        this.duplicateLine = duplicateLine;
    }

    public String getDuplicateGeneration() {
        return duplicateGeneration;
    }

    public String getDuplicateApple() {
        return duplicateApple;
    }

    public String getNoGroup() {
        return noGroup;
    }

    public String getNoUnScribeGroup() {
        return noUnScribeGroup;
    }

    public String getUserDisable() {
        return userDisable;
    }

    public String getNoReadGroups() {
        return noReadGroups;
    }

    public String getDuplicateSubscribe() {
        return duplicateSubscribe;
    }

    public String getDuplicatePending() {
        return duplicatePending;
    }

    public String getDuplicateGroup() {
        return duplicateGroup;
    }

    public void setDuplicateApple(String duplicateApple) {
        this.duplicateApple = duplicateApple;
    }

    public void setDuplicateGeneration(String duplicateGeneration) {
        this.duplicateGeneration = duplicateGeneration;
    }

    public String getDuplicate() {
        return duplicate;
    }

    public String getFailedActivateUser() {
        return failedActivateUser;
    }

    public void setFailedActivateUser(String failedActivateUser) {
        this.failedActivateUser = failedActivateUser;
    }

    public String getDuplicateInstitutionCode() {
        return duplicateInstitutionCode;
    }

    public String getDuplicateGoogle() {
        return duplicateGoogle;
    }

    public String getDuplicateLine() {
        return duplicateLine;
    }

    public void setDuplicateGoogle(String duplicateGoogle) {
        this.duplicateGoogle = duplicateGoogle;
    }

    public String getFailedLine() {
        return failedLine;
    }

    public void setFailedLine(String failedLine) {
        this.failedLine = failedLine;
    }

    public void setDuplicateInstitutionCode(String duplicateInstitutionCode) {
        this.duplicateInstitutionCode = duplicateInstitutionCode;
    }
    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }
    public String getUpdatedForget() {
        return updatedForget;
    }

    public String getDuplicateFacebook() {
        return duplicateFacebook;
    }

    public void setDuplicateFacebook(String duplicateFacebook) {
        this.duplicateFacebook = duplicateFacebook;
    }

    public void setUpdatedForget(String updatedForget) {
        this.updatedForget = updatedForget;
    }

    public String getNoExcel() {
        return noExcel;
    }

    public void setNoExcel(String noExcel) {
        this.noExcel = noExcel;
    }

    public String getFailed() {
        return failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
    }

    public String getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(String tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getDuplicateIdCard() {
        return duplicateIdCard;
    }

    public void setDuplicateIdCard(String duplicateIdCard) {
        this.duplicateIdCard = duplicateIdCard;
    }

    public String getWrongIdCard() {
        return wrongIdCard;
    }

    public void setWrongIdCard(String wrongIdCard) {
        this.wrongIdCard = wrongIdCard;
    }

    public static String getLanguageCodeDefault() {
        return LANGUAGE_CODE_DEFAULT;
    }

    public String getDuplicateVillage() {
        return duplicateVillage;
    }

    public void setDuplicateVillage(String duplicateVillage) {
        this.duplicateVillage = duplicateVillage;
    }







    public String getDuplicateEmail() {
        return duplicateEmail;
    }

    public void setDuplicateEmail(String duplicateEmail) {
        this.duplicateEmail = duplicateEmail;
    }

    public String getDuplicateUserName() {
        return duplicateUserName;
    }

    public void setDuplicateUserName(String duplicateUserName) {
        this.duplicateUserName = duplicateUserName;
    }

    public String getNoUserFound() {
        return noUserFound;
    }

    public void setNoUserFound(String noUserFound) {
        this.noUserFound = noUserFound;
    }

    public String getNoContent() {
        return noContent;
    }

    public void setNoContent(String noContent) {
        this.noContent = noContent;
    }

    public String getDuplicateCategoryCode() {
        return duplicateCategoryCode;
    }

    public void setDuplicateCategoryCode(String duplicateCategoryCode) {
        this.duplicateCategoryCode = duplicateCategoryCode;
    }

    public String getInvalidID() {
        return invalidID;
    }

    public void setInvalidID(String invalidID) {
        this.invalidID = invalidID;
    }

    public String getInvalidEmail() {
        return invalidEmail;
    }

    public void setInvalidEmail(String invalidEmail) {
        this.invalidEmail = invalidEmail;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getShowed() {
        return showed;
    }

    public void setShowed(String showed) {
        this.showed = showed;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public String getPermissionDenied() {
        return permissionDenied;
    }

    public void setPermissionDenied(String permissionDenied) {
        this.permissionDenied = permissionDenied;
    }

    public String getNewFacebookAccount() {
        return newFacebookAccount;
    }

    public void setNewFacebookAccount(String newFacebookAccount) {
        this.newFacebookAccount = newFacebookAccount;
    }

    public String getDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(String dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    public String getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(String statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    public String getPostUploaded() {
        return postUploaded;
    }

    public void setPostUploaded(String postUploaded) {
        this.postUploaded = postUploaded;
    }

    public String getPostUploadFail() {
        return postUploadFail;
    }

    public void setPostUploadFail(String postUploadFail) {
        this.postUploadFail = postUploadFail;
    }

    public String getLoginSucceed() {
        return loginSucceed;
    }

    public void setLoginSucceed(String loginSucceed) {
        this.loginSucceed = loginSucceed;
    }

    public String getLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(String loginFailed) {
        this.loginFailed = loginFailed;
    }

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getFileUploadFailed() {
        return fileUploadFailed;
    }

    public void setFileUploadFailed(String fileUploadFailed) {
        this.fileUploadFailed = fileUploadFailed;
    }

    public String getRegisterSucceed() {
        return registerSucceed;
    }

    public void setRegisterSucceed(String registerSucceed) {
        this.registerSucceed = registerSucceed;
    }

    public String getWrongPassword() {
        return wrongPassword;
    }

    public void setWrongPassword(String wrongPassword) {
        this.wrongPassword = wrongPassword;
    }

    public String getWrongPhoneNumber() {
        return wrongPhoneNumber;
    }

    public void setWrongPhoneNumber(String wrongPhoneNumber) {
        this.wrongPhoneNumber = wrongPhoneNumber;
    }

    public String getNoUpdate() {
        return noUpdate;
    }

    public void setNoUpdate(String noUpdate) {
        this.noUpdate = noUpdate;
    }

    public String getPasswordNotMatch() {
        return passwordNotMatch;
    }

    public void setPasswordNotMatch(String passwordNotMatch) {
        this.passwordNotMatch = passwordNotMatch;
    }
}
