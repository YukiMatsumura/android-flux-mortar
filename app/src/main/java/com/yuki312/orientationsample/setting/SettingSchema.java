package com.yuki312.orientationsample.setting;

import com.rejasupotaro.android.kvs.annotations.Key;
import com.rejasupotaro.android.kvs.annotations.Table;

@Table(name = "setting")
public class SettingSchema {

  public static final String ROTATE_KEY = "rotation";

  @Key(name = ROTATE_KEY) boolean rotation = false;
}
