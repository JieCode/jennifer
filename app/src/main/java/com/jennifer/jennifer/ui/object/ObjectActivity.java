package com.jennifer.jennifer.ui.object;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.List;

public class ObjectActivity extends BaseActivity {

    private static final String TAG = ObjectTest.class.getSimpleName();
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        tvContent = findViewById(R.id.tv_content);
        StringBuilder ss = new StringBuilder();
        ObjectTest from = new ObjectTest(null, null);
        logContent(ss, from,true);
        ObjectTest to = from;
        logContent(ss, to,false);
        to.setDataList(new ArrayList<>());
        to.setData("a");
        logContent(ss, from,true);
        logContent(ss, to,false);
        tvContent.setText(ss.toString());
    }

    private void logContent(StringBuilder ss, ObjectTest from, boolean isFrom) {
        Log.e(TAG, String.format("onCreate: %1$s:%2$s dataList:%3s data:%4$s", isFrom ? "from" : "to", from, from.dataList, from.data));
        ss.append(String.format("onCreate: %1$s:%2$s dataList:%3s data:%4$s", isFrom ? "from" : "to", from, from.dataList, from.data))
                .append("\n");
    }

    public static class ObjectTest{
        private List<String> dataList;
        private String data;

        public ObjectTest(List<String> dataList, String data) {
            this.dataList = dataList;
            this.data = data;
        }

        public List<String> getDataList() {
            return dataList;
        }

        public void setDataList(List<String> dataList) {
            this.dataList = dataList;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}