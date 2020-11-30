package com.example.linkgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.linkgame.ViewDataActivity.GAME_LEVEL;

public class LevelActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * One level
     */
    private Button first;
    /**
     * One level
     */
    private Button second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        first = (Button) findViewById(R.id.first);
        first.setOnClickListener(this);
        second = (Button) findViewById(R.id.second);
        second.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ViewDataActivity.class);
        switch (v.getId()) {
            default:
                break;
            case R.id.first:
                // TODO:   The actual action should be to take the set of gamelevel
                intent.putExtra(GAME_LEVEL, 1);
                break;
            case R.id.second:
                intent.putExtra(GAME_LEVEL, 2);
                break;
        }
        startActivity(intent);
    }
}