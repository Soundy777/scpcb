; ===========================================================================
; ConfigManager.bb
; ===========================================================================
; Handles saving, loading & providing game configuration data
; ===========================================================================
Include "src/engine/config/GameConfig.bb"

Include "src/engine/config/domains/AudioConfig.bb"
Include "src/engine/config/domains/ConsoleConfig.bb"
Include "src/engine/config/domains/ControlsConfig.bb"
Include "src/engine/config/domains/DebugConfig.bb"
Include "src/engine/config/domains/GameplayConfig.bb"
Include "src/engine/config/domains/GraphicsConfig.bb"
Include "src/engine/config/domains/Keybinds.bb"
Include "src/engine/config/domains/LauncherConfig.bb"

Include "src/engine/config/sources/CLIParser.bb"
Include "src/engine/config/sources/OptionsParser.bb"
; ===========================================================================

;; ToDo:: eventually refactor this out to rely soley on a dependency injection pattern instead of a global
Global Config.GameConfig

; ---------------------------------------------------------------------------
; Public API
; ---------------------------------------------------------------------------

Function Config_Init()

    Config_ResolveOptionsFile()

End Function

Function Config_Load.GameConfig()

    local config.GameConfig = new GameConfig

    config\Audio = AudioConfig_Load()
    config\Console = ConsoleConfig_Load()
    config\Controls = ControlsConfig_Load()
    config\Debug = DebugConfig_Load()
    config\Gameplay = GameplayConfig_Load()
    config\Graphics = GraphicsConfig_Load()
    ; keybinds
    config\Launcher = LauncherConfig_Load()

    Return config

End Function

; ---------------------------------------------------------------------------
; Settings resolution logic
; ---------------------------------------------------------------------------

Type Aliases
    Field list$[10] ; Define max capacity here
End Type

Function Config_ResolveIntSetting%(section$, key$)

    value = GetOptionInt(section, key)

    Local cliValue% = GetCLIInt(key, -1)
    If cliValue <> -1 Then value = cliValue

    Return value

End Function

Function Config_ResolveIntSetting%(section$, key$, a.Aliases)
    Local value% = GetOptionInt(section$, key$)

    ; 1. Check primary CLI key first
    Local cliValue% = GetCLIInt(key$, -1)
    If cliValue <> -1 Then Return cliValue

    ; 2. Iterate through aliases until an empty slot is found
    If a <> Null Then
        Local i%
        For i = 0 To 9 ; Matches the array size defined in the Type
            ; Exit loop early if we hit an empty alias
            If a\list$[i] = "" Then Exit 
            
            cliValue = GetCLIInt(a\list$[i], -1)
            If cliValue <> -1 Then Return cliValue
        Next
    EndIf

    Return value
End Function

Function Config_ResolveFloatSetting#(section$, key$)

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