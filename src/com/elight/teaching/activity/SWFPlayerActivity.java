package com.elight.teaching.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import com.elight.teaching.R;
import com.elight.teaching.multithreaddownload.download.DownloadFile;
import com.elight.teaching.multithreaddownload.download.DownloadProgressListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by dawn on 2014/11/13.
 */
public class SWFPlayerActivity extends BaseActivity{
    private WebView webView;
    private ImageView launcherImage;
    private Button btnPlay,btnPause,btnStop,btnPre,btnNext,btnFull,btnExit,btnFile;
    private LinearLayout linearLayout;
    private String flashName;
    private String swfName;
    private boolean isFullScreen;
    private int screenWidth, screenHeight;
    private String htmlFile;
    private ProgressBar progressBar;
    private TextView resultView;
    private String swfNetPath;
    File saveDir;



    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int size = msg.getData().getInt("size");
                    progressBar.setProgress(size);
                    float num = (float)progressBar.getProgress() / (float)progressBar.getMax();
                    int result = (int) (num * 100);
                    if(progressBar.getProgress() == progressBar.getMax()){

                        Log.d("wangshengyankklklkl d","save : "+"file:///"+saveDir.getAbsolutePath()+swfName);

                        webView.loadUrl("file:///"+saveDir.getAbsolutePath()+swfName);

//                        webView.loadUrl("file:///android_asset/1-130Q4161R5.swf");

//                        webView.loadUrl("file:///android_asset/gxt/gxt.swf");

                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);

                        cancelProgressDialog();
                    }
                    break;
                case -1:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题，设置全屏，必须在添加显示内容前，否则报错
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_swf_player);

        swfNetPath = getIntent().getStringExtra("swfUrl");

        swfName = swfNetPath.substring(swfNetPath.lastIndexOf("/"),swfNetPath.length());

        Log.d("wangshengyankklklkl d","save : 1 0 "+swfName);

        findViewById();
        initView();

        //mWebView.loadUrl("file:///sdcard/hht/gxt/国学堂.swf");//ok

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Log.d("wangshengyankklklkl d","save : 1 ");
            saveDir = Environment.getExternalStorageDirectory();
            downloadSwfData(swfNetPath, saveDir);
            showProgressDialog();
        }

        if(check()){

        }else{
            installPlugins();
        }


    }

    @Override
    protected void findViewById() {
        webView = (WebView) findViewById(R.id.activity_swf_player_web_view);
        progressBar = (ProgressBar) findViewById(R.id.activity_swf_player_progress_bar);
        linearLayout = (LinearLayout) findViewById(R.id.activity_swf_player_ll);
    }

    @Override
    protected void initView() {
        WebSettings settings = webView.getSettings();
        settings.setPluginState(WebSettings.PluginState.ON);//设置adobe插件可用
        settings.setJavaScriptEnabled(true);
    }



    private boolean check(){
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> infoList = packageManager.getInstalledPackages(PackageManager.GET_SERVICES);
        for(PackageInfo info : infoList){
            if("com.adobe.flashplayer".equals(info.packageName)){
                return true;
            }
        }
        return false;
    }

    private class AndroidBridge{
        public void goMarket(){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent installIntent = new Intent("android.intent.action.VIEW");
                    installIntent.setData(Uri.parse("market://details?id=com.adobe.flashplayer"));
                    startActivity(installIntent);
                }
            });
        }
    }

    private void installPlugins(){
        webView.addJavascriptInterface(new AndroidBridge(),"android");
        webView.loadUrl("file:///android_asset/go_market.html");
    }

    private DownloadTask downloadTask;

    private void downloadSwfData(String path, File saveDir){

        Log.d("wangshengyankklklkl d","save : 1.1 "+path +" : "+saveDir);

        downloadTask = new DownloadTask(path, saveDir);
        new Thread(downloadTask).start();
    }

    private void exitSwfDataDownload(){
        if(downloadTask != null)
            downloadTask.exit();
    }

    private final class DownloadTask implements Runnable{
        private String path;
        private File saveDir;
        private DownloadFile downloadFile;

        private DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        @Override
        public void run() {
            Log.d("wangshengyankklklkl d","save : 2.1 ");

            try{
                downloadFile = new DownloadFile(getApplicationContext(), path, saveDir,3);
                progressBar.setMax(downloadFile.getFileSize()); //获取文件大小设置进度条

//                swfName = downloadFile.
                downloadFile.download(new DownloadProgressListener() {
                    @Override
                    public void onDownloadSize(int size) {
                        Log.d("wangshengyankklklkl d","save : 2 size "+size);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.getData().putInt("size",size);
                        mHandler.sendMessage(msg);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                mHandler.sendMessage(mHandler.obtainMessage(-1));
            }
        }

        public void exit(){
            if(downloadFile != null){
                if(downloadFile != null) downloadFile.exit();
            }
        }
    }

}
