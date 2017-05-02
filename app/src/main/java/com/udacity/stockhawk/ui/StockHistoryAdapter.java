package com.udacity.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by makle on 01/05/2017.
 */

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.StockHistoryViewHolder> {

    private final Context context;
    private final DecimalFormat dollarFormat;
    private HashMap<String, String> data;

    public StockHistoryAdapter(Context context) {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        this.context = context;
    }

    void setData(HashMap<String, String> data) {
        HashMap<String,String> sortedData = sortHashMapByValues(data);
        this.data = sortedData;

        notifyDataSetChanged();
    }


    String getHistoryValueAtPosition(int position) {
        return (new ArrayList<String>(data.values())).get(position);
    }

    String getHistoryKeyAtPosition(int position) {
        return (new ArrayList<String>(data.keySet())).get(position);
    }


    public static String getDate(long milliSeconds)  {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        // Create a DateFormatter object for displaying date in specified format and return the formatted String
        DateFormat f = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return  f.format(calendar.getTime());
    }


    public LinkedHashMap<String, String> sortHashMapByValues(HashMap<String, String> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, String> sortedMap =
                new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    @Override
    public StockHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false);

        return new StockHistoryViewHolder(item);
    }

    @Override
    public void onBindViewHolder(StockHistoryViewHolder holder, int position) {

        String dateSeconds = getHistoryKeyAtPosition(position);
        String dateString = getDate(Long.valueOf(dateSeconds));

        double closingPrice = Double.parseDouble(getHistoryValueAtPosition(position));
        holder.closingTV.setText(dollarFormat.format(closingPrice));
        holder.dateTV.setText(dateString);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (data != null) {
            count = data.size();
        }
        return count;
    }

    class StockHistoryViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_his_date)
        TextView dateTV;

        @BindView(R.id.tv_his_closing)
        TextView closingTV;

        public StockHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
