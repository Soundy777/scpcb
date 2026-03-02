Type GraphicsConfig
    Field ScreenWidth%
    Field ScreenHeight%
    Field Fullscreen%
    Field BorderlessWindowed%
    Field SelectedGFXDriver%
    Field ScreenGamma#
    Field ShowFPS%
    Field Framelimit%
    Field Vsync%
    Field BumpMapEnabled%
    Field Anisotropy%
    Field AntiAliasing%
    Field HUDEnabled%
    Field RoomLightsEnabled%
    Field TextureDetails%
    Field Bit16Mode%
    Field ParticleAmount#
    Field EnableVRam%
    Field FOV%
    Field DefaultFOV%
    Field HUDOffset#
    Field HUDScaleFactor#
End Type

Function GraphicsConfig_Load.GraphicsConfig()

    Local config.GraphicsConfig = New GraphicsConfig

    ;; ToDo:: potentially account for width & height CLI alias of "w" & "h"
    config\ScreenWidth = Config_ResolveIntSetting("graphics", "width")
    config\ScreenHeight = Config_ResolveIntSetting("graphics", "height")
    If config\ScreenWidth <= 0 Then config\ScreenWidth = DesktopWidth()
    If config\ScreenHeight <= 0 Then config\ScreenHeight = DesktopHeight()

    Local alias_fullscreen.Aliases = New Aliases : alias_fullscreen\list[0] = "full"
    config\Fullscreen = Config_ResolveIntSetting("graphics", "fullscreen", alias_fullscreen)
    Local alias_borderlesswindowed.Aliases = New Aliases : alias_borderlesswindowed\list[0] = "noborder" : alias_borderlesswindowed\list[1] = "borderless"
    config\BorderlessWindowed = Config_ResolveIntSetting("graphics", "borderless windowed", alias_borderlesswindowed)

    If config\Fullscreen Then
        config\BorderlessWindowed = False
    End If

    ; Override in the event the user wishes to run the game in windowed mode
    If HasCLIFlag("window") Lor HasCLIFlag("windowed") Lor HasCLIFlag("sw") Lor HasCLIFlag("startwindowed") Then
        config\Fullscreen = False
        config\BorderlessWindowed = False
    End If

    config\SelectedGFXDriver = Min(Max(Config_ResolveIntSetting("graphics", "gfx driver"), 1), CountGfxDrivers())
    config\ScreenGamma = Config_ResolveFloatSetting("graphics", "screengamma")
    config\ShowFPS = Config_ResolveIntSetting("graphics", "show FPS")
    config\Framelimit = Config_ResolveIntSetting("graphics", "framelimit")
    config\Vsync = Config_ResolveIntSetting("graphics", "vsync")
    ; BumpMapEnabled
    ; Anisotropy
    config\AntiAliasing = Config_ResolveIntSetting("graphics", "antialias")
    ; HUDEnabled
    config\RoomLightsEnabled = Config_ResolveIntSetting("graphics", "room lights enabled")
    config\TextureDetails = Config_ResolveIntSetting("graphics", "texture details")
    config\Bit16Mode = Config_ResolveIntSetting("graphics", "16bit")
    ; ParticleAmount
    ; EnableVRam
    config\FOV = Config_ResolveIntSetting("graphics", "fov")
    config\DefaultFOV = 59
    config\HUDOffset = Config_ResolveFloatSetting("graphics", "hud offset")
    config\HUDScaleFactor = Config_ResolveFloatSetting("graphics", "hud scale factor")

    return config

End Function