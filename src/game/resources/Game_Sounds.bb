; ===========================================================================
; Game_Sounds.bb
; ===========================================================================
Include "src/game/resources/Game_Sounds_Door.bb"
Include "src/game/resources/Game_Sounds_Alarm.bb"
; ===========================================================================

Global SFX_INTERACT_BUTTON_1%
Function Game_RegisterSounds()

	RegisterDoorsounds()
	Register_AlarmSounds()

	SFX_INTERACT_BUTTON_1 = Resource_RegisterSound("SFX\Interact\Button.ogg")

End Function