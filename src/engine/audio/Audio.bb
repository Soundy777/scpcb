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

Global DoOnce = false
Function LoopSFX(id%, channel%, cam%, entity%, range# = 10, volume# = 1.0)

    range# = Max(range,1.0) ; Clamp range between 0-1
	
	If volume > 0 Then
		
		Local dist# = EntityDistance(cam, entity) / range#
        Local panvalue# = Sin(-DeltaYaw(cam,entity))
        
        If channel = 0 Then
            channel% = PlaySFX(id)
        Else
            If (Not ChannelPlaying(channel)) Then channel% = PlaySFX(id)
        EndIf
        
        ChannelVolume(channel, volume# * (1 - dist#) * Config\Audio\SFXVolume#)
        ChannelPan(channel, panvalue)
	Else
		If channel <> 0 Then
			ChannelVolume(channel, 0)
		EndIf 
	EndIf
	
	Return channel
End Function