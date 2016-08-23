package com.goldenratio.commonweal.ui.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.goldenratio.commonweal.MyApplication;
import com.goldenratio.commonweal.R;
import com.goldenratio.commonweal.adapter.AttentionStarListAdapter;
import com.goldenratio.commonweal.bean.U_Attention;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AttentionStarActivity extends Activity {

    @BindView(R.id.iv_attention_back)
    ImageView mIvAttentionBack;
    @BindView(R.id.lv_attention)
    ListView mLvAttention;

    private List<U_Attention> AttentionList;
    private ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_star);
        ButterKnife.bind(this);

        AttentionList = new ArrayList<>();

        getAttentionInfoFromBmob();

    }

    private void getAttentionInfoFromBmob() {
        showProgressDialog();
        String objectID = ((MyApplication) getApplication()).getObjectID();
        BmobQuery<U_Attention> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.addWhereEqualTo("U_ID", objectID);
        query.include("Star_Info");
        query.findObjects(new FindListener<U_Attention>() {
            @Override
            public void done(List<U_Attention> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        AttentionList = list;
                        Toast.makeText(AttentionStarActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
                        mLvAttention.setAdapter(new AttentionStarListAdapter(list, AttentionStarActivity.this));
                    } else
                        Toast.makeText(AttentionStarActivity.this, "您尚未关注任何人", Toast.LENGTH_SHORT).show();
                    Log.i("查询信息成功", "done: " + list);
                } else {
                    Toast.makeText(AttentionStarActivity.this, "获取信息失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                closeProgressDialog();
            }
        });
    }

    @OnClick(R.id.iv_attention_back)
    public void onClick() {
        finish();
    }

    private void closeProgressDialog() {
        if (mPd != null && mPd.isShowing()) {
            mPd.dismiss();
            mPd = null;
        }
    }

    private void showProgressDialog() {
        if (mPd == null) {
            mPd = new ProgressDialog(this);
            mPd.setMessage("加载中");
            mPd.setCancelable(false);
            mPd.show();
        }
    }
}
