package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by makle on 06/05/2017.
 */

public class StockWidgetIntentService extends IntentService {

    public StockWidgetIntentService() {
        super(StockWidgetIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(this,
                StockWidgetProvider.class));


        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stock);

            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());

            views.setTextViewText(R.id.tv_widget_stock_symbol, "WHAAAT!");
            views.setTextViewText(R.id.tv_widget_bid_price, date);

            widgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        //views.setContentDescription(R.id.widget_icon, description);
    }
}
