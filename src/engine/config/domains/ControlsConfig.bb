Type ControlsConfig
    Field MouseSensitivity#
    Field InvertMouse%
    Field MouseSmoothing#
End Type

Function ControlsConfig_Load.ControlsConfig()

    Local config.ControlsConfig = New ControlsConfig

    ;config\MouseSensitivity
    ;config\InvertMouse
    config\MouseSmoothing = Config_ResolveFloatSetting("controls", "mouse smoothing")

    Return config

End Function