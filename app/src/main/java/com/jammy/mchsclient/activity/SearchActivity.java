package com.jammy.mchsclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.jammy.mchsclient.R;
import com.jammy.mchsclient.model.Friend;

import java.util.ArrayList;

import static com.lzy.okgo.OkGo.getContext;

public class SearchActivity extends AppCompatActivity {

    ListView lvSContacts;
    SearchView svSearch;
    ImageView ivReturn;
    ArrayList<Friend> friendlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        friendlist = (ArrayList) intent.getSerializableExtra("friendlist");
        lvSContacts = (ListView) findViewById(R.id.lv_search_contacts);
        svSearch = (SearchView) findViewById(R.id.sv_contacts_search);
        ivReturn = (ImageView) findViewById(R.id.iv_search_back);
        SearchBaseAdapter adapter = new SearchBaseAdapter(friendlist);
        lvSContacts.setAdapter(adapter);
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Friend> fl = searchItem(newText);
                updateLayout(fl);
                return false;
            }
        });
        svSearch.setSubmitButtonEnabled(false);
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public ArrayList<Friend> searchItem(String name) {
        ArrayList<Friend> mSearchList = new ArrayList<>();
        for (int i = 0; i < friendlist.size(); i++) {
            if (friendlist.get(i).getUsername().indexOf(name) != -1 || (!friendlist.get(i).getRemark().equals(" ") && friendlist.get(i).getRemark().indexOf(name) != -1) || friendlist.get(i).getFriendinfo().getNickname().indexOf(name) != -1)
                // 存在匹配的数据
                mSearchList.add(friendlist.get(i));
        }
        return mSearchList;
    }

    private class SearchBaseAdapter extends BaseAdapter {
        private ArrayList<Friend> data;

        private SearchBaseAdapter(ArrayList<Friend> list) {
            this.data = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return friendlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return data.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            final View view = LayoutInflater.from(getBaseContext())
                    .inflate(R.layout.item_sfriend, null);
            final LinearLayout layoutItemContacts = (LinearLayout)view.findViewById(R.id.layout_item_sfriend);
            layoutItemContacts.getBackground().setAlpha(220);
            final TextView tvFriendNickName = (TextView) view.findViewById(R.id.tv_search_friendname);
            final ImageView ivFriendHead = (ImageView) view.findViewById(R.id.iv_search_friendhead);
            final TextView tvFriendUsername = (TextView) view.findViewById(R.id.tv_search_friendusername);
            final TextView tvFriendRemark = (TextView) view.findViewById(R.id.tv_search_remark);

            tvFriendNickName.setText(data.get(arg0).getFriendinfo().getNickname());
            tvFriendUsername.setText(data.get(arg0).getUsername());
            tvFriendRemark.setText(data.get(arg0).getRemark());

            return view;
        }
    }

    // 更新数据
    public void updateLayout(ArrayList<Friend> fl) {
        SearchBaseAdapter adapter = new SearchBaseAdapter(fl);
        lvSContacts.setAdapter(adapter);
    }

}
