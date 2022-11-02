package com.zti.kha.api;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import com.zti.kha.model.*;
import com.zti.kha.model.Content.*;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.Content.Noti.DeleteNotification;
import com.zti.kha.model.Content.Noti.Notifications;
import com.zti.kha.model.Statistic.ViewStatistic;
import com.zti.kha.model.User.GroupDisplay;
import com.zti.kha.model.User.Profile;
import com.zti.kha.model.User.ProfileDisplay;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Windows 8.1 on 10/10/2560.
 */
@RestController
public class ContentApi extends CommonApi {

    public ContentApi() throws Exception {
    }

    @CrossOrigin
    @RequestMapping(value = "/addEditBanner", method = RequestMethod.POST)
    public BaseResponse addEditBanner(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "imageBanner", required = false) MultipartFile imageBanner
            , @RequestParam(value = "url", defaultValue = "", required = false) String url
            , @RequestParam(value = "pin", defaultValue = "0") @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "startDate", defaultValue = "", required = true) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = true) @ApiParam(value = "Time in milliseconds") String endDate

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Banner banner = new Banner();
        if (id.length() > 0) {
            banner = bannerRepository.findById(id).get();
            checkSuperAdminGroups(profile, banner.getGroupId());
        } else {
            checkSuperAdminGroups(profile, groupId);
        }
        if (imageBanner != null) {
            banner.setImageBanner(thumbnail(imageBanner, profile, FOLDER_BANNER));
        }
        if (sequence == 0) {
            banner.setSequence(9999);
        } else {
            banner.setSequence(sequence);
        }
        banner.setGroupId(groupId);
        banner.setEnable(enable);
        banner.setUrl(url);
        banner.setPin(Integer.parseInt(pin));
        if (startDate.length() > 0) {
            if (startDate.equals("null")) {
                banner.setStartDate(null);
            } else {
                banner.setStartDate(new Date(Long.parseLong(startDate)));
            }
        }
        if (endDate.length() > 0) {
            if (endDate.equals("null")) {
                banner.setEndDate(null);
            } else {
                banner.setEndDate(new Date(Long.parseLong(endDate)));
            }
        }
        if (id.length() > 0) {
            banner.setUpdateDate(new Date());
            banner.setEditBy(profile.getUserName());
            bannerRepository.save(banner);
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), banner));
        } else {
            banner.setAuthor(profile.getUserName());
            banner.setEditBy(profile.getUserName());
            bannerRepository.insert(banner);
            return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), banner));
        }
    }

    @CrossOrigin
    @ApiOperation(value = "แบนเนอร์และโฆษณา", notes = "", response = Banner.class)
    @RequestMapping(value = "/getBanner", method = RequestMethod.GET)
    public BaseResponse getBanner(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "author", defaultValue = "", required = false) String authorName
            , @RequestParam(value = "pin", defaultValue = "", required = false) @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by pin,2 = Sort by top,3 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "isAdmin", defaultValue = "false", required = false) boolean isAdmin
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "orderSequence", defaultValue = "true", required = false) boolean orderSequence

    ) throws PostExceptions {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Pageable pageable = sortPage(page, sizeContents, sort, false,order,orderSequence);
        Page<Banner> byId = null;
        if (id.length() > 0) {
            byId = bannerRepository.findById(id, pageable);

        } else {
            Query query = new Query().with(pageable);
            if (authorName.length() > 0) {
                query.addCriteria(Criteria.where("author").is(authorName));
            }
            if (groupId.size() > 0) {
                query.addCriteria(Criteria.where("groupId").in(groupId));
            }

            if (pin.length() > 0) {
                int mPin = Integer.parseInt(pin);
                query.addCriteria(Criteria.where("pin").is(mPin));
            }
            if (enable.equals("1")) {
                query.addCriteria(Criteria.where("enable").is(true));
            } else if (enable.equals("2")) {
                query.addCriteria(Criteria.where("enable").is(false));
            }
            if (sequence.length() > 0) {
                query.addCriteria(Criteria.where("sequence").is(Integer.parseInt(sequence)));
            }

            if (startDate.length() > 0 && endDate.length() > 0) {
                query.addCriteria(Criteria.where("startDate").gte(new Date(Long.parseLong(startDate))));
                query.addCriteria(Criteria.where("endDate").lte(new Date(Long.parseLong(endDate))));
            } else {
                if (isAdmin == false) {
                    query.addCriteria(Criteria.where("startDate").lte(new Date()));
                    query.addCriteria(Criteria.where("endDate").gte(new Date()));
                }
            }

            Criteria title = Criteria.where("url").regex(keyWord, "i");
            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(title));
            }
            List<Banner> post = mongoTemplate.find(query, Banner.class);
            long count = mongoTemplate.count(query, Banner.class);
            byId = new PageImpl<Banner>(post, pageable, count);
        }
        for (int i = 0; i < byId.getContent().size(); i++) {
            ProfileDisplay authorProfile = profileRepository.findByUserName(byId.getContent().get(i).getAuthor());
            if (authorProfile != null) {
                byId.getContent().get(i).setAuthorProfile(authorProfile);
            }
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(byId.getContent().get(i).getGroupId());
            if (group!=null){
                byId.getContent().get(i).setGroupProfile(group);
            }
        }
        return getOk(new BaseResponse(byId));
    }


    @CrossOrigin
    @RequestMapping(value = "/addEditNews", method = RequestMethod.POST)
    public BaseResponse addEditNews(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "thumbnails", required = false) MultipartFile thumbnailFile
            , @RequestParam(value = "picturesPath", required = false) MultipartFile[] pictures
            , @RequestParam(value = "title", defaultValue = "", required = false) String title
            , @RequestParam(value = "description", defaultValue = "", required = false) String description
            , @RequestParam(value = "deletePictures", required = false) @ApiParam(value = "List name of picture") List<String> deletePictures
            , @RequestParam(value = "pin", defaultValue = "0") @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "pushnotification", defaultValue = "false") Boolean pushnotification
            , @RequestParam(value = "youtube", defaultValue = "", required = false) String youtube
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "buttonName", defaultValue = "", required = false) String buttonName
            , @RequestParam(value = "buttonUrl", defaultValue = "", required = false) String buttonUrl
            , @RequestParam(value = "facebookLive", defaultValue = "", required = false) String facebookLive
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: news") List<String> category
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "videoPath", required = false) MultipartFile videoPath
            , @RequestParam(value = "filesPath", required = false) MultipartFile filesPath
            , @RequestParam(value = "audioPath", required = false) MultipartFile audioPath

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        News newsEvent = new News();
        List<String> picturesList = new ArrayList<>();
        if (id.length() > 0) {
            Optional<News> byId = newsRepository.findById(id);
            newsEvent = byId.get();
            checkSuperAdminGroups(profile, newsEvent.getGroupId());
            picturesList = newsEvent.getPicturesPath();
        } else {
            checkSuperAdminGroups(profile, groupId);
        }
        if (pictures != null && pictures.length > 0) {
            List<String> picture = picture(picturesList, pictures, profile);
            newsEvent.setPicturesPath(picture);
        }
        if (thumbnailFile != null) {
            newsEvent.setThumbnailsPath(thumbnail(thumbnailFile, profile, FOLDER_COVER));
        }
        if (sequence == 0) {
            newsEvent.setSequence(9999);
        } else {
            newsEvent.setSequence(sequence);
        }
        if (filesPath != null) {
            newsEvent.setFilesPath(pdf(filesPath, profile).get(0));
        }

        if (videoPath != null) {
            newsEvent.setVideoPath(uploadFile(videoPath, profile, FOLDER_VIDEO, FILE_TYPE_MP4));
        }

        if (audioPath != null) {
            newsEvent.setAudioPath(uploadFile(audioPath, profile, FOLDER_AUDIO, FILE_TYPE_MP3));
        }
        newsEvent.setGroupId(groupId);
        newsEvent.setCategory(category);
        newsEvent.setTitle(title);
        newsEvent.setEnable(enable);
        newsEvent.setYoutube(youtube);
        newsEvent.setDescription(description);
        newsEvent.setPin(Integer.parseInt(pin));
        newsEvent.setButtonName(buttonName);
        newsEvent.setButtonUrl(buttonUrl);
        newsEvent.setPushnotification(pushnotification);
        newsEvent.setFacebookLive(facebookLive);
        if (startDate.length() > 0) {
            if (startDate.equals("null")) {
                newsEvent.setStartDate(null);
            } else {
                newsEvent.setStartDate(new Date(Long.parseLong(startDate)));
            }
        }
        if (endDate.length() > 0) {
            if (endDate.equals("null")) {
                newsEvent.setEndDate(null);
            } else {
                newsEvent.setEndDate(new Date(Long.parseLong(endDate)));
            }
        }
        if (id.length() > 0) {
            if (deletePictures != null && deletePictures.size() > 0) {
                picturesList = deletePicture(deletePictures, picturesList);
                newsEvent.setPicturesPath(picturesList);
            }
            newsEvent.setUpdateDate(new Date());
            newsEvent.setEditBy(profile.getUserName());
            News save = newsRepository.save(newsEvent);
            if (save.isPushnotification() == true && save.isPushAlready() == false) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendPushNotification(TYPE_NEWS, save.getId(), save.getTitle(), save.getGroupId(),TOPIC);
                        } catch (PostExceptions postExceptions) {
                            postExceptions.printStackTrace();
                        }
                    }
                }).start();
            }
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), newsEvent));
        } else {

            newsEvent.setAuthor(profile.getUserName());
            newsEvent.setEditBy(profile.getUserName());
            News save = newsRepository.insert(newsEvent);

            if (save.isPushnotification() == true && save.isPushAlready() == false) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendPushNotification(TYPE_NEWS, save.getId(), save.getTitle(), save.getGroupId(),TOPIC);
                        } catch (PostExceptions postExceptions) {
                            postExceptions.printStackTrace();
                        }
                    }
                }).start();
            }

            return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), newsEvent));
        }
    }

    @CrossOrigin
    @ApiOperation(value = "ข่าวประชาสัมพันธ์", notes = "", response = News.class)
    @RequestMapping(value = "/getNews", method = RequestMethod.GET)
    public BaseResponse getNews(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "author", defaultValue = "", required = false) String authorName
            , @RequestParam(value = "pin", defaultValue = "", required = false) @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by pin,2 = Sort by top,3 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "groupId", defaultValue = "", required = false)  List<String> groupId
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: news") String category
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "orderSequence", defaultValue = "true", required = false) boolean orderSequence

    ) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);

        List<News> byPin = newsRepository.findByPin(1);
        for (int i = 0; i < byPin.size(); i++) {
            Date dateNow = new Date();
            boolean b = dateNow.after(byPin.get(i).getStartDate()) && dateNow.before(byPin.get(i).getEndDate());
            if (b == false) {
                byPin.get(i).setPin(0);
                newsRepository.save(byPin.get(i));
            }
        }

        Pageable pageable = sortPage(page, sizeContents, sort, false,order,orderSequence);
        Page<News> byId = null;
        if (id.length() > 0) {
            byId = newsRepository.findById(id, pageable);

            byId.getContent().get(0).setCntView(byId.getContent().get(0).getCntView()+1);
            newsRepository.save(byId.getContent().get(0));
        } else {
            Query query = new Query().with(pageable);
            queryBase(query, pin, sequence, startDate, endDate, authorName, groupId, category, enable);

            Criteria title = Criteria.where("title").regex(keyWord, "i");
            Criteria description = Criteria.where("description").regex(keyWord, "i");
            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(title, description));
            }
            List<News> post = mongoTemplate.find(query, News.class);
            long count = mongoTemplate.count(query, News.class);
            byId = new PageImpl<News>(post, pageable, count);
        }

        for (int i = 0; i < byId.getContent().size(); i++) {
            ProfileDisplay authorProfile = profileRepository.findByUserName(byId.getContent().get(i).getAuthor());
            if (authorProfile != null) {
                byId.getContent().get(i).setAuthorProfile(authorProfile);
            }
            //getCateName
            byId.getContent().get(i).setCategoryProfile(getCateNameList(byId.getContent().get(i).getCategory()));
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(byId.getContent().get(i).getGroupId());
            if (group!=null){
                byId.getContent().get(i).setGroupProfile(group);
            }
        }
        return getOk(new BaseResponse(byId));
    }

    @CrossOrigin
    @RequestMapping(value = "/addEditEvent", method = RequestMethod.POST)
    public BaseResponse addEditEvent(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "thumbnails", required = false) MultipartFile thumbnailFile
            , @RequestParam(value = "title", defaultValue = "", required = false) String title
            , @RequestParam(value = "description", defaultValue = "", required = false) String description
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "placeOfEvent", defaultValue = "", required = false) String placeOfEvent
            , @RequestParam(value = "picturesPath", required = false) MultipartFile[] pictures
            , @RequestParam(value = "deletePictures", required = false) @ApiParam(value = "List name of picture") List<String> deletePictures
            , @RequestParam(value = "pin", defaultValue = "0") @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "youtube", defaultValue = "", required = false) String youtube
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "file", required = false) MultipartFile file
            , @RequestParam(value = "buttonName", defaultValue = "", required = false) String buttonName
            , @RequestParam(value = "buttonUrl", defaultValue = "", required = false) String buttonUrl
            , @RequestParam(value = "facebookLive", defaultValue = "", required = false) String facebookLive
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: event") List<String> category
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "latitude", defaultValue = "0", required = false) double latitude
            , @RequestParam(value = "longitude", defaultValue = "0", required = false) double longitude
    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);

        Event newsEvent = new Event();
        List<String> picturesList = new ArrayList<>();
        if (id.length() > 0) {
            Optional<Event> byId = eventRepository.findById(id);
            newsEvent = byId.get();
            checkSuperAdminGroups(profile, newsEvent.getGroupId());

            picturesList = newsEvent.getPicturesPath();
        } else {
            checkSuperAdminGroups(profile, groupId);
        }

        if (pictures != null && pictures.length > 0) {
            List<String> picture = picture(picturesList, pictures, profile);
            newsEvent.setPicturesPath(picture);
        }
        if (thumbnailFile != null) {
            newsEvent.setThumbnailsPath(thumbnail(thumbnailFile, profile, FOLDER_COVER));
        }
        if (sequence == 0) {
            newsEvent.setSequence(9999);
        } else {
            newsEvent.setSequence(sequence);
        }

        if (file != null) {
            newsEvent.setFilesPath(pdf(file, profile).get(0));
        }
        newsEvent.setLatitude(latitude);
        newsEvent.setLongitude(longitude);
        newsEvent.setGroupId(groupId);
        newsEvent.setCategory(category);
        newsEvent.setYoutube(youtube);
        newsEvent.setTitle(title);
        newsEvent.setDescription(description);
        newsEvent.setButtonName(buttonName);
        newsEvent.setEnable(enable);
        newsEvent.setButtonUrl(buttonUrl);
        newsEvent.setFacebookLive(facebookLive);
        if (startDate.length() > 0) {
            if (startDate.equals("null")) {
                newsEvent.setStartDate(null);

            } else {
                newsEvent.setStartDate(new Date(Long.parseLong(startDate)));
            }
        }
        if (endDate.length() > 0) {
            if (endDate.equals("null")) {
                newsEvent.setEndDate(null);
            } else {
                newsEvent.setEndDate(new Date(Long.parseLong(endDate)));
            }
        }
        newsEvent.setPlaceOfEvent(placeOfEvent);
        newsEvent.setPin(Integer.parseInt(pin));
        if (id.length() > 0) {
            if (deletePictures != null && deletePictures.size() > 0) {
                picturesList = deletePicture(deletePictures, picturesList);
                newsEvent.setPicturesPath(picturesList);
            }
            newsEvent.setUpdateDate(new Date());
            newsEvent.setEditBy(profile.getUserName());
            Event save = eventRepository.save(newsEvent);

            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), newsEvent));
        } else {

            newsEvent.setAuthor(profile.getUserName());
            newsEvent.setEditBy(profile.getUserName());
            Event save = eventRepository.insert(newsEvent);

        }
        return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), newsEvent));
    }

    @CrossOrigin
    @ApiOperation(value = "ปฏิธินกิจกรรม", notes = "", response = Event.class)
    @RequestMapping(value = "/getEvent", method = RequestMethod.GET)
    public BaseResponse getEvent(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "author", defaultValue = "", required = false) String authorName
            , @RequestParam(value = "pin", defaultValue = "", required = false) @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by pin,2 = Sort by top,3 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: event") String category
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "orderSequence", defaultValue = "true", required = false) boolean orderSequence

    ) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Pageable pageable = sortPage(page, sizeContents, sort, false,order,orderSequence);

        Page<Event> byId = null;
        if (id.length() > 0) {
            byId = eventRepository.findById(id, pageable);

            byId.getContent().get(0).setCntView(byId.getContent().get(0).getCntView()+1);
            eventRepository.save(byId.getContent().get(0));

        } else {
            Query query = new Query().with(pageable);
            queryBase(query, pin, sequence, startDate, endDate, authorName, groupId, category, enable);

            Criteria title = Criteria.where("title").regex(keyWord, "i");
            Criteria description = Criteria.where("description").regex(keyWord, "i");
            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(title, description));
            }
            List<Event> post = mongoTemplate.find(query, Event.class);
            long count = mongoTemplate.count(query, Event.class);
            byId = new PageImpl<Event>(post, pageable, count);
        }

        for (int i = 0; i < byId.getContent().size(); i++) {
            ProfileDisplay authorProfile = profileRepository.findByUserName(byId.getContent().get(i).getAuthor());
            if (authorProfile != null) {
                byId.getContent().get(i).setAuthorProfile(authorProfile);
            }
            if (byId.getContent().get(i).getStartDate() != null && byId.getContent().get(i).getEndDate() != null) {
                List<Date> datesBetweenUsingJava7 = getDatesBetweenUsingJava7(byId.getContent().get(i).getStartDate(), byId.getContent().get(i).getEndDate());
                datesBetweenUsingJava7.add(byId.getContent().get(i).getEndDate());
                byId.getContent().get(i).setDuration(datesBetweenUsingJava7);
            }
            //getCateName
            byId.getContent().get(i).setCategoryProfile(getCateNameList(byId.getContent().get(i).getCategory()));
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(byId.getContent().get(i).getGroupId());
            if (group!=null){
                byId.getContent().get(i).setGroupProfile(group);
            }
        }
        return getOk(new BaseResponse(byId));
    }

    public static List<Date> getDatesBetweenUsingJava7(
            Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    private Query queryBase(Query query, String pin, String sequence, String startDate, String endDate, String authorName, List<String> groupId, String category, String enable) throws ParseException {
        if (groupId.size() > 0) {
            query.addCriteria(Criteria.where("groupId").in(groupId));
        }
        if (category.length() > 0) {
            query.addCriteria(Criteria.where("category").in(category));
        }
        if (enable.equals("1")) {
            query.addCriteria(Criteria.where("enable").is(true));
        } else if (enable.equals("2")) {
            query.addCriteria(Criteria.where("enable").is(false));
        }

        if (pin.length() > 0) {
            int mPin = Integer.parseInt(pin);
            query.addCriteria(Criteria.where("pin").is(mPin));
        }
        if (sequence.length() > 0) {
            query.addCriteria(Criteria.where("sequence").is(Integer.parseInt(sequence)));
        }
        if (startDate.length() > 0 && endDate.length() > 0) {
            query.addCriteria(Criteria.where("createDate").gte(new Date(Long.parseLong(startDate))).lt(new Date(Long.parseLong(endDate))));
        }
        if (authorName.length() > 0) {
            query.addCriteria(Criteria.where("author").is(authorName));
        }
        return query;
    }

    @CrossOrigin
    @RequestMapping(value = "/addEditService", method = RequestMethod.POST)
    public BaseResponse addEditService(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "pin", defaultValue = "0") @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: service") List<String> category
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable
            , @RequestParam(value = "thumbnails", required = false) MultipartFile thumbnails
            , @RequestParam(value = "name", defaultValue = "", required = false) String name
            , @RequestParam(value = "address", defaultValue = "", required = false) String address
            , @RequestParam(value = "description", defaultValue = "", required = false) String description
            , @RequestParam(value = "latitude", defaultValue = "0", required = false) double latitude
            , @RequestParam(value = "longitude", defaultValue = "0", required = false) double longitude
            , @RequestParam(value = "buttonName", defaultValue = "", required = false) String buttonName
            , @RequestParam(value = "buttonUrl", defaultValue = "", required = false) String buttonUrl
            , @RequestParam(value = "picturesPath", required = false) MultipartFile[] pictures
            , @RequestParam(value = "deletePictures", required = false) @ApiParam(value = "List name of picture") List<String> deletePictures
    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        List<String> picturesList = new ArrayList<>();

        Service service = new Service();
        if (id.length() > 0) {
            Optional<Service> byId = serviceRepository.findById(id);
            service = byId.get();
            checkSuperAdminGroups(profile,service.getGroupId());
            picturesList = service.getPicturesPath();
        }else {
            checkSuperAdminGroups(profile,groupId);
        }

        if (thumbnails != null) {
            service.setThumbnailsPath(thumbnail(thumbnails, profile, FOLDER_COVER));
        }
        if (sequence == 0) {
            service.setSequence(9999);
        } else {
            service.setSequence(sequence);
        }
        service.setGroupId(groupId);
        service.setDescription(description);
        service.setName(name);
        service.setButtonName(buttonName);
        service.setButtonUrl(buttonUrl);
        service.setAddress(address);
        service.setLatitude(latitude);
        service.setLongitude(longitude);
        service.setCategory(category);
        service.setEnable(enable);
        service.setPin(Integer.parseInt(pin));
        if (pictures != null && pictures.length > 0) {
            List<String> picture = picture(picturesList, pictures, profile);
            service.setPicturesPath(picture);
        }
        if (id.length() > 0) {
            if (deletePictures != null && deletePictures.size() > 0) {
                picturesList = deletePicture(deletePictures, picturesList);
                service.setPicturesPath(picturesList);
            }
            service.setUpdateDate(new Date());
            service.setEditBy(profile.getUserName());
            Service save = serviceRepository.save(service);
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
        } else {
            service.setAuthor(profile.getUserName());
            service.setEditBy(profile.getUserName());
            Service save = serviceRepository.insert(service);
            return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), save));
        }
    }

    @CrossOrigin
    @ApiOperation(value = "จุดบริการ", notes = "", response = Service.class)
    @RequestMapping(value = "/getService", method = RequestMethod.GET)
    public BaseResponse getService(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "author", defaultValue = "", required = false) String authorName
            , @RequestParam(value = "pin", defaultValue = "", required = false) @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by pin,2 = Sort by top,3 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "a1", defaultValue = "0", required = false) double a1
            , @RequestParam(value = "a2", defaultValue = "0", required = false) double a2
            , @RequestParam(value = "b1", defaultValue = "0", required = false) double b1
            , @RequestParam(value = "b2", defaultValue = "0", required = false) double b2
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: service") String category
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "orderSequence", defaultValue = "true", required = false) boolean orderSequence

    ) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Pageable pageable = sortPage(page, sizeContents, sort, false,order,orderSequence);

        Page<Service> byId = null;
        if (id.length() > 0) {
            byId = serviceRepository.findById(id, pageable);

            byId.getContent().get(0).setCntView(byId.getContent().get(0).getCntView()+1);
            serviceRepository.save(byId.getContent().get(0));
        } else {
            Query query = new Query().with(pageable);
            queryBase(query, pin, sequence, startDate, endDate, authorName, groupId, category, enable);

            if (a1 > 0 && a2 > 0 && b1 > 0 && b2 > 0) {
                query.addCriteria(Criteria.where("latitude").gte(a1).lt(b1));
                query.addCriteria(Criteria.where("longitude").gte(a2).lt(b2));
            }
            Criteria name1 = Criteria.where("name").regex(keyWord, "i");
            Criteria address = Criteria.where("address").regex(keyWord, "i");
            Criteria description = Criteria.where("description").regex(keyWord, "i");

            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(name1, address,description));
            }
            List<Service> post = mongoTemplate.find(query, Service.class);
            long count = mongoTemplate.count(query, Service.class);
            byId = new PageImpl<Service>(post, pageable, count);
        }

        for (int i = 0; i < byId.getContent().size(); i++) {
            ProfileDisplay authorProfile = profileRepository.findByUserName(byId.getContent().get(i).getAuthor());
            if (authorProfile != null) {
                byId.getContent().get(i).setAuthorProfile(authorProfile);
            }
            //getCateName
            byId.getContent().get(i).setCategoryProfile(getCateNameList(byId.getContent().get(i).getCategory()));
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(byId.getContent().get(i).getGroupId());
            if (group!=null){
                byId.getContent().get(i).setGroupProfile(group);
            }
        }
        return getOk(new BaseResponse(byId));
    }


    @CrossOrigin
    @RequestMapping(value = "/addEditKnowledge", method = RequestMethod.POST)
    public BaseResponse addEditKnowledge(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "pin", defaultValue = "0") @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: knowledge") List<String> category
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable
            , @RequestParam(value = "thumbnailsPath", required = false) MultipartFile thumbnailsPath
            , @RequestParam(value = "title", defaultValue = "", required = false) String title
            , @RequestParam(value = "mediaType", defaultValue = "1", required = false) @ApiParam(value = "1=pdf,2=video,3=image,4=web,5=audio") int mediaType
            , @RequestParam(value = "url", defaultValue = "", required = false) String url
            , @RequestParam(value = "imagePath", required = false) MultipartFile imagePath
            , @RequestParam(value = "videoPath", required = false) MultipartFile videoPath
            , @RequestParam(value = "filesPath", required = false) MultipartFile filesPath
            , @RequestParam(value = "audioPath", required = false) MultipartFile audioPath

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Knowledge knowledge = new Knowledge();
        if (id.length() > 0) {
            Optional<Knowledge> byId = knowledgeRepository.findById(id);
            knowledge = byId.get();
            checkSuperAdminGroups(profile,knowledge.getGroupId());

        }else {
            checkSuperAdminGroups(profile,groupId);

        }

        if (thumbnailsPath != null) {
            knowledge.setThumbnailsPath(thumbnail(thumbnailsPath, profile, FOLDER_COVER));
        }
        if (sequence == 0) {
            knowledge.setSequence(9999);
        } else {
            knowledge.setSequence(sequence);
        }
        knowledge.setGroupId(groupId);
        knowledge.setMediaType(mediaType);
        if (imagePath != null) {
            knowledge.setImagePath(thumbnail(imagePath, profile, FOLDER_IMAGE));
        }
        if (videoPath != null) {
            knowledge.setVideoPath(uploadFile(videoPath, profile, FOLDER_VIDEO, FILE_TYPE_MP4));
        }

        if (filesPath != null) {
            knowledge.setFilesPath(pdf(filesPath, profile).get(0));
        }
        if (audioPath != null) {
            knowledge.setAudioPath(uploadFile(audioPath, profile, FOLDER_AUDIO, FILE_TYPE_MP3));
        }
        knowledge.setTitle(title);
        knowledge.setUrl(url);
        knowledge.setCategory(category);
        knowledge.setEnable(enable);
        knowledge.setPin(Integer.parseInt(pin));

        if (id.length() > 0) {

            knowledge.setUpdateDate(new Date());
            knowledge.setEditBy(profile.getUserName());
            Knowledge save = knowledgeRepository.save(knowledge);

            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
        } else {
            knowledge.setAuthor(profile.getUserName());
            knowledge.setEditBy(profile.getUserName());
            Knowledge save = knowledgeRepository.insert(knowledge);

            return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), save));
        }
    }

    @CrossOrigin
    @ApiOperation(value = "คลังความรู้", notes = "", response = Knowledge.class)
    @RequestMapping(value = "/getKnowledge", method = RequestMethod.GET)
    public BaseResponse getKnowledge(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "author", defaultValue = "", required = false) String authorName
            , @RequestParam(value = "pin", defaultValue = "", required = false) @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by pin,2 = Sort by top,3 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: knowledge") String category
            , @RequestParam(value = "mediaType", defaultValue = "", required = false) @ApiParam(value = "1=pdf,2=video,3=image,4=web,5=audio") String mediaType
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "orderSequence", defaultValue = "true", required = false) boolean orderSequence

    ) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Pageable pageable = sortPage(page, sizeContents, sort, false,order,orderSequence);

        Page<Knowledge> byId = null;
        if (id.length() > 0) {
            byId = knowledgeRepository.findById(id, pageable);

            byId.getContent().get(0).setCntView(byId.getContent().get(0).getCntView()+1);
            knowledgeRepository.save(byId.getContent().get(0));
        } else {
            Query query = new Query().with(pageable);
            if (mediaType.length() > 0) {
                query.addCriteria(Criteria.where("mediaType").is(Integer.parseInt(mediaType)));
            }
            queryBase(query, pin, sequence, startDate, endDate, authorName, groupId, category, enable);
            Criteria title = Criteria.where("title").regex(keyWord, "i");

            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(title));
            }
            List<Knowledge> post = mongoTemplate.find(query, Knowledge.class);
            long count = mongoTemplate.count(query, Knowledge.class);
            byId = new PageImpl<Knowledge>(post, pageable, count);
        }

        for (int i = 0; i < byId.getContent().size(); i++) {
            ProfileDisplay authorProfile = profileRepository.findByUserName(byId.getContent().get(i).getAuthor());
            if (authorProfile != null) {
                byId.getContent().get(i).setAuthorProfile(authorProfile);
            }
            //getCateName
            byId.getContent().get(i).setCategoryProfile(getCateNameList(byId.getContent().get(i).getCategory()));
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(byId.getContent().get(i).getGroupId());
            if (group!=null){
                byId.getContent().get(i).setGroupProfile(group);
            }
        }
        return getOk(new BaseResponse(byId));
    }

    @CrossOrigin
    @RequestMapping(value = "/addEditNotifications", method = RequestMethod.POST)
    public BaseResponse addEditNotifications(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "picturesPath", required = false) MultipartFile[] pictures
            , @RequestParam(value = "title", defaultValue = "", required = false) String title
            , @RequestParam(value = "url", defaultValue = "", required = false) String url
            , @RequestParam(value = "description", defaultValue = "", required = false) String description
            , @RequestParam(value = "deletePictures", required = false) @ApiParam(value = "List name of picture") List<String> deletePictures
            , @RequestParam(value = "pin", defaultValue = "0") @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "pushnotification", defaultValue = "false") Boolean pushnotification
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: notifications") List<String> category
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Notifications notifications = new Notifications();
        List<String> picturesList = new ArrayList<>();
        if (id.length() > 0) {
            Optional<Notifications> byId = notificationsRepository.findById(id);
            notifications = byId.get();
            picturesList = notifications.getPicturesPath();
            checkSuperAdminGroups(profile,notifications.getGroupId());

        }else {
            checkSuperAdminGroups(profile,groupId);

        }
        if (pictures != null && pictures.length > 0) {
            List<String> picture = picture(picturesList, pictures, profile);
            notifications.setPicturesPath(picture);
        }
        if (sequence == 0) {
            notifications.setSequence(9999);
        } else {
            notifications.setSequence(sequence);
        }
        notifications.setUrl(url);
        notifications.setGroupId(groupId);
        notifications.setCategory(category);
        notifications.setTitle(title);
        notifications.setEnable(enable);
        notifications.setPushnotification(pushnotification);
        notifications.setDescription(description);
        notifications.setPin(Integer.parseInt(pin));
        if (id.length() > 0) {
            if (deletePictures != null && deletePictures.size() > 0) {
                picturesList = deletePicture(deletePictures, picturesList);
                notifications.setPicturesPath(picturesList);
            }
            notifications.setUpdateDate(new Date());
            notifications.setEditBy(profile.getUserName());
            Notifications save = notificationsRepository.save(notifications);

            if (save.isPushnotification() == true && save.isPushAlready() == false) {
                save.setStatus(1);
                notificationsRepository.save(save);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendPushNotification(TYPE_NOTIFICATIONS, save.getId(), save.getTitle(), save.getGroupId(),TOPIC);
                        } catch (PostExceptions postExceptions) {
                            postExceptions.printStackTrace();
                        }
                    }
                }).start();
            } else {
                save.setStatus(2);
                notificationsRepository.save(save);
            }
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), notifications));
        } else {

            notifications.setAuthor(profile.getUserName());
            notifications.setEditBy(profile.getUserName());
            Notifications save = notificationsRepository.insert(notifications);

            if (save.isPushnotification() == true && save.isPushAlready() == false) {
                save.setStatus(1);
                notificationsRepository.save(save);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendPushNotification(TYPE_NOTIFICATIONS, save.getId(), save.getTitle(), save.getGroupId(),TOPIC);
                        } catch (PostExceptions postExceptions) {
                            postExceptions.printStackTrace();
                        }
                    }
                }).start();
            } else {
                save.setStatus(2);
                notificationsRepository.save(save);
            }
            return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), notifications));
        }
    }

    @CrossOrigin
    @ApiOperation(value = "การแจ้งเตือน", notes = "", response = Notifications.class)
    @RequestMapping(value = "/getNotifications", method = RequestMethod.GET)
    public BaseResponse getNotifications(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "os", defaultValue = "", required = false) @ApiParam(value = "0= web,1=ios,2=android") String os
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "author", defaultValue = "", required = false) String authorName
            , @RequestParam(value = "pin", defaultValue = "", required = false) @ApiParam(value = "0=not pin,1=pin") String pin
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by pin,2 = Sort by top,3 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "createDate", defaultValue = "", required = false) @ApiParam(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXX") String createDate
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: notifications") String category
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int order
            , @RequestParam(value = "orderSequence", defaultValue = "true", required = false) boolean orderSequence

    ) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Pageable pageable = sortPage(page, sizeContents, sort, false,order,orderSequence);

        Page<Notifications> byId = null;
        if (id.length() > 0) {
            byId = notificationsRepository.findById(id, pageable);

            if (os.length() > 0) {
                updateView(id, TYPE_NOTIFICATIONS, profile.getId(), Integer.parseInt(os));
            }
            byId.getContent().get(0).setCntView(byId.getContent().get(0).getCntView()+1);
            notificationsRepository.save(byId.getContent().get(0));
        } else {
            Query query = new Query().with(pageable);
            queryBase(query, pin, sequence, startDate, endDate, authorName, groupId, category, enable);
            if (createDate.length() > 0) {
                query.addCriteria(Criteria.where("createDate").gte(convertStringDateToIsoDate(createDate)));
            }

            Criteria title = Criteria.where("title").regex(keyWord, "i");
            Criteria description = Criteria.where("description").regex(keyWord, "i");
            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(title, description));
            }
            List<DeleteNotification> byUserId = deleteNotificationRepository.findByUserId(profile.getId());
            ArrayList<String> listDelete = new ArrayList<>();
            byUserId.forEach((xx) -> listDelete.add(String.valueOf(xx.getContentId())));

            query.addCriteria(Criteria.where("id").nin(listDelete));
            List<Notifications> post = mongoTemplate.find(query, Notifications.class);
            long count = mongoTemplate.count(query, Notifications.class);
            byId = new PageImpl<Notifications>(post, pageable, count);
        }
        List<ViewStatistic> byStatisticTypeAndUserId = viewStatisticRepository.findByStatisticTypeAndUserId(TYPE_NOTIFICATIONS, profile.getId());
        ArrayList<String> listOpen = new ArrayList<>();
        byStatisticTypeAndUserId.forEach((xx) -> listOpen.add(String.valueOf(xx.getContentId())));
        for (int i = 0; i < byId.getContent().size(); i++) {
            ProfileDisplay authorProfile = profileRepository.findByUserName(byId.getContent().get(i).getAuthor());
            if (authorProfile != null) {
                byId.getContent().get(i).setAuthorProfile(authorProfile);
            }
            if (listOpen.contains(byId.getContent().get(i).getId())) {
                byId.getContent().get(i).setOpen(true);
            }
            //getCateName
            byId.getContent().get(i).setCategoryProfile(getCateNameList(byId.getContent().get(i).getCategory()));
            //set group profile
            GroupDisplay group = groupRepository.findByIdIs(byId.getContent().get(i).getGroupId());
            if (group!=null){
                byId.getContent().get(i).setGroupProfile(group);
            }
        }
        return getOk(new BaseResponse(byId));
    }

    @CrossOrigin
    @RequestMapping(value = "/deleteNotification", method = RequestMethod.POST)
    public BaseResponse deleteNotification(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) List<String> id
    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        for (int i = 0; i < id.size(); i++) {
            List<DeleteNotification> byContentIdAndUserId = deleteNotificationRepository.findByContentIdAndUserId(id.get(i), profile.getId());
            if (byContentIdAndUserId.size() == 0) {
                DeleteNotification deleteNotification = new DeleteNotification();
                deleteNotification.setUserId(profile.getId());
                deleteNotification.setContentId(id.get(i));
                DeleteNotification insert = deleteNotificationRepository.insert(deleteNotification);
            }
        }
        return getOk(new BaseResponse());
    }

    @CrossOrigin
    @RequestMapping(value = "/deleteAllNotification", method = RequestMethod.POST)
    public BaseResponse deleteAllNotification(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: notifications") String category
            , @RequestParam(value = "os") int os

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);

        Query queryDelete = Query.query(Criteria.where("userId").is(profile.getId()));
        queryDelete.addCriteria(Criteria.where("createDate").gte(profile.getCreateDate()));
        DistinctIterable distinctDelete = mongoTemplate.getCollection("deleteNotification").distinct("contentId", queryDelete.getQueryObject(), String.class);
        List<String> viewStatisticsDelete = new ArrayList<>();

        MongoCursor cursor1 = distinctDelete.iterator();
        while (cursor1.hasNext()) {
            String delete = (String) cursor1.next();
            viewStatisticsDelete.add(delete);
        }

        Query queryAll = new Query();
        if (category.length()>0) {
            queryAll.addCriteria(Criteria.where("category").in(category));
        }
        queryAll.addCriteria(Criteria.where("enable").in(true));
        queryAll.addCriteria(Criteria.where("createDate").gte(profile.getCreateDate()));
        List<Notifications> postAll = mongoTemplate.find(queryAll, Notifications.class);

        for (int i = 0; i < postAll.size(); i++) {
            if (!viewStatisticsDelete.contains(postAll.get(i).getId())) {
                DeleteNotification deleteNotification = new DeleteNotification();
                deleteNotification.setUserId(profile.getId());
                deleteNotification.setContentId(postAll.get(i).getId());
                deleteNotificationRepository.insert(deleteNotification);
            }
        }
        return getOk(new BaseResponse());
    }

    @CrossOrigin
    @RequestMapping(value = "/countNotification", method = RequestMethod.GET)
    public BaseResponse countNotification(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);

        int i = countNoti(profile,groupId);
        return getOk(new BaseResponse(i));
    }

    @CrossOrigin
    @RequestMapping(value = "/readNotification", method = RequestMethod.POST)
    public BaseResponse readNotification(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "id", defaultValue = "", required = true) List<String> id
            , @RequestParam(value = "os") int os

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Query query = Query.query(Criteria.where("statisticType").is(TYPE_NOTIFICATIONS));
        query.addCriteria(Criteria.where("userId").is(profile.getId()));
        query.addCriteria(Criteria.where("contentId").in(id));
        List<String> viewStatisticsOpen = new ArrayList<>();
        DistinctIterable distinctOpen = mongoTemplate.getCollection("viewStatistic").distinct("contentId", query.getQueryObject(), String.class);

        MongoCursor cursor = distinctOpen.iterator();
        while (cursor.hasNext()) {
            String category = (String) cursor.next();
            viewStatisticsOpen.add(category);
        }

        Query queryDelete = Query.query(Criteria.where("userId").is(profile.getId()));
        queryDelete.addCriteria(Criteria.where("contentId").in(id));
        DistinctIterable distinctDelete = mongoTemplate.getCollection("deleteNotification").distinct("contentId", queryDelete.getQueryObject(), String.class);
        List<String> viewStatisticsDelete = new ArrayList<>();

        MongoCursor cursor1 = distinctDelete.iterator();
        while (cursor1.hasNext()) {
            String category = (String) cursor1.next();
            viewStatisticsDelete.add(category);
        }

        Query queryAll = new Query();
        queryAll.addCriteria(Criteria.where("enable").in(true));
        queryAll.addCriteria(Criteria.where("id").in(id));
        queryAll.addCriteria(Criteria.where("createDate").gte(profile.getCreateDate()));
        List<Notifications> postAll = mongoTemplate.find(queryAll, Notifications.class);

        List<String> union = union(viewStatisticsOpen, viewStatisticsDelete);
        List<String> viewStatistics = removeDuplicates(union);
        for (int i = 0; i < postAll.size(); i++) {
            if (!viewStatistics.contains(postAll.get(i).getId())) {
                updateView(postAll.get(i).getId(), TYPE_NOTIFICATIONS, profile.getId(), os);
                postAll.get(i).setCntView(postAll.get(i).getCntView()+1);
                notificationsRepository.save(postAll.get(i));
            }
        }

        return getOk(new BaseResponse());
    }

    @CrossOrigin
    @RequestMapping(value = "/readAllNotification", method = RequestMethod.POST)
    public BaseResponse readAllNotification(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of categoryType: notifications") String category
            , @RequestParam(value = "os") int os

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Query query = Query.query(Criteria.where("statisticType").is(TYPE_NOTIFICATIONS));
        query.addCriteria(Criteria.where("userId").is(profile.getId()));
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//dd/MM/yyyy
        String date = sdfDate.format(profile.getCreateDate());
        query.addCriteria(Criteria.where("time").gte(date));
        List<String> viewStatisticsOpen = new ArrayList<>();
        DistinctIterable distinctOpen = mongoTemplate.getCollection("viewStatistic").distinct("contentId", query.getQueryObject(), String.class);

        MongoCursor cursor = distinctOpen.iterator();
        while (cursor.hasNext()) {
            String open = (String) cursor.next();
            viewStatisticsOpen.add(open);
        }
        Query queryDelete = Query.query(Criteria.where("userId").is(profile.getId()));
        queryDelete.addCriteria(Criteria.where("createDate").gte(profile.getCreateDate()));
        DistinctIterable distinctDelete = mongoTemplate.getCollection("deleteNotification").distinct("contentId", queryDelete.getQueryObject(), String.class);
        List<String> viewStatisticsDelete = new ArrayList<>();

        MongoCursor cursor1 = distinctDelete.iterator();
        while (cursor1.hasNext()) {
            String delete = (String) cursor1.next();
            viewStatisticsDelete.add(delete);
        }
        Query queryAll = new Query();
        queryAll.addCriteria(Criteria.where("enable").in(true));
        if (category.length()>0) {
            queryAll.addCriteria(Criteria.where("category").in(category));
        }
        queryAll.addCriteria(Criteria.where("createDate").gte(profile.getCreateDate()));
        List<Notifications> postAll = mongoTemplate.find(queryAll, Notifications.class);

        List<String> union = union(viewStatisticsOpen, viewStatisticsDelete);
        List<String> viewStatistics = removeDuplicates(union);
        for (int i = 0; i < postAll.size(); i++) {
            if (!viewStatistics.contains(postAll.get(i).getId())) {
                updateView(postAll.get(i).getId(), TYPE_NOTIFICATIONS, profile.getId(), os);
                postAll.get(i).setCntView(postAll.get(i).getCntView()+1);
                notificationsRepository.save(postAll.get(i));
            }
        }

        return getOk(new BaseResponse());
    }

    @CrossOrigin
    @RequestMapping(value = "/reIndexContent", method = RequestMethod.POST)
    public BaseResponse reIndexContent(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestHeader(value = "listId") ArrayList<String> listId
            , @RequestParam(value = "type", defaultValue = "") @ApiParam(value = "news,banner,event,service,notifications") String name) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkSuperAdmin(profile);
        if (name.equals(TYPE_NEWS)) {
            Query query = Query.query(Criteria.where("enable").in(true));
            Update update = new Update();
            update.set("sequence", 9999);
            mongoTemplate.updateMulti(query, update, News.class);
            List<News> byEnableByOrderByCreateDateDesc = newsRepository.findByIdInAndEnableOrderByCreateDateDesc(listId, true);
            for (int i = 0; i < listId.size(); i++) {
                byEnableByOrderByCreateDateDesc.get(i).setSequence(i + 1);
                newsRepository.save(byEnableByOrderByCreateDateDesc.get(i));
            }
        } else if (name.equals(TYPE_BANNER)) {
            Query query = Query.query(Criteria.where("enable").in(true));
            Update update = new Update();
            update.set("sequence", 9999);
            mongoTemplate.updateMulti(query, update, Banner.class);
            List<Banner> byEnableByOrderByCreateDateDesc = bannerRepository.findByIdInAndEnableOrderByCreateDateDesc(listId, true);
            for (int i = 0; i < listId.size(); i++) {
                byEnableByOrderByCreateDateDesc.get(i).setSequence(i + 1);
                bannerRepository.save(byEnableByOrderByCreateDateDesc.get(i));
            }
        } else if (name.equals(TYPE_EVENT)) {
            Query query = Query.query(Criteria.where("enable").in(true));
            Update update = new Update();
            update.set("sequence", 9999);
            mongoTemplate.updateMulti(query, update, Event.class);
            List<Event> byEnableByOrderByCreateDateDesc = eventRepository.findByIdInAndEnableOrderByCreateDateDesc(listId, true);
            for (int i = 0; i < listId.size(); i++) {
                byEnableByOrderByCreateDateDesc.get(i).setSequence(i + 1);
                eventRepository.save(byEnableByOrderByCreateDateDesc.get(i));
            }
        } else if (name.equals(TYPE_SERVICE)) {
            Query query = Query.query(Criteria.where("enable").in(true));
            Update update = new Update();
            update.set("sequence", 9999);
            mongoTemplate.updateMulti(query, update, Service.class);
            List<Service> byEnableByOrderByCreateDateDesc = serviceRepository.findByIdInAndEnableOrderByCreateDateDesc(listId, true);
            for (int i = 0; i < listId.size(); i++) {
                byEnableByOrderByCreateDateDesc.get(i).setSequence(i + 1);
                serviceRepository.save(byEnableByOrderByCreateDateDesc.get(i));
            }
        } else if (name.equals(TYPE_NOTIFICATIONS)) {
            Query query = Query.query(Criteria.where("enable").in(true));
            Update update = new Update();
            update.set("sequence", 9999);
            mongoTemplate.updateMulti(query, update, Notifications.class);
            List<Notifications> byEnableByOrderByCreateDateDesc = notificationsRepository.findByIdInAndEnableOrderByCreateDateDesc(listId, true);
            for (int i = 0; i < listId.size(); i++) {
                byEnableByOrderByCreateDateDesc.get(i).setSequence(i + 1);
                notificationsRepository.save(byEnableByOrderByCreateDateDesc.get(i));
            }
        }
        return getOk(new BaseResponse(OK));

    }


    public <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public static <T> List<T> removeDuplicates(List<T> list) {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }

    private int countNoti(Profile profile,String groupId) {
        Query query = Query.query(Criteria.where("statisticType").is(TYPE_NOTIFICATIONS));
        query.addCriteria(Criteria.where("userId").is(profile.getId()));
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//dd/MM/yyyy
        String date = sdfDate.format(profile.getCreateDate());
        query.addCriteria(Criteria.where("time").gte(date));
        List<String> viewStatisticsOpen = new ArrayList<>();

        DistinctIterable distinctOpen = mongoTemplate.getCollection("viewStatistic").distinct("contentId", query.getQueryObject(), String.class);
        MongoCursor cursorOpen = distinctOpen.iterator();
        while (cursorOpen.hasNext()) {
            viewStatisticsOpen.add((String) cursorOpen.next());
        }
        List<String> viewStatisticsDelete = new ArrayList<>();
        DistinctIterable distinctDelete = mongoTemplate.getCollection("deleteNotification").distinct("contentId", query.getQueryObject(), String.class);

        MongoCursor cursor = distinctDelete.iterator();
        while (cursor.hasNext()) {
            viewStatisticsDelete.add((String) cursor.next());
        }

        Query queryAll = new Query();
        queryAll.addCriteria(Criteria.where("enable").in(true));
        queryAll.addCriteria(Criteria.where("groupId").is(groupId));
        queryAll.addCriteria(Criteria.where("createDate").gte(profile.getCreateDate()));

        List<Notifications> postAll = mongoTemplate.find(queryAll, Notifications.class);

        List<String> union = union(viewStatisticsOpen, viewStatisticsDelete);
        List<String> viewStatistics = removeDuplicates(union);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < postAll.size(); i++) {
            if (!viewStatistics.contains(postAll.get(i).getId())) {
                result.add(postAll.get(i).getId());
            }
        }
        return result.size();
    }

/*

    @CrossOrigin
    @RequestMapping(value = "/importNews", method = RequestMethod.POST)
    public BaseResponse importRisk(HttpServletRequest request
            , @RequestParam(value = "file") MultipartFile file
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "sheet", defaultValue = "0") int sheet) throws Exception {
        Profile profile = userValidateToken(token,request);
//        checkSuperAdmin(profile);
        Workbook workbook;

        if (FilenameUtils.isExtension(file.getOriginalFilename(), "xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());

        } else if (FilenameUtils.isExtension(file.getOriginalFilename(), "xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else {
            throw new PostExceptions(FAILED, localizeText.getNoExcel());

        }
        Sheet worksheet = workbook.getSheetAt(sheet);
        int lastRowNum = worksheet.getLastRowNum();
        for (int i = 3; i <= lastRowNum; i++) {
            Row row = worksheet.getRow(i);
            News news = new News();

            if (row.getCell(1) != null && row.getCell(1).getStringCellValue().length() > 0) {
                news.setTitle(row.getCell(1).getStringCellValue());

                if (row.getCell(2) != null && row.getCell(2).getStringCellValue().length() > 0) {
                    news.setDescription(row.getCell(2).getStringCellValue());
                }

                if (row.getCell(3) != null && row.getCell(3).getStringCellValue().length() > 0) {
                    news.setSequence(Integer.parseInt(row.getCell(3).getStringCellValue()));
                }else {
                    news.setSequence(9999);
                }

                if (row.getCell(4) != null && row.getCell(4).getStringCellValue().length() > 0) {
                    news.setStartDate(convertImportDateToIsoDate(row.getCell(4).getStringCellValue()));

                    if (row.getCell(5) != null && row.getCell(5).getStringCellValue().length() > 0) {
                        news.setEndDate(convertImportDateToIsoDate(row.getCell(5).getStringCellValue()));
                    }else {
                        news.setEndDate(null);
                    }

                }else {
                    news.setStartDate(null);
                }

                if (row.getCell(6) != null && row.getCell(6).getStringCellValue().length() > 0) {
                    news.setButtonName(row.getCell(6).getStringCellValue());
                }
                if (row.getCell(7) != null && row.getCell(7).getStringCellValue().length() > 0) {
                    news.setButtonUrl(row.getCell(7).getStringCellValue());
                }
                if (row.getCell(8) != null && row.getCell(8).getStringCellValue().length() > 0) {
                    news.setYoutube(row.getCell(8).getStringCellValue());
                }
                if (row.getCell(9) != null && row.getCell(9).getStringCellValue().length() > 0) {
                    Category byCategory = categoryRepository.findByCategoryNameAndCategoryType(row.getCell(9).getStringCellValue(), TYPE_NEWS);
                    if (byCategory!=null){
                      List<String>cate =  new ArrayList<>();
                        cate.add(byCategory.getCategoryCode());
                        news.setCategory(cate);
                    }else {
                        return getError(ErrorFactory.getError(FAILED, "ไม่พบหมวดหมู่ "+row.getCell(9).getStringCellValue()+" ในระบบ"));
                    }
                }
                List<String>readGroups =  new ArrayList<>();

                if (row.getCell(10) != null && row.getCell(10).getStringCellValue().length() > 0) {
                    readGroups.add("1");
                }
                if (row.getCell(11) != null && row.getCell(11).getStringCellValue().length() > 0) {
                    readGroups.add("2");
                }
                if (row.getCell(12) != null && row.getCell(12).getStringCellValue().length() > 0) {
                    readGroups.add("3");
                }
                news.setReadGroups(readGroups);
                if (row.getCell(13) != null && row.getCell(13).getStringCellValue().length() > 0) {
                    news.setPushnotification(true);
                }else {
                    news.setPushnotification(false);
                }
                if (row.getCell(14) != null && row.getCell(14).getStringCellValue().length() > 0) {
                    news.setPin(1);
                }else {
                    news.setPin(0);
                }
                row.getCell(14);
//                news.setThumbnailsPath();

            if (profile.getSuperAdmin() == false) {
                news.setReadInstitution(profile.getAdminInstitution());
            }

            news.setFacebookLive("");
            news.setEnable(true);
            news.setAuthor(profile.getUserName());
            news.setEditBy(profile.getUserName());
            News save = newsRepository.insert(news);

            if (save.getPushnotification() == true && save.isPushAlready() == false) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendNewsPushNotification(TYPE_NEWS, save.getId(), save.getDescription(), save.getTitle(),save.getReadGroups());
                    }
                }).start();
            }

            updateDate(TYPE_NEWS);

            }
        }
        return getOk(new BaseResponse());

    }
    @CrossOrigin
    @RequestMapping(value = "/importEvent", method = RequestMethod.POST)
    public BaseResponse importEvent(HttpServletRequest request
            , @RequestParam(value = "file") MultipartFile file
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "sheet", defaultValue = "0") int sheet) throws Exception {
        Profile profile = userValidateToken(token,request);
//        checkSuperAdmin(profile);
        Workbook workbook;

        if (FilenameUtils.isExtension(file.getOriginalFilename(), "xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());

        } else if (FilenameUtils.isExtension(file.getOriginalFilename(), "xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else {
            throw new PostExceptions(FAILED, localizeText.getNoExcel());

        }
        Sheet worksheet = workbook.getSheetAt(sheet);
        int lastRowNum = worksheet.getLastRowNum();
        for (int i = 3; i <= lastRowNum; i++) {
            Row row = worksheet.getRow(i);
            Event news = new Event();

            if (row.getCell(1) != null && row.getCell(1).getStringCellValue().length() > 0) {
                news.setTitle(row.getCell(1).getStringCellValue());

                if (row.getCell(2) != null && row.getCell(2).getStringCellValue().length() > 0) {
                    news.setDescription(row.getCell(2).getStringCellValue());
                }

                if (row.getCell(3) != null && row.getCell(3).getStringCellValue().length() > 0) {
                    news.setSequence(Integer.parseInt(row.getCell(3).getStringCellValue()));
                } else {
                    news.setSequence(9999);
                }

                if (row.getCell(4) != null && row.getCell(4).getStringCellValue().length() > 0) {
                    news.setStartDate(convertImportDateToIsoDate(row.getCell(4).getStringCellValue()));

                    if (row.getCell(5) != null && row.getCell(5).getStringCellValue().length() > 0) {
                        news.setEndDate(convertImportDateToIsoDate(row.getCell(5).getStringCellValue()));
                    } else {
                        news.setEndDate(null);
                    }

                } else {
                    news.setStartDate(null);
                }

                if (row.getCell(6) != null && row.getCell(6).getStringCellValue().length() > 0) {
                    news.setButtonName(row.getCell(6).getStringCellValue());
                }
                if (row.getCell(7) != null && row.getCell(7).getStringCellValue().length() > 0) {
                    news.setButtonUrl(row.getCell(7).getStringCellValue());
                }
                if (row.getCell(8) != null && row.getCell(8).getStringCellValue().length() > 0) {
                    news.setYoutube(row.getCell(8).getStringCellValue());
                }
                if (row.getCell(9) != null && row.getCell(9).getStringCellValue().length() > 0) {
                    Category byCategory = categoryRepository.findByCategoryNameAndCategoryType(row.getCell(9).getStringCellValue(), TYPE_EVENT);
                    if (byCategory != null) {
                        List<String> cate = new ArrayList<>();
                        cate.add(byCategory.getCategoryCode());
                        news.setCategory(cate);
                    } else {
                        return getError(ErrorFactory.getError(FAILED, "ไม่พบหมวดหมู่ " + row.getCell(9).getStringCellValue() + " ในระบบ"));
                    }

                }


                if (row.getCell(10) != null && row.getCell(10).getStringCellValue().length() > 0) {
                    news.setPushnotification(true);
                } else {
                    news.setPushnotification(false);
                }
                if (row.getCell(10) != null && row.getCell(10).getStringCellValue().length() > 0) {
                    news.setPin(1);
                } else {
                    news.setPin(0);

                }
//                news.setThumbnailsPath();
//                news.setPicturesPath();

                if (profile.getSuperAdmin() == false) {
                    news.setReadInstitution(profile.getAdminInstitution());
                }

                news.setFacebookLive("");
                news.setEnable(true);
                news.setAuthor(profile.getUserName());
                news.setEditBy(profile.getUserName());
                Event save = eventRepository.insert(news);
                updateDate(TYPE_EVENT);

            }
        }
        return getOk(new BaseResponse());

    }*/
    /* @CrossOrigin
    @RequestMapping(value = "/importService", method = RequestMethod.POST)
    public BaseResponse importService(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "file") MultipartFile file
            , @RequestParam(value = "sheet", defaultValue = "0") int sheet) throws Exception {
        initialize(request);
        Profile adminProfile = userValidateToken(token, request);
        checkSuperAdmin(adminProfile);
        Workbook workbook;
        if (FilenameUtils.isExtension(file.getOriginalFilename(), "xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        workbook.getNumberOfSheets();
        Sheet worksheet = workbook.getSheetAt(sheet);

        int numericCellValue = 0;
        for (int i = 1; i <= worksheet.getLastRowNum(); i++) {
            Service service = new Service();
            Row row = worksheet.getRow(i);
            Optional<Service> byName1 = null;
            if (row.getCell(1) != null && row.getCell(1).getStringCellValue().length() > 1) {
                byName1 = serviceRepository.findByName1(row.getCell(1).getStringCellValue());
                if (byName1.isPresent()) {
                    service = byName1.get();
                }

                service.setName1(row.getCell(1).getStringCellValue());

                if (row.getCell(0) != null) {
                    service.setSequence((int) row.getCell(0).getNumericCellValue());
                }

                if (row.getCell(2) != null && row.getCell(2).getStringCellValue().length() > 0) {
                    service.setName2(row.getCell(2).getStringCellValue());
                }

                if (row.getCell(3) != null && row.getCell(3).getStringCellValue().length() > 0) {
                    List<String> category = new ArrayList<>();
                    List<String> elephantList = Arrays.asList(row.getCell(3).getStringCellValue().split(","));
                    for (int j=0;j<elephantList.size();j++){
                        Category byCategoryNameAndCategoryType = categoryRepository.findByCategoryNameAndCategoryType(elephantList.get(j), TYPE_SERVICE);
                        category.add(byCategoryNameAndCategoryType.getCategoryCode());
                    }
                    service.setCategory(category);

                }

                if (row.getCell(4) != null && row.getCell(4).getStringCellValue().length() > 0) {
                    service.setAddress(row.getCell(4).getStringCellValue());
                }


                if (row.getCell(5) != null) {
                    row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                    String cell = row.getCell(5).getRichStringCellValue().getString();
                    service.setPhoneNumber(cell);
                }
                if (row.getCell(6) != null) {
                    row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                    String cell = row.getCell(6).getRichStringCellValue().getString();
                    service.setFax(cell);
                }
                if (row.getCell(7) != null && row.getCell(7).getStringCellValue().length() > 0) {
                    service.setEmail(row.getCell(7).getStringCellValue());
                }
                if (row.getCell(8) != null && row.getCell(8).getStringCellValue().length() > 0) {
                    service.setWebsite(row.getCell(8).getStringCellValue());
                }
                if (row.getCell(9) != null) {
                    String cell = row.getCell(9).getRichStringCellValue().getString();
                    if (cell.length() > 0) {
                        service.setLatitude(Double.parseDouble(cell));
                    }
                }
                if (row.getCell(10) != null) {
                    String cell = row.getCell(10).getRichStringCellValue().getString();
                    if (cell.length() > 0) {
                        service.setLongitude(Double.parseDouble(cell));
                    }
                }
                service.setAuthor(adminProfile.getUserName());
                service.setUpdateDate(new Date());
                service.setEditBy(adminProfile.getUserName());
                if (byName1 != null) {
                    serviceRepository.save(service);
                } else {
                    serviceRepository.insert(service);
                }

            }
        }
        return getOk(new BaseResponse(OK, "" + numericCellValue));
    }
*/
}
