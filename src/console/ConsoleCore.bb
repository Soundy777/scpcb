; ===========================================================================
; ConsoleCore.bb
; ===========================================================================
;; ToDo:: Implement Alias functionality for commands (i.e. "god" for "godmode", "tp" for "teleport", etc.)
; ===========================================================================
Include "src/console/ConsoleHistory.bb"
Include "src/console/ConsoleRenderer.bb"
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

Type ConsoleState
	Field IsOpen%
	Field Input$
	Field Scroll#
	Field ScrollDragging%
	Field ScrollGrabOffset#
	Field HistoryCursor.ConsoleHistoryEntry
End Type

; ---------------------------------------------------------------------------

Global Console.ConsoleState = New ConsoleState

; ---------------------------------------------------------------------------

; ToDo:: Remove these when we're finished refactoring
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
		Console_DispatchCommand(commandID, Lower(args$))
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

Type ConsoleLayout
	Field X%
	Field Y%
	Field Width%
	Field Height%
	Field ContentHeight%
	Field ScrollbarHeight%
End Type

Const CONSOLE_HEIGHT_PX = 300
Const CONSOLE_PADDING_PX = 30
Const CONSOLE_LINE_HEIGHT_PX = 20
Const CONSOLE_MIN_SCROLLBAR_HEIGHT_PX = 10
const CONSOLE_MOUSESCROLL_SPEED = 15

Function Console_CalculateLayout.ConsoleLayout()

	Local l.ConsoleLayout = New ConsoleLayout
	
	l\X = 0
	l\Y = GraphicHeight - CONSOLE_HEIGHT_PX*MenuScale
	l\Width = GraphicWidth
	l\Height = (CONSOLE_HEIGHT_PX - CONSOLE_PADDING_PX)*MenuScale

	; calculate content height
	Local count%
	For cm.ConsoleMsg = Each ConsoleMsg
		count = count + 1
	Next
	
	l\ContentHeight = count * (CONSOLE_LINE_HEIGHT_PX*MenuScale)

	If count > 0 Then
		l\ScrollbarHeight = Max(l\Height * (Float(l\Height)/l\ContentHeight), CONSOLE_MIN_SCROLLBAR_HEIGHT_PX)
	Else
		l\ScrollbarHeight = l\Height
	EndIf

	Return l
End Function

Function Console_TrimMessages(max%)

	Local count%
	For cm.ConsoleMsg = Each ConsoleMsg
		count = count + 1
	Next

	While count > max
		For cm.ConsoleMsg = Each ConsoleMsg
			Delete cm
			Exit
		Next
		count = count - 1
	Wend

End Function

; ---------------------------------------------------------------------------

Function Console_UpdateScroll(layout.ConsoleLayout)

	Console_HandleScrollbarDrag(layout)
	Console_HandleMouseWheel()
	Console_ClampScroll(layout)

End Function

Function Console_HandleMouseWheel()
	Local mouseZSpeed = MouseZSpeed()

	If mouseZSpeed <> 0 Then
		Console\Scroll = Console\Scroll - CONSOLE_MOUSESCROLL_SPEED * MenuScale * mouseZSpeed
	EndIf
End Function

Function Console_HandleScrollbarDrag(layout.ConsoleLayout)

	;Local scrollbarX = layout\Width - 20*MenuScale
	;Local scrollbarY = layout\Y

	Local scrollbarX = layout\X + layout\Width - 23 * MenuScale
    Local scrollbarY# = layout\Y + layout\Height - layout\ScrollbarHeight + (Console\Scroll * layout\ScrollbarHeight / layout\Height)

	Local scrollbarHeight = layout\ScrollbarHeight

	If MouseHit1 Then
		isHighlighted% = MouseOn(scrollbarX,scrollbarY,20*MenuScale,layout\ScrollbarHeight)
		If isHighlighted Then
			Console\ScrollDragging = True
			Console\ScrollGrabOffset = ScaledMouseY()
		EndIf
	EndIf

	If Not MouseDown(1) Then
		Console\ScrollDragging = False
	EndIf

	If Console\ScrollDragging Then
		Local delta# = ScaledMouseY() - Console\ScrollGrabOffset
		Console\Scroll = Console\Scroll + ((delta) * layout\Height / layout\ScrollbarHeight)
		Console\ScrollGrabOffset = ScaledMouseY()
	EndIf

End Function

Function Console_ClampScroll(layout.ConsoleLayout)

	If Console\Scroll > 0 Then Console\Scroll = 0

	Local minScroll# = -layout\ContentHeight + layout\Height
	If Console\Scroll < minScroll Then Console\Scroll = minScroll

End Function

; ---------------------------------------------------------------------------

Function UpdateConsole()
	Local e.Events ; This is old code - remove it once complete
	
	If Not Options\CanOpenConsole Then Return
	;If Not Console\IsOpen Then Return
	If Not Console_IsOpen Then Return ; ToDo:: remove this

	Local layout.ConsoleLayout = Console_CalculateLayout()

	Console_TrimMessages(1000)

	; Console_UpdateInput()
	; Console_UpdateScroll(layout)
	; Console_UpdateHistory()
	; Console_UpdateSubmission()

	;Console_Render(layout)

	; Refactor Line --------------------------------
	
	Local cm.ConsoleMsg
	
	SetFont ConsoleFont
	
	ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255
	
	Local x% = 0, y% = GraphicHeight-300*MenuScale, width% = GraphicWidth, height% = 300*MenuScale-30*MenuScale
	Local StrTemp$, temp%,  i%
	Local ev.Events, r.Rooms, it.Items
	
	;DrawFrame x,y,width,height+30*MenuScale
	Console_RenderBackground(layout)
	
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
	;Rect x+width-26*MenuScale,y,26*MenuScale,height,True
	;Console_HandleMouseWheel()
	;Console_ClampScroll(layout)
	Console_UpdateScroll(layout)
	Console_RenderScrollbar(layout)
	
	
	Color 120,120,120
	inBox% = MouseOn(x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight)
	If inBox Then Color 200,200,200
	If ConsoleScrollDragging Then Color 255,255,255
	;Rect x+width-23*MenuScale,y+height-scrollbarHeight+(ConsoleScroll*scrollbarHeight/height),20*MenuScale,scrollbarHeight,True
	
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
	
	;ConsoleScroll = ConsoleScroll - 15*MenuScale * MouseZSpeed()
	
	
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
	
	SetFont Font1
	
End Function
;---------------------------------------------------------------------------------------------------