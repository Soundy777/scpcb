; ===========================================================================
; Player.bb
; ===========================================================================
Include "src/game/player/Input.bb"
Include "src/game/player/Inventory.bb"
Include "src/game/player/Status.bb"
Include "src/game/player/Camera.bb"
Include "src/game/player/Movement.bb"
; ===========================================================================

;---------------------------------------------------------------------------------------------------------------------
; Global Player Vars
;; ToDo:: refactor these by forming them into the Player Type
;; ToDo:: audit each variable name to ensure they're accurately descriptive
;---------------------------------------------------------------------------------------------------------------------
Global KillTimer#, KillAnim%, FallTimer#, DeathTimer#
Global Sanity#, ForceMove#, ForceAngle#
Global RestoreSanity%

Global PlayerCanMove% = True

Global BlinkFrequency#		; Blink timer is reset to this after each blink, modulated by game difficulty
Global BlinkTimer#			; Timer to next blink
Global BlinkRate# = 1.0		; Rate at which we blink
Global BlinkRateResetTimer#	; Timer until blink rate is reset if adjusted
Global EyeIrritation#
Global EyeStuck#

Global Stamina#
Global StaminaDrainRate#=1.0
Global StaminaRateResetTimer#

Global CameraShakeTimer#, Vomit%, VomitTimer#, Regurgitate%

Global SCP1025state#[6]

Global HeartBeatRate#, HeartBeatTimer#, HeartBeatVolume#

Global WearingGasMask%, WearingHazmat%, WearingVest%, Wearing714%, WearingNightVision%

Global SuperMan%, SuperManTimer#

Global Injuries#, Bloodloss#, Infect#, HealTimer#

Global RefinedItems%

Global DropSpeed#, HeadDropSpeed#, CurrSpeed#
Global user_camera_pitch#, side#
Global Crouch%, CrouchState#

Global PlayerZone%, PlayerRoom.Rooms
Global GrabbedEntity%

Global GodMode%, NoClip%, NoClipSpeed# = 2.0

Global CoffinDistance# = 100.0

Global PlayerSoundVolume#

Global Shake#   ; Camera shake

Global ExplosionTimer#, ExplosionSFX%

Global SoundTransmission%

;This variable is for when a camera detected the player
	;False: Player is not seen (will be set after every call of the Main Loop
	;True: The Player got detected by a camera
Global PlayerDetected%
Global PrevInjuries#,PrevBloodloss#
Global NoTarget% = False

Global GuaranteedOmni% = False
Global Wearing1499% = False

;---------------------------------------------------------------------------------------------------------------------
; Player Type & Init
;---------------------------------------------------------------------------------------------------------------------
Type Player
    ; --- Stats ---
    Field Sanity#
    Field Stamina#
    Field Injuries#
    Field Bloodloss#
    Field Infect#
    
    ; --- State Timers ---
    Field KillTimer#
    Field DeathTimer#
    Field BlinkTimer#
    Field HeartBeatTimer#
    
    ; --- Equipment ---
    Field WearingGasMask%
    Field WearingHazmat%
    Field WearingVest%
    Field Wearing714%
    Field WearingNightVision%
    
    ; --- Movement ---
    Field CurrSpeed#
    Field DropSpeed#
    Field HeadDropSpeed#
    Field Crouch%
    Field CrouchState#
    
    ; --- Camera ---
    Field CameraPitch#
    Field CameraShakeTimer#
    
    ; --- Input ---
    Field MouseHit1%
    Field MouseDown1%
    Field MouseHit2%
    Field DoubleClick%
    
    ; --- Flags ---
    Field CanMove%
    Field GodMode%
    Field NoClip%
    Field NoClipSpeed#
    
    ; --- Misc ---
    Field PlayerZone%
    Field GrabbedEntity%
    Field PlayerSoundVolume#
End Type

Global PlayerData.Player

; Initialization when the player loads from a new game
; This is what we'll use when we eventually migrate all the player data into the player type
Function Player_Init_Future()

    ; Delete the existing instance of PlayerData if it exists to allow for easy resettings
    If PlayerData.Player <> Null Then
        Delete PlayerData.Player
        PlayerData = Null
    EndIf
    PlayerData.Player = New Player

    ; Setup the initial values for the PlayerData obj

End Function

; A temporary initialization used when a new game is launched
Function Player_Init_NewGame()

    ; Setup the initial values for all player data
    BlinkTimer = -10
	BlurTimer = 100
	Stamina = 100
    DropSpeed = 0

End Function

; Special initialization which is called when the game loads from a save
Function Player_Init_LoadGame()

    ; Setup the initial values for all player data
    BlinkTimer = BlinkFrequency
	Stamina = 100
    DropSpeed = 0.0
    HeartBeatRate = 70

End Function