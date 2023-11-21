package com.example.final_assignment_201931036_hyunjun;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class LoginForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginform);

        ArrayList<String> idInput = new ArrayList<>();

        try {
            FileInputStream inFs = openFileInput("idValue.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inFs));
            ArrayList<String> idInputValue = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                idInputValue.add(line);
            }
            idInput.addAll(idInputValue);
            inFs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AutoCompleteTextView idTextView=(AutoCompleteTextView) findViewById(R.id.idTextView);
        EditText pwTextView=(EditText) findViewById(R.id.pwTextView);

        Button submit=(Button) findViewById(R.id.loginSubmit);
        Button guest=(Button) findViewById(R.id.loginGuest);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,idInput);
        idTextView.setAdapter(adapter);

        submit.setOnClickListener(view -> {
            String idLogin = idTextView.getText().toString().trim();
            String pwLogin = pwTextView.getText().toString().trim();

            String idValue = idLogin + "\n";
            boolean isDuplicate = false;

            try (FileInputStream inFs = openFileInput("idValue.txt");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inFs))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equals(idValue.trim())) {
                        isDuplicate = true;
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!isDuplicate) {
                try (FileOutputStream outFs = openFileOutput("idValue.txt", Context.MODE_APPEND)) {
                    outFs.write(idValue.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(idLogin.length()<6||pwLogin.length()<8){
                Toast.makeText(getApplicationContext(), "아이디나 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            }else {
                Intent check = new Intent(LoginForm.this, LoginCheck.class);
                check.putExtra("id", idLogin);
                check.putExtra("pw", pwLogin);
                startActivity(check);
            }
        });

        guest.setOnClickListener(view -> {
            Intent main = new Intent(LoginForm.this, MainActivity.class);
            main.putExtra("property","Guest");
            startActivity(main);
        });
    }
}