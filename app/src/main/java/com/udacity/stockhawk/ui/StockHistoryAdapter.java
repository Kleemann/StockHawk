package com.udacity.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by makle on 01/05/2017.
 */

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.StockHistoryViewHolder> {

    private final Context context;

    private HashMap<String, String> data;

    public StockHistoryAdapter(Context context) {
        this.context = context;
    }

    void setData(HashMap<String, String> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    String getHistoryValueAtPosition(int position) {
        return (new ArrayList<String>(data.values())).get(position);
    }

    String getHistoryKeyAtPosition(int position) {
        return (new ArrayList<String>(data.keySet())).get(position);
    }


    @Override
    public StockHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false);

        return new StockHistoryViewHolder(item);
    }

    @Override
    public void onBindViewHolder(StockHistoryViewHolder holder, int position) {
        holder.closingTV.setText(getHistoryValueAtPosition(position));
        holder.dateTV.setText(getHistoryKeyAtPosition(position));

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
