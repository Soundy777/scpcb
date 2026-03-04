Function ResetDiseases()
	DeathTimer = 0
	Infect = 0
	Stamina = 100
	For i = 0 To 5
		SCP1025state[i]=0
	Next
	If StaminaDrainRate > 1.0 Then
		StaminaDrainRate = 1.0
		StaminaRateResetTimer = 0.0
	EndIf
End Function

Function Kill()
	If GodMode Then Return
	
	If BreathCHN <> 0 Then
		If ChannelPlaying(BreathCHN) Then StopChannel(BreathCHN)
	EndIf
	
	If KillTimer >= 0 Then
		KillAnim = Rand(0,1)
		PlaySound_Strict(DamageSFX(0))
		If SelectedDifficulty\permaDeath Then
			DeleteFile(CurrentDir() + SavePath + CurrSave+".cbsav")
			LoadSaveGames()
		End If
		
		KillTimer = Min(-1, KillTimer)
		ShowEntity Head
		PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
		ResetEntity (Head)
		RotateEntity(Head, 0, EntityYaw(Camera), 0)		
	EndIf
End Function

Global PrevSFXVolume# = Config\Audio\SFXVolume#
Global DeafPlayer% = False
Global DeafTimer# = 0.0
Function UpdateDeafPlayer()
	
	If DeafTimer > 0
		DeafTimer = DeafTimer-DeltaTime
		Config\Audio\SFXVolume# = 0.0
		If Config\Audio\SFXVolume# > 0.0
			ControlSoundVolume()
		EndIf
		DebugLog DeafTimer
	Else
		DeafTimer = 0
		Config\Audio\SFXVolume# = PrevSFXVolume#
		If DeafPlayer Then ControlSoundVolume()
		DeafPlayer = False
	EndIf
	
End Function

;; ToDo:: migrate this to our audio management system & create a more elegant solution
Function ControlSoundVolume()
	Local snd.Sound,i
	
	For snd.Sound = Each Sound
		For i=0 To 31
			ChannelVolume snd\channels[i],Config\Audio\SFXVolume#
		Next
	Next
	
End Function