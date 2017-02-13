package com.yuki312.orientationsample.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yuki312.orientationsample.App;
import com.yuki312.orientationsample.R;
import com.yuki312.orientationsample.databinding.ActivitySubBinding;

public class SettingActivity extends RxAppCompatActivity {

  private SettingActionCreator action;

  public static void startActivity(@NonNull Context context) {
    context.startActivity(new Intent(context, SettingActivity.class));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivitySubBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sub);

    SettingStore store = App.settingStore(this);
    store.rotate().compose(bindToLifecycle()).subscribe(binding::setRotation);

    action = App.settingActionCreator(this);
    binding.rotation.setOnCheckedChangeListener(
        (v, isChecked) -> action.changeRotateEnable(isChecked));
  }
}
