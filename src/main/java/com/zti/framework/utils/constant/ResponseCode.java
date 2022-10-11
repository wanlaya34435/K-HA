package com.zti.framework.utils.constant;

public class ResponseCode {
    public static final int ERROR_CODE_INTERNAL_SERVER_ERROR = 500;
    public static final int ERROR_CODE_USER_ALREADY_EXIST = 511;
    public static final int ERROR_CODE_USER_WAITING_FOR_ACTIVE = 512;
    public static final int ERROR_CODE_USER_INACTIVE = 513;
    public static final int ERROR_CODE_USER_WRONG_USERNAME_OR_PASSWORD = 514;
    public static final int ERROR_CODE_USER_EMAIL_NOT_MATCH   = 515;
    public static final int ERROR_CODE_USER_CANNOT_DECODE_TOKEN   = 516;
    public static final int ERROR_CODE_EXAM_CANNOT_PROCESS_RESULT   = 517;


    public static final int SUCCESS_CODE            = 200;
    public static final int SUCCESS_CODE_RESET_PWD = 215;
}

