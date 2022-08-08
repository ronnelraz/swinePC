package com.ronnelrazo.physical_counting;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Pdfviewer extends AsyncTask<String, Void, InputStream> {

    PDFView pdfView;
    Context context;
    MaterialButton materialButton;
    LottieAnimationView lottieAnimationView;
    String strURL;



    public Pdfviewer(PDFView pdfView, Context context,MaterialButton materialButton,LottieAnimationView lottieAnimationView,String strURL) {
        this.pdfView = pdfView;
        this.context = context;
        this.materialButton = materialButton;
        this.lottieAnimationView = lottieAnimationView;
        this.strURL = strURL;
    }

    @Override
    protected InputStream doInBackground(String... strings) {
        // we are using inputstream
        // for getting out PDF.
        InputStream inputStream = null;
        try {
            URL url = new URL(strings[0]);
            // below is the step where we are
            // creating our connection.
            HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == 200) {
                Log.d("PDFRAZO","TRUE" + "   " + url);
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            }
            else{
                Log.d("PDFRAZO","FALSE");
            }

        } catch (IOException e) {
            // this is the method
            // to handle errors.
            e.printStackTrace();
            Log.d("PDFRAZO",e.getMessage());
            return null;
        }
        return inputStream;
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {

        lottieAnimationView.setAnimation(R.raw.loading);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();

        pdfView.fromStream(inputStream).onError(new OnErrorListener() {
            @Override
            public void onError(Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                materialButton.setEnabled(false);

                lottieAnimationView.setAnimation(R.raw.npdf);
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();
            }
        }).swipeHorizontal(true).onPageError(new OnPageErrorListener() {
            @Override
            public void onPageError(int page, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                materialButton.setEnabled(false);
                lottieAnimationView.setAnimation(R.raw.npdf);
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();
            }
        }).enableAnnotationRendering(true).onRender(new OnRenderListener() {
            @Override
            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                materialButton.setEnabled(true);
                lottieAnimationView.setVisibility(View.GONE);
                materialButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Log.d("Honeybabe69", Comman.getAppPath(context));
//
//
//                        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
//                        try {
//                            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(context,inputStream);
//                            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
//                        }catch (Exception ex){
//                            Log.e("swine",""+ex.getMessage());
//                            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
//
//                        }
                    }
                });
            }
        }).load();
    }



}
