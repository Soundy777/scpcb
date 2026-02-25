; ===========================================================================
; ConsoleCore.bb
; ===========================================================================

Include "src/console/ConsoleCommands.bb"

; ===========================================================================

Type ConsoleMsg
	Field txt$
	Field isCommand%
	Field r%,g%,b%
End Type

Type ConsoleCommand
    Field Name$
    Field CommandID#
	Field ShortDescription$
	Field LongDescription$
End Type

Global Console_IsOpen%, ConsoleInput$
Global ConsoleScroll#,ConsoleScrollDragging%
Global ConsoleMouseMem%
Global ConsoleReissue.ConsoleMsg = Null
Global ConsoleR% = 0,ConsoleG% = 255,ConsoleB% = 255

Global CanOpenConsole%

; ---------------------------------------------------------------------------

Function InitConsole()
	Console_IsOpen = False
	CanOpenConsole = Options\CanOpenConsole

	InitConsoleCommands()

	CreateConsoleMsg("Console enabled. Type 'help' for a list of commands.")
End Function

; ---------------------------------------------------------------------------

Function RegisterConsoleCommand(name$, commandID#, shortDescription$, longDescription$)

    c.ConsoleCommand = New ConsoleCommand
    c\Name = Lower(name)
    c\CommandID = commandID
    c\ShortDescription = shortDescription
    c\LongDescription = longDescription

End Function

; ---------------------------------------------------------------------------

Function ExecuteConsoleCommand(input$)

    If input$ = "" Then Return

    CreateConsoleMsg("> " + input$)

    Local cmd$ = Lower(input$)
    Local spacePos% = Instr(cmd$, " ")
    Local args$ = ""

    If spacePos > 0 Then
        args = Mid(input$, spacePos + 1)
        cmd = Left(cmd$, spacePos - 1)
    EndIf

	Local commandID% = 0
    For c.ConsoleCommand = Each ConsoleCommand
		If Lower(c\Name) = Lower(cmd$) Then
			commandID = c\CommandID
			Exit
		EndIf
	Next

	If commandID = 0 Then
    	CreateConsoleMsg("Unknown command: " + cmd$)
		CreateConsoleMsg("Type 'help' for a list of available commands.")
	Else
		Console_DispatchCommand(commandID, args)
	End If

End Function

; ---------------------------------------------------------------------------

Function CreateConsoleMsg(txt$,r%=-1,g%=-1,b%=-1,isCommand%=False)
	Local c.ConsoleMsg = New ConsoleMsg
	Insert c Before First ConsoleMsg
	
	c\txt = txt
	c\isCommand = isCommand
	
	c\r = r
	c\g = g
	c\b = b
	
	If (c\r<0) Then c\r = ConsoleR
	If (c\g<0) Then c\g = ConsoleG
	If (c\b<0) Then c\b = ConsoleB
End Function

; ---------------------------------------------------------------------------

Function UpdateConsole()
	Local e.Events
	
	If CanOpenConsole = False Then
		Console_IsOpen = False
		Return
	EndIf
	
	If Console_IsOpen Then
		Local cm.ConsoleMsg
		
		SetFont ConsoleFont
		
		ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255
		
		Local x% = 0, y% = GraphicHeight-300*MenuScale, width% = GraphicWidth, height% = 300*MenuScale-30*MenuScale
		Local StrTemp$, temp%,  i%
		Local ev.Events, r.Rooms, it.Items
		
		DrawFrame x,y,width,height+30*MenuScale
		
		Local consoleHeight% = 0
		Local scrollbarHeight% = 0
		For cm.ConsoleMsg = Each ConsoleMsg
			consoleHeight = consoleHeight + 15*MenuScale
		Next
		scrollbarHeight = (Float(height)/Float(consoleHeight))*height
		If scrollbarHeight>height Then scrollbarHeight = height
		If consoleHeight<height Then consoleHeight = height
		
		Color 50,50,50
		inBar% = MouseOn(x+width-26*MenuScale,y,26*MenuScale,height)
		If inBar Then Color 70,70,70
		Rect x+width-26*MenuScale,y,26*MenuScale,height,True
		
		
		Color 120,120,120
		inBox% = MouseOn(x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight)
		If inBox Then Color 200,200,200
		If ConsoleScrollDragging Then Color 255,255,255
		Rect x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight,True
		
		If Not MouseDown(1) Then
			ConsoleScrollDragging=False
		ElseIf ConsoleScrollDragging Then
			ConsoleScroll = ConsoleScroll+((ScaledMouseY()-ConsoleMouseMem)*height/scrollbarHeight)
			ConsoleMouseMem = ScaledMouseY()
		EndIf
		
		If (Not ConsoleScrollDragging) Then
			If MouseHit1 Then
				If inBox Then
					ConsoleScrollDragging=True
					ConsoleMouseMem = ScaledMouseY()
				ElseIf inBar Then
					ConsoleScroll = ConsoleScroll+((ScaledMouseY()-(y+height))*consoleHeight/height+(height/2))
					ConsoleScroll = ConsoleScroll/2
				EndIf
			EndIf
		EndIf
		
		ConsoleScroll = ConsoleScroll - 15*MenuScale * MouseZSpeed()
		
		Local reissuePos%
		If KeyHit(200) Then
			reissuePos% = 0
			If (ConsoleReissue=Null) Then
				ConsoleReissue=First ConsoleMsg
				
				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - 15*MenuScale
					ConsoleReissue = After ConsoleReissue
				Wend
				
			Else
				cm.ConsoleMsg = First ConsoleMsg
				While cm<>Null
					If cm=ConsoleReissue Then Exit
					reissuePos = reissuePos-15*MenuScale
					cm = After cm
				Wend
				ConsoleReissue = After ConsoleReissue
				reissuePos = reissuePos-15*MenuScale
				
				While True
					If (ConsoleReissue=Null) Then
						ConsoleReissue=First ConsoleMsg
						reissuePos = 0
					EndIf
				
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos - 15*MenuScale
					ConsoleReissue = After ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue<>Null Then
				ConsoleInput = ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
			EndIf
		EndIf
		
		If KeyHit(208) Then
			reissuePos% = -consoleHeight+15*MenuScale
			If (ConsoleReissue=Null) Then
				ConsoleReissue=Last ConsoleMsg
				
				While (ConsoleReissue<>Null)
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + 15*MenuScale
					ConsoleReissue = Before ConsoleReissue
				Wend
				
			Else
				cm.ConsoleMsg = Last ConsoleMsg
				While cm<>Null
					If cm=ConsoleReissue Then Exit
					reissuePos = reissuePos+15*MenuScale
					cm = Before cm
				Wend
				ConsoleReissue = Before ConsoleReissue
				reissuePos = reissuePos+15*MenuScale
				
				While True
					If (ConsoleReissue=Null) Then
						ConsoleReissue=Last ConsoleMsg
						reissuePos=-consoleHeight+15*MenuScale
					EndIf
				
					If (ConsoleReissue\isCommand) Then
						Exit
					EndIf
					reissuePos = reissuePos + 15*MenuScale
					ConsoleReissue = Before ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue<>Null Then
				ConsoleInput = ConsoleReissue\txt
				ConsoleScroll = reissuePos+(height/2)
			EndIf
		EndIf
		
		If ConsoleScroll<-consoleHeight+height Then ConsoleScroll = -consoleHeight+height
		If ConsoleScroll>0 Then ConsoleScroll = 0
		
		Color 255, 255, 255
		
		SelectedInputBox = 2
		Local oldConsoleInput$ = ConsoleInput
		ConsoleInput = InputBox(x, y + height, width, 30*MenuScale, ConsoleInput, 2, -1)
		If oldConsoleInput<>ConsoleInput Then
			ConsoleReissue = Null
		EndIf
		ConsoleInput = Left(ConsoleInput, 100)
		
		If KeyHit(28) And ConsoleInput <> "" Then
			UsedConsole = True
			ConsoleReissue = Null
			ConsoleScroll = 0

			ExecuteConsoleCommand(ConsoleInput)

			If (False) Then

			CreateConsoleMsg(ConsoleInput,255,255,0,True)
			If Instr(ConsoleInput, " ") > 0 Then
				StrTemp$ = Lower(Left(ConsoleInput, Instr(ConsoleInput, " ") - 1))
			Else
				StrTemp$ = Lower(ConsoleInput)
			End If
			
			Select Lower(StrTemp)
				Case "help"
					If Instr(ConsoleInput, " ")<>0 Then
						StrTemp$ = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
					Else
						StrTemp$ = ""
					EndIf
					ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
					
					Select Lower(StrTemp)
						Case "1",""
							CreateConsoleMsg("LIST OF COMMANDS - PAGE 1/3")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("- asd")
							CreateConsoleMsg("- status")
							CreateConsoleMsg("- noclip")
							CreateConsoleMsg("- noclipspeed")
							CreateConsoleMsg("- godmode")
							CreateConsoleMsg("- revive")
							CreateConsoleMsg("- injure [value]")
							CreateConsoleMsg("- infect [value]")
							CreateConsoleMsg("- heal")
							CreateConsoleMsg("- infinitestamina")
							CreateConsoleMsg("- sanic")
							CreateConsoleMsg("- notarget")
							CreateConsoleMsg("- teleport [room name] [index]")
							CreateConsoleMsg("- roomlist")
							CreateConsoleMsg("- spawnitem [item name]")
							CreateConsoleMsg("- itemlist")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Use "+Chr(34)+"help 2/3"+Chr(34)+" to find more commands.")
							CreateConsoleMsg("Use "+Chr(34)+"help [command name]"+Chr(34)+" to get more information about a command.")
							CreateConsoleMsg("******************************")
						Case "2"
							CreateConsoleMsg("LIST OF COMMANDS - PAGE 2/3")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("- spawn [npc type] [state]")
							CreateConsoleMsg("- 096state")
							CreateConsoleMsg("- reset096")
							CreateConsoleMsg("- 106state")
							CreateConsoleMsg("- 106speed")
							CreateConsoleMsg("- disable106")
							CreateConsoleMsg("- enable106")
							CreateConsoleMsg("- 173speed")
							CreateConsoleMsg("- 173state")
							CreateConsoleMsg("- disable173")
							CreateConsoleMsg("- enable173")
							CreateConsoleMsg("- halloween")
							CreateConsoleMsg("- scp-420-j")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Use "+Chr(34)+"help [command name]"+Chr(34)+" to get more information about a command.")
							CreateConsoleMsg("******************************")
						Case "3"
							CreateConsoleMsg("LIST OF COMMANDS - PAGE 3/3")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("- wireframe")
							CreateConsoleMsg("- showfps")
							CreateConsoleMsg("- debughud")
							CreateConsoleMsg("- camerafog [near] [far]")
							CreateConsoleMsg("- fov [value]")
							CreateConsoleMsg("- gamma [value]")
							CreateConsoleMsg("- playmusic [clip + .wav/.ogg]")
							CreateConsoleMsg("- camerapick")
							CreateConsoleMsg("- ending")
							CreateConsoleMsg("- unlockexits")
							CreateConsoleMsg("- omni")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Use "+Chr(34)+"help [command name]"+Chr(34)+" to get more information about a command.")
							CreateConsoleMsg("******************************")
						Case "asd"
							CreateConsoleMsg("HELP - asd")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Actives godmode, noclip, wireframe and")
							CreateConsoleMsg("sets fog distance to 20 near, 30 far")
							CreateConsoleMsg("******************************")
						Case "camerafog"
							CreateConsoleMsg("HELP - camerafog")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Sets the draw distance of the fog.")
							CreateConsoleMsg("The fog begins generating at 'CameraFogNear' units")
							CreateConsoleMsg("away from the camera and becomes completely opaque")
							CreateConsoleMsg("at 'CameraFogFar' units away from the camera.")
							CreateConsoleMsg("Example: camerafog 20 40")
							CreateConsoleMsg("******************************")
						Case "gamma"
							CreateConsoleMsg("HELP - gamma")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Sets the gamma correction.")
							CreateConsoleMsg("Should be set to a value between 0.0 and 2.0.")
							CreateConsoleMsg("Default is 1.0.")
							CreateConsoleMsg("******************************")
						Case "noclip","fly"
							CreateConsoleMsg("HELP - noclip")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Toggles noclip, unless a valid parameter")
							CreateConsoleMsg("is specified (on/off).")
							CreateConsoleMsg("Allows the camera to move in any direction while")
							CreateConsoleMsg("bypassing collision.")
							CreateConsoleMsg("******************************")
						Case "godmode","god"
							CreateConsoleMsg("HELP - godmode")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Toggles godmode, unless a valid parameter")
							CreateConsoleMsg("is specified (on/off).")
							CreateConsoleMsg("Prevents player death under normal circumstances.")
							CreateConsoleMsg("******************************")
						Case "wireframe"
							CreateConsoleMsg("HELP - wireframe")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Toggles wireframe, unless a valid parameter")
							CreateConsoleMsg("is specified (on/off).")
							CreateConsoleMsg("Allows only the edges of geometry to be rendered,")
							CreateConsoleMsg("making everything else transparent.")
							CreateConsoleMsg("******************************")
						Case "spawnitem"
							CreateConsoleMsg("HELP - spawnitem")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Spawns an item at the player's location.")
							CreateConsoleMsg("Any name that can appear in your inventory")
							CreateConsoleMsg("is a valid parameter.")
							CreateConsoleMsg("Example: spawnitem Key Card Omni")
							CreateConsoleMsg("******************************")
						Case "itemlist", "items"
							CreateConsoleMsg("HELP - itemlist")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Lists all currently loaded item templates.")
							CreateConsoleMsg("******************************")
						Case "spawn"
							CreateConsoleMsg("HELP - spawn")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Spawns an NPC at the player's location.")
							CreateConsoleMsg("Valid parameters are:")
							CreateConsoleMsg("008zombie / 049 / 049-2 / 066 / 096 / 106 / 173")
							CreateConsoleMsg("/ 178-1 / 372 / 513-1 / 966 / 1499-1 / class-d")
							CreateConsoleMsg("/ guard / mtf / apache / tentacle")
							CreateConsoleMsg("******************************")
						Case "revive","undead","resurrect"
							CreateConsoleMsg("HELP - revive")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Resets the player's death timer after the dying")
							CreateConsoleMsg("animation triggers.")
							CreateConsoleMsg("Does not affect injury, blood loss")
							CreateConsoleMsg("or 008 infection values.")
							CreateConsoleMsg("******************************")
						Case "teleport"
							CreateConsoleMsg("HELP - teleport")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Teleports the player to the first (or specified)")
							CreateConsoleMsg("instance of the specified room. Any room that")
							CreateConsoleMsg("appears in rooms.ini is a valid parameter.")
							CreateConsoleMsg("******************************")
						Case "roomlist", "rooms"
							CreateConsoleMsg("HELP - roomlist")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Lists all currently loaded room templates.")
							CreateConsoleMsg("Not all room templates may appear in the map.")
							CreateConsoleMsg("******************************")
						Case "stopsound", "stfu"
							CreateConsoleMsg("HELP - stopsound")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Stops all currently playing sounds.")
							CreateConsoleMsg("******************************")
						Case "camerapick"
							CreateConsoleMsg("HELP - camerapick")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Prints the texture name and coordinates of")
							CreateConsoleMsg("the model the camera is pointing at.")
							CreateConsoleMsg("******************************")
						Case "fov"
							CreateConsoleMsg("HELP - fov")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Field of view (FOV) is the amount of game view")
							CreateConsoleMsg("that is on display during a game.")
							CreateConsoleMsg("******************************")
						Case "status"
							CreateConsoleMsg("HELP - status")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Prints player, camera, and room information.")
							CreateConsoleMsg("******************************")
						Case "weed","scp-420-j","420"
							CreateConsoleMsg("HELP - 420")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Generates dank memes.")
							CreateConsoleMsg("******************************")
						Case "playmusic"
							CreateConsoleMsg("HELP - playmusic")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Will play tracks in .ogg/.wav format")
							CreateConsoleMsg("from "+Chr(34)+"SFX\Music\Custom\"+Chr(34)+".")
							CreateConsoleMsg("******************************")
						Case "omni"
							CreateConsoleMsg("HELP - omni")
							CreateConsoleMsg("******************************")
							CreateConsoleMsg("Toggles getting guaranteed Key Card Omnis")
							CreateConsoleMsg("from SCP-914, unless a valid parameter")
							CreateConsoleMsg("is specified (on/off).")
							CreateConsoleMsg("A Key Card Omni can be obtained from SCP-914")
							CreateConsoleMsg("by refining a Level 5 Key Card on Fine")
							CreateConsoleMsg("or any Key Card on Very Fine.")
							CreateConsoleMsg("By default, the chance of success is random")
							CreateConsoleMsg("and relies on the amount of achievements")
							CreateConsoleMsg("unlocked in the current save.")
							CreateConsoleMsg("******************************")
							
						Default
							CreateConsoleMsg("There is no help available for that command.",255,150,0)
					End Select
				Case "asd"
				Case "status"
				Case "camerapick"
				Case "fov"
				Case "hidedistance"
				Case "ending"
				Case "noclipspeed"
				Case "injure"
				Case "infect"
				Case "heal"
				Case "teleport"
				Case "roomlist", "rooms"
				Case "spawnitem"
				Case "itemlist", "items"
				Case "wireframe"
				Case "173speed"
				Case "106speed"
				Case "173state"
				Case "106state"
				Case "reset096"
				Case "disable173"
				Case "enable173"
				Case "disable106"
				Case "enable106"
				Case "halloween"
				Case "sanic"
				Case "scp-420-j","420","weed"
				Case "godmode", "god"
				Case "revive","undead","resurrect"
				Case "noclip","fly"
				Case "showfps"
				Case "096state"
				Case "debughud"
				Case "stopsound", "stfu"
				Case "camerafog"
				Case "gamma"
				Case "spawn"
				;new Console Commands in SCP:CB 1.3 - ENDSHN
				Case "infinitestamina","infstam"
				Case "asd2"
				Case "toggle_warhead_lever"
				Case "unlockexits"
				Case "kill","suicide"
				Case "playmusic"
				Case "tp"
				Case "tele"
				Case "notarget"
				Case "spawnradio"
				Case "spawnnvg"
				Case "spawnpumpkin","pumpkin"
				Case "spawnnav"
				Case "teleport173"
				Case "seteventstate"
				Case "spawnparticles"
				Case "giveachievement"
				Case "427state"
				Case "teleport106"
				Case "setblinkeffect"
				Case "omni"
				Case "mav"
				Case "jorge"
			End Select

			End If
			
			ConsoleInput = ""
		End If
		
		Local TempY% = y + height - 25*MenuScale - ConsoleScroll
		Local count% = 0
		For cm.ConsoleMsg = Each ConsoleMsg
			count = count+1
			If count>1000 Then
				Delete cm
			Else
				If TempY >= y And TempY < y + height - 20*MenuScale Then
					If cm=ConsoleReissue Then
						Color cm\r/4,cm\g/4,cm\b/4
						Rect x+3*MenuScale,TempY-2*MenuScale,width-30*MenuScale,24*MenuScale,True
					EndIf
					Color cm\r,cm\g,cm\b
					If cm\isCommand Then
						Text(x + 20*MenuScale, TempY+5*MenuScale, "> "+cm\txt)
					Else
						Text(x + 20*MenuScale, TempY+5*MenuScale, cm\txt)
					EndIf
				EndIf
				TempY = TempY - 15*MenuScale
			EndIf
			
		Next
		
		Color 255,255,255
		
		If Fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
	End If
	
	SetFont Font1
	
End Function
;---------------------------------------------------------------------------------------------------