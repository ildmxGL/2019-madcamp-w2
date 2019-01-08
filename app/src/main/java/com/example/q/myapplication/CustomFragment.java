package com.example.q.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CustomFragment extends Fragment {

    private Button btn;
    private EditText nickname;
    public static final String NICKNAME = "usernickname";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        //call UI component  by id
        btn = (Button) view.findViewById(R.id.enterchat) ;
        nickname = (EditText) view.findViewById(R.id.nickname);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the nickname is not empty go to chatbox activity and add the nickname to the intent extra
                if(!nickname.getText().toString().isEmpty()){
                    Intent i  = new Intent(getActivity(), ChatBoxActivity.class);
                    //retreive nickname from textview and add it to intent extra
                    i.putExtra(NICKNAME,nickname.getText().toString());

                    startActivity(i);

//                    ChatBoxActivity newChatBoxActivity = new ChatBoxActivity();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(NICKNAME, nickname.getText().toString());
//                    newChatBoxActivity.setArguments(bundle);
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment, newChatBoxActivity);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }
}