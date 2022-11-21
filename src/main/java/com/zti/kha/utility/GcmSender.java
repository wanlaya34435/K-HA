package com.zti.kha.utility;

import com.zti.kha.api.CommonApi;
import com.zti.kha.controller.NotificationsRepository;
import com.zti.kha.model.Content.Noti.Notifications;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by S on 5/30/2016.
 */
public class GcmSender extends CommonApi {


    public static void main(String[] args) {


    }

    public static void send(String id, String title, String type, String token, boolean isTopic, String GCM_KEY, String groupId,String TOPIC) {
        //TODO condition only work only 3 topic maximum so more than that need to use loop
        try {
            JSONObject gcmJson = new JSONObject();
            //FOR iOS Pattern
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("sound", "default");
            if (isTopic == true) {
                gcmJson.put("condition", "'" + TOPIC+groupId + "' in topics");

            } else {
                gcmJson.put("to", token);
            }
            JSONObject data = new JSONObject();
            data.put("id", id);
            data.put("title", title);
            data.put("contentType", type);
            data.put("groupId", groupId);
            data.put("message", "message");
            gcmJson.put("notification", notification);
            gcmJson.put("data", data);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + GCM_KEY);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Content-Length", "" + gcmJson.toString().getBytes("UTF-8").length);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(gcmJson.toString().getBytes("UTF-8"));
            outputStream.flush();
            conn.connect();
            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
        } catch (IOException | JSONException e) {
//            e.printStackTrace();
        }
    }

    public static void sendForNotification(String id, String title, String type, int badge, String groupId, NotificationsRepository notificationsRepository, String GCM_KEY,String TOPIC) throws PostExceptions {

        //TODO condition only work only 3 topic maximum so more than that need to use loop

        try {
            JSONObject gcmJson = new JSONObject();
            //FOR iOS Pattern
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("sound", "default");

            gcmJson.put("condition", "'" + TOPIC+groupId + "' in topics");
            notification.put("badge", badge + 1);
            JSONObject data = new JSONObject();
            data.put("id", id);
            data.put("title", title);
            data.put("groupId", groupId);
            data.put("contentType", type);
            data.put("message", "message");
            gcmJson.put("notification", notification);
            gcmJson.put("data", data);




            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + GCM_KEY);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Content-Length", "" + gcmJson.toString().getBytes("UTF-8").length);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(gcmJson.toString().getBytes("UTF-8"));
            outputStream.flush();
            conn.connect();
            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
        } catch (IOException | JSONException e) {
            Notifications byId = notificationsRepository.findById(id).get();
            byId.setStatus(3);
            byId.setErrorPush(e.getStackTrace().toString()+"-------"+e.toString());
            notificationsRepository.save(byId);
            e.printStackTrace();
        }
    }

    public static void sendInformComplain(String id, String type, String token, int statusCode, String GCM_KEY, boolean isAdmin, String groupId ,String complainId) {

        //TODO condition only work only 3 topic maximum so more than that need to use loop

        try {
            String body = "";
            String title = "";
            String msgCode = "";
            if (statusCode == 0) {
                msgCode = "รอดำเนินการ";
            } else if (statusCode == 1) {
                msgCode = "ระหว่างดำเนินการ";

            } else if (statusCode == 3) {
                msgCode = "ดำเนินการเสร็จสิ้น";

            } else if (statusCode == 2) {
                msgCode = "ยกเลิก";

            }else if (statusCode == 4) {
                msgCode = "นอกเหนือเขตความรับผิดชอบ";

            }else if (statusCode == 5) {
                msgCode = "รับเรื่องแล้ว";

            }
            if (isAdmin == true) {
                body = "มีการแจ้งร้องเรียนในเขตการรับผิดชอบของท่าน สามารถดูรายละเอียดได้ที่กล่องจดหมาย";
                title = "แจ้งการร้องเรียนใหม่";
            } else {
                body = "การร้องเรียนของท่านอยู่ในสถานะ : " + msgCode + "สามารถดูรายละเอียดได้ที่หัวข้อประวัติการร้องเรียน";
                title = "แจ้งอัพเดตสถานะ";
            }

            JSONObject gcmJson = new JSONObject();
            //FOR iOS Pattern
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", body);
            notification.put("sound", "default");
            gcmJson.put("to", token);

            JSONObject data = new JSONObject();
            data.put("id", id);
            data.put("title", body);
            data.put("groupId", groupId);
            data.put("complainId", complainId);
            data.put("statusCode", statusCode);
            data.put("contentType", type);
            data.put("message", "message");
            gcmJson.put("notification", notification);
            gcmJson.put("data", data);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + GCM_KEY);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(gcmJson.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();

        } catch (IOException | JSONException e) {

//            e.printStackTrace();
        }
    }
    public static void sendCommentComplain(String id, String complainId, String type, String token, String GCM_KEY, String des, String groupId) {

        //TODO condition only work only 3 topic maximum so more than that need to use loop

        try {
            String body = des;
            String title = "มีการแสดงความคิดใหม่";


            JSONObject gcmJson = new JSONObject();
            //FOR iOS Pattern
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", body);
            notification.put("sound", "default");
            gcmJson.put("to", token);

            JSONObject data = new JSONObject();
            data.put("id", id);
            data.put("title", body);
            data.put("complainId", complainId);
            data.put("groupId", groupId);
            data.put("contentType", type);
            data.put("message", "message");
            gcmJson.put("notification", notification);
            gcmJson.put("data", data);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + GCM_KEY);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(gcmJson.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();

        } catch (IOException | JSONException e) {

//            e.printStackTrace();
        }
    }

}
