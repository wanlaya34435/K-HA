package com.zti.kha.api;


import com.zti.kha.model.Contact;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.User.GroupDisplay;
import com.zti.kha.model.User.Profile;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by up on 3/9/17.
 */
@RestController
public class ContactApi extends CommonApi {

    public ContactApi() throws Exception {
    }

    @CrossOrigin
    @ApiOperation(value = "ติดต่อ", notes = "", response = Contact.class)
    @RequestMapping(value = "/getContact", method = RequestMethod.GET)
    public BaseResponse getContact(HttpServletRequest request, HttpServletResponse response
            , @RequestHeader(value = "token", required = true) String token
            , @RequestParam(value = "groupId", defaultValue = "", required = false) List<String> groupId
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by name,2 = Sort by createDate") int sort
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
    ) throws PostExceptions {

        initialize(request);
        userValidateToken(token, request);
        Pageable pageable;
        if (sort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("name1").ascending()));
        } else {
            pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("createDate").descending()));
        }
        Page<Contact> byId = null;
        if (id.length() > 0) {
            byId = contactRepository.findById(id, pageable);

        } else {
            Query query = new Query().with(pageable);
            if (groupId.size() > 0) {
                query.addCriteria(Criteria.where("groupId").in(groupId));
            }
            Criteria name1 = Criteria.where("name1").regex(keyWord, "i");
            Criteria name2 = Criteria.where("name2").regex(keyWord, "i");
            Criteria description = Criteria.where("description").regex(keyWord, "i");
            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(name1,name2, description));
            }
            List<Contact> post = mongoTemplate.find(query, Contact.class);
            long count = mongoTemplate.count(query, Contact.class);
            byId = new PageImpl<Contact>(post, pageable, count);
            for (int i = 0; i < byId.getContent().size(); i++) {
                //set group profile
                GroupDisplay group = groupRepository.findByIdIs(byId.getContent().get(i).getGroupId());
                if (group != null) {
                    byId.getContent().get(i).setGroupProfile(group);
                }
            }
        }
        return getOk(new BaseResponse(byId));

    }
    @CrossOrigin
    @RequestMapping(value = "/addContact", method = RequestMethod.POST)
    public BaseResponse addContact(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "imageBanner", required = false) MultipartFile imageBanner
            , @RequestParam(value = "imageLogo", required = false) MultipartFile imageLogo
            , @RequestParam(value = "name1", defaultValue = "", required = false) String name1
            , @RequestParam(value = "name2", defaultValue = "", required = false) String name2
            , @RequestParam(value = "description", defaultValue = "", required = false) String description
            , @RequestParam(value = "address", defaultValue = "", required = false) String address
            , @RequestParam(value = "phoneNumber1", defaultValue = "", required = false) String phoneNumber1
            , @RequestParam(value = "phoneNumber2", defaultValue = "", required = false) String phoneNumber2
            , @RequestParam(value = "email", defaultValue = "", required = false) String email
            , @RequestParam(value = "website", defaultValue = "", required = false) String website
            , @RequestParam(value = "latitude", defaultValue = "", required = false) String latitude
            , @RequestParam(value = "longitude", defaultValue = "", required = false) String longitude
            , @RequestParam(value = "facebook", defaultValue = "", required = false) String facebook
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId
            , @RequestParam(value = "fax", defaultValue = "", required = false) String fax
            , @RequestParam(value = "youtube", defaultValue = "", required = false) String youtube
            , @RequestParam(value = "line", defaultValue = "", required = false) String line


    ) throws PostExceptions {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        checkSuperAdminGroups(profile,groupId);
        Contact contact = new Contact();
        if (imageBanner != null) {
            contact.setImageBanner(thumbnail(imageBanner, profile, FOLDER_COVER));
        }
        if (imageLogo != null) {
            contact.setImageLogo(thumbnail(imageLogo, profile, FOLDER_IMAGE));
        }
        contact.setFax(fax);
        contact.setGroupId(groupId);
        contact.setName1(name1);
        contact.setName2(name2);
        contact.setAddress(address);
        contact.setDescription(description);
        contact.setPhoneNumber1(phoneNumber1);
        contact.setPhoneNumber2(phoneNumber2);
        contact.setWebsite(website);
        contact.setEmail(email);
        contact.setLatitude(latitude);
        contact.setFacebook(facebook);
        contact.setLine(line);
        contact.setLongitude(longitude);
        contact.setYoutube(youtube);
        contact.setEditBy(profile.getUserName());
        Contact insert = contactRepository.insert(contact);

        return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), insert));
    }

    @CrossOrigin
    @RequestMapping(value = "/editContact", method = RequestMethod.POST)
    public BaseResponse editContact(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = "") String token
            , @RequestParam(value = "id", defaultValue = "") String id
            , @RequestParam(value = "imageBanner", required = false) MultipartFile imageBanner
            , @RequestParam(value = "icon", required = false) MultipartFile icon
            , @RequestParam(value = "name1", defaultValue = "", required = false) String name1
            , @RequestParam(value = "name2", defaultValue = "", required = false) String name2
            , @RequestParam(value = "address", defaultValue = "", required = false) String address
            , @RequestParam(value = "description", defaultValue = "", required = false) String description
            , @RequestParam(value = "phoneNumber1", defaultValue = "", required = false) String phoneNumber1
            , @RequestParam(value = "phoneNumber2", defaultValue = "", required = false) String phoneNumber2
            , @RequestParam(value = "email", defaultValue = "", required = false) String email
            , @RequestParam(value = "website", defaultValue = "", required = false) String website
            , @RequestParam(value = "latitude", defaultValue = "", required = false) String latitude
            , @RequestParam(value = "longitude", defaultValue = "", required = false) String longitude
            , @RequestParam(value = "facebook", defaultValue = "", required = false) String facebook
            , @RequestParam(value = "imageLogo", required = false) MultipartFile imageLogo
            , @RequestParam(value = "fax", defaultValue = "", required = false) String fax
            , @RequestParam(value = "youtube", defaultValue = "", required = false) String youtube
            , @RequestParam(value = "line", defaultValue = "", required = false) String line

    ) throws PostExceptions {
        initialize(request);

        Profile profile = userValidateToken(token, request);

        Contact contact = contactRepository.findById(id).get();
        checkSuperAdminGroups(profile,contact.getGroupId());

        if (imageBanner != null) {
            contact.setImageBanner(thumbnail(imageBanner, profile, FOLDER_COVER));
        }
        if (imageLogo != null) {
            contact.setImageLogo(thumbnail(imageLogo, profile, FOLDER_COVER));
        }
        contact.setName1(name1);
        contact.setName2(name2);
        contact.setAddress(address);
        contact.setDescription(description);
        contact.setPhoneNumber1(phoneNumber1);
        contact.setYoutube(youtube);
        contact.setPhoneNumber2(phoneNumber2);
        contact.setWebsite(website);
        contact.setEmail(email);
        contact.setLatitude(latitude);
        contact.setFax(fax);
        contact.setLine(line);
        contact.setLongitude(longitude);
        contact.setFacebook(facebook);
        contact.setEditBy(profile.getUserName());
        contact.setUpdateDate(new Date());
        Contact insert = contactRepository.save(contact);

        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), insert));
    }
    @CrossOrigin
    @RequestMapping(value = "/deleteContact", method = RequestMethod.POST)
    public BaseResponse deleteGroup(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        Contact byId = contactRepository.findById(id).get();
        checkSuperAdminGroups(profile,byId.getGroupId());
        contactRepository.delete(byId);
        return getOk(new BaseResponse(byId));
    }

}
