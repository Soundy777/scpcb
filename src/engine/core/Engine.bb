Include "src/engine/core/BuildInfo.bb"
Include "src/engine/core/FeatureFlags.bb"

Include "src/engine/infastructure/FileSystem.bb"
Include "src/engine/infastructure/IniParser.bb"
Include "src/engine/infastructure/Blitz_File_FileName.bb"
Include "src/engine/infastructure/ErrorHandling.bb"

include "src/engine/core/AppPaths.bb"

Include "StrictLoads.bb"    ;; ToDo:: this will become part of our resource management system
Include "KeyName.bb"        ;; ToDo:: this will become part of our input management system

Include "src/engine/config/ConfigManager.bb"
Include "src/engine/core/Time.bb"
Include "src/engine/graphics/Graphics.bb"
Include "src/engine/graphics/Viewport.bb"

Include "src/engine/integrations/SteamIntegration.bb"
Include "src/engine/integrations/DiscordIntegration.bb"

Include "src/engine/modding/ModManager.bb"
Include "src/engine/localization/Localization.bb"

Include "src/engine/console/ConsoleCore.bb"

Include "src/engine/effects/DevilParticleSystem.bb"

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

