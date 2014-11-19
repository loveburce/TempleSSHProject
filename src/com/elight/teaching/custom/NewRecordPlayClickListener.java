package com.elight.teaching.custom;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.util.BmobUtils;
import com.elight.teaching.R;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by dawn on 2014/9/25.
 */
public class NewRecordPlayClickListener implements View.OnClickListener{

    BmobMsg message;
    ImageView iv_voice;
    AnimationDrawable animationDrawable = null;
    Context context;
    String currentObjectId = "";
    MediaPlayer mediaPlayer = null;
    public static boolean isPlaying = false;
    public static NewRecordPlayClickListener currentPlayListener = null;
    static BmobMsg currentMsg = null;   //用于区分两个不同的语音播放

    BmobUserManager bmobUserManager;

    public NewRecordPlayClickListener(Context context, BmobMsg message, ImageView iv_voice) {
        this.context = context;
        this.message = message;
        this.iv_voice = iv_voice;

        currentMsg = message;
        currentPlayListener = this;
        currentObjectId = BmobUserManager.getInstance(context).getCurrentUserObjectId();
        bmobUserManager = BmobUserManager.getInstance(context);
    }

    @SuppressWarnings("resource")
    public void startPlayRecord(String filePath, boolean isUseSpeaker){
        if(!(new File(filePath).exists())){
            return;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        if(isUseSpeaker){
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);  //关闭扬声器
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }

        try{
            mediaPlayer.reset();
            // 单独使用此方法会报错播放错误:setDataSourceFD failed.: status=0x80000000
            // mediaPlayer.setDataSource(filePath);
            // 因此采用此方式会避免这种错误
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            mediaPlayer.setDataSource(fileInputStream.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPlaying = true;
                    currentMsg = message;
                    mp.start();
                    startRecordAnimation();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayRecord();
                }
            });
            currentPlayListener = this;
//            isPlaying = true;
//            currentMsg = message;
//            mediaPlayer.start();
//            startRecordAnimation();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopPlayRecord(){
        stopRecordAnimation();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }

    private void startRecordAnimation(){
        if(message.getBelongId().equals(currentObjectId)){
            iv_voice.setImageResource(R.anim.anim_chat_voice_right);
        } else {
            iv_voice.setImageResource(R.anim.anim_chat_voice_left);
        }
        animationDrawable = (AnimationDrawable)iv_voice.getDrawable();
        animationDrawable.start();
    }

    private void stopRecordAnimation(){
        if(message.getBelongId().equals(currentObjectId)){
            iv_voice.setImageResource(R.drawable.voice_left3);
        } else {
            iv_voice.setImageResource(R.drawable.voice_right3);
        }
        if(animationDrawable != null){
            animationDrawable.stop();
        }
    }

    @Override
    public void onClick(View v) {
        if(isPlaying){
            currentPlayListener.stopPlayRecord();
//            if (currentMsg != null && currentMsg.hashCode() == message.hashCode()) { // 2014.9.25.17.05 自己改动了
            if (currentMsg != null && currentMsg.getBelongId() == message.getBelongId()) {
                currentMsg = null;
                return;
            }
        }
        // 如果是自己发送的语音消息，则播放本地地址
        if(message.getBelongId().equals(currentObjectId)){
            String localPath = message.getContent().split("&")[0];
            startPlayRecord(localPath, true);
        } else {
            String localPath = getDownLoadFilePath(message);
            startPlayRecord(localPath, true);
        }
    }

    public String getDownLoadFilePath(BmobMsg msg){
        String accountDir = BmobUtils.string2MD5(bmobUserManager.getCurrentUserObjectId());
        File dir = new File(BmobConfig.BMOB_VOICE_DIR + File.separator + accountDir + File.separator + msg.getBelongId());
        if(!dir.exists()){
            dir.mkdirs();
        }
        //在当前用户的目录下面存放录音文件
        File audioFile = new File(dir.getAbsolutePath() + File.separator + msg.getMsgTime() + ".amr");
        try {
            if(!audioFile.exists()){
                audioFile.createNewFile();
            }
        }catch (Exception e){

        }
        return audioFile.getAbsolutePath();
    }
}
