package ddw.com.richang.Activity.banner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import ddw.com.richang.Activity.activities.Content;
import ddw.com.richang.model.PLAN;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;

public class PlanInfo extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;
    ImageView themePic;
    TextView themeTv;
    TextView time;
    TextView content;
    TextView hint;

    boolean chg=false;
    PLAN plan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planinfo);

        //
        plan=new PLAN(getIntent());
        Log.e("outplaninfo",plan.id+"");
        if(plan.id<=0){
            Toast.makeText(PlanInfo.this,"行程错误",Toast.LENGTH_SHORT).show();
            finish();
        }
        //添加详情＝
        themePic=(ImageView) findViewById(R.id.infothemepic);
        themePic.setImageResource(PLAN.imgs[0][(int) (plan.theme.id - 1<0?0:plan.theme.id - 1)]);
        themeTv=(TextView)findViewById(R.id.infotheme);
        themeTv.setText(plan.theme.name);
        time=(TextView)findViewById(R.id.infotime);
        time.setText(plan.getDatetime4tv());
        content=(TextView)findViewById(R.id.infocontent);
        content.setText(plan.content);
        hint=(TextView)findViewById(R.id.infohint);
        if(!plan.hint1 && !plan.hint2 && !plan.hint3) hint.setText("不提醒");
        else hint.setText(
                ((plan.hint1?"前一小时\n":"")+
                        (plan.hint2?"前一天\n":"")+
                        (plan.hint3?"前三天\n":"")).trim()
        );

        TextView edit=(TextView)findViewById(R.id.editplan);
        if(plan.act_id>0){
            edit.setText("查看");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(PlanInfo.this, Content.class);
                    intent.putExtra("act_id",plan.act_id);
                    startActivityForResult(intent,Content.REQCODE);
                }
            });
        }else edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=plan.toIntent();
                intent.setClass(PlanInfo.this, SetPlan.class);
                intent.putExtra("pl_id",plan.id);
                startActivityForResult(intent,SetPlan.REQCODE);
            }
        });

        //返回
        ImageView back=(ImageView)findViewById(R.id.cancelplaninfo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //删除
        ((Button)findViewById(R.id.deleteplaninfo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PlanInfo.this).setTitle("确定要删除这个行程吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                plan.delete(PlanInfo.this, new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = plan.toIntent();
                                        intent.putExtra("delete", true);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }, new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PlanInfo.this,"删除失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK!=resultCode) return;
        if(SetPlan.REQCODE==requestCode || Content.REQCODE==requestCode){
            plan=new PLAN(data);
            chg=true;
            //更新界面
            themePic.setImageResource(PLAN.imgs[0][(int) plan.theme.id-1]);
            themeTv.setText(plan.theme.name);
            content.setText(plan.content);
            time.setText(plan.getDatetime4tv());
            if(!plan.hint1 && !plan.hint2 && !plan.hint3) hint.setText("不提醒");
            else hint.setText(
                    ((plan.hint1?"前一小时\n":"")+
                    (plan.hint2?"前一天":"")+
                    (plan.hint3?"前三天":"")).trim()
            );
        }
    }

    @Override
    public void onBackPressed() {
        if(chg){
            Intent intent=plan.toIntent();
            intent.putExtra("change",true);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
