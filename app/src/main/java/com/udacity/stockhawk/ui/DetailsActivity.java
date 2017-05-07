package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static String STOCK_SYMBOL_KEY = "stock_id";
    String mSymbol;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view)
    RecyclerView stockHistoryRecyclerView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.chart_his)
    LineChart chart;

    private StockHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        mSymbol = i.getStringExtra(STOCK_SYMBOL_KEY);
        ButterKnife.bind(this);

        adapter = new StockHistoryAdapter(this);
        stockHistoryRecyclerView.setAdapter(adapter);
        stockHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportLoaderManager().initLoader(0, null, this);
    }


    void setChartWithData(TreeMap<String, String> data) {
        List<Entry> entries = new ArrayList<Entry>();

        Iterator it  = data.entrySet().iterator();
        //Since the data is sorted by value, we can use an incremental value to add the closing prices
        int i = 1;
        while (it.hasNext()) {
            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
            float k = Float.parseFloat(pair.getKey());
            float v = Float.parseFloat(pair.getValue());
            entries.add(new Entry(i, v));
            i++;
        }
        String label = this.getString(R.string.chart_closing_price_label);
        LineDataSet dataSet = new LineDataSet(entries, label);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        dataSet.setColor(Color.rgb(255,255,255));
        dataSet.setValueTextColor(Color.rgb(51,51,255));

        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false);
        left.setTextColor(Color.rgb(255,255,255));

        YAxis right = chart.getAxisRight();
        right.setTextSize(12f);
        right.setTextColor(Color.rgb(255,255,255));

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return adapter.getDate((long) value);
            }
        };
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelRotationAngle(67f);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.rgb(255,255,255));

        //chart.setExtraBottomOffset(40); //https://github.com/PhilJay/MPAndroidChart/issues/1657

        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        chart.invalidate(); // refresh

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Contract.Quote.makeUriForStock(mSymbol), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToPosition(0);
        String history = data.getString(Contract.Quote.POSITION_HISTORY);
        List<String> items = Arrays.asList(history.split("\\n"));

        TreeMap<String, String> historyEntries = new TreeMap<String, String>();
        for (String entry : items) {
            List<String> historyEntry = Arrays.asList(entry.split(","));
            historyEntries.put(historyEntry.get(0), historyEntry.get(1));
        }

        adapter.setData(historyEntries);
        this.setChartWithData(historyEntries);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
