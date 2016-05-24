package ddw.com.richang.Activity.banner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import ddw.com.richang.model.PLAN;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;

public class SetPlan extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;

    private int type;

    private PLAN plan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setplan);;

        long id=-1;
        id=getIntent().getLongExtra("pl_id",id);
        //标题
        ((TextView)findViewById(R.id.plantoptitle)).setText(id <= 0 ? "添加行程" : "修改行程");
        //返回
        ((ImageView)findViewById(R.id.cancelsetplan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //选择行程主题
        if(id>0) plan=new PLAN(getIntent());
        else plan=new PLAN();

        final LinearLayout chooseThemeList=(LinearLayout)findViewById(R.id.chooseThemeList);
        ((RelativeLayout) findViewById(R.id.chooseTheme)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int show = chooseThemeList.getVisibility();
                chooseThemeList.setVisibility(show==View.VISIBLE?View.GONE:View.VISIBLE);
            }
        });
        if(id>0) chooseThemeList.setVisibility(View.GONE);
        final int[] themesLayout=new int[]{
                R.id.chooseMeeting,
                R.id.chooseDating,
                R.id.chooseOutbusiness,
                R.id.chooseSport,
                R.id.chooseShopping,
                R.id.chooseEntertain,
                R.id.chooseParty,
                R.id.chooseOthertheme
        };

        final ImageView chosenThemepic=(ImageView)findViewById(R.id.chosenthemepic);
        final TextView chosentheme=(TextView)findViewById(R.id.chosentheme);
        for(int i=0;i<8;i++){
            final int index=i;
            ((LinearLayout)findViewById(themesLayout[i])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plan.theme=PLAN.themeList[index];
                    chooseThemeList.setVisibility(View.GONE);
                    chosenThemepic.setImageResource(PLAN.imgs[0][index]);
                    chosentheme.setText(PLAN.themeList[index].name);
                }
            });
        }
        if(id>0){
            chosenThemepic.setImageResource(PLAN.imgs[0][(int)plan.theme.id-1]);
            chosentheme.setText(plan.theme.name);
        }

        //时间
        final TextView datetimeres=(TextView)findViewById(R.id.datetimeres);
        datetimeres.setText(plan.getDatetime4tv());
        final RelativeLayout datetime=(RelativeLayout)findViewById(R.id.datetimepicker);
        final DatePicker datePicker=(DatePicker)findViewById(R.id.datePicker);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        datePicker.updateDate(plan.year, plan.month, plan.day);
        final TimePicker timePicker=(TimePicker)findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(plan.hour);
        timePicker.setCurrentMinute(plan.minute);
        ((RelativeLayout)findViewById(R.id.setplandatetime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datetime.setVisibility(View.VISIBLE);
            }
        });
        ((Button)findViewById(R.id.submitplantime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datetime.setVisibility(View.GONE);
                plan.year=datePicker.getYear();
                plan.month=datePicker.getMonth();
                plan.day=datePicker.getDayOfMonth();
                plan.hour=timePicker.getCurrentHour();
                plan.minute=timePicker.getCurrentMinute();
                datetimeres.setText(plan.getDatetime4tv());
            }
        });

        //提醒
        ((RelativeLayout) findViewById(R.id.planhint)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SetPlan.this, Planhint.class);
                intent.putExtra("hintHour", plan.hintHour);
                intent.putExtra("hint1", plan.hint1);
                intent.putExtra("hint2", plan.hint2);
                intent.putExtra("hint3", plan.hint3);
                startActivityForResult(intent, Planhint.REQCODE);
            }
        });
        String res="";
        if(this.plan.hint1 || this.plan.hint2 || this.plan.hint3){
            if(this.plan.hint1) res+=" 前一小时";
            if(this.plan.hint2) res+=" 前一天";
            if(this.plan.hint3) res+=" 前三天";
        }else res=" 不提醒";
        TextView hintres=(TextView)findViewById(R.id.hintres);
        hintres.setText(res);

        //内容
        final EditText content=(EditText) findViewById(R.id.plancontent);
        if(id>0) content.setText(plan.content);

        //确定
        final TextView sure=(TextView)findViewById(R.id.submitaplan);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(content.getText().toString().equals("")){
                Toast.makeText(SetPlan.this,"行程内容不能为空！",Toast.LENGTH_SHORT).show();
                return;
            }
            sure.setEnabled(false);
                plan.content = content.getText().toString();
                plan.submit(SetPlan.this, new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = plan.toIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        sure.setEnabled(true);
                        Toast.makeText(SetPlan.this, "设置行程失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==Planhint.REQCODE){//个性提醒时间
                this.plan.hintHour=data.getIntExtra("hintHour", 8);
                this.plan.hint1=data.getBooleanExtra("hint1", false);
                this.plan.hint2=data.getBooleanExtra("hint2", false);
                this.plan.hint3=data.getBooleanExtra("hint3", false);
                TextView hintres=(TextView)findViewById(R.id.hintres);
                String res="";
                if(this.plan.hint1 || this.plan.hint2 || this.plan.hint3){
                    if(this.plan.hint1) res+=" 前一小时";
                    if(this.plan.hint2) res+=" 前一天";
                    if(this.plan.hint3) res+=" 前三天";
                }else res=" 不提醒";
                hintres.setText(res);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
