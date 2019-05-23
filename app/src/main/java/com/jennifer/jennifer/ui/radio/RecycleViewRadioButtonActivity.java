package com.jennifer.jennifer.ui.radio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.ui.main.MainActivity;
import com.jennifer.jennifer.ui.radio.adapter.FirstAdapter;

import java.util.ArrayList;

public class RecycleViewRadioButtonActivity extends AppCompatActivity {
    private RecyclerView rv_test;
    private ArrayList<String> firstList = new ArrayList<>();
    private ArrayList<String> secondList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyle_view_radio_button);
        //条目模拟数据
        firstList.add("1.单选:你是谁");
        firstList.add("2.单选:你是谁");
        firstList.add("3.单选:你是谁");
        firstList.add("4.单选:你是谁");
        firstList.add("5.单选:你是谁");
        //选项模拟数据
        secondList.add("选项A");
        secondList.add("选项B");
        secondList.add("选项C");
        secondList.add("选项D");
        secondList.add("选项E");
        secondList.add("选项F");
        rv_test = findViewById(R.id.rv_test);
        //RecyclerView适配器
        rv_test.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        FirstAdapter firstAdapter = new FirstAdapter(this, firstList, secondList);
        rv_test.setAdapter(firstAdapter);
    }
}