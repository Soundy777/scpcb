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

;; ToDo:: BlinkRate possibly missing - also might be StaminaDrainRate