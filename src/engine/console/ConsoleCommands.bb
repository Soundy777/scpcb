; ===========================================================================
; ConsoleCommands.bb
; ===========================================================================

; Core Commands
Const CMD_HELP = 1
Const CMD_CLEAR = 2
Const CMD_EXIT = 3

; Debug Commands
Const CMD_DEBUGMODE = 10
Const CMD_DEBUGHUD = 11
Const CMD_MAV = 12
Const CMD_STATUS = 13
Const CMD_INSPECT = 14
Const CMD_WIREFRAME = 15
Const CMD_FPS = 16
Const CMD_NOCLIP = 17
Const CMD_NOCLIP_SPEED = 18

; Player Commands
Const CMD_FOV = 30
Const CMD_CAMERAFOG = 31
Const CMD_GAMMA = 32
Const CMD_HIDEDISTANCE = 33
Const CMD_INJURE = 34
Const CMD_INFECT = 35
Const CMD_HEAL = 36
Const CMD_KILL = 37
Const CMD_TELEPORTXYZ = 38
Const CMD_TELEPORTTOROOM = 39
Const CMD_LISTROOMS = 40
Const CMD_SANIC = 41
Const CMD_REVIVE = 42
Const CMD_INFSTAM = 43
Const CMD_GODMODE = 44
Const CMD_NOTARGET = 45
Const CMD_SETBLINKEFFECT = 46

; Item Commands
Const CMD_SPAWNITEM = 60
Const CMD_LISTITEMS = 61
Const CMD_SPAWN420 = 62
Const CMD_SPAWNRADIO = 63
Const CMD_SPAWNNVG = 64
Const CMD_SPAWNNAV = 65

; Gameplay Commands
Const CMD_ENDING = 80
Const CMD_TOGGLEWARHEADLEVER = 81
Const CMD_UNLOCKEXITS = 82
Const CMD_SETEVENTSTATE = 83
Const CMD_GIVEACHIEVEMENT = 84

; Utility Commands
Const CMD_STOPSOUNDS = 100
Const CMD_SPAWNPARTICLES = 101
Const CMD_PLAYMUSIC = 102

; NPC Commands
Const CMD_SPAWNNPC = 120
Const CMD_173STATE = 121
Const CMD_173ENABLE = 122
Const CMD_173DISABLE = 123
Const CMD_173SPEED = 124
Const CMD_173TELEPORT = 125
Const CMD_173HALLOWEEN = 126
Const CMD_106STATE = 131
Const CMD_106ENABLE = 132
Const CMD_106DISABLE = 133
Const CMD_106SPEED = 134
Const CMD_106TELEPORT = 135
Const CMD_096STATE = 141
Const CMD_096RESET = 142
Const CMD_427STATE = 151
Const CMD_914OMNI = 161

; Joke Commands
Const CMD_TP = 1336
Const CMD_SPAWNPUMPKIN = 1337
Const CMD_JORGE = 1338

; ---------------------------------------------------------------------------

Function InitConsoleCommands()

    ; Core Commands
    RegisterConsoleCommand("help", CMD_HELP, "Lists commands", "Lists all available console commands with a short description of each.")
    RegisterConsoleCommand("clear", CMD_CLEAR, "Clears console", "Clears the console output.")
    RegisterConsoleCommand("exit", CMD_EXIT, "Exits the game", "Exits the game.")

    ; Debug Commands
    RegisterConsoleCommand("debugmode", CMD_DEBUGMODE, "Enables debug mode", "Enables debug mode, which includes godmode, infinite stamina, and some other fun features. Use with 'debugmode wireframe' to also enable wireframe mode.")
    RegisterConsoleCommand("debughud", CMD_DEBUGHUD, "Toggles debug HUD", "Toggles the debug HUD, which shows various information about the player's status and environment.")
    RegisterConsoleCommand("mav", CMD_MAV, "Forces a MAV Crash", "Causes the game to crash via a memory access violation. Useful for testing crash handling and reporting.")
    RegisterConsoleCommand("status", CMD_STATUS, "Shows player status", "Shows various information about the player's current status, such as coordinates, rotation, room, stamina, timers, injuries, and bloodloss.")
    RegisterConsoleCommand("inspect", CMD_INSPECT, "Inspects the entity in the center of the screen", "Shows information about the entity in the center of the screen, such as its texture and coordinates.")
    RegisterConsoleCommand("wireframe", CMD_WIREFRAME, "Toggles wireframe mode", "Toggles wireframe mode, which makes all entities render as wireframes. Useful for seeing through walls and understanding the geometry of the environment.")
    RegisterConsoleCommand("fps", CMD_FPS, "Toggles FPS display", "Toggles the display of the current frames per second (FPS) in the top left corner of the screen.")
    RegisterConsoleCommand("noclip", CMD_NOCLIP, "Toggles noclip mode", "Toggles noclip mode, which allows the player to fly and pass through walls.")
    RegisterConsoleCommand("noclipspeed", CMD_NOCLIP_SPEED, "Sets noclip speed", "Sets the speed of movement while in noclip mode. The default value is 0.1, and higher values will make you move faster.")

    ; Player Commands
    RegisterConsoleCommand("fov", CMD_FOV, "Sets field of view", "Sets the player's field of view (FOV) to the specified value. Higher values will give you a wider view, while lower values will give you a narrower view. Example usage: 'fov 90'")
    RegisterConsoleCommand("camerafog", CMD_CAMERAFOG, "Sets camera fog", "Sets the camera fog distances. Requires two parameters: near and far. Example usage: 'camerafog 10 50' would set the near fog distance to 10 and the far fog distance to 50.")
    RegisterConsoleCommand("gamma", CMD_GAMMA, "Sets screen gamma", "Sets the screen gamma to the specified value. Higher values will make the screen brighter, while lower values will make it darker.")
    RegisterConsoleCommand("hidedistance", CMD_HIDEDISTANCE, "Sets hide distance", "Sets the distance at which entities will be hidden. Higher values will make entities visible from farther away, while lower values will make them disappear sooner.")
    RegisterConsoleCommand("injure", CMD_INJURE, "Inflicts injuries", "Inflicts the specified amount of injuries to the player. Injuries can be healed with the 'heal' command.")
    RegisterConsoleCommand("infect", CMD_INFECT, "Inflicts infection", "Inflicts the specified amount of infection to the player. Infection can be healed with the 'heal' command.")
    RegisterConsoleCommand("heal", CMD_HEAL, "Heals injuries and diseases", "Heals all injuries and diseases from the player.")
    RegisterConsoleCommand("kill", CMD_KILL, "Kills the player", "Kills the player.")
    RegisterConsoleCommand("teleportpos", CMD_TELEPORTXYZ, "Teleports to coordinates", "Teleports the player to the specified coordinates. Requires three parameters: X, Y, and Z. Example usage: 'teleportxyz 10 0 5' would teleport the player to coordinates (10, 0, 5).")
    RegisterConsoleCommand("teleportroom", CMD_TELEPORTTOROOM, "Teleports to room", "Teleports the player to the specified room. Requires the room name as a parameter, and optionally the room index if there are multiple rooms with the same name. Example usage: 'teleporttoroom office' teleports the player to the office room. Example usage #2: 'teleporttoroom office 2' teleports the player to the second instance of the office room if multiple exist.")
    RegisterConsoleCommand("listrooms", CMD_LISTROOMS, "Lists rooms", "Lists all available rooms with their names.")
    RegisterConsoleCommand("sanic", CMD_SANIC, "Toggles Superman mode", "Toggles Superman mode, which makes the player move at super high speeds before dying shortly there after.")
    RegisterConsoleCommand("revive", CMD_REVIVE, "Revives the player", "Revives the player if they are dead, restoring them to a healthy state and moving them slightly upwards to prevent fall damage.")
    RegisterConsoleCommand("infstam", CMD_INFSTAM, "Toggles infinite stamina", "Toggles infinite stamina, which prevents the player's stamina from decreasing. Useful for testing and exploration.")
    RegisterConsoleCommand("godmode", CMD_GODMODE, "Toggles godmode", "Toggles godmode, which prevents the player from taking any damage. Useful for testing and exploration.")
    RegisterConsoleCommand("notarget", CMD_NOTARGET, "Toggles notarget mode", "Toggles notarget mode, which prevents NPCs from targeting the player. Useful for testing and exploration.")
    RegisterConsoleCommand("setblinkeffect", CMD_SETBLINKEFFECT, "Sets blink effect", "Sets the parameters for the blink effect, which is a visual effect that can be used to simulate blinking or other vision impairments. Requires two parameters: intensity and duration. Example usage: 'setblinkeffect 0.5 2' would set the intensity of the blink effect to 0.5 and the duration to 2 seconds.")

    ; Item Commands
    RegisterConsoleCommand("spawnitem", CMD_SPAWNITEM, "Spawns an item", "Spawns the specified item at the player's location. Requires the item name as a parameter. Example usage: 'spawnitem medkit' would spawn a medkit at the player's location.")
    RegisterConsoleCommand("listitems", CMD_LISTITEMS, "Lists items", "Lists all available items with their display names and internal names.")
    RegisterConsoleCommand("spawn420", CMD_SPAWN420, "Spawns 420 Joints", "Spawns 20 SCP-420-J and/or joints in a circle around the player and plays the 420 music. Use with caution, as this can cause significant lag.")
    RegisterConsoleCommand("spawnradio", CMD_SPAWNRADIO, "Spawns a fine radio", "Spawns a fine radio at the player's location.")
    RegisterConsoleCommand("spawnnvg", CMD_SPAWNNVG, "Spawns Night Vision Goggles", "Spawns a pair of night vision goggles at the player's location.")
    RegisterConsoleCommand("spawnnav", CMD_SPAWNNAV, "Spawns a nav", "Spawns a nav at the players location.")
        
    ; Gameplay Commands
    RegisterConsoleCommand("ending", CMD_ENDING, "Triggers an ending", "Triggers the specified ending. Requires the ending ID as a parameter.")
    RegisterConsoleCommand("togglewarheadlever", CMD_TOGGLEWARHEADLEVER, "Toggles warhead lever", "Toggles the state of the warhead lever in the nuke room, allowing you to test the warhead detonation sequence.")
    RegisterConsoleCommand("unlockexits", CMD_UNLOCKEXITS, "Unlocks exits", "Unlocks the exits of the facility. Use with 'unlockexits a' to unlock only Gate A, 'unlockexits b' to unlock only Gate B, or 'unlockexits all' to unlock both.")
    RegisterConsoleCommand("seteventstate", CMD_SETEVENTSTATE, "Sets event state", "Sets the state of the event in the current room. Requires three parameters: state, state2, and state3. Use 'keep' for any parameter you don't want to change. Example usage: 'seteventstate 1 keep 0' would set the event state to 1, leave state2 unchanged, and set state3 to 0.")
    RegisterConsoleCommand("giveachievement", CMD_GIVEACHIEVEMENT, "Gives an achievement", "Unlocks the specified achievement. Requires the achievement ID as a parameter. Example usage: 'giveachievement 0' would unlock the first achievement.")
    
    ; Utility Commands
    RegisterConsoleCommand("stopsounds", CMD_STOPSOUNDS, "Stops all sounds", "Stops all currently playing sounds and music. Useful for testing and for getting rid of annoying or glitched sounds.")
    RegisterConsoleCommand("spawnparticles", CMD_SPAWNPARTICLES, "Spawns particles", "Spawns a particle emitter at the player's location. Requires the particle emitter ID as a parameter. Example usage: 'spawnparticles 0' would spawn the first particle emitter.")
    RegisterConsoleCommand("playmusic", CMD_PLAYMUSIC, "Plays custom music", "Plays a custom music track from the 'SFX/Music/Custom' folder. Requires the filename of the music track as a parameter. Example usage: 'playmusic mytrack.ogg' would play 'SFX/Music/Custom/mytrack.ogg'. Use 'playmusic' with no parameters to stop playing custom music and revert to the default music.")
    
    ; NPC Commands
    RegisterConsoleCommand("spawnnpc", CMD_SPAWNNPC, "Spawns an NPC", "Spawns the specified NPC at the player's location. Requires the NPC name as a parameter, and optionally the state to spawn them in. Example usage: 'spawnnpc 096' spawns SCP-096 in its default state. Example usage #2: 'spawnnpc 096 5' spawns SCP-096 in its state 5. NPCs: 008zombie/049/049-2/066/096/106/173/178-1/372/513-1/966/1499-1/class-d/guard/mtf/apache/tentacle.")
    
    RegisterConsoleCommand("173state", CMD_173STATE, "Displays info on SCP-173", "Displays information about SCP-173, including its position, its current idle state, and its current AI state.")
    RegisterConsoleCommand("173enable", CMD_173ENABLE, "Enables SCP-173", "Enables SCP-173.")
    RegisterConsoleCommand("173disable", CMD_173DISABLE, "Disables SCP-173", "Disables SCP-173.")
    RegisterConsoleCommand("173speed", CMD_173SPEED, "Sets SCP-173 speed", "Sets the speed of SCP-173. Requires the speed value as a parameter. Example usage: '173speed 0.1' would set SCP-173's speed to 0.1, while '173speed 1' would set it to 1 (which is its default speed).")
    RegisterConsoleCommand("173teleport", CMD_173TELEPORT, "Teleports SCP-173 to the player", "Teleports SCP-173 to the player's location.")
    RegisterConsoleCommand("173halloween", CMD_173HALLOWEEN, "Toggles Halloween mode for SCP-173", "Toggles Halloween mode for SCP-173, which gives it a halloween texture.")

    RegisterConsoleCommand("106state", CMD_106STATE, "Displays info on SCP-106", "Displays information about SCP-106, , including its position, its current idle state, and its current AI state.")
    RegisterConsoleCommand("106enable", CMD_106ENABLE, "Enables SCP-106", "Enables SCP-106")
    RegisterConsoleCommand("106disable", CMD_106DISABLE, "Disables SCP-106", "Disables SCP-106")
    RegisterConsoleCommand("106speed", CMD_106SPEED, "Sets SCP-106 speed", "Sets the speed of SCP-106. Requires the speed value as a parameter. Example usage: '106speed 0.1' would set SCP-106's speed to 0.1, while '106speed 1' would set it to 1 (which is its default speed).")
    RegisterConsoleCommand("106teleport", CMD_106TELEPORT, "Teleports SCP-106 to the player", "Teleports SCP-106 to the player's location.")

    RegisterConsoleCommand("096state", CMD_096STATE, "Display info on SCP-096", "Displays information about SCP-096, including its position, its current idle state, and its current AI state.")
    RegisterConsoleCommand("096reset", CMD_096RESET, "Resets SCP-096", "Resets SCP-096 to its default state and position. Useful for testing and for getting SCP-096 unstuck if it gets stuck somewhere.")

    RegisterConsoleCommand("427state", CMD_427STATE, "Gets info on SCP-427", "Display information about SCP-427, specifically the time remaining until it kills you.")

    RegisterConsoleCommand("914omni", CMD_914OMNI, "Ensures an omni keycard spawns", "Guarantees that an omni keycard will spawn the next time you put a keycard through on fine.")
    
    ; Joke Commands
    RegisterConsoleCommand("tp", CMD_TP, "Teleports you somewhere special", "Teleports you to a special location. Where? That's a mystery!")
    RegisterConsoleCommand("spawnpumpkin", CMD_SPAWNPUMPKIN, "Spawns a pumpkin", "Which pumpkin? That's a mystery!")
    RegisterConsoleCommand("jorge", CMD_JORGE, "Jorge", "Jorge")

End Function

; ---------------------------------------------------------------------------

Function Console_DispatchCommand(commandID%, args$)

    Select commandID
        ; Core Commands
        Case CMD_HELP
            Cmd_Help(args)
        Case CMD_CLEAR
            Cmd_Clear(args)
        Case CMD_EXIT
            Cmd_Exit(args)

        ; Debug Commands
        Case CMD_DEBUGMODE
            Cmd_DebugMode(args)
        Case CMD_DEBUGHUD
            Cmd_DebugHUD(args)
        Case CMD_MAV
            Cmd_MAV(args)
        Case CMD_STATUS
            Cmd_Status(args)
        Case CMD_INSPECT
            Cmd_Inspect(args)
        Case CMD_WIREFRAME
            Cmd_Wireframe(args)
        Case CMD_FPS
            Cmd_FPS(args)
        Case CMD_NOCLIP
            Cmd_NoClip(args)
        Case CMD_NOCLIP_SPEED
            Cmd_NoClipSpeed(args)
        
        ; Player Commands
        Case CMD_FOV
            Cmd_FOV(args)
        Case CMD_CAMERAFOG
            Cmd_CameraFog(args)
        Case CMD_GAMMA
            Cmd_Gamma(args)
        Case CMD_HIDEDISTANCE
            Cmd_HideDistance(args)
        Case CMD_INJURE
            Cmd_Injure(args)
        Case CMD_INFECT 
            Cmd_Infect(args)
        Case CMD_HEAL
            Cmd_Heal(args)
        Case CMD_KILL
            Cmd_Kill(args)
        Case CMD_TELEPORTXYZ
            Cmd_TeleportXYZ(args)
        Case CMD_TELEPORTTOROOM
            Cmd_TeleportRoom(args)
        Case CMD_LISTROOMS
            Cmd_ListRooms(args)
        Case CMD_SANIC
            Cmd_Sanic(args)
        Case CMD_REVIVE
            Cmd_Revive(args)
        Case CMD_INFSTAM
            Cmd_InfStam(args)
        Case CMD_GODMODE
            Cmd_GodMode(args)
        Case CMD_NOTARGET
            Cmd_NoTarget(args)
        Case CMD_SETBLINKEFFECT
            Cmd_SetBlinkEffect(args)
        
        ; Item Commands
        Case CMD_SPAWNITEM
            Cmd_SpawnItem(args)
        Case CMD_LISTITEMS
            Cmd_ListItems(args)
        Case CMD_SPAWN420
            Cmd_Spawn420(args)
        Case CMD_SPAWNRADIO
            Cmd_SpawnRadio(args)
        Case CMD_SPAWNNVG
            Cmd_SpawnNVG(args)
        Case CMD_SPAWNNAV
            Cmd_SpawnNav(args)

        ; Gameplay Commands
        Case CMD_ENDING
            Cmd_Ending(args)
        Case CMD_TOGGLEWARHEADLEVER
            Cmd_ToggleWarheadLever(args)
        Case CMD_UNLOCKEXITS
            Cmd_UnlockExits(args)
        Case CMD_SETEVENTSTATE
            Cmd_SetEventState(args)
        Case CMD_GIVEACHIEVEMENT
            Cmd_GiveAchievement(args)

        ; Utility Commands
        Case CMD_STOPSOUNDS
            Cmd_StopSounds(args)
        Case CMD_SPAWNPARTICLES
            Cmd_SpawnParticles(args)
        Case CMD_PLAYMUSIC
            Cmd_PlayMusic(args)
        
        ; NPC Commands
        Case CMD_SPAWNNPC
            Cmd_SpawnNPC(args)
        Case CMD_173STATE
            Cmd_173_State(args)
        Case CMD_173ENABLE
            Cmd_173_Enable(args)
        Case CMD_173DISABLE
            Cmd_173_Disable(args)
        Case CMD_173SPEED
            Cmd_173_SetSpeed(args)
        Case CMD_173TELEPORT
            Cmd_173_Teleport(args)
        Case CMD_173HALLOWEEN
            Cmd_173_Halloween(args)
        Case CMD_106STATE
            Cmd_106_State(args)
        Case CMD_106ENABLE
            Cmd_106_Enable(args)
        Case CMD_106DISABLE
            Cmd_106_Disable(args)
        Case CMD_106SPEED
            Cmd_106_SetSpeed(args)
        Case CMD_106TELEPORT
            Cmd_106_Teleport(args)
        Case CMD_096STATE
            Cmd_096_State(args)
        Case CMD_096RESET
            Cmd_096_Reset(args)
        Case CMD_427STATE
            Cmd_427_State(args)
        Case CMD_914OMNI
            Cmd_914_Omni(args)

        ; Joke Commands
        case CMD_TP
            Cmd_TP(args)
        Case CMD_SPAWNPUMPKIN
            Cmd_SpawnPumpkin(args)
        Case CMD_JORGE
            Cmd_Jorge(args)

    End Select

End Function

; ===========================================================================
; Commands
; ===========================================================================
; Core Functions
; ===========================================================================

Function Cmd_Help(args$)

    ; Determine if the user is asking for help on a specific command, e.g. "help godmode" would show the long description for the godmode command. If not, just show the list of commands with their short descriptions.
    If (args$ <> "") Then
        Local foundCommand% = False
        For c.ConsoleCommand = Each ConsoleCommand
            If c\Name = Lower(args$) Then
                CreateConsoleMsg("Help:: " + c\Name + " - " + c\ShortDescription)
                CreateConsoleMsg("******************************")

                ; Split up the long description into sentences and display each on a new console line
                Local fullStr$ = c\LongDescription
                Local periodPos = 0
                Local lastPos = 1

                Repeat
                    ; Find the next full stop starting from our last position
                    periodPos = Instr(fullStr, ".", lastPos)
                    
                    If periodPos > 0 Then
                        ; Extract the sentence (including the period)
                        Local sentence$ = Mid(fullStr, lastPos, (periodPos - lastPos) + 1)
                        CreateConsoleMsg(Trim(sentence))
                        lastPos = periodPos + 1
                    Else
                        ; Pick up any remaining text after the last period (if any)
                        Local remaining$ = Mid(fullStr, lastPos)
                        If Len(Trim(remaining)) > 0 Then CreateConsoleMsg(Trim(remaining))
                        Exit ; Exit the loop
                    End If
                Forever

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

End Function

Function Cmd_Exit(args$)

End

End Function

; ===========================================================================
; Debug Functions
; ===========================================================================

Function Cmd_DebugMode(args$)

    If (args$ = "wireframe") Then

        GodMode = 1
        NoClip = 1
        WireFrame 1
        Gfx\Wireframe=1
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

Function Cmd_DebugHUD(args$)

    Select args$
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

Function Cmd_MAV(args$)

    RuntimeErrorExt("Violation Access Memory")

End Function

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

Function Cmd_Inspect(args$)

    ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 0
    c = CameraPick(Camera,Config\Graphics\ScreenWidth/2, Config\Graphics\ScreenHeight/2)
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

Function Cmd_Wireframe(args$)

    Select args$
        Case "on", "1", "true"
            WireFrame 1
            Gfx\Wireframe=1					
        Case "off", "0", "false"
            WireFrame 0
            Gfx\Wireframe=0
        Default
            Gfx\Wireframe = 1 - Gfx\Wireframe
            WireFrame Gfx\Wireframe
    End Select

    if Gfx\Wireframe = 1 Then
        CreateConsoleMsg("Wireframe mode enabled.")
    Else
        CreateConsoleMsg("Wireframe mode disabled.")
    EndIf

End Function

Function Cmd_FPS(args$)

    Config\Graphics\ShowFPS = Not Config\Graphics\ShowFPS
    CreateConsoleMsg("ShowFPS: "+Str(Config\Graphics\ShowFPS))

End Function

Function Cmd_NoClip(args$)
					
    Select args$
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

Function Cmd_NoClipSpeed(args$)

    NoClipSpeed = Float(args$)

End Function

; ===========================================================================
; Player Functions
; ===========================================================================

Function Cmd_FOV(args$)

    Config\Graphics\FOV = Int(args$)

End Function

Function Cmd_CameraFog(args$)

    CameraFogNear = Float(Left(args, Len(args) - Instr(args, " ")))
    CameraFogFar = Float(Right(args, Len(args) - Instr(args, " ")))
    CreateConsoleMsg("Near set to: " + CameraFogNear + ", far set to: " + CameraFogFar)

End Function

Function Cmd_Gamma(args$)

    Config\Graphics\ScreenGamma = Int(args$)
    CreateConsoleMsg("Gamma set to " + Config\Graphics\ScreenGamma)

End Function

Function Cmd_HideDistance(args$)

    HideDistance = Float(args$)
    CreateConsoleMsg("Hidedistance set to " + HideDistance)

End Function

Function Cmd_Injure(args$)

    Injuries = Float(args$)

End Function

Function Cmd_Infect(args$)

    Infect = Float(args$)

End Function

Function Cmd_Heal(args$)

    Injuries = 0
    Bloodloss = 0
    ResetDiseases()

End Function

Function Cmd_Kill(args$)

    KillTimer = -1
    DeathMSG = I_Loc\DeathMessage_Suicide[Rand(4)]

End Function

Function Cmd_TeleportXYZ(args$)

    teleXPos# = Float(Piece$(args$,1," "))
    teleYPos# = Float(Piece$(args$,2," "))
    teleZPos# = Float(Piece$(args$,3," "))
    PositionEntity Collider,teleXPos#,teleYPos#,teleZPos#
    PositionEntity Camera,teleXPos#,teleYPos#,teleZPos#
    ResetEntity Collider
    ResetEntity Camera
    CreateConsoleMsg("Teleported to coordinates (X|Y|Z): "+EntityX(Collider)+"|"+EntityY(Collider)+"|"+EntityZ(Collider))

End Function

Function Cmd_TeleportRoom(args$)

    Local roomName$ = Piece(args$, 1, " ")
    Local roomIndex% = Int(Piece(args$, 2, " "))

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

Function Cmd_Sanic(args$)

    SuperMan = Not SuperMan
    If SuperMan = True Then
        CreateConsoleMsg("GOTTA GO FAST")
    Else
        CreateConsoleMsg("WHOA SLOW DOWN")
    EndIf

End Function

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

Function Cmd_InfStam(args$)
					
    Select args$
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

Function Cmd_Godmode(args$)
					
    Select args$
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

Function Cmd_NoTarget(args$)
					
    Select args
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

Function Cmd_SetBlinkEffect(args$)

    BlinkEffect = Float(Left(args, Len(args) - Instr(args, " ")))
    BlinkEffectTimer = Float(Right(args, Len(args) - Instr(args, " ")))
    CreateConsoleMsg("Set BlinkEffect to: " + BlinkEffect + "and BlinkEffect timer: " + BlinkEffectTimer)

End Function

; ===========================================================================
; Item Functions
; ===========================================================================

Function Cmd_SpawnItem(args$)

    Local itt.ItemTemplates = FindItemTemplate(args$)
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

Function Cmd_SpawnNav(args$)

    it.Items = CreateItem("snavulti", EntityX(Collider), EntityY(Camera,True), EntityZ(Collider))
    EntityType(it\collider, HIT_ITEM)
    it\state = 101

End Function

; ===========================================================================
; Gameplay Functions
; ===========================================================================

Function Cmd_Ending(args$)

    SelectedEnding = args$
    KillTimer = -0.1

End Function

Function Cmd_ToggleWarheadLever(args$)

    For e.Events = Each Events
        If e\EventName = "room2nuke" Then
            e\EventState = (Not e\EventState)
            Exit
        EndIf
    Next

End Function

Function Cmd_UnlockExits(args$)
					
    Select args$
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

Function Cmd_SetEventState(args$)

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

Function Cmd_GiveAchievement(args$)
    
    If Int(args$)>=0 And Int(args$)<MAXACHIEVEMENTS
        ;; ToDo:: Fix this -- we can't called achievements as its not yet loaded as the console now loads before achievements.bb is included
        ;; We cannot move achievements higher up as there is a global variable naming conflict higher up in main.bb
        ;Achievements(Int(StrTemp))=True
        ;CreateConsoleMsg("Achievemt "+AchievementStrings(Int(StrTemp))+" unlocked.")
    Else
        CreateConsoleMsg("Achievement with ID "+Int(args$)+" doesn't exist.",255,150,0)
    EndIf

End Function

; ===========================================================================
; Utility Functions
; ===========================================================================

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

Function Cmd_SpawnParticles(args$)
    
    Local particleIDToSpawn% = Int(args$)

    If particleIDToSpawn >= 0 And particleIDToSpawn <= 2 ;<--- This is the maximum ID of particles by Devil Particle system, will be increased after time - ENDSHN
        SetEmitter(Collider,ParticleEffect[particleIDToSpawn])
        CreateConsoleMsg("Spawned particle emitter with ID " + particleIDToSpawn + " at player's position.")
    Else
        CreateConsoleMsg("Particle emitter with ID " + particleIDToSpawn + " not found.",255,150,0)
    EndIf

End Function

Function Cmd_PlayMusic(args$)
    
    If args$ <> ""
        PlayCustomMusic% = True
        If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
        If MusicCHN <> 0 Then StopChannel MusicCHN
        CustomMusic = LoadSound_Strict("SFX\Music\Custom\"+args$)
        If CustomMusic = 0
            PlayCustomMusic% = False
        EndIf
    Else
        PlayCustomMusic% = False
        If CustomMusic <> 0 Then FreeSound_Strict CustomMusic : CustomMusic = 0
        If MusicCHN <> 0 Then StopChannel MusicCHN
    EndIf

End Function

; ===========================================================================
; NPC Functions
; ===========================================================================

Function Cmd_SpawnNPC(args$)
	Local n.NPCs
	Local consoleMSG$

    Local npcToSpawn$ = Piece$(args$, 1)
    Local npcRequestedState$ = ""
    if (Piece$(args$, 2) <> "") Then npcRequestedState = Piece$(args$, 2)
	
	Select npcToSpawn$ 
		Case "008", "008zombie"
			n.NPCs = CreateNPC(NPCtype008, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			n\State = 1
			consoleMSG = "SCP-008 infected human spawned."
			
		Case "049", "scp049", "scp-049"
			n.NPCs = CreateNPC(NPCtype049, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			n\State = 1
			consoleMSG = "SCP-049 spawned."
			
		Case "049-2", "0492", "scp-049-2", "scp049-2", "049zombie"
			n.NPCs = CreateNPC(NPCtypeZombie, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			n\State = 1
			consoleMSG = "SCP-049-2 spawned."
			
		Case "066", "scp066", "scp-066"
			n.NPCs = CreateNPC(NPCtype066, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "SCP-066 spawned."
			
		Case "096", "scp096", "scp-096"
			n.NPCs = CreateNPC(NPCtype096, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			n\State = 5
			If (Curr096 = Null) Then Curr096 = n
			consoleMSG = "SCP-096 spawned."
			
		Case "106", "scp106", "scp-106", "larry"
			n.NPCs = CreateNPC(NPCtypeOldMan, EntityX(Collider), EntityY(Collider) - 0.5, EntityZ(Collider))
			n\State = -1
			consoleMSG = "SCP-106 spawned."
			
		Case "173", "scp173", "scp-173", "statue"
			n.NPCs = CreateNPC(NPCtype173, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			Curr173 = n
			If (Curr173\Idle = 3) Then Curr173\Idle = False
			consoleMSG = "SCP-173 spawned."
		Case "372", "scp372", "scp-372"
			n.NPCs = CreateNPC(NPCtype372, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "SCP-372 spawned."
			
		Case "513-1", "5131", "scp513-1", "scp-513-1"
			n.NPCs = CreateNPC(NPCtype5131, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "SCP-513-1 spawned."
			
		Case "860-2", "8602", "scp860-2", "scp-860-2"
			CreateConsoleMsg("SCP-860-2 cannot be spawned with the console. Sorry!", 255, 0, 0)
			
		Case "939", "scp939", "scp-939"
			CreateConsoleMsg("SCP-939 instances cannot be spawned with the console. Sorry!", 255, 0, 0)

		Case "966", "scp966", "scp-966"
			n.NPCs = CreateNPC(NPCtype966, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "SCP-966 instance spawned."
			
		Case "1048a", "1048-a", "scp1048-a", "scp-1048-a", "scp1048a", "scp-1048a"
			n.NPCs = CreateNPC(NPCtype1048a, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "SCP-1048-A spawned."
			
		Case "1499-1", "14991", "scp-1499-1", "scp1499-1"
			n.NPCs = CreateNPC(NPCtype1499, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "SCP-1499-1 instance spawned."
			
		Case "class-d", "classd", "d"
			n.NPCs = CreateNPC(NPCtypeD, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "D-Class spawned."
			
		Case "guard"
			n.NPCs = CreateNPC(NPCtypeGuard, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "Guard spawned."
			
		Case "mtf"
			n.NPCs = CreateNPC(NPCtypeMTF, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "MTF unit spawned."
			
		Case "apache", "helicopter"
			n.NPCs = CreateNPC(NPCtypeApache, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "Apache spawned."
			
		Case "tentacle"
			n.NPCs = CreateNPC(NPCtypeTentacle, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
			consoleMSG = "SCP-035 tentacle spawned."
			
		Case "clerk"
			n.NPCs = CreateNPC(NPCtypeClerk, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "Clerk spawned."
			
		Default 
			CreateConsoleMsg("NPC type not found.", 255, 0, 0) : Return
	End Select
	
	If n <> Null
		If npcRequestedState <> "" Then 
            n\State = Float(npcRequestedState) 
            consoleMSG = consoleMSG + " (State = " + n\State + ")"
        EndIf
	EndIf
	
	CreateConsoleMsg(consoleMSG)
	
End Function

; ---------------------------------------------------------------------------

Function Cmd_173_State(args$)

    CreateConsoleMsg("SCP-173")
    CreateConsoleMsg("Position: " + EntityX(Curr173\obj) + ", " + EntityY(Curr173\obj) + ", " + EntityZ(Curr173\obj))
    CreateConsoleMsg("Idle: " + Curr173\Idle)
    CreateConsoleMsg("State: " + Curr173\State)

End Function

Function Cmd_173_Enable(args$)

    Curr173\Idle = False
    ShowEntity Curr173\obj
    ShowEntity Curr173\Collider

End Function

Function Cmd_173_Disable(args$)

    Curr173\Idle = 3 ;This phenominal comment is brought to you by PolyFox. His absolute wisdom in this fatigue of knowledge brought about a new era of 173 state checks.
    HideEntity Curr173\obj
    HideEntity Curr173\Collider

End Function

Function Cmd_173_SetSpeed(args$)

    Curr173\Speed = Float(args$)
    CreateConsoleMsg("173's speed set to " + args$)

End Function

Function Cmd_173_Teleport(args$)

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

; ---------------------------------------------------------------------------

Function Cmd_106_State(args$)

    CreateConsoleMsg("SCP-106")
    CreateConsoleMsg("Position: " + EntityX(Curr106\obj) + ", " + EntityY(Curr106\obj) + ", " + EntityZ(Curr106\obj))
    CreateConsoleMsg("Idle: " + Curr106\Idle)
    CreateConsoleMsg("State: " + Curr106\State)

End Function

Function Cmd_106_Enable(args$)

    Curr106\Idle = False
    Contained106 = False
    ShowEntity Curr106\Collider
    ShowEntity Curr106\obj

End Function

Function Cmd_106_Disable(args$)

    Curr106\Idle = True
    Curr106\State = 200000
    Contained106 = True

End Function

Function Cmd_106_SetSpeed(args$)

    Curr106\Speed = Float(args$)
    CreateConsoleMsg("106's speed set to " + args$)

End Function

Function Cmd_106_Teleport(args$)

    Curr106\State = 0
    Curr106\Idle = False

End Function

; ---------------------------------------------------------------------------

Function Cmd_096_State(args$)

    For n.NPCs = Each NPCs
        If n\NPCtype = NPCtype096 Then
            CreateConsoleMsg("SCP-096")
            CreateConsoleMsg("Position: " + EntityX(n\obj) + ", " + EntityY(n\obj) + ", " + EntityZ(n\obj))
            CreateConsoleMsg("Idle: " + n\Idle)
            CreateConsoleMsg("State: " + n\State)
            return
        EndIf
    Next
    CreateConsoleMsg("SCP-096 has not spawned.")

End Function

Function Cmd_096_Reset(args$)

    For n.NPCs = Each NPCs
        If n\NPCtype = NPCtype096 Then
            n\State = 0
            If n\SoundChn<>0
                StopStream_Strict(n\SoundChn) : n\SoundChn=0 : n\SoundChn_isStream = False
            EndIf
            If n\SoundChn2<>0
                StopStream_Strict(n\SoundChn2) : n\SoundChn2=0 : n\SoundChn2_isStream = False
            EndIf
            return
        EndIf
    Next

End Function

; ---------------------------------------------------------------------------

Function Cmd_427_State(args$)

    I_427\Timer = Float(args$)*70.0

End Function

; ---------------------------------------------------------------------------

Function Cmd_914_Omni(args$)
					
    Select args$
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

; ===========================================================================
; Joke Functions
; ===========================================================================

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

Function Cmd_SpawnPumpkin(args$)

    CreateConsoleMsg("Which pumpkin? That's a mystery!");

End Function

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