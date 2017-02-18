package com.yuki312.orientationsample;

import com.yuki312.orientationsample.core.di.ActivityComponentBuilder;
import com.yuki312.orientationsample.core.di.ActivityMapKey;
import com.yuki312.orientationsample.main.MainActivity;
import com.yuki312.orientationsample.main.MainComponent;
import com.yuki312.orientationsample.setting.SettingActivity;
import com.yuki312.orientationsample.setting.SettingComponent;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import javax.inject.Singleton;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@Module(subcomponents = {
    MainComponent.class, SettingComponent.class
})
public abstract class ActivityBindingModule {

  @Singleton @Binds @IntoMap @ActivityMapKey(MainActivity.class)
  public abstract ActivityComponentBuilder mainActivityComponentBuilder(
      MainComponent.Builder builder);

  @Singleton @Binds @IntoMap @ActivityMapKey(SettingActivity.class)
  public abstract ActivityComponentBuilder settingActivityComponentBuilder(
      SettingComponent.Builder builder);
}
