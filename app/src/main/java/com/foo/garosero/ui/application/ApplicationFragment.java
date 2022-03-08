package com.foo.garosero.ui.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.foo.garosero.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplicationFragment extends Fragment {
    View root;

    EditText et_name, et_phone, et_loc, et_motive, et_tree;
    Button bt_search, bt_application;
    RadioButton cb_indi, cb_team, cb_school;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_application, container, false);

        // init view
        et_name = root.findViewById(R.id.application_et_name);
        et_phone = root.findViewById(R.id.application_et_phone);
        et_loc = root.findViewById(R.id.application_et_loc);
        et_motive = root.findViewById(R.id.application_et_motive);
        et_tree = root.findViewById(R.id.application_et_road);
        bt_search = root.findViewById(R.id.application_btn_search);
        bt_application = root.findViewById(R.id.application_btn_application);
        cb_indi = root.findViewById(R.id.application_cb_indi);
        cb_team = root.findViewById(R.id.application_cb_team);
        cb_school = root.findViewById(R.id.application_cb_school);

        // onclick event
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
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
                String tree_id = et_tree.getText().toString(); // 나무고유번호- ID
                String location = et_loc.getText().toString(); // 구명

                String unit = "";
                if (cb_indi.isChecked()) unit = "개인";
                else if (cb_team.isChecked()) unit = "단체";
                else if (cb_school.isChecked()) unit = "학교";

                if (name.equals("") || phone.equals("") || motive.equals("")
                        || unit.equals("") || tree_id.equals("") || location.equals("")) {
                    Toast.makeText(getContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
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
                childUpdate.put("location", location); // 위치 (xx구)
                //childUpdate.put("tree_type", apiData.getWDPT_NM()); // 가로명 (나무 종류)

                final ProgressDialog mDialog = new ProgressDialog(getContext());
                mDialog.setMessage("신청중입니다");
                mDialog.show();
                ref.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(getContext(),"신청이 완료되었습니다",  Toast.LENGTH_SHORT).show();
                            // todo : go to home frag
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getContext(),"신청 실패",  Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return root;
    }
}