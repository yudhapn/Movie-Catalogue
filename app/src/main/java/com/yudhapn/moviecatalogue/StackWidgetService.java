package com.yudhapn.moviecatalogue;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.yudhapn.moviecatalogue.adapter.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}