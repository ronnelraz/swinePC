package com.ronnelrazo.physical_counting;

import android.content.Context;
import android.os.AsyncTask;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Pdfviewer_single extends AsyncTask<String, Void, InputStream> {

    PDFView pdfView;
    Context context;
    LottieAnimationView lottieAnimationView;
    String strURL;



    public Pdfviewer_single(PDFView pdfView, Context context,LottieAnimationView lottieAnimationView, String strURL) {
        this.pdfView = pdfView;
        this.context = context;
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
                lottieAnimationView.setAnimation(R.raw.npdf);
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();
            }
        }).swipeHorizontal(true).onPageError(new OnPageErrorListener() {
            @Override
            public void onPageError(int page, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                lottieAnimationView.setAnimation(R.raw.npdf);
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();
            }
        }).enableAnnotationRendering(true).onRender(new OnRenderListener() {
            @Override
            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                lottieAnimationView.setVisibility(View.GONE);
            }
        }).load();
    }



}
