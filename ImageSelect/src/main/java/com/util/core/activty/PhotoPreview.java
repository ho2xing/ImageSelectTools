package com.util.core.activty;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.util.core.imageselect.R;
import com.util.core.photopreview.GestureImageView;

public class PhotoPreview extends LinearLayout implements OnClickListener {

	private GestureImageView ivContent;
	private OnClickListener l;
	private Context context;

	public PhotoPreview(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.image_select_image_view_photopreview, this, true);

		ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
		ivContent.setOnClickListener(this);
		this.context = context;
	}

	public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
		this(context);
	}

	public PhotoPreview(Context context, AttributeSet attrs) {
		this(context);
	}

	/**
	 * 相片按相框的比例动态缩放
	 * 
	 * @param context
	 * @param 要缩放的图片
	 * @param width
	 *            模板宽度
	 * @param height
	 *            模板高度
	 * @return
	 */
	public static Bitmap upImageSize(Context context, Bitmap bmp, int width, int height) {
		if (bmp == null) {
			return null;
		}
		// 计算比例
		float scaleX = (float) width / bmp.getWidth();// 宽的比例
		float scaleY = (float) height / bmp.getHeight();// 高的比例
		// 新的宽高
		int newW = 0;
		int newH = 0;
		if (scaleX > scaleY) {
			newW = (int) (bmp.getWidth() * scaleX);
			newH = (int) (bmp.getHeight() * scaleX);
		} else if (scaleX <= scaleY) {
			newW = (int) (bmp.getWidth() * scaleY);
			newH = (int) (bmp.getHeight() * scaleY);
		}
		return Bitmap.createScaledBitmap(bmp, newW, newH, true);
	}

	public class CropSquareTransformation implements Transformation {
		@Override
		public Bitmap transform(Bitmap source) {
					
			Log.i("ImageDisplay ", " old image size is " + source.getHeight() + "  " + source.getWidth());
			Bitmap result = upImageSize(context, source, 480, 800);

			Log.i("ImageDisplay ", " image size is " + result.getHeight() + "  " + result.getWidth());
			if (result != source) {
				source.recycle();
			}
			return result;
		}

		@Override
		public String key() {
			return "square()";
		}
	}

    public static boolean isUrl (String pInput) {
        if(pInput == null){
            return false;
        }
        return pInput.contains("http");
    }
	

	public void loadImage(String path) {
		if(!isUrl(path)){
			path =   "file:////" + path  ;
		}
		
		
		Log.i("image loading path ", path);

		Picasso.with(context).load(path).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).noFade()
				.transform(new CropSquareTransformation()).config(Bitmap.Config.RGB_565).into(ivContent);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_content_vpp && l != null)
			l.onClick(ivContent);
	};

}
