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

    return config
    
End Function