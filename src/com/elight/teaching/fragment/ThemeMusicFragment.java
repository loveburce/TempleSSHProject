package com.elight.teaching.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.elight.teaching.R;
import com.elight.teaching.activity.SWFPlayerActivity;
import com.elight.teaching.activity.ThemeMusicActivity;
import com.elight.teaching.adapter.ThemeMusicAdapter;
import com.elight.teaching.config.Constants;
import com.elight.teaching.entity.LessonInfo;
import com.elight.teaching.entity.TeachClassInfo;
import com.elight.teaching.parse.ParseTeachInfo;
import com.elight.teaching.utils.asyncTask.LoadJsonAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/9.
 */
public class ThemeMusicFragment extends Fragment{
    public final static int SET_NEWS_LIST = 0;
    List<TeachClassInfo> teachClassInfoList = new ArrayList<TeachClassInfo>();
    ThemeMusicAdapter themeMusicAdapter;
    Activity activity;
    GridView gridView;
    View view;
    String lessonNumber;
    String bookNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        lessonNumber = args != null ? args.getString("lessonNumber") : "";
        bookNumber = args != null ? args.getString("bookNumber") : "";
        themeMusicAdapter = new ThemeMusicAdapter(getActivity());
        //数据初始化

        Log.d("wangshengyanopopopop","opopopop :1 "+lessonNumber);
        fetchData();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){


            if(teachClassInfoList != null && teachClassInfoList.size() != 0){
                handler.obtainMessage(SET_NEWS_LIST).sendToTarget();
            }else{
                //从网络获取数据, 获取后发送handler
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        try {
//                            Thread.sleep(2);
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        handler.obtainMessage(SET_NEWS_LIST).sendToTarget();
//                    }
//                }).start();


            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme_music,null);
        gridView = (GridView) view.findViewById(R.id.fragment_theme_music_grid_view);

        gridView.setAdapter(themeMusicAdapter);
        //gridview的单个点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  Intent intent = new Intent(getActivity(), SWFPlayerActivity.class);
                  intent.putExtra("swfUrl",teachClassInfoList.get(position).getSwfUrl());
                  getActivity().startActivity(intent);
              }
        });

        return view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SET_NEWS_LIST:

                    themeMusicAdapter.addItems(teachClassInfoList);
                    themeMusicAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private void fetchData(){
        LoadJsonAsyncTask loadJsonAsyncTask = new LoadJsonAsyncTask(new LoadJsonAsyncTask.LoadJsonAsyncTaskCallBack() {
            @Override
            public void beforeJsonLoad() {

            }

            @Override
            public void onJsonLoaded(String str) {
                teachClassInfoList = ParseTeachInfo.parseTeachClassList(str);
//                if(null != teachClassInfoList){
//                for(int i=0; i<teachClassInfoList.size(); i++) {
//                    Log.d("wangshengyanioioioioio", "referInfoList : " + teachClassInfoList.get(i).toString());
//                }
                    handler.obtainMessage(SET_NEWS_LIST).sendToTarget();
//                }


            }
        });
        //public static final String pathEducationReference="http://www.guoxue365.cn/teacher/plus/readreference.php?mid=50&cid=1&tid=1";

        String urlPath = Constants.teachClassInfoPath+"&cid="+((ThemeMusicActivity)getActivity()).bookNumber+"&tid="+lessonNumber;
        loadJsonAsyncTask.execute(urlPath);
    }
}
