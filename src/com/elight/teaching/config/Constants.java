package com.elight.teaching.config;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;


public class Constants {

    /*Bmob的运用申请ID*/
    public static String applicationId = "8f3a3cdec985bcac2e197e6aad292390";

	// 应用名称
	public static String APP_NAME = "";

	// 图片路径
	public static final String IMAGE_URL = "";

	// 视频路径
	public static final String VIDEO_URL = "";

	// 保存参数文件夹名称
	public static final String SHARED_PREFERENCE_NAME = "dawn_elight_prefs";

	// SDCard路径
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/dawn/elight/";

	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";

	// 需要分享的图片
	public static final String SHARE_FILE = BASE_PATH + "ShareImage.png";

    //保存发送的图片的目录
    public static String My_PICTURE_PATH = SD_PATH + "/elight/image/";

    //我的头像保存目录
    public static String MyAvatarDir = "/sdcard/elight/avatar/";

    //拍照回调
    public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像

    public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
    public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
    public static final String EXTRA_STRING = "extra_string";

    public static final String ACTION_REGISTER_SUCCESS_FINISH ="register.success.finish";//注册成功之后登陆页面退出

    public static final int PUBLISH_COMMENT = 1;
    public static final int NUMBERS_PER_PAGE = 15;//每次请求返回评论条数
    public static final int SAVE_FAVOURITE = 2;
    public static final int GET_FAVOURITE = 3;
    public static final int GO_SETTINGS = 4;

    public static final String SEX_MALE = "male";
    public static final String SEX_FEMALE = "female";

    public static final List<String> listTitle = new ArrayList<String>();

    //资源地址
//    public static final String pathNetCourseware="http://www.guoxue365.cn/teacher/plus/readlist.php?mid=50&cid=1&tid=1";
    public static final String teachClassInfoPath="http://www.guoxue365.cn/teacher/plus/readlist.php?mid=50";
//    public static final String pathEducationReference="http://www.guoxue365.cn/teacher/plus/readreference.php?mid=50&cid=1&tid=1";
    public static final String teachReferInfoPath="http://www.guoxue365.cn/teacher/plus/readreference.php?mid=50";
//    public static final String pathVideoFrequency="http://www.guoxue365.cn/teacher/plus/readvideo.php?mid=50&cid=1&tid=1";
    public static final String teachVideoInfoPath="http://www.guoxue365.cn/teacher/plus/readvideo.php?mid=50";
}
