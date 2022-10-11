package com.zti.kha.api;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.User.Group;
import com.zti.kha.model.User.Profile;
import com.zti.kha.model.User.ReadGroup;
import com.zti.kha.utility.ErrorFactory;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by S on 9/22/2016.
 */
@RestController
public class GroupApi extends CommonApi {
    @CrossOrigin
    @ApiOperation(value = "กลุ่ม", notes = "", response = Group.class)
    @RequestMapping(value = "/getGroup", method = RequestMethod.GET)
    public BaseResponse getGroup(HttpServletRequest request
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "enable", defaultValue = "1", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String enable
            , @RequestParam(value = "isPrivate", defaultValue = "0", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String isPrivate
            , @RequestParam(value = "isDefault", defaultValue = "0", required = false) @ApiParam(value = "0=not filter,1=true,2=false") String isDefault
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord

    ) throws PostExceptions {
        initialize(request);

        if (id.length() != 0) {
            Optional<Group> byId = groupRepository.findById(id);
            if (byId.isPresent() == false) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
            }
            return getOk(new BaseResponse(byId.get()));
        } else {
            Query query = new Query().with( Sort.by("sequenceNo").ascending().and(Sort.by("name").ascending()));
            if (enable.equals("1")) {
                query.addCriteria(Criteria.where("enable").is(true));
            } else if (enable.equals("2")) {
                query.addCriteria(Criteria.where("enable").is(false));
            }

            if (isPrivate.equals("1")) {
                query.addCriteria(Criteria.where("isPrivate").is(true));
            } else if (isPrivate.equals("2")) {
                query.addCriteria(Criteria.where("isPrivate").is(false));
            }
            if (isDefault.equals("1")) {
                query.addCriteria(Criteria.where("isDefault").is(true));
            } else if (isDefault.equals("2")) {
                query.addCriteria(Criteria.where("isDefault").is(false));
            }
            Criteria name = Criteria.where("name").regex(keyWord, "i");
            if (keyWord != null && keyWord.length() > 0) {
                query.addCriteria(new Criteria().orOperator(name));
            }
            List<Group> post = mongoTemplate.find(query, Group.class);
            for (int i = 0; i < post.size(); i++) {
                post.get(i).setProvinceName(getProvinceName(post.get(i).getProvinceCode()));
                post.get(i).setDistrictName(getDistrictName(post.get(i).getDistrictCode()));
                post.get(i).setSubDistrictName(getSubDistrictName(post.get(i).getSubDistrictCode()));

            }
            return getOk(new BaseResponse(post));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/addEditGroup", method = RequestMethod.POST)
    public BaseResponse addGroup(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = false) String id
            , @RequestParam(value = "name", defaultValue = "", required = true) String name
            , @RequestParam(value = "icon", required = false) MultipartFile icon
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "enable", defaultValue = "true", required = false) boolean enable
            , @RequestParam(value = "isPrivate", defaultValue = "false", required = false) boolean isPrivate
            , @RequestParam(value = "isMain", defaultValue = "false", required = false) boolean isMain
            , @RequestParam(value = "provinceCode", defaultValue = "") String provinceCode
            , @RequestParam(value = "districtCode", defaultValue = "", required = false) String districtCode
            , @RequestParam(value = "subDistrictCode", defaultValue = "", required = false) String subDistrictCode
            , @RequestParam(value = "latitude", defaultValue = "0", required = false) double latitude
            , @RequestParam(value = "longitude", defaultValue = "0", required = false) double longitude
    ) throws PostExceptions {

        initialize(request);

        Profile profile = userValidateToken(token, request);
        Group group = new Group();

        if (id.length() > 0) {
            Optional<Group> byId = groupRepository.findById(id);
            if (byId.isPresent() == false) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
            }
            group = byId.get();

            if (!group.getName().equals(name)){
                Group byName = groupRepository.findByName(name);
                if (byName != null) {
                    return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateGroup()));
                }
            }
        } else {
            Group byName = groupRepository.findByName(name);
            if (byName != null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateGroup()));
            }
        }


        if (icon != null) {
            String iconImagePath = saveFile(icon, FILE_TYPE_IMAGE, profile, FOLDER_IMAGE);
            if (iconImagePath != null || iconImagePath.length() > 0) {
                group.setIcon(iconImagePath);
            }
        }
        group.setProvinceCode(provinceCode);
        group.setDistrictCode(districtCode);
        group.setSubDistrictCode(subDistrictCode);
        group.setLatitude(latitude);
        group.setLongitude(longitude);
        group.setName(name);
        if (sequence == 0) {
            group.setSequenceNo(9999);
        } else {
            group.setSequenceNo(sequence);
        }
        group.setEditBy(profile.getUserName());
        group.setUpdateDate(new Date());
        group.setEnable(enable);
        group.setMain(isMain);
        group.setPrivate(isPrivate);
        if (id.length() > 0) {
            checkSuperAdminGroups(profile,id);
            Group save = groupRepository.save(group);
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));

        } else {
            checkSuperAdmin(profile);
            Group insert = groupRepository.insert(group);
            return getOk(new BaseResponse(OK, localizeText.getPostUploaded(), insert));
        }
    }
    @CrossOrigin
    @ApiOperation(value = "ติดตามกลุ่ม ", notes = "", response = ReadGroup.class)
    @RequestMapping(value = "/subscribeGroup", method = RequestMethod.POST)
    public BaseResponse subscribeGroup(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        Optional<Group> byId = groupRepository.findById(groupId);
        if (byId.isPresent() == true) {

            for (int i = 0;i<profile.getReadGroups().size();i++) {
                if (profile.getReadGroups().get(i).getGroupId().contains(groupId)){
                    return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateSubscribe()));
                }

            }
            for (int i = 0;i<profile.getPendingGroups().size();i++) {
                if (profile.getPendingGroups().get(i).getGroupId().equals(groupId)){
                    return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicatePending()));
                }

            }
            if (byId.get().isPrivate() == false) {
                    profile.getReadGroups().add(new ReadGroup(groupId));
            } else {
                profile.getPendingGroups().add(new ReadGroup(groupId));
            }
            Profile save = profileRepository.save(profile);
            save.setSecret("");
            save.setReadGroups(setGroupName(save.getReadGroups()));
            save.setPendingGroups(setGroupName(save.getPendingGroups()));
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(),save));

        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
        }

    }
    @CrossOrigin
    @ApiOperation(value = "ยกเลิกติดตามกลุ่ม ", notes = "", response = ReadGroup.class)
    @RequestMapping(value = "/unScribeGroup", method = RequestMethod.POST)
    public BaseResponse unScribeGroup(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        Optional<Group> byId = groupRepository.findById(groupId);
        if (byId.isPresent() == true) {
            if (byId.get().isMain()==true){
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoUnScribeGroup()));

            }else {
                for (int i = 0; i < profile.getReadGroups().size(); i++) {
                    if (profile.getReadGroups().get(i).getGroupId().equals(groupId)) {
                        profile.getReadGroups().remove(i);
                        profileRepository.save(profile);
                        break;
                    }
                }

                profile.setSecret("");
                profile.setReadGroups(setGroupName(profile.getReadGroups()));
                profile.setPendingGroups(setGroupName(profile.getPendingGroups()));
                return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), profile));

            }
        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
        }

    }
  /*  @CrossOrigin
    @ApiOperation(value = "ติดตามกลุ่มสำหรับเจ้าหน้าที่", notes = "", response = ReadGroup.class)
    @RequestMapping(value = "/subscribeGroupAdmin", method = RequestMethod.POST)
    public BaseResponse subscribeGroupAdmin(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "userId", defaultValue = "") String userId
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId) throws PostExceptions {

        initialize(request);
        Profile admin = userValidateToken(token, request);
        checkAdminGroups(admin,groupId);
        Profile profile = userValidateId(userId);

        Optional<Group> byId = groupRepository.findById(groupId);
        if (byId.isPresent() == true) {

            for (int i = 0;i<profile.getReadGroups().size();i++) {
                if (profile.getReadGroups().get(i).getGroupId().contains(groupId)){
                    return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateSubscribe()));
                }

            }
            for (int i = 0;i<profile.getPendingGroups().size();i++) {
                if (profile.getPendingGroups().get(i).getGroupId().equals(groupId)){
                    return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicatePending()));
                }

            }
            profile.getReadGroups().add(new ReadGroup(admin.getUserName(),groupId));

            Profile save = profileRepository.save(profile);
            save.setSecret("");
            save.setReadGroups(setGroupName(save.getReadGroups()));
            save.setPendingGroups(setGroupName(save.getPendingGroups()));
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(),save));

        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
        }

    }*/
    @CrossOrigin
    @ApiOperation(value = "ยกเลิกติดตามกลุ่มสำหรับเจ้าหน้าที่", notes = "", response = ReadGroup.class)
    @RequestMapping(value = "/unScribeGroupAdmin", method = RequestMethod.POST)
    public BaseResponse unScribeGroupAdmin(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "userId", defaultValue = "") String userId
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId) throws PostExceptions {

        initialize(request);
        Profile admin =userValidateToken(token, request);
        checkAdminGroups(admin,groupId);
        Profile profile = userValidateId(userId);
        Optional<Group> byId = groupRepository.findById(groupId);
        if (byId.isPresent() == true) {
            if (byId.get().isMain()==true){
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoUnScribeGroup()));

            }else {
                for (int i = 0; i < profile.getReadGroups().size(); i++) {
                    if (profile.getReadGroups().get(i).getGroupId().equals(groupId)) {
                        profile.getReadGroups().remove(i);
                        profileRepository.save(profile);
                        break;
                    }
                }

                profile.setSecret("");
                profile.setReadGroups(setGroupName(profile.getReadGroups()));
                profile.setPendingGroups(setGroupName(profile.getPendingGroups()));
                return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), profile));
            }
        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
        }

    }
    @CrossOrigin
    @ApiOperation(value = "ยกเลิกติดตามกลุ่มที่รออนุมัติ", notes = "", response = ReadGroup.class)
    @RequestMapping(value = "/unScribeGroupPending", method = RequestMethod.POST)
    public BaseResponse unScribeGroupPending(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "groupId", defaultValue = "", required = true) String groupId) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        Optional<Group> byId = groupRepository.findById(groupId);
        if (byId.isPresent() == true) {
            for (int i = 0;i<profile.getPendingGroups().size();i++) {
                if (profile.getPendingGroups().get(i).getGroupId().equals(groupId)){
                    profile.getPendingGroups().remove(i);
                    profileRepository.save(profile);
                    break;
                }
            }

            profile.setSecret("");
            profile.setReadGroups(setGroupName(profile.getReadGroups()));
            profile.setPendingGroups(setGroupName(profile.getPendingGroups()));
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(),profile));

        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
        }

    }
    @CrossOrigin
    @ApiOperation(value = "รายชื่อกลุ่มที่เข้าร่วม", notes = "", response = ReadGroup.class)
    @RequestMapping(value = "/getReadGroups", method = RequestMethod.GET)
    public BaseResponse getReadGroups(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token


    ) throws PostExceptions {

        initialize(request);
        Profile adminProfile = userValidateToken(token, request);
        List<ReadGroup> readGroups = adminProfile.getReadGroups();
        List<ReadGroup> newsList = new ArrayList<>();
        for (int i = 0; i < readGroups.size(); i++) {
            Optional<Group> byId = groupRepository.findById(readGroups.get(i).getGroupId());
            if (byId.isPresent()==true){
                if (byId.get().isEnable()==true) {
                    readGroups.get(i).setGroupName(byId.get().getName());
                    readGroups.get(i).setGroupIcon(byId.get().getIcon());
                    readGroups.get(i).setProvinceName(getProvinceName(byId.get().getProvinceCode()));
                    readGroups.get(i).setDistrictName(getDistrictName(byId.get().getDistrictCode()));
                    readGroups.get(i).setSubDistrictName(getSubDistrictName(byId.get().getSubDistrictCode()));

                    readGroups.get(i).setDefault(byId.get().isMain());

                    newsList.add(readGroups.get(i));
                }
            }

        }
        return getOk(new BaseResponse( newsList));
    }
    @CrossOrigin
    @ApiOperation(value = "รายชื่อกลุ่มที่เข้าร่วมรออนุมัติ", notes = "", response = ReadGroup.class)
    @RequestMapping(value = "/getPendingGroups", method = RequestMethod.GET)
    public BaseResponse getPendingGroups(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
    ) throws PostExceptions {

        initialize(request);
        Profile adminProfile = userValidateToken(token, request);
        List<ReadGroup> pendingGroups = adminProfile.getPendingGroups();
        List<ReadGroup> newsList = new ArrayList<>();
        for (int i = 0; i < pendingGroups.size(); i++) {
            Optional<Group> byId = groupRepository.findById(pendingGroups.get(i).getGroupId());
            if (byId.isPresent()==true){
                if (byId.get().isEnable()==true) {
                    pendingGroups.get(i).setGroupName(byId.get().getName());
                    pendingGroups.get(i).setGroupIcon(byId.get().getIcon());
                    pendingGroups.get(i).setProvinceName(getProvinceName(byId.get().getProvinceCode()));
                    pendingGroups.get(i).setDistrictName(getDistrictName(byId.get().getDistrictCode()));
                    pendingGroups.get(i).setSubDistrictName(getSubDistrictName(byId.get().getSubDistrictCode()));

                    pendingGroups.get(i).setDefault(byId.get().isMain());
                    newsList.add(pendingGroups.get(i));
                }
            }

        }
        return getOk(new BaseResponse( newsList));
    }
    @CrossOrigin
    @RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
    public BaseResponse deleteGroup(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);

        checkSuperAdmin(profile);
        Group byId = groupRepository.findById(id).get();
        groupRepository.delete(byId);
        return getOk(new BaseResponse(byId));
    }




}
