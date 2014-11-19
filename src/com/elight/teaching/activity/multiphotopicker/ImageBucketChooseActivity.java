package com.elight.teaching.activity.multiphotopicker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.activity.BaseActivity;
import com.elight.teaching.adapter.multiphotopicker.ImageBucketAdapter;
import com.elight.teaching.entity.multiphotopicker.ImageBucket;
import com.elight.teaching.utils.multiphotopicker.CustomConstants;
import com.elight.teaching.utils.multiphotopicker.ImageFetcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/10/29.
 */
public class ImageBucketChooseActivity extends BaseActivity{

    private ImageFetcher imageFetcher;
    private List<ImageBucket> dataList = new ArrayList<ImageBucket>();
    private ListView listView;
    private ImageBucketAdapter bucketAdapter;
    private int availableSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiphotopicker_bucket_choose_activity);

        imageFetcher = ImageFetcher.getInstance(getApplicationContext());

        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        dataList = imageFetcher.getImagesBucketList(false);
        availableSize = getIntent().getIntExtra(
                CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                CustomConstants.MAX_IMAGE_SIZE);
    }

    @Override
    protected void initView() {
        listView = (ListView) findViewById(R.id.multiphotopicker_bucket_choose_lv);
        bucketAdapter = new ImageBucketAdapter(getApplicationContext(), dataList);
        listView.setAdapter(bucketAdapter);
        TextView textViewTitle = (TextView) findViewById(R.id.multiphotopicker_header_title);
        textViewTitle.setText("相册");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectOne(position);

                Intent intent = new Intent(ImageBucketChooseActivity.this, ImageChooseActivity.class);
                intent.putExtra(CustomConstants.EXTRA_IMAGE_LIST, (Serializable)dataList.get(position).imageItemList);
                intent.putExtra(CustomConstants.EXTRA_BUCKET_NAME, dataList.get(position).bucketName);
                intent.putExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE, availableSize);
                startActivity(intent);
                ImageBucketChooseActivity.this.finish();
            }
        });

        TextView cancelTV = (TextView) findViewById(R.id.multiphotopicker_header_action);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                ImageBucketChooseActivity.this.finish();
            }
        });
    }

    private void selectOne(int position){
        int size = dataList.size();
        for(int i=0; i!=size; i++){
            if(i == position){
                dataList.get(i).selected = true;
            } else {
                dataList.get(i).selected = false;
            }
        }
        bucketAdapter.notifyDataSetChanged();
    }
}
