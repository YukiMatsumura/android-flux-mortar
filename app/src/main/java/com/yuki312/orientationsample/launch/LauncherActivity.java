package com.yuki312.orientationsample.launch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.yuki312.orientationsample.R;
import com.yuki312.orientationsample.main.MainActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

// Splash screen activity.
public class LauncherActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);

    // Expression of heavy processing by timer,
    // such as application initialization processing.
    Observable.timer(3000, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(t -> {
          MainActivity.startActivity(this);
        });
  }
}
