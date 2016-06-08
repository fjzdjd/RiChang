package ddw.com.richang.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.manager.SharePreferenceManager;

/**
 * 我的
 * Created by zzp on 2016/5/13.
 */
public class MineFragment extends BaseFragment {

    @ViewInject(R.id.mine_img_header)
    ImageView mHeaderPic;

    @ViewInject(R.id.mine_txt_userId)
    TextView mUserId;

    @ViewInject(R.id.mine_txt_userName)
    TextView mUserName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mine_fragment_layout, container, false);

        x.view().inject(this, view);

        initWidgets();

        setClickEvent(view);
        return view;
    }

    private void initWidgets() {
        x.image().bind(mHeaderPic, SharePreferenceManager.getInstance().getString(ConstantData
                .USER_PIC, ""));

        mUserId.setText("手机号:" + SharePreferenceManager.getInstance().getString(ConstantData
                        .USER_PHONE,
                ""));

        mUserName.setText("用户名:" + SharePreferenceManager.getInstance().getString(ConstantData
                        .USER_NAME,
                ""));
    }

    @Event(value = {R.id.mine_img_setting, R.id.mine_lyt_enroll, R.id.mine_img_header, R.id
            .mine_lyt_publish, R.id
            .mine_lyt_collect, R.id.mine_lyt_feedback, R.id.mine_lyt_us}, type = View
            .OnClickListener.class)
    private void setClickEvent(View view) {
        switch (view.getId()) {
            //设置
            case R.id.mine_img_setting:

                break;

            //修改信息
            case R.id.mine_img_header:

                startActivity(new Intent(getActivity(), ModifyInfoActivity.class));

                break;

            //我的报名
            case R.id.mine_lyt_enroll:
                startActivity(new Intent(getActivity(), EnrollActivity.class));
                break;

            //我的发布
            case R.id.mine_lyt_publish:

                break;

            //我的收藏
            case R.id.mine_lyt_collect:

                break;

            //关于我们
            case R.id.mine_lyt_us:

                break;

            //建议反馈
            case R.id.mine_lyt_feedback:

                break;

            default:
                break;
        }

    }


}
