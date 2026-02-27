Include "src/core/BuildInfo.bb"
Include "src/infastructure/FileSystem.bb"
include "src/core/AppPaths.bb"

Include "src/utils/Blitz_File_FileName.bb"
Include "src/utils/IniParser.bb"

Include "StrictLoads.bb"
Include "KeyName.bb"

Include "src/core/ErrorHandling.bb"
include "src/core/CLIParser.bb"
Include "src/config/Options.bb"

Include "src/console/ConsoleCore.bb"

Function BootGame()

    DebugLog "Starting Bootup Sequence"

    Local paths.AppPaths = AppPaths_Init()
    InitErrorHandling(GAME_VERSION$)

    ;; ToDo:: replace these with the game config overhaul
    InitCLI()
    LoadOptions()
    ;; End of todo

    InitConsole()

    DebugLog "Bootup Sequence Complete"

End Function
