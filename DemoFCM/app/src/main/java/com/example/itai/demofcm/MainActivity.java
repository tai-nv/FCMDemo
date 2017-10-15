package com.example.itai.demofcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import DTO.Schedule;
import DataManager.OnLoadDataComplete;
import DataManager.ScheduleDataManager;

public class MainActivity extends AppCompatActivity implements OnLoadDataComplete<Schedule> {
    ScheduleDataManager scheduleDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleDataManager = new ScheduleDataManager();
        scheduleDataManager.setOnLoadDataComplete(this);


    }

    @Override
    public void onComplete(Schedule item) {
        scheduleDataManager.update(item.getId(), new Schedule("ghi"));

    }

    @Override
    public void onUpdate(Schedule item) {
        Toast.makeText(this, "update:"+ item.getId(), Toast.LENGTH_SHORT).show();
    }
}
