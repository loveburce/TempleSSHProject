package com.elight.teaching.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.*;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.activity.multiphotopicker.ImageZoomActivity;
import com.elight.teaching.adapter.EmoViewPagerAdapter;
import com.elight.teaching.adapter.EmoteAdapter;
import com.elight.teaching.adapter.multiphotopicker.ImagePublishAdapter;
import com.elight.teaching.custom.EmotionsEditText;
import com.elight.teaching.entity.EventInfo;
import com.elight.teaching.entity.FaceText;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.entity.multiphotopicker.ImageItem;
import com.elight.teaching.utils.ActivityUtils;
import com.elight.teaching.utils.FaceTextUtils;
import com.elight.teaching.utils.multiphotopicker.CustomConstants;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/10/17.
 */
public class EventAddFragment extends BaseFragment implements View.OnClickListener {

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new EventAddFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }


    View rootView;
    EditText content;

    private GridView gridView;
    private ImagePublishAdapter publishAdapter;
    private TextView textView;

    String targetUrl = null;

    private Button btn_publisher_emo,btn_publisher_pointer,btn_publisher_send;
    EmotionsEditText emotionsEditText;

    List<FaceText> emos;
    private ViewPager pager_emo;
    private LinearLayout linearLayoutEmotions;

    private LocationClient locationClient;
    private LocationClientOption locationClientOption;
    private boolean LBSIsReceiver;
    private String LBSAddress;
    private Drawable poi_off_icon;
    private Drawable poi_on_icon;

    @Override
    public int getIndex() {
        return super.getIndex();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_add,null);
        findViews();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    protected void findViews() {
        // TODO Auto-generated method stub
        content = (EditText) rootView.findViewById(R.id.fragment_event_add_edit_content);
        emotionsEditText = (EmotionsEditText) rootView.findViewById(R.id.fragment_event_add_edit_content);
        emotionsEditText.setOnClickListener(this);
        emotionsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_publisher_emo = (Button) rootView.findViewById(R.id.fragment_event_add_btn_chat_location);
        btn_publisher_emo.setOnClickListener(this);
        btn_publisher_pointer = (Button) rootView.findViewById(R.id.fragment_event_add_btn_chat_emotions);
        btn_publisher_pointer.setOnClickListener(this);

        gridView = (GridView) rootView.findViewById(R.id.fragment_event_add_grid_view);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        publishAdapter = new ImagePublishAdapter(getActivity(), dataList);
        gridView.setAdapter(publishAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == getImageDataSize()){

                    showPopWindow();

                } else {
                    Intent intent = new Intent(getActivity(), ImageZoomActivity.class);
                    intent.putExtra(CustomConstants.EXTRA_IMAGE_LIST, (Serializable)dataList);
                    intent.putExtra(CustomConstants.EXTRA_CURRENT_IMG_POSITION,position);
                    startActivity(intent);
                }
            }
        });

        linearLayoutEmotions = (LinearLayout) rootView.findViewById(R.id.fragment_event_add_emotions_content);
        linearLayoutEmotions.setOnClickListener(this);
        initEmoView();
        btn_publisher_send = (Button) rootView.findViewById(R.id.fragment_event_add_commit_edit);
        btn_publisher_send.setOnClickListener(this);
//        CustomApplication.getInstance().

    }



    private int getImageDataSize(){
        return dataList == null ? 0 : dataList.size();
    }


    @Override
    public void onResume() {
        super.onResume();
        notifyDataChanged();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.fragment_event_add_btn_chat_emotions:{
                if(linearLayoutEmotions.getVisibility() == View.GONE){
                    linearLayoutEmotions.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutEmotions.setVisibility(View.GONE);
                }
            }
            break;
		case R.id.fragment_event_add_commit_edit:
			String commitContent = content.getText().toString().trim();
			if(TextUtils.isEmpty(commitContent)){
                ActivityUtils.showShortToast(getActivity(),"发表内容不能为空！");
				return;
			}
			if(dataList.size() == 0){
				publishWithoutFigure(commitContent, null);
			}else{
				publish(commitContent);
			}
			break;
//            case R.id.open_layout:
//                Date date1 = new Date(System.currentTimeMillis());
//                dateTime = date1.getTime() + "";
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, REQUEST_CODE_ALBUM);
//                break;
//            case R.id.take_layout:
//                Date date = new Date(System.currentTimeMillis());
//                dateTime = date.getTime() + "";
//                File f = new File(CacheUtils.getCacheDirectory(getActivity(), true, "pic") + dateTime);
//                if (f.exists()) {
//                    f.delete();
//                }
//                try {
//                    f.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Uri uri = Uri.fromFile(f);
//                Log.e("uri", uri + "");
//
//                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                startActivityForResult(camera, REQUEST_CODE_CAMERA);
//                break;
//            default:
//                break;
        }
    }

    /*
     * 发表带图片
     */
    private void publish(final String commitContent){

        //上传前得压缩

//        final BmobFile figureFile = new BmobFile(EventInfo.class, new File(dataList.get(0).sourcePath));
//
//        figureFile.upload(getActivity(), new UploadFileListener() {
//
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                publishWithoutFigure(commitContent, figureFile);
//
//            }
//
//            @Override
//            public void onProgress(Integer arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onFailure(int arg0, String arg1) {
//            }
//        });

    }

    private void publishWithoutFigure(final String commitContent,
                                      final BmobFile figureFile) {
        UserInfo user = BmobChatUser.getCurrentUser(getActivity(), UserInfo.class);

        final EventInfo qiangYu = new EventInfo();
        qiangYu.setAuthor(user);
        qiangYu.setContent(commitContent);
        if(figureFile!=null){
            qiangYu.setImageFile(figureFile);
        }
        qiangYu.setLove(0);
        qiangYu.setHate(0);
        qiangYu.setShare(0);
        qiangYu.setComment(0);
        qiangYu.setPass(true);
        qiangYu.save(getActivity(), new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ActivityUtils.showShortToast(getActivity(), "发表成功！");
                getActivity().setResult(getActivity().RESULT_OK);
                getActivity().finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ActivityUtils.showShortToast(getActivity(), "发表失败！yg"+arg1);
            }
        });
    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.d("wangshengyanooooppp","wwwwwwwwwww : "+requestCode +" : "+resultCode);
//
//
//        if(resultCode == getActivity().RESULT_OK){
//            switch (requestCode) {
//                case REQUEST_CODE_ALBUM:
//                    String fileName = null;
////                    if(data!=null){
////                        Uri originalUri = data.getData();
////                        ContentResolver cr = CustomApplication.getInstance().getContentResolver();
////                        Cursor cursor = cr.query(originalUri, null, null, null, null);
////                        if(cursor.moveToFirst()){
////                            do{
////                                fileName= cursor.getString(cursor.getColumnIndex("_data"));
////                            }while (cursor.moveToNext());
////                        }
////
////                        Bitmap bitmap = BitmapUtils.compressImageFromFile(fileName);
////                        targeturl = CacheUtils.saveToSdCard(getActivity(),bitmap,dateTime);
////
////                        albumPic.setBackgroundDrawable(new BitmapDrawable(bitmap));
////                        takeLayout.setVisibility(View.GONE);
////                    }
//                    break;
//                case REQUEST_CODE_CAMERA:
////                    String files =CacheUtils.getCacheDirectory(getActivity(), true, "pic") + dateTime;
////                    File file = new File(files);
////                    if(file.exists()){
////                        Bitmap bitmap = BitmapUtils.compressImageFromFile(files);
////                        targeturl = CacheUtils.saveToSdCard(getActivity(), bitmap, dateTime);
////                        takePic.setBackgroundDrawable(new BitmapDrawable(bitmap));
////                        openLayout.setVisibility(View.GONE);
//
//                    Log.d("wangshengyanooooppp","wwwwwwwwwww : "+requestCode);
//                    if(dataList.size() < CustomConstants.MAX_IMAGE_SIZE && requestCode == -1 && TextUtils.isEmpty(path)){
//                        ImageItem item = new ImageItem();
//                        item.sourcePath = path;
//                        dataList.add(item);
//                    }
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


    private void notifyDataChanged(){
        publishAdapter.notifyDataSetChanged();
    }


    /*显示软键盘*/
    public void showSoftInputView(){
        if(getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
            if(getActivity().getCurrentFocus() != null){
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                        showSoftInput(emotionsEditText, 0);
            }
        }
    }

    /**
     * 初始化表情布局
     */
    private void initEmoView(){
        pager_emo = (ViewPager) rootView.findViewById(R.id.fragment_event_add_view_pager_emotions);
        emos = FaceTextUtils.faceTexts;

        List<View> views = new ArrayList<View>();
        for(int i=0; i<2; ++i){
            views.add(getGridView(i));
        }
        pager_emo.setAdapter(new EmoViewPagerAdapter(views));
    }

    private View getGridView(final int i){
        View view = View.inflate(getActivity(), R.layout.include_emo_gridview, null);
        GridView gridV = (GridView) view.findViewById(R.id.gridview);
        List<FaceText> list = new ArrayList<FaceText>();
        if(i == 0){
            list.addAll(emos.subList(0,21));
        } else if(i == 1){
            list.addAll(emos.subList(21,emos.size()));
        }
        final EmoteAdapter gridAdapter = new EmoteAdapter(getActivity(),list);
        gridV.setAdapter(gridAdapter);
        gridV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FaceText name = (FaceText) gridAdapter.getItem(position);
                String key = name.text.toString();

                try{
                    if(emotionsEditText != null && !TextUtils.isEmpty(key)){
                        int start = emotionsEditText.getSelectionStart();
                        CharSequence charSequence = emotionsEditText.getText();
                        emotionsEditText.setText(charSequence+key);
                        //定位光标位置
                        CharSequence info = emotionsEditText.getText();
                        if(info instanceof Spannable){
                            Spannable spannable = (Spannable) info;
                            Selection.setSelection(spannable, start + key.length());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    protected void initLBS(){
        locationClientOption = new LocationClientOption();
        locationClientOption.setOpenGps(true);
        locationClientOption.setCoorType("bd0911");
        locationClientOption.setScanSpan(100);
        locationClient = new LocationClient(getActivity().getApplicationContext(),locationClientOption);

        locationClient.start();
        LBSIsReceiver = true;
        locationClient.requestLocation();

//        locationClient.registerLocationListener(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                LBSAddress = bdLocation.getAddrStr();
//
//            }
//        });
    }



}
