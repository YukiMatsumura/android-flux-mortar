package com.yuki312.orientationsample.core.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yuki312.orientationsample.main.MainActivity;
import com.yuki312.orientationsample.main.MainComponent;
import com.yuki312.orientationsample.setting.SettingActivity;
import com.yuki312.orientationsample.setting.SettingComponent;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

public class DaggerService {

  public static final String SERVICE_NAME = DaggerService.class.getName();

  @SuppressWarnings("unchecked") public static <T> T getComponent(@NonNull Context context) {
    // noinspection ResourceType
    return (T) context.getSystemService(SERVICE_NAME);
  }

  @SuppressWarnings("unchecked")
  public static MainComponent.Builder getComponentBuilder(@NonNull MainActivity activity) {
    // noinspection ResourceType
    return ((AppComponent) activity.getApplicationContext()
        .getSystemService(SERVICE_NAME)).mainComponentBuilder();
  }

  @SuppressWarnings("unchecked")
  public static SettingComponent.Builder getComponentBuilder(@NonNull SettingActivity activity) {
    // noinspection ResourceType
    return ((AppComponent) activity.getApplicationContext()
        .getSystemService(SERVICE_NAME)).settingComponentBuilder();
  }
}
