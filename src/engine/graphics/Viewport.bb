; ============================================================
; Viewport.bb
; ------------------------------------------------------------
; Responsible for calculating resolution-dependent
; layout bounds and UI scaling.
; 
; Recalculate when:
;   - Engine initializes
;   - Resolution changes
;   - HUD offset changes
;   - UI scale settings change
; ============================================================

Type ViewportState
	Field StartX%
	Field EndX%
	Field StartY%
	Field EndY%
	Field Width%
	Field Height%
	Field Scale#
End Type

Global Viewport.ViewportState

; ------------------------------------------------------------
; Initializes viewport state.
; Call once during Engine_Init()
; ------------------------------------------------------------
Function Viewport_Init()
	If Viewport = Null Then
		Viewport = New ViewportState
	EndIf
	
	Viewport_Recalculate()
End Function


; ------------------------------------------------------------
; Recalculates viewport bounds and scale.
; 
; IMPORTANT:
; Not intended to be called every frame.
; Only call when graphics or UI settings change.
; ------------------------------------------------------------
Function Viewport_Recalculate()

	Local screenW% = Config\Graphics\ScreenWidth
	Local screenH% = Config\Graphics\ScreenHeight
	Local offset#  = Config\Graphics\HUDOffset

	; --------------------------------------------------------
	; Calculate scale (formerly HUDScale)
	; --------------------------------------------------------
	Viewport\Scale = Max(Gfx\MenuScale * Config\Graphics\HUDScaleFactor, 1)

	; --------------------------------------------------------
	; Calculate bounds
	; --------------------------------------------------------
	If screenW > screenH Then
		; Landscape (pillarbox logic)

		Viewport\StartY = 0
		Viewport\EndY   = screenH

		Viewport\StartX = Int(offset * screenW / 2)
		Viewport\EndX   = screenW - Viewport\StartX
	Else
		; Portrait / Tall aspect (letterbox logic)

		Viewport\StartX = 0
		Viewport\EndX   = screenW

		Viewport\StartY = Int(offset * screenH / 2)
		Viewport\EndY   = screenH - Viewport\StartY
	EndIf


	; --------------------------------------------------------
	; Cache derived dimensions
	; --------------------------------------------------------
	Viewport\Width  = Viewport\EndX - Viewport\StartX
	Viewport\Height = Viewport\EndY - Viewport\StartY

End Function


; ------------------------------------------------------------
; Accessors
; ------------------------------------------------------------

Function Viewport_GetWidth%()
	Return Viewport\Width
End Function

Function Viewport_GetHeight%()
	Return Viewport\Height
End Function

Function Viewport_GetStartX%()
	Return Viewport\StartX
End Function

Function Viewport_GetEndX%()
    Return Viewport\EndX
End Function

Function Viewport_GetStartY%()
	Return Viewport\StartY
End Function

Function Viewport_GetEndY%()
    Return Viewport\EndY
End Function

Function Viewport_GetScale#()
	Return Viewport\Scale
End Function