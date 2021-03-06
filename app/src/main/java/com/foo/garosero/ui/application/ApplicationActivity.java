package com.foo.garosero.ui.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.foo.garosero.R;
import com.foo.garosero.data.TreeApiData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplicationActivity extends AppCompatActivity {
    EditText et_name, et_phone, et_loc, et_motive, et_tree;
    Button bt_search, bt_application;
    RadioButton cb_indi, cb_team, cb_school;

    public static TreeApiData apiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // init view
        et_name = findViewById(R.id.application_et_name);
        et_phone = findViewById(R.id.application_et_phone);
        et_loc = findViewById(R.id.application_et_loc);
        et_motive = findViewById(R.id.application_et_motive);
        et_tree = findViewById(R.id.application_et_road);
        bt_search = findViewById(R.id.application_btn_search);
        bt_application = findViewById(R.id.application_btn_application);
        cb_indi = findViewById(R.id.application_cb_indi);
        cb_team = findViewById(R.id.application_cb_team);
        cb_school = findViewById(R.id.application_cb_school);

        // onclick event
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        // submit the application (BB)
        bt_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                String motive = et_motive.getText().toString();
                String tree_id = et_tree.getText().toString(); // ??????????????????- ID
                String location = et_loc.getText().toString(); // ??????

                String unit = "";
                if (cb_indi.isChecked()) unit = "??????";
                else if (cb_team.isChecked()) unit = "??????";
                else if (cb_school.isChecked()) unit = "??????";

                if (name.equals("") || phone.equals("") || motive.equals("")
                        || unit.equals("") || tree_id.equals("") || location.equals("")) {
                    Toast.makeText(ApplicationActivity.this, "????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                String uid = FirebaseAuth.getInstance().getUid();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String today = format.format(date);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Candidates/"+tree_id);
                Map<String, Object> childUpdate = new HashMap<>();
                childUpdate.put("uid", uid);
                childUpdate.put("name", name);
                childUpdate.put("phone", phone);
                childUpdate.put("motive", motive);
                childUpdate.put("unit", unit);
                childUpdate.put("date", today);
                childUpdate.put("tree_id", tree_id);
                childUpdate.put("location", location); // ?????? (xx???)
                childUpdate.put("tree_type", apiData.getWDPT_NM()); // ????????? (?????? ??????)

                final ProgressDialog mDialog = new ProgressDialog(ApplicationActivity.this);
                mDialog.setMessage("??????????????????");
                mDialog.show();
                ref.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(ApplicationActivity.this,"????????? ?????????????????????",  Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(ApplicationActivity.this,"?????? ??????",  Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // ?????? ??????
        findViewById(R.id.application_ImageButton_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            if (apiData!=null){
                et_tree.setText(apiData.getOBJECTID());
                et_loc.setText(apiData.getGU_NM());
            }
        } catch (Exception e){

        }
    }
}