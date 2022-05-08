package com.ronnelrazo.physical_counting.Database;

public class TABLE_HEADER_DETAILS {

    public static final String TABLE_CHECKLIST_HEADER_DETAILS = "TABLE_HEADER_DETAILS";

    public String POSITION = "POSITION"; //PK

    public String MAINCODE = "MAINCODE"; //PK
    public String MAINDESC = "MAINDESC";
    public String MAINSEQ = "MAINSEQ";

    public String SUBCODE = "SUBCODE"; //PK
    public String SUBDESC = "SUBDESC";
    public String SUBSEQ = "SUBSEQ";

    public String DETAILSCODE = "DETAILSCODE"; //PK
    public String DETAILSDESC = "DETAILSDESC";
    public String DETAILSSEQ = "DETAILSSEQ";


    public String BUCODE = "BUCODE";
    public String BU_TYPE_CODE = "BU_TYPE_CODE";


    public String ORG_CODE = "ORG_CODE"; //fk
    public String FARM_CODE = "FARM_CODE"; //fk;
    public String TYPE = "TYPE";
    public String CHECK_ITEM = "CHECK_ITEM";
    public String REMARK = "REMARK";

}
