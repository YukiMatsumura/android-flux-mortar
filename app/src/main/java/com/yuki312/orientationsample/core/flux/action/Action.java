package com.yuki312.orientationsample.core.flux.action;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.yuki312.orientationsample.util.Objects;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Yuki312 on 2017/02/12.
 */

public class Action {

  @NonNull private final ActionId id;
  @NonNull private final Object param;

  public Action(@NonNull ActionId id, @NonNull Object param) {
    this.id = Objects.nonNull(id);
    this.param = Objects.nonNull(param);
  }

  @NonNull public ActionId id() {
    return id;
  }

  @NonNull public Object param() {
    return param;
  }
}
