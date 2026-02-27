Type AppPaths
    Field DataDir$
    Field DefaultsFile$
    Field OptionsFile$
    Field ModsFile$
End Type


Function AppPaths_Init.AppPaths()

    Local p.AppPaths = New AppPaths

    p\DataDir = ResolveDataDir()
    EnsureDirectory(p\DataDir)

    p\DefaultsFile = p\DataDir + "defaults.ini"
    p\OptionsFile = p\DataDir + "\options.ini"
    p\ModsFile = p\DataDir + "\mods.ini"

    ;; ToDo:: Remove this when we've completed the config overhaul
    PATH_FILE_OPTIONS = p\OptionsFile
    PATH_FILE_MODS = p\ModsFile
    ;; End of ToDo

    Return p

End Function

;; ToDo:: Remove this when we've completed the config overhaul
Global PATH_FILE_OPTIONS$
Const PATH_FILE_OPTIONS_DEFAULT$ = "defaults.ini"
Global PATH_FILE_MODS$
;; End of ToDo

Function ResolveDataDir$()

    Local dir$ = GetEnv("AppData") + "\Undertow Games"
    EnsureDirectory(dir)

    dir = dir + "\SCP - Containment Breach"
    EnsureDirectory(dir)

    Return dir

End Function

;; ToDo:: move this to GameConfig.bb
; The idea here is that we check if we want to restore the config to defaults via the CLI flag
Function ResolveOptionsFile$(paths.AppPaths)

    Local file$ = paths\DataDir + "\options.ini"

	If FileType(file) <> 1 Lor HasCLIFlag("defaults") Lor HasCLIFlag("default") Then
		Local f% = WriteFile(file)
		CloseFile(f)
	EndIf

	Return file

End Function