package com.zti.kha.api;

import com.zti.kha.model.ComplainInfo.CommentComplain;
import com.zti.kha.model.ComplainInfo.Complain;
import com.zti.kha.model.ComplainInfo.ComplainRate;

import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.ComplainInfo.Status;
import com.zti.kha.model.User.Group;
import com.zti.kha.model.User.GroupDisplay;
import com.zti.kha.model.User.Profile;
import com.zti.kha.model.User.ProfileDisplay;
import com.zti.kha.utility.ErrorFactory;
import com.zti.kha.utility.GcmSender;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by S on 9/22/2016.
 */
@RestController
public class ComplainApi extends CommonApi {

    public ComplainApi() throws Exception {
    }

    protected Pageable sortPage(int page, int sizeContents, int sort, int orderSort) {

        Pageable pageable = null;
        if (sort == 1 && orderSort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending());
        } else if (sort == 1 && orderSort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").descending());
        } else if (sort == 2 && orderSort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("complainId").ascending());
        } else if (sort == 2 && orderSort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("complainId").descending());
        } else if (sort == 3 && orderSort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("createDate").ascending());
        } else if (sort == 3 && orderSort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("createDate").descending());
        }
        return pageable;
    }
    @CrossOrigin
    @RequestMapping(value = "/testMail", method = RequestMethod.GET)
    public BaseResponse testMail(HttpServletRequest request
    ) throws PostExceptions, ParseException {

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MAIL_SENDER, MAIL_PASSWORD);
                    }
                });
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(MAIL_SENDER));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("wanlaya.c@zealtechinter.com"));
            message.setSubject("แจ้งเตือนการร้องเรียนใหม่จากระบบ myKHA");


            message.setContent("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<body>\n" +
                    "<p>เรียน เจ้าหน้าที่และผู้ที่เกี่ยวข้อง</p>\n" +
                    "<p>เราขอแจ้งให้ทราบว่าขณะนี้มีผู้ใช้งานร้องเรียน แจ้งเหตุผ่านระบบรับเรื่องร้องเรียน แจ้งเหตุ ผ่าน Website และ Mobile Application โดยมีรายละเอียดดังนี้</p>" +
                    "<p>ท่านสามารถอัปเดตสถานะการเนินการผ่าน Web Service ของระบบได้ที่ลิงค์ : " + URL_CMS + "login.html" + "</p>\n" +
                    "<p>หมายเหตุ : ข้อความนี้เป็นระบบอัตโนมัติ</p>" +
                    "</body>\n" +
                    "</html>", "text/html; charset=utf-8"
            );

            Transport.send(message);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            return getOk(new BaseResponse(OK,e.toString()));


        }



            return getOk(new BaseResponse(OK));
}

    @CrossOrigin
    @RequestMapping(value = "/addComplain", method = RequestMethod.POST)
    public BaseResponse addComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "titleId", defaultValue = "", required = true) @ApiParam(value = "categoryCode of categoryType complain") String titleId
            , @RequestParam(value = "description", defaultValue = "", required = true) String description
            , @RequestParam(value = "audioPath", required = false) MultipartFile audioPath
            , @RequestParam(value = "videoPath", required = false) MultipartFile videoPath
            , @RequestParam(value = "picturesPath", required = false) MultipartFile[] pictures
            , @RequestParam(value = "latitude", defaultValue = "", required = false) String latitude
            , @RequestParam(value = "longitude", defaultValue = "", required = false) String longitude
            , @RequestParam(value = "placeName", defaultValue = "") String placeName
            , @RequestParam(value = "provinceCode", defaultValue = "", required = false) String provinceCode
            , @RequestParam(value = "districtCode", defaultValue = "", required = false) String districtCode
            , @RequestParam(value = "subDistrictCode", defaultValue = "", required = false) String subDistrictCode
            , @RequestParam(value = "postalCode", defaultValue = "", required = false) String postalCode
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of sub category complain") String category
            , @RequestParam(value = "flgPrivate", defaultValue = "false", required = false) boolean flgPrivate
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId


    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Complain complain = new Complain();
        List<String> picturesList = new ArrayList<>();
        if (pictures != null && pictures.length > 0) {
            List<String> picture = picture(picturesList, pictures, profile);
            complain.setPicturesPath(picture);
        }

        if (videoPath != null) {
            complain.setVideoPath(uploadFile(videoPath, profile, FOLDER_VIDEO, FILE_TYPE_MP4));
        }
        if (audioPath != null) {
            complain.setAudioPath(uploadFile(audioPath, profile, FOLDER_AUDIO, FILE_TYPE_MP3));
        }
        complain.setProvinceCode(provinceCode);
        complain.setDistrictCode(districtCode);
        complain.setSubDistrictCode(subDistrictCode);
        complain.setPostalCode(postalCode);
        complain.setSequence(generateSequence());
        complain.setGroupId(groupId);
        complain.setComplainId(generateComplainId("C"));
        complain.setPlaceName(placeName);
        complain.setDescription(description);
        complain.setTitleId(titleId);
        complain.setFlgPrivate(flgPrivate);

        if (latitude.length() > 0) {
            complain.setLatitude(convertDigit(Double.parseDouble(latitude)));
        }

        if (longitude.length() > 0) {
            complain.setLongitude(convertDigit(Double.parseDouble(longitude)));
        }
        complain.setEditBy(profile.getId());
        complain.setAuthor(profile.getId());
        List<Status> statusComplains = complain.getStatusComplains();
        statusComplains.add(new Status(0, "", "", "", new ArrayList<>()));
        complain.setStatusComplains(statusComplains);
        complain.setCategory(category);

        Complain insert = complainRepository.insert(complain);

        insert.setProvinceName(getProvinceName(insert.getProvinceCode()));
        insert.setDistrictName(getDistrictName(insert.getDistrictCode()));
        insert.setSubDistrictName(getSubDistrictName(insert.getSubDistrictCode()));
        //getCateName
        insert.setCategoryName(getCateName(insert.getCategory()));
        insert.setTitleName(getCateName(insert.getTitleId()));
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                sendAdminComplainNotification(insert);
            }
        }).start();*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendEmailAdminComplain(insert);
            }
        }).start();


        return getOk(new BaseResponse(insert));
    }


    private int generateSequence() {
        Complain sequence = complainRepository.findTopByOrderByCreateDateDesc();
        return sequence != null ? sequence.getSequence() + 1 : 1;
    }

    private void sendEmailAdminComplain(Complain complain) {

        List<Profile> profileList = getAdminComplainNotification(complain);


        for (int i = 0; i < profileList.size(); i++) {
            if (profileList.get(i).getEmail() != null && !profileList.get(i).getEmail().equals("")) {


                String pic = "";
                for (int j = 0; j < complain.getPicturesPath().size(); j++) {
                    pic = pic + "<img src=" + URL_SERVER + "download?file=" + complain.getPicturesPath().get(j) + "\"width=\"512\">\n";
                }

                Properties props = new Properties();
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(MAIL_SENDER, MAIL_PASSWORD);
                            }
                        });
                Message message = new MimeMessage(session);
                try {
                    message.setFrom(new InternetAddress(MAIL_SENDER));

                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(profileList.get(i).getEmail()));
                    message.setSubject("แจ้งเตือนการร้องเรียนใหม่จากระบบ myKHA");


                    message.setContent("<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<body>\n" +
                            "<img src=" + URL_SERVER + "download?file=image/dd9349cb-8055-4aa5-9b53-1b16624d3e1a1626244261065.jpg\">\n\n" +
                            "<p>เรียน เจ้าหน้าที่และผู้ที่เกี่ยวข้อง</p>\n" +
                            "<p>เราขอแจ้งให้ทราบว่าขณะนี้มีผู้ใช้งานร้องเรียน แจ้งเหตุผ่านระบบรับเรื่องร้องเรียน แจ้งเหตุ ผ่าน Website และ Mobile Application โดยมีรายละเอียดดังนี้</p>" +
                            "<p>วันที่ร้องเรียน : " + convertDateToShortTH(complain.getCreateDate()) + " น.</p>" +
                            "<p>หมายเลขการร้องเรียน : " + complain.getComplainId() + "</p>" +
                            "<p>หัวข้อ : " + complain.getTitleName() + "</p>" +
                            "<p>รายละเอียด : " + complain.getDescription() + "</p>" +
                            "<p>สถานที่เกิดเหตุ : " + complain.getPlaceName() + "</p>" +
                            "<p>อำเภอ : " + complain.getDistrictName() + "</p>" +
                            "<p>จังหวัด : " + complain.getProvinceName() + "</p>\n"
                            + pic +
                            "<p>ท่านสามารถอัปเดตสถานะการเนินการผ่าน Web Service ของระบบได้ที่ลิงค์ : " + URL_CMS + "login.html" + "</p>\n" +
                            "<p>หมายเหตุ : ข้อความนี้เป็นระบบอัตโนมัติ</p>" +
                            "</body>\n" +
                            "</html>", "text/html; charset=utf-8"
                    );

                    Transport.send(message);
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private String generateComplainId(String title) {
        Complain topByOrder = complainRepository.findTopByOrderByCreateDateDesc();
        String value;
        String currentDate = convertDateComplainId(new Date());

        if (topByOrder != null) {
            String complainId = topByOrder.getComplainId();
            Date createDate = topByOrder.getCreateDate();
            String lastDate = convertDateComplainId(createDate);
            if (lastDate.equals(currentDate)) {
                int substring = Integer.parseInt(complainId.substring(complainId.length() - 5));
                value = title + currentDate + String.format("%05d", substring + 1);
            } else {
                value = title + currentDate + String.format("%05d", 1);
            }
        } else {
            value = title + currentDate + String.format("%05d", 1);
        }
        return value;
    }


    @CrossOrigin
    @ApiOperation(value = "ประวัติการร้องเรียน", notes = "", response = Complain.class)
    @RequestMapping(value = "historyComplain", method = RequestMethod.GET)
    public BaseResponse historyComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "currentStatus", defaultValue = "", required = false) @ApiParam(value = "0=waiting,1=in process,2=cancel,3=done,4=out of control,5=receive") String currentStatus
            , @RequestParam(value = "titleId", defaultValue = "", required = true) @ApiParam(value = "categoryCode of categoryType complain") String titleId
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of sub category complain") String category
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "sort", defaultValue = "3", required = false) @ApiParam(value = "1 = Sort by sequence,2 = Sort by complainId,3 = Sort by createDate") int sort
            , @RequestParam(value = "sizeContents", defaultValue = "30", required = false) int sizeContents) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Pageable pageable = sortPage(page, sizeContents, sort, order);

        Page<Complain> byId = null;
        if (id.length() > 0) {
            byId = complainRepository.findById(id, pageable);
            Complain complain = byId.getContent().get(0);

            if (!complain.getAuthor().equals(profile.getId())) {
                throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
            }

            List<Status> statusComplains = complain.getStatusComplains();

            for (int i = 0; i < statusComplains.size(); i++) {
                Optional<ProfileDisplay> authorProfile = profileRepository.findByIdIs(statusComplains.get(i).getEditBy());
                if (authorProfile.isPresent()) {
                    statusComplains.get(i).setEditByProfile(authorProfile.get());
                }
            }

        } else {
            Query query = new Query().with(pageable);
            query.addCriteria(Criteria.where("author").is(profile.getId()));
            if (groupId.size() > 0) {

                query.addCriteria(Criteria.where("groupId").in(groupId));
            }
            if (titleId.length() > 0) {
                query.addCriteria(Criteria.where("titleId").is(titleId));
            }
            if (sequence.length() > 0) {
                query.addCriteria(Criteria.where("sequence").is(Integer.parseInt(sequence)));
            }
            if (category.length() > 0) {
                query.addCriteria(Criteria.where("category").in(category));
            }
            if (currentStatus.length() > 0) {
                query.addCriteria(Criteria.where("currentStatus").is(Integer.parseInt(currentStatus)));
            }

            if (!startDate.equals("") && !endDate.equals("")) {
                query.addCriteria(Criteria.where("createDate").gte(new Date(Long.parseLong(startDate))).lt(new Date(Long.parseLong(endDate))));
            }
            Criteria description = Criteria.where("description").regex(keyWord, "i");

            Criteria complain = Criteria.where("complainId").regex(keyWord, "i");
            Criteria placeName = Criteria.where("placeName").regex(keyWord, "i");
            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(description, complain, placeName));
            }
            List<Complain> post = mongoTemplate.find(query, Complain.class);
            long count = mongoTemplate.count(query, Complain.class);
            byId = new PageImpl<Complain>(post, pageable, count);
        }

        for (int i = 0; i < byId.getContent().size(); i++) {
            Complain complain = byId.getContent().get(i);
            Optional<ProfileDisplay> authorProfile = profileRepository.findByIdIs(complain.getAuthor());
            if (authorProfile.isPresent() == true) {
                complain.setAuthorProfile(authorProfile.get());
            }
            complain.setProvinceName(getProvinceName(byId.getContent().get(i).getProvinceCode()));
            complain.setDistrictName(getDistrictName(byId.getContent().get(i).getDistrictCode()));
            complain.setSubDistrictName(getSubDistrictName(byId.getContent().get(i).getSubDistrictCode()));
            //getCateName
            complain.setCategoryName(getCateName(byId.getContent().get(i).getCategory()));
            complain.setTitleName(getCateName(byId.getContent().get(i).getTitleId()));
            if (complain.getStatusComplains().size() > 0) {
                Optional<ProfileDisplay> byId1 = profileRepository.findByIdIs(complain.getStatusComplains().get(complain.getStatusComplains().size() - 1).getEditBy());
                if (byId1.isPresent()) {
                    complain.getStatusComplains().get(complain.getStatusComplains().size() - 1).setEditByProfile(byId1.get());
                }
            }
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(complain.getGroupId());
            if (group!=null){
                complain.setGroupProfile(group);
            }

        }

        return getOk(new BaseResponse(byId));

    }

    @CrossOrigin
    @ApiOperation(value = "รายการร้องเรียน", notes = "", response = Complain.class)
    @RequestMapping(value = "inboxComplain", method = RequestMethod.GET)
    public BaseResponse inboxComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "currentStatus", defaultValue = "", required = false) @ApiParam(value = "0=waiting,1=in process,2=cancel,3=done,4=out of control,5=receive") String currentStatus
            , @RequestParam(value = "titleId", defaultValue = "", required = true) @ApiParam(value = "categoryCode of categoryType complain") String titleId
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of sub category complain") String category
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "provinceCode", defaultValue = "", required = false) String provinceCode
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "lastDays", defaultValue = "0", required = false) int lastDays
            , @RequestParam(value = "sort", defaultValue = "3", required = false) @ApiParam(value = "1 = Sort by sequence,2 = Sort by complainId,3 = Sort by createDate") int sort
            , @RequestParam(value = "sizeContents", defaultValue = "30", required = false) int sizeContents
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId

    ) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Pageable pageable = sortPage(page, sizeContents, sort, order);
        if (groupId.size() == 0) {
            checkSuperAdmin(profile);
        } else {
            checkAdminComplain(profile, groupId);
        }
        Page<Complain> byId = null;
        if (id.length() > 0) {
            byId = complainRepository.findById(id, pageable);
            Complain complain = byId.getContent().get(0);

            List<Status> statusComplains = complain.getStatusComplains();
            for (int i = 0; i < statusComplains.size(); i++) {
                Optional<ProfileDisplay> authorProfile = profileRepository.findByIdIs(statusComplains.get(i).getEditBy());
                if (authorProfile.isPresent()) {
                    statusComplains.get(i).setEditByProfile(authorProfile.get());
                }
            }
            complain.setStatusComplains(statusComplains);

        } else {
            Query query = new Query().with(pageable);

            if (groupId.size() > 0) {
                query.addCriteria(Criteria.where("groupId").in(groupId));

            }
            if (sequence.length() > 0) {
                query.addCriteria(Criteria.where("sequence").is(Integer.parseInt(sequence)));
            }
            if (category.length() > 0) {
                query.addCriteria(Criteria.where("category").in(category));
            }
            if (currentStatus.length() > 0) {
                query.addCriteria(Criteria.where("currentStatus").is(Integer.parseInt(currentStatus)));
            }
            if (titleId.length() > 0) {
                query.addCriteria(Criteria.where("titleId").is(titleId));
            }
            if (provinceCode.length() > 0) {
                query.addCriteria(Criteria.where("provinceCode").is(provinceCode));
            }
            if (keyWord.length() > 0) {
                query.addCriteria(Criteria.where("complainId").regex(keyWord, "i"));
            }
            if (!startDate.equals("") && !endDate.equals("")) {
                query.addCriteria(Criteria.where("createDate").gte(new Date(Long.parseLong(startDate))).lt(new Date(Long.parseLong(endDate))));
            }
            if (startDate.equals("") && endDate.equals("") && lastDays > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -lastDays);
                Date dateStart = calendar.getTime();
                query.addCriteria(Criteria.where("createDate").gte(dateStart));
            }

            List<Complain> post = mongoTemplate.find(query, Complain.class);
            long count = mongoTemplate.count(query, Complain.class);
            byId = new PageImpl<Complain>(post, pageable, count);
        }
        for (int i = 0; i < byId.getContent().size(); i++) {
            Complain complain = byId.getContent().get(i);
            Optional<ProfileDisplay> authorProfile = profileRepository.findByIdIs(complain.getAuthor());
            if (authorProfile.isPresent()) {
                byId.getContent().get(i).setAuthorProfile(authorProfile.get());
            }
            complain.setProvinceName(getProvinceName(byId.getContent().get(i).getProvinceCode()));
            complain.setDistrictName(getDistrictName(byId.getContent().get(i).getDistrictCode()));
            complain.setSubDistrictName(getSubDistrictName(byId.getContent().get(i).getSubDistrictCode()));
            //getCateName
            complain.setCategoryName(getCateName(byId.getContent().get(i).getCategory()));
            complain.setTitleName(getCateName(byId.getContent().get(i).getTitleId()));
            if (complain.getStatusComplains().size() > 0) {
                Optional<ProfileDisplay> byId1 = profileRepository.findByIdIs(complain.getStatusComplains().get(complain.getStatusComplains().size() - 1).getEditBy());
                if (byId1.isPresent()) {
                    complain.getStatusComplains().get(complain.getStatusComplains().size() - 1).setEditByProfile(byId1.get());
                }
            }
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(complain.getGroupId());
            if (group!=null){
                complain.setGroupProfile(group);
            }
        }

        return getOk(new BaseResponse(byId));
    }


    @CrossOrigin
    @RequestMapping(value = "/editComplain", method = RequestMethod.POST)
    public BaseResponse editComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "titleId", defaultValue = "", required = true) @ApiParam(value = "categoryCode of categoryType complain") String titleId
            , @RequestParam(value = "id", defaultValue = "", required = true) String id
            , @RequestParam(value = "description", defaultValue = "", required = true) String description
            , @RequestParam(value = "audioPath", required = false) MultipartFile audioPath
            , @RequestParam(value = "videoPath", required = false) MultipartFile videoPath
            , @RequestParam(value = "picturesPath", required = false) MultipartFile[] pictures
            , @RequestParam(value = "latitude", defaultValue = "", required = false) String latitude
            , @RequestParam(value = "longitude", defaultValue = "", required = false) String longitude
            , @RequestParam(value = "placeName", defaultValue = "") String placeName
            , @RequestParam(value = "provinceCode", defaultValue = "") String provinceCode
            , @RequestParam(value = "districtCode", defaultValue = "") String districtCode
            , @RequestParam(value = "subDistrictCode", defaultValue = "", required = false) String subDistrictCode
            , @RequestParam(value = "postalCode", defaultValue = "", required = false) String postalCode
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of sub category complain") String category
            , @RequestParam(value = "deletePictures", required = false) List<String> deletePictures
            , @RequestParam(value = "flgPrivate", defaultValue = "false", required = false) boolean flgPrivate

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);

        Optional<Complain> byId = complainRepository.findById(id);
        Complain complain = byId.get();


        if (!complain.getAuthor().equals(profile.getId())) {
            List<String>check = new ArrayList<>();
            check.add( complain.getGroupId());
            checkAdminComplain(profile,check);

        }else if (complain.getAuthor().equals(profile.getId())&&profile.getRole()!=null) {
            List<String>check = new ArrayList<>();
            check.add( complain.getGroupId());
            checkAdminComplain(profile,check);
        }else {


            if (complain.getCurrentStatus() != 0) {
                throw new PostExceptions(FAILED, localizeText.getNoUpdate());
            }
        }
        List<String> picturesList = complain.getPicturesPath();

        if (pictures != null && pictures.length > 0) {
            List<String> picture = picture(picturesList, pictures, profile);
            complain.setPicturesPath(picture);
        }

        if (videoPath != null) {
            complain.setVideoPath(uploadFile(videoPath, profile, FOLDER_VIDEO, FILE_TYPE_MP4));
        }
        if (audioPath != null) {
            complain.setAudioPath(uploadFile(audioPath, profile, FOLDER_AUDIO, FILE_TYPE_MP3));
        }
        complain.setTitleId(titleId);

        complain.setProvinceCode(provinceCode);
        complain.setDistrictCode(districtCode);
        complain.setSubDistrictCode(subDistrictCode);
        complain.setPostalCode(postalCode);
        complain.setFlgPrivate(flgPrivate);
        complain.setSequence(generateSequence());
        complain.setPlaceName(placeName);
        complain.setDescription(description);
        complain.setLatitude(latitude);
        complain.setCategory(category);
        complain.setLongitude(longitude);
        if (deletePictures != null && deletePictures.size() > 0) {
            picturesList = deletePicture(deletePictures, picturesList);
            complain.setPicturesPath(picturesList);
        }
        complain.setUpdateDate(new Date());
        complain.setEditBy(profile.getId());
        Complain save = complainRepository.save(complain);
        save.setProvinceName(getProvinceName(save.getProvinceCode()));
        save.setDistrictName(getDistrictName(save.getDistrictCode()));
        save.setSubDistrictName(getSubDistrictName(save.getSubDistrictCode()));
        //getCateName
        complain.setCategoryName(getCateName(save.getCategory()));
        complain.setTitleName(getCateName(save.getTitleId()));
        return getOk(new BaseResponse(save));
    }

    @CrossOrigin
    @RequestMapping(value = "/adminManageStatusComplain", method = RequestMethod.POST)
    public BaseResponse adminManageStatusComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id
            , @RequestParam(value = "statusCode", defaultValue = "", required = true) @ApiParam(value = "0=waiting,1=in process,2=cancel,3=done,4=out of control,5=receive") int statusCode
            , @RequestParam(value = "remark", defaultValue = "", required = false) String remark
            , @RequestParam(value = "pushnotification", defaultValue = "false") Boolean pushnotification
            , @RequestParam(value = "filesPath", required = false) MultipartFile filesPath
            , @RequestParam(value = "picturePath", required = false) MultipartFile[] pictures

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);

        Optional<Complain> byId = complainRepository.findById(id);
        Complain complain = byId.get();
        List<String>check = new ArrayList<>();
        check.add( complain.getGroupId());
        checkAdminComplain(profile,check);
        List<String> picturesList = new ArrayList<>();
        String file = "";
        if (filesPath != null) {
            file = pdf(filesPath, profile).get(0);
        }
        if (pictures != null && pictures.length > 0) {
            picturesList = picture(picturesList, pictures, profile);
        }
        List<Status> statusComplains = complain.getStatusComplains();
        statusComplains.add(new Status(statusCode, profile.getId(), remark, file, picturesList));
        complain.setStatusComplains(statusComplains);
        complain.setCurrentStatus(statusCode);
        complain.setUpdateDate(new Date());
        complain.setEditBy(profile.getId());
        Complain save = complainRepository.save(complain);
        if (pushnotification == true) {
            Optional<Profile> byUserName = profileRepository.findById(save.getAuthor());
            if (byUserName.isPresent() && !byUserName.get().getGcmToken().equals("")) {
                GcmSender.sendInformComplain(save.getId(), TYPE_NOTI_USER, byUserName.get().getGcmToken(), save.getCurrentStatus(), GCM_KEY, false,complain.getGroupId(),save.getComplainId());
            }
        }
        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
    }


    @CrossOrigin
    @RequestMapping(value = "/cancelComplain", method = RequestMethod.POST)
    public BaseResponse cancelComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id
            , @RequestParam(value = "remark", defaultValue = "", required = false) String remark
    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Optional<Complain> byIdIn = complainRepository.findById(id);

        if (!profile.getId().equals(byIdIn.get().getAuthor())) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
        if (byIdIn.get().getCurrentStatus() != 0) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
        List<Status> statusComplains = byIdIn.get().getStatusComplains();
        statusComplains.add(new Status(2, profile.getId(), remark, "", new ArrayList<>()));
        byIdIn.get().setStatusComplains(statusComplains);
        byIdIn.get().setCurrentStatus(2);
        byIdIn.get().setUpdateDate(new Date());
        byIdIn.get().setEditBy(profile.getId());
        Complain save = complainRepository.save(byIdIn.get());
        return getOk(new BaseResponse(save));
    }

    @CrossOrigin
    @RequestMapping(value = "/deleteComplain", method = RequestMethod.DELETE)
    public BaseResponse deleteComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) List<String> id) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        for (int i = 0; i < id.size(); i++) {
            Optional<Complain> byIdIn = complainRepository.findById(id.get(i));
            List<String>check = new ArrayList<>();
            check.add( byIdIn.get().getGroupId());
            checkAdminComplain(profile,check);
//            if (byIdIn.get().getCurrentStatus() != 0) {
//                throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
//            }
            complainRepository.delete(byIdIn.get());
        }
        return getOk(new BaseResponse(OK, localizeText.getDeleted()));
    }

    @CrossOrigin
    @RequestMapping(value = "/checkComplainRate", method = RequestMethod.GET)
    public BaseResponse checkComplainRate(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Query query = new Query();
        query.addCriteria(Criteria.where("author").is(profile.getId()));
        query.addCriteria(Criteria.where("currentStatus").is(3));
        query.addCriteria(Criteria.where("rate").is(null));

        List<Complain> post = mongoTemplate.find(query, Complain.class);
        return getOk(new BaseResponse(post));
    }

    @CrossOrigin
    @RequestMapping(value = "/addComplainRate", method = RequestMethod.POST)
    public BaseResponse addComplainRate(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "") String id
            , @RequestParam(value = "rateUpdate") @ApiParam(value = "1=very bad,2=bad,3=good,4=very good,5=excellence") int rateUpdate
            , @RequestParam(value = "commentUpdate", defaultValue = "", required = false) String commentUpdate
            , @RequestParam(value = "rateDuration") @ApiParam(value = "1=very bad,2=bad,3=good,4=very good,5=excellence") int rateDuration
            , @RequestParam(value = "commentDuration", defaultValue = "", required = false) String commentDuration
    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Optional<Complain> byIdIn = complainRepository.findById(id);

        if (!profile.getId().equals(byIdIn.get().getAuthor())) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
        if (byIdIn.get().getCurrentStatus() == 3) {

            ComplainRate complainRate = new ComplainRate();
            complainRate.setCommentUpdate(commentUpdate);
            complainRate.setRateUpdate(rateUpdate);
            complainRate.setCommentDuration(commentDuration);
            complainRate.setRateDuration(rateDuration);
            byIdIn.get().setRate(complainRate);
            Complain save = complainRepository.save(byIdIn.get());
            return getOk(new BaseResponse(save));
        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getFailed()));
        }
    }

    @CrossOrigin
    @ApiOperation(value = "คอมเม้น", notes = "", response = CommentComplain.class)
    @RequestMapping(value = "/commentComplain", method = RequestMethod.POST)
    public BaseResponse commentComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "complainId", defaultValue = "") String complainId
            , @RequestParam(value = "picturesPath", required = false) MultipartFile[] pictures
            , @RequestParam(value = "description", defaultValue = "") String description) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);

        Optional<Complain> post = complainRepository.findByComplainId(complainId);
        if (post.isPresent() == true) {
            checkRoleCommentComplain(profile, post.get());

            post.get().setComment(post.get().getComment() + 1);
            complainRepository.save(post.get());


            CommentComplain comment = new CommentComplain();
            comment.setUserId(profile.getId());
            comment.setDescription(description);
            comment.setEditBy(profile.getId());
            comment.setComplainId(complainId);
            List<String> picturesList = new ArrayList<>();
            if (pictures != null && pictures.length > 0) {
                picturesList = picture(picturesList, pictures, profile);
            }
            comment.setPicturesPath(picturesList);
            CommentComplain insert = commentComplainRepository.insert(comment);

            Optional<Profile> byUserName = profileRepository.findById(post.get().getAuthor());
            if (byUserName.isPresent()&& byUserName.get().getGcmToken()!=null && !byUserName.get().getGcmToken().equals("")) {
                GcmSender.sendCommentComplain(insert.getId(), insert.getComplainId(), TYPE_NOTI_COMMENT, byUserName.get().getGcmToken(), GCM_KEY, insert.getDescription(),post.get().getGroupId());
            }

            return getOk(new BaseResponse(comment));
        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/deleteCommentComplain", method = RequestMethod.POST)
    public BaseResponse deleteCommentComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "") String id) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Optional<CommentComplain> comment = commentComplainRepository.findById(id);
        if (comment.isPresent() == true) {

            Optional<Complain> post = complainRepository.findById(comment.get().getComplainId());
            if (post.isPresent() == true && post.get().getComment() > 0) {
                checkRoleCommentComplain(profile, post.get());

                post.get().setComment(post.get().getComment() - 1);
                complainRepository.save(post.get());
            }
            commentComplainRepository.delete(comment.get());
            return getOk(new BaseResponse(OK, localizeText.getDeleted()));
        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/getCommentComplain", method = RequestMethod.GET)
    public BaseResponse getCommentComplain(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "complainId", defaultValue = "", required = false) String complainId
            , @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "endDate", defaultValue = "", required = false) String endDate
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "30", required = false) int sizeContents) throws PostExceptions {
        initialize(request);
        userValidateToken(token, request);

        Pageable pageable = PageRequest.of(page, sizeContents, Sort.by("createDate").descending());

        Query query = new Query().with(pageable);

        Criteria criteria_createDate = null;
        Page<CommentComplain> byContent;
        if (id.length() > 0) {
            byContent = commentComplainRepository.findByIdIs(id, pageable);
        } else {
            if (complainId.length() > 0) {
                query.addCriteria(Criteria.where("complainId").is(complainId));
            }

            if (enable.equals("1")) {
                query.addCriteria(Criteria.where("enable").is(true));
            } else if (enable.equals("2")) {
                query.addCriteria(Criteria.where("enable").is(false));
            }

            if (!startDate.equals("") && !endDate.equals("")) {
                Date date_startDate = new Date(Long.parseLong(startDate));
                Date date_endDate = new Date(Long.parseLong(endDate));
                criteria_createDate = Criteria.where("createDate").gte(date_startDate).lte(date_endDate);
                query.addCriteria(criteria_createDate);
            }
            List<CommentComplain> comments = mongoTemplate.find(query, CommentComplain.class);
            long count = mongoTemplate.count(query, CommentComplain.class);
            byContent = new PageImpl<CommentComplain>(comments, pageable, count);
        }
        for (int i = 0; i < byContent.getContent().size(); i++) {
            Optional<ProfileDisplay> authorProfile = profileRepository.findByIdIs(byContent.getContent().get(i).getUserId());
            if (authorProfile.isPresent() == true) {

                byContent.getContent().get(i).setAuthorProfile(authorProfile.get());
            }
        }

        return getOk(new BaseResponse(byContent));
    }


}