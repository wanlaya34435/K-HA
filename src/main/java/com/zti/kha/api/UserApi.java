package com.zti.kha.api;

import com.zti.kha.model.*;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.User.*;
import com.zti.kha.security.Rfc2898DeriveBytes;
import com.zti.kha.utility.ErrorFactory;
import com.zti.kha.utility.PostExceptions;
import com.zti.kha.utility.SSOToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by S on 9/22/2016.
 */
@RestController
public class UserApi extends CommonApi {

    static Logger log = LogManager.getLogger(SSOToken.class);
    public UserApi() throws Exception {
    }
  /*  @CrossOrigin
    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public BaseResponse generate(HttpServletRequest request) throws PostExceptions, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        initialize(request);
        for (int i =21;i<41;i++){
            Profile profile = new Profile();
            RoleAdmin roleAdmin = new RoleAdmin();
            roleAdmin.setSuperAdmin(true);
            profile.setRole(roleAdmin);
            profile.setSecret(createPassword("123456789"));
            String username ="";
            if (i<10){
                username = "superKHA0" + i;
            }else {
                username = "superKHA" + i;
            }

            profile.setUserName(username);
            profile.setFirstName(username);
            profile.setLastName("SuperAdmin");
            profile.setIdCard("");
            profile.setAmountResidents(100);
            profile.setType(1);
            profile.setJob("เจ้าหน้าที่");
            profile.setKhaId("");
            profile.setAddress("");
            profile.setProvinceCode("");
            profile.setDistrictCode("");
            profile.setSubDistrictCode("");
            profile.setEndContract(null);
            List<ReadGroup> readList = new ArrayList<>();
            //set default group
            List<Group> byDefaultIs = groupRepository.findByMain(true);
            for (Group group : byDefaultIs) {
                ReadGroup readGroup = new ReadGroup(group.getId());
                readList.add(readGroup);
            }
            profile.setReadGroups(readList);

            profile.setEmail("");
            profile.setPhoneNumber("");
            profile.setPermissionMenu("");
            profile.setPermissionButton("");

            Profile insert = profileRepository.insert(profile);
        }
        for (int i =1;i<41;i++){
            Profile profile = new Profile();
            profile.setSecret(createPassword("123456789"));
            String username ="";
            if (i<10){
                username = "userKHA0" + i;
            }else {
                username = "userKHA" + i;
            }

            profile.setUserName(username);
            profile.setFirstName(username);
            profile.setLastName("User");
            profile.setIdCard("");
            profile.setAmountResidents(100);
            profile.setType(1);
            profile.setJob("พนักงานเอกชน");
            profile.setKhaId("");
            profile.setAddress("");
            profile.setProvinceCode("");
            profile.setDistrictCode("");
            profile.setSubDistrictCode("");
            profile.setEndContract(null);
            List<ReadGroup> readList = new ArrayList<>();
            //set default group
            List<Group> byDefaultIs = groupRepository.findByMain(true);
            for (Group group : byDefaultIs) {
                ReadGroup readGroup = new ReadGroup(group.getId());
                readList.add(readGroup);
            }
            profile.setReadGroups(readList);

            profile.setEmail("");
            profile.setPhoneNumber("");
            profile.setPermissionMenu("");
            profile.setPermissionButton("");

            Profile insert = profileRepository.insert(profile);
        }

        return getOk(new BaseResponse());
    }*/

  @CrossOrigin
  @ApiOperation(value = "แอดมินเพิ่มสมาชิก", notes = "", response = Profile.class)
  @RequestMapping(value = "/addUser", method = {RequestMethod.POST})
  public BaseResponse addUser(HttpServletRequest request,
                               @RequestHeader(value = "token", defaultValue = "")  @ApiParam(value = "")String token,
                               @RequestParam(value = "password", defaultValue = "", required = false) String password,
                               @RequestParam(value = "phoneNumber", defaultValue = "", required = true) String phoneNumber,
                               @RequestParam(value = "username", defaultValue = "", required = false) String userName,
                               @RequestParam(value = "email", defaultValue = "", required = false) String email,
                               @RequestParam(value = "firstName", defaultValue = "", required = false) String firstName,
                               @RequestParam(value = "lastName", defaultValue = "", required = false) String lastName,
                               @RequestParam(value = "imageProfile", defaultValue = "", required = false) String imageProfile,
                               @RequestParam(value = "permissionButton", defaultValue = "", required = false) String permissionButton,
                               @RequestParam(value = "permissionMenu", defaultValue = "", required = false) String permissionMenu,
                               @RequestParam(value = "groupId", defaultValue = "")  @ApiParam(value = "") String groupId,
                               @RequestParam(value = "idCard", defaultValue = "", required = false) String idCard,
                               @RequestParam(value = "amountResidents", defaultValue = "0", required = false) int amountResidents,
                               @RequestParam(value = "type", defaultValue = "1", required = false)@ApiParam(value = "1=กลุ่มคนโสด,2=กลุ่มคนทำงาน,3=กลุ่มครอบครัว,4=ผู้สูงอายุ/ผู้พิการ") int type,
                               @RequestParam(value = "job", defaultValue = "", required = false) String job,
                               @RequestParam(value = "khaId", defaultValue = "", required = false) String khaId,
                               @RequestParam(value = "address", defaultValue = "", required = false) String address,
                               @RequestParam(value = "provinceCode", defaultValue = "", required = false) String provinceCode,
                               @RequestParam(value = "districtCode", defaultValue = "", required = false) String districtCode,
                               @RequestParam(value = "subDistrictCode", defaultValue = "", required = false) String subDistrictCode,
                               @RequestParam(value = "endContract", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endContract
  )throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, PostExceptions {

      initialize(request);
      Profile profile = new Profile();
      Profile adminProfile = new Profile();
          adminProfile = userValidateToken(token, request);
          checkSuperAdminGroups(adminProfile,groupId);


      if ( userName != null&&!userName.equals("")&& profileRepository.findByUserNameIgnoreCase(userName) != null  ) {
          return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateUserName()));
      } else if (phoneNumber.length() > 0 && phoneNumber.length() != 10&& profileRepository.findByPhoneNumber(phoneNumber) != null ) {
          return getError(ErrorFactory.getError(FAILED, localizeText.getWrongPhoneNumber()));
      }else if (phoneNumber.length() > 0 &&profileRepository.findByPhoneNumber(phoneNumber) != null ) {
          return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicatePhoneNumber()));
      } else if (email.length()>0&&profileRepository.findByEmailIgnoreCase(email)!= null ) {
          return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateEmail()));
      } else {
          if (password.length()>0) {
              profile.setSecret(createPassword(password));
          }
          profile.setUserName(userName);
          profile.setFirstName(firstName);
          profile.setLastName(lastName);
          profile.setIdCard(idCard);
          profile.setAmountResidents(amountResidents);
          profile.setType(type);
          profile.setJob(job);
          profile.setKhaId(khaId);
          profile.setAddress(address);
          profile.setProvinceCode(provinceCode);
          profile.setDistrictCode(districtCode);
          profile.setSubDistrictCode(subDistrictCode);

          if (endContract.length() > 0) {
              if (endContract.equals("null")) {
                  profile.setEndContract(null);
              } else {
                  profile.setEndContract(new Date(Long.parseLong(endContract)));
              }
          }
          List<ReadGroup> readList = new ArrayList<>();
          //set default group
          List<Group> byDefaultIs = groupRepository.findByMain(true);
          for (Group group : byDefaultIs) {
              ReadGroup readGroup = new ReadGroup(group.getId());
              readList.add(readGroup);
          }

          if (groupId.length()>0) {
              Optional<Group> byId = groupRepository.findById(groupId);
              if (byId.isPresent() == true) {
                      ReadGroup readGroup = new ReadGroup( adminProfile.getUserName(),groupId);
                      readList.add(readGroup);
              } else {
                  return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
              }
          }

          profile.setReadGroups(readList);
          profile.setEmail(email);
          profile.setPhoneNumber(phoneNumber);
          profile.setPermissionMenu(permissionMenu);
          profile.setPermissionButton(permissionButton);

          if (imageProfile.length() > 0) {
              profile.setImageProfile(imageProfile);
          }

          Profile insert = profileRepository.insert(profile);
          insert.setProvinceName(getProvinceName(insert.getProvinceCode()));
          insert.setDistrictName(getDistrictName(insert.getDistrictCode()));
          insert.setSubDistrictName(getSubDistrictName(insert.getSubDistrictCode()));
          insert.setZipcode(getZipcode(insert.getSubDistrictCode()));
          insert.setKhaProfile(getGroupProfile(insert.getKhaId()));
          return getOk(new BaseResponse(OK, localizeText.getRegisterSucceed(), insert));
      }
  }
    @CrossOrigin
    @ApiOperation(value = "สมัครสมาชิก", notes = "", response = Profile.class)
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public BaseResponse register(HttpServletRequest request,
                                   @RequestParam(value = "password", defaultValue = "", required = false) String password,
                                   @RequestParam(value = "phoneNumber", defaultValue = "", required = true) String phoneNumber,
                                   @RequestParam(value = "username", defaultValue = "", required = false) String userName,
                                   @RequestParam(value = "email", defaultValue = "", required = false) String email,
                                   @RequestParam(value = "firstName", defaultValue = "", required = false) String firstName,
                                   @RequestParam(value = "lastName", defaultValue = "", required = false) String lastName,
                                   @RequestParam(value = "imageProfile", defaultValue = "", required = false) String imageProfile,
                                   @RequestParam(value = "permissionButton", defaultValue = "", required = false) String permissionButton,
                                   @RequestParam(value = "permissionMenu", defaultValue = "", required = false) String permissionMenu,
                                   @RequestParam(value = "groupId", defaultValue = "", required = false)  @ApiParam(value = "") String groupId,
                                   @RequestParam(value = "idCard", defaultValue = "", required = false) String idCard,
                                   @RequestParam(value = "amountResidents", defaultValue = "0", required = false) int amountResidents,
                                   @RequestParam(value = "type", defaultValue = "1", required = false)@ApiParam(value = "1=กลุ่มคนโสด,2=กลุ่มคนทำงาน,3=กลุ่มครอบครัว,4=ผู้สูงอายุ/ผู้พิการ") int type,
                                   @RequestParam(value = "job", defaultValue = "", required = false) String job,
                                   @RequestParam(value = "khaId", defaultValue = "", required = false) String khaId,
                                   @RequestParam(value = "address", defaultValue = "", required = false) String address,
                                   @RequestParam(value = "provinceCode", defaultValue = "", required = false) String provinceCode,
                                   @RequestParam(value = "districtCode", defaultValue = "", required = false) String districtCode,
                                   @RequestParam(value = "subDistrictCode", defaultValue = "", required = false) String subDistrictCode,
                                   @RequestParam(value = "endContract", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endContract
                                 )throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, PostExceptions {

        initialize(request);
        Profile profile = new Profile();
        Profile adminProfile = new Profile();


        if ( userName != null&&!userName.equals("")&& profileRepository.findByUserNameIgnoreCase(userName) != null  ) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateUserName()));
        } else if (phoneNumber.length() > 0 && phoneNumber.length() != 10&& profileRepository.findByPhoneNumber(phoneNumber) != null ) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getWrongPhoneNumber()));
        }else if (phoneNumber.length() > 0 &&profileRepository.findByPhoneNumber(phoneNumber) != null ) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicatePhoneNumber()));
        } else if (email.length()>0&&profileRepository.findByEmailIgnoreCase(email)!= null ) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateEmail()));
        } else {
            if (password.length()>0) {
                profile.setSecret(createPassword(password));
            }
            profile.setUserName(userName);
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setIdCard(idCard);
            profile.setAmountResidents(amountResidents);
            profile.setType(type);
            profile.setJob(job);
            profile.setKhaId(khaId);
            profile.setAddress(address);
            profile.setProvinceCode(provinceCode);
            profile.setDistrictCode(districtCode);
            profile.setSubDistrictCode(subDistrictCode);

            if (endContract.length() > 0) {
                if (endContract.equals("null")) {
                    profile.setEndContract(null);
                } else {
                    profile.setEndContract(new Date(Long.parseLong(endContract)));
                }
            }
            List<ReadGroup> readList = new ArrayList<>();
            //set default group
            List<Group> byDefaultIs = groupRepository.findByMain(true);
                for (Group group : byDefaultIs) {
                    ReadGroup readGroup = new ReadGroup(group.getId());
                    readList.add(readGroup);
                }

            if (groupId.length()>0) {
                Optional<Group> byId = groupRepository.findById(groupId);
                if (byId.isPresent() == true) {



                        ReadGroup readGroup = new ReadGroup(groupId);
                        if (byId.get().isPrivate() == false) {
                            readList.add(readGroup);
                        } else {
                            List<ReadGroup> pendingList = new ArrayList<>();
                            pendingList.add(readGroup);

                            profile.setPendingGroups(pendingList);
                        }

                } else {
                    return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
                }
            }
            profile.setReadGroups(readList);

            profile.setEmail(email);
            profile.setPhoneNumber(phoneNumber);
            profile.setPermissionMenu(permissionMenu);
            profile.setPermissionButton(permissionButton);

            if (imageProfile.length() > 0) {
                profile.setImageProfile(imageProfile);
            }

            Profile insert = profileRepository.insert(profile);
            insert.setProvinceName(getProvinceName(insert.getProvinceCode()));
            insert.setDistrictName(getDistrictName(insert.getDistrictCode()));
            insert.setSubDistrictName(getSubDistrictName(insert.getSubDistrictCode()));
            insert.setZipcode(getZipcode(insert.getSubDistrictCode()));
            insert.setKhaProfile(getGroupProfile(insert.getKhaId()));
            return getOk(new BaseResponse(OK, localizeText.getRegisterSucceed(), insert));
        }
    }


    @CrossOrigin
    @ApiOperation(value = "แก้ไขข้อมูลสมาชิก", notes = "", response = Profile.class)
    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public BaseResponse editProfile(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id
            , @RequestParam(value = "phoneNumber", defaultValue = "", required = false) String phoneNumber
            , @RequestParam(value = "firstName", defaultValue = "", required = false) String firstName
            , @RequestParam(value = "lastName", defaultValue = "", required = false) String lastName
            , @RequestParam(value = "imageProfile", defaultValue = "", required = false) String imageProfile
            , @RequestParam(value = "permissionMenu", defaultValue = "", required = false) String permissionMenu
            , @RequestParam(value = "permissionButton", defaultValue = "", required = false) String permissionButton
            , @RequestParam(value = "groupId", defaultValue = "", required = false)@ApiParam(value = "") List<String> groupId
            , @RequestParam(value = "groupIdDelete", defaultValue = "", required = false) @ApiParam(value = "for admin")List<String> groupIdDelete
            , @RequestParam(value = "email", defaultValue = "", required = false) String email
            , @RequestParam(value = "idCard", defaultValue = "", required = false) String idCard
            , @RequestParam(value = "amountResidents", defaultValue = "0", required = false) int amountResidents
            , @RequestParam(value = "type", defaultValue = "1", required = false)@ApiParam(value = "1=กลุ่มคนโสด,2=กลุ่มคนทำงาน,3=กลุ่มครอบครัว,4=ผู้สูงอายุ/ผู้พิการ") int type
            , @RequestParam(value = "job", defaultValue = "", required = false) String job
            , @RequestParam(value = "khaId", defaultValue = "", required = false) String khaId
            , @RequestParam(value = "address", defaultValue = "", required = false) String address
            , @RequestParam(value = "provinceCode", defaultValue = "", required = false) String provinceCode
            , @RequestParam(value = "districtCode", defaultValue = "", required = false) String districtCode
            , @RequestParam(value = "subDistrictCode", defaultValue = "", required = false) String subDistrictCode
            , @RequestParam(value = "endContract", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endContract

    ) throws PostExceptions {


        initialize(request);

        Profile adminProfile = userValidateToken(token, request);
        Profile profile = userValidateId(id);
        if (!adminProfile.getId().equals(profile.getId())) {
            //check superAdmin permission
            if (profile.getRole() != null && profile.getRole().getSuperAdmin() == true) {
                if (adminProfile.getRole() == null) {
                    throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
                } else {
                    if (adminProfile.getRole().getSuperAdmin() == false) {
                        throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
                    }

                }

            }
        }

        if (!email.equals(profile.getEmail())) {
            if (email.length() > 0 && profileRepository.findByEmailIgnoreCase(email) != null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicateEmail()));
            } else {
                profile.setEmail(email);
            }
        }
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setPhoneNumber(phoneNumber);
            profile.setIdCard(idCard);
            profile.setAmountResidents(amountResidents);
            profile.setType(type);
            profile.setJob(job);
            profile.setKhaId(khaId);
            profile.setAddress(address);
            profile.setProvinceCode(provinceCode);
            profile.setDistrictCode(districtCode);
            profile.setSubDistrictCode(subDistrictCode);
        if (endContract.length() > 0) {
            if (endContract.equals("null")) {
                profile.setEndContract(null);
            } else {
                profile.setEndContract(new Date(Long.parseLong(endContract)));
            }
        }
            profile.setPermissionMenu(permissionMenu);
            profile.setPermissionButton(permissionButton);

            if (imageProfile != null && imageProfile.length() > 0) {
                profile.setImageProfile(imageProfile);
            }
        List<ReadGroup>list=profile.getReadGroups();

        if (groupId.size()>0) {
            for (int i=0;i<groupId.size();i++){
                checkSuperAdminGroups(adminProfile,groupId.get(i));
                Optional<Group> byId = groupRepository.findById(groupId.get(i));
                if (byId.isPresent() == true) {
                    ReadGroup readGroup = new ReadGroup( adminProfile.getUserName(),groupId.get(i));
                    list.add(readGroup);
                } else {
                    return getError(ErrorFactory.getError(FAILED, localizeText.getNoGroup()));
                }
            }
        }

        if (groupIdDelete.size()>0) {
            for (int i=0;i<groupIdDelete.size();i++){
                checkSuperAdminGroups(adminProfile,groupIdDelete.get(i));
                for (int j=0;j<list.size();j++) {
                    if (list.get(j).getGroupId().equals(groupIdDelete.get(i))){
                        list.remove(j);
                        break;
                    }
                }
            }
        }


        profile.setReadGroups(list);
        profile.setEditBy(adminProfile.getUserName());
        profile.setUpdateDate(new Date());
        Profile save = profileRepository.save(profile);

        save.setProvinceName(getProvinceName(save.getProvinceCode()));
        save.setDistrictName(getDistrictName(save.getDistrictCode()));
        save.setSubDistrictName(getSubDistrictName(save.getSubDistrictCode()));
        save.setZipcode(getZipcode(save.getSubDistrictCode()));

        save.setKhaProfile(getGroupProfile(save.getKhaId()));

        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));

    }
    @CrossOrigin
    @RequestMapping(value = "/checkPhoneNumber", method = RequestMethod.GET)
    public BaseResponse checkPhoneNumber(HttpServletRequest request
            , @RequestParam(value = "phoneNumber", defaultValue = "", required = true) String phoneNumber) {

        initialize(request);
         if (profileRepository.findByPhoneNumber(phoneNumber) != null ) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicatePhoneNumber()));
        }
        return getOk(new BaseResponse());
    }
    @CrossOrigin
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public BaseResponse deleteUser(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id) throws PostExceptions {

        initialize(request);
        Profile adminProfile = userValidateToken(token, request);
        checkSuperAdmin(adminProfile);
        Profile byId = userValidateId(id);

        profileRepository.delete(byId);
        List<LogLogin> byUserId = logLoginRepository.findByUserId(id);
        logLoginRepository.deleteAll(byUserId);
        return getOk(new BaseResponse(OK, localizeText.getDeleted()));
    }

    @CrossOrigin
    @RequestMapping(value = "/manageStatusUser", method = RequestMethod.POST)
    public BaseResponse manageStatusUser(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id
            , @RequestParam(value = "status", defaultValue = "", required = true) boolean status) throws PostExceptions {

        initialize(request);
        Profile adminProfile = userValidateToken(token, request);

        Profile byId = profileRepository.findById(id).get();
        checkAdminGroupsList(adminProfile,byId.getReadGroups());

        byId.setEnable(status);
        byId.setEditBy(adminProfile.getUserName());

        Profile save = profileRepository.save(byId);
        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
    }


    @CrossOrigin
    @ApiOperation(value = "รายชื่อรออนุมัติกลุ่ม", notes = "", response = Profile.class)
    @RequestMapping(value = "/getPendingGroupsAdmin", method = RequestMethod.GET)
    public BaseResponse getPendingGroupsAdmin(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "groupId", defaultValue = "", required = false) String groupId
            , @RequestParam(value = "sizeContents", defaultValue = "25", required = false) int sizeContents
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord

    ) throws PostExceptions {

        initialize(request);
        userValidateToken(token, request);

        Pageable pageable =  PageRequest.of(page, sizeContents,  Sort.by("pendingGroups.createDate").ascending());
        Query query = new Query().with(pageable);
        query.addCriteria(Criteria.where("pendingGroups").ne(new ArrayList<>()));

        if (groupId.length()>0) {
            query.addCriteria(Criteria.where("pendingGroups.groupId").is(groupId));
        }

        if (keyWord != null && keyWord.length() > 0) {
            Criteria firstName = Criteria.where("firstName").regex(keyWord, "i");
            Criteria lastName = Criteria.where("lastName").regex(keyWord, "i");
            Criteria email = Criteria.where("email").regex(keyWord, "i");
            Criteria userName = Criteria.where("userName").regex(keyWord, "i");

            query.addCriteria(new Criteria().orOperator(firstName, lastName,email,userName));
        }
        List<Profile> post = mongoTemplate.find(query, Profile.class);
        long count = mongoTemplate.count(query, Profile.class);
        Page<Profile> profiles = new PageImpl<Profile>(post, pageable, count);
        for (int i = 0; i < profiles.getContent().size(); i++) {
            profiles.getContent().get(i).setReadGroups(setGroupName(profiles.getContent().get(i).getReadGroups()));
            profiles.getContent().get(i).setPendingGroups(setGroupName(profiles.getContent().get(i).getPendingGroups()));
            profiles.getContent().get(i).setSecret("");
        }
        return getOk(new BaseResponse( profiles));
    }


    @CrossOrigin
    @ApiOperation(value = "เจ้าหน้าที่จัดการรายชื่อรออนุมัติกลุ่ม", notes = "", response = Profile.class)
    @RequestMapping(value = "/approvePendingGroups", method = RequestMethod.POST)
    public BaseResponse managePendingGroups(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true)@ApiParam(value = "userId") String id
            , @RequestParam(value = "groupId", defaultValue = "", required = true)String groupId
            , @RequestParam(value = "approve", defaultValue = "true", required = true)@ApiParam(value = "true,false")boolean approve) throws PostExceptions {

        initialize(request);
        Profile adminProfile = userValidateToken(token, request);
        checkAdminGroups(adminProfile,groupId);

        Profile byId = profileRepository.findById(id).get();
        if (byId==null){
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoUserFound()));
        }else {
            for (int i = 0;i<byId.getPendingGroups().size();i++) {
                    if (byId.getPendingGroups().get(i).getGroupId().equals(groupId)) {
                        if (approve == true) {
                            byId.getReadGroups().add(new ReadGroup(adminProfile.getUserName(),groupId));
                            byId.getPendingGroups().remove(i);
                            if (byId.getEmail()!=null&&byId.getEmail().length()>0) {
                                sendEmailApprove(byId.getEmail(), "[myKHA] Approve new group");
                            }
                        } else {
                            byId.getPendingGroups().remove(i);
                        }
                         profileRepository.save(byId);
                        break ;
                    }
            }
            byId.setSecret("");
            byId.setReadGroups(setGroupName(byId.getReadGroups()));
            byId.setPendingGroups(setGroupName(byId.getPendingGroups()));
            if (byId.getRole() != null) {
                byId.getRole().setGroupsName(setRoleName(byId.getRole().getAdminGroups()));
                byId.getRole().setTechnicianName(setRoleName(byId.getRole().getTechnicianGroups()));

            }

            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), byId));
        }
    }
    @CrossOrigin
    @ApiOperation(value = "สิทธิ์สำหรับเจ้าหน้าที่", notes = "", response = Profile.class)
    @RequestMapping(value = "/manageAdmin", method = RequestMethod.POST)
    public BaseResponse manageAdmin(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id
            , @RequestParam(value = "groupId", defaultValue = "", required = false)@ApiParam(value = "groupId") List<String> groupId
            , @RequestParam(value = "groupIdDelete", defaultValue = "", required = false)@ApiParam(value = "groupId") List<String> groupIdDelete
            , @RequestParam(value = "techId", defaultValue = "", required = false)@ApiParam(value = "groupId") List<String> techId
            , @RequestParam(value = "techIdDelete", defaultValue = "", required = false)@ApiParam(value = "groupId") List<String> techIdDelete
    ) throws PostExceptions {

        initialize(request);
        Profile adminProfile = userValidateToken(token, request);
        Profile byId = profileRepository.findById(id).get();
        if (byId==null){
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoUserFound()));
        }else {
            //check superAdmin permission
            if (byId.getRole()!=null&&byId.getRole().getSuperAdmin()==true){
                if (adminProfile.getRole()==null){
                    throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
                }else {
                    if (byId.getRole().getSuperAdmin()==false){
                        throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
                    }

                }

            }

            if (groupId.size()>0) {
                for (String groupAdd:groupId) {
                    checkSuperAdminGroups(adminProfile, groupAdd);
                    RoleAdmin role = new RoleAdmin();
                    if (byId.getRole() != null) {
                        role = byId.getRole();
                    }

                    if (!role.getAdminGroups().contains(groupAdd)) {
                        role.getAdminGroups().add(groupAdd);
                        byId.setRole(role);
                    }
                }
            }
            if (groupIdDelete.size()>0) {
                for (String groupDelete:groupIdDelete) {
                    checkSuperAdminGroups(adminProfile, groupDelete);
                    byId.getRole().getAdminGroups().remove(groupDelete);
                }
            }
            if (techId.size()>0) {
                for (String groupAdd:techId) {
                    checkSuperAdminGroups(adminProfile, groupAdd);
                    RoleAdmin role = new RoleAdmin();
                    if (byId.getRole() != null) {
                        role = byId.getRole();
                    }
                    if (!role.getTechnicianGroups().contains(groupAdd)) {
                        role.getTechnicianGroups().add(groupAdd);
                        byId.setRole(role);
                    }
                }
            }
            if (techIdDelete.size()>0) {
                for (String groupDelete:techIdDelete) {
                    checkSuperAdminGroups(adminProfile, groupDelete);
                    byId.getRole().getTechnicianGroups().remove(groupDelete);

                }
            }

            //check no admin
            if ( byId.getRole()!=null&&byId.getRole().getSuperAdmin()==false&&byId.getRole().getAdminGroups().size() == 0&&byId.getRole().getTechnicianGroups().size() == 0) {
                byId.setRole(null);
            }

            //set ReadGroup
            if (byId.getRole()!=null) {
                for (String adminGroups: byId.getRole().getAdminGroups()) {
                  boolean isAdd = true;
                  for (ReadGroup readGroup:byId.getReadGroups()){
                      if (readGroup.getGroupId().equals(adminGroups)){
                          isAdd=false;
                      }
                  }
                    if (isAdd==true){
                        byId.getReadGroups().add(new ReadGroup(adminGroups));
                    }
                }
            }
            Profile profile = profileRepository.save(byId);
            profile.setSecret("");
            profile.setReadGroups(setGroupName(profile.getReadGroups()));
            profile.setPendingGroups(setGroupName(profile.getPendingGroups()));
            if (profile.getRole() != null) {
                profile.getRole().setGroupsName(setRoleName(profile.getRole().getAdminGroups()));
                profile.getRole().setTechnicianName(setRoleName(profile.getRole().getTechnicianGroups()));

            }
            return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), profile));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public BaseResponse forgotPassword(HttpServletRequest request
            , @RequestParam(value = "email", defaultValue = "", required = true) String email
    ) throws PostExceptions, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        initialize(request);
        Profile byEmail = profileRepository.findByEmailIgnoreCase(email);
        if (byEmail==null){
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoUserFound()));
        }else {
                sendEmailForget(email, "แจ้งเตือนจากแอปพลิเคชัน myKHA เรื่องลืมรหัสผ่าน");
                return getOk(new BaseResponse(OK, localizeText.getUpdatedForget()));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/changeNewPassword", method = RequestMethod.POST)
    public BaseResponse changeNewPassword(HttpServletRequest request
            , @RequestParam(value = "email", defaultValue = "") String email
            , @RequestParam(value = "newPassword", defaultValue = "", required = true) String newPassword
            , @RequestParam(value = "newPasswordConfirm", defaultValue = "", required = true) String newPasswordConfirm) throws NoSuchAlgorithmException, PostExceptions, UnsupportedEncodingException, InvalidKeyException {

       initialize(request);

       Profile byEmail = profileRepository.findByEmailIgnoreCase(email);
        if (byEmail!=null) {
            if (!newPassword.equals(newPasswordConfirm)) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getPasswordNotMatch()));
            }

            if (!(newPassword.length() > 0)) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoUpdate()));
            }
            Profile profile = byEmail;
            profile.setSecret(createPassword(newPassword));
            profile.setUpdateDate(new Date());
            profileRepository.save(profile);

            return getOk(new BaseResponse(OK, localizeText.getDataUpdated()));
        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoUserFound()));

        }

    }


    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }


    private Boolean checkEmail(String email) {
        Pattern ptr = Pattern.compile("(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)");

        return ptr.matcher(email).matches();

    }


    @CrossOrigin
    @RequestMapping(value = "/editPassword", method = RequestMethod.POST)
    public BaseResponse editPassword(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "oldPassword", defaultValue = "", required = true) String oldPassword
            , @RequestParam(value = "newPassword", defaultValue = "", required = true) String newPassword
            , @RequestParam(value = "newPasswordConfirm", defaultValue = "", required = true) String newPasswordConfirm) throws InvalidKeySpecException, NoSuchAlgorithmException, PostExceptions, UnsupportedEncodingException, InvalidKeyException {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        if (!checkPassword(oldPassword, profile.getSecret())) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getWrongPassword()));
        }
        if (oldPassword.equals(newPassword)) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicatePassword()));
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getPasswordNotMatch()));
        }
        if (!(newPassword.length() > 0)) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoUpdate()));
        }
        profile.setSecret(createPassword(newPassword));
        profile.setUpdateDate(new Date());
        Profile save = profileRepository.save(profile);
        save.setSecret("");
        save.setProvinceName(getProvinceName(save.getProvinceCode()));
        save.setDistrictName(getDistrictName(save.getDistrictCode()));
        save.setSubDistrictName(getSubDistrictName(save.getSubDistrictCode()));
        save.setZipcode(getZipcode(save.getSubDistrictCode()));
        save.setKhaProfile(getGroupProfile(save.getKhaId()));
        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
    }


    @CrossOrigin
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public BaseResponse editPassword(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id) throws PostExceptions, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {

        initialize(request);

        Profile profileAdmin = userValidateToken(token, request);
        checkAdmin(profileAdmin);
        Profile profile = userValidateId(id);

        profile.setSecret(createPassword("123456789"));
        profile.setEditBy(profileAdmin.getUserName());
        profile.setUpdateDate(new Date());
        Profile save = profileRepository.save(profile);
        save.setSecret("");

        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
    }


    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse login(HttpServletRequest request
            , @RequestParam(value = "username", defaultValue = "", required = true) String userName
            , @RequestParam(value = "password", defaultValue = "", required = true) String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        initialize(request);

        List<Profile> profiles = profileRepository.findByUserNameIgnoreCaseAndEnable(userName, true);

        if (profiles.size() == 1) {
            if (profiles.get(0).isEnable() == true ) {
                if (profiles.get(0).getReadGroups().size()>0||profiles.get(0).getRole()!=null) {
                    Profile profileByUserName = profiles.get(0);
                    boolean correct = false;
                    if (password.length() > 0) {
                        correct = checkPassword(password, profileByUserName.getSecret());
                    }
                    if (correct ) {

                        String token = SSOToken.create(profileByUserName.getId(), secret, expire, issuer);
                        Map<String, Object> hashMap = new HashMap<>();
                        profileByUserName.setSecret("");
                        profileByUserName.setToken(token);
                        profileByUserName.setReadGroups(setGroupName(profileByUserName.getReadGroups()));
                        profileByUserName.setPendingGroups(setGroupName(profileByUserName.getPendingGroups()));
                        profileByUserName.setProvinceName(getProvinceName(profileByUserName.getProvinceCode()));
                        profileByUserName.setDistrictName(getDistrictName(profileByUserName.getDistrictCode()));
                        profileByUserName.setSubDistrictName(getSubDistrictName(profileByUserName.getSubDistrictCode()));
                        profileByUserName.setZipcode(getZipcode(profileByUserName.getSubDistrictCode()));

                        profileByUserName.setKhaProfile(getGroupProfile(profileByUserName.getKhaId()));
                        hashMap.put("profile", profileByUserName);
                        LogLogin logLogin = new LogLogin();
                        logLogin.setIpAddress(request.getRemoteAddr());
                        logLogin.setUserId(profileByUserName.getId());
                        logLogin.setUserName(profileByUserName.getUserName());
                        logLogin.setTime(new Date());
                        logLoginRepository.insert(logLogin);
                        return getOk(new BaseResponse(OK, localizeText.getLoginSucceed(), hashMap));
                    }
                }else {
                    return getError(ErrorFactory.getError(FAILED, localizeText.getNoReadGroups()));
                }
            } else {
                return getError(ErrorFactory.getError(FAILED, localizeText.getUserDisable()));
            }
        }

        return getError(ErrorFactory.getError(FAILED, localizeText.getLoginFailed()));
    }

    @CrossOrigin
    @RequestMapping(value = "/loginAdmin", method = RequestMethod.POST)
    public BaseResponse loginAdmin(HttpServletRequest request
            , @RequestParam(value = "username", defaultValue = "") String userName
            , @RequestParam(value = "password", defaultValue = "") String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        initialize(request);
        List<Profile> profiles;
        profiles = profileRepository.findByUserNameIgnoreCaseAndEnable(userName, true);

        if (profiles.size() == 1) {
            if (profiles.get(0).getRole()!=null) {
                Profile profileByUserName = profiles.get(0);
                boolean correct = false;
                if (password.length() > 0) {
                    correct = checkPassword(password, profileByUserName.getSecret());
                }
                if (correct) {

                    String token = SSOToken.create( profileByUserName.getId(),secret,expire,issuer);

                    Map<String, Object> hashMap = new HashMap<>();
                    profileByUserName.setSecret("");
                    profileByUserName.setToken(token);
                    profileByUserName.setReadGroups(setGroupName(profileByUserName.getReadGroups()));
                    profileByUserName.setPendingGroups(setGroupName(profileByUserName.getPendingGroups()));
                    profileByUserName.getRole().setGroupsName(setRoleName(profileByUserName.getRole().getAdminGroups()));
                    profileByUserName.getRole().setTechnicianName(setRoleName(profileByUserName.getRole().getTechnicianGroups()));
                    profileByUserName.setProvinceName(getProvinceName(profileByUserName.getProvinceCode()));
                    profileByUserName.setDistrictName(getDistrictName(profileByUserName.getDistrictCode()));
                    profileByUserName.setSubDistrictName(getSubDistrictName(profileByUserName.getSubDistrictCode()));
                    profileByUserName.setZipcode(getZipcode(profileByUserName.getSubDistrictCode()));
                    profileByUserName.setKhaProfile(getGroupProfile(profileByUserName.getKhaId()));

                    hashMap.put("profile", profileByUserName);
                    LogLogin logLogin = new LogLogin();
                    logLogin.setIpAddress(request.getRemoteAddr());
                    logLogin.setUserId(profileByUserName.getId());
                    logLogin.setUserName(profileByUserName.getUserName());
                    logLogin.setTime(new Date());
                    logLoginRepository.insert(logLogin);

                    return getOk(new BaseResponse(OK, localizeText.getLoginSucceed(), hashMap));
                }
            } else {
                return getError(ErrorFactory.getError(FAILED, localizeText.getPermissionDenied()));
            }
        }
        return getError(ErrorFactory.getError(FAILED, localizeText.getLoginFailed()));
    }

    @CrossOrigin
    @RequestMapping(value = "/getLogLoginDetail", method = RequestMethod.GET)
    public BaseResponse getLogLoginDetail(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "") String id) throws PostExceptions {
        initialize(request);
        userValidateToken(token, request);
        userValidateId(id);
        List<LogLogin> byUserId = logLoginRepository.findByUserId(id);
        return getOk(new BaseResponse(byUserId));
    }


    @CrossOrigin
    @RequestMapping(value = "/getLogLoginAll", method = RequestMethod.GET)
    public BaseResponse getLogLoginAll(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword
            , @RequestParam(value = "page", defaultValue = "0", required = false) int page
            , @RequestParam(value = "sizeContents", defaultValue = "30", required = false) int sizeContents
    ) throws PostExceptions {

        initialize(request);
        userValidateToken(token, request);
        Page<LogLogin> logLogins = logLoginAll(page, sizeContents, keyword);
        return getOk(new BaseResponse(logLogins));
    }


    private Page<LogLogin> logLoginAll(int page, int sizeContents, String keyword) {
        Pageable pageable =  PageRequest.of(page, sizeContents);
        Query query = new Query();
        if (!keyword.equals(null) && !keyword.equals("")) {
            Criteria criteria_editor = Criteria.where("userName").regex(keyword);
            query.addCriteria(new Criteria().orOperator(criteria_editor));
        }
        List<LogLogin> posts = mongoTemplate.find(query, LogLogin.class);
        List<String> idList = new ArrayList<>();
        Page<LogLogin> pagepPosts;
        for (LogLogin loopFindPost : posts) {
            idList.add(loopFindPost.getId());
        }
        pagepPosts = logLoginRepository.findByIdIn(idList, pageable);

        for (int i = 0; i < pagepPosts.getContent().size(); i++) {
            Profile authorProfile = profileRepository.findById(pagepPosts.getContent().get(i).getUserId()).get();
            if (authorProfile != null) {
                authorProfile.setSecret("");
                pagepPosts.getContent().get(i).setAuthorProfile(authorProfile);
            }
        }
        return pagepPosts;
    }


    @CrossOrigin
    @ApiOperation(value = "ข้อมูลผู้ใช้งานทั้งหมด", notes = "", response = Profile.class)
    @RequestMapping(value = "/getUserAll", method = RequestMethod.GET)
    public BaseResponse getUserAll(HttpServletRequest request, @RequestHeader(value = "token", defaultValue = TOKEN) String token,
                                   @RequestParam(value = "role", defaultValue = "", required = false) @ApiParam(value = "1=admin,2=user") String role,
                                   @RequestParam(value = "readGroupId", defaultValue = "", required = false) @ApiParam(value = "groupId")List<String>  readGroupId,
                                   @RequestParam(value = "adminGroupId", defaultValue = "", required = false) @ApiParam(value = "groupId") List<String> adminGroupId,
                                   @RequestParam(value = "technicianGroupId", defaultValue = "", required = false) @ApiParam(value = "groupId") List<String> technicianGroupId,
                                   @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate,
                                   @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate,
                                   @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord,
                                   @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(value = "sort", defaultValue = "1", required = false) @ApiParam(value = "1 = Sort by firstName,2 = Sort by email,3 = Sort by createDate,4 = Sort by enable") int sort,
                                   @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int orderSort,
                                   @RequestParam(value = "sizeContents", defaultValue = "30", required = false) int sizeContents) throws PostExceptions {
        initialize(request);
        Profile adminProfile = userValidateToken(token, request);

        if (readGroupId.size()>0){
            for (String group:readGroupId){
                checkSuperAdminGroups(adminProfile,group);
            }
        }else {
            if (adminProfile.getRole() == null) {
                throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
            }
            if (adminProfile.getRole().getSuperAdmin() == false) {
                    throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
            }
        }

        Pageable pageable = null;
        if (sort == 1 && orderSort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("firstName").ascending());
        } else if (sort == 1 && orderSort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("firstName").descending());
        } else if (sort == 2 && orderSort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("email").ascending());
        } else if (sort == 2 && orderSort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("email").descending());
        } else if (sort == 3 && orderSort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("createDate").ascending());
        } else if (sort == 3 && orderSort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("createDate").descending());
        }else if (sort == 4 && orderSort == 1) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("enable").ascending());
        } else if (sort == 4 && orderSort == 2) {
            pageable = PageRequest.of(page, sizeContents, Sort.by("enable").descending());
        }
        Query query = new Query().with(pageable);

        if (role.length() > 0) {
            if (role.equals("1")) {
                query.addCriteria(Criteria.where("role").ne(null));

            } else if (role.equals("2")) {
                query.addCriteria(Criteria.where("role").is(null));
            }
        }
        if (readGroupId.size()>0){
            query.addCriteria(Criteria.where("readGroups.groupId").in(readGroupId));
        }
        if (adminGroupId.size()>0){
            query.addCriteria(Criteria.where("role.adminGroups").in(adminGroupId));

        }
        if (technicianGroupId.size()>0){
            query.addCriteria(Criteria.where("role.technicianGroups").in(technicianGroupId));
        }

        if (startDate.length() > 0 && endDate.length() > 0) {
            query.addCriteria(Criteria.where("createDate").gte(new Date(Long.parseLong(startDate))).lt(new Date(Long.parseLong(endDate))));
        }
        Criteria firstName = Criteria.where("firstName").regex(keyWord, "i");
        Criteria lastName = Criteria.where("lastName").regex(keyWord, "i");
        Criteria userName = Criteria.where("userName").regex(keyWord, "i");
        Criteria email = Criteria.where("email").regex(keyWord, "i");

        if (keyWord != null && keyWord.length() > 0) {
            query.addCriteria(new Criteria().orOperator(firstName, lastName, userName, email));
        }
        List<Profile> post = mongoTemplate.find(query, Profile.class);
        long count = mongoTemplate.count(query, Profile.class);
        Page<Profile> profiles = new PageImpl<Profile>(post, pageable, count);

        for (int i = 0; i < profiles.getContent().size(); i++) {
            Profile profile =profiles.getContent().get(i);
            profile.setSecret("");
            profile.setReadGroups(setGroupName(profile.getReadGroups()));
            profile.setPendingGroups(setGroupName(profile.getPendingGroups()));
            if (profile.getRole()!=null) {
                profile.getRole().setGroupsName(setRoleName(profile.getRole().getAdminGroups()));
                profile.getRole().setTechnicianName(setRoleName(profile.getRole().getTechnicianGroups()));
            }
            profile.setProvinceName(getProvinceName(profile.getProvinceCode()));
            profile.setDistrictName(getDistrictName(profile.getDistrictCode()));
            profile.setSubDistrictName(getSubDistrictName(profile.getSubDistrictCode()));
            profile.setZipcode(getZipcode(profile.getSubDistrictCode()));
            profile.setKhaProfile(getGroupProfile(profile.getKhaId()));
        }
        return getOk(new BaseResponse(profiles));
    }

    @CrossOrigin
    @ApiOperation(value = "ข้อมูลสมาชิก", notes = "", response = Profile.class)
    @RequestMapping(value = "/getProfile", method = RequestMethod.GET)
    public BaseResponse getProfile(HttpServletRequest request, @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "") String id) throws PostExceptions {
        initialize(request);
        Profile adminProfile = userValidateToken(token, request);


        Profile profile1 = profileRepository.findById(id).get();
        if (!adminProfile.getId().equals(id)){
            checkAdminGroupsList(adminProfile,profile1.getReadGroups());
        }

        profile1.setSecret("");
        profile1.setReadGroups(setGroupName(profile1.getReadGroups()));
        profile1.setPendingGroups(setGroupName(profile1.getPendingGroups()));
        if (profile1.getRole()!=null) {
            profile1.getRole().setGroupsName(setRoleName(profile1.getRole().getAdminGroups()));
            profile1.getRole().setTechnicianName(setRoleName(profile1.getRole().getTechnicianGroups()));

        }
        profile1.setProvinceName(getProvinceName(profile1.getProvinceCode()));
        profile1.setDistrictName(getDistrictName(profile1.getDistrictCode()));
        profile1.setSubDistrictName(getSubDistrictName(profile1.getSubDistrictCode()));
        profile1.setZipcode(getZipcode(profile1.getSubDistrictCode()));
        profile1.setKhaProfile(getGroupProfile(profile1.getKhaId()));
        return getOk(new BaseResponse(profile1));
    }

    @CrossOrigin
    @RequestMapping(value = "/updateGcmToken", method = RequestMethod.POST)
    public BaseResponse updateGcmToken(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "gcmToken", defaultValue = "") String gcmToken
    ) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        profile.setGcmToken(gcmToken);
        Profile save = profileRepository.save(profile);
        save.setSecret("");
        return getOk(new BaseResponse(OK, localizeText.getDataUpdated(), save));
    }


    private String GenerateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        String base64Encoded = Base64.getEncoder().encodeToString(salt);
        base64Encoded = base64Encoded.substring(0, Math.min(base64Encoded.length(), 32));
        return base64Encoded;
    }

    private int Length2Bytes(int strLength) {
        int length = strLength * 6;
        int i = (int) (Math.round((double) ((length + 7) / 8)));
        return i;
    }

    private String createPassword(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String salt = GenerateSalt();
        byte[] hashValue;
        byte[] saltBytes = salt.getBytes("UTF-8");
        password = salt + password;
        Rfc2898DeriveBytes rfc = new Rfc2898DeriveBytes(password, saltBytes, 10000);
        hashValue = rfc.getBytes(Length2Bytes(255));
        String base64Encoded = Base64.getEncoder().encodeToString(hashValue);

        base64Encoded = salt + base64Encoded;
        return base64Encoded;
    }

    private boolean checkPassword(String password, String realPass) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String salt = realPass.substring(0, Math.min(realPass.length(), 32));
        byte[] hashValue;
        byte[] saltBytes = salt.getBytes("UTF-8");
        password = salt + password;
        Rfc2898DeriveBytes rfc = new Rfc2898DeriveBytes(password, saltBytes, 10000);
        hashValue = rfc.getBytes(Length2Bytes(255));
        String base64Encoded = Base64.getEncoder().encodeToString(hashValue);

        base64Encoded = salt + base64Encoded;
        if (base64Encoded.equals(realPass)) {
            return true;
        } else {
            return false;
        }
    }


    public void sendEmailForget(String email ,String subject) throws PostExceptions {
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
                    InternetAddress.parse(email));
            message.setSubject(subject);
            String html = "คุณสามารถเปลี่ยนแปลงรหัสผ่านของคุณได้จากลิงค์นี้ "  + "\n<a href='"+URL_CMS+"password-change?m=" + email+"'>คลิก</a>";
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (javax.mail.MessagingException e) {
            throw new PostExceptions(FAILED, e.toString());
        }
    }
    public void sendEmailApprove(String email ,String subject) throws PostExceptions {
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
                    InternetAddress.parse(email));
            message.setSubject(subject);
            String html = "บัญชีของท่านได้รับการอนุมัติจากผู้ดูแลกลุ่มแล้ว ท่านสามารถเข้าใช้งานได้ทันที";
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (javax.mail.MessagingException e) {
            throw new PostExceptions(FAILED, e.toString());
        }
    }
}
