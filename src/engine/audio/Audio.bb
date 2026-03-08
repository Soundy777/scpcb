Function PlaySFX%(id%)

    Local internalHandle%
    Local channelID = -1
    internalHandle = Resource_GetSound(id)
    If internalHandle <> 0
        channelID = PlaySound_Strict(internalHandle)
    EndIf

    Return channelID
    
End Function

Function PlaySFX%(id%, cam%, entity%, range#=10, volume#=1)

    Local internalHandle%
    Local channelID = -1
    internalHandle = Resource_GetSound(id)
    If internalHandle <> 0
        channelID = PlaySpatialSound(internalHandle, cam, entity, range, volume)
    EndIf

    Return channelID

End Function

;; Loops a specificed sfx for as long as this function is continually called
; A better eventual implementation will be to use an event to add a looped sfx to a list which the audio manager then manages the lifecycle & updating of.
Function LoopSFX%(id%, channel%, cam%, entity%, range# = 10.0, volume# = 1.0)

    ; Ensure sane range
    range = Max(range, 1.0)

    ; If volume is zero we only mute the channel
    If volume <= 0
        If channel <> 0 Then ChannelVolume(channel, 0)
        Return channel
    EndIf


    ; ----------------------------------------------------
    ; Ensure a valid playing channel
    ; ----------------------------------------------------
    If channel = 0 Or (Not ChannelPlaying(channel))
        channel = PlaySFX(id)
    EndIf


    ; ----------------------------------------------------
    ; Spatial calculations
    ; ----------------------------------------------------
    Local dist# = EntityDistance(cam, entity) / range
    dist = Min(dist, 1.0) ; clamp

    Local pan# = Sin(-DeltaYaw(cam, entity))


    ; ----------------------------------------------------
    ; Apply audio parameters
    ; ----------------------------------------------------
    Local finalVolume# = volume * (1.0 - dist) * Config\Audio\SFXVolume

    ChannelVolume(channel, finalVolume)
    ChannelPan(channel, pan)


    Return channel

End Function