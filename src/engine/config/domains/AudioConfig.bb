Type AudioConfig
    Field MusicVolume#
    Field SFXVolume#
    Field EnableUserTracks%
    Field UserTrackMode%
    Field EnableSFXRelease%
End Type

Function AudioConfig_Load.AudioConfig()
    
    Local config.AudioConfig = New AudioConfig

    return config
    
End Function