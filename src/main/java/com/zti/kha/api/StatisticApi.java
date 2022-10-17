package com.zti.kha.api;

import com.zti.kha.model.*;
import com.zti.kha.model.ComplainInfo.Complain;
import com.zti.kha.model.Content.Event;
import com.zti.kha.model.Content.Knowledge;
import com.zti.kha.model.Content.News;
import com.zti.kha.model.Content.Service;
import com.zti.kha.model.Statistic.*;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.User.Profile;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class StatisticApi extends CommonApi {
    @CrossOrigin
    @ApiOperation(value = "เก็บสถิติแอพ")
    @RequestMapping(value = "/updateStatistic", method = RequestMethod.POST)
    public BaseResponse updateStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "os", defaultValue = "") @ApiParam(value = "ios = 1,android=2") int os
            , @RequestParam(value = "deviceId", defaultValue = "") String deviceId

    ) throws PostExceptions {

        initialize(request);
        String userId;
        Profile profile = userValidateToken(token, request);
        userId = profile.getId();

        int size = installStatisticRepository.findByDeviceId(deviceId).size();
        if (size == 0) {
            InstallStatistic downloadStatistic = new InstallStatistic();
            downloadStatistic.setOs(os);
            downloadStatistic.setDeviceId(deviceId);
            installStatisticRepository.insert(downloadStatistic);
        }
        DailyStatistic dailyStatistic = new DailyStatistic();
        dailyStatistic.setOs(os);
        dailyStatistic.setTime(dateView());
        dailyStatistic.setUserId(userId);
        dailyStatisticRepository.insert(dailyStatistic);
        return getOk(new BaseResponse());
    }

    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสถิติ")
    @RequestMapping(value = "/getInstallStatistic", method = RequestMethod.GET)
    public BaseResponse getInstallStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token

    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkAdmin(profile);

        LocalDateTime date = LocalDateTime.now();
        List<ReturnStatisticInstall> dateStatistic = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {

            date = date.minusDays(1L);
            Date datesStart = Date.from(date.toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date dates = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();
            cal.setTime(dates);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date datesEnd = cal.getTime();

            Query query = new Query();
            Criteria createDate = Criteria.where("time").gte(datesStart).lte(datesEnd);
            query.addCriteria(Criteria.where("os").is(1));
            query.addCriteria(createDate);
            List<InstallStatistic> ios = mongoTemplate.find(query, InstallStatistic.class);

            Query queryAndroid = new Query();
            queryAndroid.addCriteria(Criteria.where("os").is(2));
            queryAndroid.addCriteria(createDate);
            List<InstallStatistic> android = mongoTemplate.find(queryAndroid, InstallStatistic.class);

            dateStatistic.add(new ReturnStatisticInstall(datesEnd, android.size(), ios.size()));
        }
        int ios = installStatisticRepository.findByOs(1).size();
        int android = installStatisticRepository.findByOs(2).size();

        Map<String, Object> result = new HashMap<>();
        result.put("installIOS", ios);
        result.put("installAndroid", android);
        result.put("dateStatistic", dateStatistic);

        return getOk(new BaseResponse(result));
    }

    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสถิติการเข้าใช้งานแอพ")
    @RequestMapping(value = "/getDailyStatistic", method = RequestMethod.GET)
    public BaseResponse getDailyStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token

    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkAdmin(profile);

        LocalDateTime date = LocalDateTime.now();
        List<ReturnStatistic> dateStatistic = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {

            date = date.minusDays(1L);
            Date datesStart = Date.from(date.toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date dates = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();
            cal.setTime(dates);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date datesEnd = cal.getTime();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//dd/MM/yyyy
            String datesStarts = sdfDate.format(datesStart);
            String datesEnds = sdfDate.format(datesEnd);

            Query query = new Query();
            Criteria createDate = Criteria.where("time").gte(datesStarts).lte(datesEnds);
            query.addCriteria(createDate);
            List<DailyStatistic> post = mongoTemplate.find(query, DailyStatistic.class);

            dateStatistic.add(new ReturnStatistic(datesEnd, post.size()));
        }

        return getOk(new BaseResponse(dateStatistic));
    }
    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสถิติจำนวน")
    @RequestMapping(value = "/getSizeStatistic", method = RequestMethod.GET)
    public BaseResponse getSizeStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId

    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkAdmin(profile);
        int user = 0;
        int pending = 0;
        int process = 0;
        int cancel = 0;
        int done = 0;
        int complain = 0;
        if (groupId.size() > 0) {
            user = profileRepository.findByReadGroupsGroupIdIn(groupId).size();
            complain = complainRepository.findByGroupIdIn(groupId).size();
            pending = complainRepository.findByGroupIdInAndCurrentStatus(groupId, 0).size();
            process = complainRepository.findByGroupIdInAndCurrentStatus(groupId, 1).size();
            cancel = complainRepository.findByGroupIdInAndCurrentStatus(groupId, 2).size();
            done = complainRepository.findByGroupIdInAndCurrentStatus(groupId, 3).size();
        } else {

            user = profileRepository.findAll().size();
            complain = complainRepository.findAll().size();
            pending = complainRepository.findByCurrentStatus(0).size();
            process = complainRepository.findByCurrentStatus(1).size();
            cancel = complainRepository.findByCurrentStatus(2).size();
            done = complainRepository.findByCurrentStatus(3).size();
        }


        Calendar calSt = Calendar.getInstance();
        calSt.setTime(new Date());
        calSt.set(Calendar.HOUR_OF_DAY, 00);
        calSt.set(Calendar.MINUTE, 00);
        calSt.set(Calendar.SECOND, 00);
        Date start = calSt.getTime();

        Calendar calEn = Calendar.getInstance();
        calEn.setTime(new Date());
        calEn.set(Calendar.HOUR_OF_DAY, 23);
        calSt.set(Calendar.MINUTE, 59);
        calEn.set(Calendar.SECOND, 59);
        Date end = calEn.getTime();
        int complainNow = 0;
        if (groupId.size() > 0) {
            complainNow = complainRepository.findByGroupIdInAndCreateDateBetween(groupId, start, end).size();
        } else {
            complainNow = complainRepository.findByCreateDateBetween(start, end).size();

        }
        Map<String, Object> result = new HashMap<>();
        result.put("userSize", user);
        result.put("complainSize", complain);
        result.put("complainNowSize", complainNow);
        result.put("pendingSize", pending);
        result.put("processsSize", process);
        result.put("doneSize", done);
        result.put("cancelSize", cancel);
        List<Complain> byGroupIdAndCurrentStatus = new ArrayList<>();
        if (groupId.size() > 0) {
            byGroupIdAndCurrentStatus = complainRepository.findByGroupIdInAndCurrentStatus(groupId, 3);
        } else {
            byGroupIdAndCurrentStatus = complainRepository.findByCurrentStatus(3);

        }
        int rateUpdate = 0;
        int rateDuration = 0;
        int count = 0;
        for (Complain complain1 : byGroupIdAndCurrentStatus) {
            if (complain1.getRate() != null) {
                rateUpdate = rateUpdate + complain1.getRate().getRateUpdate();
                rateDuration = rateDuration + complain1.getRate().getRateDuration();
                count++;
            }
        }
        float rateUpdateSize = 0;
        float rateDurationSize = 0;
        if (count > 0) {
            rateUpdateSize = rateUpdate / count;
            rateDurationSize = rateDuration / count;
        }
        result.put("rateUpdateSize", rateUpdateSize);
        result.put("rateDurationSize", rateDurationSize);
        return getOk(new BaseResponse(result));
    }



    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสถิติจำนวนการร้องเรียน30วัน")
    @RequestMapping(value = "/getComplainMonthStatistic", method = RequestMethod.GET)
    public BaseResponse getComplainMonthStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId

    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkAdmin(profile);


        LocalDateTime date = LocalDateTime.now();
        List<ReturnStatistic> dateStatistic = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {

            date = date.minusDays(1L);
            Date datesStart = Date.from(date.toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date dates = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();
            cal.setTime(dates);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date datesEnd = cal.getTime();

            Query query = new Query();

            query.addCriteria(Criteria.where("createDate").gte(datesStart).lte(datesEnd));
            if (groupId.size() > 0) {
                query.addCriteria(Criteria.where("groupId").in(groupId));
            }
            List<Complain> post = mongoTemplate.find(query, Complain.class);

            dateStatistic.add(new ReturnStatistic(datesEnd, post.size()));
        }

        return getOk(new BaseResponse(dateStatistic));
    }

    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสถิติจำนวนการร้องเรียน12เดือน")
    @RequestMapping(value = "/getComplainYearStatistic", method = RequestMethod.GET)
    public BaseResponse getComplainYearStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId

    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkAdmin(profile);


        LocalDateTime date = LocalDateTime.now();
        List<ReturnStatistic> dateStatistic = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {

            date = date.minusMonths(1L);
            Date dates = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());

            Calendar calst = Calendar.getInstance();
            calst.setTime(dates);
            calst.set(Calendar.HOUR_OF_DAY, 00);
            calst.set(Calendar.MINUTE, 00);
            calst.set(Calendar.SECOND, 00);
            calst.set(Calendar.MILLISECOND, 001);
            calst.set(Calendar.DATE, 1);
            Date datesStart = calst.getTime();

//            Date datesStart = Date.from(date.toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());


            Calendar cal = Calendar.getInstance();
            cal.setTime(dates);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date datesEnd = cal.getTime();

            Query query = new Query();

            query.addCriteria(Criteria.where("createDate").gte(datesStart).lte(datesEnd));
            if (groupId.size() > 0) {
                query.addCriteria(Criteria.where("groupId").in(groupId));
            }
            List<Complain> post = mongoTemplate.find(query, Complain.class);

            dateStatistic.add(new ReturnStatistic(datesEnd, post.size()));
        }

        return getOk(new BaseResponse(dateStatistic));
    }

    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสถิติจำนวนการร้องเรียนตามหมวดหมู่")
    @RequestMapping(value = "/getComplainCategorypStatistic", method = RequestMethod.GET)
    public BaseResponse getComplainTopStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId

    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkAdmin(profile);
        List<Category> category = new ArrayList<>();
        List<ReturnTopStatistic> dateStatistic = new ArrayList<>();

            category = categoryRepository.findByParentCategoryAndCategoryType("", "complain");


        for (int i = 0; i < category.size(); i++) {
            Query query = new Query();
            if (groupId.size() > 0) {
                query.addCriteria(Criteria.where("groupId").in(groupId));
            }
            query.addCriteria(Criteria.where("titleId").is(category.get(i).getCategoryCode()));
            List<Complain> post = mongoTemplate.find(query, Complain.class);
            dateStatistic.add(new ReturnTopStatistic(category.get(i).getCategoryName(), post.size()));
        }

        return getOk(new BaseResponse(dateStatistic));
    }

    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสถิติจำนวนการเข้าชม")
    @RequestMapping(value = "/getSumViewsStatistic", method = RequestMethod.GET)
    public BaseResponse getSumViewsStatistic(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId

    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token,request);
        checkAdmin(profile);
        int newsSize = 0;
        int eventSize = 0;
        int knowledgeSize = 0;
        int serviceSize = 0;
        Aggregation agg;
        if (groupId.size() > 0) {
            agg = Aggregation.newAggregation(
                    Aggregation.match(new Criteria()
                            .and("groupId").in(groupId)),
                    Aggregation.group().sum("cntView").as("total")
            );
        } else {
            agg = Aggregation.newAggregation(
                    Aggregation.match(new Criteria()),
                            Aggregation.group().sum("cntView").as("total")
                    );
        }


        AggregationResults<Total> resultNews = mongoTemplate.aggregate(agg, News.class, Total.class);
        List<Total> newsList = resultNews.getMappedResults();
        if (newsList.size() > 0) {
            newsSize = newsList.get(0).getTotal();
        }

        AggregationResults<Total> resultEvent = mongoTemplate.aggregate(agg, Event.class, Total.class);
        List<Total> eventList = resultEvent.getMappedResults();
        if (eventList.size() > 0) {
            eventSize = eventList.get(0).getTotal();
        }

        AggregationResults<Total> resultKnowledge = mongoTemplate.aggregate(agg, Knowledge.class, Total.class);
        List<Total> knowledgeList = resultKnowledge.getMappedResults();
        if (knowledgeList.size() > 0) {
            knowledgeSize = knowledgeList.get(0).getTotal();
        }

        AggregationResults<Total> resultService = mongoTemplate.aggregate(agg, Service.class, Total.class);
        List<Total> servicegeList = resultService.getMappedResults();
        if (servicegeList.size() > 0) {
            serviceSize = servicegeList.get(0).getTotal();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("allSize", newsSize + eventSize + knowledgeSize + serviceSize);
        map.put("newsSize", newsSize);
        map.put("eventSize", eventSize);
        map.put("knowledgeSize", knowledgeSize);
        map.put("serviceSize", serviceSize);
        return getOk(new BaseResponse(map));
    }
}
