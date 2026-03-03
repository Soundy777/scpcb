; ============================================================
; ImageScaler.bb
; ------------------------------------------------------------
; GPU-based image scaling utility.
; Optimized for per-frame usage.
;
; Must be initialised AFTER Graphics_Init()
; ============================================================

Const IMAGE_SCALER_BUFFER_SIZE% = 2048

Type TImageScaler
	Field Camera%
	Field Quad%
	Field Surface%
	Field WorkingTexture%
	Field BufferSize%
End Type

Global gImageScaler.TImageScaler

; ============================================================
; Initialisation (call once during Graphics init phase)
; ============================================================

Function ImageScaler_Init()

	gImageScaler = New TImageScaler
	gImageScaler\BufferSize = IMAGE_SCALER_BUFFER_SIZE

	; --- Create orthographic camera ---
	gImageScaler\Camera = CreateCamera()
	CameraProjMode gImageScaler\Camera, 2
	CameraZoom gImageScaler\Camera, 0.1
	CameraClsMode gImageScaler\Camera, 0, 0
	CameraRange gImageScaler\Camera, 0.1, 1.5
	MoveEntity gImageScaler\Camera, 0, 0, -10000
	HideEntity gImageScaler\Camera

	; --- Create fullscreen quad ---
	Local quad% = CreateMesh(gImageScaler\Camera)
	Local surf% = CreateSurface(quad)

	AddVertex surf, -1,  1, 0, 0, 0
	AddVertex surf,  1,  1, 0, 1, 0
	AddVertex surf, -1, -1, 0, 0, 1
	AddVertex surf,  1, -1, 0, 1, 1

	AddTriangle surf, 0, 1, 2
	AddTriangle surf, 3, 2, 1

	EntityFX quad, 1
	EntityBlend quad, 1
	EntityOrder quad, -100001
	PositionEntity quad, 0, 0, 1.0001

	ScaleEntity quad, Float(gImageScaler\BufferSize) / Float(Gfx\RealWidth), Float(gImageScaler\BufferSize) / Float(Gfx\RealHeight), 1

	gImageScaler\Quad = quad
	gImageScaler\Surface = surf

	; --- Create reusable working texture ---
	gImageScaler\WorkingTexture = CreateTexture( gImageScaler\BufferSize, gImageScaler\BufferSize, 1+256)

	EntityTexture quad, gImageScaler\WorkingTexture

End Function



; ============================================================
; GPU Image Scaling (fast path, reusable context)
; ============================================================
Function Image_ScaleGPU%(sourceImage%, newWidth%, newHeight%)

	If sourceImage = 0 Then Return 0
	If gImageScaler = Null Then Return 0

	Local oldWidth%  = ImageWidth(sourceImage)
	Local oldHeight% = ImageHeight(sourceImage)

	If oldWidth = 0 Or oldHeight = 0 Then Return 0

	; Create destination image
	Local resultImage% = CreateImage(newWidth, newHeight)

	Local bufferSize% = gImageScaler\BufferSize

	; ------------------------------------------------------------
	; Copy source image into working texture (centered)
	; ------------------------------------------------------------
	SetBuffer TextureBuffer(gImageScaler\WorkingTexture)
	ClsColor 0,0,0 : Cls

	CopyRect 0,0,oldWidth,oldHeight, bufferSize/2 - oldWidth/2, bufferSize/2 - oldHeight/2, ImageBuffer(sourceImage), TextureBuffer(gImageScaler\WorkingTexture)

	SetBuffer BackBuffer()

	; ------------------------------------------------------------
	; Render scaled quad
	; ------------------------------------------------------------
	Local scaleX# = Float(newWidth)  / Float(oldWidth)
	Local scaleY# = Float(newHeight) / Float(oldHeight)

    Local scaleMod# = IMAGE_SCALER_BUFFER_SIZE / Float(Gfx\RealWidth)

    Local finalScaleX# = scaleMod * scaleX
    Local finalScaleY# = scaleMod * scaleY

	; show our orthographic camera and quad and hide whatever camera the
	; game was using so that RenderWorld() draws only the quad.
	If Camera<>0 Then HideEntity Camera
	WireFrame 0
	ShowEntity gImageScaler\Quad
	ShowEntity gImageScaler\Camera

	SetBuffer BackBuffer()
	ClsColor 0,0,0 : Cls

	; apply relative scale and render
	ScaleEntity gImageScaler\Quad, finalScaleX#, finalScaleY#, 1
	PositionEntity gImageScaler\Quad, 0,0,1.0
	RenderWorld() ; draws quad with working texture

	; revert scale so the next call starts from the base transform
	ScaleEntity gImageScaler\Quad, 1.0/scaleX#, 1.0/scaleY#, 1

	; restore previous rendering state
	HideEntity gImageScaler\Camera
	HideEntity gImageScaler\Quad
	WireFrame Gfx\Wireframe
	If Camera<>0 Then ShowEntity Camera

	; ------------------------------------------------------------
	; Copy scaled result into destination image
	; ------------------------------------------------------------
	CopyRect Gfx\RealWidth/2  - newWidth/2, Gfx\RealHeight/2 - newHeight/2, newWidth, newHeight, 0, 0, BackBuffer(), ImageBuffer(resultImage)

    FreeImage sourceImage
	Return resultImage

End Function

; ============================================================
; Cleanup
; ============================================================

Function ImageScaler_Free()

	If gImageScaler = Null Then Return

	FreeTexture gImageScaler\WorkingTexture
	FreeEntity gImageScaler\Quad
	FreeEntity gImageScaler\Camera

	Delete gImageScaler
	gImageScaler = Null

End Function