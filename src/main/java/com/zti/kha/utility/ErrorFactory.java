package com.zti.kha.utility;


import com.zti.kha.model.Base.BaseResponse;

/**
 * Created by S on 10/5/2016.
 */
public class ErrorFactory {

    public static BaseResponse getError(int code, String message){
        return new BaseResponse(code,message,null);
    }


}
