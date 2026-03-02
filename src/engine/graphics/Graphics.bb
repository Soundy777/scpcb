; ===========================================================================
; Graphics SubSystem
; ===========================================================================
; Handles::
; Graphics driver selection
; Window creation (Graphics3DExt)
; Resolution state
; Aspect ratio math
; Texture detail bias (TextureFloat)
; Menu scale calculation
; Render target creation (your fresize textures later)
; Wireframe state
; Back buffer selection
; ===========================================================================

Type GraphicsState
	Field DriverIndex%
	Field DriverName$
	
	Field RealWidth%
	Field RealHeight%
	Field AspectRatio#
	
	Field MenuScale#
	Field TextureLODBias#
	
	Field Wireframe%
	
	; Post-process resources (migrated next phase)
	Field ResizeImage%
	Field ResizeTexture%
	Field ResizeTexture2%
	Field ResizeCamera%
End Type

Global Gfx.GraphicsState = new GraphicsState

;-----------------------------------------------------------------------------

;; ToDo These still need looking at seperately
Global fresize_image%, fresize_texture%, fresize_texture2%
Global fresize_cam%
;; ToDo End

Global MenuScale#

;-----------------------------------------------------------------------------

Function Graphics_Init(config.GraphicsConfig)

	; -------------------------
	; Driver Selection
	; -------------------------
	Gfx\DriverIndex = config\SelectedGFXDriver
	SetGfxDriver(Gfx\DriverIndex)
	Gfx\DriverName = GFXDriverName(Gfx\DriverIndex)

	; -------------------------
	; Create Window
	; -------------------------
	If config\BorderlessWindowed
		
		Graphics3DExt DesktopWidth(), DesktopHeight(), 0, 4
		
		Gfx\RealWidth = DesktopWidth()
		Gfx\RealHeight = DesktopHeight()
		
		Gfx\AspectRatio = (Float(config\ScreenWidth)/Float(config\ScreenHeight)) / (Float(Gfx\RealWidth)/Float(Gfx\RealHeight))
			
	Else
		
		Gfx\AspectRatio = 1.0
		Gfx\RealWidth = config\ScreenWidth
		Gfx\RealHeight = config\ScreenHeight
		
		If config\Fullscreen
			Graphics3DExt(config\ScreenWidth, config\ScreenHeight, (16*config\Bit16Mode), 1)
		Else
			Graphics3DExt(config\ScreenWidth, config\ScreenHeight, 0, 2)
		EndIf
		
	EndIf

	; -------------------------
	; Texture Bias
	; -------------------------
	Gfx\TextureLODBias = Graphics_CalcTextureBias(config\TextureDetails)

	; -------------------------
	; Menu Scale
	; -------------------------
	Gfx\MenuScale = Graphics_CalcMenuScale()

	; -------------------------
	; Default States
	; -------------------------
	Gfx\Wireframe = False
	WireFrame False
	
	SetBuffer BackBuffer()

    ; -------------------------
	; Temp Global Assignment
	; -------------------------
    MenuScale = Gfx\MenuScale

End Function

;-----------------------------------------------------------------------------

Function Graphics_CalcTextureBias#(detail%)
	Select detail
		Case 0 : Return 0.8
		Case 1 : Return 0.4
		Case 2 : Return 0.0
		Case 3 : Return -0.4
		Case 4 : Return -0.8
	End Select
	Return 0.0
End Function

Function Graphics_CalcMenuScale#()
	Local short% = Min(Gfx\RealWidth, Gfx\RealHeight)
	If short > 1024 Then Return short / 1024.0
	If short > 840 Then Return 1
	Return short / 840.0
End Function