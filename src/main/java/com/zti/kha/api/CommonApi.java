package com.zti.kha.api;

import com.zti.kha.component.Messages;
import com.zti.kha.controller.*;
import com.zti.kha.model.Address.District;
import com.zti.kha.model.Address.Province;
import com.zti.kha.model.Address.SubDistrict;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.Category;
import com.zti.kha.model.Common.FileInfo;
import com.zti.kha.model.Common.LocalizeText;
import com.zti.kha.model.ComplainInfo.Complain;
import com.zti.kha.model.Content.News;
import com.zti.kha.model.Content.Noti.Notifications;
import com.zti.kha.model.Statistic.ViewStatistic;
import com.zti.kha.model.User.Group;
import com.zti.kha.model.User.Profile;
import com.zti.kha.model.User.ReadGroup;
import com.zti.kha.utility.GcmSender;
import com.zti.kha.utility.PostExceptions;

import com.zti.kha.utility.SSOToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.*;
import java.util.List;
import java.util.*;


/**
 * Created by S on 9/22/2016.
 */
@RestController
public class CommonApi {
    @Autowired
    public MongoTemplate mongoTemplate;
    @Autowired
    protected LogLoginRepository logLoginRepository;
    @Autowired
    protected ProfileRepository profileRepository;
    @Autowired
    protected FileInfoRepository fileInfoRepository;
    @Autowired
    public ContactRepository contactRepository;
    @Autowired
    public NewsRepository newsRepository;
    @Autowired
    public EventRepository eventRepository;

    @Autowired
    public ViewStatisticRepository viewStatisticRepository;
    @Autowired
    public DailyStatisticRepository dailyStatisticRepository;
    @Autowired
    public RudeWordRepository rudeWordRepository;
    @Autowired
    public BannerRepository bannerRepository;
    @Autowired
    public NotificationsRepository notificationsRepository;

    @Autowired
    public DeleteNotificationRepository deleteNotificationRepository;
    @Autowired
    public InstallStatisticRepository installStatisticRepository;

    @Autowired
    public CategoryRepository categoryRepository;
    @Autowired
    public GroupRepository groupRepository;
    @Autowired
    public ProvinceRepository provinceRepository;
    @Autowired
    public DistrictRepository districtRepository;
    @Autowired
    public SubDistrictRepository subDistrictRepository;

    @Autowired
    public ServiceRepository serviceRepository;
    @Autowired
    public KnowledgeRepository knowledgeRepository;
    @Autowired
    public ComplainRepository complainRepository;

    @Autowired
    public LogDeleteAccountRepository logDeleteAccountRepository;
    @Autowired
    public RemarkDeleteRepository remarkDeleteRepository;
    @Autowired
    public MainPopUpRepository mainPopUpRepository;
    @Autowired
    public WebLinkRepository webLinkRepository;
    @Autowired
    public CommentComplainRepository commentComplainRepository;

    protected LocalizeText localizeText;
    @Value("${dir}")
    public String MEDIA_FOLDER;
    @Value("${jwt.secret}")
    public String secret;
    @Value("${jwt.issuer}")
    public String issuer;
    @Value("${jwt.limitExpire}")
    public long expire;

    public final static String TOKEN = "";
    @Value("${gcm.token}")
    public String GCM_KEY;
    @Value("${type.news}")
    public String TYPE_NEWS;
    @Value("${type.notifications}")
    public String TYPE_NOTIFICATIONS;
    @Value("${type.banner}")
    public String TYPE_BANNER;
    @Value("${type.event}")
    public String TYPE_EVENT;
    @Value("${type.service}")
    public String TYPE_SERVICE;
    @Value("${type.knowledge}")
    public String TYPE_KNOWLEDGE;
    @Value("${type.complain}")
    public String TYPE_COMPLAIN;
    @Value("${type.noti.user}")
    public String TYPE_NOTI_USER;
    @Value("${type.noti.admin}")
    public String TYPE_NOTI_ADMIN;


    @Value("${type.noti.comment}")
    public String TYPE_NOTI_COMMENT;
    @Value("${topic}")
    public String TOPIC;

    public final static String TH = "th";
    public final static int OK = 200;
    public final static int FAILED = 400;
    public final static int UNAUTHOR = 401;

    @Value("${dir.category}")
    public String FOLDER_CATEGORY;
    @Value("${dir.image}")
    public String FOLDER_IMAGE;
    @Value("${dir.cover}")
    public String FOLDER_COVER;
    @Value("${dir.banner}")
    public String FOLDER_BANNER;
    @Value("${dir.pdf}")
    public String FOLDER_PDF;
    @Value("${dir.profile}")
    public String FOLDER_PROFILE;
    @Value("${dir.files}")
    public String FOLDER_FILES;
    @Value("${dir.video}")
    public String FOLDER_VIDEO;
    @Value("${dir.audio}")
    public String FOLDER_AUDIO;
    @Value("${mail.sender}")
    public String MAIL_SENDER;
    @Value("${mail.password}")
    public String MAIL_PASSWORD;
    @Value("${dir.image}")
    public String FILE_TYPE_IMAGE;
    @Value("${file.mp4}")
    public String FILE_TYPE_MP4;
    @Value("${dir.pdf}")
    public String FILE_TYPE_PDF;
    @Value("${file.mp3}")
    public String FILE_TYPE_MP3;
    @Value("${cms.url}")
    public String URL_CMS;
    @Value("${server.url}")
    public String URL_SERVER;
    private String[] zone1 = {"18", "12", "13", "14", "16", "19", "17", "15"};//8
    private String[] zone2 = {"22", "24", "20", "23", "26", "25", "21", "11", "27"};//9
    private String[] zone3 = {"36", "30", "31", "35", "33", "32", "34", "37"};//8
    private String[] zone4 = {"46", "40", "48", "44", "49", "42", "47", "43", "39", "41", "38", "45"};//12
    private String[] zone5 = {"57", "50", "55", "56", "54", "58", "52", "51"};//8
    private String[] zone6 = {"62", "63", "60", "66", "65", "67", "64", "53", "61"};//9
    private String[] zone7 = {"71", "73", "77", "76", "70", "75", "74", "72"};//8
    private String[] zone8 = {"81", "86", "80", "82", "83", "85", "84", "92", "93"};//9
    private String[] zone9 = {"96", "94", "95", "90", "91"};//5

    public static String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }

    public BaseResponse getOk(BaseResponse object) {
        return object;
    }

    public BaseResponse getError(BaseResponse object) {
        return object;
    }

    public static String convertDateToString(Date time) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return formatter.format(time);
    }

    public static Date convertStringDateToIsoDate(String time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXX", Locale.ENGLISH);
        Date parse = formatter.parse(time);
        return parse;
    }

    public static Date convertImportDateToIsoDate(String time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date parse = formatter.parse(time);
        return parse;
    }

    public static String convertDateComplainId(Date time) {
        Format formatter = new SimpleDateFormat("yyMMdd", Locale.ENGLISH);

        return formatter.format(time);
    }

    public static Date convertLongDateToIsoDate(String time) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXX", Locale.ENGLISH);
        Date parse = (Date) formatter.parseObject(time + "");

        return parse;
    }

    public static String convertDateToShort(Date time) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));

        return formatter.format(time);
    }

    public static String convertDateToShortTH(Date time) {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy HH.mm", new Locale("th", "TH"));

        return formatter.format(time);
    }

    //TODO change from parameter to header if it possible.
    public void initialize(HttpServletRequest request) {
        localizeText = new LocalizeText();
       /* String languageCode = request.getParameter("lang");
        localizeText = getText(languageCode);*/
    }


    @Autowired
    protected Messages messages;


    public String thumbnail(MultipartFile thumbnailFile, Profile profile, String folder) {
        String thumbNailPath = saveFile(thumbnailFile, FILE_TYPE_IMAGE, profile, folder);
        return thumbNailPath;
    }

    public String uploadFile(MultipartFile thumbnailFile, Profile profile, String folder, String type) {
        String thumbNailPath = saveFile(thumbnailFile, type, profile, folder);
        return thumbNailPath;
    }

    public List<String> pdf(MultipartFile pdf, Profile profile) {
        List<String> filesList = new ArrayList<>();
        String filesPath = saveFile(pdf, FILE_TYPE_PDF, profile, FOLDER_PDF);
        if (filesPath != null || filesPath.length() > 0) {
            filesList.add(filesPath);
        }
        return filesList;
    }

    public List<String> picture(List<String> picturesList, MultipartFile[] pictures, Profile profile) {
        for (int i = 0; i < pictures.length; i++) {
            if (pictures[i] != null && !pictures[i].equals("") && !pictures[i].equals("null")) {
                String picturesPath = saveFile(pictures[i], FILE_TYPE_IMAGE, profile, FOLDER_IMAGE);
                if (picturesPath != null) {
                    picturesList.add(picturesPath);
                }
            }
        }
        return picturesList;
    }

    public List<String> pdf(List<String> fileList, MultipartFile[] file, Profile profile) {
        for (int i = 0; i < file.length; i++) {
            if (file[i] != null && !file[i].equals("") && !file[i].equals("null")) {
                String picturesPath = saveFile(file[i], FILE_TYPE_PDF, profile, FOLDER_FILES);
                if (picturesPath != null) {
                    fileList.add(picturesPath);
                }
            }
        }
        return fileList;
    }


    public List<String> deletePicture(List<String> deletePictures, List<String> picturesList) {
        for (int i = 0; i < deletePictures.size(); i++) {
            for (int j = 0; j < picturesList.size(); j++) {
                if (deletePictures.get(i).equals(picturesList.get(j))) {
                    FileInfo byFileName = fileInfoRepository.findByFileName(picturesList.get(j));
                    if (byFileName != null) {
                        fileInfoRepository.delete(byFileName);
                        try {
                            File f = new File(MEDIA_FOLDER + picturesList.get(j));           //file to be delete
                            f.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    picturesList.remove(picturesList.get(j));

                }
            }
        }
        return picturesList;
    }


    protected Profile userValidateToken(String token, HttpServletRequest reques) throws PostExceptions {

        Profile profile = null;
        if (token.equals("zealtech")) {
            profile = profileRepository.findByUserNameIs("guest");
        } else {
            String decodeToken = (String) SSOToken.decode(token, secret);
            if (decodeToken != null) {
                List<Profile> byId = profileRepository.findByIdAndEnable(decodeToken, true);
                if (byId.size() == 0) {
                    throw new PostExceptions(UNAUTHOR, localizeText.getNoUserFound());
                }
                if (byId.get(0).isEnable() == false) {
                    throw new PostExceptions(UNAUTHOR, localizeText.getNoUserFound());
                }
                profile = byId.get(0);
            } else {
                throw new PostExceptions(UNAUTHOR, localizeText.getNoUserFound());
            }
        }
        return profile;
    }


    protected Profile userValidateId(String id) throws PostExceptions {
        Profile profile = profileRepository.findById(id).get();
        if (profile == null) {
            throw new PostExceptions(FAILED, localizeText.getNoUserFound());
        } else {
            return profile;
        }
    }

    public String dateView() {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//dd/MM/yyyy
        String date = sdfDate.format(new Date());
        return date;
    }

    public void updateView(String cid, String type, String userId, int os) {
        List<ViewStatistic> byContentIdAndStatisticTypeAndUserId = viewStatisticRepository.findByContentIdAndStatisticTypeAndUserId(cid, type, userId);
        if (byContentIdAndStatisticTypeAndUserId.size()==0) {
            ViewStatistic viewStatistic = new ViewStatistic();
            viewStatistic.setContentId(cid);
            viewStatistic.setUserId(userId);
            viewStatistic.setOs(os);
            viewStatistic.setStatisticType(type);
            viewStatistic.setTime(dateView());
            viewStatisticRepository.insert(viewStatistic);
        }
    }


    public String saveFile(MultipartFile file, String fileType, Profile profile, String folder) {


        String uu = UUID.randomUUID().toString();
        String time = Long.toString(System.currentTimeMillis());
        String names[] = file.getOriginalFilename().split("\\.");
        String extension = names[names.length - 1];
        String fileName = folder + "/" + uu + time + (extension != null ? "." + extension : "");
        String name = MEDIA_FOLDER + fileName;
        String checkFolder = MEDIA_FOLDER + folder;
        Path path = Paths.get(checkFolder);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!file.isEmpty()) {
            try {
                if (fileType.equals("image")) {
                    byte[] bytes = file.getBytes();
                    File readFileFromMultipartFile = new File(name);

                    BufferedOutputStream writeFileToServer = new BufferedOutputStream(new FileOutputStream(readFileFromMultipartFile));
                    writeFileToServer.write(bytes);
                    writeFileToServer.close();

                    File readFileFromServer = new File(readFileFromMultipartFile.getPath());
                    BufferedImage image = ImageIO.read(readFileFromServer);

                    double max_size = 1600;

                    double width = image.getWidth();
                    double height = image.getHeight();

                    if (width > height) {
                        if (width > max_size) {
                            height *= max_size / width;
                            width = max_size;
                        }
                    } else {
                        if (height > max_size) {
                            width *= max_size / height;
                            height = max_size;
                        }
                    }

                    int intWidth = (int) width;
                    int intHeight = (int) height;

                    BufferedImage resized = resize(image, intWidth, intHeight);

                    readFileFromServer.delete();

                    String newFileName = folder + "/" + uu + time + ".jpg";
                    String newPathFileToServer = MEDIA_FOLDER + newFileName;

                    File output = new File(newPathFileToServer);
                    ImageIO.write(resized, "JPG", output);

                    FileInfo info = new FileInfo();
                    info.setFileType(fileType);
                    info.setFileName(newFileName);
                    info.setEditBy(profile.getId());
                    info.setFileLength(output.length());

                    fileInfoRepository.save(info);

                    return newFileName;
                } else {

                    byte[] bytes = file.getBytes();
                    File file1 = new File(name);
                    BufferedOutputStream stream =
                            new BufferedOutputStream(new FileOutputStream(file1));
                    stream.write(bytes);
                    stream.close();

                    FileInfo info = new FileInfo();
                    info.setFileType(fileType);
                    info.setFileName(fileName);
                    info.setEditBy(profile.getId());
                    info.setFileLength(file1.length());

                    fileInfoRepository.save(info);
                    return fileName;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;

        }
    }

    public String convertDigit(double location) {
        DecimalFormat df = new DecimalFormat("#.######");
        String format = df.format(location);
        return format;

    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    protected void checkSuperAdmin(Profile profile) throws PostExceptions {
        if (profile.getRole() == null || profile.getRole().getSuperAdmin() == false) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
    }

    protected void checkSuperAdminGroups(Profile profile, String groupId) throws PostExceptions {
        if (profile.getRole() == null) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
        if (profile.getRole().getSuperAdmin() == false) {
            if (!profile.getRole().getAdminGroups().contains(groupId)) {
                throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
            }
        }

    }

    protected void checkAdminGroups(Profile profile, String groupId) throws PostExceptions {
        if (profile.getRole() == null) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
        if (!profile.getRole().getAdminGroups().contains(groupId)) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }

    }
    protected void checkAdminComplain(Profile profile, List<String> groupId) throws PostExceptions {
        if (profile.getRole() == null) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
        if (profile.getRole().getSuperAdmin() == false) {
            for (String group:groupId) {
                if (!profile.getRole().getAdminGroups().contains(group) && !profile.getRole().getTechnicianGroups().contains(group)) {
                    throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
                }
            }
        }

    }

    public List<ReadGroup> setGroupName(List<ReadGroup> readGroups) {
        for (int i = 0; i < readGroups.size(); i++) {
            Optional<Group> byId = groupRepository.findById(readGroups.get(i).getGroupId());
            if (byId.isPresent() == true) {
                readGroups.get(i).setGroupName(byId.get().getName());
                readGroups.get(i).setGroupIcon(byId.get().getIcon());
                readGroups.get(i).setProvinceName(getProvinceName(byId.get().getProvinceCode()));
                readGroups.get(i).setDistrictName(getDistrictName(byId.get().getDistrictCode()));
                readGroups.get(i).setSubDistrictName(getSubDistrictName(byId.get().getSubDistrictCode()));
                readGroups.get(i).setDefault(byId.get().isMain());

            }
        }
        return readGroups;
    }

    public List<String> setRoleName(List<String> adminGroups) {
        List<String> name = new ArrayList<>();
        for (int i = 0; i < adminGroups.size(); i++) {
            Optional<Group> byId = groupRepository.findById(adminGroups.get(i));
            if (byId.isPresent() == true) {
                name.add(byId.get().getName());
            }
        }
        return name;
    }

    protected void checkAdmin(Profile profile) throws PostExceptions {
        if (profile.getRole() == null) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        } else if (profile.getRole().getSuperAdmin() == false && profile.getRole().getAdminGroups().equals(new ArrayList<>())) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());

        }
    }
    protected void checkAdminStatistic(Profile profile) throws PostExceptions {
        if (profile.getRole() == null) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        } else if (profile.getRole().getSuperAdmin() == false && profile.getRole().getAdminGroups().equals(new ArrayList<>())&&profile.getRole().getTechnicianGroups().equals(new ArrayList<>())) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());

        }
    }
    protected Pageable sortPage(int page, int sizeContents, int sort, boolean isRate, int orderSort, boolean isSequence) {
        Pageable pageable = null;
        if (isSequence==true) {
            if (sort == 1 && orderSort == 1) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("pin").descending().and(Sort.by("createDate").ascending())));
            } else if (sort == 1 && orderSort == 2) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("pin").descending().and(Sort.by("createDate").descending())));
            } else if (sort == 3 && orderSort == 1) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("createDate").ascending()));
            } else if (sort == 3 && orderSort == 2) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("createDate").descending()));
            } else if (sort == 4 && orderSort == 1) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("enable").ascending()));
            } else if (sort == 4 && orderSort == 2) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("enable").descending()));
            } else {
                //sort == 2
                if (orderSort == 1) {
                    if (isRate == false) {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("cntView").ascending()));
                    } else {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("rate").ascending()));
                    }
                } else if (orderSort == 2) {
                    if (isRate == false) {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("cntView").descending()));
                    } else {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("sequence").ascending().and(Sort.by("rate").descending()));
                    }
                }
            }
        }else {
            if (sort == 1 && orderSort == 1) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("pin").descending().and(Sort.by("createDate").ascending()));
            } else if (sort == 1 && orderSort == 2) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("pin").descending().and(Sort.by("createDate").descending()));
            } else if (sort == 3 && orderSort == 1) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("createDate").ascending());
            } else if (sort == 3 && orderSort == 2) {
                pageable = PageRequest.of(page, sizeContents,Sort.by("createDate").descending());
            } else if (sort == 4 && orderSort == 1) {
                pageable = PageRequest.of(page, sizeContents,Sort.by("enable").ascending());
            } else if (sort == 4 && orderSort == 2) {
                pageable = PageRequest.of(page, sizeContents, Sort.by("enable").descending());
            } else {
                //sort == 2
                if (orderSort == 1) {
                    if (isRate == false) {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("cntView").ascending());
                    } else {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("rate").ascending());
                    }
                } else if (orderSort == 2) {
                    if (isRate == false) {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("cntView").descending());
                    } else {
                        pageable = PageRequest.of(page, sizeContents, Sort.by("rate").descending());
                    }
                }
            }
        }
        return pageable;
    }


    public void sendAdminComplainNotification(Complain complain) {
        List<Profile> profileList = getAdminComplainNotification(complain);

        for (int i = 0; i < profileList.size(); i++) {
            if (profileList.get(i).getGcmToken() != null && !profileList.get(i).getGcmToken().equals("")) {
                GcmSender.sendInformComplain(complain.getId(), TYPE_NOTI_ADMIN, profileList.get(i).getGcmToken(), 0, GCM_KEY, true, complain.getGroupId(), complain.getComplainId());
            }
        }
    }

    public List<Profile> getAdminComplainNotification(Complain complain) {

        Query query = new Query();
        Criteria adminGroups = Criteria.where("role.adminGroups").in(complain.getGroupId());
        Criteria technicianGroups = Criteria.where("role.technicianGroups").in(complain.getGroupId());

        query.addCriteria(new Criteria().orOperator(adminGroups,technicianGroups));

        List<Profile> profileList = mongoTemplate.find(query, Profile.class);
        return profileList;

    }


    public void checkRoleCommentComplain(Profile profile, Complain complain) throws PostExceptions {
        if (!complain.getAuthor().equals(profile.getId()) && (profile.getRole() == null ||(profile.getRole().getSuperAdmin() == false&& !profile.getRole().getAdminGroups().contains(complain.getGroupId())&&!profile.getRole().getTechnicianGroups().contains(complain.getGroupId())))) {
            throw new PostExceptions(FAILED, localizeText.getPermissionDenied());
        }
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

    public String getCateName(String category) {
        String name = "";
        Category byCategoryCode = categoryRepository.findByCategoryCode(category);
        if (byCategoryCode != null) {
            name = byCategoryCode.getCategoryName();
        }

        return name;
    }

    public List<String> getCateNameList(List<String> category) {
        List<String> categoryProfile = new ArrayList<>();
        for (int i = 0; i < category.size(); i++) {
            Category byCategoryCode = categoryRepository.findByCategoryCode(category.get(i));
            if (byCategoryCode != null) {
                categoryProfile.add(byCategoryCode.getCategoryName());
            }
        }
        return categoryProfile;
    }


    public String getProvinceName(String code) {
        String name = "";
        if (code.length() > 0) {
            Query query = Query.query(Criteria.where("province_code").is(Integer.parseInt(code)));
            List<Province> province = mongoTemplate.find(query, Province.class);
            if (province.size() > 0) {
                name = province.get(0).getProvince_th();
            }
        }

        return name;
    }

    public String getDistrictName(String code) {
        String name = "";
        if (code.length() > 0) {
            Query query = Query.query(Criteria.where("district_code").is(Integer.parseInt(code)));
            List<District> district = mongoTemplate.find(query, District.class);
            if (district.size() > 0) {
                name = district.get(0).getDistrict_th();
            }
        }

        return name;
    }

    public String getSubDistrictName(String code) {
        String name = "";
        if (code.length() > 0) {
            Query query = Query.query(Criteria.where("subDistrict_code").is(Integer.parseInt(code)));
            List<SubDistrict> subDistrict = mongoTemplate.find(query, SubDistrict.class);
            if (subDistrict.size() > 0) {
                name = subDistrict.get(0).getSubDistrict_th();
            }
        }

        return name;
    }
    public String getZipcode(String code) {
        String name = "";
        if (code.length() > 0) {
            Query query = Query.query(Criteria.where("subDistrict_code").is(Integer.parseInt(code)));
            List<SubDistrict> subDistrict = mongoTemplate.find(query, SubDistrict.class);
            if (subDistrict.size() > 0) {
                name = subDistrict.get(0).getPostal_code();
            }
        }

        return name;
    }

    public Group getGroupProfile(String id) {
        Group group = null;
        if (id.length() > 0) {
            Optional<Group> byId = groupRepository.findById(id);
            if (byId.isPresent() == true) {
                group = byId.get();
            } else {
                group = null;
            }
        }
        return group;
    }
        public void sendPushNotification (String type, String id, String title, String groupId, String TOPIC) throws
        PostExceptions {

            if (type.equals(TYPE_NEWS)) {
                GcmSender.send(id, title, type, "", true, GCM_KEY, groupId, TOPIC);
                News byId = newsRepository.findById(id).get();
                byId.setPushAlready(true);
                byId.setSendNotiDate(new Date());
                newsRepository.save(byId);
            } else if (type.equals(TYPE_NOTIFICATIONS)) {
                GcmSender.sendForNotification(id, title, type, 1, groupId, notificationsRepository, GCM_KEY, TOPIC);
//                Notifications byId = notificationsRepository.findById(id).get();
//                if (byId.getStatus() != 3) {
//                    byId.setStatus(2);
//                    byId.setSendNotiDate(new Date());
//                    byId.setPushAlready(true);
//                    notificationsRepository.save(byId);
//                }
            }
        }


    }
