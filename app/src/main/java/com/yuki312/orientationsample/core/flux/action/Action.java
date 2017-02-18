package com.yuki312.orientationsample.core.flux.action;

/**
 * Created by Yuki312 on 2017/02/12.
 */

public abstract class Action {

  public interface ActionId {
  }

  public final ActionId id;
  public String tag;

  public Action(ActionId actionId) {
    this(actionId, "");
  }

  public Action(ActionId actionId, String tag) {
    this.id = actionId;
    this.tag = tag;
  }
}