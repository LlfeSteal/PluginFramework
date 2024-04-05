package fr.lifesteal.pluginframework.api.config;

public interface FrameworkLangService extends ConfigService {
    String getCommandDisabledErrorMessage();
    String getCommandNoPermissionErrorMessage();

}
