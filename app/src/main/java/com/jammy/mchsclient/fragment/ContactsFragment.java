package com.jammy.mchsclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jammy.mchsclient.activity.AddContactsActivity;
import com.jammy.mchsclient.activity.InfoActivity;
import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.activity.SearchActivity;
import com.jammy.mchsclient.model.Friend;
import com.jammy.mchsclient.model.Msg;
import com.jammy.mchsclient.model.ReturnFriends;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.friends;
import static com.jammy.mchsclient.MyApplication.userOnLine;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class ContactsFragment extends Fragment {

    public static final String TAG = "ContactsFragment";
    ImageView ivAdd;
    ImageView ivSearch;
    ListView lvContacts;
    ReturnFriends returnFriends;
    int fnum = 0;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Friend> friendlist = new ArrayList<>();
    public static Handler handler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ContactsView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ivAdd = (ImageView)ContactsView.findViewById(R.id.iv_add_friend);
        lvContacts = (ListView) ContactsView.findViewById(R.id.lv_contacts);
        ivSearch = (ImageView)ContactsView.findViewById(R.id.iv_search);
        getNetData();
        init();
        return ContactsView;
    }
    private void init(){
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.clear();
                bundle.putSerializable("friendlist", friendlist);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContactsActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler(){
            /**
             * Subclasses must implement this to receive messages.
             *
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    Msg newAp = (Msg) msg.obj;
                }
            }
        };

    }

    private void getNetData() {
        Log.i(TAG, "useronline:"+userOnLine.getUsername());
        OkGo.post(API.RECEIVE_FRIENDS_API)
                .params("userInfo.username", userOnLine.getUsername())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("ContactsFragment", "getNetdata:success");
                        Gson gson = new Gson();
                        Log.i("ContactsFragment", "friends:"+gson);
                        returnFriends = gson.fromJson(s, ReturnFriends.class);
                        if (returnFriends.getCode() == 200 && returnFriends.getMsg().equals("SUCCESS")) {
                            fnum = returnFriends.getData().length;
                            for(int i =0;i<returnFriends.getData().length;i++){
                                friends.put(returnFriends.getData()[i].getUsername(),returnFriends.getData()[i]);
                            }
                            MyBaseAdapter adapter = new MyBaseAdapter(returnFriends.getData());
                            lvContacts.setAdapter(adapter);
                            lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getActivity(), InfoActivity.class);
                                    list.clear();
                                    list.add(returnFriends.getData()[position].getUsername());
                                    Log.i("ContactsFragment", "onItemClick: "+returnFriends.getData()[position].getUsername());
                                    list.add(returnFriends.getData()[position].getFriendinfo().getEmail());
                                    list.add(returnFriends.getData()[position].getFriendinfo().getPhone());
                                    list.add(returnFriends.getData()[position].getFriendinfo().getHead());
                                    list.add(returnFriends.getData()[position].getFriendinfo().getNickname());
                                    list.add(returnFriends.getData()[position].getFriendinfo().getGender()+"");
                                    list.add(returnFriends.getData()[position].getRemark());
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("friend", list);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                });
    }

    private class MyBaseAdapter extends BaseAdapter {
        private Friend[] data;

        private MyBaseAdapter(Friend[] list) {
            this.data = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fnum;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return data[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            final View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_contacts, null);
            final LinearLayout layoutItemContacts = (LinearLayout)view.findViewById(R.id.layout_item_contacts);
            layoutItemContacts.getBackground().setAlpha(220);
            final TextView tvFriendName = (TextView) view.findViewById(R.id.tv_contacts_friendname);
            final ImageView ivFriendHead = (ImageView) view.findViewById(R.id.iv_contacts_friendhead);
            final Friend friendItem = data[arg0];
            Log.i("ContactsFragment", "" + friendItem.getUsername());
            friendlist.add(friendItem);
            if (!friendItem.getRemark().equals(" ")) {
                tvFriendName.setText(friendItem.getRemark());
            } else {
                tvFriendName.setText(friendItem.getFriendinfo().getNickname());
            }
            Picasso.with(getContext())
                    .load(API.HEAD_PATH+friendItem.getUsername()+"_Head.png")
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.head)
                    .into(ivFriendHead);
            return view;
        }
    }

}
