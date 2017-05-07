package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteJobService;
import com.udacity.stockhawk.sync.QuoteSyncJob;

/**
 * Created by makle on 06/05/2017.
 */

public class StockWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        QuoteSyncJob.initialize(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, StockWidgetIntentService.class);
        context.startService(intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, StockWidgetIntentService.class));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(QuoteSyncJob.ACTION_DATA_UPDATED)) {
            context.startService(new Intent(context, StockWidgetIntentService.class));
        }
    }
}
