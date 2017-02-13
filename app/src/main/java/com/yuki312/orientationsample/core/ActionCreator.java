package com.yuki312.orientationsample.core;

import android.support.annotation.NonNull;

/**
 * Created by Yuki312 on 2017/02/12.
 */

public abstract class ActionCreator {

  private Dispatcher dispatcher;

  public ActionCreator(@NonNull Dispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  protected void send(@NonNull Action action) {
    dispatcher.dispatch(action);
  }
}
