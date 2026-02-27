Include "src/core/BuildInfo.bb"

Include "src/infastructure/FileSystem.bb"
Include "src/infastructure/IniParser.bb"
include "src/infastructure/CLIParser.bb"
Include "src/infastructure/Blitz_File_FileName.bb"
Include "src/infastructure/ErrorHandling.bb"

include "src/core/AppPaths.bb"

Include "StrictLoads.bb"    ;; ToDo:: this will become part of our resource management system
Include "KeyName.bb"        ;; ToDo:: this will become part of our input management system

;; ToDo:: this will be reworked into our config management system
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
