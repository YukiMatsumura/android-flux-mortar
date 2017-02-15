package com.yuki312.orientationsample.setting;

import com.yuki312.orientationsample.core.di.ScenarioScope;
import com.yuki312.orientationsample.core.flux.Dispatcher;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@ScenarioScope
@Subcomponent(modules = SettingComponent.SettingModule.class)
public interface SettingComponent {

  void inject(SettingActivity activity);

  @Module
  class SettingModule {

    private List<String> scenarioLog;

    public SettingModule() {
      scenarioLog = new ArrayList<>();
    }

    @ScenarioScope @Provides
    public SettingActionCreator provideSettingActionCreator(Dispatcher dispatcher) {
      return new SettingActionCreator(dispatcher);
    }

    @ScenarioScope @Provides public List<String> provideScenarioLog() {
      return scenarioLog;
    }
  }
}
