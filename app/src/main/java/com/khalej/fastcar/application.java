package com.khalej.fastcar;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class application extends Application {
    @Override
    public void onCreate() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        super.onCreate();
    }

}
