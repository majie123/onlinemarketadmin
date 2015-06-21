package com.online.market.admin;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.online.market.admin.bean.CommodityBean;
import com.online.market.admin.util.BitmapUtil;
import com.online.market.admin.util.ProgressUtil;

public class MainActivity extends BaseActiviity {
	/**
	 * SDK初始化建议放在启动页
	 */
	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";
	public int PICK_REQUEST_CODE = 0;
	
	private EditText etName;
	private EditText etPrice;
	private ImageView ivPic;
	private Button btSubmit;
	
	private String name;
	private String price;
	
	private String picPath;
	private BmobFile bmobFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_commodity);

		initViews();
		setListeners();
		initData();
	}

	@Override
	public void initViews() {
		etName=(EditText) findViewById(R.id.et_name);
		etPrice=(EditText) findViewById(R.id.et_price);
		ivPic=(ImageView) findViewById(R.id.iv_pic);
		btSubmit=(Button) findViewById(R.id.bt_submit);

	}

	@Override
	public void initData() {
		Bmob.initialize(getApplicationContext(),APPID);
	}

	@Override
	public void setListeners() {
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				name=etName.getText().toString();
				price=etPrice.getText().toString();
				if(TextUtils.isEmpty(name)){
					toastMsg("name is null");
					return;
				}
				if(TextUtils.isEmpty(price)){
					toastMsg("price is null");
					return;
				}
				if(picPath==null){
					toastMsg("pic is null");
					return;
				}
				uploadFile();
			}
		});
		
		ivPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getFileFromSD();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);

					if (path != null) {
						saveThubPic(path);
					}
				}

			} catch (Exception e) {
			}

		}
	}
	
	private void saveThubPic(final String path){
		new Thread(){
			public void run() {
				super.run();
				Bitmap bitmap=BitmapUtil.getThumbilBitmap(path, 200);
				int wh=Math.min(bitmap.getWidth(), bitmap.getHeight());
				bitmap=BitmapUtil.getCanvasBitmap(bitmap, wh, wh);
				picPath=dir+path.substring(path.lastIndexOf("/")+1);
				BitmapUtil.saveBitmapToSdcard(bitmap, picPath);
				handler.sendEmptyMessage(0);
			};
		}.start();
		
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			Bitmap bitmap=BitmapUtil.getOriginBitmap(picPath);
			ivPic.setImageBitmap(BitmapUtil.zoom(bitmap, 150, 150));
			
		};
	};
	
	private void getFileFromSD() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_REQUEST_CODE);
	}
	
    private void uploadFile() {
    	ProgressUtil.showProgress(this, "");
    	bmobFile=new BmobFile(new File(picPath));
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				publishCommodity();
			}

			@Override
			public void onProgress(Integer arg0) {
			}

			@Override
			public void onFailure(int arg0, String msg) {
				toastMsg("失败：" + msg);
			}

		});

	}
    
    private void publishCommodity(){
    	
    	CommodityBean p=new CommodityBean();
    	p.setName(name);
    	p.setPrice(new Float(price));
    	p.setPics(bmobFile);
    	p.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toastMsg("publish成功");
				
				ProgressUtil.closeProgress();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("失败：" + msg);
				ProgressUtil.closeProgress();
			}
		});
    	
    }
	
}
