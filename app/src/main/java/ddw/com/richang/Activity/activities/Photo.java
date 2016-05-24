package ddw.com.richang.Activity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import ddw.com.richang.R;
import ddw.com.richang.controller.data.imgloader.bitmap.NormalView;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.controller.data.imgloader.core.imageaware.ImageViewAware;

public class Photo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.photo);

        ImageView photo=(ImageView)findViewById(R.id.photo);
        ImageLoader.getInstance().displayImage(getIntent().getStringExtra("photo"), photo, NormalView.BITMAPDISPLAYER);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
