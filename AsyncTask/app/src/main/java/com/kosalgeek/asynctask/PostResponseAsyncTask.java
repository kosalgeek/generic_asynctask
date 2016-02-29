package com.kosalgeek.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Oum Saokosal, the author of KosalGeek on 9/6/15.
 * Get More Free Source Codes at https://github.com/kosalgeek
 * Subscribe my Youtube channel https://youtube.com/c/Kosalgeekvideos
 * Follow Me on Twitter https://twitter.com/kosalgeek
 */
public class PostResponseAsyncTask extends AsyncTask<String, Void, String> {

    private String LOG = "PostResponseAsyncTask";

    private ProgressDialog progressDialog;

    private AsyncResponse asyncResponse;
    private Context context;
    private HashMap<String, String> postData = new HashMap<String, String>();
    private String loadingMessage = "Loading...";
    private boolean showLoadingMessage = true;


    private ExceptionHandler exceptionHandler;
    private EachExceptionsHandler eachExceptionsHandler;

    private Exception exception = new Exception();

    //Valid Constructor
    public PostResponseAsyncTask(Context context,
                                 AsyncResponse asyncResponse){
        this.asyncResponse = asyncResponse;
        this.context = context;
    }

    //Constructor
    public PostResponseAsyncTask(Context context,
                                 boolean showLoadingMessage,
                                 AsyncResponse asyncResponse
    ){
        this.asyncResponse = asyncResponse;
        this.context = context;
        this.showLoadingMessage = showLoadingMessage;
    }

    public PostResponseAsyncTask(Context context,
                                 HashMap<String, String> postData,
                                 AsyncResponse asyncResponse){
        this.context = context;
        this.postData = postData;
        this.asyncResponse = asyncResponse;
    }


    public PostResponseAsyncTask(Context context,
                                 HashMap<String, String> postData,
                                 boolean showLoadingMessage,
                                 AsyncResponse asyncResponse){
        this.context = context;
        this.postData = postData;
        this.asyncResponse = asyncResponse;
        this.showLoadingMessage = showLoadingMessage;
    }

    public PostResponseAsyncTask(Context context,
                                 String loadingMessage,
                                 AsyncResponse asyncResponse){
        this.context = context;
        this.loadingMessage = loadingMessage;
        this.asyncResponse = asyncResponse;
    }

    public PostResponseAsyncTask(Context context,
                                 HashMap<String, String> postData,
                                 String loadingMessage,
                                 AsyncResponse asyncResponse){
        this.context = context;
        this.postData = postData;
        this.loadingMessage = loadingMessage;
        this.asyncResponse = asyncResponse;
    }
    //End Constructor

    //Setter and Getter
    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public HashMap<String, String> getPostData() {
        return postData;
    }

    public void setPostData(HashMap<String, String> postData) {
        this.postData = postData;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setEachExceptionsHandler(EachExceptionsHandler eachExceptionsHandler) {
        this.eachExceptionsHandler = eachExceptionsHandler;
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    public Context getContext() {
        return context;
    }

    public AsyncResponse getAsyncResponse() {
        return asyncResponse;
    }


    //End Setter & Getter



    @Override
    protected void onPreExecute() {
        if(showLoadingMessage == true){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(loadingMessage);
            progressDialog.show();
        }

        super.onPreExecute();
    }//onPreExecute

    @Override
    protected String doInBackground(String... urls){

        String result = "";
        for(int i = 0; i <= 0; i++){
            result = invokePost(urls[i], postData);
        }
        return result;
    }//doInBackground

    private String invokePost(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

                Log.d("PostResponseAsyncTask", responseCode + "");
            }
        } catch (MalformedURLException e) {
            Log.d("PostResponseAsyncTask", "MalformedURLException Error: " + e.toString());
            exception = e;
        }
        catch (ProtocolException e) {
            Log.d("PostResponseAsyncTask", "ProtocolException Error: " + e.toString());
            exception = e;
        }
        catch (UnsupportedEncodingException e) {
            Log.d("PostResponseAsyncTask", "UnsupportedEncodingException Error: " + e.toString());
            exception = e;
        }
        catch (IOException e) {
            Log.d("PostResponseAsyncTask", "IOException Error: " + e.toString());
            exception = e;
        }

        return response;

    }//performPostCall

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }//getPostDataString

    @Override
    protected void onPostExecute(String result) {
        if(showLoadingMessage == true){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        result = result.trim();

        if(asyncResponse != null){
            asyncResponse.processFinish(result);
        }

        if(exception != null) {
            if(exceptionHandler != null){
                exceptionHandler.handleException(exception);
            }
            if(eachExceptionsHandler != null){
                Log.d(LOG, "" + exception.getClass().getSimpleName());
                if(exception instanceof MalformedURLException){
                    eachExceptionsHandler.handleMalformedURLException((MalformedURLException) exception);
                }
                else if(exception instanceof ProtocolException){
                    eachExceptionsHandler.handleProtocolException((ProtocolException) exception);
                }
                else if(exception instanceof UnsupportedEncodingException){
                    eachExceptionsHandler.handleUnsupportedEncodingException((UnsupportedEncodingException) exception);
                }
                else if(exception instanceof IOException){
                    eachExceptionsHandler.handleIOException((IOException) exception);
                }
            }
        }

    }//onPostExecute

    //Deprecated Constructor
    @Deprecated
    public PostResponseAsyncTask(AsyncResponse asyncResponse){
        this.asyncResponse = asyncResponse;
        this.context = (Context) asyncResponse;
    }

    @Deprecated
    public PostResponseAsyncTask(AsyncResponse asyncResponse, HashMap<String, String> postData){
        this.asyncResponse = asyncResponse;
        this.context = (Context) asyncResponse;
        this.postData = postData;
    }

    @Deprecated
    public PostResponseAsyncTask(AsyncResponse asyncResponse, String loadingMessage){
        this.asyncResponse = asyncResponse;
        this.context = (Context) asyncResponse;
        this.loadingMessage = loadingMessage;
    }

    @Deprecated
    public PostResponseAsyncTask(AsyncResponse asyncResponse, HashMap<String, String> postData, String loadingMessage){
        this.asyncResponse = asyncResponse;
        this.context = (Context) asyncResponse;
        this.postData = postData;
        this.loadingMessage = loadingMessage;
    }
    //End Deprecated Constructor
}
