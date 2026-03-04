Global Camera%, CameraShake#, CurrCameraZoom#

Global CameraFogNear#
Global CameraFogFar#

Global Brightness%		;; ToDo:: review if this might be more to do with the environment vs the camera

Global StoredCameraFogFar# = CameraFogFar

Function ZoomCamera(fov%)
	CameraZoom(Camera, Min(1.0+(CurrCameraZoom/400.0),1.1) / Tan((ATan(Tan(fov%/2.0)*Gfx\RealWidth/Gfx\RealHeight))))
End Function