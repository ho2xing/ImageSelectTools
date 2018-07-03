package com.util.core.activty;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.util.core.imageselect.R;

public class ImageOnlyDisplayActivity extends Activity implements OnPageChangeListener {

	/** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
	public static final String EXTRA_RESULT = "display_result";

	/** 默认选择集 */
	public static final String EXTRA_DEFAULT_DIAPLAY_LIST = "default_display_list";
	/**
	 * 默认从0开始
	 */
	public static final String  EXTRA_DEFAULT_DISPLAY_ID = "EXTRA_DEFAULT_DISPLAY_ID";

	private ViewPager mViewPager;

	private List<String> resultList = new ArrayList<>();

	private ImageView back_btn = null;



	private TextView count_text = null;

	private int currentDispalyImageId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.image_select_activity_only_display);

		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_DEFAULT_DIAPLAY_LIST)) {
			resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_DIAPLAY_LIST);
		} else {
			Log.w("ImageDisplay Error", "传入的默认展示图片为空");
		}
		if(intent.hasExtra(EXTRA_DEFAULT_DISPLAY_ID)){
			currentDispalyImageId = intent.getIntExtra(EXTRA_DEFAULT_DISPLAY_ID , 0);
		}

		bindView();
	}

	private void toResult() {
		if (resultList != null) {
			// 返回已选择的图片数据
			Intent data = new Intent();
			data.putStringArrayListExtra(EXTRA_RESULT, (ArrayList<String>) resultList);
			setResult(RESULT_OK, data);
			finish();
		}
	}

	private void bindView() {
		back_btn = (ImageView) findViewById(R.id.btn_back);
		count_text = (TextView) findViewById(R.id.count_text);
		
		mViewPager = (ViewPager) findViewById(R.id.vp_base_app);
		mViewPager.setOnPageChangeListener(this);
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toResult();
			}
		});

	
		mViewPager.setAdapter(mPagerAdapter);

		String text = String.format(getString(R.string.imageselect_count_text), currentDispalyImageId + 1, resultList.size());
		count_text.setText(text);
		mViewPager.setCurrentItem(currentDispalyImageId , false);
	}

	@Override
	public void onPageScrollStateChanged(int newState) {
		// if (newState == ViewPager.SCROLL_STATE_IDLE) {
		// Picasso.with(this).resumeTag(currentDisplayImagePath);
		// } else {
		// Picasso.with(this).pauseTag(currentDisplayImagePath);
		// }
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	protected void updatePercent() {
		String text = String.format(getString(R.string.imageselect_count_text), currentDispalyImageId + 1, resultList.size());
		count_text.setText(text);
	}

	@Override
	public void onPageSelected(int postion) {
		currentDispalyImageId = postion;
		updatePercent();
	}


	
	private PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public int getCount() {
			if (resultList == null) {
				return 0;
			} else {
				return resultList.size();
			}
		}

		@Override
		public View instantiateItem(final ViewGroup container, final int position) {
			PhotoPreview photoPreview = new PhotoPreview(getApplicationContext());
			((ViewPager) container).addView(photoPreview);
		
			photoPreview.loadImage(resultList.get(position));
			return photoPreview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		Picasso.with(this).cancelTag(this);
		if (alertDialog != null) {
			alertDialog.dismiss();
		}
	}

	private AlertDialog alertDialog = null;

	private Button submitButton;

	private Button cancelButton;

	private void showDialog() {
		if (alertDialog == null) {
			LayoutInflater factory = LayoutInflater.from(this);
			View view = factory.inflate(R.layout.image_select_alert_dialog, null);
			submitButton = (Button) view.findViewById(R.id.submit);
			cancelButton = (Button) view.findViewById(R.id.cancel);
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setView(view);
		}

		if (submitButton != null) {
			submitButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
				
					if (resultList.size() > 0) {
						resultList.remove(currentDispalyImageId);
						if (currentDispalyImageId + 1 == resultList.size()) {
							currentDispalyImageId = resultList.size() - 1;
						}
						if (currentDispalyImageId < 0) {
							currentDispalyImageId = 0;
						}
						mPagerAdapter.notifyDataSetChanged();
						
					}
					if (alertDialog != null) {
						alertDialog.dismiss();
						alertDialog = null;
					}
					
					if(resultList.size() <= 0){
						toResult();
					}
					if(resultList.size()  > 0){
						updatePercent();
					}
					
				}
			});
		}

		if (cancelButton != null) {
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.dismiss();
					alertDialog = null;
				}
			});
		}

		if (alertDialog != null) {
			alertDialog.show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			toResult();
		}
		return false;
	}

}
