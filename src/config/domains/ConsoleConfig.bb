Type ConsoleConfig
    Field ConsoleEnabled%
    Field ConsoleAutoOpen%
End Type

Function ConsoleConfig_Load.ConsoleConfig()
    Local config.ConsoleConfig = New ConsoleConfig

    config\ConsoleEnabled = ResolveIntSetting("console", "enabled")

    return config
End Function