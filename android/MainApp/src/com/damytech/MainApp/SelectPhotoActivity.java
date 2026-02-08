package com.damytech.MainApp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: KimHakMin
 * Date: 13-11-29
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
public class SelectPhotoActivity extends SuperActivity
{
	Button btn_takephoto = null, btn_selimage = null, btn_cancel = null;

	static Uri fileUri = null;

	public static int IMAGE_WIDTH = 500;
	public static int IMAGE_HEIGHT = 500;

	public static int REQCODE_TAKE_PHOTO = 0;
	public static int REQCODE_SELECT_GALLERY = 1;

	public static String szRetCode = "RET";
	public static String szRetPath = "PATH";
	public static int nRetSuccess = 1;
	public static int nRetCancelled = 0;
	public static int nRetFail = -1;

	private String photo_path = "";
	private Uri photo_uri = null;

	private boolean isFromCamera = true;
	private String resPath = "";

    private String photoType = "userPhoto";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.act_selphoto);

		initVariables();
		initControls();
		initResolution();

		initHandlers();
	}

	private void initResolution()
	{
		RelativeLayout parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);
		parent_layout.getViewTreeObserver().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Point ptTemp = getScreenSize();
				boolean bNeedUpdate = false;
				if (mScrSize.x == 0 && mScrSize.y == 0)
				{
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}
				else if (mScrSize.x != ptTemp.x || mScrSize.y != ptTemp.y)
				{
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}

				if (bNeedUpdate)
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ResolutionSet.instance.iterateChild(findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);
						}
					});
				}
			}
		});	}

	private void initVariables()
	{
		btn_takephoto = null;
		btn_selimage = null;
		btn_cancel = null;

		REQCODE_TAKE_PHOTO = 0;
		REQCODE_SELECT_GALLERY = 1;

		szRetCode = "RET";
		szRetPath = "PATH";

		nRetSuccess = 1;
		nRetCancelled = 0;
		nRetFail = -1;

		photo_path = "";
		photo_uri = null;
	}

	public void initControls()
	{
		btn_takephoto = (Button)findViewById(R.id.btn_take_photo);
		btn_selimage = (Button)findViewById(R.id.btn_sel_image);
		btn_cancel = (Button)findViewById(R.id.btn_cancel);

        photoType = getIntent().getStringExtra(Global.PHOTO_TYPE());
	}

	public void initHandlers()
	{
		btn_takephoto.setOnClickListener(onClickTakePhoto);
		btn_selimage.setOnClickListener(onClickSelImage);
		btn_cancel.setOnClickListener(onClickCancel);
	}

	@Override
	protected void onResume()
	{
		super.onResume();	//To change body of overridden methods use File | Settings | File Templates.

		if (resPath.equals("") || resPath == null)
			return;

		if (isFromCamera)
			correctBitmap(resPath);

		Intent retIntent = new Intent();
		retIntent.putExtra(szRetCode, nRetSuccess);
		retIntent.putExtra(szRetPath, resPath);
		setResult(RESULT_OK, retIntent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_TAKE_PHOTO)
		{
			Uri photoUri = null;

			if (data == null)
				photoUri = fileUri;
			else
				photoUri = data.getData();

			try
			{
				if (photoUri != null)
				{
					String szPath = photoUri.getPath();
					if (szPath == null || szPath.equals(""))
					{
						Global.showAdvancedToast(SelectPhotoActivity.this, getResources().getString(R.string.STR_LOADING_IMAGE_FAILED), Gravity.CENTER);
					}
					else
					{
						photo_path = szPath;
						photo_uri = null;
					}
				}
				else
				{
					photo_path = fileUri.getPath();
					photo_uri = null;
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				Global.showAdvancedToast(SelectPhotoActivity.this, getResources().getString(R.string.STR_TAKING_PHOTO_FAILED), Gravity.CENTER);
			}
		}
		else if (requestCode == REQCODE_SELECT_GALLERY)
		{
			if (resultCode == RESULT_OK && data != null)
			{
				Uri selImage = data.getData();
				if (selImage != null)
				{
					photo_path = "";
					photo_uri = selImage;
				}
				else
				{
				}
			}
			else
			{
			}
		}


		if (photo_path != null && !photo_path.equals(""))
		{
			resPath = photo_path;
			isFromCamera = true;
		}
		else if (photo_uri != null)
		{
			resPath = getRealPathFromURI(photo_uri);
			isFromCamera = false;
		}
	}


	public View.OnClickListener onClickTakePhoto = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = getOutputPhotoFile();
			if (file == null) {
				Global.showAdvancedToast(SelectPhotoActivity.this, getResources().getString(R.string.STR_CANNOT_TAKEPHOTO), Gravity.CENTER);
			} else {
				fileUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, REQCODE_TAKE_PHOTO);
			}
		}
	};


	public View.OnClickListener onClickSelImage = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQCODE_SELECT_GALLERY);
		}
	};


	public View.OnClickListener onClickCancel = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			cancelWithData();
		}
	};


	private File getOutputPhotoFile()
	{
		File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getPackageName());
		if (!directory.exists())
		{
			if (!directory.mkdirs()) {          // No sd card.
				directory = null;
			}
		}

		if (directory == null)
		{
			directory = getDir(getPackageName(), Context.MODE_PRIVATE);
			if (!directory.exists())
			{
				if (!directory.mkdir())
					directory = null;
			}
		}

		if (directory == null)
			return null;

		return new File(directory.getPath() + File.separator + "IMG_" + photoType + ".jpg");
	}


	private void cancelWithData()
	{
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		SelectPhotoActivity.this.finish();
	}


	private void correctBitmap(String szPath)
	{
		int nAngle = Global.getImageOrientation(szPath);
		if (nAngle == 0)                // Image is correct. No need to rotate
			return;

		Bitmap bmpRot = Global.rotateImage(szPath, nAngle);
		FileOutputStream ostream = null;

		try {
			File file = new File(szPath);
			file.deleteOnExit();

			ostream = new FileOutputStream(file);
			bmpRot.compress(Bitmap.CompressFormat.JPEG, 50, ostream);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (ostream != null) {
				try { ostream.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}
	}

	private String getRealPathFromURI(Uri contentUri)
	{
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = SelectPhotoActivity.this.getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}


}
