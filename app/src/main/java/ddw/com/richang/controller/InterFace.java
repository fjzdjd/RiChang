package ddw.com.richang.controller;

/**
 * 接口
 * Created by dingdewen on 15/12/14.
 */
public class InterFace {//单例
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
    public String getFlash;
    public String getHotSearch;
    public String getSearch;

    /**
     * 获取推荐列表
     */
    public String getRecommendActivity;

    /**
     * 设置标签
     */
    public String setTags;

    /**
     * 获取活动详情
     */
    public String getActivityContent;
    public String getIndustry;
    public String getIndActivity;
    public String getVersion;
    public String getUsrActivity;
    public String getUsrPlan;
    public String setCity;


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
    public String resetPasswd;

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
        this.getIndustry = domain + "/Home/Industry/getAllIndustries";
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
    }

    public static InterFace getInstance() {
        return InterFace.INTERFACE;
    }
}