package com.betelgeuse.blockchain.userinterface.analysis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.betelgeuse.blockchain.R;
import com.betelgeuse.blockchain.userinterface.model.InformationToAnalysisModel;
import com.betelgeuse.blockchain.userinterface.model.modelListener.AnalysisModelListListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class AnalysisActivity extends AppCompatActivity {

    AnalysisController analysisController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        /*------------------------------------------------------------*/
        String informationToAnalysisModel = getIntent().getStringExtra("informationToAnalysisModel");
        InformationToAnalysisModel model = new Gson().fromJson(informationToAnalysisModel, InformationToAnalysisModel.class);
        analysisController = AnalysisController.getSingleton(getApplicationContext());
        analysisController.getData(model.email, model.fromCurrency, model.toCurrency,null);

    }

}