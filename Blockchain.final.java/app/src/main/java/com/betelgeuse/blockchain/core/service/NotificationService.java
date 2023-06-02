package com.betelgeuse.blockchain.core.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.betelgeuse.blockchain.data.firebase.TickerDB;
import com.betelgeuse.blockchain.data.output.DatabaseManager;
import com.betelgeuse.blockchain.data.sqLite.UserOptionDB;

public class NotificationService extends Service {
    DatabaseManager databaseManager ;
    @Override
    public void onCreate() {
        super.onCreate();
        databaseManager = new DatabaseManager(new TickerDB(),new UserOptionDB(getApplicationContext()),null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }
}
