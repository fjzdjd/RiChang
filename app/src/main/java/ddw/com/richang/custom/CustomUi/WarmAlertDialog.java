package ddw.com.richang.custom.CustomUi;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ddw.com.richang.R;

/**
 * 弹出提示框
 * Created by zzp on 2016/6/14.
 */
public class WarmAlertDialog {

    public static Context context;

    public WarmAlertDialog() {

    }

    public static WarmAlertDialog getInstance() {
        return SingletonHolder.sInstance;
    }


    public void initDialog(Context context, String title, View view, final View.OnClickListener
            click) {
        View layout = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
        ((RelativeLayout) layout.findViewById(R.id.dialog_container)).addView(view);
        ((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.show();
        layout.findViewById(R.id.dialog_cancel).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        layout.findViewById(R.id.dialog_sure).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onClick(v);
                alertDialog.dismiss();
            }
        });
    }

    private static class SingletonHolder {
        private static final WarmAlertDialog sInstance = new WarmAlertDialog();
    }

}
