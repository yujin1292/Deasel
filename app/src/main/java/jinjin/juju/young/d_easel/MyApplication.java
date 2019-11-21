package jinjin.juju.young.d_easel;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // myrealm.realm 파일
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("deasel.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);


    }
}
