package com.nd.sdp.player.demo.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.brucetoo.listvideoplay.demo.ListSupportFragment;
import com.brucetoo.listvideoplay.demo.PagerSupportFragment;
import com.nd.sdp.bk.video.R;
import com.nd.sdp.video.tracker.Tracker;

/**
 * @author JiaoYun
 * @date 2019/10/17 20:27
 */
public class PagerSupportActivity extends AppCompatActivity {

    public static void start(Context context){
        context.startActivity(new Intent(context,PagerSupportActivity.class));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contain);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.root, new ListSupportFragment(), "PagerSupportFragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        boolean attach = Tracker.isAttach(this);
        if(attach){
            Tracker.destroy(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Tracker.onConfigurationChanged(this, newConfig);
    }
}
