package ddw.com.richang.Activity.egg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import ddw.com.richang.R;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;

public class LogoEgg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logoegg);

        WebView webView=(WebView)findViewById(R.id.logoeggweb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/team.html");
    }
}
