; ===========================================================================
; Game_Sounds.bb
; ===========================================================================
Include "src/game/resources/Game_Sounds_Door.bb"
; ===========================================================================

Const SFX_INTERACT_BUTTON_1% = 1
Function Game_RegisterSounds()

	RegisterDoorsounds()

	Resource_RegisterSound(SFX_INTERACT_BUTTON_1, "SFX\Interact\Button.ogg")

End Function