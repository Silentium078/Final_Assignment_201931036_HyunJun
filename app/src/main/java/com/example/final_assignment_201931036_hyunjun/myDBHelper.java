package com.example.final_assignment_201931036_hyunjun;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context) {
        super(context, "userDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USERINFO (USERID TEXT PRIMARY KEY, USERPW TEXT, USERSALT TEXT, STARTDATE DATE, EXP INTEGER DEFAULT 0, LEVEL INTEGER DEFAULT 1, GOLD INTEGER DEFAULT 0,BANK TEXT DEFAULT '0', ITEM INTEGER DEFAULT 0,GOLDUP INTEGER DEFAULT 1,EXPUP INTEGER DEFAULT 1);");
        SecurePw securePw = new SecurePw("12345678");
        String adminSalt = securePw.getSalt();
        String adminSecure = securePw.getSecurePw();
        /*관리자 계정 삽입*/
        ContentValues adminValues = new ContentValues();
        adminValues.put("USERID", "Administrator");
        adminValues.put("USERPW", adminSecure);
        adminValues.put("USERSALT",adminSalt);
        adminValues.put("STARTDATE", "2023-11-03");
        adminValues.put("EXP", 10000);
        adminValues.put("LEVEL", 99);
        adminValues.put("GOLD", 99999999);
        adminValues.put("BANK","99999999999999");
        adminValues.put("ITEM", 0);
        adminValues.put("GOLDUP", 1000);
        adminValues.put("EXPUP", 1000);

        db.insert("USERINFO", null, adminValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERINFO;");
        onCreate(db);
    }
}