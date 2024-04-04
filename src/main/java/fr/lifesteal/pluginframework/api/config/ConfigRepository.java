package fr.lifesteal.pluginframework.api.config;

public interface ConfigRepository {
    <T> T getConfigValue(String key, T defaultValue);
}
