package com.yuki312.orientationsample.setting;

import android.content.Context;
import android.support.annotation.NonNull;
import com.yuki312.orientationsample.core.flux.Dispatcher;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import javax.inject.Inject;
import javax.inject.Singleton;

import static com.yuki312.orientationsample.setting.SettingActions.Id.ChangeLandscapeMode;

/**
 * Created by Yuki312 on 2017/02/10.
 */
@Singleton
public class SettingStore {

  private final CompositeDisposable compositeDisposable;
  private final Setting setting;

  private final Subject<Boolean> rotate = BehaviorSubject.<Boolean>create().toSerialized();

  @Inject public SettingStore(@NonNull Context context, @NonNull Dispatcher dispatcher) {
    this.compositeDisposable = new CompositeDisposable();
    this.setting = Setting.get(context.getApplicationContext());

    // init properties
    rotate.onNext(setting.getRotation());

    Disposable disposable = dispatcher.stream()
        .filter(a -> ChangeLandscapeMode == a.id())
        .map(a -> (boolean) a.param())
        .subscribe(rotate::onNext);
    compositeDisposable.add(disposable);
  }

  @NonNull public Observable<Boolean> rotate() {
    return rotate.hide();
  }

  public void dispose() {
    if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
  }
}
