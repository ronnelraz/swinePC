package com.ronnelrazo.physical_counting.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Physical_Count";
    private static final int DATABASE_VERSION = 1;

    //tables
    private TABLE_HEADER CHECKLIST_HEADER = new TABLE_HEADER();
    private TABLE_HEADER_DETAILS CHECKLIST_DETAILS = new TABLE_HEADER_DETAILS();



    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String header = "CREATE TABLE " + CHECKLIST_HEADER.TABLE_CHECKLIST_HEADER +
                        " (" + CHECKLIST_HEADER.ORG_CODE + " TEXT, " +
                               CHECKLIST_HEADER.FARM_CODE + " TEXT, " +
                               CHECKLIST_HEADER.ORG_NAME + " TEXT, " +
                               CHECKLIST_HEADER.FARM_NAME + " TEXT, " +
                               CHECKLIST_HEADER.DOC_DATE + " TEXT, " +
                               CHECKLIST_HEADER.AUDIT_DATE + " TEXT, " +
                               CHECKLIST_HEADER.TYPE + " TEXT," +
                               CHECKLIST_HEADER.BU_TYPE + " TEXT," +
                               CHECKLIST_HEADER.BU_CODE + " TEXT," +
                               CHECKLIST_HEADER.BU_NAME + " TEXT," +
                               CHECKLIST_HEADER.BU_TYPE_NAME + " TEXT," +
                              "PRIMARY KEY(ORG_CODE, FARM_CODE)  ); ";
        db.execSQL(header);


        String header_details = "CREATE TABLE " + CHECKLIST_DETAILS.TABLE_CHECKLIST_HEADER_DETAILS +
                " (" + CHECKLIST_DETAILS.POSITION + " INTEGER, " +
                CHECKLIST_DETAILS.ORG_CODE + " TEXT, " +
                CHECKLIST_DETAILS.FARM_CODE + " TEXT, " +
                CHECKLIST_DETAILS.TYPE + " TEXT, " +
                CHECKLIST_DETAILS.CHECK_ITEM + " TEXT, " +
                CHECKLIST_DETAILS.REMARK + " TEXT," +
                "PRIMARY KEY(POSITION,ORG_CODE,FARM_CODE)); ";
        db.execSQL(header_details);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + CHECKLIST_HEADER.TABLE_CHECKLIST_HEADER );
        onCreate(db);


        db.execSQL("DROP TABLE IF EXISTS " + CHECKLIST_DETAILS.TABLE_CHECKLIST_HEADER_DETAILS );
        onCreate(db);

    }



}
