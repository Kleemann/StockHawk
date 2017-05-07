package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.firebase.jobdispatcher.Constraint;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.data.StockProvider;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private Cursor data;

    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;
    private final DecimalFormat dollarFormatWithPlus;

    private  Uri uri = Contract.Quote.URI;


    public StackRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());

        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
        percentageFormat.setNegativePrefix("-");

        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        dollarFormatWithPlus.setPositivePrefix("+$");
    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {
        //http://stackoverflow.com/a/13197534
        Thread thread = new Thread() {
            public void run() {
                query();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void query() {
        data = mContext.getContentResolver().query(uri, null, null, null, null);
    }

    @Override
    public void onDestroy() {
        data = null;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (data != null) {
            count = data.getCount();
        }
        return count;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        data.moveToPosition(position);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.stock_widget_item);

        if (position <= getCount()) {
            rv.setTextViewText(R.id.tv_symbol, data.getString(Contract.Quote.POSITION_SYMBOL));

            float closingPrice = data.getFloat(Contract.Quote.POSITION_PRICE);
            rv.setTextViewText(R.id.tv_closing_price, dollarFormat.format((double)closingPrice));

            float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
            String change = dollarFormatWithPlus.format(rawAbsoluteChange);
            String percentage = percentageFormat.format(percentageChange / 100);

            if (PrefUtils.getDisplayMode(mContext).equals(mContext.getString(R.string.pref_display_mode_absolute_key))) {
                rv.setTextViewText(R.id.tv_change, change);
            } else {
                rv.setTextViewText(R.id.tv_change, percentage);
            }
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