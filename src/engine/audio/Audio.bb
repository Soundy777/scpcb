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