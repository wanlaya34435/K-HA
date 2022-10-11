package com.zti.kha.api;
import com.zti.kha.model.Base.BaseResponse;
import com.zti.kha.model.Common.FileInfo;
import com.zti.kha.model.Common.RudeWord;
import com.zti.kha.model.Content.*;
import com.zti.kha.model.Content.Noti.Notifications;
import com.zti.kha.model.Statistic.ViewStatistic;
import com.zti.kha.model.TestMulti;
import com.zti.kha.model.User.Profile;
import com.zti.kha.utility.ErrorFactory;
import com.zti.kha.utility.PostExceptions;
import com.zti.kha.utility.WriteFileStream;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by up on 2/20/17.
 */
@RestController
public class PostApi extends CommonApi {

    public PostApi() throws Exception {
    }

    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse main_page(HttpServletRequest request) {



        return getOk(new BaseResponse(OK,"welcome"));
    }


    @CrossOrigin
    @RequestMapping(value = "/deletePost", method = RequestMethod.POST)
    public BaseResponse deletePost(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "") String id
            , @RequestParam(value = "name", defaultValue = "") @ApiParam(value = "news,service,banner,event,notifications,knowledge") String name) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        if (name.equals(TYPE_NEWS)) {
            News post = newsRepository.findById(id).get();
            checkAdminGroups(profile,post.getGroupId());
            if (post == null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }
            newsRepository.delete(post);
            clearData(id, TYPE_NEWS);
        } else if (name.equals(TYPE_BANNER)) {
            Banner post = bannerRepository.findById(id).get();
            checkAdminGroups(profile,post.getGroupId());
            if (post == null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }
            bannerRepository.delete(post);
            clearData(id, TYPE_BANNER);
        } else if (name.equals(TYPE_EVENT)) {
            Event post = eventRepository.findById(id).get();
            checkAdminGroups(profile,post.getGroupId());
            if (post == null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }
            eventRepository.delete(post);
            clearData(id, TYPE_EVENT);
        } else if (name.equals(TYPE_NOTIFICATIONS)) {
            Notifications post = notificationsRepository.findById(id).get();
            checkAdminGroups(profile,post.getGroupId());
            if (post == null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }
            notificationsRepository.delete(post);
            clearData(id, TYPE_NOTIFICATIONS);
        }else if (name.equals(TYPE_SERVICE)) {
            Service post = serviceRepository.findById(id).get();
            checkAdminGroups(profile,post.getGroupId());
            if (post == null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }
            serviceRepository.delete(post);
            clearData(id, TYPE_SERVICE);
        }else if (name.equals(TYPE_KNOWLEDGE)) {
            Knowledge post = knowledgeRepository.findById(id).get();
            checkAdminGroups(profile,post.getGroupId());
            if (post == null) {
                return getError(ErrorFactory.getError(FAILED, localizeText.getNoContent()));
            }
            knowledgeRepository.delete(post);
            clearData(id, TYPE_KNOWLEDGE);
        }

        return getOk(new BaseResponse(OK, localizeText.getDeleted()));

    }


    @CrossOrigin
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public BaseResponse uploadImage(HttpServletRequest request
            , @RequestParam("file") MultipartFile file
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        String newFileName = saveFile(file, FILE_TYPE_IMAGE, profile, FOLDER_IMAGE);

        return getOk(new BaseResponse(OK, localizeText.getFileUploaded(), newFileName));

    }
    @CrossOrigin
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public BaseResponse uploadFile(HttpServletRequest request
            , @RequestParam("file") MultipartFile file
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        String newFileName = saveFile(file, FOLDER_FILES, profile, FOLDER_FILES);



        return getOk(new BaseResponse(OK, localizeText.getFileUploaded(), newFileName));

    }
    @CrossOrigin
    @RequestMapping(value = "/uploadMulti", method = RequestMethod.POST)
    public BaseResponse uploadMulti(HttpServletRequest request
            , @RequestParam("file") MultipartFile [] file
            , @RequestParam("type")@ApiParam(value = "image or file") String type
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        List<String> newFileName =new ArrayList<>();
        if (type.equals("image")){
            newFileName = picture(new ArrayList<>(), file, profile);

        }else if (type.equals("file")) {
             newFileName = pdf(new ArrayList<>(), file, profile);
        }
        TestMulti testMulti = new TestMulti(newFileName);
        return getOk(new BaseResponse(OK, localizeText.getFileUploaded(), testMulti));

    }
    @CrossOrigin
    @RequestMapping(value = "/uploadImageProfile", method = RequestMethod.POST)
    public BaseResponse handleFileUpload(HttpServletRequest request
            , @RequestParam("file") MultipartFile file
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token) throws PostExceptions {
        initialize(request);
        Profile profile = userValidateToken(token, request);
        if (profile != null) {
            String uu = UUID.randomUUID().toString();
            String time = Long.toString(System.currentTimeMillis());
            String names[] = file.getOriginalFilename().split("\\.");
            String extension = names[names.length - 1];
            String fileName = FOLDER_PROFILE + "/" + uu + time + (extension != null ? "." + extension : "");
            String name = MEDIA_FOLDER+ fileName;
            String checkFolder = MEDIA_FOLDER+ FOLDER_PROFILE;
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
                    String newFileName = FOLDER_PROFILE + "/" + uu + time + ".jpg";
                    String newPathFileToServer = MEDIA_FOLDER+ newFileName;
                    File output = new File(newPathFileToServer);
                    ImageIO.write(resized, "JPG", output);

//                     Add file to data common
                    FileInfo info = new FileInfo();
                    info.setFileType("image");
                    info.setFileName(newFileName);
                    info.setEditBy(profile.getId());
                    info.setFileLength(output.length());
                    fileInfoRepository.save(info);

                    return getOk(new BaseResponse(OK, localizeText.getFileUploaded(), newFileName));

                } catch (Exception e) {
                    return getError(ErrorFactory.getError(FAILED, "You failed to upload " + name + " => " + e.getMessage()));

                }
            } else {
                return getError(ErrorFactory.getError(FAILED, "You failed to upload " + name + " because the file was empty."));
            }


        } else {
            return getError(ErrorFactory.getError(FAILED, localizeText.getNoUserFound()));
        }


    }

    public static final String FILE_TYPE_IMAGE = "image";
    public static final String FILE_TYPE_VIDEO = "mp4";

    @CrossOrigin
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void getDownload(HttpServletResponse response, HttpServletRequest request
            , @RequestParam(value = "file", defaultValue = "") String fileName) throws PostExceptions {

        // Get your file stream from wherever.
        initialize(request);

            String filePath = MEDIA_FOLDER + fileName;
            WriteFileStream writeFileStream = new WriteFileStream(filePath);
            try {
                writeFileStream.writeStream(request, response);
            } catch (IOException e) {
                e.printStackTrace();

            }



    }

    private void clearData(String contentId, String type) {

        List<ViewStatistic> byContentIdAndStatisticType = viewStatisticRepository.findByContentIdAndStatisticType(contentId, type);
        viewStatisticRepository.deleteAll(byContentIdAndStatisticType);


    }
    @CrossOrigin
    @RequestMapping(value = "/deleteRudeWord", method = RequestMethod.POST)
    public BaseResponse deleteRudeWord(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "", required = true) String id) throws PostExceptions {

        initialize(request);
        Profile adminProfile = userValidateToken(token,request);
        checkSuperAdmin(adminProfile);
        RudeWord rudeWord = rudeWordRepository.findById(id).get();
        rudeWordRepository.delete(rudeWord);
        return getOk(new BaseResponse(OK, localizeText.getDeleted()));
    }

    @CrossOrigin
    @RequestMapping(value = "/addRudeWord", method = RequestMethod.POST)
    public BaseResponse addRudeWord(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "name", defaultValue = "") String name
    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkSuperAdmin(profile);
        boolean b = rudeWordRepository.findByName(name).size() > 0;
        if (b) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicate()));

        } else {
            RudeWord rudeWord = new RudeWord();
            rudeWord.setName(name);
            rudeWordRepository.insert(rudeWord);
            return getOk(new BaseResponse());
        }

    }
    @CrossOrigin
    @RequestMapping(value = "/editRudeWord", method = RequestMethod.POST)
    public BaseResponse editRudeWord(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "id", defaultValue = "") String id
            , @RequestParam(value = "name", defaultValue = "") String name
    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);
        checkSuperAdmin(profile);
        RudeWord rudeWord = rudeWordRepository.findById(id).get();
        boolean b = rudeWordRepository.findByName(name).size() > 0;
        if (b) {
            return getError(ErrorFactory.getError(FAILED, localizeText.getDuplicate()));

        } else {
            rudeWord.setName(name);
            rudeWordRepository.save(rudeWord);
            return getOk(new BaseResponse());
        }

    }

    @CrossOrigin
    @ApiOperation(value = "คำหยาบ", notes = "",response = RudeWord.class)
    @RequestMapping(value = "/getRudeWord", method = RequestMethod.GET)
    public BaseResponse getRudeWord(HttpServletRequest request
            , @RequestHeader(value = "token", defaultValue = TOKEN) String token
            , @RequestParam(value = "keyWord", defaultValue = "", required = false) String keyWord
    ) throws PostExceptions {

        initialize(request);
        Profile profile = userValidateToken(token, request);

        List<RudeWord> all;
        if (keyWord != null && keyWord.length() > 0) {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").regex(keyWord));
            all = mongoTemplate.find(query, RudeWord.class);

        } else {
            all = rudeWordRepository.findAll();
        }
        return getOk(new BaseResponse(all));
    }

   /* @CrossOrigin
    @RequestMapping(value = "/generateRude", method = RequestMethod.POST)
    public BaseResponse generateRude(HttpServletRequest request) throws PostExceptions, InvalidKeySpecException, NoSuchAlgorithmException, FileNotFoundException, UnsupportedEncodingException, FileNotFoundException {

        initialize(request);

        String csvFile = "C:/Work/Plan/koratCity/rude.csv";

        Scanner scanner = new Scanner(new File(csvFile), "UTF-8");
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine());

            if (line.get(0).length() > 0) {
                RudeWord rudeWord = new RudeWord();
                rudeWord.setName(line.get(0));
                rudeWordRepository.save(rudeWord);

            } else {
//                System.out.println(line.get(0) +"," +line.get(1)+"," +line.get(2)+"," +line.get(3)+"," +line.get(4)+"," +line.get(5)+"," +line.get(6));
            }
        }
        scanner.close();
        //TODO check permission if user is admin role

        return getOk(new BaseResponse(OK));
    }*/
}




