package com.elight.teaching.multithreaddownload.download;

import android.content.Context;
import android.util.Log;
import com.elight.teaching.multithreaddownload.service.FileService;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dawn on 2014/11/13.
 */
public class DownloadFile {
    private static final String TAG = DownloadFile.class.getSimpleName();
    private Context context;
    private FileService fileService;
    /*停止下载*/
    private boolean exit;
    /*已下载文件长度*/
    private int downloadSize = 0;
    /*原始文件大小*/
    private int fileSize = 0;
    /*线程数*/
    private DownloadThread[] threads;
    /*本地保存文件*/
    private File saveFile;
    /*缓存各线程下载的长度*/
    private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
    /*每条线程下载的长度*/
    private int block;
    /*下载路径*/
    private String downloadUrl;
    /*下载的文件名*/
    private String swfName;

    /**
     * 获取线程数
     * @return
     */
    public int getThreadSize(){
        return threads.length;
    }

    /**
     * 退出下载
     */
    public void exit(){
        this.exit = true;
    }

    /**
     * 取得是否退出
     * @return
     */
    public boolean getExit(){
        return this.exit;
    }

    /**
     * 获取文件大小
     * @return
     */
    public int getFileSize(){
        return fileSize;
    }

    /**
     * 新增下载文件长度
     * @param size
     */
    protected synchronized void append(int size){
        downloadSize += size;
    }

    /**
     * 更新指定线程最后下载的位置
     * @param threadId  线程ID
     * @param pos       最后下载的位置
     */
    protected synchronized void update(int threadId, int pos){
        this.data.put(threadId, pos);
        this.fileService.updateData(this.downloadUrl, threadId, pos);
    }

    /**
     * 构建文件下载器
     * @param context   传入上下文
     * @param downloadUrl   下载文件路径
     * @param fileSaveDir   文件保存路径
     * @param threadNum     最后下载的位置
     */
    public DownloadFile(Context context, String downloadUrl, File fileSaveDir, int threadNum){
        try {
            this.context = context;
            this.downloadUrl = downloadUrl;
            fileService = new FileService(this.context);
            URL url = new URL(this.downloadUrl);
            if(!fileSaveDir.exists()) fileSaveDir.mkdirs();
            this.threads = new DownloadThread[threadNum];

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5*1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Referer", downloadUrl);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.connect();

            printResponseHeader(connection);
            if(connection.getResponseCode() == 200){
                this.fileSize = connection.getContentLength();  //根据响应获取文件大小
                if(this.fileSize <= 0){
                    throw  new RuntimeException("Unkown file size ");
                }

                String filename = getFileName(connection);    //获取文件名称
                this.saveFile = new File(fileSaveDir, filename);    //构建保存文件
                Map<Integer, Integer> logData = fileService.getData(downloadUrl);   //获取下载记录
                if(logData.size() > 0){     //如果存在下载记录
                    for(Map.Entry<Integer, Integer> entry : logData.entrySet()){
                        //把各条线程已经下载的数据长度放入data中
                        data.put(entry.getKey(), entry.getValue());
                    }
                }
                //下面计算所有线程已经下载的数据总长度
                if(this.data.size() == this.threads.length){
                    for(int i=0; i<this.threads.length; i++){
                        this.downloadSize += this.data.get(i+1);
                    }
                }
                //计算每条线程下载的数据长度
                this.block = (this.fileSize % this.threads.length)==0? this.fileSize / this.threads.length : this.fileSize / this.threads.length + 1;
            }else{
                throw  new RuntimeException("server no response ");
            }
        }catch (Exception e){
            print(e.toString());
            throw  new RuntimeException("don't connection this url");
        }
    }

    /**
     * 获取文件名
     * @param connection
     * @return
     */
    private String getFileName(HttpURLConnection connection){
        String fileName = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/')+1);
        if(fileName == null || "".equals(fileName.trim())){     //如果获取不到文件名
            for(int i=0; ; i++){
                String mine = connection.getHeaderField(i);
                if(mine == null){
                    break;
                }
                if("content-disposition".equals(connection.getHeaderFieldKey(i).toLowerCase())){
                    Matcher matcher = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if(matcher.find()) return matcher.group(1);
                }
            }
            fileName = UUID.randomUUID()+".tmp";    //默认取一个文件名
        }
        return fileName;
    }

    public int download(DownloadProgressListener listener) throws Exception{
        try {
            RandomAccessFile randOut = new RandomAccessFile(this.saveFile,"rw");
            if(this.fileSize > 0){
                randOut.setLength(this.fileSize);
            }
            randOut.close();
            URL url = new URL(this.downloadUrl);
            //如果原先未曾下载或者原先的下载线程数与现在的线程数不一致
            if(this.data.size() != this.threads.length){
                this.data.clear();
                for(int i=0; i<this.threads.length; i++){
                    //初始化每条线程已经下载的数据长度为0
                    this.data.put(i+1, 0);
                }
                this.downloadSize = 0;
            }
            for (int i = 0; i < this.threads.length; i++) {//开启线程进行下载
                int downLength = this.data.get(i+1);
                if(downLength < this.block && this.downloadSize<this.fileSize){//判断线程是否已经完成下载,否则继续下载
                    this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);
                    this.threads[i].setPriority(7);
                    this.threads[i].start();
                }else{
                    this.threads[i] = null;
                }
            }
            fileService.deleteData(this.downloadUrl);//如果存在下载记录，删除它们，然后重新添加
            fileService.saveData(this.downloadUrl, this.data);
            boolean notFinish = true;//下载未完成
            while (notFinish) {// 循环判断所有线程是否完成下载
                Thread.sleep(900);
                notFinish = false;//假定全部线程下载完成
                for (int i = 0; i < this.threads.length; i++){
                    if (this.threads[i] != null && !this.threads[i].isFinish()) {//如果发现线程未完成下载
                        notFinish = true;//设置标志为下载没有完成
                        if(this.threads[i].getDownLength() == -1){//如果下载失败,再重新下载
                            this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);
                            this.threads[i].setPriority(7);
                            this.threads[i].start();
                        }
                    }
                }
                if(listener!=null) listener.onDownloadSize(this.downloadSize);//通知目前已经下载完成的数据长度
            }
            if(downloadSize == this.fileSize) fileService.deleteData(this.downloadUrl);//下载完成删除记录
        }catch (Exception e) {
            print(e.toString());
            throw new Exception("file download error");
        }
        return this.downloadSize;
    }

    /**
     * 打印Http头字段
     * @param http
     */
    public static void printResponseHeader(HttpURLConnection http){
        Map<String, String> header = getHttpResponseHeader(http);
        for(Map.Entry<String, String> entry : header.entrySet()){
            String key = entry.getKey()!=null ? entry.getKey()+ ":" : "";
            print(key+ entry.getValue());
        }
    }

    /**
     * 获取Http响应头字段
     * @param http
     * @return
     */
    public static Map<String, String> getHttpResponseHeader(HttpURLConnection http){
        Map<String, String> header = new LinkedHashMap<String, String>();
        for(int i=0; ; i++){
            String mine = http.getHeaderField(i);
            if(mine == null) break;
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }

    private static void print(String msg){
        Log.i(TAG, msg);
    }
}
