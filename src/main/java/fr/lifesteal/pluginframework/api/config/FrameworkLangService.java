package fr.lifesteal.pluginframework.api.config;

public interface FrameworkLangService extends ConfigService {
    String getDefaultErrorMessage();
    String getCommandDisabledErrorMessage();
    String getCommandNoPermissionErrorMessage();

}
