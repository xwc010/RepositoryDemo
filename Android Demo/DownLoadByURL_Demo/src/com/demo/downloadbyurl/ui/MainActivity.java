package com.demo.downloadbyurl.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.demo.downloadbyurl.R;
import com.demo.downloadbyurl.tools.DownLoadByURL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    private Button btn_download;
    private Button btn_save;
    private Button btn_read;
    private ImageView imgv_show;
    
    private String strUrl = "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1307/11/c0/23208204_1373527878254_800x600.jpg";
    private static String filePath = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
        
        setListener();
    }
    
    /** 
    * @Description: 初始化界面控件     
    * @return void
    */
    private void init(){
    	
    	btn_download = (Button)this.findViewById(R.id.btn_download);
    	btn_save     = (Button)this.findViewById(R.id.btn_save);
    	btn_read     = (Button)this.findViewById(R.id.btn_read);
    	imgv_show    = (ImageView)this.findViewById(R.id.imgv_show);
    }
    
    /** 
    * @Description:  为按钮设置监听事件    
    * @return void    
    */
    private void setListener(){
    	
    	btn_download.setOnClickListener(listener);
    	btn_read.setOnClickListener(listener);
    	btn_save.setOnClickListener(listener);
    }
    
    /**
     *  单击事件监听实现
     */
    OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btn_download:
				Toast.makeText(MainActivity.this, "开始下载！", Toast.LENGTH_SHORT).show();
				new Thread(new MyRunnable(1)).start();
				break;
				
			case R.id.btn_save:
				
				Toast.makeText(MainActivity.this, "开始下载！", Toast.LENGTH_SHORT).show();
				new Thread(new MyRunnable(2)).start();
				break;
				
			case R.id.btn_read:
	
				Toast.makeText(MainActivity.this, "开始读取！", Toast.LENGTH_SHORT).show();
				InputStream in = null;
				try {
					
					in = new FileInputStream(filePath);
					imgv_show.setImageBitmap(BitmapFactory.decodeStream(in));
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;

			default:
				break;
			}
			
		}
	};
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what) {
			
			case 0:
				Toast.makeText(MainActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
				break;
				
			case 1:
				imgv_show.setImageBitmap((Bitmap)msg.obj);
				break;
				
			case 2:
				
				filePath = msg.obj.toString();
				Toast.makeText(MainActivity.this, filePath, Toast.LENGTH_SHORT).show();
				Log.i("下载路径", filePath);
				break;

			default:
				break;
			}
		}
		
	};
	
	private class MyRunnable implements Runnable{
		
		private int tag; // 下载类型标识
		
		public MyRunnable(int tag) {
			super();
			this.tag = tag;
		}

		@Override
		public void run() {
			
			Message msg = new Message();
			URL url = null;
			try {
				url = new URL(strUrl);
				
				if(tag == 1){
					
					Bitmap bit = null;
					bit = DownLoadByURL.getBitmapByURL(url);
					msg.what = 1;
					msg.obj = bit;
					
				}else if(tag == 2){
					
					String savePath = Environment.getExternalStorageDirectory().toString()
											+ File.separator
											+ "apkdownload" + File.separator
											+ strUrl.substring(strUrl.lastIndexOf("/")+1, strUrl.length());
					
					String path = DownLoadByURL.saveFileByURL(url, savePath);
					
					msg.what = 2;
					msg.obj = path;
					
					Log.i("保存路径savePath", savePath);
				}
				
			} catch (MalformedURLException e) {
				
				msg.what = 0;
				e.printStackTrace();
			}
			
			mHandler.sendMessage(msg);
		}
	};
}