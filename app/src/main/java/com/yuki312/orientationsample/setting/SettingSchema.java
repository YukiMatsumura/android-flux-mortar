package com.yuki312.orientationsample.setting;

import com.rejasupotaro.android.kvs.annotations.Key;
import com.rejasupotaro.android.kvs.annotations.Table;
import com.yuki312.orientationsample.setting.model.Rotate;

@Table(name = "setting")
public class SettingSchema {

  public static final String ROTATE_KEY = "rotation";

  @Key(name = ROTATE_KEY) boolean rotation = Rotate.DEFAULT_VALUE;
}
