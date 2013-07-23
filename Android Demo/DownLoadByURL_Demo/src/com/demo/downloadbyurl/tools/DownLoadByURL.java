/**
 * @Copyright 版权所有(C) 2013，熊文财
 * @Filename DownLoadByURL.java
 * @Description 
 * @Auther XiongWenCai
 * @Email xwc_010@163.com
 * @Date 2013-7-18 下午04:03:31
 */

package com.demo.downloadbyurl.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.utils.URLEncodedUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

/**
 * @Description  通过URL地址下载文件
 * @Author XiongWenCai
 * @Date 2013-7-18下午04:03:31
 */

public class DownLoadByURL 
{

	/** 
	* @Description:   通过URL地址获取网络图片
	* @param url 网络图片的url地址
	* @return Bitmap   为空时，表示下载图片失败
	*/
	public static Bitmap getBitmapByURL(URL url)
	{
		Bitmap bit = null;
		InputStream input = null;
		URLConnection conn = null;
		
		// 判断String是否为NetworkUrl
//		URLUtil.isNetworkUrl(string);
		
		try 
		{
			conn = url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			
			// 获取输入流
			input = conn.getInputStream();
			
			// 通过Bitmap工厂生成BitMap
			bit = BitmapFactory.decodeStream(input);
			Log.i("下载线程", "--------------11");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			
			return null;
		}
		finally
		{
			// 关闭输入流
			if(input != null)
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		return bit;
	}
	
	
	
	/** 
	* @Description:  通过URL地址下载文件
	* @param url 网络文件url地址
	* @param savePath 文件保存路径
	* @return String  文件保存的路径，null表示文件下载或保存失败
	*/
	
	public static String saveFileByURL(URL url, String savePath)
	{
		InputStream  input = null;
		OutputStream output = null;
		URLConnection conn = null;
		
		// 验证sdcard是否存在
		if(!Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
		{
			return null;
		}
		
		File file = new File(savePath);
		
		// 判断文件的父目录是否存在
		if(!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs(); // 不存在就创建
		}
		
		try 
		{
			conn = url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			
			// 获取输入流
			input = conn.getInputStream();
			
			// 构造输出流, 可追加的模式
			output = new FileOutputStream(file, true); 
			
			byte[] b = new byte[100];
 			
			// 边读边写
 			do 
 			{    
                 int numread = input.read(b);
                 
                 if (numread <= 0) 
                 {    
                     break;    
                 } 
                 Log.i("下载线程", "--------------22");
                 output.write(b, 0, numread); 
                 
             } while (true);
			
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			
			return null;
		}
		finally
		{
			// 关闭输入流
			if(input != null)
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
			if(output != null)
			{
				try 
				{
					output.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		return savePath;
	}
}
