package com.yuki312.orientationsample.setting;

import android.support.annotation.NonNull;
import com.yuki312.orientationsample.core.Action;
import com.yuki312.orientationsample.core.ActionCreator;
import com.yuki312.orientationsample.core.BooleanPropertyChangeAction;
import com.yuki312.orientationsample.core.Dispatcher;
import com.yuki312.orientationsample.core.PropertyChangeAction;

/**
 * Created by Yuki312 on 2017/02/12.
 */

public class SettingActionCreator extends ActionCreator {

  public enum Id implements Action.ActionId {
    RotateChange
  }

  public SettingActionCreator(@NonNull Dispatcher dispatcher) {
    super(dispatcher);
  }

  public void changeRotateEnable(boolean enable) {
    send(new BooleanPropertyChangeAction(Id.RotateChange, enable));
  }
}
