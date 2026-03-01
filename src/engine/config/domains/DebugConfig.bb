Type DebugConfig
    Field ShowMapGen%
    Field ShowForestGen%
    Field DebugResourcePacks%
End Type

Function DebugConfig_Load.DebugConfig()

    Local config.DebugConfig = New DebugConfig

    config\DebugResourcePacks = Config_ResolveIntSetting("debug", "resource pack strict load")

    Return config

End Function