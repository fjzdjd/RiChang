package ddw.com.richang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.model.RiActivityRecommend;
import ddw.com.richang.ui.agenda.AgendaFragment;
import ddw.com.richang.ui.column.ColumnFragment;
import ddw.com.richang.ui.everyday.EverydayFragment;
import ddw.com.richang.ui.mine.MineFragment;

/**
 * 2.0主程序入口
 * Created by zzp on 2016/5/13.
 */
public class HomeActivity extends BaseActivity {

    private static FragmentManager mFragmentManager;

    /**
     * 定义一个变量，来标识是否退出
     */
    private static boolean isExit = false;

    /**
     * 监听退出状态
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    /**
     * 装载Fragment
     */
    private ArrayList<Fragment> fragments = new ArrayList<>();

    /**
     * 日常
     */
    private RadioButton mEveryday;

    /**
     * 专栏
     */
    private RadioButton mColumn;

    /**
     * 行程
     */
    private RadioButton mAgenda;

    /**
     * 我的
     */
    private RadioButton mMine;

    /**
     * 当前选中的界面
     */
    private int currentFootIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        mFragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initWidgets();
        initFragment();



    }

    /**
     * 初始化界面
     */
    private void initWidgets() {
        mEveryday = (RadioButton) findViewById(R.id.footer_rb_everyday);
        mColumn = (RadioButton) findViewById(R.id.footer_rb_column);
        mAgenda = (RadioButton) findViewById(R.id.footer_rb_agenda);
        mMine = (RadioButton) findViewById(R.id.footer_rb_mine);

        mEveryday.setOnClickListener(new HomeOnClickListener());
        mColumn.setOnClickListener(new HomeOnClickListener());
        mAgenda.setOnClickListener(new HomeOnClickListener());
        mMine.setOnClickListener(new HomeOnClickListener());
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
        EverydayFragment everydayFragment = new EverydayFragment();
        AgendaFragment agendaFragment = new AgendaFragment();
        ColumnFragment columnFragment = new ColumnFragment();
        MineFragment mineFragment = new MineFragment();

        fragments.add(everydayFragment);
        fragments.add(columnFragment);
        fragments.add(agendaFragment);
        fragments.add(mineFragment);

        ArrayList<RiActivityRecommend> mJson = (ArrayList<RiActivityRecommend>) getIntent()
                .getSerializableExtra("mJsonRecommends");

        Bundle bundle=new Bundle();
        bundle.putSerializable("mJson",mJson);
        everydayFragment.setArguments(bundle);


        fragmentTransaction.add(R.id.content, fragments.get(0)).commit();
    }


    /**
     * 切换tab更换展示内容
     *
     * @param currentFragment 当前页
     * @param index           跳转页
     */
    public void setFootItemSelected(int currentFragment, int index) {
        if (currentFragment != index && fragments != null && fragments.get(index) != null) {
            mFragmentManager.popBackStack();
            mFragmentManager.beginTransaction().setCustomAnimations(R.anim.push_right_in, R.anim
                    .push_left_out);
            if (fragments.get(index).isAdded()) {
                mFragmentManager.beginTransaction().hide(fragments.get(currentFragment)).show
                        (fragments.get(index))
                        .commitAllowingStateLoss();
            } else {
                mFragmentManager.beginTransaction().hide(fragments.get(currentFragment))
                        .add(R.id.content, fragments.get(index)).commitAllowingStateLoss();
            }
            currentFootIndex = index;
        }
    }

    /**
     * 退出程序
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 主程序点击事件
     */
    private class HomeOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.footer_rb_everyday:
                    setFootItemSelected(currentFootIndex, 0);
                    break;

                case R.id.footer_rb_column:
                    setFootItemSelected(currentFootIndex, 1);
                    break;

                case R.id.footer_rb_agenda:
                    setFootItemSelected(currentFootIndex, 2);

                    break;

                case R.id.footer_rb_mine:
                    setFootItemSelected(currentFootIndex, 3);

                    break;


                default:
                    break;
            }

        }
    }
}
