package com.example.android.mcproject;

import android.content.Context;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

//    variables
    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    public static Logger logger = Logger.getLogger("MCPROJECT");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkStoragePermissions(this);
//        lambdaCallTest();
        initTabs();
    }

    private void checkStoragePermissions(Activity thisActivity) {
//        ref: https://developer.android.com/training/permissions/requesting.html
        int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(thisActivity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            System.out.println("Read storage permission granted!");
            initLogger();
        } else {
            System.out.println("Read storage permission denied!");
        }
    }

//    Init tab view
    private void initTabs(){
        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Knn"));
        tabLayout.addTab(tabLayout.newTab().setText("Svm"));
        tabLayout.addTab(tabLayout.newTab().setText("NBC"));
        tabLayout.addTab(tabLayout.newTab().setText("Logs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views4
//        ref: https://stackoverflow.com/questions/38658480/swiping-the-view-pager-fragment-will-not-move-the-tabs
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initLogger() {
        Context ctx = this.getApplicationContext();
        LogConfigurator configurator = new LogConfigurator();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data" + File.separator + ctx.getPackageName());

        if (!file.exists()) {
            file.mkdirs();
        }

        String logFileName = file + File.separator + "logs.txt";
        configurator.setFileName(logFileName);
        configurator.setRootLevel(Level.INFO);
        configurator.setFilePattern("[%-5p] %d - %m%n");
        configurator.configure();
        logger.info("The logger is configured");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
