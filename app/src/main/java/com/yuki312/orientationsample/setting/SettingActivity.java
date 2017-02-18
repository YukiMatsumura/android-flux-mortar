package com.yuki312.orientationsample.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yuki312.orientationsample.R;
import com.yuki312.orientationsample.core.di.DaggerService;
import com.yuki312.orientationsample.databinding.ActivitySubBinding;
import com.yuki312.orientationsample.setting.SettingComponent.SettingModule;
import java.util.List;
import javax.inject.Inject;
import mortar.MortarScope;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

public class SettingActivity extends RxAppCompatActivity {

  public static final String SCOPE_NAME = SettingActivity.class.getName();

  @Inject SettingActionCreator settingAction;
  @Inject SettingStore settingStore;
  @Inject List<String> log;

  public static void startActivity(@NonNull Context context) {
    context.startActivity(new Intent(context, SettingActivity.class));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivitySubBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sub);

    // DI
    DaggerService.<SettingComponent>getComponent(this).injectMembers(this);

    settingStore.rotate().compose(bindToLifecycle()).subscribe(binding::setRotation);

    binding.rotation.setOnCheckedChangeListener(
        (v, isChecked) -> settingAction.changeRotateEnable(isChecked));

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
      SettingComponent settingComponent =
          DaggerService.<SettingComponent.Builder>getComponentBuilder(this).activityModule(
              new SettingModule(this)).build();
      scenarioScope = buildChild(getApplicationContext()).withService(DaggerService.SERVICE_NAME,
          settingComponent).build(SCOPE_NAME);
    }

    return scenarioScope.hasService(name) ? scenarioScope.getService(name)
        : super.getSystemService(name);
  }
}
