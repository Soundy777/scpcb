Type ConsoleConfig
    Field ConsoleEnabled%
    Field ConsoleAutoOpen%
End Type

Function ConsoleConfig_Load.ConsoleConfig()

    Local config.ConsoleConfig = New ConsoleConfig

    config\ConsoleEnabled = Config_ResolveIntSetting("console", "enabled")
    config\ConsoleAutoOpen = Config_ResolveIntSetting("console", "auto opening")

    return config
    
End Function