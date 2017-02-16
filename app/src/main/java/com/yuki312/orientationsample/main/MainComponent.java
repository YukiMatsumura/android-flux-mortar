package com.yuki312.orientationsample.main;

import com.yuki312.orientationsample.core.di.ActivityComponent;
import com.yuki312.orientationsample.core.di.ActivityComponentBuilder;
import com.yuki312.orientationsample.core.di.ActivityModule;
import com.yuki312.orientationsample.core.di.ScenarioScope;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@ScenarioScope
@Subcomponent(modules = MainComponent.MainModule.class)
public interface MainComponent extends ActivityComponent<MainActivity> {

  @Subcomponent.Builder
  interface Builder {
    Builder activityModule(MainModule module);

    MainComponent build();
  }

  @Module
  class MainModule extends ActivityModule {

    @ScenarioScope @Provides
    public MainComponent.Builder mainActivityComponentBuilder(MainComponent.Builder builder) {
      return builder;
    }

    private List<String> scenarioLog;

    public MainModule() {
      scenarioLog = new ArrayList<>();
    }

    @ScenarioScope @Provides public List<String> provideScenarioLog() {
      return scenarioLog;
    }
  }
}
