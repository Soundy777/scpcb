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

;; ToDo:: implement a more elegant solution using our config types
;; ToDo:: possibly split it into the domains to let each one handle saving its own data
;; ToDo:: gather up stray PutINIValues & merge them into various "Saving" functions
Function Config_Save()
	
	PutINIValue(Paths\OptionsFile, "controls", "mouse sensitivity", MouseSens)
	PutINIValue(Paths\OptionsFile, "controls", "invert mouse y", Config\Controls\InvertMouse)
	PutINIValue(Paths\OptionsFile, "graphics", "HUD enabled", HUDenabled)
	PutINIValue(Paths\OptionsFile, "graphics", "screengamma", Config\Graphics\ScreenGamma)
	PutINIValue(Paths\OptionsFile, "graphics", "antialias", Config\Graphics\AntiAliasing)
	PutINIValue(Paths\OptionsFile, "graphics", "vsync", Config\Graphics\Vsync)
	PutINIValue(Paths\OptionsFile, "graphics", "show FPS", Config\Graphics\ShowFPS)
	PutINIValue(Paths\OptionsFile, "graphics", "framelimit", Config\Graphics\Framelimit%)
	PutINIValue(Paths\OptionsFile, "general", "achievement popup enabled", AchvMSGenabled%)
	PutINIValue(Paths\OptionsFile, "launcher", "launcher enabled", Config\Launcher\LauncherEnabled%)
	PutINIValue(Paths\OptionsFile, "graphics", "texture details", Config\Graphics\TextureDetails%)
	PutINIValue(Paths\OptionsFile, "console", "enabled", ConsoleEnabled%)
	PutINIValue(Paths\OptionsFile, "console", "auto opening", Config\Console\ConsoleAutoOpen%)
	PutINIValue(Paths\OptionsFile, "general", "speed run mode", SpeedRunMode%)
	PutINIValue(Paths\OptionsFile, "general", "numeric seeds", Config\Gameplay\UseNumericSeeds%)
	PutINIValue(Paths\OptionsFile, "graphics", "enable vram", EnableVRam)
	PutINIValue(Paths\OptionsFile, "controls", "mouse smoothing", Config\Controls\MouseSmoothing)
	PutINIValue(Paths\OptionsFile, "graphics", "hud offset", Config\Graphics\HUDOffset)
	PutINIValue(Paths\OptionsFile, "graphics", "fov", Config\Graphics\FOV)
	
	PutINIValue(Paths\OptionsFile, "audio", "music volume", MusicVolume)
	PutINIValue(Paths\OptionsFile, "audio", "sound volume", PrevSFXVolume)
	PutINIValue(Paths\OptionsFile, "audio", "sfx release", Config\Audio\EnableSFXRelease)
	PutINIValue(Paths\OptionsFile, "audio", "enable user tracks", EnableUserTracks%)
	PutINIValue(Paths\OptionsFile, "audio", "user track setting", UserTrackMode%)
	
	PutINIValue(Paths\OptionsFile, "binds", "Right key", KEY_RIGHT)
	PutINIValue(Paths\OptionsFile, "binds", "Left key", KEY_LEFT)
	PutINIValue(Paths\OptionsFile, "binds", "Up key", KEY_UP)
	PutINIValue(Paths\OptionsFile, "binds", "Down key", KEY_DOWN)
	PutINIValue(Paths\OptionsFile, "binds", "Blink key", KEY_BLINK)
	PutINIValue(Paths\OptionsFile, "binds", "Sprint key", KEY_SPRINT)
	PutINIValue(Paths\OptionsFile, "binds", "Inventory key", KEY_INV)
	PutINIValue(Paths\OptionsFile, "binds", "Crouch key", KEY_CROUCH)
	PutINIValue(Paths\OptionsFile, "binds", "Save key", KEY_SAVE)
	PutINIValue(Paths\OptionsFile, "binds", "Console key", KEY_CONSOLE)
	
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