package com.zti.kha.api;

import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.ComplainInfo.Complain;
import com.zti.kha.model.ComplainInfo.Status;
import com.zti.kha.model.User.GroupDisplay;
import com.zti.kha.model.User.Profile;
import com.zti.kha.model.User.ProfileDisplay;
import com.zti.kha.model.User.ReadGroup;
import com.zti.kha.utility.PostExceptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.ParseException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;

@RestController
@RequestMapping("/api/v1")
public class ReportApi extends CommonApi {
    @CrossOrigin
    @ApiOperation(value = "รายงานข้อมูลผู้ใช้งาน", notes = "", response = Profile.class)
    @RequestMapping(value = "/exportReport/user", method = RequestMethod.GET)
    public void exportReportUser(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "token", defaultValue = TOKEN) String token,
                                   @RequestParam(value = "role", defaultValue = "", required = false) @ApiParam(value = "1=admin,2=user") String role,
                                   @RequestParam(value = "readGroupId", defaultValue = "", required = false) @ApiParam(value = "groupId")List<String>  readGroupId,
                                   @RequestParam(value = "adminGroupId", defaultValue = "", required = false) @ApiParam(value = "groupId") List<String> adminGroupId,
                                   @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate,
                                   @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate,
                                   @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord) throws PostExceptions {
        initialize(request);
        Profile profile1 = userValidateToken(token, request);
        Query query = new Query().with(Sort.by("createDate").descending());

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


        for (int i = 0; i < post.size(); i++) {
            post.get(i).setSecret("");
            post.get(i).setReadGroups(setGroupName(post.get(i).getReadGroups()));
            post.get(i).setPendingGroups(setGroupName(post.get(i).getPendingGroups()));
            if (post.get(i).getRole()!=null) {
                post.get(i).getRole().setGroupsName(setRoleName(post.get(i).getRole().getAdminGroups()));
                post.get(i).getRole().setTechnicianName(setRoleName(post.get(i).getRole().getTechnicianGroups()));
            }

            post.get(i).setProvinceName(getProvinceName(post.get(i).getProvinceCode()));
            post.get(i).setDistrictName(getDistrictName(post.get(i).getDistrictCode()));
            post.get(i).setSubDistrictName(getSubDistrictName(post.get(i).getSubDistrictCode()));
            post.get(i).setKhaProfile(getGroupProfile(post.get(i).getKhaId()));
        }
        excelSheetUser(response, post);

    }

    private void excelSheetUser(HttpServletResponse response, List<Profile> results) {
        String reportName = "Report_MyKHA_User_" + convertDateToString(new Date()) + ".xlsx";
        response.setHeader("Content-Disposition", "inline; filename=" + reportName);
        response.setContentType("application/vnd.ms-excel");

        XSSFWorkbook workbook = new XSSFWorkbook();
        // Set Column Excel
        XSSFSheet worksheet = workbook.createSheet("Sheet1");
        worksheet.setColumnWidth(0, 8000);
        worksheet.setColumnWidth(1, 8000);
        worksheet.setColumnWidth(2, 7000);
        worksheet.setColumnWidth(3, 6000);
        worksheet.setColumnWidth(4, 6000);
        worksheet.setColumnWidth(5, 5000);
        worksheet.setColumnWidth(6, 6000);
        worksheet.setColumnWidth(7, 9000);
        worksheet.setColumnWidth(8, 7000);
        worksheet.setColumnWidth(9, 7000);
        worksheet.setColumnWidth(10, 7000);
        worksheet.setColumnWidth(11, 7000);
        worksheet.setColumnWidth(12, 5000);
        worksheet.setColumnWidth(13, 7000);
        worksheet.setColumnWidth(14, 6000);
        worksheet.setColumnWidth(15, 5000);
        worksheet.setColumnWidth(16, 8000);
        worksheet.setColumnWidth(17, 4000);

        // Set Font Excel
        Font font = worksheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // Set Style Header
        CellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setFont(font);
        headerCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        // Set Style Column
        CellStyle columnCellStyle = worksheet.getWorkbook().createCellStyle();
        columnCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        columnCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        columnCellStyle.setWrapText(true);
        columnCellStyle.setFont(font);
        columnCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        columnCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        columnCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        columnCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        columnCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        columnCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        // Set Style Row
        CellStyle rowCellStyle = worksheet.getWorkbook().createCellStyle();
        rowCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        rowCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        rowCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        rowCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        rowCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        rowCellStyle.setWrapText(true);

        // Set Cell Header
        Row rowHeader = worksheet.createRow((short) 0);
        rowHeader.setHeight((short) 500);


        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("รายงานผู้ใช้งาน");
        cellHeader.setCellStyle(headerCellStyle);
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));

        // Set Cell Column
        Row rowColumn = worksheet.createRow((short) 1);
        rowColumn.setHeight((short) 600);

        List<String> title = new ArrayList<>();
        title.add("ชื่อ");
        title.add("นามสกุล");
        title.add("อีเมล");
        title.add("บัตรประชาชน");
        title.add("เบอร์ติดต่อ");
        title.add("วันที่ลงทะเบียน");
        title.add("ตำแหน่ง");
        title.add("การติดตาม");
        title.add("ที่อยู่");
        title.add("ตำบล");
        title.add("อำเภอ");
        title.add("จังหวัด");
        title.add("รหัสไปษณีย์");
        title.add("อาชีพ");
        title.add("กลุ่มผู้ใช้");
        title.add("วันสิ้นสุดสัญญา");
        title.add("โครงการที่อยู่");
        title.add("ผู้พักอาศัย");

        for (int i = 0; i <= 17; i++) {
            Cell cell = rowColumn.createCell(i);
            cell.setCellValue(title.get(i));
            cell.setCellStyle(columnCellStyle);
        }
        // Set Cell Row
        Row row;
        int no = 0;
        int startRowIndex = 1;
        for (Profile result : results) {
//            ProfileLc result=results.get(i);
            startRowIndex++;

            row = worksheet.createRow((short) startRowIndex);
            row.setHeight((short) 500);
            for (int i = 0; i <= 17; i++) {
                Cell row1 = row.createCell(i);

                if (i == 0) {
                    if (result.getFirstName()!=null&&result.getFirstName().length()>0){
                        row1.setCellValue(result.getFirstName());
                    }
                } else if (i == 1) {
                    if (result.getLastName()!=null&&result.getLastName().length()>0){
                        row1.setCellValue(result.getLastName());
                    }
                } else if (i == 2) {
                    if (result.getEmail()!=null&&result.getEmail().length()>0){
                        row1.setCellValue(result.getEmail());
                    }
                } else if (i == 3) {
                    if (result.getIdCard()!=null&&result.getIdCard().length()>0){
                        row1.setCellValue(result.getIdCard());
                    }                }
                else if (i == 4) {
                    if (result.getPhoneNumber()!=null&&result.getPhoneNumber().length()>0){
                        row1.setCellValue(result.getPhoneNumber());
                    }
                }  else if (i == 5) {
                    row1.setCellValue(convertDateToString(result.getCreateDate()));

                } else if (i == 6) {
                    String role="";
                    if (result.getRole()!=null){
                        role="ผู้ดูแลระบบ";
                    }else {
                        role="ผู้ใช้งาน";
                    }
                    row1.setCellValue(role);

                } else if (i == 7) {
                    String group="";

                    for (int j=0;j<result.getReadGroups().size();j++){
                        if (j==0){
                            group=group+result.getReadGroups().get(j).getGroupName();
                        }else {
                            group=group+","+result.getReadGroups().get(j).getGroupName();
                        }
                    }
                    row1.setCellValue(group);

                } else if (i == 8) {
                    if (result.getAddress()!=null&&result.getAddress().length()>0){
                        row1.setCellValue(result.getAddress());
                    }
                } else if (i == 9) {
                        row1.setCellValue(result.getSubDistrictName());

                } else if (i == 10) {
                    row1.setCellValue(result.getDistrictName());
                } else if (i == 11) {

                    row1.setCellValue(result.getProvinceName());
                } else if (i == 12) {
                    row1.setCellValue(result.getZipcode());
                }else if (i == 13) {
                    if (result.getJob()!=null&&result.getJob().length()>0) {
                        row1.setCellValue(result.getJob());
                    }
                }else if (i == 14) {
                    String type="";
                    if (result.getType()==1){
                        type="กลุ่มคนโสด";
                    }else if (result.getType()==2){
                        type="กลุ่มคนทำงาน";
                    }if (result.getType()==3){
                        type="กลุ่มครอบครัว";
                    }if (result.getType()==4){
                        type="ผู้สูงอายุ/ผู้พิการ";
                    }
                    row1.setCellValue(type);
                }else if (i == 15) {
                    if (result.getEndContract()!=null) {
                        row1.setCellValue(convertDateToString(result.getEndContract()));
                    }
                }else if (i == 16) {
                    if (result.getKhaProfile()!=null&&result.getKhaProfile().getName().length()>0) {

                        row1.setCellValue(result.getKhaProfile().getName());
                    }
                }else if (i == 17) {
                    row1.setCellValue(result.getAmountResidents());

                }
                row1.setCellStyle(rowCellStyle);

            }

        }
        try {
            ServletOutputStream outStream = response.getOutputStream();
            workbook.write(outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @CrossOrigin
    @ApiOperation(value = "รายงานร้องเรียนแจ้งเหตุ'", notes = "", response = Complain.class)
    @GetMapping(value = "exportReport/complain")
    public void exportReport(HttpServletRequest request, HttpServletResponse response
            , @RequestParam(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "currentStatus", defaultValue = "", required = false) @ApiParam(value = "0=waiting,1=in process,2=cancel,3=done,4=out of control") String currentStatus
            , @RequestParam(value = "titleId", defaultValue = "", required = true) @ApiParam(value = "categoryCode of categoryType complain") String titleId
            , @RequestParam(value = "startDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String startDate
            , @RequestParam(value = "endDate", defaultValue = "", required = false) @ApiParam(value = "Time in milliseconds") String endDate
            , @RequestParam(value = "category", defaultValue = "", required = false) @ApiParam(value = "categoryCode of sub category complain") String category
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
            , @RequestParam(value = "sequence", defaultValue = "", required = false) String sequence
            , @RequestParam(value = "provinceCode", defaultValue = "", required = false) String provinceCode
            , @RequestParam(value = "orderBy", defaultValue = "2", required = false) @ApiParam(value = "1=asc,2=desc") int orderSort
            , @RequestParam(value = "lastDays", defaultValue = "0", required = false) int lastDays
            , @RequestParam(value = "sort", defaultValue = "3", required = false) @ApiParam(value = "1 = Sort by sequence,2 = Sort by complainId,3 = Sort by createDate") int sort
            , @RequestParam(value = "groupId", defaultValue = "", required = false) String groupId

    ) throws PostExceptions, ParseException {
        initialize(request);

        Profile profile = userValidateToken(token, request);
        Sort sort1 = null;
        if (sort == 1 && orderSort == 1) {
            sort1 = Sort.by("sequence").ascending();
        } else if (sort == 1 && orderSort == 2) {
            sort1 = Sort.by("sequence").descending();
        } else if (sort == 2 && orderSort == 1) {
            sort1 = Sort.by("complainId").ascending();
        } else if (sort == 2 && orderSort == 2) {
            sort1 =  Sort.by("complainId").descending();
        } else if (sort == 3 && orderSort == 1) {
            sort1 = Sort.by("createDate").ascending();
        } else if (sort == 3 && orderSort == 2) {
            sort1 =  Sort.by("createDate").descending();
        }
        if (groupId.length() == 0) {
            checkSuperAdmin(profile);
        } else {
            checkSuperAdminGroups(profile, groupId);
        }

            Query query = new Query().with(sort1);

            if (groupId.length() > 0) {
                query.addCriteria(Criteria.where("groupId").is(groupId));

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


        for (int i = 0; i < post.size(); i++) {
            Complain complain = post.get(i);
            Optional<ProfileDisplay> authorProfile = profileRepository.findByIdIs(complain.getAuthor());
            if (authorProfile.isPresent()) {
                authorProfile.get().setKhaProfile(getGroupProfile(authorProfile.get().getKhaId()));

                post.get(i).setAuthorProfile(authorProfile.get());

            }
            complain.setProvinceName(getProvinceName(post.get(i).getProvinceCode()));
            complain.setDistrictName(getDistrictName(post.get(i).getDistrictCode()));
            complain.setSubDistrictName(getSubDistrictName(post.get(i).getSubDistrictCode()));
            //getCateName
            complain.setCategoryName(getCateName(post.get(i).getCategory()));
            complain.setTitleName(getCateName(post.get(i).getTitleId()));
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

        excelSheet(response,post);
    }


    private void excelSheet(HttpServletResponse response, List<Complain> results) {
        String reportName = "Report_MyKHA_Complain_" + convertDateToString(new Date()) + ".xlsx";
        response.setHeader("Content-Disposition", "inline; filename=" + reportName);
        response.setContentType("application/vnd.ms-excel");

        XSSFWorkbook workbook = new XSSFWorkbook();

        // Set Column Excel
        XSSFSheet worksheet = workbook.createSheet("Sheet1");
        worksheet.setColumnWidth(0, 5000);
        worksheet.setColumnWidth(1, 10000);
        worksheet.setColumnWidth(2, 10000);
        worksheet.setColumnWidth(3, 8000);
        worksheet.setColumnWidth(4, 10000);
        worksheet.setColumnWidth(5, 8000);
        worksheet.setColumnWidth(6, 5000);
        worksheet.setColumnWidth(7, 5000);
        worksheet.setColumnWidth(8, 5000);
        worksheet.setColumnWidth(9, 5000);
        worksheet.setColumnWidth(10, 5000);
        worksheet.setColumnWidth(11, 7000);
        worksheet.setColumnWidth(12, 5000);
        worksheet.setColumnWidth(13, 7000);
        worksheet.setColumnWidth(14, 5000);
        worksheet.setColumnWidth(15, 5000);
        worksheet.setColumnWidth(16, 7000);
        worksheet.setColumnWidth(17, 7000);
        worksheet.setColumnWidth(18, 7000);
        worksheet.setColumnWidth(19, 5000);
        worksheet.setColumnWidth(20, 5000);
        worksheet.setColumnWidth(21, 10000);

        // Set Font Excel
        Font font = worksheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // Set Style Header
        CellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setFont(font);
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        // Set Style Column
        CellStyle columnCellStyle = worksheet.getWorkbook().createCellStyle();
        columnCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        columnCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        columnCellStyle.setWrapText(true);
        columnCellStyle.setFont(font);
        columnCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        columnCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        columnCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        columnCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        columnCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        columnCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        // Set Style Row
        CellStyle rowCellStyle = worksheet.getWorkbook().createCellStyle();
        rowCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        rowCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        rowCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        rowCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        rowCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        rowCellStyle.setWrapText(true);

        // Set Cell Header
        Row rowHeader = worksheet.createRow((short) 0);
        rowHeader.setHeight((short) 500);

        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("รายงานการร้องเรียน");
        cellHeader.setCellStyle(headerCellStyle);
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 21));

        Row rowSubHeader = worksheet.createRow((short) 1);
        rowSubHeader.setHeight((short) 600);
        for (int i = 0; i <= 21; i++) {
            if (i != 0 || i != 5 || i != 8 || i != 16) {
                rowSubHeader.createCell(i).setCellStyle(headerCellStyle);
            }
        }
        Cell cellSubHeader0 = rowSubHeader.createCell(0);
        cellSubHeader0.setCellValue("รายละเอียด");
        cellSubHeader0.setCellStyle(headerCellStyle);
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));


        Cell cellSubHeader5 = rowSubHeader.createCell(5);
        cellSubHeader5.setCellValue("ตำแหน่ง");
        cellSubHeader5.setCellStyle(headerCellStyle);
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));

        Cell cellSubHeader9 = rowSubHeader.createCell(8);
        cellSubHeader9.setCellValue("สถานะ");
        cellSubHeader9.setCellStyle(headerCellStyle);
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 15));

        Cell cellSubHeader15 = rowSubHeader.createCell(16);
        cellSubHeader15.setCellValue("ข้อมูลผู้แจ้ง");
        cellSubHeader15.setCellStyle(headerCellStyle);
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 16, 21));


        // Set Cell Column
        Row rowColumn = worksheet.createRow((short) 2);
        rowColumn.setHeight((short) 600);

        Cell cell0 = rowColumn.createCell(0);
        cell0.setCellValue("หมายเลขร้องเรียน");
        cell0.setCellStyle(columnCellStyle);

        Cell cell1 = rowColumn.createCell(1);
        cell1.setCellValue("โครงการ");
        cell1.setCellStyle(columnCellStyle);

        Cell cell1_ = rowColumn.createCell(2);
        cell1_.setCellValue("หัวข้อ");
        cell1_.setCellStyle(columnCellStyle);

        Cell cell11 = rowColumn.createCell(3);
        cell11.setCellValue("หมวดหมู่");
        cell11.setCellStyle(columnCellStyle);

        Cell cell2 = rowColumn.createCell(4);
        cell2.setCellValue("รายละเอียด");
        cell2.setCellStyle(columnCellStyle);

        Cell cell3 = rowColumn.createCell(5);
        cell3.setCellValue("ตำแหน่ง/จุดสังเกต");
        cell3.setCellStyle(columnCellStyle);


        Cell cell5 = rowColumn.createCell(6);
        cell5.setCellValue("ละติจูด");
        cell5.setCellStyle(columnCellStyle);

        Cell cell6 = rowColumn.createCell(7);
        cell6.setCellValue("ลองติจูด");
        cell6.setCellStyle(columnCellStyle);

        Cell cell7 = rowColumn.createCell(8);
        cell7.setCellValue("วันที่ร้องเรียน");
        cell7.setCellStyle(columnCellStyle);


        Cell cell8 = rowColumn.createCell(9);
        cell8.setCellValue("สถานะ");
        cell8.setCellStyle(columnCellStyle);

        Cell cell81 = rowColumn.createCell(10);
        cell81.setCellValue("ระยะเวลา (วัน)");
        cell81.setCellStyle(columnCellStyle);

        Cell cell9 = rowColumn.createCell(11);
        cell9.setCellValue("ผู้อัปเดต");
        cell9.setCellStyle(columnCellStyle);

        Cell cell10 = rowColumn.createCell(12);
        cell10.setCellValue("วันที่อัปเดต");
        cell10.setCellStyle(columnCellStyle);

        Cell cell23 = rowColumn.createCell(13);
        cell23.setCellValue("หมายเหตุ");
        cell23.setCellStyle(columnCellStyle);

        Cell cell12 = rowColumn.createCell(14);
        cell12.setCellValue("คะแนนความพึงพอใจต่อบริการ");
        cell12.setCellStyle(columnCellStyle);

        Cell cell12_ = rowColumn.createCell(15);
        cell12_.setCellValue("คะแนนความพึงพอใจต่อเจ้าหน้าที่");
        cell12_.setCellStyle(columnCellStyle);

        Cell cell13 = rowColumn.createCell(16);
        cell13.setCellValue("อีเมล");
        cell13.setCellStyle(columnCellStyle);

        Cell cell14 = rowColumn.createCell(17);
        cell14.setCellValue("ชื่อ");
        cell14.setCellStyle(columnCellStyle);

        Cell cell15 = rowColumn.createCell(18);
        cell15.setCellValue("สกุล");
        cell15.setCellStyle(columnCellStyle);

        Cell cell16 = rowColumn.createCell(19);
        cell16.setCellValue("รหัสประจำตัวประชาชน");
        cell16.setCellStyle(columnCellStyle);

        Cell cell17 = rowColumn.createCell(20);
        cell17.setCellValue("โทรศัพท์");
        cell17.setCellStyle(columnCellStyle);

        Cell cell18 = rowColumn.createCell(21);
        cell18.setCellValue("โครงการที่อยู่");
        cell18.setCellStyle(columnCellStyle);


      // Set Cell Row
        Row row;
        int no = 0;
        int startRowIndex = 2;
        for (Complain result : results) {
            startRowIndex++;

            row = worksheet.createRow((short) startRowIndex);
            row.setHeight((short) 500);

            no++;

            Cell row0 = row.createCell(0);
            row0.setCellValue(result.getComplainId());
            row0.setCellStyle(rowCellStyle);

            Cell row1_ = row.createCell(1);
            row1_.setCellValue(result.getGroupProfile().getName());
            row1_.setCellStyle(rowCellStyle);

            Cell row1 = row.createCell(2);
            row1.setCellValue(result.getTitleName());
            row1.setCellStyle(rowCellStyle);

            Cell row2 = row.createCell(3);
                row2.setCellValue(result.getCategoryName());

            row2.setCellStyle(rowCellStyle);

            Cell row3 = row.createCell(4);
            row3.setCellValue(result.getDescription());
            row3.setCellStyle(rowCellStyle);

            Cell row4 = row.createCell(5);
            row4.setCellValue(result.getPlaceName());
            row4.setCellStyle(rowCellStyle);


            Cell row7 = row.createCell(6);
            row7.setCellValue(result.getLatitude());
            row7.setCellStyle(rowCellStyle);

            Cell row8 = row.createCell(7);
            row8.setCellValue(result.getLongitude());
            row8.setCellStyle(rowCellStyle);

            Cell row9 = row.createCell(8);
            row9.setCellValue(convertDateToShortTH(result.getCreateDate()) + " น.");
            row9.setCellStyle(rowCellStyle);




                Cell row10 = row.createCell(9);
                String msgCode = "";
                if (result.getCurrentStatus() == 0) {
                    msgCode = "รอการตรวจสอบ";
                } else if (result.getCurrentStatus() == 1) {
                    msgCode = "กำลังดำเนินการ";

                } else if (result.getCurrentStatus() == 3) {
                    msgCode = "ดำเนินการแล้ว";

                } else if (result.getCurrentStatus() == 2) {
                    msgCode = "ยกเลิก";

                } else if (result.getCurrentStatus() == 4 ){
                    msgCode = "นอกเหนือความรับผิดชอบ";

                }
                row10.setCellValue(msgCode);
                row10.setCellStyle(rowCellStyle);



            Cell row91 = row.createCell(10);
            if (result.getCurrentStatus() == 3){
                long diff =  result.getStatusComplains().get(result.getStatusComplains().size()-1).getUpdateDate().getTime()-result.getCreateDate().getTime() ;

                row91.setCellValue(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

            }else {
                row91.setCellValue("-");

            }
            row91.setCellStyle(rowCellStyle);

            Cell row11 = row.createCell(11);
            Cell row12 = row.createCell(12);
            Cell row13 = row.createCell(13);
            if (result.getStatusComplains().size() > 0) {
                List<Status> statusComplains = result.getStatusComplains();
                if (statusComplains.get(statusComplains.size() - 1).getEditByProfile() != null) {
                    row11.setCellValue(statusComplains.get(statusComplains.size() - 1).getEditByProfile().getFirstName() + " " + statusComplains.get(statusComplains.size() - 1).getEditByProfile().getLastName());
                }
                row12.setCellValue(convertDateToShortTH(statusComplains.get(statusComplains.size() - 1).getUpdateDate()) + " น.");
                row13.setCellValue(statusComplains.get(statusComplains.size() - 1).getRemark());

            }
            row11.setCellStyle(rowCellStyle);
            row12.setCellStyle(rowCellStyle);
            row13.setCellStyle(rowCellStyle);

            Cell row14 = row.createCell(14);
            if (result.getRate() != null) {
                row14.setCellValue(result.getRate().getRateDuration());
            }
            row14.setCellStyle(rowCellStyle);

            Cell row14_ = row.createCell(15);
            if (result.getRate() != null) {
                row14_.setCellValue(result.getRate().getRateUpdate());
            }
            row14_.setCellStyle(rowCellStyle);

            Cell row15 = row.createCell(16);
            Cell row16 = row.createCell(17);
            Cell row17 = row.createCell(18);
            Cell row18 = row.createCell(19);
            Cell row19 = row.createCell(20);
            Cell row20 = row.createCell(21);


            if (result.getAuthorProfile() != null) {
                if (result.isFlgPrivate() == true) {
                    row15.setCellValue("***");
                    row16.setCellValue("***");
                    row17.setCellValue("***");
                    row18.setCellValue("***");
                    row19.setCellValue("***");
                    row20.setCellValue("***");

                } else {
                    row15.setCellValue(result.getAuthorProfile().getEmail());
                    row16.setCellValue(result.getAuthorProfile().getFirstName());
                    row17.setCellValue(result.getAuthorProfile().getLastName());
                    row18.setCellValue(result.getAuthorProfile().getIdCard());
                    row19.setCellValue(result.getAuthorProfile().getPhoneNumber());
                    if (result.getAuthorProfile().getKhaProfile()!=null) {
                        row20.setCellValue(result.getAuthorProfile().getKhaProfile().getName());
                    }else {
                        row20.setCellValue("");

                    }

                }
            }
            row15.setCellStyle(rowCellStyle);
            row16.setCellStyle(rowCellStyle);
            row17.setCellStyle(rowCellStyle);
            row18.setCellStyle(rowCellStyle);
            row19.setCellStyle(rowCellStyle);
            row20.setCellStyle(rowCellStyle);


        }

        try {
            ServletOutputStream outStream = response.getOutputStream();
            workbook.write(outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
