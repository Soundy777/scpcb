; ============================================================
; FrameCompositor.bb
; ------------------------------------------------------------
; Handles:
;   - Borderless window resolution scaling
;   - Fullscreen presentation scaling
;   - Software gamma correction
;   - Final frame compositing
;
; Must be initialised AFTER Graphics_Init()
; Depends on:
;   Gfx\RealWidth
;   Gfx\RealHeight
;   Gfx\AspectRatio
; ============================================================

Const FRAMECOMPOSITOR_BUFFER_SIZE% = 2048

Type TFrameCompositor
	Field Camera%
	Field Quad%
	Field MainBuffer%
	Field OverlayBuffer%
	Field BufferSize%
End Type

Global gFrameCompositor.TFrameCompositor

Global fresize_image%, fresize_texture%, fresize_texture2%
Global fresize_cam%

; ============================================================
; Initialisation
; ============================================================

Function FrameCompositor_Init()

	gFrameCompositor = New TFrameCompositor
	gFrameCompositor\BufferSize = FRAMECOMPOSITOR_BUFFER_SIZE

	; --- Create orthographic camera ---
	gFrameCompositor\Camera = CreateCamera()
	CameraProjMode gFrameCompositor\Camera, 2
	CameraZoom gFrameCompositor\Camera, 0.1
	CameraClsMode gFrameCompositor\Camera, 0, 0
	CameraRange gFrameCompositor\Camera, 0.1, 1.5
	MoveEntity gFrameCompositor\Camera, 0, 0, -10000
	HideEntity gFrameCompositor\Camera

	; --- Create fullscreen quad ---
	Local quad% = CreateMesh(gFrameCompositor\Camera)
	Local surf% = CreateSurface(quad)

	AddVertex surf, -1,  1, 0, 0, 0
	AddVertex surf,  1,  1, 0, 1, 0
	AddVertex surf, -1, -1, 0, 0, 1
	AddVertex surf,  1, -1, 0, 1, 1

	AddTriangle surf, 0, 1, 2
	AddTriangle surf, 3, 2, 1

	EntityFX quad, 17
	EntityOrder quad, -100001
	EntityBlend quad, 1
	PositionEntity quad, 0, 0, 1.0001

	; Scale quad to buffer size
	ScaleEntity quad, Float(gFrameCompositor\BufferSize) / Float(Gfx\RealWidth), Float(gFrameCompositor\BufferSize) / Float(Gfx\RealHeight), 1

	gFrameCompositor\Quad = quad

	; --- Create render textures ---
	gFrameCompositor\MainBuffer = CreateTexture(gFrameCompositor\BufferSize, gFrameCompositor\BufferSize, 1+256)
	gFrameCompositor\OverlayBuffer = CreateTexture(gFrameCompositor\BufferSize, gFrameCompositor\BufferSize, 1+256)

	TextureBlend gFrameCompositor\OverlayBuffer, 3

	; Clear overlay buffer once
	SetBuffer(TextureBuffer(gFrameCompositor\OverlayBuffer))
	ClsColor 0,0,0
	Cls
	SetBuffer(BackBuffer())

	; Attach textures to quad
	EntityTexture quad, gFrameCompositor\MainBuffer, 0, 0
	EntityTexture quad, gFrameCompositor\OverlayBuffer, 0, 1

End Function



; ============================================================
; Apply Final Presentation Pass
; Call this at end of render loop BEFORE Flip
; ============================================================

Function FrameCompositor_Apply(borderless%, screenWidth%, screenHeight%, gamma#)

	If gFrameCompositor = Null Then Return

	Local bufferSize% = gFrameCompositor\BufferSize

	; ------------------------------------------------------------
	; Borderless Resolution Scaling
	; ------------------------------------------------------------
	If borderless Then
		If (Gfx\RealWidth <> screenWidth) Or (Gfx\RealHeight <> screenHeight) Then

			SetBuffer TextureBuffer(gFrameCompositor\MainBuffer)
			ClsColor 0,0,0 : Cls

			CopyRect 0,0,screenWidth,screenHeight, bufferSize/2 - screenWidth/2, bufferSize/2 - screenHeight/2, BackBuffer(), TextureBuffer(gFrameCompositor\MainBuffer)

			SetBuffer BackBuffer()
			ClsColor 0,0,0 : Cls

			ScaleRender(0, 0, Float(bufferSize+2) / Float(screenWidth) * Gfx\AspectRatio, Float(bufferSize+2) / Float(screenWidth) * Gfx\AspectRatio)

		EndIf
	EndIf


	; ------------------------------------------------------------
	; Software Gamma Correction
	; ------------------------------------------------------------
	If gamma <> 1.0 Then

		; Copy current frame into main buffer
		CopyRect 0,0,Gfx\RealWidth,Gfx\RealHeight, bufferSize/2 - Gfx\RealWidth/2, bufferSize/2 - Gfx\RealHeight/2, BackBuffer(), TextureBuffer(gFrameCompositor\MainBuffer)

		EntityBlend gFrameCompositor\Quad, 1
		ClsColor 0,0,0 : Cls

		Local scale# = Float(bufferSize) / Float(Gfx\RealWidth)

		ScaleRender(-1.0/Float(Gfx\RealWidth), 1.0/Float(Gfx\RealWidth), scale, scale)

		EntityFX gFrameCompositor\Quad, 1+32

		If gamma > 1.0 Then
			EntityBlend gFrameCompositor\Quad, 3
			EntityAlpha gFrameCompositor\Quad, gamma - 1.0
		Else
			EntityBlend gFrameCompositor\Quad, 2
			EntityAlpha gFrameCompositor\Quad, 1.0

			SetBuffer TextureBuffer(gFrameCompositor\OverlayBuffer)
			ClsColor 255*gamma,255*gamma,255*gamma
			Cls
			SetBuffer BackBuffer()
		EndIf

		ScaleRender(-1.0/Float(Gfx\RealWidth), 1.0/Float(Gfx\RealWidth), scale, scale)

	EndIf


	; ------------------------------------------------------------
	; Restore Defaults
	; ------------------------------------------------------------
	EntityFX gFrameCompositor\Quad, 1
	EntityBlend gFrameCompositor\Quad, 1
	EntityAlpha gFrameCompositor\Quad, 1.0

End Function



; ============================================================
; Cleanup
; ============================================================

Function FrameCompositor_Free()

	If gFrameCompositor = Null Then Return

	FreeTexture gFrameCompositor\MainBuffer
	FreeTexture gFrameCompositor\OverlayBuffer
	FreeEntity gFrameCompositor\Quad
	FreeEntity gFrameCompositor\Camera

	Delete gFrameCompositor
	gFrameCompositor = Null

End Function