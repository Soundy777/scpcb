; -----------------------------
; Registration
; -----------------------------
Function Resource_RegisterFont(id%, size%, path$)
	Local rf.ResourceFont = New ResourceFont
	rf\id = id
    rf\size = size
	rf\path = path
	rf\loaded = False
End Function

; -----------------------------
; Access
; -----------------------------
Function Resource_GetFont%(id%)
	For rf.ResourceFont = Each ResourceFont
		If rf\id = id
			
			If rf\loaded = False
				rf\internalHandle = LoadFont_Strict(rf\path, Int(rf\size * Gfx\MenuScale))
				rf\loaded = True
			EndIf
			
			Return rf\internalHandle
		EndIf
	Next
	
	RuntimeError("Texture resource ID not found: " + id)
	Return 0
End Function

; -----------------------------
; Preload
; -----------------------------
Function Resource_PreloadFont(id%)
    Local rf.ResourceFont
    For rf = Each ResourceFont
        If rf\id = id
            If rf\loaded = False
                rf\internalHandle = LoadFont_Strict(rf\path, Int(rf\size * Gfx\MenuScale))
                rf\loaded = True
            EndIf
            Return
        EndIf
    Next
End Function