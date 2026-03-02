; ============================================================
; Time System
; ============================================================

Type TTime
	Field CurTime%
	Field PrevTime%
	Field ElapsedTime%
	
	Field LoopDelay%
	
	Field FPS%
	Field CheckFPS%
	Field ElapsedLoops%
End Type

;-----------------------------------------------------------------------------

; This is used to adjust the deltatime due to the origonal calculations all assuming 60 FPS & thus the game is tuned to expecting a deltatime in this domain
;; ToDo:: eventually remove this tuning & allow pure deltatime to drive everything
Const FPS_BASE_FACTOR# = 70.0
Const MAX_FRAME_SCALE# = 5.0

;-----------------------------------------------------------------------------

Global ttime.TTime
Global DeltaTime# ; legacy scaled (matches old FPSfactor behaviour)
Global RawDeltaTime# ; Not subject to pausing

;-----------------------------------------------------------------------------
; Public API
;-----------------------------------------------------------------------------

Function Time_GetElapsedTime%()
    return ttime\ElapsedTime
End Function

Function Time_GetFPS%()
    return ttime\FPS
End Function

;-----------------------------------------------------------------------------

Function Time_Init()

	ttime = New TTime
	
	ttime\CurTime = MilliSecs()
	ttime\PrevTime = ttime\CurTime
	ttime\LoopDelay = ttime\CurTime
	
	ttime\CheckFPS = ttime\CurTime + 1000
	ttime\ElapsedLoops = 0
	ttime\FPS = 0
	
	; Initialize DeltaTime to 1 frame at base rate
	DeltaTime = (1.0 / FPS_BASE_FACTOR) * FPS_BASE_FACTOR
	; which resolves to 1.0

End Function

;-----------------------------------------------------------------------------

Function Time_Update()

	; --- Measure frame ---
	ttime\CurTime = MilliSecs()
	ttime\ElapsedTime = ttime\CurTime - ttime\PrevTime
	ttime\PrevTime = ttime\CurTime
	
	; --- Legacy delta calculation ---
	DeltaTime = Float(ttime\ElapsedTime) / 1000.0 * FPS_BASE_FACTOR
	
	If DeltaTime > MAX_FRAME_SCALE Then
		DeltaTime = MAX_FRAME_SCALE
	EndIf
	
    RawDeltaTime = DeltaTime

	; --- Pause handling ---
	If IsPaused() Then
		DeltaTime = 0
	EndIf
	
	; --- Frame limiting ---
	If Config\Graphics\Framelimit > 0 Then
	
		Local WaitingTime% = Int(1000.0 / Config\Graphics\Framelimit) - (MilliSecs() - ttime\LoopDelay)
		
		If WaitingTime > 0 Then
			Delay WaitingTime
		EndIf
		
		ttime\LoopDelay = MilliSecs()
	EndIf
	
	; --- FPS counter ---
	If ttime\CheckFPS < MilliSecs() Then
		ttime\FPS = ttime\ElapsedLoops
		ttime\ElapsedLoops = 0
		ttime\CheckFPS = MilliSecs() + 1000
	EndIf
	
	ttime\ElapsedLoops = ttime\ElapsedLoops + 1

End Function