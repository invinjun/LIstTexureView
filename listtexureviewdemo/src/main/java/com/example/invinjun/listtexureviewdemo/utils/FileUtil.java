package com.example.invinjun.listtexureviewdemo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
/**
 * 文件工具�?
 * @author spring sky
 * Email:vipa1888@163.com
 * QQ:840950105
 * name:石明�?
 *
 */
public class FileUtil {
	/**
	 * 检查SD卡状态
	 * @return boolean
	 */
	public static boolean checkSDCard()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 保存文件
	 * @param context
	 * @return  文件保存的目录
	 * @throws IOException 
	 */
	public static String setMkdir(Context context) throws IOException
	{
		String filePath;
		if(checkSDCard())
		{
			filePath = Environment.getExternalStorageDirectory()+File.separator+"CTBRIVideo";
		}else{
			filePath = context.getCacheDir().getAbsolutePath()+File.separator+"CTBRIVideo";
		}
		File file = new File(filePath);
		if(!file.exists()||null==file)
		{
			boolean b=file.mkdirs();
			Log.e("file", "文件不存在  创建文件    "+b);
		}else{
			Log.e("file", "文件目录存在");
		}
		return filePath;
	}
	public static int isExists(String name,int fileSize){
		int b = 1;
		if(checkSDCard()){
			File file = new File(name);
			if(!file.exists())
			{
				b = 1;
				Log.e("file", "判断文件不存在   ");
			}else if(file.exists()){
				if (file.length()==fileSize) {
					b = 2;
					Log.e("file", "文件存在大小相等"+file.length()+"网络上获取大小"+fileSize);
				}else {
					b = 3;//暂时设置为true
					Log.e("file", "文件存在大小相等"+file.length()+"网络上获取大小"+fileSize);
					Log.e("file", "文件存在,大小不一致");
				}
				
			}
		}
		return b;
		
	}
	public static boolean isExist(String name){
		boolean b = false;
		if(checkSDCard()){
			File file = new File(name);
			if(!file.exists())
			{
				b = false;
				Log.e("file", "判断文件不存在   ");
			}else if(file.exists()){
				b=true;
				Log.e("file", "判断文件存在   ");
			}
		}
		return b;
		
	}
	public static boolean isCompFile(String name){
		boolean b = false;
		if(checkSDCard()){
			String filePath=Environment.getExternalStorageDirectory()+File.separator+"CTBRIVideo"+File.separator;
			File file = new File(filePath+name);
			if(!file.exists())
			{
				b = false;
				Log.e("file", "判断文件不存在   ");
			}else{
				b = true;
				Log.e("file", "文件存在。。。");
			}
		}
		return b;
		
	}
	public static boolean deleFile(String fileName){
		File file =new File(fileName);
		if (file.exists()&&file.isFile()) {
			if (file.delete()) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	// 删除文件夹
		// param folderPath 文件夹完整绝对路径

		public static void delFolder(String folderPath) {
			try {
				delAllFile(folderPath); // 删除完里面所有内容
				String filePath = folderPath;
				filePath = filePath.toString();
				File myFilePath = new File(filePath);
				myFilePath.delete(); // 删除空文件夹
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				//删除文件夹之前 先重命名在删除
				final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
				temp.renameTo(to);
				temp.delete();
				flag = true;
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		// 最后删除空文件
		file.delete();
		return flag;
	}
	//清理缓存文件
	public static boolean clearCache(){
		String FilePath=Environment.getExternalStorageDirectory()+File.separator+"CTBRIVideo";
		boolean isDeled=FileUtil.delAllFile(FilePath);
		return isDeled;
		
	}
	
	 
	   /**  
	     *  新建目录  
	     *  @param  folderPath  String  如  c:/fqf  
	     *  @return  boolean  
	     */  
	   public  void  newFolder(String  folderPath)  {  
	       try  {  
	           String  filePath  =  folderPath;  
	           filePath  =  filePath.toString();  
	           File  myFilePath  =  new  File(filePath);
	           if  (!myFilePath.exists())  {  
	               myFilePath.mkdir();  
	           }  
	       }  
	       catch  (Exception  e)  {  
	           System.out.println("新建目录操作出错");  
	           e.printStackTrace();  
	       }  
	   }  
	 
	   /**  
	     *  新建文件  
	     *  @param  filePathAndName  String  文件路径及名称  如c:/fqf.txt  
	     *  @param  fileContent  String  文件内容  
	     *  @return  boolean  
	     */  
	   public  void  newFile(String  filePathAndName,  String  fileContent)  {  
	 
	       try  {  
	           String  filePath  =  filePathAndName;  
	           filePath  =  filePath.toString();  //取的路径及文件名
	           File  myFilePath  =  new  File(filePath);  
	           /**如果文件不存在就建一个新文件*/
	           if  (!myFilePath.exists())  {  
	               myFilePath.createNewFile();  
	           }  
	           FileWriter  resultFile  =  new  FileWriter(myFilePath);  //用来写入字符文件的便捷类, 在给出 File 对象的情况下构造一个 FileWriter 对象
	           PrintWriter  myFile  =  new  PrintWriter(resultFile);  //向文本输出流打印对象的格式化表示形式,使用指定文件创建不具有自动行刷新的新 PrintWriter。
	           String  strContent  =  fileContent;  
	           myFile.println(strContent);  
	           resultFile.close();  
	 
	       }  
	       catch  (Exception  e)  {  
	           System.out.println("新建文件操作出错");  
	           e.printStackTrace();  
	 
	       }  
	 
	   }  
	 
	   /**  
	     *  删除文件  
	     *  @param  filePathAndName  String  文件路径及名称  如c:/fqf.txt  
	     *  @param  fileContent  String  
	     *  @return  boolean  
	     */  
	   public static void  delFile(String  filePathAndName)  {  
	       try  {  
	           String  filePath  =  filePathAndName;  
	           filePath  =  filePath.toString();  
	           File  myDelFile  =  new  File(filePath);
	           myDelFile.delete();  
	 
	       }  
	       catch  (Exception  e)  {  
	           System.out.println("删除文件操作出错");  
	           e.printStackTrace();  
	 
	       }  
	 
	   }  
	 
	  
	 
	   /**  
	     *  复制单个文件  
	     *  @param  oldPath  String  原文件路径  如：c:/fqf.txt  
	     *  @param  newPath  String  复制后路径  如：f:/fqf.txt  
	     *  @return  boolean  
	     */  
	   public static void  copyFileReName(String  oldPath,  String  newPath)  {  
	       try  {  
//	           int  bytesum  =  0;  
	           int  byteread  =  0;  
	           File  oldfile  =  new  File(oldPath); 
	           String path=oldPath.substring(oldPath.indexOf("CTBRI"),oldPath.lastIndexOf("."));
	           File toFile = new File(newPath+path);
	           if (!toFile.exists()) {
	        	   toFile.createNewFile();
				}
	           newPath=newPath+path;
	           
	           if  (oldfile.exists())  {  //文件存在时  
	               InputStream  inStream  =  new  FileInputStream(oldPath);  //读入原文件 
	               FileOutputStream  fs  =  new  FileOutputStream(newPath);  
	               byte[]  buffer  =  new  byte[1444];  
//	               int  length;  
	               while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {  
//	                   bytesum  +=  byteread;  //字节数  文件大小  
//	                   System.out.println(bytesum);  
	                   fs.write(buffer,  0,  byteread);  
	               }  
	               inStream.close();  
	           }  
	       }  
	       catch  (Exception  e)  {  
	           System.out.println("复制单个文件操作出错");  
	           e.printStackTrace();  
	 
	       }  
	 
	   }  
	 
	   /**  
	     *  复制整个文件夹内容  
	     *  @param  oldPath  String  原文件路径  如：c:/fqf  
	     *  @param  newPath  String  复制后路径  如：f:/fqf/ff  
	     *  @return  boolean  
	     */  
	   public  void  copyFolder(String  oldPath,  String  newPath)  {  
	 
	       try  {  
	           (new  File(newPath)).mkdirs();  //如果文件夹不存在  则建立新文件夹  
	           File  a=new  File(oldPath);  
	           String[]  file=a.list();  
	           File  temp=null;  
	           for  (int  i  =  0;  i  <  file.length;  i++)  {  
	               if(oldPath.endsWith(File.separator)){  
	                   temp=new  File(oldPath+file[i]);  
	               }  
	               else{  
	                   temp=new  File(oldPath+File.separator+file[i]);  
	               }  
	 
	               if(temp.isFile()){  
	                   FileInputStream  input  =  new  FileInputStream(temp);  
	                   FileOutputStream  output  =  new  FileOutputStream(newPath  +  "/"  + 
	                           (temp.getName()).toString());  
	                   byte[]  b  =  new  byte[1024  *  5];  
	                   int  len;  
	                   while  (  (len  =  input.read(b))  !=  -1)  {  
	                       output.write(b,  0,  len);  
	                   }  
	                   output.flush();  
	                   output.close();  
	                   input.close();  
	               }  
	               if(temp.isDirectory()){//如果是子文件夹  
	                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);  
	               }  
	           }  
	       }  
	       catch  (Exception  e)  {  
	           System.out.println("复制整个文件夹内容操作出错");  
	           e.printStackTrace();  
	 
	       }  
	 
	   }  
	 
	   /**  
	     *  移动文件到指定目录  
	     *  @param  oldPath  String  如：c:/fqf.txt  
	     *  @param  newPath  String  如：d:/fqf.txt 
	     *   
	     */  
	   public  static void  moveFile(String  oldPath,  String  newPath)  {  
	       copyFileReName(oldPath,  newPath);  
	       delFile(oldPath);  
	 
	   }  
	 
	   /**  
	     *  移动文件到指定目录  
	     *  @param  oldPath  String  如：c:/fqf.txt  
	     *  @param  newPath  String  如：d:/fqf.txt  
	     */  
	   public  void  moveFolder(String  oldPath,  String  newPath)  {  
	       copyFolder(oldPath,  newPath);  
	       delFolder(oldPath);  
	 
	   }  
	// 拷贝文件
	   private void copyFile2(String source, String dest) {
	   try {
	   File in = new File(source);
	   File out = new File(dest);
	   FileInputStream inFile = new FileInputStream(in);
	   FileOutputStream outFile = new FileOutputStream(out);
	   byte[] buffer = new byte[10240];
	   int i = 0;
	   while ((i = inFile.read(buffer)) != -1) {
	   outFile.write(buffer, 0, i);
	   }//end while
	   inFile.close();
	   outFile.close();
	   }//end try
	   catch (Exception e) {

	   }//end catch
	   }//end copyFile

	//给android有R文件的项目打包 用到的R转换工具类
	public static int getLayoutResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "layout",
				context.getPackageName());
	}

	public static int getIdResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "id",
				context.getPackageName());
	}

	public static int getStringResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "string",
				context.getPackageName());
	}

	public static int getStyleResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "style",
				context.getPackageName());
	}
	public static int getStyleableResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "styleable",
				context.getPackageName());
	}

	public static int getDrawableResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}
	public static String subString(String response) {
		if (response != null && !response.equals("")) {
			return response.substring(response.indexOf("=") + 1,
					response.lastIndexOf(";"));
		}
		return null;
	}

}
