package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by makle on 07/05/2017.
 */

public class StackWidgetService extends RemoteViewsService {
    private Context mContext;
    private int mAppWidgetId;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;

    public StackRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d("BLLAH", "onDataSetChanged: ");
        //Cursor c = mContext.getContentResolver().query(Contract.Quote.URI, null, null, null, null);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.stock_widget_item);

        if (position <= getCount()) {
            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            rv.setTextViewText(R.id.tv_symbol, date);
            rv.setTextViewText(R.id.tv_closing_price, "100");
            rv.setTextViewText(R.id.tv_change, "+100");
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}