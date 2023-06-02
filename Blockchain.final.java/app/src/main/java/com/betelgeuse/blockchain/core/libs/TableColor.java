package com.betelgeuse.blockchain.core.libs;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TableColor {


    public void setColors(TableResponsiveLayout tableResponsiveLayout) {

        setColor(tableResponsiveLayout.headerTableLayout);
        setColor(tableResponsiveLayout.dateTableLayout);
        setColor(tableResponsiveLayout.dataTableLayout);
        setColor(tableResponsiveLayout.symbolTableLayout);
    }

    private List<TableRow> getAllTableInTableLayout(TableLayout layout) {
        List<TableRow> tableRows = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                tableRows.add(row);
            }
        }
        return tableRows;
    }

    private List<TextView> getTextViewInTableRow(TableRow tableRow) {
        List<TextView> textViews = new ArrayList<>();
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            View child = tableRow.getChildAt(i);
            if (child instanceof TextView) {
                textViews.add((TextView) child);
            }
        }
        return textViews;
    }

    public TableColor setColor(TableLayout tableLayout) {
        TableColor tableColor = new TableColor();
        /*--------------STRIP COLORS---------------*/
        int stripColor = Color.rgb(121, 140, 136);
        /*--------------TEXT COLORS---------------*/
        int colorTextSymbol = Color.rgb(212, 200, 178);
        int colorTextHeader = Color.rgb(212, 200, 178);
        int colorTextDate = Color.rgb(212, 200, 178);
        int colorTextData = Color.rgb(121, 140, 136);
        /*-----------------TEXT BACKGROUND COLORS--------------------*/
        int colorSymbol = Color.rgb(99, 99, 99);
        int colorHeader = Color.rgb(48, 182, 131);
        int colorDate = Color.rgb(40, 147, 105);
        int colorData0 = Color.rgb(213, 236, 228);
        int colorData1 = Color.rgb(255, 255, 255);
        /*----------------SELECTED COLORS----------------------*/
        int chosenDataColor0 = 0, chosenDataColor1 = 0;
        int chosenTextColor = 0;
        /*----------------------------------------------------*/
        if (tableLayout.getTag().toString() == TableTag.DATA_LAYOUT_TAG) {
            chosenDataColor0 = colorData0;
            chosenDataColor1 = colorData1;
            chosenTextColor = colorTextData;
        }
        if (tableLayout.getTag().toString() == TableTag.SYMBOL_LAYOUT_TAG) {
            chosenDataColor0 = colorSymbol;
            chosenDataColor1 = colorSymbol;
            chosenTextColor = colorTextSymbol;
        }
        if (tableLayout.getTag().toString() == TableTag.HEADER_LAYOUT_TAG) {
            chosenDataColor0 = colorHeader;
            chosenDataColor1 = colorHeader;
            chosenTextColor = colorTextHeader;
        }
        if (tableLayout.getTag().toString() == TableTag.DATE_LAYOUT_TAG) {
            chosenDataColor0 = colorDate;
            chosenDataColor1 = colorDate;
            chosenTextColor = colorTextDate;
        }

        List<TableRow> rowList = tableColor.getAllTableInTableLayout(tableLayout);
        for (int i = 0; i < rowList.size(); i++) {
            /*-----------------STRIPS COLOR---------------*/
            TableRow row = rowList.get(i);
            if ((i % 2) == 0) row.setBackgroundColor(stripColor);
            if (((i % 2) == 1)) row.setBackgroundColor(stripColor);
            /*------------------------------------------------------*/
            if ((i % 2) == 0) {
                List<TextView> textViewList = tableColor.getTextViewInTableRow(row);
                for (int j = 0; j < textViewList.size(); j++) {
                    TextView textView = textViewList.get(j);
                    textView.setBackgroundColor(chosenDataColor0);
                    textView.setTextColor(chosenTextColor);

                }
            }
            if ((i % 2) == 1) {
                List<TextView> textViewList = tableColor.getTextViewInTableRow(row);
                for (int j = 0; j < textViewList.size(); j++) {
                    TextView textView = textViewList.get(j);
                    textView.setBackgroundColor(chosenDataColor1);
                    textView.setTextColor(chosenTextColor);
                }
            }

        }
        return this;
    }
}
