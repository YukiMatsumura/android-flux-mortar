package com.yuki312.orientationsample.core.di;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@Module
public abstract class ActivityModule<T extends Activity> {
  protected final T activity;

  public ActivityModule(T activity) {
    this.activity = activity;
  }

  @Provides public T provideActivity() {
    return activity;
  }
}
