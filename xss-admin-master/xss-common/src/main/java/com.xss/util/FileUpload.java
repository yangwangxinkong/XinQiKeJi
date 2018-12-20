package com.xss.util;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.Date;

public class FileUpload {
//	private final static String ROOT_PATH = System.getProperty("user.dir").substring(0,System.getProperty("user.dir").length()-3)+"webapps/"; //该项目下的路径
	
	private static String ROOT_PATH = "";
	
	/**
	 * 存储图片到指定路径
	 * @param path
	 * @param file
	 */
	public static void saveFile(String path,File file){
		try {			
			FileInputStream fis = new FileInputStream(file);
			byte b[] = new byte[fis.available()];
			fis.read(b);

			File savefile = new File(ROOT_PATH + path);
//			System.out.println("aa===="+ROOT_PATH + path);
			FileOutputStream fos = new FileOutputStream(savefile);
			fos.write(b);

			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String gemPath(String solarId,String name, String rootPath){		
		Date date = new Date();
		String firstfolder = DateUtil.format(date,"yyyy")+"/";
		String secondfolder = DateUtil.format(date,"MMdd")+"/";
		name = name.substring(name.lastIndexOf("."));
		if(!"".equals(firstfolder)){
			createDir(rootPath+firstfolder);		
			if(!"".equals(secondfolder)) {
				createDir(rootPath + firstfolder + secondfolder);
			}
		}		
		return firstfolder+secondfolder+solarId+name;
	}
	
	public static void main(String[] args){
		

//		System.out.println("year======="+DateUtil.format(date,"yyyy"));
//		System.out.println("date======="+DateUtil.format(date,"MMdd"));
	}
	
	/**
	 * 图片存储路径
	 * @param solarId
	 * @param name
	 * @return
	 */
	public static String gemPath(String solarId,String name){
		return gemPath(solarId, name, ROOT_PATH);
	}
	
	public static String gemPath(Long longDate,String name, String rootPath){
		Date date = new Date();
		String firstfolder = DateUtil.format(date,"yyyy");
		String secondfolder = DateUtil.format(date,"MMdd");
		name = name.substring(name.lastIndexOf("."));
		if(!"".equals(firstfolder)){
			createDir(rootPath+firstfolder);		
			if(!"".equals(secondfolder)) {
				createDir(rootPath + firstfolder + secondfolder);
			}
		}		
		return firstfolder+secondfolder+longDate+name;
	}
	
	public static String gemPath(Long date,String name){
		return gemPath(date, name, ROOT_PATH);
	}
	/**
	 * 根据文件后缀判断上传的是否是图片文件
	 * @param name
	 * @return
	 */
	public static boolean ifPicture(String name){
		String suffix = name.substring(name.lastIndexOf(".")+1).toLowerCase();
		if(suffix.equals("jpg")||suffix.equals("jpeg")
				||suffix.equals("gif")||suffix.equals("bmp")||suffix.equals("png")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 如果该文件夹不存在，创建新文件夹
	 * @param path
	 */
	public static void createDir(String path){
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
	}	
	/**
	 * 删除文件
	 * @param src
	 */
	public static void deleteFile(String src){
		File file =new File(ROOT_PATH+src);
		if(file.exists()) {
			file.delete();
		}
	}
	
	public static boolean existPdf(String filename){
		File file = new File("");
//		if(ROOT_PATH.indexOf("ROOT")>0)
//			file = new File(ROOT_PATH.replace("ROOT","")+"solarf_uploads/pdf/"+filename+".pdf");		
//		else
			file = new File(ROOT_PATH+"solarf_uploads/pdf/"+filename+".pdf");		
		return file.exists();
	}
	/** 
	 * 通过BASE64Decoder解码，并生成图片 
	 * @param imgStr 解码后的string 
	 */  
	public static boolean string2Image(String imgStr, String imgFilePath) {  
	    // 对字节数组字符串进行Base64解码并生成图片  
	    if (imgStr == null) {
			return false;
		}
	    try {  
	        // Base64解码  
	        byte[] b = new BASE64Decoder().decodeBuffer(imgStr.split(",")[1]);  
	        for (int i = 0; i < b.length; ++i) {  
	            if (b[i] < 0) {  
	                // 调整异常数据  
	                b[i] += 256;  
	            }  
	        }  
	        // 生成Jpeg图片  
	        OutputStream out = new FileOutputStream(imgFilePath);  
	        out.write(b);  
	        out.flush();  
	        out.close();  
	        return true;  
	    } catch (Exception e) {  
	    	e.printStackTrace();
	        return false;  
	    }  
	}
}
