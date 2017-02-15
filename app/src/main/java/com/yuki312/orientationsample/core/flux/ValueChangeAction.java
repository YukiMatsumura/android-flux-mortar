package com.yuki312.orientationsample.core.flux;

/**
 * Created by Yuki312 on 2017/02/11.
 */

public abstract class ValueChangeAction<T> extends Action {

  public final T value;

  public ValueChangeAction(ActionId id, T value) {
    super(id);
    this.value = value;
  }
}
