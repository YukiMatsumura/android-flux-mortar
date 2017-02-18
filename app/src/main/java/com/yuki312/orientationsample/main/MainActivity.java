package com.yuki312.orientationsample.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yuki312.orientationsample.R;
import com.yuki312.orientationsample.core.di.DaggerService;
import com.yuki312.orientationsample.databinding.ActivityMainBinding;
import com.yuki312.orientationsample.main.MainComponent.MainModule;
import com.yuki312.orientationsample.setting.SettingActivity;
import com.yuki312.orientationsample.setting.SettingStore;
import java.util.List;
import javax.inject.Inject;
import mortar.MortarScope;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

public class MainActivity extends RxAppCompatActivity {

  public static final String SCOPE_NAME = MainActivity.class.getName();

  @Inject SettingStore settingStore;
  @Inject List<String> log;

  public static void startActivity(@NonNull Context context) {
    context.startActivity(new Intent(context, MainActivity.class));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.button.setOnClickListener(v -> SettingActivity.startActivity(this));

    // DI
    DaggerService.<MainComponent>getComponent(this).injectMembers(this);

    // 画面回転の設定値が変更されたらそれに従ったOrientation値をリクエストする.
    settingStore.rotate().compose(bindToLifecycle()).subscribe(on -> {
      setRequestedOrientation(on ? ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
          : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    });

    log.add(this.toString());
    Toast.makeText(this, "log=" + log.size() + " fm." + this, Toast.LENGTH_SHORT).show();
  }

  @Override protected void onDestroy() {
    if (isFinishing()) {
      MortarScope activityScope = findChild(getApplicationContext(), SCOPE_NAME);
      if (activityScope != null) activityScope.destroy();
    }
    super.onDestroy();
  }

  @Override public Object getSystemService(@NonNull String name) {
    MortarScope scenarioScope = findChild(getApplicationContext(), SCOPE_NAME);
    if (scenarioScope == null) {
      MainComponent mainComponent =
          DaggerService.<MainComponent.Builder>getComponentBuilder(this).activityModule(
              new MainModule(this)).build();
      scenarioScope =
          buildChild(getApplicationContext()).withService(DaggerService.SERVICE_NAME, mainComponent)
              .build(SCOPE_NAME);
    }

    return scenarioScope.hasService(name) ? scenarioScope.getService(name)
        : super.getSystemService(name);
  }
}
