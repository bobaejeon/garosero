package com.foo.garosero.ui.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.foo.garosero.R;

public class ApplicationActivity extends AppCompatActivity {
    EditText et_name, et_phone, et_loc, et_motive;
    Button bt_search, bt_application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        et_name = findViewById(R.id.application_et_name);
        et_phone = findViewById(R.id.application_et_phone);
        et_loc = findViewById(R.id.application_et_loc);
        et_motive = findViewById(R.id.application_et_motive);

        bt_search = findViewById(R.id.application_btn_search);
        bt_application = findViewById(R.id.application_btn_application);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        // 뒤로 가기
        findViewById(R.id.application_ImageButton_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}