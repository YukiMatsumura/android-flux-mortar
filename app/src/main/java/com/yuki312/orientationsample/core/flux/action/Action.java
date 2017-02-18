package com.yuki312.orientationsample.core.flux.action;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.yuki312.orientationsample.util.Objects;

/**
 * Created by Yuki312 on 2017/02/12.
 */

public abstract class Action {

  public interface ActionId {
  }

  @NonNull public final ActionId id;
  @NonNull public final Bundle bundle;
  @NonNull public final String tag;

  public Action(@NonNull ActionId actionId, @Nullable Bundle bundle, @Nullable String tag) {
    this.id = Objects.nonNull(actionId);
    this.bundle = Objects.nullValue(bundle, Bundle.EMPTY);
    this.tag = Objects.nullValue(tag, "");
  }
}
