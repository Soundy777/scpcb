Type GameOptions
    ; Graphics
    Field ScreenWidth%
    Field ScreenHeight%
    Field Fullscreen%
    Field BorderlessWindowed%
    Field HUDScaleFactor#
    Field HUDEnabled%
    Field ScreenGamma#
    Field AntiAliasing%
    Field Vsync%
    Field ShowFPS%
    Field Framelimit%
    Field TextureDetails%
    Field HUDOffsetScale#
    Field FOV%
    Field Bit16Mode%
    Field SelectedGFXDriver%
    Field EnableVRam%

    ; Launcher
    Field LauncherEnabled%

    ; Input
    Field MouseSensitivity#
    Field InvertMouseY%
    Field MouseSmoothing%
    
    ; General
    Field AchievementPopups%
    Field SpeedRunMode%
    Field UseNumericSeeds%
    Field IntroEnabled%

    ; Console
    Field ConsoleEnabled%
    Field ConsoleAutoOpen%

    ; Audio
    Field MusicVolume#
    Field SFXVolume#
    Field EnableSFXRelease%
    Field EnableUserTracks%
    Field UserTrackMode%

    ; Binds
    Field KEY_LEFT%
    Field KEY_RIGHT%
    Field KEY_UP%
    Field KEY_DOWN%
    Field KEY_BLINK%
    Field KEY_SPRINT%
    Field KEY_INVENTORY%
    Field KEY_CROUCH%
    Field KEY_SAVE%
    Field KEY_CONSOLE%
End Type

Global Options.GameOptions

Function LoadOptions()
    Options = New GameOptions

    ;; ToDo:: load all options here
    Options\ConsoleEnabled = GetOptionInt("console", "enabled")
End Function

Function GetOptionString$(section$, key$)
	Local opt$ = GetINIString(Paths_OptionsFile, section, key)
	If opt = "" Then Return GetINIString(OPTION_DEFAULT_FILE, section, key)
	Return opt
End Function

Function GetOptionInt%(section$, key$)
	Return ParseINIInt(GetOptionString(section, key))
End Function

Function GetOptionFloat#(section$, key$)
	Return Float(GetOptionString(section, key))
End Function