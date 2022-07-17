package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_breeder;
import com.ronnelrazo.physical_counting.model.model_med;
import com.ronnelrazo.physical_counting.tab_from;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BreederTab_adapter extends RelativeLayout {
    public final String TAG = "TableMainLayout.java";

    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    public List<String> variancePos = new ArrayList<>();

    // set the header titles
    String headers[] = {
            "Farm",
            "  Female  ",
            "   Male   ",
            "   Stock  ",
            "      Count      ",
            " Variance ",
            "      Unpost     ",
            "Active Var.",
            "              Remark             "
    };

    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;

    Context context;
    String str_orgCode;
    String str_farmOrg;
    public List<model_breeder> dana = new ArrayList<>();

    int headerCellsWidth[] = new int[headers.length];

    public BreederTab_adapter(Context context,String str_orgCode,String str_farmOrg) {
        super(context);
        this.context = context;
        this.str_orgCode = str_orgCode;
        this.str_farmOrg = str_farmOrg;
        this.putangina();
        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
    }




    private void putangina(){


        API_.getClient().Breeder(str_orgCode,str_farmOrg).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            Log.d("putangina", object.getString("femaleQty") + " " + i);
                            model_breeder sampleObject = new model_breeder(
                                    object.getString("locationCode"),
                                    object.getString("locationName"),
                                    object.getString("orgCode"),
                                    object.getString("orgName"),
                                    object.getString("farmOrg"),
                                    object.getString("farmName"),
                                    object.getString("maleQty"),
                                    object.getString("femaleQty"),
                                    object.getString("stockBalance"),
                                    tab_from.business_type,
                                    tab_from.bu_code,
                                    str_farmOrg
                            );
                            dana.add(sampleObject);
                        }

                       initComponents();
                       setComponentsId();
                       setScrollViewAndHorizontalScrollViewTag();

//                        need to assemble component A, since it is just a table
                       horizontalScrollViewB.addView(tableB);

                       scrollViewC.addView(tableC);

                       scrollViewD.addView(horizontalScrollViewD);
                       horizontalScrollViewD.addView(tableD);

//                       d the components to be part of the main layout
                       addComponentToMainLayout();
                       setBackgroundColor(Color.WHITE);


//                       d some table rows
                       addTableRowToTableA();
                        addTableRowToTableB();

                       resizeHeaderHeight();

                       getTableRowHeaderCellWidth();

                       generateTableC_AndTable_B();

                       resizeBodyTableRowHeight();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("putangina",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d("putangina",t.getMessage());
                }
            }
        });

    }



    // initalized components
    private void initComponents(){

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);

        this.tableA.setBackgroundColor(Color.GREEN);
//        this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);

    }

    // set essential component IDs
    @SuppressLint("ResourceType")
    private void setComponentsId(){
        this.tableA.setId(1);
        this.horizontalScrollViewB.setId(2);
        this.scrollViewC.setId(3);
        this.scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag(){

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout(){

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }



    private void addTableRowToTableA(){
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB(){
        this.tableB.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow(){

        TableRow componentATableRow = new TableRow(this.context);
        componentATableRow.setBackgroundColor(Color.parseColor("#1abc9c"));
        TextView textView = this.headerTextView(this.headers[0]);
        textView.setBackgroundColor(Color.parseColor("#1abc9c"));
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setHeight(100);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(10,0,0,0);
        textView.setTypeface(null, Typeface.BOLD);
        if(headers[0].equals("Farm")){
                textView.setWidth(400);
        }
        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow(){

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);

        for(int x=0; x<(headerFieldCount-1); x++){

            TextView textView = this.headerTextView(this.headers[x+1]);
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }






    // generate table row of table C and table D
    private void generateTableC_AndTable_B(){

        // just seeing some header cell width
        for(int x=0; x<this.headerCellsWidth.length; x++){
            Log.v("TableMainLayout.java", this.headerCellsWidth[x]+"");
        }

        for(int i = 0; i < dana.size(); i++){

            final model_breeder getData = dana.get(i);

            TableRow tableRowForTableC = this.tableRowForTableC(getData,dana.size(),i);
            tableRowForTableC.setGravity(Gravity.LEFT);

            TableRow taleRowForTableD = this.taleRowForTableD(getData,i);
//            tableRowForTableC.setBackgroundResource(R.drawable.edit_text_dot_line);
//            taleRowForTableD.setBackgroundResource(R.drawable.edit_text_dot_line);
//            taleRowForTableD.setBackgroundColor(Color.LTGRAY);

            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);



            boolean save_breeder_details = Globalfunction.getInstance(context)
                    .ADD_BREEDER_DETAILS(i,
                            getData.getOrg_Code(),
                            getData.getBu_code(),
                            getData.getBu_type(),
                            getData.getLocation_Code(),
                            getData.getFarm_code(),
                            getData.getFarm_Org(),
                            getData.farm_Name,
                            getData.getFemale_Stock().replace(",",""),
                            getData.getMale_Stock().replace(",",""),
                            getData.getStock_Balance().replace(",",""),
                            "0",
                            "",
                            "0",
                            "0",
                            "0"
                    );
            if(save_breeder_details){
                Log.d("cebu","save breeder details" + i);
            }
            else{
                Log.d("cebu","existing breeder details" + i);
            }
        }

//        for(model_breeder sampleObject : dana){
//
//            TableRow tableRowForTableC = this.tableRowForTableC(sampleObject,dana.size());
//            tableRowForTableC.setGravity(Gravity.LEFT);
//
//            TableRow taleRowForTableD = this.taleRowForTableD(sampleObject);
//            tableRowForTableC.setBackgroundColor(Color.LTGRAY);
//            taleRowForTableD.setBackgroundColor(Color.LTGRAY);
//
//            this.tableC.addView(tableRowForTableC);
//            this.tableD.addView(taleRowForTableD);
//
//        }
    }

    // a TableRow for table C
    TableRow tableRowForTableC(model_breeder sampleObject,int i,int position){
        int backgroundColor = position%2==0 ? R.color.even : R.color.odd;

        TableRow.LayoutParams params = new TableRow.LayoutParams( this.headerCellsWidth[0],LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.bodyTextView(sampleObject.getFarm_Org() + " \n" + sampleObject.getFarm_Name());
//        textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        textView.setBackgroundColor(ContextCompat.getColor(context,backgroundColor));
        textView.setGravity(Gravity.LEFT);

        textView.setPadding(2,30,0,30);
        tableRowForTableC.addView(textView,params);

        return tableRowForTableC;
    }


    TableRow taleRowForTableD(model_breeder sampleObject,int position){
        final model_breeder getData = dana.get(position);
        int backgroundColor = position%2==0 ? R.color.even : R.color.odd;
        int backgroundColorEdit = position%2==0 ? R.drawable.edit_text_dot_form_even : R.drawable.edit_text_dot_form_odd;
        TableRow taleRowForTableD = new TableRow(this.context);


        EditText editTextCount = this.bodyEditText();
        TextView textview_variance = this.variance("0");
        EditText unpostEditText = this.unpostEditText();
        TextView activeVar = this.activeVar("0");
        EditText Remarks = this.RemarksEditText();
        int loopCount = ((TableRow)tableB.getChildAt(0)).getChildCount();

        String info[] = {
                sampleObject.getFemale_Stock(),
                sampleObject.getMale_Stock(),
                sampleObject.getStock_Balance(),
               "",
               "",
               "",
               "",
               ""
        };





        for(int x=0 ; x<loopCount; x++){



            TableRow.LayoutParams params = new TableRow.LayoutParams( headerCellsWidth[x+1],LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);




            if(x <= 2){
                TextView textViewB = this.bodyTextView(info[x]);
                textViewB.setBackgroundColor(ContextCompat.getColor(context,backgroundColor));
                taleRowForTableD.addView(textViewB,params);
            }
            else if(x == 3){
                editTextCount.setBackgroundResource(backgroundColorEdit);
                taleRowForTableD.addView(editTextCount,params);
            }

            else if(x == 4){
                textview_variance.setBackgroundColor(ContextCompat.getColor(context,backgroundColor));
                editTextCount.setWidth(200);
                taleRowForTableD.addView(textview_variance,params);

                editTextCount.addTextChangedListener(new TextWatcher() {
                     @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }


                    @Override
                    public void afterTextChanged(Editable s) {
                        String dash = "[ -]+";
                        if(s.toString().matches(dash)){
                            Log.d("swine","error dash");
                            textview_variance.setText("0");
                        }
                        else if(s.toString().isEmpty()){
                            Log.d("swine","error dash");
                            textview_variance.setText("0");
                        }
                        else if(s.toString().equals("")){
                            Log.d("swine","error dash");
                            textview_variance.setText("0");
                        }
                        else if(s.length() == 0){
                            Log.d("swine","error dash");
                            textview_variance.setText("0");
                        }
                        else{
                            if (s.length() > 0) {
                                try{
                                    int stocks = Integer.parseInt(info[2].replace(",",""));
                                    int counts = Integer.parseInt(s.toString());
                                    int varience_total = stocks - counts;
                                    textview_variance.setText(String.valueOf(varience_total));

                                    String count = s.toString().isEmpty() ? "0" : s.toString();
                                    String getRemarks = Remarks.getText().toString();
                                    String getunpost = unpostEditText.getText().toString();
                                    String getactiveVaR = activeVar.getText().toString();

                                    boolean updateBreederList = Globalfunction.getInstance(context)
                                            .updatebreederlist(position,getData.getOrg_Code(),getData.getFarm_code(),count,getRemarks,String.valueOf(varience_total),getunpost,getactiveVaR);
                                    if(updateBreederList){
                                        Log.d("swine","update breeder details" + position);
                                    }
                                    else{
                                        Log.d("swine","update breeder details" + position);
                                    }


                                } catch(NumberFormatException ex){
                                    Log.d("swine","error number");
                                }

//                                last_text_edit = System.currentTimeMillis();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
//
//                                        }
//                                    }
//
//
//                                }, delay);
                            }
                        }


                    }
                });

            }

            else if(x == 5){
                unpostEditText.setBackgroundResource(backgroundColorEdit);
                taleRowForTableD.addView(unpostEditText,params);
                unpostEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }


                    @Override
                    public void afterTextChanged(Editable s) {
                        String dash = "[ -]+";
                        if(s.toString().matches(dash)){
                            Log.d("swine","error dash");
                            activeVar.setText("0");
                        }
                        else if(s.toString().isEmpty()){
                            Log.d("swine","error dash");
                            activeVar.setText("0");
                        }
                        else if(s.toString().equals("")){
                            Log.d("swine","error dash");
                            textview_variance.setText("0");
                        }
                        else if(s.length() == 0){
                            Log.d("swine","error dash");
                            activeVar.setText("0");
                        }
                        else{
                            if (s.length() > 0) {

                                last_text_edit = System.currentTimeMillis();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                                            try{
                                                int varience = Integer.parseInt(textview_variance.getText().toString());
                                                int counts = Integer.parseInt(s.toString());
                                                int active_varience_total = varience - counts;
                                                activeVar.setText(String.valueOf(active_varience_total));



                                                String count = editTextCount.getText().toString();
                                                String getRemarks = Remarks.getText().toString();
                                                String getunpost = s.toString().isEmpty() ? "0" : s.toString();
                                                String getvariance = textview_variance.getText().toString();
                                                String getactiveVaR = activeVar.getText().toString();

                                                boolean updateBreederList = Globalfunction.getInstance(context)
                                                        .updatebreederlist(position,getData.getOrg_Code(),getData.getFarm_code(),count,getRemarks,getvariance,getunpost,getactiveVaR);
                                                if(updateBreederList){
                                                    Log.d("swine","update breeder details" + position);
                                                }
                                                else{
                                                    Log.d("swine","update breeder details" + position);
                                                }

                                            } catch(NumberFormatException ex){
                                                Log.d("swine","error number");
                                            }
                                        }
                                    }


                                }, delay);
                            }
                        }


                    }
                });

            }
            else if(x == 6){
                activeVar.setBackgroundColor(ContextCompat.getColor(context,backgroundColor));
                taleRowForTableD.addView(activeVar,params);
            }

            else if(x == 7){
                Remarks.setBackgroundResource(backgroundColorEdit);
                taleRowForTableD.addView(Remarks,params);
                Remarks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            try{
                                String count = editTextCount.getText().toString();
                                String getRemarks = s.toString();
                                String getunpost = unpostEditText.getText().toString().isEmpty() ? "0" : unpostEditText.getText().toString();
                                String getvariance = textview_variance.getText().toString();
                                String getactiveVaR = activeVar.getText().toString();

                                boolean updateBreederList = Globalfunction.getInstance(context)
                                        .updatebreederlist(position,getData.getOrg_Code(),getData.getFarm_code(),count,getRemarks,getvariance,getunpost,getactiveVaR);
                                if(updateBreederList){
                                    Log.d("swine","update breeder details" + position);
                                }
                                else{
                                    Log.d("swine","update breeder details" + position);
                                }

                            } catch(NumberFormatException ex){
                                Log.d("swine","error number");
                            }
//                            last_text_edit = System.currentTimeMillis();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
//
//                                    }
//                                }
//
//
//                            }, delay);
                        }
                    }
                });
            }
        }

        return taleRowForTableD;

    }

    TextView variance(String label){
        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        bodyTextView.setSingleLine(true);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);
        return bodyTextView;
    }

    TextView activeVar(String label){
        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);
        return bodyTextView;
    }

    // table cell standard TextView
    TextView bodyTextView(String label){
        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);
        return bodyTextView;
    }


    EditText unpostEditText(){
        EditText bodyTextView = new EditText(this.context);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setBackgroundColor(Color.parseColor("#ffffff"));
        bodyTextView.setTextSize(13);
        bodyTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        bodyTextView.setBackgroundResource(R.drawable.edit_text_dot_form);
        return bodyTextView;
    }


    EditText RemarksEditText(){
        EditText bodyTextView = new EditText(this.context);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setTextSize(13);
        bodyTextView.setBackgroundColor(Color.parseColor("#ffffff"));
        bodyTextView.setBackgroundResource(R.drawable.edit_text_dot_form);
        return bodyTextView;
    }

    EditText bodyEditText(){
        EditText bodyTextView = new EditText(this.context);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setBackgroundColor(Color.parseColor("#ffffff"));
        bodyTextView.setTextSize(13);
        bodyTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        bodyTextView.setBackgroundResource(R.drawable.edit_text_dot_form);
        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label){

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setBackgroundColor(Color.parseColor("#1abc9c"));
        headerTextView.setTextColor(Color.parseColor("#FFFFFF"));
        headerTextView.setTypeface(null, Typeface.BOLD);
        headerTextView.setPadding(15, 5, 15, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth(){

        int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();;

        for(int x=0; x<(tableAChildCount+tableBChildCount); x++){

            if(x==0){
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x));
            }else{
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x-1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight(){

        int tableC_ChildCount = this.tableC.getChildCount();

        for(int x=0; x<tableC_ChildCount; x++){

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow)  this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if(tableRow.getChildCount()==1){

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return ;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView{

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("horizontal scroll view b")){
                horizontalScrollViewD.scrollTo(l, 0);
            }else{
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // scroll view custom class
    class MyScrollView extends ScrollView{

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("scroll view c")){
                scrollViewD.scrollTo(0, t);
            }else{
                scrollViewC.scrollTo(0,t);
            }
        }
    }

}