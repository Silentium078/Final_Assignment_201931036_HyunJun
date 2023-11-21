package com.example.final_assignment_201931036_hyunjun;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("deprecation")

public class MainActivity extends TabActivity {
    /*기타 경험치, 레벨 변수 설정 */
    int expNow =0;
    int expMax =1000;
    int userLevel=1;
    int userGoldSave=0;
    int goldLevel=1;
    int EXPLevel=1;
    int goldUp=10;
    int EXPUp=10;
    int userItem=0;
    int goldUpMoney=10;
    int EXPUpMoney=10;

    String strGold;
    String userBar;
    String goldscale;
    String expscale;
    String startDate;
    String Bank;

    @Override
	    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*데이터 들고오는 코드*/
        Intent received=getIntent();
        String getPro=received.getStringExtra("property");

        myDBHelper dbHelper = new myDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        /*탭 호스트 영역*/
        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecSong = tabHost.newTabSpec("Home").setIndicator("메인");
        tabSpecSong.setContent(R.id.mainSection);
        tabHost.addTab(tabSpecSong);

        TabHost.TabSpec tabSpecArtist = tabHost.newTabSpec("Roulette").setIndicator("가챠");
        tabSpecArtist.setContent(R.id.rouletteList);
        tabHost.addTab(tabSpecArtist);

        TabHost.TabSpec tabSpecAlbum = tabHost.newTabSpec("Info").setIndicator("정보");
        tabSpecAlbum.setContent(R.id.information);
        tabHost.addTab(tabSpecAlbum);

        tabHost.setCurrentTab(0);
        /*탭 호스트 끝*/
        /*메인 화면 정리*/


        TextView userGold= findViewById(R.id.userGold);
        TextView userName=findViewById(R.id.userName);
        TextView BankValue=findViewById(R.id.BankValue);
        ImageButton mainImage= findViewById(R.id.mainPage);
        ImageView characterImage=findViewById(R.id.charactorImage);
        ProgressBar progressBar= findViewById(R.id.progressBar);
        TextView exper= findViewById(R.id.experienceBar);
        Button save= findViewById(R.id.save);
        TextView goldScale=findViewById(R.id.goldUPScale);
        TextView expScale=findViewById(R.id.expUPScale);
        Button goldUPButton=findViewById(R.id.goldupButton);
        Button expUPButton=findViewById(R.id.expupButton);
        Button withdraw=findViewById(R.id.withdraw);
        Button deposit=findViewById(R.id.deposit);

        /*DATABASE에서 값 읽어오기*/
        String[] projection = {"EXP", "LEVEL", "GOLD","BANK", "ITEM","GOLDUP","EXPUP","STARTDATE"};
        String selection = "USERID = ?";
        String[] selectionArgs = {getPro};
        Cursor cursor = db.query("USERINFO", projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int exp = cursor.getInt(cursor.getColumnIndex("EXP"));
            expNow=exp;
            @SuppressLint("Range") int level = cursor.getInt(cursor.getColumnIndex("LEVEL"));
            userLevel=level;
            for(int i=1;i<level;i++)
                expMax*=1.1;
            @SuppressLint("Range") int gold = cursor.getInt(cursor.getColumnIndex("GOLD"));
            userGoldSave=gold;
            @SuppressLint("Range") int item = cursor.getInt(cursor.getColumnIndex("ITEM"));

            @SuppressLint("Range") int golduplevel = cursor.getInt(cursor.getColumnIndex("GOLDUP"));
            goldLevel=golduplevel;
            goldUp=goldLevel*10+(goldLevel/100)*10;
            for(int i=1;i<goldLevel;i++) {
                if (i < 100)
                    goldUpMoney *= 1.1;
                else if (i < 200 && i >= 100)
                    goldUpMoney *= 1.05;
                else if (i >= 200 && i < 500)
                    goldUpMoney *= 1.02;
                else
                    goldUpMoney *= 1.01;
            }
            if(goldUpMoney>=2147483647)
                goldUPButton.setVisibility(View.INVISIBLE);
            @SuppressLint("Range") int expuplevel = cursor.getInt(cursor.getColumnIndex("EXPUP"));
            EXPLevel=expuplevel;
            EXPUp=expuplevel*10+(expuplevel/100)*10;
            for(int i=1;i<EXPLevel;i++)
                if(i<100)
                    EXPUpMoney*=1.1;
                else if (i<200&&i>=100)
                    EXPUpMoney*=1.05;
                else if(i>=200&&i<500)
                    EXPUpMoney*=1.02;
                else
                    EXPUpMoney*=1.01;
            if(EXPUpMoney>=2147483647)
                expUPButton.setVisibility(View.INVISIBLE);
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("STARTDATE"));
            startDate=date;
            @SuppressLint("Range") String bankSQL = cursor.getString(cursor.getColumnIndex("BANK"));
            Bank=bankSQL;
        }

        cursor.close();

        /*데이터 정리*/
        strGold=Integer.toString(userGoldSave);
        userGold.setText(strGold);
        userBar= "LV."+userLevel + "    "+expNow+"/"+expMax;
        exper.setText(userBar);
        progressBar.setProgress(expNow);
        progressBar.setMax(expMax);
        goldscale = "현재 레벨: " + goldLevel+ "\n다음 레벨: " + (goldLevel + 1);
        goldScale.setText(goldscale);
        goldUPButton.setText(String.valueOf(goldUpMoney));
        expscale = "현재 레벨: " + EXPLevel + "\n다음 레벨: " + (EXPLevel + 1);
        expScale.setText(expscale);
        expUPButton.setText(String.valueOf(EXPUpMoney));
        BankValue.setText(Bank);
        /*정리 끝*/

        String textLogin="로그인 계정 : " + getPro;
        userName.setText(textLogin);

        mainImage.setOnClickListener(view -> {
            expNow+=EXPUp;
            while(expNow>expMax){
                userLevel++;
                expNow=expNow-expMax;
                expMax*=1.1;
                progressBar.setMax(expMax);
            }

            userGoldSave+=userLevel*goldUp+userLevel*(goldLevel/100)*goldUp;
            if(userGoldSave>1000000000){
                userGoldSave=999999999;
                Toast.makeText(getApplicationContext(),"더 이상 모을 수 없습니다. 은행에 저장해주세요.",Toast.LENGTH_SHORT).show();
            }
            strGold=Integer.toString(userGoldSave);
            userGold.setText(strGold);
            userBar= "LV."+userLevel + "    "+expNow+"/"+expMax;
            exper.setText(userBar);
            progressBar.setProgress(expNow);
        });

        save.setOnClickListener(view -> {
            if(getPro.equals("Guest"))
                Toast.makeText(getApplicationContext(),"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();

            ContentValues userSave = new ContentValues();
            userSave.put("EXP", expNow);
            userSave.put("LEVEL", userLevel);
            userSave.put("GOLD", userGoldSave);
            userSave.put("BANK",Bank);
            userSave.put("ITEM", userItem);
            userSave.put("GOLDUP", goldLevel);
            userSave.put("EXPUP", EXPLevel);

            String whereClause = "USERID = ?";
            String[] whereArgs = {getPro};

            dbWrite.update("USERINFO", userSave, whereClause, whereArgs);
        });


//      강화창
        goldUPButton.setOnClickListener(view -> {
            if(userGoldSave<goldUpMoney){
                Toast.makeText(getApplicationContext(),"업그레이드 실패",Toast.LENGTH_SHORT).show();
            }else {
                goldLevel++;
                userGoldSave -= goldUpMoney;
                strGold = Integer.toString(userGoldSave);
                userGold.setText(strGold);
                goldUp+=10;
                if (goldLevel < 100)
                    goldUpMoney *= 1.1;
                else if (goldLevel < 200 && goldLevel >= 100)
                    goldUpMoney *= 1.05;
                else if (goldLevel >= 200 && goldLevel < 500)
                    goldUpMoney *= 1.02;
                else
                    goldUpMoney *= 1.01;
                if(goldUpMoney>=2147483647)
                    goldUPButton.setVisibility(View.INVISIBLE);
                goldscale = "현재 레벨: " + goldLevel + "\n다음 레벨: " + (goldLevel + 1);
                goldScale.setText(goldscale);
                goldUPButton.setText(String.valueOf(goldUpMoney));
            }
        });

        expUPButton.setOnClickListener(view -> {
            if (userGoldSave < EXPUpMoney) {
                Toast.makeText(getApplicationContext(), "업그레이드 실패", Toast.LENGTH_SHORT).show();
            } else {
                EXPLevel++;
                userGoldSave -= EXPUpMoney;
                strGold = Integer.toString(userGoldSave);
                userGold.setText(strGold);
                EXPUp+=10;
                if(EXPLevel<100)
                    EXPUpMoney*=1.1;
                else if (EXPLevel<200&&EXPLevel>=100)
                    EXPUpMoney*=1.05;
                else if(EXPLevel>=200&&EXPLevel<500)
                    EXPUpMoney*=1.02;
                else
                    EXPUpMoney*=1.01;
                if(EXPUpMoney>=2147483647)
                    expUPButton.setVisibility(View.INVISIBLE);
                expscale = "현재 레벨: " + EXPLevel + "\n다음 레벨: " + (EXPLevel + 1);
                expScale.setText(expscale);
                expUPButton.setText(String.valueOf(EXPUpMoney));
            }
        });



        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = View.inflate(MainActivity.this, R.layout.bankwithdraw, null);

                TextView Banknow=findViewById(R.id.Bankhave);
                Banknow.setText(Bank);

                AlertDialog.Builder dlg=new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("은행 출금");
                dlg.setView(dialogView);

                dlg.setPositiveButton("확인", (dialog, which) -> {
                    final EditText Value= dialogView.findViewById(R.id.withdrawValue);
                    String wdvalue=Value.getText().toString();
                    long withdrawbank=Long.parseLong(wdvalue);
                    long banksave=Long.parseLong(Bank);
                    if(withdrawbank>banksave)
                        Toast.makeText(getApplicationContext(),"입력하신 값이 보유 잔고보다 많습니다.",Toast.LENGTH_SHORT).show();
                    else if((withdrawbank+ userGoldSave)>1000000000){
                        Toast.makeText(getApplicationContext(),"유저가 보유할 수 있는 총량보다 많습니다.",Toast.LENGTH_SHORT).show();
                    }else if(withdrawbank<0){
                        Toast.makeText(getApplicationContext(),"0보다 작은 값은 입력할 수 없습니다.",Toast.LENGTH_SHORT).show();
                    }
//                    오류 발생 Bank 오류확인
                    else{
                        userGoldSave+=(int)withdrawbank;
                        banksave-=withdrawbank;

                        strGold=Integer.toString(userGoldSave);
                        userGold.setText(strGold);

                        Bank= Long.toString(banksave);
                        Banknow.setText(Bank);
                    }
                });

                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /*가챠 탭*/
        Button goldRandom=findViewById(R.id.goldRandom);
        Button roulette=findViewById(R.id.roulette);
        Button racingCar=findViewById(R.id.RacingCar);

        goldRandom.setOnClickListener(view -> {

        });















        /*정보 탭*/
        TextView InfoLogin=findViewById(R.id.InfoLogin);
        TextView InfoGold=findViewById(R.id.InfoGold);
        TextView InfoBank=findViewById(R.id.InfoBank);
        TextView InfoDate=findViewById(R.id.InfoDate);
        Button InfoNew=findViewById(R.id.NewLogin);
        Button saveDB=findViewById(R.id.saveDB);
        Button sql= findViewById(R.id.sql);

        InfoLogin.setText(textLogin);
        String textGold="보유 골드 : "+strGold;
        InfoGold.setText(textGold);
        String textDate="첫 접속일 : "+startDate;
        InfoDate.setText(textDate);
        String textBank="은행 보유 잔고 : " + Bank;
        InfoBank.setText(textBank);
//        회원가입
        InfoNew.setOnClickListener(v -> {
            final View dialogView = View.inflate(MainActivity.this, R.layout.membership, null);

            Date currentDate = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(currentDate);

            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
            dlg.setTitle("회원가입");
            dlg.setView(dialogView);
            dlg.setPositiveButton("확인", (dialog, which) -> {
                final EditText dlgInfoId= dialogView.findViewById(R.id.JoinId);
                final EditText dlgInfoPw=dialogView.findViewById(R.id.JoinPw);
                String infoId=dlgInfoId.getText().toString().trim();
                String infoPw=dlgInfoPw.getText().toString().trim();
                AlertDialog.Builder alertDialog;

                if (infoId.length()<6||infoPw.length()<8)
                    Toast.makeText(getApplicationContext(), "아이디나 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    SecurePw securePw=new SecurePw(infoPw);
                    String getSecurePw=securePw.getSecurePw();
                    String getSalt= securePw.getSalt();
                    ContentValues userSave = new ContentValues();
                    userSave.put("USERID", infoId);
                    userSave.put("USERPW", getSecurePw);
                    userSave.put("USERSALT",getSalt);
                    userSave.put("STARTDATE", formattedDate);

                    long return_values = dbWrite.insert("USERINFO", null, userSave);

                    alertDialog = new AlertDialog.Builder(MainActivity.this);
                    if (return_values > 0) {
                        alertDialog.setTitle("회원가입 성공");
                        alertDialog.setMessage("재시작 해 주세요.");
                    } else {
                        alertDialog.setTitle("회원가입 실패");
                        alertDialog.setMessage("아이디 중복입니다. 다시 회원가입 해 주세요.");
                    }
                    alertDialog.setPositiveButton("확인", null);
                    alertDialog.show();

                    dbWrite.close();
                }
            });

            dlg.setNegativeButton("취소", null);

            dlg.show();
        });

        saveDB.setOnClickListener(view -> {
            String DBGold,DBBank;

            String[] projection1 = {"GOLD","BANK"};
            String selection1 = "USERID = ?";
            String[] selectionArg1 = {getPro};
            Cursor cursor1 = db.query("USERINFO", projection1, selection1, selectionArg1, null, null, null);

            if (cursor1.moveToFirst()) {
                @SuppressLint("Range") int gold = cursor1.getInt(cursor1.getColumnIndex("GOLD"));
                DBGold="보유 골드 : "+gold;
                InfoGold.setText(DBGold);
                @SuppressLint("Range") String bank = cursor1.getString(cursor1.getColumnIndex("BANK"));
                DBBank="은행 보유 잔고 : "+ bank;
                InfoBank.setText(DBBank);
            }
            cursor1.close();
        });

        sql.setOnClickListener(view -> {
            File fileToDelete = new File(getFilesDir(), "idValue.txt");

            dbHelper.onUpgrade(db, 1, 2);
            db.close();

            if (fileToDelete.exists()) {
                boolean isDeleted = fileToDelete.delete();
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(),"파일 삭제 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"파일 삭제 실패", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"파일이 존재하지 않음", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

