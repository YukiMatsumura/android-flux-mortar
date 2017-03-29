package com.yuki312.orientationsample;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import com.yuki312.orientationsample.core.di.ActivityComponentBuilder;
import com.yuki312.orientationsample.core.flux.Dispatcher;
import com.yuki312.orientationsample.setting.SettingStore;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import java.util.Map;
import javax.inject.Singleton;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@Singleton
@Component(modules = { AppComponent.AppModule.class, ActivityBindingModule.class })
public interface AppComponent {

  App application();

  void inject(App app);

  Map<Class<? extends Activity>, ActivityComponentBuilder> activityComponentBuilders();

  @Module
  class AppModule {

    private App app;

    public AppModule(@NonNull App app) {
      this.app = app;
    }

    @Singleton @Provides public Dispatcher provideDispatcher() {
      return new Dispatcher();
    }

    @Singleton @Provides public App provideApp() {
      return app;
    }

    @Singleton @Provides public Context provideApplicationContext() {
      return app.getApplicationContext();
    }
  }
}
