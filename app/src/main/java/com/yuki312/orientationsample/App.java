package com.yuki312.orientationsample;

import android.app.Application;
import com.yuki312.orientationsample.AppComponent.AppModule;
import com.yuki312.orientationsample.core.di.DaggerService;
import mortar.MortarScope;

/**
 * Created by Yuki312 on 2017/02/10.
 */

public class App extends Application {

  public static final String SCOPE_NAME = App.class.getName();

  private MortarScope rootScope;

  @Override public void onCreate() {
    super.onCreate();

    AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    appComponent.inject(this);
    rootScope = MortarScope.buildRootScope()
        .withService(DaggerService.SERVICE_NAME, appComponent)
        .build(App.SCOPE_NAME);
  }

  @Override public Object getSystemService(String name) {
    return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
  }
}
