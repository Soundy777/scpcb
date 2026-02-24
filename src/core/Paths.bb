Global gDataDir$
Global gOptionsFile$
Global gModsFile$

Const OPTION_DEFAULT_FILE$ = "defaults.ini"

Function InitPaths()

    gDataDir = ResolveDataDir()
    gOptionsFile = ResolveOptionsFile()
    gModsFile = gDataDir + "\mods.ini"

End Function

Function ResolveDataDir$()

    Local dir$ = GetEnv("AppData") + "\Undertow Games"
    If FileType(dir) <> 2 Then CreateDir(dir)

    dir = dir + "\SCP - Containment Breach"
    If FileType(dir) <> 2 Then CreateDir(dir)

    Return dir

End Function

Function ResolveOptionsFile$()

    Local file$ = gDataDir + "\options.ini"

	If FileType(file) <> 1 Lor HasCLIFlag("defaults") Lor HasCLIFlag("default") Then
		Local f% = WriteFile(file)
		CloseFile(f)
	EndIf

	Return file

End Function