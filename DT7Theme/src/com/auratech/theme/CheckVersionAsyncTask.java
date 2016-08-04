package com.auratech.theme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.CoreConnectionPNames;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class CheckVersionAsyncTask extends AsyncTask<String, Void, Boolean> {

    Context context;
    String currentVersion; // Google Play �ϵİ汾

    String oldVersion;     // Ŀǰ�� app �汾


    public CheckVersionAsyncTask(Context context, String version) {
        this.context = context;
        this.oldVersion = version;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        String url = params[0];   //Ո�� excecute �r������ google play �� url λ�ã����� market ร�Ҫ http

        HttpPost post = new HttpPost(url);

        AndroidHttpClient client = AndroidHttpClient.newInstance("android");
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3*1000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3*1000);
        

        BufferedReader reader = null; 
        try {
            HttpResponse reponse = client.execute(post);

            reader = new BufferedReader(new InputStreamReader(reponse.getEntity().getContent()));
            String line;
            String content = "";
            Pattern p = Pattern.compile("\"softwareVersion\"\\W*([\\d\\.]+)");
            while( ( line = reader.readLine() ) != null ){
                Matcher matcher = p.matcher(line);
                if( matcher.find() ){
                    Log.v("ids", "ver.:" + matcher.group(1));

                    currentVersion = matcher.group(1);
                }
                content += line;
            }
            /*
            * �z��汾���ڴ�ʹ�õ����҂��Զ��İ汾���Q <app version name>���K���ǰ汾̖ <app version code>
            * Ո�؄eע���@һ�c
            */

            if (currentVersion != null && currentVersion.compareTo(oldVersion) > 0)
                result = true;
            Log.v("ids", content+",thread:"+Thread.currentThread().getName());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.v("ids", "close reader");
            try {
                if( reader != null ) {
                    reader.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            // �z�鵽���µİ汾������һ������
            new AlertDialog.Builder(context, R.style.MyDialog).setTitle(R.string.update_update).setMessage(R.string.update_info).setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            }).setNegativeButton(R.string.update_cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.update_update, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateApp();
                }
            }).create().show();
        }
    }

    private void updateApp() {
        String packageName = context.getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id="+packageName));
        context.startActivity(intent);
    }
}