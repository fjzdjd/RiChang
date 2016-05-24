package ddw.com.richang.components.ui.CustomUi;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.TextView;

import ddw.com.richang.R;


/**
 * 可自定义菊花
 *
 * @author zzp
 */
public class WaitingAlertDialog {
    private android.app.AlertDialog alertDialog;


    private TextView messageTextView;

    private int backgroundColor;
    private Window window;

    public WaitingAlertDialog(Context context) {
        alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.show();

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);

        window = alertDialog.getWindow();
        window.setContentView(R.layout.custom_waitingdialog_layout);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;

        window.setLayout(w_screen, h_screen);

        //设置对话框背景色为透明
        window.setBackgroundDrawableResource(R.color.white);
        messageTextView = (TextView) window.findViewById(R.id.loading_text);
    }

    /**
     * 显示progressBar
     *
     * @param context this
     * @param msgText 显示文本
     */
    public WaitingAlertDialog(Context context, String msgText,int backgroundColor) {
        this(context);
        setShowText(msgText);
        window.setBackgroundDrawableResource(backgroundColor);
    }

    /**
     * 显示progressBar
     *
     * @param context this
     * @param msgText 显示文本
     */
    public WaitingAlertDialog(Context context, int msgText) {
        this(context);

        setShowText(msgText);
    }

    /**
     * 显示progressBar
     *
     * @param context this
     * @param msgText 显示文本
     */
    public WaitingAlertDialog(Context context, String msgText) {
        this(context);
        setShowText(msgText);
    }


    /**
     * 菊花下的字
     *
     * @param resId 资源文件中
     */
    public void setShowText(int resId) {
        messageTextView.setText(resId);
    }

    /**
     * 菊花下的字
     *
     * @param message 菊花下的字(string)
     */
    public void setShowText(String message) {
        messageTextView.setText(message);
    }

    /**
     * 判断菊花是否在
     *
     * @return boolean值
     */
    public boolean isShown() {
        return alertDialog.isShowing();
    }

    /**
     * 显示菊花
     */
    public void showUp() {
        alertDialog.show();
    }

    /**
     * 关闭菊花
     */
    public void dismiss() {
        alertDialog.dismiss();
    }

}