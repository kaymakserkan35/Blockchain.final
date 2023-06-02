package com.betelgeuse.blockchain.userinterface.information;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.betelgeuse.blockchain.R;
import com.betelgeuse.blockchain.userinterface.analysis.AnalysisActivity;
import com.betelgeuse.blockchain.userinterface.model.InformationModel;
import com.betelgeuse.blockchain.userinterface.model.InformationToAnalysisModel;
import com.betelgeuse.blockchain.userinterface.model.InformationToUserSettingModel;
import com.betelgeuse.blockchain.userinterface.useroption.UserOptionActivity;
import com.google.gson.Gson;

import java.util.List;

public class InformationActivity extends AppCompatActivity {
    InformationController informationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // cache mantığı gibi kodları yaz activity nesnelerini yaratmak gibi....
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        String userEmail = getIntent().getStringExtra("email");
        informationController = new InformationController();
        informationController.getData(userEmail, this);
        InformationAdapter adapter = new InformationAdapter(null);
        informationController.infos.observe(this, new Observer<List<InformationModel>>() {
            @Override
            public void onChanged(List<InformationModel> informationModels) {
                adapter.setInfos(informationModels);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener((view, obj) -> {
            InformationModel informationModel = (InformationModel) obj;
            //
            // Log.d(view.getTag().toString(), "clicked!!");
            if (view.getId() == R.id.delete) {
                informationController.delete(userEmail, informationModel);
                return;
            }
            if (view.getId() == R.id.setting) {
                InformationToUserSettingModel data = new InformationToUserSettingModel(userEmail,informationModel.fromCurrency,informationModel.toCurrency);
                changeActivity(UserOptionActivity.class, true, "informationToUserOptionModel" , data);
                return;
            }
            if (view.getId() == R.id.symbolLayout) {
                InformationToAnalysisModel data = new InformationToAnalysisModel(userEmail,informationModel.fromCurrency,informationModel.toCurrency);
                changeActivity(AnalysisActivity.class, true, "informationToAnalysisModel", data);
                return;
            }
        });
        final ImageButton addUserOption = findViewById(R.id.addUserOption);
        addUserOption.setOnClickListener((View v) -> {
            changeActivity(UserOptionActivity.class, true, null,null);
        });
    }


    @Override
    protected void onRestart() {
        // surekli yenilenmesi gereken kodları yaz... sayfanın calısma mantığı ....
        super.onRestart();
        String userEmail = getIntent().getStringExtra("email");
        informationController.getData(userEmail, this);
    }

    private <Activity> void changeActivity(Class<Activity> activity, boolean addBackStack,String key, @Nullable Object data) {
        Intent intent =
                new Intent(InformationActivity.this, activity);
        if (data != null && key!=null) intent.putExtra(key, new Gson().toJson(data));
        if (!addBackStack) {
            finish();
        }
        startActivity(intent);
    }
}