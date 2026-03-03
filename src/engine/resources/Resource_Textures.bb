Function Resource_RegisterTexture(id%, path$)
	Local rt.ResourceTexture = New ResourceTexture
	rt\id = id
	rt\path = path
	rt\loaded = False
End Function

Function Resource_GetTexture%(id%)
	For rt.ResourceTexture = Each ResourceTexture
		If rt\id = id
			
			If rt\loaded = False
				rt\internalHandle = LoadImage_Strict(rt\path)
				rt\loaded = True
			EndIf
			
			Return rt\internalHandle
		EndIf
	Next
	
	RuntimeError("Texture resource ID not found: " + id)
	Return 0
End Function

Function Resource_PreloadTexture(id%)
    Local rt.ResourceTexture
    For rt = Each ResourceTexture
        If rt\id = id
            If rt\loaded = False
                rt\internalHandle = LoadImage_Strict(rt\path)
                rt\loaded = True
            EndIf
            Return
        EndIf
    Next
End Function