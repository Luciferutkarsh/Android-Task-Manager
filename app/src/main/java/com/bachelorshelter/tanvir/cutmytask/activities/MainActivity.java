package com.bachelorshelter.tanvir.cutmytask.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.bachelorshelter.tanvir.cutmytask.R;
import com.bachelorshelter.tanvir.cutmytask.model.Task;
import com.bachelorshelter.tanvir.cutmytask.adapter.TaskAdapter;
import com.bachelorshelter.tanvir.cutmytask.helper.DatabaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recView;
    private View rview;
    private TaskAdapter adapter;
    private List<Task> taskItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHandler db;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rview = findViewById(R.id.rootView);
        calendar = Calendar.getInstance();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(MainActivity.this,TaskEditActivity.class));
                intent.putExtra("origin","forNew");
                startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        //RecyclerView Implement
        taskItems = new ArrayList<>();
        recView = (RecyclerView)findViewById(R.id.rec_list_f_task);
        adapter = new TaskAdapter(taskItems,this);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
        //adapter.setTaskItemClickCallback(this);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_task);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Date date = new Date();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTask(dateFormat.format(date));
            }
        });
        fetchTask(dateFormat.format(date));
    }




    private void fetchTask(String date){
        taskItems.clear();

        db = new DatabaseHandler(getApplicationContext());
        List<Task> results = db.getAllTaskByDate(date);
        db.closeDB();

        for (Task task : results) {
            taskItems.add(task);
            /*if(task.getAction()==1){
                taskItems.add(taskItems.size(),task);
            }
            else {
                taskItems.add(task);
            }*/

        }
        //Toast.makeText(getApplicationContext(),taskItems.get(0).getTaskDesc(),Toast.LENGTH_SHORT).show();

        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);


        //Set day name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1  = null;
        try {
            date1 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long today = new Date().getTime();
        today -= today%86400000;

        long due = date1.getTime();
        due -= due%86400000;

        long diff = ((due-today)/86400000);


        if(diff == -1){
            //yesterday
            getSupportActionBar().setTitle("Yesterday");
        }
        else if(diff == 0){
            //another date which is more than tomorrow or less than yesterday
            getSupportActionBar().setTitle("Today");
        }
        else if(diff == 1){
            //tomorrow
            getSupportActionBar().setTitle("Tomorrow");
        }
        else{
            getSupportActionBar().setTitle(date);
        }



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_date) {

            DatePickerDialog datePickerDialog = new DatePickerDialog(rview.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date1  = null;
                    try {
                        date1 = dateFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fetchTask(dateFormat.format(date1));
                }


            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()- 1000);
            datePickerDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.unfinished_task) {
            startActivity(new Intent(MainActivity.this,UnfinishedTaskActivity.class));
        } else if (id == R.id.nav_task_manager) {

        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
