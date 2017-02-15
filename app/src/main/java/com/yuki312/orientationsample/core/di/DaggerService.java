package com.yuki312.orientationsample.core.di;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

public class DaggerService {

  public static final String SERVICE_NAME = DaggerService.class.getName();

  @SuppressWarnings("unchecked") public static <T> T getDaggerComponent(@NonNull Context context) {
    // noinspection ResourceType
    return (T) context.getSystemService(SERVICE_NAME);
  }
}
