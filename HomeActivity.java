package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView display1;
    private TextView display2;
    private Button increment1;
    private Button decrement1;
    private Button increment2;
    private Button decrement2;
    private Button continueButton;

    private int value1 = 1;
    private int value2 = 1;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        display1 = findViewById(R.id.display1);
        display2 = findViewById(R.id.display2);
        increment1 = findViewById(R.id.increment1);
        decrement1 = findViewById(R.id.decrement1);
        increment2 = findViewById(R.id.increment2);
        decrement2 = findViewById(R.id.decrement2);
        continueButton = findViewById(R.id.continueButtonId);

        increment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value1++;
                display1.setText(String.valueOf(value1));
            }
        });

        decrement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value1 > 1) {
                    value1--;
                    display1.setText(String.valueOf(value1));
                }
            }
        });

        increment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value2++;
                display2.setText(String.valueOf(value2));
            }
        });

        decrement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value2 > 1) {
                    value2--;
                    display2.setText(String.valueOf(value2));
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date and time
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = dateFormat.format(currentDate);

                int dayTokens = value1;
                int nightTokens = value2;

                Map<String, Object> order = new HashMap<>();
                order.put("dayTokens", dayTokens);
                order.put("nightTokens", nightTokens);
                order.put("orderDate", orderDate);

                db.collection("Users")
                        .document(currentUser.getUid())
                        .collection("Orders")
                        .add(order)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Intent intent = new Intent(HomeActivity.this, Generate.class);
                                intent.putExtra("value1", value1);
                                intent.putExtra("value2", value2);
                                startActivity(intent);
                                Toast.makeText(HomeActivity.this, "Added order data", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this, "Error adding order data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
