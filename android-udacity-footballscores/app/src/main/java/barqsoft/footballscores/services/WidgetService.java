package barqsoft.footballscores.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.adapters.WidgetDataProvider;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(getApplicationContext(), intent);
    }
}