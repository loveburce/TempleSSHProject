package com.elight.teaching.fragment;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import com.elight.teaching.R;
import com.elight.teaching.adapter.EventAdapter;
import com.elight.teaching.config.Constants;
import com.elight.teaching.custom.PullToRefreshView;
import com.elight.teaching.entity.EventInfo;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.utils.ActivityUtils;
import com.elight.teaching.utils.multiphotopicker.CustomConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dawn on 2014/10/14.
 */
public class EventListFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    public static BaseFragment newInstance(int index){
        BaseFragment fragment = new EventListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        fragment.setIndex(index);
        return fragment;
    }

    private View contentView;
    private int currentIndex;
    private int pageNum;
    private String lastItemTime;    //当前列表结尾的条目的创建时间

    private ArrayList<EventInfo> eventInfoList;
    private PullToRefreshView pullToRefreshView;
    private EventAdapter eventAdapter;
    private ListView listView;

    private TextView networkTips;
    private ProgressBar progressBar;
    private boolean pullFromUser;

    public enum RefreshType{
        REFRESH,LOAD_MORE
    }

    private RefreshType refreshType = RefreshType.LOAD_MORE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventInfoList = new ArrayList<EventInfo>();
        pageNum = 0;
        lastItemTime = getCurrentTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_event_list, container, false);

        pullToRefreshView = (PullToRefreshView) contentView.findViewById(R.id.fragment_event_pull_refresh);
        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);
        pullToRefreshView.setLastUpdated(lastItemTime);

        listView = (ListView) contentView.findViewById(R.id.fragment_event_list_view);
        eventAdapter = new EventAdapter(getActivity(),eventInfoList);
        listView.setAdapter(eventAdapter);

        if(eventInfoList.size() == 0){
            fetchData();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                onListItemClick(parent, view, position, id);
            }
        });

        return contentView;
    }

    private String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = format.format(new Date(System.currentTimeMillis()));
        return times;
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshType = RefreshType.LOAD_MORE;
                fetchData();
                pullToRefreshView.onFooterRefreshComplete();
            }
        },1000);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_ABBREV_ALL);
                pullToRefreshView.onHeaderRefreshComplete("更新于:"+label);
                refreshType = RefreshType.REFRESH;
                pageNum = 0;
                lastItemTime = getCurrentTime();
                fetchData();
            }
        },1000);
    }

    public void fetchData(){
        setState(LOADING);
        BmobQuery<EventInfo> query = new BmobQuery<EventInfo>();
        query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
        query.setLimit(Constants.NUMBERS_PER_PAGE);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);

        query.setSkip(Constants.NUMBERS_PER_PAGE*(pageNum++));

        query.include("author");
        query.findObjects(getActivity(), new FindListener<EventInfo>() {
            @Override
            public void onSuccess(List<EventInfo> list) {

                if(list.size() != 0 && list.get(list.size()-1) != null){
                    if(refreshType == RefreshType.REFRESH){
                        eventInfoList.clear();
                    }
                    if(list.size() < Constants.NUMBERS_PER_PAGE){
                        ActivityUtils.showShortToast(getActivity(),"已经加载完所有数据~");
                    }
                    eventInfoList.addAll(list);
                    eventAdapter.notifyDataSetChanged();
                    setState(LOADING_COMPLETED);
                    pullToRefreshView.onHeaderRefreshComplete();
                }else{
                    ActivityUtils.showShortToast(getActivity(),"暂无更多数据~");
                    if(list.size()==0 && eventInfoList.size()==0){
                        setState(LOADING_FAILED);
                        pageNum--;
                        pullToRefreshView.onHeaderRefreshComplete();
                        return;
                    }
                    pageNum--;
                    setState(LOADING_COMPLETED);
                    pullToRefreshView.onHeaderRefreshComplete();
                }
            }

            @Override
            public void onError(int i, String s) {
                pageNum--;
                setState(LOADING_FAILED);
                pullToRefreshView.onHeaderRefreshComplete();
            }
        });


//        setState(LOADING);
//        BmobQuery<EventInfo> query = new BmobQuery<EventInfo>();
//        query.order("-createdAt");
////		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
//        query.setLimit(Constants.NUMBERS_PER_PAGE);
//        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
//        query.addWhereLessThan("createdAt", date);
//
//        query.setSkip(Constants.NUMBERS_PER_PAGE*(pageNum++));
//
//        query.include("author");
//        query.findObjects(getActivity(), new FindListener<EventInfo>() {
//
//            @Override
//            public void onSuccess(List<EventInfo> list) {
//                // TODO Auto-generated method stub
//                if(list.size()!=0&&list.get(list.size()-1)!=null){
//                    if(mRefreshType==RefreshType.REFRESH){
//                        mListItems.clear();
//                    }
//                    if(list.size()<Constant.NUMBERS_PER_PAGE){
//                        LogUtils.i(TAG,"已加载完所有数据~");
//                    }
//                    if(MyApplication.getInstance().getCurrentUser()!=null){
//                        list = DatabaseUtil.getInstance(mContext).setFav(list);
//                    }
//                    mListItems.addAll(list);
//                    mAdapter.notifyDataSetChanged();
//
//                    setState(LOADING_COMPLETED);
//                    mPullRefreshListView.onRefreshComplete();
//                }else{
//                    ActivityUtil.show(getActivity(), "暂无更多数据~");
//                    pageNum--;
//                    setState(LOADING_COMPLETED);
//                    mPullRefreshListView.onRefreshComplete();
//                }
//            }
//
//            @Override
//            public void onError(int arg0, String arg1) {
//                // TODO Auto-generated method stub
//                LogUtils.i(TAG,"find failed."+arg1);
//                pageNum--;
//                setState(LOADING_FAILED);
//                mPullRefreshListView.onRefreshComplete();
//            }
//        });
    }

    private static final int LOADING = 1;
    private static final int LOADING_COMPLETED = 2;
    private static final int LOADING_FAILED = 3;
    private static final int NORMAL = 4;

    public void setState(int state){

    }
}
