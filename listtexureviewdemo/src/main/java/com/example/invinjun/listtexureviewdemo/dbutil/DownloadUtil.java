package com.example.invinjun.listtexureviewdemo.dbutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DownloadUtil {
	   /**  
     *  移动文件到指定目录  
     *  @param  oldPath  String  如：c:/fqf.txt  
     *  @param  newPath  String  如：d:/fqf.txt 
     *   
     */  
   public  static void  moveFile(String  oldPath,  String  newPath)  {  
       copyFileReName(oldPath,  newPath);  
       delFile(oldPath);  
 
   }  	   /**  
    *  复制单个文件  
    *  @param  oldPath  String  原文件路径  如：c:/fqf.txt  
    *  @param  newPath  String  复制后路径  如：f:/fqf.txt  
    *  @return  boolean  
    */  
  public static void  copyFileReName(String  oldPath,  String  newPath)  {  
      try  {  
//          int  bytesum  =  0;  
          int  byteread  =  0;  
          File  oldfile  =  new  File(oldPath); 
          String path=oldPath.substring(oldPath.lastIndexOf("/")+1,oldPath.lastIndexOf("."));
          File toFile = new File(newPath);
          if (!toFile.exists()) {
       	   toFile.createNewFile();
			}
          
          if  (oldfile.exists())  {  //文件存在时  
              InputStream  inStream  =  new  FileInputStream(oldPath);  //读入原文件 
              FileOutputStream  fs  =  new  FileOutputStream(newPath);  
              byte[]  buffer  =  new  byte[1444];  
//              int  length;  
              while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {  
//                  bytesum  +=  byteread;  //字节数  文件大小  
//                  System.out.println(bytesum);  
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
}
