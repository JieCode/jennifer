package com.jennifer.jennifer.ui.ceiling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jennifer.jennifer.R;
import com.jennifer.jennifer.entity.TermsEntity;
import com.jennifer.jennifer.entity.TermsResultEntity;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

public class CeilingActivity extends AppCompatActivity {

    private TermsAdapter adapter;
    private List<TermsEntity> termsList = new ArrayList<>();
    private LinearLayoutManager manager;
    private List<String> letterList = new ArrayList<>();
    private RecyclerView recyclerView;
    private IndexView indexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceiling);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        indexView = findViewById(R.id.index_view);
        try {
            manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            adapter = new TermsAdapter(this, termsList);
            adapter.setListener(onItemClickListener);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new HoverItemDecoration(this, bindItemTextCallback));
            indexView.setOnTouchingLetterChangedListener(onTouchingLetterChangedListener);
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        TermsResultEntity result = new Gson().fromJson(json, TermsResultEntity.class);
        Set<String> stringSet = new HashSet<>();
        if (result != null && result.getTerms() != null) {
            for (TermsEntity terms : result.getTerms()) {
                terms.setFirstSpell(getFirstSpell(terms.getTerms()));
                stringSet.add(getFirstSpell(terms.getTerms()));
            }
            termsList.addAll(result.getTerms());
            adapter.notifyDataSetChanged();

            letterList.addAll(new ArrayList<>(stringSet));
            indexView.setWords(letterList.toArray(new String[letterList.size()]));
        }
    }

    private String getFirstSpell(String terms) {
        StringBuilder sb = new StringBuilder();
        char c = terms.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyinArray != null) {
            sb.append(pinyinArray[0].charAt(0));
        } else {
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }

    private TermsAdapter.OnItemClickListener onItemClickListener = new TermsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if (position < termsList.size() && termsList.get(position) != null && !TextUtils.isEmpty(termsList.get(position).getTerms())) {
                Toast.makeText(CeilingActivity.this, termsList.get(position).getTerms(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private HoverItemDecoration.BindItemTextCallback bindItemTextCallback = new HoverItemDecoration.BindItemTextCallback() {
        @Override
        public String getItemText(int position) {
            if (position < termsList.size() && termsList.get(position) != null && !TextUtils.isEmpty(termsList.get(position).getFirstSpell())) {
                return termsList.get(position).getFirstSpell();
            }
            return "";
        }
    };

    private IndexView.OnTouchingLetterChangedListener onTouchingLetterChangedListener = new IndexView.OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String letter) {
            int position = getPositionForSelection(letter);
            if (position >= 0) {
                manager.scrollToPositionWithOffset(position, 0);
                manager.setStackFromEnd(false);
            }
        }
    };

    private int getPositionForSelection(String letter) {
        for (int i = 0; i < termsList.size(); i++) {
            if (termsList.get(i) != null && TextUtils.equals(termsList.get(i).getFirstSpell(), letter)) {
                return i;
            }
        }
        return -1;
    }

    private String json = "{\n" +
            "    \"allTermsCount\": 0,\n" +
            "    \"masteryCount\": 0,\n" +
            "    \"outlineCode\": {\n" +
            "        \"category\": 2,\n" +
            "        \"id\": 2106,\n" +
            "        \"name\": \"六年级\",\n" +
            "        \"parentId\": 10,\n" +
            "        \"slug\": \"grade6\",\n" +
            "        \"wordsCount\": 426\n" +
            "    },\n" +
            "    \"terms\": [\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2407,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"哀思\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2386,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"安定\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2312,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"罢了\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2298,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"摆摊儿\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2147,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"斑点\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2221,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"半径\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2193,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"苞蕾\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2176,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"抱怨\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2124,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"暴露无遗\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2091,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"爆发\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2045,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"笨拙\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2299,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"彼此\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2374,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"避免\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2290,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"鞭炮\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2117,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"别出心裁\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2116,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"冰棍\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2354,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"拨弄\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2439,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"玻璃\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2396,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"剥削\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2344,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"不禁\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2418,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"不可思议\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2085,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"不屈\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2134,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"猜测\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2314,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"猜想\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2028,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"彩虹\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2304,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"彩绘\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2046,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"参差\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2383,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"残暴\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2355,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"草丛\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2194,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"草坪\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2019,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"草原\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2154,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"测试\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2346,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"察觉\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2168,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"搀扶\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2269,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"潮汛\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2393,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"彻底\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2174,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"沉思\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2362,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"沉郁\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2063,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"沉着\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2438,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"齿轮\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2352,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"赤裸裸\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2081,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"冲锋\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2077,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"仇恨\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2054,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"愁怨\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2441,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"丑恶\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2264,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"厨房\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2329,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"处境\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2251,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"纯熟\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2111,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"次序\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2266,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"刺猬\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2274,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"错综\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2121,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"大步流星\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2115,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"呆头呆脑\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2048,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"单薄\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2435,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"单调\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2361,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"耽搁\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2163,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"党员\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2276,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"荡漾\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2189,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"倒霉\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2108,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"灯笼\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2225,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"低质\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2150,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"抵御\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2159,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"跌跌撞撞\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2068,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"顶峰\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2204,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"斗篷\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2284,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"陡然\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2214,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"逗引\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2246,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"断断续续\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2120,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"跺脚\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2130,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"发达\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2385,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"法庭\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2357,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"翻箱倒柜\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2335,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"防御\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2384,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"匪徒\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2165,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"废话\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2071,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"沸腾\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2305,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"分外\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2076,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"粉身碎骨\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2317,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"粉碎\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2060,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"奋战\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2175,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"风暴\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2142,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"封冻\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2043,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"伏案\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2281,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"浮动\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2035,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"干部\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2149,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"干燥\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2196,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"甘蔗\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2307,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"感觉\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2038,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"感人\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2244,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"钢琴\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2107,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"高潮\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2113,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"疙瘩\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2391,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"革命\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2260,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"供品\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2232,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"贡献\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2094,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"鼓舞\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2145,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"观测\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2390,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"过度\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2373,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"含糊\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2083,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"豪迈\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2308,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"何况\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2350,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"何曾\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2220,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"和蔼\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2300,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"贺年\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2132,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"恒星\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2172,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"轰鸣\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2430,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"洪亮\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2191,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"后脑勺\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2356,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"画报\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2370,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"荒凉\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2360,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"晃动\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2023,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"回味\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2088,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"汇聚\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2039,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"会心\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2388,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"会意\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2233,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"毁坏\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2273,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"昏沉\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2044,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"浑浊\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2192,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"活生生\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2428,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"机器\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2427,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"机遇\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2238,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"基地\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2118,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"技高一筹\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2325,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"寂寞\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2406,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"寄托\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2261,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"祭器\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2437,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"加速\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2258,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"家景\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2378,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"尖锐\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2084,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"坚强\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2433,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"坚硬\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2293,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"间断\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2155,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"检测\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2103,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"检阅\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2425,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"见微知著\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2410,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"建树\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2262,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"讲究\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2210,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"讲座\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2212,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"酱油\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2288,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"饺子\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2309,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"搅和\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2143,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"揭开\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2431,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"街心\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2229,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"节制\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2295,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"截然\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2392,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"解放\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2277,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"解散\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2320,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"解释\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2201,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"尽量\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2058,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"进犯\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2268,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"经历\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2157,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"惊慌\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2366,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"惊惶\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2022,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"惊叹\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2086,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"惊天动地\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2316,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"惊异\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2217,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"晶莹\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2255,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"景象\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2093,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"就位\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2074,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"居高临下\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2034,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"拘束\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2375,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"局势\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2125,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"沮丧\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2036,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"举杯\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2106,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"距离\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2230,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"开采\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2228,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"慷概\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2152,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"考察\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2311,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"可靠\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2343,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"空虚\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2442,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"恐怖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2326,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"恐惧\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2331,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"控制\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2231,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"枯竭\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2148,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"枯萎\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2381,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"苦刑\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2338,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"宽慰\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2227,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"矿产\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2224,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"矿物\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2340,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"困境\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2181,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"困难\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2306,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"腊八粥\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2234,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"滥用\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2321,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"浪漫\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2024,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"乐趣\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2133,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"类似\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2382,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"冷笑\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2033,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"礼貌\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2131,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"理论\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2330,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"理智\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2394,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"利益\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2237,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"例如\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2183,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"脸蛋\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2267,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"伶俐\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2426,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"灵感\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2285,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"凌乱\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2409,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"领域\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2323,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"流落\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2216,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"楼梯\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2301,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"骆驼\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2029,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"马蹄\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2371,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"埋头\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2363,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"漫长\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2432,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"盲人\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2247,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"茅屋\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2167,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"猛然\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2030,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"蒙古包\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2051,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"梦想\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2052,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"迷蒙\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2413,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"敏感\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2353,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"明媚\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2415,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"明显\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2380,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"魔鬼\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2401,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"目标\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2025,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"目的地\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2240,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"目睹\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2032,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"奶豆腐\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2322,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"奈何\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2213,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"闹钟\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2139,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"能源\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2358,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"念叨\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2126,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"念念有词\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2200,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"农作物\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2122,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"怒气冲冲\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2345,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"挪移\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2420,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"偶然\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2146,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"拍摄\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2348,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"徘徊\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2090,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"排山倒海\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2072,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"攀登\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2263,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"盼望\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2333,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"抛弃\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2156,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"咆哮\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2398,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"批评\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2243,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"皮鞋\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2114,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"疲倦\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2379,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"僻静\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2056,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"平淡\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2275,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"萍藻\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2239,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"破碎\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2242,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"谱写\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2197,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"瀑布\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2324,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"凄凉\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2098,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"旗帜\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2135,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"起源\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2302,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"恰好\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2180,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"敲门\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2367,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"亲吻\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2336,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"侵袭\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2253,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"琴键\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2248,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"琴声\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2129,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"轻手轻脚\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2377,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"轻易\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2337,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"倾覆\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2140,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"倾角\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2177,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"倾听\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2434,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"清脆\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2185,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"清新\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2252,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"清幽\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2205,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"情况\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2436,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"请求\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2065,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"全神贯注\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2342,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"确乎\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2296,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"燃放\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2319,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"染缸\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2031,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"热乎乎\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2286,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"热情\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2057,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"日寇\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2278,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"融和\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2021,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"柔美\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2198,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"软绵绵\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2026,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"洒脱\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2158,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"嗓子\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2162,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"沙哑\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2254,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"霎时\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2075,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"山涧\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2236,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"设想\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2066,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"射击\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2137,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"摄氏度\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2164,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"呻吟\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2339,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"深重\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2144,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"神秘\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2235,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"生态\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2250,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"失明\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2186,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"湿淋淋\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2365,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"时光\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2067,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"始终\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2136,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"适当\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2064,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"手榴弹\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2280,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"瘦削\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2328,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"书籍\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2160,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"书记\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2055,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"顺心\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2282,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"瞬间\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2411,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"司空见惯\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2404,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"死得其所\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2097,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"肃静\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2195,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"苔藓\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2347,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"叹息\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2178,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"探望\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2256,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"陶醉\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2138,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"提供\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2414,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"提取\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2079,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"眺望\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2359,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"停顿\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2292,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"通宵\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2123,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"颓然\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2279,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"退缩\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2166,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"吞没\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2207,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"瓦蓝\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2102,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"完毕\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2369,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"挽回\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2294,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"万不得已\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2289,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"万象更新\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2127,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"忘乎所以\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2112,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"威风凛凛\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2349,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"微风\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2040,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"微笑\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2440,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"唯恐\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2050,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"文思\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2421,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"文献\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2419,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"吻合\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2416,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"无独有偶\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2417,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"无聊\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2226,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"无私\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2400,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"五湖四海\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2109,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"五颜六色\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2403,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"牺牲\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2444,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"蟋蟀\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2080,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"喜悦\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2153,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"系列\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2423,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"系统\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2062,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"险要\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2020,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"线条\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2265,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"项圈\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2119,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"橡皮\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2297,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"小贩\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2087,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"协商\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2173,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"心惊肉跳\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2128,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"心满意足\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2332,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"心平气和\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2399,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"兴旺\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2171,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"汹涌澎湃\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2096,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"雄伟\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2364,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"休止\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2037,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"羞涩\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2203,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"袖筒\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2206,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"袖子\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2092,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"宣布\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2099,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"宣读\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2095,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"宣告\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2069,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"悬崖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2101,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"选举\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2397,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"压迫\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2271,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"烟草\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2272,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"烟雾\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2341,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"焉知非福\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2376,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"严峻\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2047,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"眼帘\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2199,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"谚语\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2218,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"摇篮\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2313,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"要不然\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2303,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"一律\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2257,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"一望无际\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2105,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"一致\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2027,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"衣裳\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2073,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"依托\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2368,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"依偎\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2412,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"疑问\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2078,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"屹立\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2395,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"意义\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2151,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"因素\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2182,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"阴冷\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2053,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"印象\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2161,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"拥戴\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2190,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"忧虑\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2327,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"忧伤\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2245,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"幽静\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2042,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"幽雅\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2211,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"油锅\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2059,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"游击\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2351,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"游丝\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2110,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"游行\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2223,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"有限\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2372,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"幼稚\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2170,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"渔夫\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2187,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"渔网\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2202,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"雨衣\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2100,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"语调\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2208,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"预报\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2089,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"预定\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2270,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"预告\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2188,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"糟糕\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2402,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"责任\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2291,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"眨眼\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2041,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"宅院\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2070,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"斩钉截铁\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2387,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"占据\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2169,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"丈夫\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2179,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"照顾\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2049,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"照耀\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2209,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"遮盖\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2408,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"真理\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2424,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"整理\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2422,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"证据\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2443,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"证实\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2259,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"郑重\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2389,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"执行\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2405,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"制度\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2104,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"制服\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2429,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"钟楼\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2315,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"肿胀\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2334,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"重见天日\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2141,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"昼夜\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2283,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"骤然\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2249,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"烛光\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2061,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"转移\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2219,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"壮观\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2082,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"壮烈\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2310,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"资格\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2222,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"资源\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2241,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"子孙\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2287,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"自傲\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2184,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"自作自受\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2318,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"总之\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"gradeId\": 2106,\n" +
            "            \"id\": 2215,\n" +
            "            \"score\": 0.0,\n" +
            "            \"terms\": \"嘴唇\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}