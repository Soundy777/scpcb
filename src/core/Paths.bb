Global Paths_DataDir$
Global Paths_OptionsFile$
Global Paths_ModsFile$

Const OPTION_DEFAULT_FILE$ = "defaults.ini"

Function InitPaths()

    Paths_DataDir = ResolveDataDir()
    Paths_OptionsFile = ResolveOptionsFile()
    Paths_ModsFile = Paths_DataDir + "\mods.ini"

End Function

Function ResolveDataDir$()

    Local dir$ = GetEnv("AppData") + "\Undertow Games"
    If FileType(dir) <> 2 Then CreateDir(dir)

    dir = dir + "\SCP - Containment Breach"
    If FileType(dir) <> 2 Then CreateDir(dir)

    Return dir

End Function

Function ResolveOptionsFile$()

    Local file$ = Paths_DataDir + "\options.ini"

	If FileType(file) <> 1 Lor HasCLIFlag("defaults") Lor HasCLIFlag("default") Then
		Local f% = WriteFile(file)
		CloseFile(f)
	EndIf

	Return file

End Function