Type GameplayConfig
    Field IntroEnabled%
    Field AchievementPopups%
    Field MoveInputCancelling%
    Field UseNumericSeeds%
    Field SpeedRunMode%
    Field PlayStartupVideo%
    Field EnableMods%
    Field EnableSteam%
    Field EnableDiscord%
    Field LoadingScreenCyclePerCharMS%
End Type

Function GameplayConfig_Load.GameplayConfig()

    Local config.GameplayConfig = New GameplayConfig

    config\EnableSteam = Config_ResolveIntSetting("general", "enable steam")
    config\EnableDiscord = Config_ResolveIntSetting("general", "enable discord rich presence")
    config\EnableMods = Config_ResolveIntSetting("general", "enable mods")

    return config

End Function