package com.elight.teaching.activity.multiphotopicker;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.elight.teaching.R;
import com.elight.teaching.activity.BaseActivity;
import com.elight.teaching.entity.multiphotopicker.ImageItem;
import com.elight.teaching.utils.multiphotopicker.CustomConstants;
import com.elight.teaching.utils.multiphotopicker.ImageDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/11/2.
 */
public class ImageZoomActivity extends BaseActivity{

    private ViewPager viewPager;
    private MyPageAdapter  myPagerAdapter;
    private int currentPosition;
    private List<ImageItem> imageList = new ArrayList<ImageItem>();
    private RelativeLayout photoRelativeLayout;
    private Button photoExit;
    private Button photoDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiphotopicker_image_zoom_activity);
        findViewById();
        initView();
        initData();
    }

    @Override
    protected void findViewById() {
        photoRelativeLayout = (RelativeLayout) findViewById(R.id.multiphotopicker_image_zoom_relative_layout);
//        photoRelativeLayout.setBackgroundColor(0x70000000);
        photoExit = (Button) findViewById(R.id.multiphotopicker_image_zoom_photo_exit);
        photoDelete = (Button) findViewById(R.id.multiphotopicker_image_zoom_photo_del);
        viewPager = (ViewPager) findViewById(R.id.multiphotopicker_image_zoom_view_pager);
    }

    @Override
    protected void initView() {
        photoExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageZoomActivity.this.finish();
            }
        });

        photoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageList.size() == 1){
                    removeImages();
                    finish();
                }else{
                    removeImages();
                    viewPager.removeAllViews();
                    myPagerAdapter.removeView(currentPosition);
                    myPagerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    protected void initData(){
        //获取数据源 和 数据的标签
        currentPosition = getIntent().getIntExtra(CustomConstants.EXTRA_CURRENT_IMG_POSITION,0);
        imageList = (List<ImageItem>) getIntent().getSerializableExtra(CustomConstants.EXTRA_IMAGE_LIST);

        viewPager.setOnPageChangeListener(pageChangeListener);
        myPagerAdapter = new MyPageAdapter(imageList);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(currentPosition);

    }

    private void removeImages(){
        imageList.clear();
    }

    private void removeImageItem(int location){
        if(location+1 <= imageList.size()){
            imageList.remove(location);
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            currentPosition = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private class MyPageAdapter extends PagerAdapter{

        private List<ImageItem> imageItemList = new ArrayList<ImageItem>();
        private ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>();

        private MyPageAdapter(List<ImageItem> imageItemList) {
            this.imageItemList = imageItemList;
            int size = imageItemList.size();
            for(int i=0; i!= size; i++){
                ImageView imageView = new ImageView(ImageZoomActivity.this);
                ImageDisplayer.getInstance(ImageZoomActivity.this).displayBmp(
                        imageView, null,imageItemList.get(i).sourcePath,false);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageViewArrayList.add(imageView);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewArrayList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(imageViewArrayList.size() >= position+1){
                container.removeView(imageViewArrayList.get(position));
            }
        }

        @Override
        public int getCount() {
            return imageItemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        public void removeView(int position){
            if(position +1 <= imageViewArrayList.size()){
                imageViewArrayList.remove(position);
            }
        }
    }


}
