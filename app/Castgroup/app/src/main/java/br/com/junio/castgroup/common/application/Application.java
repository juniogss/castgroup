package br.com.junio.castgroup.common.application;

import com.facebook.stetho.Stetho;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
