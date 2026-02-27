Global PATH_DIR_DATA$
Global PATH_FILE_OPTIONS$
Const PATH_FILE_OPTIONS_DEFAULT$ = "defaults.ini"
Global PATH_FILE_MODS$

Function InitPaths()

    PATH_DIR_DATA = ResolveDataDir()
    PATH_FILE_OPTIONS = ResolveOptionsFile()
    PATH_FILE_MODS = PATH_DIR_DATA + "\mods.ini"

End Function

Function ResolveDataDir$()

    Local dir$ = GetEnv("AppData") + "\Undertow Games"
    If FileType(dir) <> 2 Then CreateDir(dir)

    dir = dir + "\SCP - Containment Breach"
    If FileType(dir) <> 2 Then CreateDir(dir)

    Return dir

End Function

Function ResolveOptionsFile$()

    Local file$ = PATH_DIR_DATA + "\options.ini"

	If FileType(file) <> 1 Lor HasCLIFlag("defaults") Lor HasCLIFlag("default") Then
		Local f% = WriteFile(file)
		CloseFile(f)
	EndIf

	Return file

End Function