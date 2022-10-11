package com.zti.kha.api;



import com.zti.kha.model.Address.District;
import com.zti.kha.model.Address.Province;
import com.zti.kha.model.Address.SubDistrict;
import com.zti.kha.model.Base.BaseResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AddressApi extends CommonApi {




    @CrossOrigin
    @RequestMapping(value = "/getProvince", method = RequestMethod.GET)
    public BaseResponse getProvince(HttpServletRequest request) {
        initialize(request);

        Query query = new Query().with(Sort.by("province_th").ascending());

        List<Province> addresses = mongoTemplate.find(query, Province.class);
        return getOk(new BaseResponse(addresses));
    }

    @CrossOrigin
    @RequestMapping(value = "/getDistrict", method = RequestMethod.GET)
    public BaseResponse getDistrict(HttpServletRequest request
            , @RequestParam(value = "provinceCode", defaultValue = "", required = true) int province) {
        initialize(request);

        Query query = Query.query(Criteria.where("province_code").is(province)).with(Sort.by("district_th").ascending());

        List<District> addresses = mongoTemplate.find(query, District.class);

        return getOk(new BaseResponse(addresses));
    }

    @CrossOrigin
    @RequestMapping(value = "/getSubDistrict", method = RequestMethod.GET)
    public BaseResponse getSubDistrict(HttpServletRequest request
            , @RequestParam(value = "provinceCode", defaultValue = "", required = true) int province
            , @RequestParam(value = "districtCode", defaultValue = "", required = true) int district

    ) {
        initialize(request);

        Query query = Query.query(Criteria.where("province_code").is(province)).with(Sort.by("subDistrict_th").ascending());
        query.addCriteria(Criteria.where("district_code").is(district));

        List<SubDistrict> addresses = mongoTemplate.find(query, SubDistrict.class);

        return getOk(new BaseResponse(addresses));

    }


}
