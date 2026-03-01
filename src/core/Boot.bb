Include "src/core/BuildInfo.bb"

Include "src/infastructure/FileSystem.bb"
Include "src/infastructure/IniParser.bb"
Include "src/infastructure/Blitz_File_FileName.bb"
Include "src/infastructure/ErrorHandling.bb"

include "src/core/AppPaths.bb"

Include "StrictLoads.bb"    ;; ToDo:: this will become part of our resource management system
Include "KeyName.bb"        ;; ToDo:: this will become part of our input management system

Include "src/config/ConfigManager.bb"

Include "src/console/ConsoleCore.bb"

Function BootGame()

    DebugLog "Starting Bootup Sequence"

    AppPaths_Init()
    ErrorHandling_Init(GAME_VERSION$)
    Config_Init()
    Console_Init()

    DebugLog "Bootup Sequence Complete"

End Function
