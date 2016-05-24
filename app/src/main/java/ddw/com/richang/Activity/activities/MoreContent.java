package ddw.com.richang.Activity.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import ddw.com.richang.R;

public class MoreContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_content);

        ((ImageView) findViewById(R.id.morecontent_back)).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String html = getIntent().getStringExtra("html");
        final String text = getIntent().getStringExtra("title");
        final WebView webView = (WebView) findViewById(R.id.acthtml);
        String css = "<head><meta charset='utf-8' /><meta name=\"viewport\" " +
                "content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0," +
                "user-scalable=no\"/><style>*{font-size:16px;max-width:100%;" +
                "word-break:break-all}\nh2{font-size:20px;color:#646464}" +
                ".contentsss{line-height:150%;color:#646464}</style></head>";
        String head = "<h2>" + text + "</h2><hr />";
        String showHtml = css + head + "<div class='contentsss'>" + html + "</div>";
        webView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        webView.loadData(showHtml, "text/html; charset=UTF-8", null);//这种写法可以正确解码
    }

}
