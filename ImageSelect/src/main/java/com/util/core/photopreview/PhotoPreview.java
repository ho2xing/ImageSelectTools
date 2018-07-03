package com.util.core.photopreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.util.core.imageselect.R;

public class PhotoPreview extends LinearLayout implements OnClickListener {

	private GestureImageView ivContent;
	private OnClickListener l;
	private Context context;

	public PhotoPreview(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.image_select_image_view_photopreview,
				this, true);

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

	public void loadImage(PhotoModel photoModel) {
		loadImage(photoModel.getOriginalPath());
	}

	public class CropSquareTransformation implements Transformation {
		@Override
		public Bitmap transform(Bitmap source) {
			int size = Math.min(source.getWidth(), source.getHeight());
			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;
			Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
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

	private void loadImage(String path) {
		Picasso.with(context).load(path).noFade().config(Bitmap.Config.RGB_565)

		.into(ivContent);
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
