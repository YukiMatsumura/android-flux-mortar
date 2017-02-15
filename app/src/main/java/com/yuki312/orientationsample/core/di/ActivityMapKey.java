package com.yuki312.orientationsample.core.di;

import android.app.Activity;
import dagger.MapKey;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@MapKey
public @interface ActivityMapKey {
  Class<? extends Activity> value();
}
