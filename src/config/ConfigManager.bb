; ===========================================================================
; ConfigManager.bb
; ===========================================================================
; Handles saving, loading & providing game configuration data
; ===========================================================================
Include "src/config/GameConfig.bb"

Include "src/config/domains/AudioConfig.bb"
Include "src/config/domains/ConsoleConfig.bb"
Include "src/config/domains/ControlsConfig.bb"
Include "src/config/domains/DebugConfig.bb"
Include "src/config/domains/GameplayConfig.bb"
Include "src/config/domains/GraphicsConfig.bb"
Include "src/config/domains/Keybinds.bb"
Include "src/config/domains/LauncherConfig.bb"

Include "src/config/sources/CLIParser.bb"
Include "src/config/sources/OptionsParser.bb"
; ===========================================================================

;; ToDo:: eventually refactor this out to rely soley on a dependency injection pattern instead of a global
Global Config.GameConfig

; ---------------------------------------------------------------------------
; Temp Globals (to eventually refactor)
; ---------------------------------------------------------------------------

; ---------------------------------------------------------------------------
; Public API
; ---------------------------------------------------------------------------

Function Config_Init()

    Config_ResolveOptionsFile()

    Config = Config_Load()

End Function

Function Config_Load.GameConfig()

    local config.GameConfig = new GameConfig

    config\Audio = AudioConfig_Load()
    config\Console = ConsoleConfig_Load()

    Return config

End Function

; ---------------------------------------------------------------------------
; Settings resolution logic
; ---------------------------------------------------------------------------

Function ResolveIntSetting%(section$, key$)

    value = GetOptionInt(section, key)

    Local cliValue% = GetCLIInt(key, -1)

    If cliValue <> -1 Then value = cliValue

    Return value

End Function

Function ResolveFloatSetting#(section$, key$)

    Return GetOptionFloat(section, key)

End Function

; ---------------------------------------------------------------------------
; Options file resolution logic
; ---------------------------------------------------------------------------

Function Config_ResolveOptionsFile()

    Local file$ = Paths\OptionsFile

    If FileType(file$) <> 1 Or HasCLIFlag("defaults") Or HasCLIFlag("default") Then
        DebugLog "Recreating options file..."

        Local f% = WriteFile(file$)
        CloseFile(f)

        Config_WriteDefaults(file$)
    EndIf

End Function

Function Config_WriteDefaults(file$)

    Local defaults$ = Paths\DefaultsFile

    If FileType(defaults$) = 1 Then
        CopyFile defaults$, file$
    EndIf

End Function