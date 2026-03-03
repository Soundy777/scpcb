Function PlaySFX(id%)

    Local internalHandle%
    internalHandle = Resource_GetSound(id)
    If internalHandle <> 0
        PlaySound_Strict(internalHandle)
    EndIf
    
End Function