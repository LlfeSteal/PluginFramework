package fr.lifesteal.pluginframework.core.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigParam {
    String paramKey();
}
