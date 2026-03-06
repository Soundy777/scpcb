; -----------------------------
; Registration
; -----------------------------
Global Resource_Sound_Counter = 0
Function Resource_RegisterSound%(path$)
	Local rs.ResourceSound = New ResourceSound
	Resource_Sound_Counter = Resource_Sound_Counter + 1
	rs\id = Resource_Sound_Counter
	rs\path = path

	Return Resource_Sound_Counter
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