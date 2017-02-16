package com.yuki312.orientationsample.core.di;

import com.yuki312.orientationsample.main.MainComponent;
import com.yuki312.orientationsample.setting.SettingComponent;
import dagger.Module;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@Module(subcomponents = {
    MainComponent.class, SettingComponent.class
})
public abstract class ActivityBindingModule {
}
