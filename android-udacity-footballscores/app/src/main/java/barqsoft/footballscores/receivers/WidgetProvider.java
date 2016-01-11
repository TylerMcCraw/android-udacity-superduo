package barqsoft.footballscores.receivers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.R;
import barqsoft.footballscores.services.WidgetService;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent onClickPendingIntent = PendingIntent
                    .getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews rv = initViews(context, appWidgetManager, widgetId);
            rv.setPendingIntentTemplate(R.id.widget_list, onClickPendingIntent);
            appWidgetManager.updateAppWidget(widgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews initViews(Context context, AppWidgetManager widgetManager, int widgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        rv.setRemoteAdapter(R.id.widget_list, intent);

        rv.setEmptyView(R.id.item_container, R.id.empty_view);

        return rv;
    }

}