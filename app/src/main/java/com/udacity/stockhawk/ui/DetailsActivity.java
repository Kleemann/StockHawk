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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    static String STOCK_SYMBOL_KEY = "stock_id";
    String mSymbol;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view)
    RecyclerView stockHistoryRecyclerView;

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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Contract.Quote.makeUriForStock(mSymbol), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToPosition(0);
        String history = data.getString(Contract.Quote.POSITION_HISTORY);
        List<String> items = Arrays.asList(history.split("\\n"));

        HashMap<String, String> historyEntries = new HashMap<String, String>();
        for (String entry : items) {
            List<String> historyEntry = Arrays.asList(entry.split(","));
            historyEntries.put(historyEntry.get(0), historyEntry.get(1));
        }

        adapter.setData(historyEntries);

        /*
        List<Entry> entries = new ArrayList<Entry>();

        Iterator it  = historyEntries.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();

            entries.add(new Entry(Float.parseFloat(pair.getKey()), Float.parseFloat(pair.getValue())));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(R.color.material_green_700);
        dataSet.setValueTextColor(R.color.material_red_700);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
        */
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
