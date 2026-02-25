; ===========================================================================
; ConsoleCommands.bb
; ===========================================================================

Const CMD_HELP = 1
Const CMD_CLEAR = 2
Const CMD_MAV = 3

; ---------------------------------------------------------------------------

Function InitConsoleCommands()

    RegisterConsoleCommand("help", CMD_HELP, "Lists commands", "Lists all available console commands with a short description of each.")
    RegisterConsoleCommand("clear", CMD_CLEAR, "Clears console", "Clears the console output.")
    RegisterConsoleCommand("mav", CMD_MAV, "Forces a MAV Crash", "Causes the game to crash via a memory access violation. Useful for testing crash handling and reporting.")

End Function

; ---------------------------------------------------------------------------

Function Console_DispatchCommand(commandID%, args$)
    Select commandID
        Case CMD_HELP
            Cmd_Help(args)
        Case CMD_CLEAR
            Cmd_Clear(args)
        Case CMD_MAV
            Cmd_MAV(args)
    End Select
End Function

; ===========================================================================
; Commands
; ===========================================================================

Function Cmd_Help(args$)

    ; Determine if the user is asking for help on a specific command, e.g. "help godmode" would show the long description for the godmode command. If not, just show the list of commands with their short descriptions.
    If (args$ <> "") Then
        Local foundCommand% = False
        For c.ConsoleCommand = Each ConsoleCommand
            If c\Name = Lower(args$) Then
                CreateConsoleMsg("Help:: " + c\Name + " - " + c\ShortDescription)
                CreateConsoleMsg("******************************")
                CreateConsoleMsg(c\LongDescription)
                CreateConsoleMsg("******************************")
                foundCommand = True
                Exit
            EndIf
        Next
        If foundCommand Then return
    EndIf

    ; If no specific command was asked about, show the list of commands with their short descriptions.
    CreateConsoleMsg("Available Commands:")
    CreateConsoleMsg("******************************")
    For c.ConsoleCommand = Each ConsoleCommand
        CreateConsoleMsg(c\Name + " - " + c\ShortDescription)
    Next
    CreateConsoleMsg("******************************")

End Function

Function Cmd_Clear(args$)

    ; Clear all console messages
    Local cm.ConsoleMsg
    While First ConsoleMsg <> Null
        cm = First ConsoleMsg
        Delete cm
    Wend

    ; Introduce an invisible console message to prevent the console from being completely empty, which can cause some rendering issues.
    CreateConsoleMsg("")

End Function

; ---------------------------------------------------------------------------

Function Cmd_DebugMode(args$)

    If (args$ = "wireframe") Then

        GodMode = 1
        NoClip = 1
        WireFrame 1
        WireframeState=1
        CameraFogNear = 15
        CameraFogFar = 20

    Else

        GodMode = 1
        InfiniteStamina = 1
        Curr173\Idle = 3
        Curr106\Idle = True
        Curr106\State = 200000
        Contained106 = True

    EndIf

End Function

Function Cmd_MAV(args$)

    RuntimeErrorExt("Violation Access Memory")

End Function

; ---------------------------------------------------------------------------

Function Cmd_Status(args$)

    ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
    CreateConsoleMsg("******************************")
    CreateConsoleMsg("Status: ")
    CreateConsoleMsg("Coordinates: ")
    CreateConsoleMsg("    - collider: "+EntityX(Collider)+", "+EntityY(Collider)+", "+EntityZ(Collider))
    CreateConsoleMsg("    - camera: "+EntityX(Camera)+", "+EntityY(Camera)+", "+EntityZ(Camera))
    
    CreateConsoleMsg("Rotation: ")
    CreateConsoleMsg("    - collider: "+EntityPitch(Collider)+", "+EntityYaw(Collider)+", "+EntityRoll(Collider))
    CreateConsoleMsg("    - camera: "+EntityPitch(Camera)+", "+EntityYaw(Camera)+", "+EntityRoll(Camera))
    
    CreateConsoleMsg("Room: "+PlayerRoom\RoomTemplate\Name)
    For ev.Events = Each Events
        If ev\room = PlayerRoom Then
            CreateConsoleMsg("Room event: "+ev\EventName)	
            CreateConsoleMsg("-    state: "+ev\EventState)
            CreateConsoleMsg("-    state2: "+ev\EventState2)	
            CreateConsoleMsg("-    state3: "+ev\EventState3)
            Exit
        EndIf
    Next
    
    CreateConsoleMsg("Room coordinates: "+Floor(EntityX(PlayerRoom\obj) / 8.0 + 0.5)+", "+ Floor(EntityZ(PlayerRoom\obj) / 8.0 + 0.5))
    CreateConsoleMsg("Stamina: "+Stamina)
    CreateConsoleMsg("Death timer: "+KillTimer)					
    CreateConsoleMsg("Blinktimer: "+BlinkTimer)
    CreateConsoleMsg("Injuries: "+Injuries)
    CreateConsoleMsg("Bloodloss: "+Bloodloss)
    CreateConsoleMsg("******************************")

End Function

; ---------------------------------------------------------------------------

Function Cmd_CameraPick(args$)

    ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
    c = CameraPick(Camera,GraphicWidth/2, GraphicHeight/2)
    If c = 0 Then
        CreateConsoleMsg("******************************")
        CreateConsoleMsg("No entity  picked")
        CreateConsoleMsg("******************************")								
    Else
        CreateConsoleMsg("******************************")
        CreateConsoleMsg("Picked entity:")
        sf = GetSurface(c,1)
        b = GetSurfaceBrush( sf )
        t = GetBrushTexture(b,0)
        texname$ =  StripPath(TextureName(t))
        CreateConsoleMsg("Texture name: "+texname)
        CreateConsoleMsg("Coordinates: "+EntityX(c)+", "+EntityY(c)+", "+EntityZ(c))
        CreateConsoleMsg("******************************")							
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_FOV(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))				
    FOV = Int(StrTemp)

End Function

; ---------------------------------------------------------------------------

Function Cmd_HideDistance(args$)

    HideDistance = Float(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    CreateConsoleMsg("Hidedistance set to "+HideDistance)

End Function

; ---------------------------------------------------------------------------

Function Cmd_Ending(args$)

    SelectedEnding = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    KillTimer = -0.1

End Function

; ---------------------------------------------------------------------------

Function Cmd_NoclipSpeed(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))	
    NoClipSpeed = Float(StrTemp)

End Function

; ---------------------------------------------------------------------------

Function Cmd_Injure(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))				
    Injuries = Float(StrTemp)

End Function

; ---------------------------------------------------------------------------

Function Cmd_Infect(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))		
    Infect = Float(StrTemp)

End Function

; ---------------------------------------------------------------------------

Function Cmd_Heal(args$)

    Injuries = 0
    Bloodloss = 0
    ResetDiseases()

End Function

; ---------------------------------------------------------------------------

Function Cmd_TeleportToRoom(args$)

    Local roomName$ = Piece(ConsoleInput, 2, " ")
    Local roomIndex% = Int(Piece(ConsoleInput, 3, " "))

    Select roomName
        Case "895", "scp-895", "room895"
            roomName = "coffin"
        Case "scp-914", "room914"
            roomName = "914"
        Case "offices", "office"
            roomName = "room2offices"
        Case "room372"
            roomName = "roompj"
        Case "room970"
            roomName = "room2storage"
    End Select
    
    Local roomFound% = False

    For r.Rooms = Each Rooms
        If r\RoomTemplate\Name = roomName Then
            If roomIndex <> 0 Then
                roomIndex = roomIndex - 1
            Else
                PositionEntity (Collider, EntityX(r\obj), EntityY(r\obj)+0.7, EntityZ(r\obj))
                ResetEntity(Collider)
                UpdateDoors()
                UpdateRooms()
                For it.Items = Each Items
                    it\disttimer = 0
                Next
                PlayerRoom = r
                roomFound = True
                Exit
            EndIf
        EndIf
    Next
    
    If Not roomFound Then CreateConsoleMsg("Room not found.",255,150,0)

End Function

Function Cmd_ListRooms(args$)

    CreateConsoleMsg("Listing rooms:")
    For rt.RoomTemplates = Each RoomTemplates
        CreateConsoleMsg("- " + rt\Name)
    Next

End Function

; ---------------------------------------------------------------------------

Function Cmd_SpawnItem(args$)

    Local itt.ItemTemplates = FindItemTemplate(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    If itt = Null Then
        CreateConsoleMsg("Item not found.",255,150,0)
    Else
        CreateConsoleMsg(itt\displayname + " spawned.")
        it.Items = CreateItem(itt\name, EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
        EntityType(it\collider, HIT_ITEM)

        If itt\name = "snavulti" Lor itt\name = "fineradio" Lor itt\name = "veryfineradio" Then
            it\state = 101
        EndIf
    End If

End Function

Function Cmd_ListItems(args$)

    CreateConsoleMsg("Listing items:")
    For itt.ItemTemplates = Each ItemTemplates
        CreateConsoleMsg("- " + itt\displayname + "(" + itt\name + ")")
    Next

End Function

; ---------------------------------------------------------------------------

Function Cmd_Wireframe(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    If StrTemp = "on" Then
        WireFrame 1
        WireframeState=1
    ElseIf StrTemp = "off" Then
        WireFrame 0
        WireframeState=0
    Else
        WireframeState = 1 - WireframeState
        WireFrame WireframeState
    EndIf

    if WireframeState = 1 Then
        CreateConsoleMsg("Wireframe mode enabled.")
    Else
        CreateConsoleMsg("Wireframe mode disabled.")
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_173State(args$)

    CreateConsoleMsg("SCP-173")
    CreateConsoleMsg("Position: " + EntityX(Curr173\obj) + ", " + EntityY(Curr173\obj) + ", " + EntityZ(Curr173\obj))
    CreateConsoleMsg("Idle: " + Curr173\Idle)
    CreateConsoleMsg("State: " + Curr173\State)

End Function

Function Cmd_Set173Speed(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    Curr173\Speed = Float(StrTemp)
    CreateConsoleMsg("173's speed set to " + StrTemp)

End Function

Function Cmd_Teleport173(args$)

    PositionEntity Curr173\Collider,EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider)
    ResetEntity Curr173\Collider

End Function

Function Cmd_173_Halloween(args$)

    HalloweenTex = Not HalloweenTex
    If HalloweenTex Then
        Local tex = LoadTexture_Strict("GFX\npcs\173h.pt", 1)
        EntityTexture Curr173\obj, tex, 0, 0
        FreeTexture tex
        CreateConsoleMsg("173 JACK-O-LANTERN ON")
    Else
        Local tex2 = LoadTexture_Strict("GFX\npcs\173texture.jpg", 1)
        EntityTexture Curr173\obj, tex2, 0, 0
        FreeTexture tex2
        CreateConsoleMsg("173 JACK-O-LANTERN OFF")
    EndIf

End Function

Function Cmd_Disable173(args$)

    Curr173\Idle = 3 ;This phenominal comment is brought to you by PolyFox. His absolute wisdom in this fatigue of knowledge brought about a new era of 173 state checks.
    HideEntity Curr173\obj
    HideEntity Curr173\Collider

End Function

Function Cmd_Enable173(args$)

    Curr173\Idle = False
    ShowEntity Curr173\obj
    ShowEntity Curr173\Collider

End Function

; ---------------------------------------------------------------------------

Function Cmd_106State(args$)

    CreateConsoleMsg("SCP-106")
    CreateConsoleMsg("Position: " + EntityX(Curr106\obj) + ", " + EntityY(Curr106\obj) + ", " + EntityZ(Curr106\obj))
    CreateConsoleMsg("Idle: " + Curr106\Idle)
    CreateConsoleMsg("State: " + Curr106\State)

End Function

Function Cmd_Set106Speed(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    Curr106\Speed = Float(StrTemp)
    CreateConsoleMsg("106's speed set to " + StrTemp)

End Function

Function Cmd_Teleport106(args$)

    Curr106\State = 0
    Curr106\Idle = False

End Function

Function Cmd_Disable106(args$)

    Curr106\Idle = True
    Curr106\State = 200000
    Contained106 = True

End Function

Function Cmd_Enable106(args$)

    Curr106\Idle = False
    Contained106 = False
    ShowEntity Curr106\Collider
    ShowEntity Curr106\obj

End Function

; ---------------------------------------------------------------------------

Function Cmd_096State(args$)

    For n.NPCs = Each NPCs
        If n\NPCtype = NPCtype096 Then
            CreateConsoleMsg("SCP-096")
            CreateConsoleMsg("Position: " + EntityX(n\obj) + ", " + EntityY(n\obj) + ", " + EntityZ(n\obj))
            CreateConsoleMsg("Idle: " + n\Idle)
            CreateConsoleMsg("State: " + n\State)
            Exit
        EndIf
    Next
    CreateConsoleMsg("SCP-096 has not spawned.")

End Function

Function Cmd_Reset096(args$)

    For n.NPCs = Each NPCs
        If n\NPCtype = NPCtype096 Then
            n\State = 0
            If n\SoundChn<>0
                StopStream_Strict(n\SoundChn) : n\SoundChn=0 : n\SoundChn_isStream = False
            EndIf
            If n\SoundChn2<>0
                StopStream_Strict(n\SoundChn2) : n\SoundChn2=0 : n\SoundChn2_isStream = False
            EndIf
            Exit
        EndIf
    Next

End Function

; ---------------------------------------------------------------------------

Function Cmd_427_State(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))				
    I_427\Timer = Float(StrTemp)*70.0

End Function

; ---------------------------------------------------------------------------

Function Cmd_Sanic(args$)

    SuperMan = Not SuperMan
    If SuperMan = True Then
        CreateConsoleMsg("GOTTA GO FAST")
    Else
        CreateConsoleMsg("WHOA SLOW DOWN")
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_Spawn420(args$)

    For i = 1 To 20
        If Rand(2)=1 Then
            it.Items = CreateItem("scp420j", EntityX(Collider,True)+Cos((360.0/20.0)*i)*Rnd(0.3,0.5), EntityY(Camera,True), EntityZ(Collider,True)+Sin((360.0/20.0)*i)*Rnd(0.3,0.5))
        Else
            it.Items = CreateItem("joint", EntityX(Collider,True)+Cos((360.0/20.0)*i)*Rnd(0.3,0.5), EntityY(Camera,True), EntityZ(Collider,True)+Sin((360.0/20.0)*i)*Rnd(0.3,0.5))
        EndIf
        EntityType (it\collider, HIT_ITEM)
    Next
    PlaySound_Strict LoadTempSound("SFX\Music\420J.ogg")

End Function

; ---------------------------------------------------------------------------

Function Cmd_Godmode(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
    Select StrTemp
        Case "on", "1", "true"
            GodMode = True						
        Case "off", "0", "false"
            GodMode = False
        Default
            GodMode = Not GodMode
    End Select

    If GodMode Then
        CreateConsoleMsg("GODMODE ON")
    Else
        CreateConsoleMsg("GODMODE OFF")	
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_Revive(args$)

    DropSpeed = -0.1
    HeadDropSpeed = 0.0
    Shake = 0
    CurrSpeed = 0
    
    HeartBeatVolume = 0
    
    CameraShake = 0
    Shake = 0
    LightFlash = 0
    BlurTimer = 0
    
    FallTimer = 0
    MenuOpen = False
    
    GodMode = 0
    NoClip = 0
    
    ShowEntity Collider
    TranslateEntity Collider, 0, 1.5, 0
    RotateEntity Collider, EntityPitch(Collider), EntityYaw(Collider), 0
    
    KillTimer = 0
    KillAnim = 0

End Function

; ---------------------------------------------------------------------------

Function Cmd_NoClip(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
    Select StrTemp
        Case "on", "1", "true"
            NoClip = True
            Playable = True
        Case "off", "0", "false"
            NoClip = False	
            RotateEntity Collider, 0, EntityYaw(Collider), 0
        Default
            NoClip = Not NoClip
            If NoClip = False Then		
                RotateEntity Collider, 0, EntityYaw(Collider), 0
            Else
                Playable = True
            EndIf
    End Select
    
    If NoClip Then
        CreateConsoleMsg("NOCLIP ON")
    Else
        CreateConsoleMsg("NOCLIP OFF")
    EndIf
    
    DropSpeed = 0

End Function

; ---------------------------------------------------------------------------

Function Cmd_ShowFPS(args$)

    ShowFPS = Not ShowFPS
    CreateConsoleMsg("ShowFPS: "+Str(ShowFPS))

End Function

; ---------------------------------------------------------------------------

Function Cmd_DebugHUD(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    Select StrTemp
        Case "on", "1", "true"
            DebugHUD = True
        Case "off", "0", "false"
            DebugHUD = False
        Default
            DebugHUD = Not DebugHUD
    End Select
    
    If DebugHUD Then
        CreateConsoleMsg("Debug Mode On")
    Else
        CreateConsoleMsg("Debug Mode Off")
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_StopSounds(args$)

    KillSounds()
					
    For e.Events = Each Events
        If e\EventName = "alarm" Then 
            If e\room\NPC[0] <> Null Then RemoveNPC(e\room\NPC[0])
            If e\room\NPC[1] <> Null Then RemoveNPC(e\room\NPC[1])
            If e\room\NPC[2] <> Null Then RemoveNPC(e\room\NPC[2])
            
            FreeEntity e\room\Objects[0] : e\room\Objects[0]=0
            FreeEntity e\room\Objects[1] : e\room\Objects[1]=0
            PositionEntity Curr173\Collider, 0,0,0
            ResetEntity Curr173\Collider
            ShowEntity Curr173\obj
            RemoveEvent(e)
            Exit
        EndIf
    Next
    CreateConsoleMsg("Stopped all sounds.")

End Function

; ---------------------------------------------------------------------------

Function Cmd_CameraFog(args$)

    args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    CameraFogNear = Float(Left(args, Len(args) - Instr(args, " ")))
    CameraFogFar = Float(Right(args, Len(args) - Instr(args, " ")))
    CreateConsoleMsg("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)

End Function

; ---------------------------------------------------------------------------

Function Cmd_Gamma(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    ScreenGamma = Int(StrTemp)
    CreateConsoleMsg("Gamma set to " + ScreenGamma)

End Function

; ---------------------------------------------------------------------------

Function Cmd_Spawn(args$)

    args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    StrTemp$ = Piece$(args$, 1)
    StrTemp2$ = Piece$(args$, 2)
    
    ;Hacky fix for when the user doesn't input a second parameter.
    If (StrTemp <> StrTemp2) Then
        Console_SpawnNPC(StrTemp, StrTemp2)
    Else
        Console_SpawnNPC(StrTemp)
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_InfStam(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
    Select StrTemp
        Case "on", "1", "true"
            InfiniteStamina% = True						
        Case "off", "0", "false"
            InfiniteStamina% = False
        Default
            InfiniteStamina% = Not InfiniteStamina%
    End Select
    
    If InfiniteStamina
        CreateConsoleMsg("INFINITE STAMINA ON")
    Else
        CreateConsoleMsg("INFINITE STAMINA OFF")	
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_ToggleWarheadLever(args$)

    For e.Events = Each Events
        If e\EventName = "room2nuke" Then
            e\EventState = (Not e\EventState)
            Exit
        EndIf
    Next

End Function

; ---------------------------------------------------------------------------

Function Cmd_UnlockExits(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
    Select StrTemp
        Case "a"
            For e.Events = Each Events
                If e\EventName = "gateaentrance" Then
                    e\EventState3 = 1
                    e\room\RoomDoors[1]\open = True
                    Exit
                EndIf
            Next
            CreateConsoleMsg("Gate A is now unlocked.")	
        Case "b"
            For e.Events = Each Events
                If e\EventName = "exit1" Then
                    e\EventState3 = 1
                    e\room\RoomDoors[4]\open = True
                    Exit
                EndIf
            Next	
            CreateConsoleMsg("Gate B is now unlocked.")	
        Default
            For e.Events = Each Events
                If e\EventName = "gateaentrance" Then
                    e\EventState3 = 1
                    e\room\RoomDoors[1]\open = True
                ElseIf e\EventName = "exit1" Then
                    e\EventState3 = 1
                    e\room\RoomDoors[4]\open = True
                EndIf
            Next
            CreateConsoleMsg("Gate A and B are now unlocked.")	
    End Select
    
    RemoteDoorOn = True

End Function

; ---------------------------------------------------------------------------

Function Cmd_Kill(args$)

    KillTimer = -1
    DeathMSG = I_Loc\DeathMessage_Suicide[Rand(4)]

End Function

; ---------------------------------------------------------------------------

Function Cmd_PlayMusic(args$)

    ; I think this might be broken since the FMod library streaming was added. -Mark
    If Instr(ConsoleInput, " ")<>0 Then
        StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    Else
        StrTemp$ = ""
    EndIf
    
    If StrTemp$ <> ""
        PlayCustomMusic% = True
        If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
        If MusicCHN <> 0 Then StopChannel MusicCHN
        CustomMusic = LoadSound_Strict("SFX\Music\Custom\"+StrTemp$)
        If CustomMusic = 0
            PlayCustomMusic% = False
        EndIf
    Else
        PlayCustomMusic% = False
        If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
        If MusicCHN <> 0 Then StopChannel MusicCHN
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_TP(args$)

    For n.NPCs = Each NPCs
        If n\NPCtype = NPCtypeMTF
            If n\MTFLeader = Null
                PositionEntity Collider,EntityX(n\Collider),EntityY(n\Collider)+5,EntityZ(n\Collider)
                ResetEntity Collider
                Exit
            EndIf
        EndIf
    Next

End Function

; ---------------------------------------------------------------------------

Function Cmd_Tele(args$)

    args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    StrTemp$ = Piece$(args$,1," ")
    StrTemp2$ = Piece$(args$,2," ")
    StrTemp3$ = Piece$(args$,3," ")
    PositionEntity Collider,Float(StrTemp$),Float(StrTemp2$),Float(StrTemp3$)
    PositionEntity Camera,Float(StrTemp$),Float(StrTemp2$),Float(StrTemp3$)
    ResetEntity Collider
    ResetEntity Camera
    CreateConsoleMsg("Teleported to coordinates (X|Y|Z): "+EntityX(Collider)+"|"+EntityY(Collider)+"|"+EntityZ(Collider))

End Function

; ---------------------------------------------------------------------------

Function Cmd_NoTarget(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
    Select StrTemp
        Case "on", "1", "true"
            NoTarget% = True						
        Case "off", "0", "false"
            NoTarget% = False	
        Default
            NoTarget% = Not NoTarget%
    End Select
    
    If NoTarget% = False Then
        CreateConsoleMsg("NOTARGET OFF")
    Else
        CreateConsoleMsg("NOTARGET ON")	
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_SpawnRadio(args$)

    it.Items = CreateItem("fineradio", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
    EntityType(it\collider, HIT_ITEM)
    it\state = 101

End Function

Function Cmd_SpawnNVG(args$)

    it.Items = CreateItem("nvgoggles", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
    EntityType(it\collider, HIT_ITEM)
    it\state = 1000

End Function

Function Cmd_SpawnPumpkin(args$)

    CreateConsoleMsg("What pumpkin?")

End Function

Function Cmd_SpawnNav(args$)

    it.Items = CreateItem("snavulti", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
    EntityType(it\collider, HIT_ITEM)
    it\state = 101

End Function

; ---------------------------------------------------------------------------

Function Cmd_SetEventState(args$)

    args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    StrTemp$ = Piece$(args$,1," ")
    StrTemp2$ = Piece$(args$,2," ")
    StrTemp3$ = Piece$(args$,3," ")
    Local pl_room_found% = False
    If StrTemp="" Or StrTemp2="" Or StrTemp3=""
        CreateConsoleMsg("Too few parameters. This command requires 3.",255,150,0)
    Else
        For e.Events = Each Events
            If e\room = PlayerRoom
                If Lower(StrTemp)<>"keep"
                    e\EventState = Float(StrTemp)
                EndIf
                If Lower(StrTemp2)<>"keep"
                    e\EventState2 = Float(StrTemp2)
                EndIf
                If Lower(StrTemp3)<>"keep"
                    e\EventState3 = Float(StrTemp3)
                EndIf
                CreateConsoleMsg("Changed event states from current player room to: "+e\EventState+"|"+e\EventState2+"|"+e\EventState3)
                pl_room_found = True
                Exit
            EndIf
        Next
        If (Not pl_room_found)
            CreateConsoleMsg("The current room doesn't has any event applied.",255,150,0)
        EndIf
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_SpawnParticles(args$)

    If Instr(ConsoleInput, " ")<>0 Then
        StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    Else
        StrTemp$ = ""
    EndIf
    
    If Int(StrTemp) >= 0 And Int(StrTemp) <= 2 ;<--- This is the maximum ID of particles by Devil Particle system, will be increased after time - ENDSHN
        SetEmitter(Collider,ParticleEffect[Int(StrTemp)])
        CreateConsoleMsg("Spawned particle emitter with ID "+Int(StrTemp)+" at player's position.")
    Else
        CreateConsoleMsg("Particle emitter with ID "+Int(StrTemp)+" not found.",255,150,0)
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_GiveAchievement(args$)

    If Instr(ConsoleInput, " ")<>0 Then
        StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    Else
        StrTemp$ = ""
    EndIf
    
    If Int(StrTemp)>=0 And Int(StrTemp)<MAXACHIEVEMENTS
        ;; ToDo:: Fix this -- we can't called achievements as its not yet loaded as the console now loads before achievements.bb is included
        ;; We cannot move achievements higher up as there is a global variable naming conflict higher up in main.bb
        ;Achievements(Int(StrTemp))=True
        ;CreateConsoleMsg("Achievemt "+AchievementStrings(Int(StrTemp))+" unlocked.")
    Else
        CreateConsoleMsg("Achievement with ID "+Int(StrTemp)+" doesn't exist.",255,150,0)
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_SetBlinkEffect(args$)

    args$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
    BlinkEffect = Float(Left(args, Len(args) - Instr(args, " ")))
    BlinkEffectTimer = Float(Right(args, Len(args) - Instr(args, " ")))
    CreateConsoleMsg("Set BlinkEffect to: " + BlinkEffect + "and BlinkEffect timer: " + BlinkEffectTimer)

End Function

; ---------------------------------------------------------------------------

Function Cmd_Omni(args$)

    StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					
    Select StrTemp
        Case "on", "1", "true"
            GuaranteedOmni% = True						
        Case "off", "0", "false"
            GuaranteedOmni% = False	
        Default
            GuaranteedOmni% = Not GuaranteedOmni%
    End Select
    
    If GuaranteedOmni% Then
        CreateConsoleMsg("GUARANTEED KEY CARD OMNI ON")	
    Else
        CreateConsoleMsg("GUARANTEED KEY CARD OMNI OFF")
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Cmd_Jorge(args$)

    CreateConsoleMsg(Chr(74)+Chr(79)+Chr(82)+Chr(71)+Chr(69)+Chr(32)+Chr(72)+Chr(65)+Chr(83)+Chr(32)+Chr(66)+Chr(69)+Chr(69)+Chr(78)+Chr(32)+Chr(69)+Chr(88)+Chr(80)+Chr(69)+Chr(67)+Chr(84)+Chr(73)+Chr(78)+Chr(71)+Chr(32)+Chr(89)+Chr(79)+Chr(85)+Chr(46))
    ; Return
    ; ConsoleFlush = True 
    
    ; If ConsoleFlushSnd = 0 Then
    ;     ConsoleFlushSnd = LoadSound(Chr(83)+Chr(70)+Chr(88)+Chr(92)+Chr(83)+Chr(67)+Chr(80)+Chr(92)+Chr(57)+Chr(55)+Chr(48)+Chr(92)+Chr(116)+Chr(104)+Chr(117)+Chr(109)+Chr(98)+Chr(115)+Chr(46)+Chr(100)+Chr(98))
    ;     ;FMOD_Pause(MusicCHN)
    ;     ;FSOUND_Stream_Stop()
    ;     ConsoleMusFlush% = LoadSound(Chr(83)+Chr(70)+Chr(88)+Chr(92)+Chr(77)+Chr(117)+Chr(115)+Chr(105)+Chr(99)+Chr(92)+Chr(116)+Chr(104)+Chr(117)+Chr(109)+Chr(98)+Chr(115)+Chr(46)+Chr(100)+Chr(98))
    ;     ConsoleMusPlay = PlaySound(ConsoleMusFlush)
    ; Else
    ;     CreateConsoleMsg(Chr(74)+Chr(32)+Chr(79)+Chr(32)+Chr(82)+Chr(32)+Chr(71)+Chr(32)+Chr(69)+Chr(32)+Chr(32)+Chr(67)+Chr(32)+Chr(65)+Chr(32)+Chr(78)+Chr(32)+Chr(78)+Chr(32)+Chr(79)+Chr(32)+Chr(84)+Chr(32)+Chr(32)+Chr(66)+Chr(32)+Chr(69)+Chr(32)+Chr(32)+Chr(67)+Chr(32)+Chr(79)+Chr(32)+Chr(78)+Chr(32)+Chr(84)+Chr(32)+Chr(65)+Chr(32)+Chr(73)+Chr(32)+Chr(78)+Chr(32)+Chr(69)+Chr(32)+Chr(68)+Chr(46))
    ; EndIf

End Function