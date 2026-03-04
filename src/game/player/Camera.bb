Function ZoomCamera(fov%)
	CameraZoom(Camera, Min(1.0+(CurrCameraZoom/400.0),1.1) / Tan((ATan(Tan(fov%/2.0)*Gfx\RealWidth/Gfx\RealHeight))))
End Function