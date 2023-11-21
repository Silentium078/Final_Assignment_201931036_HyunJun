package com.example.final_assignment_201931036_hyunjun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDBHelper dbHelper = new myDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Intent received = getIntent();
        String idLogin = received.getStringExtra("id");
        String pwLogin = received.getStringExtra("pw");

        String[] projection = {"USERPW","USERSALT"};
        String selection = "USERID = ?";
        String[] selectionArgs = {idLogin};

        Cursor cursor = db.query("USERINFO", projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            // 비밀번호(PW) 가져오기
            @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex("USERPW"));
            @SuppressLint("Range") String userSalt = cursor.getString(cursor.getColumnIndex("USERSALT"));
            SecurePw securePw=new SecurePw(pwLogin,userSalt);

            if (securePw.securePassword.equals(userPassword)) {
                Intent main = new Intent(LoginCheck.this, MainActivity.class);
                    main.putExtra("property", idLogin);
                startActivity(main);
            } else {
                Toast.makeText(getApplicationContext(), "아이디나 비밀번호 오류입니다.", Toast.LENGTH_SHORT).show();
                Intent rollback = new Intent(LoginCheck.this, LoginForm.class);
                startActivity(rollback);
            }

        }else{
            Toast.makeText(getApplicationContext(), "아이디나 비밀번호 오류입니다.", Toast.LENGTH_SHORT).show();
            Intent rollback = new Intent(LoginCheck.this, LoginForm.class);
            rollback.putExtra("addId", idLogin);
            startActivity(rollback);
        }
        cursor.close();
    }
}