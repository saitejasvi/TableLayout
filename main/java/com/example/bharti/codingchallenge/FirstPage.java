package com.example.bharti.codingchallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.bitmap;
import static android.R.attr.data;
//import static android.R.attr.name;
import static com.example.bharti.codingchallenge.R.id.tablerow;
//import static com.example.bharti.codingchallenge.R.id.text;

public class FirstPage extends AppCompatActivity {

    ArrayList<HashMap<String, String>> userList = new ArrayList<>();
    String tag;
  //  TableLayout tl;
    TableRow itemsName;

    //TextView tvItemName0;
    private static final String id1 = "Avatar";
    private static final String id2 = "Name";
    private static final String id3 = "Text";
    // TextView tvItemName1;
    public String url;
    // ImageView tvItemName2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);


    }

    @Override
    protected void onStart() {
        super.onStart();
        url = "https://alpha-api.app.net/stream/0/posts/stream/global";
        MyTask task = new MyTask();
        task.execute(url);
    }

    public class MyTask extends AsyncTask<String, String, ArrayList> {
        HttpURLConnection conn;
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream;
        URL downloadUrl;
        String data;


        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            try {

                downloadUrl = new URL(url);
                conn = (HttpURLConnection) downloadUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // conn.setDoOutput(true);
                conn.connect();
                inputStream = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = br.readLine()) != null)
                    buffer.append(line + "rn");

                inputStream.close();
                conn.disconnect();
                data = buffer.toString();
                JSONObject jObj = new JSONObject(data);

                JSONArray dataList = jObj.getJSONArray("data");
                // JSONObject time = mainObj.getJSONObject("created_at");

                for (int i = 0; i < dataList.length(); i++) {
                    HashMap<String, String> datam = new HashMap<>();

                    JSONObject c = dataList.getJSONObject(i);
                    String time = c.getString("created_at");
                    String text = c.getString("text");
                    JSONObject user = c.getJSONObject("user");
                    String username = user.getString("username");
                    JSONObject userI = user.getJSONObject("avatar_image");
                    String imageUrl = userI.getString("url");
                    Log.d(tag, imageUrl);


                    Bitmap bit = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());

                    // convert bitmap to base64
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    datam.put(id1, encoded);
                    datam.put(id2, username);
                    datam.put(id3, text);
                    datam.put("time", time);

                    Log.d("data is", username);


                    userList.add(i,datam);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();


            }
            return userList;

        }

        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);

            TableLayout tl = (TableLayout) findViewById(R.id.table);

            Log.d(tag, Integer.toString(userList.size()));
            for (int i = 0; i < userList.size(); i++) {

                TableRow tr = new TableRow(FirstPage.this);
                tr.setId(100 + i);

                //Note that you must use TableLayout.LayoutParams,
                //since the parent of this TableRow is a TableLayout
                TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f);


                byte[] decodedString = Base64.decode(userList.get(i).get(id1), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                ImageView image = new ImageView(FirstPage.this);
                image.setImageBitmap(decodedByte);
                //Note that here you must use TableRow.LayoutParams
                //since TableRow is the parent of this ImageView
                TableRow.LayoutParams imageParams = new TableRow.LayoutParams();

                //And this is how you set the gravity:
                imageParams.gravity = Gravity.CENTER;
                imageParams.width = 250;
                imageParams.height = 250;
                image.setLayoutParams(imageParams);
                tr.addView(image);
                TextView tv1 = new TextView(FirstPage.this);
                String id = userList.get(i).get(id2);
                tv1.setText(id);

                tr.addView(tv1);
                TextView tv2 = new TextView(FirstPage.this);
                String id2 = userList.get(i).get(id3);
                tv2.setText(id2);
                tr.addView(tv2);
                tr.setLayoutParams(params);
                tl.addView(tr, params);
                tl.setStretchAllColumns(true);
                itemsName = (TableRow) findViewById(tablerow);

                 Bitmap bmp = BitmapFactory.decodeFile(id1);

                Log.d("Row#", "" +i);
                Log.d("Row name", "" +userList.get(i).get(id2));

            }
        }
    }
}

