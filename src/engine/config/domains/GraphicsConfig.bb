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

    config\Fullscreen = Config_ResolveIntSetting("graphics", "fullscreen")
    ; BorderlessWindowed
    config\SelectedGFXDriver = Min(Max(Config_ResolveIntSetting("graphics", "gfx driver"), 1), CountGfxDrivers())
    ; ScreenGamma
    config\ShowFPS = Config_ResolveIntSetting("graphics", "show FPS")
    ; Framelimit
    ; Vsync
    ; BumpMapEnabled
    ; Anisotropy
    ; AntiAliasing
    ; HUDEnabled
    ; RoomLightsEnabled
    ; TextureDetails
    ; Bit16Mode
    ; ParticleAmount
    ; EnableVRam
    ; FOV
    ; HUDOffset
    ; HUDScaleFactor

    return config

End Function