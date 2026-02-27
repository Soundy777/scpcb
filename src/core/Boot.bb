Include "src/core/BuildInfo.bb"

Include "src/utils/Blitz_File_FileName.bb"
Include "src/utils/IniParser.bb"

Include "StrictLoads.bb"
Include "KeyName.bb"

Include "src/core/ErrorHandling.bb"
include "src/core/CLIParser.bb"
include "src/core/Paths.bb"
Include "src/config/Options.bb"

Include "src/console/ConsoleCore.bb"

Function BootGame()

    DebugLog "Starting Bootup Sequence"

    InitErrorHandling(GAME_VERSION$)

    InitCLI()

    InitPaths()

    LoadOptions()

    InitConsole()

    DebugLog "Bootup Sequence Complete"

End Function
