package com.foo.garosero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foo.garosero.data.AsteriskPasswordTransformationMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private String uid;
    private Button  btn_login, btn_signup;
    private TextView tv_login_failed;
    private EditText et_email, et_password;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth =  FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        //버튼 등록하기
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_login_failed = findViewById(R.id.tv_login_failed);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        et_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }
    @Override
    public void onClick(View view) throws IllegalArgumentException{
        if(view==btn_signup){
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        }
        else if(view==btn_login){
            String email = et_email.getText().toString().trim();
            String pwd = et_password.getText().toString().trim();
            try{
                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                                Log.e("LoginActivity",uid);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("current_user", uid);
                                startActivity(intent);
                                finish();

                                ValueEventListener eventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Log.e("LoginActivity","data does exist");
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("current_user", uid);
                                            startActivity(intent);
                                            finish();

                                        }else Log.e("LoginActivity","data doesnt exist");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("LoginActivity", String.valueOf(error.toException()));
                                    }
                                };
                                ref.child("Users").child(uid).addListenerForSingleValueEvent(eventListener);
                            } else {
                                tv_login_failed.setVisibility(View.VISIBLE);
                            }
                        });
            } catch(Exception e){
                tv_login_failed.setVisibility(View.VISIBLE);
            }
        }

    }
}