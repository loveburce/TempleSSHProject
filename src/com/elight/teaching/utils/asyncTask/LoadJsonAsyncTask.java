package com.elight.teaching.utils.asyncTask;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dawn on 2014/11/11.
 */
public class LoadJsonAsyncTask extends AsyncTask<String, Integer, String>{
    private LoadJsonAsyncTaskCallBack callBack;

    public LoadJsonAsyncTask(LoadJsonAsyncTaskCallBack callBack){
        this.callBack = callBack;
    }

    public interface LoadJsonAsyncTaskCallBack{
        public void beforeJsonLoad();
        public void onJsonLoaded(String str);
    }

    @Override
    protected void onPreExecute() {
        callBack.beforeJsonLoad();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        callBack.onJsonLoaded(s);
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String path = params[0];
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            if(connection.getResponseCode() == 200){
                InputStream inputStream = connection.getInputStream();
                byte[] data = readStream(inputStream);
                String jsonString = new String(data, "UTF-8");
//                String jsonString = new String(data);
                return jsonString;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int total = values[0];
        int max = values[1];
        super.onProgressUpdate(values);
    }

    public static byte[] readStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0 , len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }
}
