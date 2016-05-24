package ddw.com.richang.Activity.banner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ddw.com.richang.R;
import ddw.com.richang.controller.Config;

public class Planhint extends AppCompatActivity {

    public static int REQCODE=++Config.REQ;

    private int hintHour;
    private boolean hint1=false;
    private boolean hint2=false;
    private boolean hint3=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planhint);

        Intent intent=getIntent();
        this.hintHour=intent.getIntExtra("hintHour", 8);
        this.hint1=intent.getBooleanExtra("hint1", false);
        this.hint2=intent.getBooleanExtra("hint2", false);
        this.hint3=intent.getBooleanExtra("hint3",false);

        ((ImageView) findViewById(R.id.cancelsetplanhint)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView)findViewById(R.id.submitplanhint)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("hintHour",hintHour);
                intent.putExtra("hint1",hint1);
                intent.putExtra("hint2",hint2);
                intent.putExtra("hint3",hint3);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //hint
        final ImageView imgnohint=(ImageView)findViewById(R.id.nohint);
        final ImageView imghint1=(ImageView)findViewById(R.id.hint1);
        final ImageView imghint2=(ImageView)findViewById(R.id.hint2);
        final ImageView imghint3=(ImageView)findViewById(R.id.hint3);

        imgnohint.setVisibility((this.hint1||this.hint2||this.hint3)?View.INVISIBLE:View.VISIBLE);
        imghint1.setVisibility(this.hint1?View.VISIBLE:View.INVISIBLE);
        imghint2.setVisibility(this.hint2?View.VISIBLE:View.INVISIBLE);
        imghint3.setVisibility(this.hint3?View.VISIBLE:View.INVISIBLE);

        ((RelativeLayout)findViewById(R.id.nohintbox)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint1 = false;
                hint2 = false;
                hint3 = false;
                imgnohint.setVisibility(View.VISIBLE);
                imghint1.setVisibility(View.INVISIBLE);
                imghint2.setVisibility(View.INVISIBLE);
                imghint3.setVisibility(View.INVISIBLE);
            }
        });

        ((RelativeLayout)findViewById(R.id.hint1box)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint1=!hint1;
                imgnohint.setVisibility((hint1||hint2|hint3)?View.INVISIBLE:View.VISIBLE);
                imghint1.setVisibility(hint1?View.VISIBLE:View.INVISIBLE);
            }
        });
        ((RelativeLayout)findViewById(R.id.hint2box)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint2=!hint2;
                imgnohint.setVisibility((hint1||hint2|hint3)?View.INVISIBLE:View.VISIBLE);
                imghint2.setVisibility(hint2?View.VISIBLE:View.INVISIBLE);
            }
        });
        ((RelativeLayout)findViewById(R.id.hint3box)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint3=!hint3;
                imgnohint.setVisibility((hint1||hint2|hint3)?View.INVISIBLE:View.VISIBLE);
                imghint3.setVisibility(hint3?View.VISIBLE:View.INVISIBLE);
            }
        });

        //time
        final TextView textView=(TextView)findViewById(R.id.hinttime);
        textView.setText(hintHour+"点");


    }


}
