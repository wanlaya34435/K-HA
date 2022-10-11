package com.zti.kha.api;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.LogDelete.LogDeleteAccount;
import com.zti.kha.model.LogDelete.RemarkDelete;

import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.User.LogLogin;
import com.zti.kha.model.User.Profile;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class LogDeleteAccountApi extends CommonApi {
    @CrossOrigin
    @ApiOperation(value = "ลบบัญชีผู้ใช้งาน", notes = "", response = LogDeleteAccount.class)
    @RequestMapping(value = "/deleteAccount", method = RequestMethod.DELETE)
    public BaseResponse deleteAccount(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "remark", defaultValue = "", required = true) String remark) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        LogDeleteAccount logDeleteAccount =new LogDeleteAccount();
        if (profile.getUserName()!=null) {
            logDeleteAccount.setUserName(profile.getUserName());
        }

        if (profile.getFirstName()!=null) {
            logDeleteAccount.setFirstName(profile.getFirstName());
        }

        if (profile.getLastName()!=null) {
            logDeleteAccount.setLastName(profile.getLastName());
        }

        if (profile.getEmail()!=null) {
            logDeleteAccount.setEmail(profile.getEmail());
        }


        logDeleteAccount.setUserId(profile.getId());
        logDeleteAccount.setRemark(remark);
        logDeleteAccountRepository.insert(logDeleteAccount);
        profileRepository.delete(profile);

        List<LogLogin> byUserId = logLoginRepository.findByUserId(profile.getId());
        logLoginRepository.deleteAll(byUserId);

        return getOk(new BaseResponse(OK, localizeText.getDeleted()));
    }
    @CrossOrigin
    @ApiOperation(value = "เหตุผลลบบัญชีผู้ใช้งาน", notes = "", response = RemarkDelete.class)
    @RequestMapping(value = "/getRemarkDeleteAccount", method = RequestMethod.GET)
    public BaseResponse getRemarkDeleteAccount(HttpServletRequest request) throws PostExceptions {

        initialize(request);
        List<RemarkDelete> all = remarkDeleteRepository.findAll();


        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), all));

    }
    @CrossOrigin
    @ApiOperation(value = "ข้อมูลลบบัญชีผู้ใช้งาน", notes = "", response = LogDeleteAccount.class)
    @RequestMapping(value = "/getLogDeleteAccount", method = RequestMethod.GET)
    public BaseResponse getLogDeleteAccount(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "30", required = false) int sizeContents

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkSuperAdmin(profile);
        Pageable pageable = new PageRequest(page, sizeContents, new Sort(new Sort.Order(Sort.Direction.ASC, "name")));

        Page<LogDeleteAccount> byId = null;
        if (id.length() > 0) {
            byId = logDeleteAccountRepository.findById(id, pageable);
        }else {
            Query query = new Query().with(pageable);

            Criteria userName = Criteria.where("userName").regex(keyWord, "i");
            Criteria firstName = Criteria.where("firstName").regex(keyWord, "i");
            Criteria lastName = Criteria.where("lastName").regex(keyWord, "i");
            Criteria email = Criteria.where("email").regex(keyWord, "i");
            Criteria remark = Criteria.where("remark").regex(keyWord, "i");
            Criteria appleId = Criteria.where("appleId").regex(keyWord, "i");
            Criteria googleId = Criteria.where("googleId").regex(keyWord, "i");
            Criteria facebookId = Criteria.where("facebookId").regex(keyWord, "i");

            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(userName, firstName,lastName,email,remark,appleId,googleId,facebookId));
            }
            List<LogDeleteAccount> post = mongoTemplate.find(query, LogDeleteAccount.class);
            long count = mongoTemplate.count(query, LogDeleteAccount.class);
            byId = new PageImpl<LogDeleteAccount>(post, pageable, count);
        }

        return getOk(new BaseResponse(byId));

    }
}
