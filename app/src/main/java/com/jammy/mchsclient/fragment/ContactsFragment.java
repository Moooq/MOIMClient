package com.jammy.mchsclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jammy.mchsclient.activity.InfoActivity;
import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.model.Friend;
import com.jammy.mchsclient.model.ReturnFriends;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.jammy.mchsclient.MyApplication.friends;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class ContactsFragment extends Fragment {

    ImageView ivAdd;
    EditText etSearch;
    ImageView ivEtDelete;
    ListView lvContacts;
    ReturnFriends returnFriends;
    int fnum = 0;
    ArrayList<String> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ContactsView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ivAdd = (ImageView)ContactsView.findViewById(R.id.iv_add_friend);
        lvContacts = (ListView) ContactsView.findViewById(R.id.lv_contacts);
        etSearch = (EditText)ContactsView.findViewById(R.id.et_contacts_search);
        etSearch.clearFocus();
        ivEtDelete = (ImageView)ContactsView.findViewById(R.id.iv_search_delete);
        init();
        getNetData();
        return ContactsView;
    }
    private void init(){
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        etSearch.addTextChangedListener(new EditChangedListener());
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,KeyEvent event)  {
                if (actionId==EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0) ;
                    etSearch.clearFocus();
                    etSearch.setText("");
                    //do something;
                    return true;
                }
                return false;
            }
        });

        ivEtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
    }

    private void getNetData() {
        OkGo.post(API.RECEIVE_FRIENDS_API)
                .params("userInfo.username", MyApplication.userOnLine.getUsername())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
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
                                    Log.i(TAG, "onItemClick: "+returnFriends.getData()[position].getUsername());
                                    list.add(returnFriends.getData()[position].getEmail());
                                    list.add(returnFriends.getData()[position].getPhone());
                                    list.add(returnFriends.getData()[position].getHead());
                                    list.add(returnFriends.getData()[position].getNickname());
                                    list.add(returnFriends.getData()[position].getGender()+"");
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
            Log.i("body", "" + friendItem.getUsername());
            if (!friendItem.getRemark().equals(" ")) {
                tvFriendName.setText(friendItem.getRemark());
            } else {
                tvFriendName.setText(friendItem.getNickname());
            }
            return view;
        }
    }

    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 10;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()>0){
                ivEtDelete.setVisibility(View.VISIBLE);
            }else{
                ivEtDelete.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}
