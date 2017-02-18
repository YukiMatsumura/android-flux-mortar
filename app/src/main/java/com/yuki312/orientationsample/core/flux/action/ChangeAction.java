package com.yuki312.orientationsample.core.flux.action;

/**
 * Created by Yuki312 on 2017/02/11.
 */

public abstract class ChangeAction<T> extends Action {

  public final T value;

  public ChangeAction(ActionId id, T value) {
    super(id);
    this.value = value;
  }
}
