package ddw.com.richang.controller;

/**
 * 接口
 * Created by dingdewen on 15/12/14.
 */
public class InterFace {
    /**
     * 实现单例
     */
    public static InterFace INTERFACE = new InterFace();

    /**
     * 图片
     */
    public String picDomain = "http://img.myrichang.com";

    /**
     * 用户登录
     */
    public String getUSRInfo;

    /**
     * 获取精选
     */
    public String getAlbums;

    /**
     * 获取精选详情
     */
    public String getAlbumAcs;

    /**
     * 获取城市列表
     */
    public String getCities;

    /**
     * 获取所有标签
     */
    public String getAllTags;

    /**
     * 用户设置的标签值
     */
    public String getMyTags;

    /**
     * banner images
     */
    public String getFlash;
    public String getHotSearch;

    /**
     * 活动搜索
     */
    public String getSearch;

    /**
     * 获取推荐列表
     */
    public String getRecommendActivity;

    /**
     * 获取推荐的发布者列表
     */
    public String getPublisherRecommend;

    /**
     * 设置标签
     */
    public String setTags;

    /**
     * 获取活动详情
     */
    public String getActivityContent;

    /**
     * 获取专栏 menu
     */
    public String getAllIndustries;

    /**
     * 获取专栏 list
     */
    public String getIndActivity;

    /**
     * 获取版本信息
     */
    public String getVersion;

    /**
     * 获取用户发布，参加，收藏，报名活动
     */
    public String getUsrActivity;

    /**
     * 获取附近的活动
     */
    public String getNearbyAcs;

    public String getUsrPlan;
    public String setCity;

    /**
     * 收藏
     */
    public String setCollection;
    public String setAccount;
    public String joinPlan;
    public String setPlan;
    public String addPlan;
    public String feedBack;
    public String deletePlan;
    public String submitImg;

    /**
     * 获取短信验证码
     */
    public String sendSMS;
    public String isExist;

    /**
     * 密码重置
     */
    public String resetPasswd;

    /**
     * 热门评论
     */
    public String getPopularComments;

    private InterFace() {
        String domain = "http://app.myrichang.com";
        this.getUSRInfo = domain + "/home/Person/login";
        this.getCities = domain + "/Home/PersonalInfo/getCityList";
        this.getAllTags = domain + "/Home/PersonalInfo/getAllTags";
        this.getMyTags = domain + "/Home/PersonalInfo/getUsrTags";
        this.getFlash = domain + "/Home/Activity/getFlash";
        this.getHotSearch = domain + "/Home/Activity/getPopularSearch";
        this.getSearch = domain + "/Home/Activity/getActivitySearch";
        this.getRecommendActivity = domain + "/Home/Activity/getActivityRecommend";
        this.getActivityContent = domain + "/Home/Activity/getActivityContent";
        this.getAllIndustries = domain + "/Home/Industry/getAllIndustries";
        this.getIndActivity = domain + "/Home/Industry/checkIndustry";
        this.getVersion = domain + "/Home/Person/getVersion?app_type=1";
        this.getUsrActivity = domain + "/Home/Person/getUserActivity";
        this.getUsrPlan = domain + "/Home/Plan/getPlan";

        this.setCity = domain + "/Home/PersonalInfo/SetCity";
        this.setTags = domain + "/Home/PersonalInfo/setTags";
        this.setCollection = domain + "/Home/Activity/setActivityCollect";
        this.setAccount = domain + "/Home/Person/modifyAccount";

        this.addPlan = domain + "/Home/Plan/addPlan";
        this.setPlan = domain + "/Home/Plan/addPlan";
        this.joinPlan = domain + "/Home/Activity/joinTrip";
        this.feedBack = domain + "/Home/Person/putFeedback";
        this.deletePlan = domain + "/Home/Plan/delPlan";
        this.submitImg = "http://myrichang.com/Publish/Public/Uploads/android/upload_android.php";

        this.sendSMS = domain + "/Home/Person/sendMobileMsg";
        this.isExist = domain + "/Home/Person/isExist";
        this.resetPasswd = domain + "/Home/Person/reset";

        this.getAlbums = "http://appv2.myrichang.com/Home/Industry/getAlbums";
        this.getAlbumAcs = "http://appv2.myrichang.com/Home/Industry/getAlbumAcs";
        this.getNearbyAcs = "http://appv2.myrichang.com/Home/Activity/getNearbyAcs";
        this.getPublisherRecommend = "http://appv2.myrichang" +
                ".com/Home/UserRelation/getPublisherRecommend";
        this.getPopularComments = "http://appv2.myrichang.com/Home/Comment/getPopularComments";
    }

    public static InterFace getInstance() {
        return InterFace.INTERFACE;
    }
}