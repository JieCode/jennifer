package com.jennifer.jennifer.ui.text;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.daquexian.flexiblerichtextview.FlexibleRichTextView;
import com.jennifer.jennifer.R;

import org.scilab.forge.jlatexmath.core.AjLatexMath;

import io.github.kbiakov.codeview.classifier.CodeProcessor;

public class TextActivity extends AppCompatActivity {

    private String htmlText = "<div><p style=\"text-align: justify;\"><span style=\"color: #000000;\">一枚实心纪念币的质量为</span><span style=\"color: #000000;\"><latex>\\(16g\\)</latex></span><span style=\"color: #000000;\">，体积为</span><span style=\"color: #000000;\"><latex>\\(2c{m^3}\\)</latex></span><span style=\"color: #000000;\">，纪念币的密度是<textentryinteraction responseidentifier=\"RESPONSE_1-1\"></textentryinteraction></span><span style=\"color: #000000;\"></span><span style=\"color: #000000;\"><latex>\\(g/c{m^3}\\)</latex></span><span style=\"color: #000000;\">。可见，这枚纪念币<textentryinteraction responseidentifier=\"RESPONSE_1-2\"></textentryinteraction></span><span style=\"color: #000000;\"></span><span style=\"color: #000000;\">（选填&ldquo;是&rdquo;或&ldquo;不是&rdquo;</span><span style=\"color: #000000;\"><latex>\\()\\)</latex></span><span style=\"color: #000000;\">纯金制成。若宇航员将这枚纪念币带到太空，其质量<textentryinteraction responseidentifier=\"RESPONSE_1-3\"></textentryinteraction></span><span style=\"color: #000000;\"></span><span style=\"color: #000000;\">（选填&ldquo;变大&rdquo;&ldquo;变小&rdquo;或&ldquo;不变&rdquo;</span><span style=\"color: #000000;\"><latex>\\()\\)</latex></span><span style=\"color: #000000;\">。</span><span style=\"color: #000000;\"><latex>\\(\\left( {{\\rho _金} = 19.3 \\times {{10}^3}kg/{m^3}} \\right)\\)</latex></span></p></div>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        //使用之前必须初始化 JLaTeXMath，在 Application 或其他地方添加
        AjLatexMath.init(this); // init library: load fonts, create paint, etc.
        //如果希望自动识别代码段中的语言以实现高亮，在 Application 或其他地方添加
        // train classifier on app start
        CodeProcessor.init(this);
        FlexibleRichTextView flexibleRichTextView = findViewById(R.id.id_rich_tv);
        flexibleRichTextView.setText(htmlText);
        Log.i("TextActivity", "onCreate: ");
    }
}