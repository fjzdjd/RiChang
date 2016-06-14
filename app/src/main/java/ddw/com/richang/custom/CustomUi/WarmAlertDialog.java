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

    protected Context context;


    public WarmAlertDialog(Context context) {
        this.context = context;

    }

    public void initDialog(String title, View view, final View.OnClickListener click) {
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

}
