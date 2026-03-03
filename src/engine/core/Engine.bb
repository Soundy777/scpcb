Include "src/engine/core/EngineIncludes.bb"

Function Engine_Init()

    DebugLog "Starting Bootup Sequence"

    SeedRnd MilliSecs() ; Initialize the rng seed

    AppPaths_Init()
    ErrorHandling_Init(GAME_VERSION$)
    ;; ToDo:: init Logger
    ;; ToDo:: init Time

    Config_Init()
    Config = Config_Load()

    Time_Init()
    Graphics_Init(Config\Graphics)
    Viewport_Init()
    ;; ToDo:: init AudioSystem

    ;; ToDo:: init ResourceManager
    ;; ToDo:: init InputManager

    Flags\SteamActive = SteamIntegration_Init()
    Flags\DiscordActive = DiscordIntegration_Init()
    Flags\ModsEnabled = ModManager_Init()

    Localization_Init()

    Console_Init()

    ;; ToDo:: init EventBus
    ;; ToDo:: init GameStateManager

    ;; ToDo:: Game_RegisterStates() <-- Its at this point the actual game layer has control passed to it by the Engine

    DebugLog "Completed Bootup Sequence"

End Function

Function Engine_Run()

End Function

Function Engine_Shutdown()

End Function

