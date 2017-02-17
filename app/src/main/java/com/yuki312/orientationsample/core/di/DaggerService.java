package com.yuki312.orientationsample.core.di;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

public class DaggerService {

  public static final String SERVICE_NAME = DaggerService.class.getName();

  @SuppressWarnings("unchecked") public static <T> T getComponent(@NonNull Context context) {
    // noinspection ResourceType
    return (T) context.getSystemService(SERVICE_NAME);
  }

  public static <T extends ActivityComponentBuilder> T getComponentBuilder(
      @NonNull Activity activity) {
    return getComponentBuilder(activity, activity.getClass());
  }

  @SuppressWarnings("unchecked")
  public static <T extends ActivityComponentBuilder> T getComponentBuilder(@NonNull Context context,
      @NonNull Class<? extends Activity> activity) {
    // noinspection ResourceType
    return (T) ((AppComponent) context.getApplicationContext()
        .getSystemService(SERVICE_NAME)).activityComponentBuilders().get(activity);
  }
}
