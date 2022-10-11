package com.zti.framework.utils;

import com.google.gson.Gson;
import com.zti.kha.component.Messages;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


public class Utils {

	static Logger log = LogManager.getLogger(Utils.class);

	public static <T> T copy(Object object, Class c){
		return (T) new Gson().fromJson(new Gson().toJson(object), c);
	}

	public static void writeHttpResponse(HttpServletResponse response, String message) throws IOException {
//		response.setContentType("application/json;charset=UTF-8");

		ServletOutputStream sout = response.getOutputStream();
		sout.print(message);
		sout.flush();
	}

	/**
	 * Check empty String parameters
	 * @param params
	 * @return
	 */
	public static boolean isEmpty(String... params){
		if(params != null && params.length > 0){
			for(String p : params){
				if(p == null || p.trim().equalsIgnoreCase("") || p.trim().length() == 0){
					return true;
				}
			}
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Check empty Object parameters
	 *
	 * @param objs
	 * @return
	 */
	public static boolean isEmptyObj(Object... objs){
		if(objs != null && objs.length > 0){
			
			
			//================================//
			// LIST or ARRAY LIST			  //
			//================================//
			if(objs[0] instanceof List || objs[0] instanceof ArrayList){
				List object = (List)objs[0];
				if(object != null && object.size() > 0){
					return false;
				}else{
					return true;
				}
			}
			
			//================================//
			// STRING						  //
			//================================//
			else if(objs[0] instanceof String){
				for(String p : (String[])objs){
					if(p == null || p.equalsIgnoreCase("") || p.length() == 0){
						return true;
					}
				}
			}
			
			return false;
		}
		else{
			return true;
		}	
	}
	
	
	public static boolean isEmptyFile(MultipartFile sourceFile){
		if(sourceFile != null && !sourceFile.getOriginalFilename().equalsIgnoreCase("")){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * Convert to CDATA tag
	 * @param text
	 * @return
	 */
	public static String toCDATA(String text){
		return "<![CDATA["+ text + "]]>";
	}
	
	/**
	 * Print stack trace to String
	 * @param e
	 * @return
	 */
	public static String stackTraceToString(Throwable e) {
	     final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     e.printStackTrace(pw);
	     
	     String stackTraceStr = sw.getBuffer().toString();

	     return stackTraceStr;
	}
	
	/**
	 * Get current datetime in timestamp
	 * @return
	 */
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	public static String getDatetime(String format){
		SimpleDateFormat sdfDate = new SimpleDateFormat(format);
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}

	public static String resizeImage(String absolutePath, int max_pixel){
		String absPath = null;
		try{

			File currentFile = new File(absolutePath);
			BufferedImage image = ImageIO.read(currentFile);
			double max_size = max_pixel;
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
			BufferedImage resized = Utils.resize(image, intWidth, intHeight);

			// create new resize file
			String fileName = currentFile.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
			absPath = replaceFileName(currentFile, "resize_" + fileName, ".jpg");

			ImageIO.write(resized, "JPG", new File(absPath));

			// delete current file
			FileUtils.forceDelete(currentFile);
		}catch(Exception ex){
//			ex.printStackTrace();
            log.error(ex);

        }

		return absPath;
	}

	public static BufferedImage resize(BufferedImage img, int width, int height) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}

	public static String replaceFileName(File file, String newName, String extension){
		String filePath = file.getParentFile().getAbsolutePath();

		return filePath + File.separator + newName + extension;
	}
	
	public static String readTextfromFile(String fullPathFile) throws IOException {
	
		StringBuilder str = new StringBuilder();

		try(
				FileInputStream fis = new FileInputStream(new File(fullPathFile));
				BufferedReader buffR = new BufferedReader(new InputStreamReader(fis)))
		{
			String line = null;
			while((line = buffR.readLine()) != null){
				str.append(line);
			}

		}catch(Exception ex){
			log.error(ex);
		}

		return str.toString();
	}
	
	/**
	 * Write text string into a file.
	 * @param text
	 * @param fullPathFile
	 */
	public static void writeText2File(String text, String fullPathFile) throws IOException {

		try(
				FileOutputStream fos = new FileOutputStream(fullPathFile);
				Writer out = new OutputStreamWriter(fos, "utf-8");
				){
			out.write(text);

			out.flush();
		}catch(Exception ex){
           log.error(ex);
		}
	}
	
	/**
	 * Create directory
	 * @param DIR_PATH
	 */
	public static void createDirectory(String DIR_PATH){
		File cDir = new File(DIR_PATH);
		if(!cDir.exists()){
			cDir.mkdirs();
		}
	}

	public static void createDirectoryFromAbs(String ABS_PATH){
		String path = new File(ABS_PATH).getParentFile().getAbsolutePath();
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	public static void deleteFile(String fileLocation){
		if(fileLocation != null) {
			File file = new File(fileLocation);
			if(file.exists()){
				try {
					FileUtils.forceDelete(file);
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}
	
	public static boolean isOSWindows(){
		String yourOS = System.getProperty("os.name").toLowerCase();
		if (yourOS.indexOf("win") >= 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public static String fileSeparator(){
		String yourOS = System.getProperty("os.name").toLowerCase();
		String fileseparator = "/";
		if (yourOS.indexOf("win") >= 0) {
			//if windows
			fileseparator = "\\";
 
		} else if (yourOS.indexOf("nix") >= 0 ||
				yourOS.indexOf("nux") >= 0 ||
				yourOS.indexOf("mac") >= 0) {
 
			//if unix or mac 
			fileseparator = "/";
 
		}
		return fileseparator;
	}
	
	@SuppressWarnings("finally")
	public static Timestamp convertStringToTimeStamp(String dateStr,String DateFormat){		
		 SimpleDateFormat formatter = new SimpleDateFormat(DateFormat);
		 Timestamp timestamp = null;
		 try {
			 Date date = formatter.parse(dateStr);
             timestamp = new  Timestamp(date.getTime());
	     } catch (ParseException e) {
	           log.error(e);
	     }
		return timestamp;
	}
	
	public static Timestamp convertStringToTimeStampMultiFormat(String input){				
		List<SimpleDateFormat> dateFormats  = new ArrayList<SimpleDateFormat>();
        dateFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
        dateFormats.add(new SimpleDateFormat("MMM dd, yyyy h:m:s a")); 
	    for (SimpleDateFormat formatter : dateFormats) {
			 try {
				 Date date = formatter.parse(input);
	             return new  Timestamp(date.getTime());
		     } catch (ParseException e) {
                log.error(e);
		     } 
	    }		
	    return null;
	}
	
	public static Date convertStringToDate(String input){		
		 List<SimpleDateFormat> dateFormats  = new ArrayList<SimpleDateFormat>();
         dateFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
         dateFormats.add(new SimpleDateFormat("yyyy-MM-dd")); 		 
         for (SimpleDateFormat formatter : dateFormats) {
			 try {
				 Date date = formatter.parse(input);
	             return date;
		     } catch (ParseException e) {
               log.error(e);
		     } 
	    }		
	    return null;
	}

	public static String base64encode(String text) {       
        byte[] encodedBytes = Base64.getEncoder().encode(text.getBytes());
        return new String(encodedBytes);
        
    }//base64encode

    public static String base64decode(String text) {              
        byte[] decodedBytes = Base64.getDecoder().decode(text.getBytes());
        return new String(decodedBytes);
        
    }//base64decode

	public static String xorMessage(String message, String key) {
        try {
            if (message == null || key == null) return null;

            char[] keys = key.toCharArray();
            char[] mesg = message.toCharArray();

            int ml = mesg.length;
            int kl = keys.length;
            char[] newmsg = new char[ml];

            for (int i = 0; i < ml; i++) {
                newmsg[i] = (char)(mesg[i] ^ keys[i % kl]);
            }//for i

            return new String(newmsg);
        } catch (Exception e) {
            return null;
        }
    }//xorMessage
	
	
	public static String convertAD2ThaiYYYY(String pattern, String dateStr){
		int index = pattern.indexOf("YYYY");
		if(index == -1){
			index = pattern.indexOf("yyyy");
		}
		
		String result = null;
		if(dateStr != null){
			String yearStr = dateStr.substring(index, index+4);
			int adYear = Integer.parseInt(yearStr);
			int year = 0;
			if(adYear < 2400){
				year = adYear + 543;
			}else{
				year = adYear;
			}
			dateStr = dateStr.replace(""+adYear, ""+year);
		}
		return dateStr;
	}
	
	public static String convertThai2ADYYYY(String pattern, String dateStr){
		int index = pattern.indexOf("YYYY");
		if(index == -1){
			index = pattern.indexOf("yyyy");
		}
		
		String result = null;
		if(dateStr != null){
			String yearStr = dateStr.substring(index, index+4);
			int adYear = Integer.parseInt(yearStr);
			int year = 0;
			if(adYear < 2400){
				year = adYear + 543;
			}else{
				year = adYear - 543;
			}
			dateStr = dateStr.replace(""+adYear, ""+year);
		}
		return dateStr;
	}
	
	public static String covert2ThaiDate(Date date, Messages messages){
		return covert2ThaiDate(date, true, true, messages);
	}
	
	
	public static String covert2ThaiDate(Date date, boolean displayTime, Messages messages){
		return covert2ThaiDate(date, true, displayTime, messages);
	}
	
	private static String covert2ThaiDate(Date date, boolean displayDate, boolean displayTime, Messages messages){
		String result = null;
		if(date != null){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			String day = c.get(Calendar.DAY_OF_MONTH)+"";
			if(day.length() == 1){
				day = "0" + day;
			}
			
			int month = c.get(Calendar.MONTH)+1;
			int year = c.get(Calendar.YEAR);
			if(year < 2500){
				year = year + 543;
			}
			String hh = c.get(Calendar.HOUR_OF_DAY)+"";
			String mm = c.get(Calendar.MINUTE)+"";
			if(hh.length() == 1){
				hh = "0" + hh;
			}
			if(mm.length() == 1){
				mm = "0" + mm;
			}
			
			String monthStr = messages.get("month."+month+".abbr");
			
			String date_ = day + " " + monthStr + " " + year;
			String time_ = hh + ":" + mm;
			if(displayDate){
				result = date_;
			}
			if(displayTime){
				result += " " + time_ + " " + messages.get("clock.abbr");
			}
		}
		return result;
	}
	
	public static Date convertString2Date(String pattern, String dateStr){
		DateFormat fm = new SimpleDateFormat(pattern);
		try {
			return fm.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//            log.error(e);
		}
		return new Date(System.currentTimeMillis());
	}
	
	public static String convertDate2String(String pattern, Date dateStr){
		if(dateStr != null){
			DateFormat fm = new SimpleDateFormat(pattern);
			return fm.format(dateStr);
		}else{
			return null;
		}
	}
	
	public static <T> T mergeObjects(T first, T second) throws IllegalAccessException, InstantiationException {
	    Class<?> clazz = first.getClass();
	    Field[] fields = clazz.getDeclaredFields();
	    Object returnValue = clazz.newInstance();
	    for (Field field : fields) {
	        field.setAccessible(true);
	        Object value1 = field.get(first);
	        Object value2 = field.get(second);
	        Object value = null;
    		if(value1 != null) {
				value = mergeObjectsValue(value1, value2, value);
			} else {
				value = value2;
			}
	        field.set(returnValue, value);
	    }
	    return (T) returnValue;
	}

	private static Object mergeObjectsValue(Object value1, Object value2, Object value){
		Object newValue = value;
		if(value2 != null){
			newValue = value2;
		} else {
			newValue = value1;
		}
		return newValue;
	}
	
	public static void writeFileNetwork(HttpServletResponse response, String username, String password, String srcFilePath, boolean forceDownload) throws IOException {
		BufferedOutputStream out = null;
		OutputStream os = null;

		String account = username + ":" + password;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(account);
		SmbFile sFile = new SmbFile(srcFilePath, auth);
		try(
				SmbFileInputStream fis = new SmbFileInputStream(sFile);
				BufferedInputStream in = new BufferedInputStream(fis);
				){

			if(forceDownload){
				response.setContentType("application/octet-stream");
			}else{
				String extension = sFile.getName().substring(sFile.getName().indexOf(".")+1);
				if(extension.endsWith("jpg")){
					response.setContentType("image/jpeg");
				}
				else if(extension.endsWith("png")){
					response.setContentType("image/png");
				}
				else if(extension.endsWith("mp4")){
					response.setContentType("video/mp4");
				}
				else if(extension.endsWith("mp3")){
					response.setContentType("video/mp3");
				}

			}

			response.setHeader("Content-Disposition", "filename=\""+sFile.getName()+"\"");

			os = new BufferedOutputStream(response.getOutputStream());
			out = new BufferedOutputStream(os);
			byte[] buffer = new byte[1024 * 8];
			int j = -1;
			while ((j = in.read(buffer)) != -1) {
			    out.write(buffer, 0, j);
			}

			os.flush();
			out.flush();
		}catch(Exception ex){
            log.error(ex);
		}finally{
			if(out != null) {
				out.close();
			}
			if(os != null) {
				os.close();
			}
		}
	
	}
	
	/**
	 * Create directory in mapped network (for Windows).
	 * @param username
	 * @param password
	 * @param dirs
	 */
	public static boolean makeDirsNetwork(String username, String password, String dirs){
		try{
			String account = username + ":" + password;
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(account);
			SmbFile sFile = new SmbFile(dirs, auth);
			if(!sFile.exists()){
				sFile.mkdirs();
			}
			
			return sFile.exists();
		}catch(Exception ex){
            log.error(ex);
		}
		return false;
	}
	
	
	/**
	 * Create file in mapped network's directory.
	 * @param username
	 * @param password
	 * @param srcPathFile
	 * @param destPathFile
	 */
	public static void createFileNetwork(String username, String password, String srcPathFile, String destPathFile) throws IOException {
		String account = username + ":" + password;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(account);

		SmbFile destFile = new SmbFile(destPathFile, auth);
		try(
				FileInputStream fis = new FileInputStream(srcPathFile);
				SmbFileOutputStream sfos = new SmbFileOutputStream(destFile);

				){
			byte[] buf = new byte[1024];
	        int len;
	        while((len=fis.read(buf))>0){
	        	sfos.write(buf,0,len);
	        }
	        sfos.flush();
		}catch(Exception ex){
            log.error(ex);
		}
	}
	
	/**
	 * Create file in mapped network's directory.
	 * @param username
	 * @param password
	 * @param srcBytes
	 * @param destPathFile
	 */
	public static void createFileNetwork(String username, String password, byte[] srcBytes, String destPathFile) throws IOException {
		String account = username + ":" + password;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(account);

		SmbFile destFile = new SmbFile(destPathFile, auth);

		try(
				SmbFileOutputStream sfos = new SmbFileOutputStream(destFile);
				){

			sfos.write(srcBytes);
			sfos.flush();
		}catch(Exception ex){
            log.error(ex);
		}finally{
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param deleteFilePath
	 */
	public static void deleteFileNetwork(String username, String password, String deleteFilePath){
		try{
			String account = username + ":" + password;
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(account);			

			SmbFile destFile = new SmbFile(deleteFilePath, auth);			
			destFile.delete();
			
		}catch(Exception ex){
            log.error(ex);
		}finally{
			
		}
	}
	
	
	/**
	 * 
	 * @param request
	 * 
	 * @return
	 */
	public static String getCompleteRequestUrl(HttpServletRequest request) {
		String reqUrl = request.getRequestURL().toString();
		String queryString = request.getQueryString(); // d=789
		if (queryString != null) {
			reqUrl += "?" + queryString;
		}
		return reqUrl;
	}

	public static long dateDiff(Timestamp start, Timestamp end){
		long diff = 0;
		diff = end.getTime() - start.getTime();
		diff = diff / (24 * 60 * 60 * 1000);
		return diff;
	}

	public static long hourDiff(Timestamp start, Timestamp end){
		long diff = 0;
		diff = end.getTime() - start.getTime();
		diff = diff / (60 * 60 * 1000);
		return diff;
	}


	public static long minuteDiff(Timestamp start, Timestamp end){
		long diff = 0;
		diff = end.getTime() - start.getTime();
		diff = diff / (60 * 1000);
		return diff;
	}
}
