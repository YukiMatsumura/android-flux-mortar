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
  @NonNull private Map<Enum, Object> param;

  public enum Debug {
    Trace
  }

  public Action(@NonNull ActionId id) {
    this.id = Objects.nonNull(id);
    this.param = new LinkedHashMap<>();
  }

  @NonNull public ActionId id() {
    return id;
  }

  public Action putValue(@NonNull Enum k, @Nullable Object v) {
    param.put(Objects.nonNull(k), v);
    return this;
  }

  public Object get(@NonNull Enum k) {
    return param.get(k);
  }

  public Object get(@NonNull Enum k, Object defaultValue) {
    Object o;
    return ((o = param.get(k)) != null || param.containsKey(k)) ? o : defaultValue;
  }

  // for debug only
  public Action track(String tag) {
    putValue(Debug.Trace, new Throwable("[DEBUG TRACE]" + tag).fillInStackTrace());
    return this;
  }
}
