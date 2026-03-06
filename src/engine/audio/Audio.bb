Function PlaySFX%(id%)

    Local internalHandle%
    Local channelID = -1
    internalHandle = Resource_GetSound(id)
    If internalHandle <> 0
        channelID = PlaySound_Strict(internalHandle)
    EndIf

    Return channelID
    
End Function