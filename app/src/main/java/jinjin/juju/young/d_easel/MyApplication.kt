package jinjin.juju.young.d_easel

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // myrealm.realm 파일
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("deasel.realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)


    }
}
