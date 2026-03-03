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

Global FrameCompositor.TFrameCompositor

Global fresize_image%, fresize_texture%, fresize_texture2%
Global fresize_cam%

; ============================================================
; Initialisation
; ============================================================

Function FrameCompositor_Init()

	FrameCompositor = New TFrameCompositor
	FrameCompositor\BufferSize = FRAMECOMPOSITOR_BUFFER_SIZE

	; --- Create orthographic camera ---
	FrameCompositor\Camera = CreateCamera()
	CameraProjMode FrameCompositor\Camera, 2
	CameraZoom FrameCompositor\Camera, 0.1
	CameraClsMode FrameCompositor\Camera, 0, 0
	CameraRange FrameCompositor\Camera, 0.1, 1.5
	MoveEntity FrameCompositor\Camera, 0, 0, -10000
	HideEntity FrameCompositor\Camera

	; --- Create fullscreen quad ---
	Local quad% = CreateMesh(FrameCompositor\Camera)
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
	ScaleEntity quad, Float(FrameCompositor\BufferSize) / Float(Gfx\RealWidth), Float(FrameCompositor\BufferSize) / Float(Gfx\RealHeight), 1

	FrameCompositor\Quad = quad

	; --- Create render textures ---
	FrameCompositor\MainBuffer = CreateTexture(FrameCompositor\BufferSize, FrameCompositor\BufferSize, 1+256)
	FrameCompositor\OverlayBuffer = CreateTexture(FrameCompositor\BufferSize, FrameCompositor\BufferSize, 1+256)

	TextureBlend FrameCompositor\OverlayBuffer, 3

	; Clear overlay buffer once
	SetBuffer(TextureBuffer(FrameCompositor\OverlayBuffer))
	ClsColor 0,0,0
	Cls
	SetBuffer(BackBuffer())

	; Attach textures to quad
	EntityTexture quad, FrameCompositor\MainBuffer, 0, 0
	EntityTexture quad, FrameCompositor\OverlayBuffer, 0, 1



End Function



; ============================================================
; Apply Final Presentation Pass
; Call this at end of render loop BEFORE Flip
; ============================================================

Function FrameCompositor_Apply(borderless%, screenWidth%, screenHeight%, gamma#)

	If FrameCompositor = Null Then Return

	Local bufferSize% = FrameCompositor\BufferSize

	; ------------------------------------------------------------
	; Borderless Resolution Scaling
	; ------------------------------------------------------------
	If borderless Then
		If (Gfx\RealWidth <> screenWidth) Or (Gfx\RealHeight <> screenHeight) Then

			SetBuffer TextureBuffer(FrameCompositor\MainBuffer)
			ClsColor 0,0,0 : Cls

			CopyRect 0,0,screenWidth,screenHeight, bufferSize/2 - screenWidth/2, bufferSize/2 - screenHeight/2, BackBuffer(), TextureBuffer(FrameCompositor\MainBuffer)

			SetBuffer BackBuffer()
			ClsColor 0,0,0 : Cls

			FrameCompositor_ScaleRender(0, 0, Float(bufferSize+2) / Float(screenWidth) * Gfx\AspectRatio, Float(bufferSize+2) / Float(screenWidth) * Gfx\AspectRatio)

		EndIf
	EndIf


	; ------------------------------------------------------------
	; Software Gamma Correction
	; ------------------------------------------------------------
	If gamma <> 1.0 Then

        ; --- Copy current frame into main buffer ---
        CopyRect 0,0,Gfx\RealWidth,Gfx\RealHeight, bufferSize/2 - Gfx\RealWidth/2, bufferSize/2 - Gfx\RealHeight/2, BackBuffer(), TextureBuffer(FrameCompositor\MainBuffer)

        EntityBlend FrameCompositor\Quad, 1
        ClsColor 0,0,0 : Cls

        Local scale# = Float(bufferSize) / Float(Gfx\RealWidth)

        ; --- Draw first pass (normal) ---
        FrameCompositor_ScaleRender(-1.0/Float(Gfx\RealWidth), 1.0/Float(Gfx\RealWidth), scale, scale)
        EntityFX FrameCompositor\Quad, 1+32

        If gamma > 1.0 Then
            ; --- Brightening pass ---
            EntityBlend FrameCompositor\Quad, 3   ; additive
            EntityAlpha FrameCompositor\Quad, gamma - 1.0
            FrameCompositor_ScaleRender(-1.0/Float(Gfx\RealWidth), 1.0/Float(Gfx\RealWidth), scale, scale)

        Else
            ; --- Darkening pass ---
            EntityBlend FrameCompositor\Quad, 2   ; modulate
            EntityAlpha FrameCompositor\Quad, 1.0

            SetBuffer TextureBuffer(FrameCompositor\OverlayBuffer)
            ClsColor 255*gamma, 255*gamma, 255*gamma
            Cls
            SetBuffer BackBuffer()

            FrameCompositor_ScaleRender(-1.0/Float(Gfx\RealWidth), 1.0/Float(Gfx\RealWidth), scale, scale)

            SetBuffer TextureBuffer(FrameCompositor\OverlayBuffer)
		    ClsColor 0,0,0
		    Cls
		    SetBuffer(BackBuffer())
        EndIf

    EndIf

	; ------------------------------------------------------------
	; Restore Defaults
	; ------------------------------------------------------------
	EntityFX FrameCompositor\Quad, 1
	EntityBlend FrameCompositor\Quad, 1
	EntityAlpha FrameCompositor\Quad, 1.0

End Function

; ------------------------------------------------------------
; Internal helper: render the FrameCompositor quad with scale & position
; ------------------------------------------------------------
Function FrameCompositor_ScaleRender(x#, y#, hscale#=1.0, vscale#=1.0)

	If FrameCompositor = Null Then Return

	; Hide any external camera
	If Camera<>0 Then HideEntity Camera

	WireFrame 0

	; Show quad + compositor camera
	ShowEntity FrameCompositor\Quad
	ShowEntity FrameCompositor\Camera

	; Transform quad
	ScaleEntity FrameCompositor\Quad, hscale#, vscale#, 1.0
	PositionEntity FrameCompositor\Quad, x#, y#, 1.0001

	; Render quad to current buffer
	RenderWorld()

	; Restore state
	HideEntity FrameCompositor\Camera
	HideEntity FrameCompositor\Quad

	WireFrame Gfx\Wireframe

	If Camera<>0 Then ShowEntity Camera

End Function

; ============================================================
; Cleanup
; ============================================================

Function FrameCompositor_Free()

	If FrameCompositor = Null Then Return

	FreeTexture FrameCompositor\MainBuffer
	FreeTexture FrameCompositor\OverlayBuffer
	FreeEntity FrameCompositor\Quad
	FreeEntity FrameCompositor\Camera

	Delete FrameCompositor
	FrameCompositor = Null

End Function