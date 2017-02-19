package com.yuki312.orientationsample.setting;

import android.support.annotation.NonNull;
import com.yuki312.orientationsample.core.flux.action.Action;
import com.yuki312.orientationsample.core.flux.action.ActionCreator;
import com.yuki312.orientationsample.core.flux.Dispatcher;
import com.yuki312.orientationsample.setting.model.Rotate;

/**
 * Created by Yuki312 on 2017/02/12.
 */

public class SettingActionCreator extends ActionCreator {

  public SettingActionCreator(@NonNull Dispatcher dispatcher) {
    super(dispatcher);
  }

  public void changeRotateEnable(boolean enable) {
    send(new Action(Rotate.Action.ChangeRotateMode).putValue(Rotate.Param.RotateEnable, enable));
  }
}
