package com.yuki312.orientationsample.core.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

/**
 * Created by YukiMatsumura on 2017/02/15.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ScenarioScope {
}
