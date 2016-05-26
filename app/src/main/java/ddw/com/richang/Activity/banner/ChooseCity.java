package ddw.com.richang.Activity.banner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ddw.com.richang.MainActivity;
import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.imgloader.bitmap.NormalView;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.controller.view.layout.HLView;
import ddw.com.richang.model.common.IdNameMap;

/**
 * 选择城市
 */
public class ChooseCity extends BaseActivity {
    public static int REQCODE = ++Config.REQ;
    ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecity);
        //从网络获取城市列表
        bitmaps.clear();
        final Runnable error = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChooseCity.this, "城市获取出错", Toast.LENGTH_SHORT).show();
            }
        };
        if (Config.CITYS.size() > 0)
            showCity();
        else
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String h = WebHTTP.getStr(Config.getInterface().getCities);
                    try {
                        JSONObject obj = new JSONObject(h);
                        if (200 == obj.getInt("code")) {
                            JSONArray array = obj.getJSONArray("data");
                            Config.CITYS.clear();
                            for (int i = 0; i < array.length(); i++) {
                                Config.CITYS.add(new IdNameMap(array.getJSONObject(i).getLong
                                        ("ct_id"), array.getJSONObject(i).getString("ct_name")));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showCity();
                                }
                            });
                        } else {
                            runOnUiThread(error);
                        }
                    } catch (Exception e) {
                        runOnUiThread(error);
                    }
                }
            }).start();
        //关闭
        ImageView close = (ImageView) findViewById(R.id.closechoosecity);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showCity() {
        final HLView citylist = (HLView) findViewById(R.id.citylist);
        for (int i = 0; i < Config.CITYS.size(); i++) {
            final ImageView img = new ImageView(getApplicationContext());
            img.setVisibility(View.VISIBLE);
            final String url = Config.getInterface().picDomain + "/img/city/" + Config.CITYS.get
                    (i).id + ".png";
            final TextView tv = new TextView(getApplicationContext());
            ImageLoader.getInstance().displayImage(url, img, NormalView.BITMAPDISPLAYER);

            final LinearLayout onecity = new LinearLayout(getApplicationContext());
            onecity.setOrientation(LinearLayout.VERTICAL);
            onecity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            onecity.setPadding(18 * Config.SCALE, 20 * Config.SCALE, 18 * Config.SCALE, 20 *
                    Config.SCALE);
            onecity.setGravity(Gravity.CENTER_HORIZONTAL);

            final LinearLayout cityText = new LinearLayout(getApplicationContext());
            tv.setText(Config.CITYS.get(i).name);
            tv.setTextColor(Color.BLACK);
            tv.setPadding(2 * Config.SCALE, 0, 2 * Config.SCALE, 0);
            cityText.setOrientation(LinearLayout.HORIZONTAL);
            ImageView loc = new ImageView(getApplicationContext());
            loc.setImageResource(R.drawable.location_icon);
            cityText.addView(loc);
            cityText.addView(tv);
            tv.measure(0, 0);
            int height = (int) (tv.getMeasuredHeight() * 0.75);
            loc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, height));
            loc.setVisibility(Config.getUSR().getCity().id == Config.CITYS.get(i).id ? View
                    .VISIBLE : View.GONE);
            cityText.setGravity(Gravity.CENTER);

            onecity.addView(img, 80 * Config.SCALE, 80 * Config.SCALE);
            onecity.addView(cityText);

            final long city = Config.CITYS.get(i).id;
            onecity.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(ChooseCity.this,
                                                       MainActivity.class);
                                               intent.putExtra("city", city);
                                               setResult(Activity.RESULT_OK, intent);
                                               finish();
                                           }
                                       }
            );
            citylist.addView(onecity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null && !bitmap.isRecycled())
                bitmap.recycle();
        }
        bitmaps.clear();
    }
}
