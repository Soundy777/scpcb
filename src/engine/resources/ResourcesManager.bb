; =============================
; ResourceManager.bb
; =============================
Include "src/engine/resources/StrictLoads.bb"
Include "src/engine/resources/ResourceTypes.bb"
Include "src/engine/resources/Resource_Sounds.bb"
Include "src/engine/resources/Resource_Textures.bb"
Include "src/engine/resources/Resource_Fonts.bb"

Function Resource_Init()
	; Reserved for future resource initialzation at the engine level
End Function

Function Resource_Shutdown()
	;; ToDo:: unload everything
End Function