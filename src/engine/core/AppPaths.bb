Type AppPaths
    Field DataDir$
    Field DefaultsFile$
    Field OptionsFile$
    Field ModsFile$
    Field StringsFile$
End Type

Global Paths.AppPaths

Function AppPaths_Init()

    Paths = New AppPaths

    Paths\DataDir = ResolveDataDir()
    EnsureDirectory(Paths\DataDir)

    Paths\DefaultsFile = "defaults.ini"
    Paths\OptionsFile = Paths\DataDir + "\options.ini"
    Paths\ModsFile = Paths\DataDir + "\mods.ini"
    Paths\StringsFile = "Data\strings.ini" ; "Data" here refers to the local Data folder of the game files & not the "DataDir" which is in %AppData%

End Function

Function ResolveDataDir$()

    Local dir$ = GetEnv("AppData") + "\Undertow Games"
    EnsureDirectory(dir)

    dir = dir + "\SCP - Containment Breach"
    EnsureDirectory(dir)

    Return dir

End Function