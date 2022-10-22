package com.example.applicationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Hellopage extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hellopage);
        textView = findViewById(R.id.text);

        findViewById(R.id.logOut).setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        FirebaseFirestore.getInstance().collection("Client")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().getData() != null){
                                textView.setText("Welcome Client " + task.getResult().get("name").toString());
                            } else {
                                FirebaseFirestore.getInstance().collection("Cooker")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().getData() != null) {
                                                        textView.setText("Welcome Cooker " + task.getResult().get("name").toString());
                                                    } else {
                                                        FirebaseFirestore.getInstance().collection("admin")
                                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        if(documentSnapshot.getData() != null){
                                                                            textView.setText("welcome " + documentSnapshot.get("email"));
                                                                        }
                                                                    }
                                                                });
//
                                                    }
                                                }
                                            }
                                        });
                            }
//                            done
                            System.out.println("task success " + task.getResult().getData());
                        }
                    }
                });


    }
}