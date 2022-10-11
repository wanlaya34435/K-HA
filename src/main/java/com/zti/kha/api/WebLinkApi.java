package com.zti.kha.api;


import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.Content.WebLink;

import com.zti.kha.model.User.GroupDisplay;
import com.zti.kha.model.User.Profile;
import com.zti.kha.model.User.ProfileDisplay;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.utility.ErrorFactory;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Created by S on 9/22/2016.
 */
@RestController
public class WebLinkApi extends CommonApi  {
    @CrossOrigin
    @ApiOperation(value = "บริการ", notes = "", response = WebLink.class)
    @RequestMapping(value = "/getWebLink", method = RequestMethod.GET)
    public BaseResponse getWebLink(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by sequence,2 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
    ) throws PostExceptions {
        initialize(request);
       userValidateToken(token, request);
        Pageable pageable = null;
        Page<WebLink> byId = null;
        if (sort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("createDate").descending()));
        } else if (sort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("createDate").descending());
        }
        if (id.length() != 0) {
             byId = webLinkRepository.findById(id, pageable);
            if (byId==null){
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }
            return getOk(new BaseResponse(byId.getContent().get(0)));
        } else {
            Query query = new Query().with(pageable);
            if (enable.equals("1")) {
                query.addCriteria(Criteria.where("enable").is(true));
            } else if (enable.equals("2")) {
                query.addCriteria(Criteria.where("enable").is(false));
            }
            if (groupId.size()>0){
                query.addCriteria(Criteria.where("groupId").in(groupId));

            }
            Criteria url = Criteria.where("url").regex(keyWord, "i");

            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(url));
            }
            List<WebLink> post = mongoTemplate.find(query, WebLink.class);
            long count = mongoTemplate.count(query, WebLink.class);
            byId = new PageImpl<WebLink>(post, pageable, count);
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
    @RequestMapping(value = "/addEditWebLink", method = RequestMethod.POST)
    public BaseResponse addEditWebLink(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "",required = false) String id
            , @RequestParam(value = "image", required = false) MultipartFile image
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "sequence", defaultValue = "9999", required = false) int sequence
            , @RequestParam(value = "url", defaultValue = "", required = false) String url
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable

    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        WebLink byId = new WebLink();
        if (id.length()>0){
            Optional<WebLink> byId1 = webLinkRepository.findById(id);
            if (byId1.isPresent()==false){
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }else {
                byId=byId1.get();
                checkSuperAdminGroups(profile,byId1.get().getGroupId());
            }
        }else {
            checkSuperAdminGroups(profile,groupId);
        }
        if (image != null) {
            String imageMobilePath = saveFile(image, FILE_TYPE_IMAGE, profile, FOLDER_IMAGE);
            if (imageMobilePath != null || imageMobilePath.length() > 0) {
                byId.setImage(imageMobilePath);
            }
        }
        byId.setEditBy(profile.getUserName());
        byId.setUpdateDate(new Date());
        byId.setGroupId(groupId);
        byId.setSequence(sequence);
        byId.setUrl(url);
        byId.setEnable(enable);
        if (id.length()>0){
            WebLink save = webLinkRepository.save(byId);
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
        }else {
            byId.setAuthor(profile.getUserName());
            WebLink insert = webLinkRepository.insert(byId);
            return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), insert));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/deleteWebLink", method = RequestMethod.POST)
    public BaseResponse deleteWebLink(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Optional<WebLink> byId = webLinkRepository.findById(id);
        if (byId.isPresent()==false){
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
        }
        checkSuperAdminGroups(profile,byId.get().getGroupId());
        webLinkRepository.delete(byId.get());
        return getOk(new BaseResponse(byId));
    }

}
