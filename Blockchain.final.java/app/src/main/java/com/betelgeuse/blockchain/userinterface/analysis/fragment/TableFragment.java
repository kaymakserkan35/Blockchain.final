package com.betelgeuse.blockchain.userinterface.analysis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.betelgeuse.blockchain.core.libs.TableData;
import com.betelgeuse.blockchain.core.libs.TableResponsiveLayout;
import com.betelgeuse.blockchain.userinterface.analysis.AnalysisController;
import com.betelgeuse.blockchain.userinterface.model.AnalysisModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableFragment extends Fragment {

    AnalysisController controller;

    // TODO: Rename parameter arguments, choose names that match


    // TODO: Rename and change types of parameters


    public TableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TableFragment newInstance(String param1, String param2) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //
        //
        controller = AnalysisController.getSingleton(getContext());
        TableResponsiveLayout table = new TableResponsiveLayout(getContext());
        List<TableData> tableDataList = new ArrayList<>();
        controller.analyses.observe(getViewLifecycleOwner(), analysisModels -> {
            AnalysisModel model = analysisModels.get(0);
            String[] headers = {"-",
                    "   RSI   ", "  LOWERBAND  ", " MIDDLEBAND ", " UPPERBAND ", " CURRENTPRICE "};
            table.setHeaders(headers);
            for (AnalysisModel m : analysisModels
            ) {
                TableData data = new TableData().setData1(m.calendar).setData2(m.rsi).setData3(m.lowerBand)
                        .setData4(m.middleBand).setData5(m.upperBand).setData6(m.price);
                tableDataList.add(data);
            }
            table.setTableDataList(tableDataList);
            table.render();
        });
        return table;
    }
}