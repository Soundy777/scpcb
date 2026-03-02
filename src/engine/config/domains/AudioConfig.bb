Type AudioConfig
    Field MusicVolume#
    Field SFXVolume#
    Field EnableUserTracks%
    Field UserTrackMode%
    Field EnableSFXRelease%
End Type

Function AudioConfig_Load.AudioConfig()
    
    Local config.AudioConfig = New AudioConfig

    config\EnableSFXRelease = Config_ResolveIntSetting("audio", "sfx release")
    config\SFXVolume = Config_ResolveFloatSetting("audio", "sound volume")

    return config
    
End Function