package com.zti.kha.api;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.Category;
import com.zti.kha.model.User.Profile;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by S on 9/22/2016.
 */
@RestController
public class CategoryApi extends CommonApi {

     @CrossOrigin
    @ApiOperation(value = "หมวดหมู่", notes = "",response = Category.class)
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public BaseResponse category(HttpServletRequest request
               , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "categoryCode", defaultValue = "", required = false) String categoryCode
            , @RequestParam(value = "parentCategory", defaultValue = "",required = false) String parentCategory
            , @RequestParam(value = "readInstitution", defaultValue = "",required = false) String readInstitution
            , @RequestParam(value = "enable", defaultValue = "true",required = false) String enable
            , @RequestParam(value = "root", defaultValue = "false",required = false) boolean root
            , @RequestParam(value = "categoryType", defaultValue = "news", required = true) @ApiParam(value = "news,event,service,knowledge,notifications,complain")String categoryType) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        if (categoryCode.length()!=0) {
            Category byId = categoryRepository.findByCategoryCode(categoryCode);
            return getOk(new BaseResponse(byId));
        }else {
            Query query = new Query();
            query.with(Sort.by( "sequenceNo").ascending().and(Sort.by("createDate").descending()));

            if (!enable.equals("-1")){
                query.addCriteria(Criteria.where("enable").is(Boolean.parseBoolean(enable)));
            }

            if (categoryType.length()>0){
                query.addCriteria(Criteria.where("categoryType").is(categoryType));
            }

            if (parentCategory.length()>0){
                query.addCriteria(Criteria.where("parentCategory").is(parentCategory));
            }else {
                if (root==false){
                    query.addCriteria(Criteria.where("parentCategory").is(""));
                }
            }
            if (readInstitution.length()>0){
                query.addCriteria(Criteria.where("readInstitution").is(readInstitution));
            }
            List<Category> categoryList = mongoTemplate.find(query, Category.class);

            return getOk(new BaseResponse(categoryList));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    public BaseResponse addCategory(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "categoryName", defaultValue = "", required = true) String  categoryName
            , @RequestParam(value = "categoryImage", required = false) MultipartFile categoryImage
            , @RequestParam(value = "parentCategory", defaultValue = "", required = false) String parentCategory
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "categoryType", defaultValue = "", required = true)@ApiParam(value = "news,event,service,knowledge,notifications,complain") String categoryType
         ) throws PostExceptions {
        initialize(request);

            Profile profile = userValidateToken(token,request);
        checkSuperAdmin(profile);
        Category byCategoryCode = categoryRepository.findByCategoryNameAndCategoryType(categoryName,categoryType);
            if (byCategoryCode!=null){
                throw new PostExceptions(FAILED, localizeText.getDuplicateCategoryCode());
            }
        Category category = new Category();
        if (categoryImage != null) {
            String categoryImagePath = saveFile(categoryImage, FILE_TYPE_IMAGE, profile,FOLDER_CATEGORY);
            if (categoryImagePath != null || categoryImagePath.length() > 0) {
                category.setCategoryImage(categoryImagePath);
            }
        }
        int sequenceNo = generateSequence();
        category.setCategoryCode(String.valueOf(sequenceNo));
        category.setCategoryType(categoryType);
        category.setCategoryName(categoryName);
        category.setEditBy(profile.getUserName());
        category.setParentCategory(parentCategory);
        if (sequence==0){
            category.setSequenceNo(9999);
        }else {
            category.setSequenceNo(sequence);
        }
        categoryRepository.insert(category);
        return getOk(new BaseResponse(category));
    }
    private int generateSequence() {
        Category sequence ;
        sequence = categoryRepository.findTopByOrderByCreateDateDesc();
        return  sequence!=null? Integer.parseInt(sequence.getCategoryCode())+1 : 1;
    }

    @CrossOrigin
    @RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
    public BaseResponse deleteCategory(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue =TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id) throws PostExceptions {
        initialize(request);
            Profile profile = userValidateToken(token,request);

            Category byId = categoryRepository.findById(id).get();
        checkSuperAdmin(profile);

        categoryRepository.delete(byId);
            return getOk(new BaseResponse(byId));
    }

    @CrossOrigin
    @RequestMapping(value = "/editCategory", method = RequestMethod.POST)
    public BaseResponse editCategory(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id
            , @RequestParam(value = "enable", defaultValue = "true") Boolean enable
            , @RequestParam(value = "categoryName", defaultValue = "", required = false) String categoryName
            , @RequestParam(value = "parentCategory", defaultValue = "", required = false) String parentCategory
            , @RequestParam(value = "sequence", defaultValue = "0", required = false) int sequence
            , @RequestParam(value = "categoryImage", required = false) MultipartFile categoryImage) throws PostExceptions {
        initialize(request);

            Profile profile = userValidateToken(token,request);
            Category byId = categoryRepository.findById(id).get();

            if (byId!=null) {
                checkSuperAdmin(profile);

                if (!byId.getCategoryName().equals(categoryName)) {
                    Category byCategoryCode = categoryRepository.findByCategoryNameAndCategoryType(categoryName, byId.getCategoryType());
                    if (byCategoryCode != null) {
                        throw new PostExceptions(FAILED, localizeText.getDuplicateCategoryCode());
                    }
                    byId.setCategoryName(categoryName);
                }
                byId.setParentCategory(parentCategory);
                byId.setEnable(enable);

                if (categoryImage != null) {
                    String categoryImagePath = saveFile(categoryImage, FILE_TYPE_IMAGE, profile,FOLDER_CATEGORY);
                    if (categoryImagePath != null || categoryImagePath.length() > 0) {
                        byId.setCategoryImage(categoryImagePath);
                    }
                }
                if (sequence==0){
                    byId.setSequenceNo(9999);
                }else {
                    byId.setSequenceNo(sequence);
                }
                byId.setEditBy(profile.getUserName());
                categoryRepository.save(byId);
            }
            return getOk(new BaseResponse(byId));
    }

}
