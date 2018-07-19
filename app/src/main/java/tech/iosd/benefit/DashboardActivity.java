package tech.iosd.benefit;

import android.app.DatePickerDialog;
import android.app.Activity;/*
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;*/
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import tech.iosd.benefit.DashboardFragments.Chat;
import tech.iosd.benefit.DashboardFragments.ChoosePlan;
import tech.iosd.benefit.DashboardFragments.Main;
import tech.iosd.benefit.DashboardFragments.Notification;
import tech.iosd.benefit.Model.DatabaseHandler;
import tech.iosd.benefit.Utils.Constants;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Context ctx;
    FragmentManager fm;
    private SharedPreferences mSharedPreferences;
    private DatabaseHandler db;


    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Call the function callInstamojo to start payment here
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = this;

        db = new DatabaseHandler(ctx);

        showUserProfile();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.dashboard_content, new Main()).commit();

        ImageView notificationsBtn = findViewById(R.id.navigation_dashboard_notification);
        notificationsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fm.beginTransaction().replace(R.id.dashboard_content, new Notification()).addToBackStack(null).commit();
                FloatingActionButton contactBtn = findViewById(R.id.dashboard_contact);
                if(contactBtn != null)
                {
                    if(contactBtn.getVisibility()==View.VISIBLE) {
                        contactBtn.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.top_down));
                        contactBtn.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        ImageView coachBtn = findViewById(R.id.navigation_dashboard_coach);
        coachBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fm.beginTransaction().replace(R.id.dashboard_content, new ChoosePlan()).addToBackStack(null).commit();
                FloatingActionButton contactBtn = findViewById(R.id.dashboard_contact);
                if(contactBtn != null)
                {
                    if(contactBtn.getVisibility()==View.VISIBLE) {
                        contactBtn.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.top_down));
                        contactBtn.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private void showUserProfile() {
        String message = "Age: "+ db.getUserAge()+"\nHeight: "+db.getUserHeight()+"\nWeight: "+db.getUserWeight()+"\nGender "+db.getUserGender();
        Toast.makeText(ctx,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_dashboard:
            {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fm.beginTransaction().replace(R.id.dashboard_content, new Main()).addToBackStack(null).commit();
                return true;
            }
            case R.id.nav_logout:
            {
               /* SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.TOKEN,"");
                editor.apply();*/
                db.userLogOut();
                startActivity(new Intent(
                        DashboardActivity.this,OnBoardingActivity.class
                ));
                finish();
            }
            case R.id.nav_account:
            {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fm.beginTransaction().replace(R.id.dashboard_content, new AccountFragment()).addToBackStack(null).commit();
                FloatingActionButton contactBtn = findViewById(R.id.dashboard_contact);
                if(contactBtn != null)
                {
                    if(contactBtn.getVisibility()==View.VISIBLE) {
                        contactBtn.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.top_down));
                        contactBtn.setVisibility(View.INVISIBLE);
                    }
                }
            }
            break;
        }

        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
