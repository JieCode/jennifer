package com.jennifer.jennifer.ui.palette;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.ui.palette.view.MyPalette;

public class PaletteActivity extends AppCompatActivity implements View.OnClickListener {
    private MyPalette myPalette;
    private Button btnPaint, btnEraser, btnPaintRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        initView();
        initListener();
    }

    private void initView() {
        myPalette = findViewById(R.id.my_palette);
        btnPaint = findViewById(R.id.btn_paint);
        btnPaintRed = findViewById(R.id.btn_paint_red);
        btnEraser = findViewById(R.id.btn_eraser);
    }

    private void initListener() {
        btnPaint.setOnClickListener(this);
        btnPaintRed.setOnClickListener(this);
        btnEraser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_paint:
                myPalette.selectPaintStyle(MyPalette.MODE_PAINT);
                break;
            case R.id.btn_paint_red:
                myPalette.selectPaintStyle(MyPalette.MODE_PAINT_RED);
                break;
            case R.id.btn_eraser:
                myPalette.selectPaintStyle(MyPalette.MODE_ERASER);
                break;
        }
    }
}
