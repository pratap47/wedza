package com.anmol.wedza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class IntroduceYourselfActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> arrayAdapter;
    String relation;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = FirebaseFirestore.getInstance().document("weddings/mrxwedsmsy/users/user1");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_yourself);
        spinner = (Spinner)findViewById(R.id.relation);
        arrayAdapter =  ArrayAdapter.createFromResource(this,R.array.relationtype,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                relation = (String) adapterView.getItemAtPosition(i);
                Map<String,Object> map = new HashMap<>();
                map.put("relation",relation);
                //db.document("weddings/mrxwedsmsy/users/user1").set(map);
                documentReference.set(map);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Toast.makeText(this,relation,Toast.LENGTH_SHORT).show();
    }
}