Type LauncherConfig
    Field LauncherWidth%
    Field LauncherHeight%
    Field LauncherEnabled%
End Type

Function LauncherConfig_Load.LauncherConfig()

    Local config.LauncherConfig = New LauncherConfig

    config\LauncherWidth = Min(Config_ResolveIntSetting("launcher", "launcher width"), 1024)
    config\LauncherHeight = Min(Config_ResolveIntSetting("launcher", "launcher height"), 768)
    config\LauncherEnabled = Config_ResolveIntSetting("launcher", "launcher enabled")

    return config

End Function