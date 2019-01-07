package com.example.q.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* Contacts Fragment */
public class ContactsFragment extends Fragment {

    private ListView listView;
    private NameListAdapter adapter;
    private List<NameListItem> nameList;
    private String str;

    private JSONArray jsonArray;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        listView = (ListView) rootView.findViewById(R.id.listview);
        nameList = new ArrayList<NameListItem>();
        adapter = new NameListAdapter(getContext().getApplicationContext(), nameList);

        listView.setAdapter(adapter);
        new JSONTask().execute("http://socrip3.kaist.ac.kr:5880/contacts");

        return rootView;
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                /*jsonObject.accumulate("user_id", "androidTest");
                jsonObject.accumulate("name", "yun");*/

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            try {
                nameList.clear();
                JSONArray jsonArray = new JSONArray(result); // 목록들
                String name;
                //int weight;
                //String phone;
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    name = object.getString("name");

                    //phone = object.getString("phone");
                    NameListItem item = new NameListItem(name);
                    nameList.add(item);
                    count++;
                }
                if (count == 0) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsFragment.this.getActivity());
                    dialog = builder.setMessage("등록된 전화번호가 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // listview event
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = adapter.getItem(position);
                    String temp;
                    String phone = null;
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            temp = object.getString("name");
                            if (name.equals(temp)) {
                                phone = object.getString("phone_number");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(name);
                    builder.setMessage(phone);
                    final String finalPhone = phone;
                    builder.setPositiveButton("전화",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + finalPhone));
                                    startActivity(dial);
                                }
                            });
                    builder.setNegativeButton("문자",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + finalPhone));
                                    startActivity(sms);
                                }
                            });
                    builder.show();
                }
            });
        }
    }

}
