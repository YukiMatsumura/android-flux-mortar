package com.yuki312.orientationsample.setting;

import android.support.annotation.NonNull;
import com.yuki312.orientationsample.core.flux.action.Action;
import com.yuki312.orientationsample.core.flux.action.ActionCreator;
import com.yuki312.orientationsample.core.flux.Dispatcher;
import com.yuki312.orientationsample.core.flux.action.ActionId;

/**
 * Created by Yuki312 on 2017/02/12.
 */

public class SettingActions extends ActionCreator {

  public enum Id implements ActionId {
    ChangeLandscapeMode
  }

  public SettingActions(@NonNull Dispatcher dispatcher) {
    super(dispatcher);
  }

  public void setLandscapeMode(boolean enable) {
    send(new Action(Id.ChangeLandscapeMode, enable));
  }
}
