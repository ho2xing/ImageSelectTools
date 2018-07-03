package com.util.core.photopreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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


	private void loadImage(String path) {

		Glide.with(context)
				.load(path)
				.apply(new RequestOptions().placeholder(R.drawable.image_select_default_error)
						.error(R.drawable.image_select_default_error))
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
