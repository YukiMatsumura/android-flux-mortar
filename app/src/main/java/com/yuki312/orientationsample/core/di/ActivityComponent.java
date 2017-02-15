package com.yuki312.orientationsample.core.di;

import android.app.Activity;
import dagger.MembersInjector;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

public interface ActivityComponent<T extends Activity> extends MembersInjector<T> {
}
