package com.uday.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.uday.R;
import com.uday.adapter.ContactsAdapter;
import com.uday.controller.APIService;
import com.uday.controller.RetrofitClient;
import com.uday.model.Contacts.ContactsOutput;
import com.uday.other.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener, SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<ContactsOutput> contactList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    APIService aAPIService;
    SwipeRefreshLayout swipeRefreshLayout;
    // url to fetch contacts json
    private static final String URL = "https://api.androidhive.info/json/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        aAPIService = RetrofitClient.getClient(URL).create(APIService.class);
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, contactList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchContacts();
                                    }
                                }
        );
        fetchContacts();
    }

    /**
     * fetches json by making http calls
     */
    private void fetchContacts() {
       // showDialog();
        aAPIService= RetrofitClient.getClient(URL).create(APIService.class);
        try {
            Call<List<ContactsOutput>> call = aAPIService.readContacts();
            call.enqueue(new Callback<List<ContactsOutput>>() {
                @Override
                public void onResponse(Call<List<ContactsOutput>> call, Response<List<ContactsOutput>> response) {
                   // hideDialog();
                    List<ContactsOutput> mContacts=response.body();
                    Log.w("Respoooonse", mContacts.size()+"<<>>>");
                    //Log.w("RespoooonseMessage", response.body().getData().size()+"");
                    //Log.w("RespoooonseMessage",response.body().getData().equals(null)+"");
                    // adding contacts to contacts list
                    contactList.clear();
                    contactList.addAll(mContacts);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();

                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<List<ContactsOutput>> call, Throwable t) {
                    //hideDialog();

                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }




    @Override
    public void onContactSelected(ContactsOutput contact) {

    }

    @Override
    public void onRefresh() {
        fetchContacts();
    }
}
