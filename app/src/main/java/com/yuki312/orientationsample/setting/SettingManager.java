package com.yuki312.orientationsample.setting;

import android.content.Context;
import android.support.annotation.NonNull;
import com.yuki312.orientationsample.util.Objects;

/**
 * Created by Yuki312 on 2017/02/19.
 */

public class SettingManager {

  private final Setting repo;

  public SettingManager(@NonNull Context context, @NonNull Setting repo) {
    this.repo = Objects.nonNull(repo);
  }


}
