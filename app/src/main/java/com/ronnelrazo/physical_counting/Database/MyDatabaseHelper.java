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
    private TABLE_BREEDER_DETAILS BREEDER_DETAILS = new TABLE_BREEDER_DETAILS();
    private TABLE_FEED_DETAILS FEED_DETAILS = new TABLE_FEED_DETAILS();
    private TABLE_MED_DETAILS MED_DETAILS = new TABLE_MED_DETAILS();



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

                CHECKLIST_DETAILS.MAINCODE + " TEXT," +
                CHECKLIST_DETAILS.MAINDESC + " TEXT," +
                CHECKLIST_DETAILS.MAINSEQ + " TEXT," +

                CHECKLIST_DETAILS.SUBCODE + " TEXT," +
                CHECKLIST_DETAILS.SUBDESC + " TEXT," +
                CHECKLIST_DETAILS.SUBSEQ + " TEXT," +

                CHECKLIST_DETAILS.DETAILSCODE + " TEXT," +
                CHECKLIST_DETAILS.DETAILSDESC + " TEXT," +
                CHECKLIST_DETAILS.DETAILSSEQ + " TEXT," +

                CHECKLIST_DETAILS.BUCODE + " TEXT," +
                CHECKLIST_DETAILS.BU_TYPE_CODE + " TEXT," +

                "PRIMARY KEY(POSITION,ORG_CODE,FARM_CODE)); ";
        db.execSQL(header_details);



        String breeder_details = "CREATE TABLE " + BREEDER_DETAILS.TABLE_BREEDER_DETAILS +
                " (" + BREEDER_DETAILS.POSITION + " INTEGER, " +
                BREEDER_DETAILS.ORG_CODE + " TEXT, " +
                BREEDER_DETAILS.BUCODE + " TEXT, " +
                BREEDER_DETAILS.BU_TYPE_CODE + " TEXT, " +
                BREEDER_DETAILS.LOCATION + " TEXT," +

                BREEDER_DETAILS.FARM_CODE + " TEXT," +
                BREEDER_DETAILS.FARM_ORG + " TEXT," +
                BREEDER_DETAILS.FARM_NAME + " TEXT," +

                BREEDER_DETAILS.FEMALE_STOCK + " TEXT," +
                BREEDER_DETAILS.MALE_STOCK + " TEXT," +
                BREEDER_DETAILS.TOTAL_STOCK + " TEXT," +

                BREEDER_DETAILS.COUNTING_STOCK + " TEXT," +
                BREEDER_DETAILS.REMARK + " TEXT," +
                "PRIMARY KEY(POSITION,ORG_CODE,FARM_CODE)); ";
        db.execSQL(breeder_details);



        String feed_details = "CREATE TABLE " + FEED_DETAILS.TABLE_FEED_DETAILS +
                " (" + FEED_DETAILS.POSITION + " INTEGER, " +
                FEED_DETAILS.ORG_CODE + " TEXT, " +
                FEED_DETAILS.BUCODE + " TEXT, " +
                FEED_DETAILS.BU_TYPE_CODE + " TEXT, " +
                FEED_DETAILS.FARM_CODE + " TEXT," +
                FEED_DETAILS.FARM_ORG + " TEXT," +
                FEED_DETAILS.FARM_NAME + " TEXT," +
                FEED_DETAILS.FEED_CODE + " TEXT," +
                FEED_DETAILS.FEED_NAME + " TEXT," +
                FEED_DETAILS.SYS_FEED_STOCK_QTY + " TEXT," +
                FEED_DETAILS.SYS_FEED_STOCK_WGH + " TEXT," +
                FEED_DETAILS.STOCK_UNIT + " TEXT," +
                FEED_DETAILS.COUNTING_STOCK + " TEXT," +
                FEED_DETAILS.REMARK + " TEXT," +
                "PRIMARY KEY(POSITION,ORG_CODE,FARM_CODE)); ";
        db.execSQL(feed_details);


        String med_details = "CREATE TABLE " + MED_DETAILS.TABLE_MED_DETAILS +
                " (" + MED_DETAILS.POSITION + " INTEGER, " +
                MED_DETAILS.ORG_CODE + " TEXT, " +
                MED_DETAILS.BUCODE + " TEXT, " +
                MED_DETAILS.BU_TYPE_CODE + " TEXT, " +
                MED_DETAILS.FARM_CODE + " TEXT," +
                MED_DETAILS.FARM_ORG + " TEXT," +
                MED_DETAILS.FARM_NAME + " TEXT," +
                MED_DETAILS.MED_CODE + " TEXT," +
                MED_DETAILS.MED_NAME + " TEXT," +
                MED_DETAILS.SYS_MED_STOCK_QTY + " TEXT," +
                MED_DETAILS.SYS_MED_STOCK_WGH + " TEXT," +
                MED_DETAILS.STOCK_UNIT + " TEXT," +
                MED_DETAILS.COUNTING_STOCK + " TEXT," +
                MED_DETAILS.REMARK + " TEXT," +
                "PRIMARY KEY(POSITION,ORG_CODE,FARM_CODE)); ";
        db.execSQL(med_details);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CHECKLIST_HEADER.TABLE_CHECKLIST_HEADER );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + CHECKLIST_DETAILS.TABLE_CHECKLIST_HEADER_DETAILS );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + BREEDER_DETAILS.TABLE_BREEDER_DETAILS );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + FEED_DETAILS.TABLE_FEED_DETAILS );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + MED_DETAILS.TABLE_MED_DETAILS );
        onCreate(db);

    }



}
