Include "src/core/BuildInfo.bb"

Include "StrictLoads.bb"
Include "KeyName.bb"

Include "src/core/ErrorHandling.bb"
include "src/core/CLI.bb"
include "src/core/Paths.bb"
Include "src/config/Options.bb"

Function BootGame()

    DebugLog "Starting Bootup Sequence"

    InitErrorHandling(GAME_VERSION$)

    InitCLI()

    InitPaths()
    
    LoadOptions()

    DebugLog "Bootup Sequence Complete"

End Function
