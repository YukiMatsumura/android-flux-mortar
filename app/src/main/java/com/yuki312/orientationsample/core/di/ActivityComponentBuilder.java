package com.yuki312.orientationsample.core.di;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

public interface ActivityComponentBuilder<M extends ActivityModule, C extends ActivityComponent> {
  ActivityComponentBuilder<M, C> activityModule(M activityModule);

  C build();
}
