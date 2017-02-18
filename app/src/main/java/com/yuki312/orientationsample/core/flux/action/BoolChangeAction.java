package com.yuki312.orientationsample.core.flux.action;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by Yuki312 on 2017/02/13.
 */

public class BoolChangeAction extends ChangeAction<Boolean> {

  public BoolChangeAction(@NonNull ActionId id, boolean value, @NonNull Bundle bundle,
      @NonNull String tag) {
    super(id, value, bundle, tag);
  }

  public static class Builder extends ChangeAction.Builder<BoolChangeAction, Boolean> {
    @Override public BoolChangeAction build() {
      return new BoolChangeAction(id, value, bundle, tag);
    }
  }
}
