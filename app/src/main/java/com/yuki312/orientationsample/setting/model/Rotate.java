package com.yuki312.orientationsample.setting.model;

import com.yuki312.orientationsample.core.flux.action.ActionId;

/**
 * Created by Yuki312 on 2017/02/11.
 */

public class Rotate {

  public enum Action implements ActionId {
    ChangeRotateMode
  }

  public enum Param {
    RotateEnable
  }

  public static final boolean DEFAULT_VALUE = false;

  public Rotate() {
  }
}
