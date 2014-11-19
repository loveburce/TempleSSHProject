package com.elight.teaching.activity.multiphotopicker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.elight.teaching.R;
import com.elight.teaching.activity.BaseActivity;
import com.elight.teaching.adapter.multiphotopicker.ImageBucketAdapter;
import com.elight.teaching.adapter.multiphotopicker.ImageGridAdapter;
import com.elight.teaching.entity.multiphotopicker.ImageItem;
import com.elight.teaching.fragment.BaseFragment;
import com.elight.teaching.utils.multiphotopicker.CustomConstants;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dawn on 2014/10/30.
 */
public class ImageChooseActivity extends BaseActivity{

    private List<ImageItem> dataList = new ArrayList<ImageItem>();
    private String bucketName;
    private int availableSize;
    private GridView gridView;
    private ImageGridAdapter imageGridAdapter;
    private TextView bucketNameTv;
    private TextView cancelTv;
    private Button finishBtn;
    private HashMap<String, ImageItem> selectedImages = new HashMap<String, ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiphotopicker_image_choose_activity);
        dataList = (List<ImageItem>) getIntent().getSerializableExtra(CustomConstants.EXTRA_IMAGE_LIST);
        if(dataList == null){
            dataList = new ArrayList<ImageItem>();
        }
        bucketName = getIntent().getStringExtra(CustomConstants.EXTRA_BUCKET_NAME);
        if(TextUtils.isEmpty(bucketName)){
            bucketName = "请选择";
        }
        availableSize = getIntent().getIntExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,CustomConstants.MAX_IMAGE_SIZE);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        bucketNameTv = (TextView) findViewById(R.id.multiphotopicker_header_title);
        bucketNameTv.setText(bucketName);

        gridView = (GridView) findViewById(R.id.multiphotopicker_image_choose_grid_view);

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        imageGridAdapter = new ImageGridAdapter(getApplicationContext(),dataList);
        gridView.setAdapter(imageGridAdapter);
        finishBtn = (Button) findViewById(R.id.multiphotopicker_image_choose_finish_btn);
        cancelTv = (TextView) findViewById(R.id.multiphotopicker_header_action);

        finishBtn.setText("完成"+"("+selectedImages.size()+"/"+availableSize+")");
        imageGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ImageChooseActivity.this, Pub);
                //携带数据回到主界面
                BaseFragment.dataList.addAll(dataList);
                ImageChooseActivity.this.finish();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = dataList.get(position);
                if(item.isSelected){
                    item.isSelected = false;
                    selectedImages.remove(item.imageId);
                } else {
                    if(selectedImages.size() >= availableSize){
                        Toast.makeText(ImageChooseActivity.this,
                                "最多选择"+availableSize+"张图片",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    item.isSelected = true;
                    selectedImages.put(item.imageId, item);
                }

                finishBtn.setText("完成"+"("+selectedImages.size()+"/"+availableSize+")");
                imageGridAdapter.notifyDataSetChanged();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ImageChooseActivity.this,)
            }
        });

    }

}
