package com.betelgeuse.blockchain.core.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.betelgeuse.blockchain.R;

import java.util.ArrayList;
import java.util.List;

public class TableResponsiveLayout extends RelativeLayout {

    public TableResponsiveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TableResponsiveLayout(Context context) {
        super(context);

        this.context = context;

        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)

    }

    public void render(){
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();
        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.headerTableLayout);
        this.scrollViewC.addView(this.dateTableLayout);
        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.dataTableLayout);
        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        // add some table rows
        this.addTableRowToTableA();
        this.addTableRowToTableB();
        this.resizeHeaderHeight();
        this.getTableRowHeaderCellWidth();
        this.generateTableC_AndTable_B();
        this.resizeBodyTableRowHeight();
        new TableColor().setColors(this);

    }

    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }

    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }


    /*-------------------------------------------------------------------------------------------------------------*/
    // set the header titles
    String headers[] = {"Header 1 ", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6", "Header 7", "Header 8", "Header 9"};
    List<TableData> tableDataList = this.sampleData();

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setTableDataList(List<TableData> tableDataList) {
        this.tableDataList = tableDataList;
    }

    private void reArrangeDataTextsAndHeaderTextsLength(){
        /*---------------------OLMADI BEEE---------------------------------*/
        if (headers.length!=tableDataList.size()) return;
        for (int i = 0; i < tableDataList.size(); i++) {
            String header = headers[i];
            for (int j = 0; j <tableDataList.size() ; j++) {
                TableData data = tableDataList.get(j);
                int dif = header.length()-data.data1.length();
                while (header.length()<data.data1.length()){
                    header = "  " + header + "  ";
                }
                headers[i] = header;

            }
        }
    }

    /*-------------------------------------------------------------------------------------------------------------*/
    TableLayout symbolTableLayout;
    TableLayout headerTableLayout;
    TableLayout dateTableLayout;
    TableLayout dataTableLayout;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;


    Context context;


    int headerCellsWidth[] = new int[headers.length];




    // this is just the sample data
    List<TableData> sampleData() {

        List<TableData> tableDataList = new ArrayList<TableData>();

        for (int x = 1; x <= 30; x++) {

            TableData tableData = new TableData(
                    "C 1, R " + x,
                    "C 2, R " + x + " ",
                    "C 3, R " + x,
                    "C 4, R " + x,
                    "C 5, R " + x,
                    "C 6, R " + x,
                    "C 7, R " + x,
                    "C 8, R " + x,
                    "C 9, R " + x
            );

            tableDataList.add(tableData);
        }

        return tableDataList;

    }

    // initalized components
    private void initComponents() {

        this.symbolTableLayout = new TableLayout(this.context);
        this.symbolTableLayout.setTag(TableTag.SYMBOL_LAYOUT_TAG);
        this.headerTableLayout = new TableLayout(this.context);
        this.headerTableLayout.setTag(TableTag.HEADER_LAYOUT_TAG);
        this.dateTableLayout = new TableLayout(this.context);
        this.dateTableLayout.setTag(TableTag.DATE_LAYOUT_TAG);
        this.dataTableLayout = new TableLayout(this.context);
        this.dataTableLayout.setTag(TableTag.DATA_LAYOUT_TAG);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);
        /*------------------Hide scrollbar in ScrollView-----------------------------*/
        this.horizontalScrollViewB.setVerticalScrollBarEnabled(false);
        this.horizontalScrollViewB.setHorizontalScrollBarEnabled(false);
        this.scrollViewC.setVerticalScrollBarEnabled(false);
        this.scrollViewC.setHorizontalScrollBarEnabled(false);
        this.scrollViewD.setVerticalScrollBarEnabled(false);
        this.scrollViewD.setHorizontalScrollBarEnabled(false);
        this.horizontalScrollViewD.setHorizontalScrollBarEnabled(false);
        this.horizontalScrollViewD.setVerticalScrollBarEnabled(false);
        /*--------------------------Remove RecyclerView scroll effects---------------*/
        this.horizontalScrollViewB.setOverScrollMode(View.OVER_SCROLL_NEVER);
        this.scrollViewC.setOverScrollMode(View.OVER_SCROLL_NEVER);
        this.scrollViewD.setOverScrollMode(View.OVER_SCROLL_NEVER);
        this.horizontalScrollViewD.setOverScrollMode(View.OVER_SCROLL_NEVER);

    }

    // set essential component IDs
    private void setComponentsId() {
        /*
             this.tableA.setId(1);
             this.horizontalScrollViewB.setId(2);
             this.scrollViewC.setId(3);
             this.scrollViewD.setId(4);
         */

        this.symbolTableLayout.setId(getResources().getInteger(R.integer.one));
        this.horizontalScrollViewB.setId(getResources().getInteger(R.integer.two));
        this.scrollViewC.setId(getResources().getInteger(R.integer.three));
        this.scrollViewD.setId(getResources().getInteger(R.integer.four));
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout() {

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.symbolTableLayout.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.symbolTableLayout.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        this.addView(this.symbolTableLayout);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }


    private void addTableRowToTableA() {
        this.symbolTableLayout.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        this.headerTableLayout.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow() {

        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView(this.headers[0]);
        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount - 1); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    // generate table row of table C and table D
    private void generateTableC_AndTable_B() {

        // just seeing some header cell width
        for (int x = 0; x < this.headerCellsWidth.length; x++) {
            Log.v("TableMainLayout.java", this.headerCellsWidth[x] + "");
        }


        for (TableData tableData : this.tableDataList) {

            TableRow tableRowForTableC = this.tableRowForTableC(tableData);
            TableRow taleRowForTableD = this.taleRowForTableD(tableData);

            this.dateTableLayout.addView(tableRowForTableC);
            this.dataTableLayout.addView(taleRowForTableD);


        }
    }

    // a TableRow for table C
    TableRow tableRowForTableC(TableData tableData) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.bodyTextView(tableData.data1);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow taleRowForTableD(TableData tableData) {

        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow) this.headerTableLayout.getChildAt(0)).getChildCount();
        String info[] = {
                tableData.data2,
                tableData.data3,
                tableData.data4,
                tableData.data5,
                tableData.data6,
                tableData.data7,
                tableData.data8,
                tableData.data9
        };

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            if ((x % 2) == 0) {
                TextView textViewB = this.bodyTextView(info[x]);
                taleRowForTableD.addView(textViewB, params);
            } else {
                TextView textViewB = this.bodyTextView(info[x]);
                taleRowForTableD.addView(textViewB, params);
            }


        }

        return taleRowForTableD;

    }

    // table cell standard TextView
    TextView bodyTextView(String label) {
        TextView bodyTextView = new TextView(this.context);


        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);

        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.symbolTableLayout.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.headerTableLayout.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) this.symbolTableLayout.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.headerTableLayout.getChildAt(0)).getChildCount();
        ;

        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.symbolTableLayout.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.headerTableLayout.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.dateTableLayout.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.dateTableLayout.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.dataTableLayout.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
}