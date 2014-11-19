package com.elight.teaching.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cn.bmob.im.task.BRequest;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.elight.teaching.R;
import com.elight.teaching.adapter.ContactNearByAdapter;
import com.elight.teaching.custom.xlist.XListView;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.utils.ActivityUtils;
import com.elight.teaching.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/3.
 */
public class ContactNearbyFragment extends BaseFragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener{

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new ContactNearbyFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }


    XListView mListView;
    private View layoutView;
    ContactNearByAdapter contactNearByAdapter;
    String from = "";
    List<UserInfo> nears = new ArrayList<UserInfo>();
    //默认查询一公里内的人
    private double QUERY_KILOMETERS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_contact_nearby,null);
        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        mListView = (XListView) findViewById(R.id.list_near);
        mListView.setOnItemClickListener(this);
        //首先不允许加载更多
        mListView.setPullLoadEnable(false);
        //允许下拉
        mListView.setPullRefreshEnable(true);
        //设置监听器
        mListView.setXListViewListener(this);
        mListView.pullRefreshing();

        contactNearByAdapter = new ContactNearByAdapter(getActivity(), nears);
        mListView.setAdapter(contactNearByAdapter);
        initNearByList(false);
    }

    int curPage = 0;
    ProgressDialog progressDialog;
    private void initNearByList(final boolean isUpdate){
        if(!isUpdate){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在查询附近的人...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }
        if(!mApplication.getLatitude().equals("") && !mApplication.getLongitude().equals("")){
            double latitude = Double.parseDouble(mApplication.getLatitude());
            double longtitude = Double.parseDouble(mApplication.getLongitude());
            //封装的查询方法，当进入此页面时isUpdate 为false. 当下拉刷新的时候设置为TRUE就行
            //此方法默认每页的查询10条数据，若想查询多余10条，可在查询之前设置BRequest.QUERY_LIMIT_COUNT.
            //如：BRequest.QUERY_LIMIT_COUNT=20

            // 此方法是新增的查询指定10公里内的性别为女性的用户列表，默认包含好友列表

            //如果你不想查询性别为女的用户，可以将equalProperty设为null或者equalObj设为null即可
            bmobUserManager.queryKiloMetersListByPage(isUpdate,0,"location", longtitude, latitude, true,QUERY_KILOMETERS,"sex",false,new FindListener<UserInfo>() {
               //此方法默认查询所有带地理位置信息的且性别为女性的用户列表，如果你不想保函好友列表的话，将查询条件中的isShowFriend设置FALSE
               //userManager.queryNearByListByPage(isUpdate,0,"location", longtitude, latitude, true,"sex",false,new FindListener<User>() {
                @Override
                public void onSuccess(List<UserInfo> objects) {
                    if(CollectionUtils.isNotNull(objects)){
                        if(isUpdate){
                            nears.clear();
                        }
                        contactNearByAdapter.addAll(nears);
                        if(objects.size() < BRequest.QUERY_LIMIT_COUNT){
                            mListView.setPullLoadEnable(false);
                            ActivityUtils.showShortToast(getActivity(),"附近的人搜索完成!");
                        } else {
                            mListView.setPullLoadEnable(true);
                        }
                    } else {
                        ActivityUtils.showShortToast(getActivity(),"暂无附近的人!");
                    }
                    if(!isUpdate){
                        progressDialog.dismiss();
                    } else {
                        refreshPull();
                    }
                }

                @Override
                public void onError(int i, String s) {
                    ActivityUtils.showShortToast(getActivity(),"暂无附近的人!");
                    mListView.setPullLoadEnable(false);
                    if(!isUpdate){
                        progressDialog.dismiss();
                    } else {
                        refreshPull();
                    }

                }
            });
        } else {
            ActivityUtils.showShortToast(getActivity(),"暂无附近的人!");
            progressDialog.dismiss();
        }
    }

    /*查询更多*/
    private void queryMoreNearList(int page){
        double latitude = Double.parseDouble(mApplication.getLatitude());
        double longtitude = Double.parseDouble(mApplication.getLongitude());
        //查询10公里范围内的性别为女的用户列表
        bmobUserManager.queryKiloMetersListByPage(true,page,"location",longtitude,latitude,true,QUERY_KILOMETERS,"sex",false, new FindListener<UserInfo>() {
           //查询10公里范围内的性别为女的用户列表
           //userManager.queryNearByListByPage(true,page, "location", longtitude, latitude, true,"sex",false,new FindListener<User>() {
            @Override
            public void onSuccess(List<UserInfo> objects) {
                if(CollectionUtils.isNotNull(objects)){
                    contactNearByAdapter.addAll(objects);
                }
                refreshLoad();
            }

            @Override
            public void onError(int i, String s) {
                mListView.setPullLoadEnable(false);
                refreshLoad();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfo userInfo = (UserInfo) contactNearByAdapter.getItem(position-1);
//        Intent intent = new Intent(getActivity(),)
//        进入用户详细信息页面
    }

    @Override
    public void onRefresh() {
        initNearByList(true);
    }

    private void refreshLoad(){
        if(mListView.getPullLoading()){
            mListView.stopLoadMore();
        }
    }

    private void refreshPull(){
        if(mListView.getPullRefreshing()){
            mListView.stopRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        double latitude = Double.parseDouble(mApplication.getLatitude());
        double longtitude = Double.parseDouble(mApplication.getLongitude());
        //这是查询10公里范围内的性别为女用户总数
        bmobUserManager.queryKiloMetersTotalCount(UserInfo.class, "location", longtitude, latitude, true, QUERY_KILOMETERS, "sex", false, new CountListener() {
            //这是查询附近的人且性别为女性的用户总数
//		userManager.queryNearTotalCount(User.class, "location", longtitude, latitude, true,"sex",false,new CountListener() {

            @Override
            public void onSuccess(int arg0) {
                // TODO Auto-generated method stub
                if (arg0 > nears.size()) {
                    curPage++;
                    queryMoreNearList(curPage);
                } else {
                    ActivityUtils.showShortToast(getActivity(),"数据加载完成");
                    mListView.setPullLoadEnable(false);
                    refreshLoad();
                }
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                refreshLoad();
            }
        });
    }
}
