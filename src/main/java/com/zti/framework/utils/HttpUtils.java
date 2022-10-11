package com.zti.framework.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HttpUtils {
    static Logger log = LogManager.getLogger(HttpUtils.class);

    //====================================================================================================//
    //
    // Standard POST PUT GET DELETE
    //
    //====================================================================================================//

    public static String GET(String url, String contentType) throws IOException {
        log.info("GET URL : " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", contentType);
        httpGet.setHeader("Content-type", contentType);

        CloseableHttpResponse response = client.execute(httpGet);

        String result = getResponse(response);
        client.close();

        return result;
    }

    public static String POST(String url, String contentType, String data, String headerToken) throws IOException {
//        log.info("POST URL : " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", contentType);
        httpPost.setHeader("Content-type", contentType);
        httpPost.setHeader("token", headerToken);

        if (data != null) {
            StringEntity entity = new StringEntity(data, "UTF-8");
            httpPost.setEntity(entity);
            log.info(data);
        }
        CloseableHttpResponse response = client.execute(httpPost);

        String result = getResponse(response);
        client.close();

        return result;
    }

    private static String PUT(String url, String contentType, String data) throws IOException {
        log.info("PUT URL : " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Accept", contentType);
        httpPut.setHeader("Content-type", contentType);

        if (data != null) {
            StringEntity entity = new StringEntity(data, "UTF-8");
            httpPut.setEntity(entity);
            log.info(data);
        }
        CloseableHttpResponse response = client.execute(httpPut);

        String result = getResponse(response);
        client.close();

        return result;
    }

    private static String DELETE(String url, String contentType) throws IOException {
        log.info("DELETE URL : " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader("Accept", contentType);
        httpDelete.setHeader("Content-type", contentType);


        CloseableHttpResponse response = client.execute(httpDelete);

        String result = getResponse(response);
        client.close();

        return result;
    }

    public static String GET_TO_SERVICE(String url, String contentType) throws IOException {
        log.info("GET_TO_SERVICE URL : " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", contentType);
        httpGet.setHeader("Content-type", contentType);

        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        String responseStr = EntityUtils.toString(httpEntity);
        client.close();
        log.info("  GET_TO_SERVICE RESPONSE : " + responseStr);

        return responseStr;
    }

    public static String POST_TO_SERVICE(String url, String contentType, String data) throws IOException {
        log.info("POST_TO_SERVICE URL : " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", contentType);
        httpPost.setHeader("Content-type", contentType);

        if (data != null) {
            StringEntity entity = new StringEntity(data, "UTF-8");
            httpPost.setEntity(entity);
            log.info(data);
        }
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity httpEntity = response.getEntity();
        String responseStr = EntityUtils.toString(httpEntity);
        client.close();

        log.info("  POST_TO_SERVICE RESULT : " + responseStr);
        return responseStr;
    }

    private static String getResponse(CloseableHttpResponse response)  {
        HttpEntity httpEntity = response.getEntity();
        int responseCode = response.getStatusLine().getStatusCode();
        String responseStr = null;
        try {
            responseStr = EntityUtils.toString(httpEntity);
        }
        catch (IllegalArgumentException e){
            log.error(e);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return responseStr;
    }


}
