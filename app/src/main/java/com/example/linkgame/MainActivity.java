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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.linkgame.ViewDataActivity.GAME_LEVEL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView time;
    private LinkAdapter linkAdapter;
    private LinkItem preLinkItem;
    private List<Integer> clearedKeyList;

    /**
     * Timer
     */
    private CountDownTimerSuspended countDownTimerSuspended;

    public static final String GAME_DATA = "data";
    private GameLevel gameLevel;
    private ArrayList<LinkItem> dataList;

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
        setContentView(R.layout.activity_main);
        this.clearedKeyList = new ArrayList<>();
        time = (TextView) findViewById(R.id.time);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        int level = 1;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(GAME_LEVEL)) {
            level = intent.getIntExtra(GAME_LEVEL, 1);
            FadeData.initGameLevels();
            gameLevel = FadeData.getGameLevelByLevel(level);
        }
        if (intent != null && intent.hasExtra(GAME_DATA)) {
            dataList = intent.getParcelableArrayListExtra(GAME_DATA);
        }

        linkAdapter = new LinkAdapter(dataList);
        linkAdapter.setOnItemClickListener(new LinkAdapter.OnItemClickListener<LinkItem>() {
            @Override
            public void onItemClick(int position, LinkItem linkItem) {

                if (preLinkItem == null) {
                    //两个不一样，重新填充数据
                    preLinkItem = linkItem;
                } else {
                    if (preLinkItem.getKey() == linkItem.getKey()) {
                        //两个都消除
                        clearedKeyList.add(linkItem.getKey());
                    }
                    preLinkItem = null;
                    getRemainData();
                    linkAdapter.notifyDataSetChanged();
                    if (clearedKeyList.size() == gameLevel.getCardNum()) {
                        // TODO: the gamer win
                        Toast.makeText(MainActivity.this, "gamer win", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        recyclerView.setAdapter(linkAdapter);

        long intervalTime = gameLevel.getIntervalTime();
        countDownTimerSuspended = new CountDownTimerSuspended(gameLevel.getTotalTime() * intervalTime, intervalTime) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.format("The left time: %d s", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (countDownTimerSuspended != null) {
                    countDownTimerSuspended.cancel();
                }
                if (clearedKeyList.size() == gameLevel.getCardNum()) {
                    // TODO: the gamer win
                    Toast.makeText(MainActivity.this, "gamer win", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: the gamer failed
                    Toast.makeText(MainActivity.this, "game over", Toast.LENGTH_SHORT).show();
                }
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


    private void getRemainData() {
        for (int i = 0; i < dataList.size(); i++) {
            LinkItem linkItem = dataList.get(i);
            if (clearedKeyList.contains(linkItem.getKey())) {
                linkItem.setKey(-1);
            }
            dataList.set(i, linkItem);
        }
    }
}