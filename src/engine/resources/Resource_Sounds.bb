; -----------------------------
; Registration
; -----------------------------
Function Resource_RegisterSound(id%, path$)
	Local rs.ResourceSound = New ResourceSound
	rs\id = id
	rs\path = path
End Function

; -----------------------------
; Access
; -----------------------------
Function Resource_GetSound%(id%)
	For rs.ResourceSound = Each ResourceSound
		If rs\id = id

            rs\internalHandle = LoadSound_Strict(rs\path)
			
			Return rs\internalHandle
		EndIf
	Next
	
	RuntimeError("Sound resource ID not found: " + id)
	Return 0
End Function