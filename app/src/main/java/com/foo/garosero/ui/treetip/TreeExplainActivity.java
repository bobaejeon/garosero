package com.foo.garosero.ui.treetip;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.foo.garosero.R;
import com.foo.garosero.data.TreeTipData;
import com.foo.garosero.myUtil.TreeTipHelper;

public class TreeExplainActivity extends AppCompatActivity {

    private TextView banner_title;
    private TextView group1_title1, group1_title2, group1_content;
    private TextView group2_title1, group2_title2, group2_content;
    private ImageView character;
    private LinearLayout banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_explain);

        // init view
        banner_title = findViewById(R.id.explain_TextView_banner_title);
        group1_title1 = findViewById(R.id.explain_TextView_group1_title1);
        group1_title2 = findViewById(R.id.explain_TextView_group1_title2);
        group1_content = findViewById(R.id.explain_TextView_group1_content);
        group2_title1 = findViewById(R.id.explain_TextView_group2_title1);
        group2_title2 = findViewById(R.id.explain_TextView_group2_title2);
        group2_content = findViewById(R.id.explain_TextView_group2_content);
        character = findViewById(R.id.explain_ImageView_character);
        banner = findViewById(R.id.explain_LinearLayout_banner);

        // 페이지 채우기
        String mKey = getIntent().getStringExtra("mKey");
        TreeTipHelper treeTipHelper = new TreeTipHelper();
        TreeTipData treeData = treeTipHelper.TreeHashMap.get(mKey);
        banner_title.setText(treeData.banner_title);
        group1_title1.setText(treeData.group1_title1);
        group1_title2.setText(treeData.group1_title2);
        group1_content.setText(treeData.group1_content);
        group2_title1.setText(treeData.group2_title1);
        group2_title2.setText(treeData.group2_title2);
        group2_content.setText(treeData.group2_content);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            character.setBackground(ContextCompat.getDrawable(this, treeData.character));
        }else {
            character.setBackgroundDrawable(ContextCompat.getDrawable(this, treeData.character));
        }
        banner.setBackgroundColor(this.getResources().getColor(treeData.bannerColor));

        // 뒤로가기 버튼
        findViewById(R.id.explain_ImageButton_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeExplainActivity.super.onBackPressed();
            }
        });
    }
}