package com.example.android.mcproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Pramodh on 11/29/2017.
 */

public class Pager extends FragmentStatePagerAdapter {
//    ref: https://www.simplifiedcoding.net/android-tablayout-example-using-viewpager-fragments/
    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
//    this is from old code. check to see if its still needed
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Knn tab1 = new Knn();
                return tab1;
            case 1:
                Svm tab2 = new Svm();
                return tab2;
            case 2:
                Nbc tab3 = new Nbc();
                return tab3;
            case 3:
                Logs tab4 = new Logs();
                return tab4;
            default:
                return null;
        }
    }


//    this is being used
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);

        //Returning the current tabs
        switch (position) {
            case 0:
                Knn tab1 = new Knn();
                return "KNN";
            case 1:
                Svm tab2 = new Svm();
                return "SVM";
            case 2:
                Nbc tab3 = new Nbc();
                return "NBC";
            case 3:
                Logs tab4 = new Logs();
                return "Logs";
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
