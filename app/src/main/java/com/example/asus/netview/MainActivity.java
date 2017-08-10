package com.example.asus.netview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<NetViewData> list = new ArrayList<>();
        list.add(new NetViewData("语文", 80));
        list.add(new NetViewData("数学", 90));
        list.add(new NetViewData("英语", 70));
        list.add(new NetViewData("物理", 100));
        list.add(new NetViewData("化学", 80));


        NetView netView = (NetView) findViewById(R.id.netview);
        netView.setMdataList(list);
    }
}
