package com.yuki312.orientationsample;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.yuki312.orientationsample.core.Dispatcher;
import com.yuki312.orientationsample.setting.SettingActionCreator;
import com.yuki312.orientationsample.setting.SettingStore;

/**
 * Created by Yuki312 on 2017/02/10.
 */

public class App extends Application {

  private Dispatcher dispatcher;

  private SettingActionCreator actionCreator;

  private SettingStore setting;

  @Override public void onCreate() {
    super.onCreate();

    dispatcher = new Dispatcher();
    actionCreator = new SettingActionCreator(dispatcher);
    setting = new SettingStore(getApplicationContext(), dispatcher);
  }

  // TODO: replace Singleton component with Dagger2
  public static Dispatcher dispatcher(@NonNull Context context) {
    return ((App) context.getApplicationContext()).dispatcher;
  }

  // TODO: replace Singleton component with Dagger2
  public static SettingActionCreator settingActionCreator(@NonNull Context context) {
    return ((App) context.getApplicationContext()).actionCreator;
  }

  // TODO: replace Singleton component with Dagger2
  public static SettingStore settingStore(@NonNull Context context) {
    return ((App) context.getApplicationContext()).setting;
  }
}
