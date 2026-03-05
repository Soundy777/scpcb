; ---------------------------------------------------------------------------
; DOOR Sounds
; ---------------------------------------------------------------------------
Type SFX_Door
	Field SCP_1123_Open%
	Field Airlock%
	Field Big_Close_1%
	Field Big_Close_2%
	Field Big_Close_3%
	Field Big_Open_1%
	Field Big_Open_2%
	Field Big_Open_3%
	Field Alt_Close_1%
	Field Alt_Close_2%
	Field Alt_Close_3%
	Field Alt_DistantOpen%
	Field Alt_Open_1%
	Field Alt_Open_2%
	Field Alt_Open_3%
	Field Checkpoint%
	Field Close_1%
	Field Close_2%
	Field Close_3%
	Field SCP_079_Close%
	Field Error%
	Field Open_1%
	Field Open_2%
	Field Open_3%
	Field SCP_079_Open%
	Field SCP_173_Open%
	Field OpenFast%
	Field Sparks%
	Field Elevator_Close_1%
	Field Elevator_Close_2%
	Field Elevator_Close_3%
	Field Elevator_Open_1%
	Field Elevator_Open_2%
	Field Elevator_Open_3%
	Field Endroom%
	Field Wooden_Budge%
	Field Wooden_Close%
	Field Wooden_Open%
End Type

Global DoorSFX.SFX_Door = New SFX_Door

Function RegisterDoorsounds()
	Resource_RegisterSound(DoorSFX\SCP_1123_Open, "SFX\Door\1123DoorOpen.ogg")
	Resource_RegisterSound(DoorSFX\Airlock, "SFX\Door\Airlock.ogg")

	Resource_RegisterSound(DoorSFX\Big_Close_1, "SFX\Door\BigDoorClose.ogg")
	Resource_RegisterSound(DoorSFX\Big_Close_2, "SFX\Door\BigDoorClose1.ogg")
	Resource_RegisterSound(DoorSFX\Big_Close_3, "SFX\Door\BigDoorClose2.ogg")

	Resource_RegisterSound(DoorSFX\Big_Open_1, "SFX\Door\BigDoorOpen.ogg")
	Resource_RegisterSound(DoorSFX\Big_Open_2, "SFX\Door\BigDoorOpen1.ogg")
	Resource_RegisterSound(DoorSFX\Big_Open_3, "SFX\Door\BigDoorOpen2.ogg")

	Resource_RegisterSound(DoorSFX\Alt_Close_1, "SFX\Door\Door2Close1.ogg")
	Resource_RegisterSound(DoorSFX\Alt_Close_2, "SFX\Door\Door2Close2.ogg")
	Resource_RegisterSound(DoorSFX\Alt_Close_3, "SFX\Door\Door2Close3.ogg")

	Resource_RegisterSound(DoorSFX\Alt_DistantOpen, "SFX\Door\Door2Open1_dist.ogg")
	Resource_RegisterSound(DoorSFX\Alt_Open_1, "SFX\Door\Door2Open1.ogg")
	Resource_RegisterSound(DoorSFX\Alt_Open_2, "SFX\Door\Door2Open2.ogg")
	Resource_RegisterSound(DoorSFX\Alt_Open_3, "SFX\Door\Door2Open3.ogg")

	Resource_RegisterSound(DoorSFX\Checkpoint, "SFX\Door\DoorCheckpoint.ogg")

	Resource_RegisterSound(DoorSFX\Close_1, "SFX\Door\DoorClose1.ogg")
	Resource_RegisterSound(DoorSFX\Close_2, "SFX\Door\DoorClose2.ogg")
	Resource_RegisterSound(DoorSFX\Close_3, "SFX\Door\DoorClose3.ogg")

	Resource_RegisterSound(DoorSFX\SCP_079_Close, "SFX\Door\DoorClose079.ogg")

	Resource_RegisterSound(DoorSFX\Error, "SFX\Door\DoorError.ogg")

	Resource_RegisterSound(DoorSFX\Open_1, "SFX\Door\DoorOpen1.ogg")
	Resource_RegisterSound(DoorSFX\Open_2, "SFX\Door\DoorOpen2.ogg")
	Resource_RegisterSound(DoorSFX\Open_3, "SFX\Door\DoorOpen3.ogg")

	Resource_RegisterSound(DoorSFX\SCP_079_Open, "SFX\Door\DoorOpen079.ogg")
	Resource_RegisterSound(DoorSFX\SCP_173_Open, "SFX\Door\DoorOpen173.ogg")

	Resource_RegisterSound(DoorSFX\OpenFast, "SFX\Door\DoorOpenFast.ogg")
	Resource_RegisterSound(DoorSFX\Sparks, "SFX\Door\DoorSparks.ogg")

	Resource_RegisterSound(DoorSFX\Elevator_Close_1, "SFX\Door\ElevatorClose1.ogg")
	Resource_RegisterSound(DoorSFX\Elevator_Close_2, "SFX\Door\ElevatorClose2.ogg")
	Resource_RegisterSound(DoorSFX\Elevator_Close_3, "SFX\Door\ElevatorClose3.ogg")

	Resource_RegisterSound(DoorSFX\Elevator_Open_1, "SFX\Door\ElevatorOpen1.ogg")
	Resource_RegisterSound(DoorSFX\Elevator_Open_2, "SFX\Door\ElevatorOpen2.ogg")
	Resource_RegisterSound(DoorSFX\Elevator_Open_3, "SFX\Door\ElevatorOpen3.ogg")

	Resource_RegisterSound(DoorSFX\Endroom, "SFX\Door\EndroomDoor.ogg")

	Resource_RegisterSound(DoorSFX\Wooden_Budge, "SFX\Door\WoodenDoorBudge.ogg")
	Resource_RegisterSound(DoorSFX\Wooden_Close, "SFX\Door\WoodenDoorClose.ogg")
	Resource_RegisterSound(DoorSFX\Wooden_Open, "SFX\Door\WoodenDoorOpen.ogg")
End Function

;; ToDo:: migrate all current door globals into here
;; Find/Replace them with the new DoorSFX type 
;; Remove any left over initialization for them
Dim OpenDoorSFX%(3,3)
Dim CloseDoorSFX%(3,3)
Global KeyCardSFX1 
Global KeyCardSFX2 
Global ButtonSFX2 
Global ScannerSFX1
Global ScannerSFX2 
Global OpenDoorFastSFX
Global CautionSFX% 
; ---------------------------------------------------------------------------
Const SFX_INTERACT_BUTTON_1% = 1
Function Game_RegisterSounds()

	RegisterDoorsounds()

	Resource_RegisterSound(SFX_INTERACT_BUTTON_1, "SFX\Interact\Button.ogg")

End Function