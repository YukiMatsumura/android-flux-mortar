package com.yuki312.orientationsample.core;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Yuki312 on 2017/02/10.
 */

public class Dispatcher {

  private final Subject<Action> actionStream = BehaviorSubject.<Action>create().toSerialized();

  public Dispatcher() {
  }

  public void dispatch(Action action) {
    actionStream.onNext(action);
  }

  public Observable<Action> stream() {
    return actionStream.hide();
  }
}
