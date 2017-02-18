package com.yuki312.orientationsample.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Yuki312 on 2017/02/18.
 */

public class Objects {

  public static <T> T nonNull(T o) {
    if (o == null) {
      throw new NullPointerException("Require Non null object");
    }
    return o;
  }

  public static <T> T nullValue(@Nullable T o, @NonNull T nullValue) {
    nonNull(nullValue);
    if (o == null) {
      return nullValue;
    }
    return o;
  }
}
