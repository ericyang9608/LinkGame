package com.example.linkgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.linkgame.MainActivity.GAME_DATA;

public class ViewDataActivity extends AppCompatActivity {

    private TextView time;
    private ViewDataAdapter viewDataAdapter;

    /**
     * Timer
     */
    private CountDownTimerSuspended countDownTimerSuspended;

    private GameLevel gameLevel;
    public static final String GAME_LEVEL = "gameLevel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_view_data);
        time = (TextView) findViewById(R.id.time);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        int level = 1;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(GAME_LEVEL)) {
            level = intent.getIntExtra(GAME_LEVEL, 1);
            FadeData.initGameLevels();
            gameLevel = FadeData.getGameLevelByLevel(level);
        }

        //设置数据源
        final ArrayList<LinkItem> dataList = FadeData.getData(gameLevel.getCardNum());
        viewDataAdapter = new ViewDataAdapter(dataList);
        recyclerView.setAdapter(viewDataAdapter);

        //开启定时
        final int finalLevel = level;
        countDownTimerSuspended = new CountDownTimerSuspended(gameLevel.getViewTime() * gameLevel.getIntervalTime(), gameLevel.getIntervalTime()) {
            //        countDownTimerSuspended = new CountDownTimerSuspended(3 * gameLevel.getIntervalTime(), gameLevel.getIntervalTime()) {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.format("The left time: %d s", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(ViewDataActivity.this, MainActivity.class);
                intent.putParcelableArrayListExtra(GAME_DATA, dataList);
                intent.putExtra(GAME_LEVEL, finalLevel);
                ViewDataActivity.this.startActivity(intent);
                ViewDataActivity.this.finish();
            }
        };
        //Start timer
        countDownTimerSuspended.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (countDownTimerSuspended != null && countDownTimerSuspended.isPaused()) {
            countDownTimerSuspended.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimerSuspended != null && !countDownTimerSuspended.isPaused()) {
            countDownTimerSuspended.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimerSuspended != null) {
            countDownTimerSuspended.cancel();
        }
    }

}