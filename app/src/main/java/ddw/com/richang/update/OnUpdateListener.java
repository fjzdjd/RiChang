package ddw.com.richang.update;


/**
 * Created by ShelWee on 14-5-16.
 */
public interface OnUpdateListener {
    /**
     * on start check
     */
    void onStartCheck();

    /**
     * on finish check
     */
    void onFinishCheck();

    void onStartDownload();

    void onDownloading(int progress);

    void onFinshDownload();

}
