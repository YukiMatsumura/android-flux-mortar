package com.yuki312.orientationsample.core;

import com.yuki312.orientationsample.core.Action;

/**
 * Created by Yuki312 on 2017/02/11.
 */

public abstract class PropertyChangeAction<T> extends Action {

  public final T value;

  public PropertyChangeAction(ActionId id, T value) {
    super(id);
    this.value = value;
  }
}
