package com.example.applicationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    com.example.applicationproject.databinding.ActivityRegisterBinding context;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = com.example.applicationproject.databinding.ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(context.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        context.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context.password.getText().length() < 8){
                    Toast.makeText(Register.this, "Password must be above 8 laters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(context.username.getText().length() < 4){
                    Toast.makeText(Register.this, "Username must be above 4 latters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(context.email.getText().length() < 5){
                    Toast.makeText(Register.this, "please Enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!context.password.getText().toString().equals(context.cPassword.getText().toString())){
                    Toast.makeText(Register.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!context.checkBox.isChecked()){
                    Toast.makeText(Register.this, "Password accept terms and condition to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(context.radio.getCheckedRadioButtonId() != R.id.client && (context.radio.getCheckedRadioButtonId() != R.id.cooker)){
                    Toast.makeText(Register.this, "Please select", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(context.email.getText().toString(), context.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                String database = "Cooker";
                                int type = 0;
//                                0 = cooker  1 = client
                                if(context.radio.getCheckedRadioButtonId() == R.id.client) {
                                    database = "Client";
                                    type = 1;
                                }
                                if(task.isSuccessful())
                                {

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", context.username.getText().toString());
                                    data.put("email", context.email.getText().toString());
                                    data.put("id", firebaseAuth.getCurrentUser().getUid());
                                    data.put("type",type);

//                                     user account created successfully
                                    firebaseFirestore.collection(database)
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .set(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Register.this, "Register completed", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Register.this, Hellopage.class));
                                                    } else {
                                                        Toast.makeText(Register.this, "Failed to add data " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
//bro not enough space

                                } else {
                                    Toast.makeText(Register.this, "Register failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}