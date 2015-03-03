package java2.devaunteledee.com.offlineapistorage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {
    String _title,_Author;
    ArrayList<TheSubReddit> theSubRedditArrayList;
    ListView listView;
    Network task;
    ProgressDialog progressDialog;
    String jsonString, url;
    MyAdapter adapter;

TheSubReddit mySubReddits;
    String[] sectionArray;
    Spinner subRedditSpinner;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.myListView);
         mContext = this;
        theSubRedditArrayList = new ArrayList<TheSubReddit>();





        subRedditSpinner = (Spinner) findViewById(R.id.spinnerPort);


        sectionArray = new String[]{
        "BlackPeopleTwitter",
        "funny",
        "gaming",
        "technology",
        "nba",
        };

        ArrayAdapter<String> adapterSpinnerOne = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,sectionArray);

        subRedditSpinner.setAdapter(adapterSpinnerOne);

        theSubRedditArrayList.add(new TheSubReddit(_title,_Author));
//        click.onClick(subRedditSpinner);

//        subRedditSpinner.setOnClickListener(click);
        subRedditSpinner.setOnItemSelectedListener(itemClick);


    }
    private class Network extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle((ProgressDialog.STYLE_SPINNER));
            progressDialog.setMessage("Loading ");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressPercentFormat(null);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                //URL connection check
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                jsonString = IOUtils.toString(is);
                is.close();
                connection.disconnect();
            }catch (MalformedURLException e){
                e.printStackTrace();

            }catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try{
                //JSON
                JSONObject outerObject = new JSONObject(jsonString);

                JSONArray childrenArray = outerObject.getJSONObject("data").getJSONArray("children");
                for (int i = 0; i < childrenArray.length(); i++){
                    JSONObject childObject = childrenArray.getJSONObject(i).getJSONObject("data");

                    if(childObject.has("title")){
                        _title = childObject.getString("title");
                        Log.e("Title",_title);


                    }else{
                        _title = "N/A";
                    }
                    if (childObject.has("author")){
                        _Author = childObject.getString("author");
                        Log.i("Author",_Author);
                    }else{
                        _Author = "N/A";

                    }

                    theSubRedditArrayList.add(new TheSubReddit(_title,_Author));
                }
            }catch (JSONException e){
                e.printStackTrace();

            }


        adapter = new MyAdapter(MainActivity.this,theSubRedditArrayList);
            Log.i("a","a:...." + MainActivity.this);
            Log.i("a","a:...." + theSubRedditArrayList);

            listView.setAdapter(adapter);

            progressDialog.dismiss();

        }


    };
    AdapterView.OnItemSelectedListener itemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if (isConnected()){
                Log.e("Click","Click");

                task = new Network();
                task.execute("http://www.reddit.com/r/" + subRedditSpinner.getSelectedItem().toString() + "/hot.json");

                theSubRedditArrayList.clear();


                //save the object
                saveObject(mySubReddits);

                // Get the Object
                TheSubReddit allofthesavedSubReddits = (TheSubReddit)loadSerializedObject(new File("/save_object.bin")); //get the serialized object from the sdcard and caste it into the Person class.
                Log.e("REDDITS","" + mySubReddits.toString());






            }
            else{
                Log.e("Click","Click no internet!!!!");
                Toast.makeText(MainActivity.this, "Internet connection unavalable", Toast.LENGTH_LONG).show();

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    public void saveObject(TheSubReddit subReddit){

        File outsideRoot = Environment.getExternalStorageDirectory();
        File theFolder = new File(outsideRoot,theSubRedditArrayList+".bin");
        try
        {

            FileOutputStream fos = new FileOutputStream(theFolder);
            ObjectOutputStream finalSaveShow = new ObjectOutputStream(fos);

            for (int i = 0;i< theSubRedditArrayList.size();i++){
                finalSaveShow.write(theSubRedditArrayList.get(i).title.getBytes());
                Log.i("SAVING!!!","Saving");
            }
        }
        catch(FileNotFoundException ex)
        {
            Log.v(" Save Error : ", ex.getMessage());
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object loadSerializedObject(File f)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
            Log.v("Serialization Read Error : ",ex.getMessage());
            ex.printStackTrace();
        }
        return f;
    }


    private boolean isConnected(){
        ConnectivityManager mgr = (ConnectivityManager)getSystemService((Context.CONNECTIVITY_SERVICE));

        if (mgr != null){
            NetworkInfo info = mgr.getActiveNetworkInfo();

            if (info != null && info.isConnected()){
                return true;
            }
        }
        return false;
    }



}
