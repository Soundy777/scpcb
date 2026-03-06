; ---------------------------------------------------------------------------
; DOOR Sounds
;; ToDo:: move this into the actual door system when we build that
; ---------------------------------------------------------------------------
Const DOORTYPE_PRIMARY = 0
Const DOORTYPE_BIG = 1
Const DOORTYPE_ALT = 2
Const DOORTYPE_ELEVATOR = 3

Type DoorVariant
    Field DoorType% = 0

    ; Set UpperBound to (ArraySize - 1)
    Field SFX_Open_UpperBound = 2
    Field SFX_Open%[3]

    Field SFX_Close_UpperBound = 2
    Field SFX_Close%[3]
End Type

Dim DoorVariants.DoorVariant(10)

Global SFX_Door_Wooden_Open%
Global SFX_Door_Wooden_Close%
Global SFX_Door_Wooden_Budge%

Global SFX_Door_Error%
Global SFX_Door_Sparks%
Global SFX_Door_DistantOpen%
Global SFX_Door_OpenFast%

Global SFX_Door_Airlock%
Global SFX_Door_Checkpoint%
Global SFX_Door_Endroom%

Global SFX_Door_SCP_1123_Open%
Global SFX_Door_SCP_079_Open%
Global SFX_Door_SCP_079_Close%
Global SFX_Door_SCP_173_Open%

Function RegisterDoorsounds()
    DoorVariants(DOORTYPE_PRIMARY) = New DoorVariant
    DoorVariants(DOORTYPE_PRIMARY)\DoorType = DOORTYPE_PRIMARY
    
    DoorVariants(DOORTYPE_PRIMARY)\SFX_Open[0] = Resource_RegisterSound("SFX\Door\DoorOpen1.ogg")
    DoorVariants(DOORTYPE_PRIMARY)\SFX_Open[1] = Resource_RegisterSound("SFX\Door\DoorOpen2.ogg")
    DoorVariants(DOORTYPE_PRIMARY)\SFX_Open[2] = Resource_RegisterSound("SFX\Door\DoorOpen3.ogg")

    DoorVariants(DOORTYPE_PRIMARY)\SFX_Close[0] = Resource_RegisterSound("SFX\Door\DoorClose1.ogg")
    DoorVariants(DOORTYPE_PRIMARY)\SFX_Close[1] = Resource_RegisterSound("SFX\Door\DoorClose2.ogg")
    DoorVariants(DOORTYPE_PRIMARY)\SFX_Close[2] = Resource_RegisterSound("SFX\Door\DoorClose3.ogg")

    DoorVariants(DOORTYPE_ALT) = New DoorVariant
    DoorVariants(DOORTYPE_ALT)\DoorType = DOORTYPE_ALT
    
    DoorVariants(DOORTYPE_ALT)\SFX_Open[0] = Resource_RegisterSound("SFX\Door\Door2Open1.ogg")
    DoorVariants(DOORTYPE_ALT)\SFX_Open[1] = Resource_RegisterSound("SFX\Door\Door2Open2.ogg")
    DoorVariants(DOORTYPE_ALT)\SFX_Open[2] = Resource_RegisterSound("SFX\Door\Door2Open3.ogg")

    DoorVariants(DOORTYPE_ALT)\SFX_Close[0] = Resource_RegisterSound("SFX\Door\Door2Close1.ogg")
    DoorVariants(DOORTYPE_ALT)\SFX_Close[1] = Resource_RegisterSound("SFX\Door\Door2Close2.ogg")
    DoorVariants(DOORTYPE_ALT)\SFX_Close[2] = Resource_RegisterSound("SFX\Door\Door2Close3.ogg")

    DoorVariants(DOORTYPE_BIG) = New DoorVariant
    DoorVariants(DOORTYPE_BIG)\DoorType = DOORTYPE_BIG
    
    DoorVariants(DOORTYPE_BIG)\SFX_Open[0] = Resource_RegisterSound("SFX\Door\BigDoorOpen.ogg")
    DoorVariants(DOORTYPE_BIG)\SFX_Open[1] = Resource_RegisterSound("SFX\Door\BigDoorOpen1.ogg")
    DoorVariants(DOORTYPE_BIG)\SFX_Open[2] = Resource_RegisterSound("SFX\Door\BigDoorOpen2.ogg")

    DoorVariants(DOORTYPE_BIG)\SFX_Close[0] = Resource_RegisterSound("SFX\Door\BigDoorClose.ogg")
    DoorVariants(DOORTYPE_BIG)\SFX_Close[1] = Resource_RegisterSound("SFX\Door\BigDoorClose1.ogg")
    DoorVariants(DOORTYPE_BIG)\SFX_Close[2] = Resource_RegisterSound("SFX\Door\BigDoorClose2.ogg")

    DoorVariants(DOORTYPE_ELEVATOR) = New DoorVariant
    DoorVariants(DOORTYPE_ELEVATOR)\DoorType = DOORTYPE_ELEVATOR
    
    DoorVariants(DOORTYPE_ELEVATOR)\SFX_Open[0] = Resource_RegisterSound("SFX\Door\ElevatorOpen1.ogg")
    DoorVariants(DOORTYPE_ELEVATOR)\SFX_Open[1] = Resource_RegisterSound("SFX\Door\ElevatorOpen2.ogg")
    DoorVariants(DOORTYPE_ELEVATOR)\SFX_Open[2] = Resource_RegisterSound("SFX\Door\ElevatorOpen3.ogg")

    DoorVariants(DOORTYPE_ELEVATOR)\SFX_Close[0] = Resource_RegisterSound("SFX\Door\ElevatorClose1.ogg")
    DoorVariants(DOORTYPE_ELEVATOR)\SFX_Close[1] = Resource_RegisterSound("SFX\Door\ElevatorClose2.ogg")
    DoorVariants(DOORTYPE_ELEVATOR)\SFX_Close[2] = Resource_RegisterSound("SFX\Door\ElevatorClose3.ogg")

	SFX_Door_Wooden_Open = Resource_RegisterSound("SFX\Door\WoodenDoorOpen.ogg")
	SFX_Door_Wooden_Close = Resource_RegisterSound("SFX\Door\WoodenDoorClose.ogg")
	SFX_Door_Wooden_Budge = Resource_RegisterSound("SFX\Door\WoodenDoorBudge.ogg")

	SFX_Door_Error = Resource_RegisterSound("SFX\Door\DoorError.ogg")
	SFX_Door_Sparks = Resource_RegisterSound("SFX\Door\DoorSparks.ogg")
	SFX_Door_DistantOpen = Resource_RegisterSound("SFX\Door\Door2Open1_dist.ogg")
	SFX_Door_OpenFast = Resource_RegisterSound("SFX\Door\DoorOpenFast.ogg")

	SFX_Door_Airlock = Resource_RegisterSound("SFX\Door\Airlock.ogg")
	SFX_Door_Checkpoint = Resource_RegisterSound("SFX\Door\DoorCheckpoint.ogg")
	SFX_Door_Endroom = Resource_RegisterSound("SFX\Door\EndroomDoor.ogg")

	SFX_Door_SCP_1123_Open = Resource_RegisterSound("SFX\Door\1123DoorOpen.ogg")
    SFX_Door_SCP_079_Open = Resource_RegisterSound("SFX\Door\DoorOpen079.ogg")
	SFX_Door_SCP_079_Close = Resource_RegisterSound("SFX\Door\DoorClose079.ogg")
	SFX_Door_SCP_173_Open = Resource_RegisterSound("SFX\Door\DoorOpen173.ogg")

End Function

Function PlayDoorSFX%(doorType%, isOpening%)
    Local sfx% = GetDoorSFX(doorType, isOpening)
    if sfx >= 0 Then Return PlaySFX(sfx)
    Return -1
End Function

Function PlayDoorSFX%(doorType%, isOpening%, cam%, entity%, range#=10, volume#=1)
    Local sfx% = GetDoorSFX(doorType, isOpening)
    if sfx >= 0 Then Return PlaySFX(sfx, cam, entity, range, volume)
    Return -1
End Function

Function GetDoorSFX%(doorType%, isOpening%)
    Local door.DoorVariant = DoorVariants(doorType)

    ; Exit early if door type isn't initialized
    If door = Null Then Return -1

    If isOpening Then 
        Return door\SFX_Open[Rand(0,door\SFX_Open_UpperBound)]
    Else 
        Return door\SFX_Close[Rand(0,door\SFX_Close_UpperBound)]
    End If
End Function

; These are part of the interaction sfx grouping
Global KeyCardSFX1 
Global KeyCardSFX2 
Global ButtonSFX2 
Global ScannerSFX1
Global ScannerSFX2 

; This is part of the alarms sfx grouping
Global CautionSFX% 