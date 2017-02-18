package com.yuki312.orientationsample.core.flux.action;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by Yuki312 on 2017/02/11.
 */

public abstract class ChangeAction<T> extends Action {

  public final T value;

  public ChangeAction(@NonNull ActionId id, T value, Bundle bundle, String tag) {
    super(id, bundle, tag);
    this.value = value;
  }

  public static abstract class Builder<A extends ChangeAction<T>, T> {
    protected ActionId id;
    protected T value;
    protected Bundle bundle;
    protected String tag;

    public Builder<A, T> setId(ActionId id) {
      this.id = id;
      return this;
    }

    public Builder<A, T> setValue(T value) {
      this.value = value;
      return this;
    }

    public Builder<A, T> setBundle(Bundle bundle) {
      this.bundle = bundle;
      return this;
    }

    public Builder<A, T> setTag(String tag) {
      this.tag = tag;
      return this;
    }

    public abstract A build();
  }
}
