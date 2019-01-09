package com.example.q.myapplication.tab1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.q.myapplication.LoginActivity;
import com.example.q.myapplication.MainActivity;
import com.example.q.myapplication.R;

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

import static com.facebook.FacebookSdk.getApplicationContext;

/* Contacts Fragment */
public class ContactsFragment extends Fragment {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private ListView listView;
    private NameListAdapter adapter;
    private List<NameListItem> nameList;
    private String str;

    private AlertDialog.Builder builder;
    private FloatingActionButton more,download;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        more = (FloatingActionButton) rootView.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactAdd.class);
                startActivityForResult(intent, 100);
            }
        });

        download = (FloatingActionButton)rootView.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://socrip2.kaist.ac.kr:5080/contacts");
            }
        });

        listView = (ListView) rootView.findViewById(R.id.listview);
        nameList = new ArrayList<NameListItem>();
        adapter = new NameListAdapter(getContext().getApplicationContext(), nameList);

        listView.setAdapter(adapter);
        new JSONTask().execute("http://socrip2.kaist.ac.kr:5080/contacts");


        return rootView;
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
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

            /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = adapter.getItem(position);
                    String temp;
                    String _id = null;
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            temp = object.getString("name");
                            if (name.equals(temp)) {
                                _id = object.getString("_id");
                                new JSONTask().execute("http://socrip3.kaist.ac.kr:5880/"+_id);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });*/
        }
    }
}
