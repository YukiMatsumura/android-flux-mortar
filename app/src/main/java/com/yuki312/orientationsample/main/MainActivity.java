package com.yuki312.orientationsample.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yuki312.orientationsample.App;
import com.yuki312.orientationsample.R;
import com.yuki312.orientationsample.databinding.ActivityMainBinding;
import com.yuki312.orientationsample.setting.SettingActivity;

public class MainActivity extends RxAppCompatActivity {

  public static void startActivity(@NonNull Context context) {
    context.startActivity(new Intent(context, MainActivity.class));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e("TEST", "onCreate");

    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.button.setOnClickListener(v -> SettingActivity.startActivity(this));

    App.settingStore(this).rotate().compose(bindToLifecycle()).subscribe(on -> {
      setRequestedOrientation(on ? ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
          : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    });
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.e("TEST", "onConfigurationChanged");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Log.e("TEST", "onDestroy");
  }
}
