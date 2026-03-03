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

    ;config\IntroEnabled
    ;config\AchievementPopups
    ;config\MoveInputCancelling
    config\UseNumericSeeds = Config_ResolveIntSetting("general", "numeric seeds")
    ;config\SpeedRunMode
    config\PlayStartupVideo = Config_ResolveIntSetting("general","play startup video") And (Not HasCLIFlag("novid"))
    config\EnableMods = Config_ResolveIntSetting("general", "enable mods")
    config\EnableSteam = Config_ResolveIntSetting("general", "enable steam")
    config\EnableDiscord = Config_ResolveIntSetting("general", "enable discord rich presence")
    ;config\LoadingScreenCyclePerCharMS

    return config

End Function