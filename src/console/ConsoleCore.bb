; ===========================================================================
; ConsoleCore.bb
; ===========================================================================
;; ToDo:: Implement Alias functionality for commands (i.e. "god" for "godmode", "tp" for "teleport", etc.)
;; ToDo:: MenuScale:: Find a better way to implement menu scaling in a way where we can do it all at once instead of adhoc throughout
;; ToDo:: MenuScale:: Additionally, test that the console actually properly applies menu scale to all of its UI Elements
;; ToDo:: MenuScale:: consider applying menu scaling at the UI component level instead of ever at the higher order "Screen" level
;; ToDo:: The InputBox logic inside of the renderer handles layout, input, rendering and data - refactor that out but when we overhaul the UI Widgets
;; ToDo:: build a reusable TextArea UI Component which manages the scroll bar & scrolling logic itself - dramatically reducing the consoles code
;; ToDo:: Consider overhauling some of the naming conventions used here for various functions and variables (especially Console_UpdateSubmission)
;; ToDo:: move ConsoleR/G/B into the ConsoleState Type (this is used to globally override the color for one tick)
;; ToDo:: refactor ConsoleOpen such that its usage throughout the codebase is replaced with ConsoleOpen()/Close()/Toggle() 
;; ToDo:: addtionally, remove checks for ConsoleOpen throughout the codebase with IsConsoleOpen() calls
;; ToDo:: consider splitting out the update logic from this to make this purely high level orcestration (ConsoleCore/ConsoleLogic/ConsoleRenderer)
;; ToDo:: During the second iteration pass, overhaul commands to use service layers instead of directly manipulating state
; ===========================================================================
Include "src/console/ConsoleHistory.bb"
Include "src/console/ConsoleRenderer.bb"
Include "src/console/ConsoleCommands.bb"
; ===========================================================================

Type ConsoleMsg
	Field txt$
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

Global ConsoleOpen%
Global ConsoleEnabled%

Global Console.ConsoleState = New ConsoleState

Global ConsoleR% = 255, ConsoleG% = 255, ConsoleB% = 255

; ---------------------------------------------------------------------------

Function InitConsole()
	ConsoleOpen = False
	ConsoleEnabled = Options\ConsoleEnabled

	InitConsoleCommands()

	CreateConsoleMsg("Console enabled. Type 'help' for a list of commands.", 0, 255, 255)
End Function

; ---------------------------------------------------------------------------

Function OpenConsole()
	Console\IsOpen = True
End Function

Function CloseConsole()
	Console\IsOpen = False
End Function

Function ToggleConsole()
	Console\IsOpen = Not Console\IsOpen
End Function

Function IsConsoleOpen%()
	return Console\IsOpen
End Function

; ---------------------------------------------------------------------------

Type ConsoleLayout
	Field X%
	Field Y%
	Field Width%
	Field Height%
	Field ContentHeight%
	Field ScrollbarX%
	Field ScrollbarY%
	Field ScrollbarHeight%
	Field ScrollbarWidth%
	Field InputBoxHeight%
End Type

Const CONSOLE_HEIGHT_PX = 300
Const CONSOLE_PADDING_PX = 30
Const CONSOLE_LINE_HEIGHT_PX = 20
const CONSOLE_LINE_LEFTPADDING_PX = 20
const CONSOLE_MOUSESCROLL_SPEED = 15

Const CONSOLE_SCROLLBAR_MINHEIGHT_PX = 10
Const CONSOLE_SCROLLBAR_WIDTH_PX = 20
Const CONSOLE_SCROLLBAR_PADDINGRIGHT_PX = 6
Const CONSOLE_SCROLLBAR_PADDINGTOP_PX = 8
Const CONSOLE_SCROLLBAR_PADDINGBOTTOM_PX = 6

Const CONSOLE_INPUTBOX_HEIGHT = 30

Function Console_CalculateLayout.ConsoleLayout()

	Local l.ConsoleLayout = New ConsoleLayout
	
	; Calculate console dimensions & position
	l\X = 0
	l\Y = GraphicHeight - CONSOLE_HEIGHT_PX*MenuScale
	l\Width = GraphicWidth
	l\Height = (CONSOLE_HEIGHT_PX - CONSOLE_PADDING_PX)*MenuScale

	; Compute total number of console messages
	Local numConsoleMessages% = 0
	For cm.ConsoleMsg = Each ConsoleMsg
		numConsoleMessages = numConsoleMessages + 1
	Next
	
	; Calculate content height
	l\ContentHeight = numConsoleMessages * (CONSOLE_LINE_HEIGHT_PX*MenuScale)

	; Zero out scroll if no messages are present
	If numConsoleMessages = 0 Then Console\Scroll = 0

	; Calculate scroll bar dimensions & position
	l\ScrollbarWidth = CONSOLE_SCROLLBAR_WIDTH_PX * MenuScale

	If numConsoleMessages > 0 And l\ContentHeight > l\Height Then
		l\ScrollbarHeight = Max(l\Height * (Float(l\Height)/l\ContentHeight), CONSOLE_SCROLLBAR_MINHEIGHT_PX)
	Else
		l\ScrollbarHeight = l\Height - (CONSOLE_SCROLLBAR_PADDINGTOP_PX + CONSOLE_SCROLLBAR_PADDINGBOTTOM_PX)
	EndIf

	l\ScrollbarX = Int(l\X + l\Width - CONSOLE_SCROLLBAR_WIDTH_PX - CONSOLE_SCROLLBAR_PADDINGRIGHT_PX)
    
	; Calculate the y position of the scrollbar account for both scrolling and vertical padding
	Local minScroll# = l\Height - l\ContentHeight
	If minScroll > 0 Then minScroll = 0

	Local scrollRange# = -minScroll
	Local scrollRatio# = 0
	If scrollRange > 0 Then
		scrollRatio = 1 + (Console\Scroll / scrollRange)
	EndIf
	If scrollRatio < 0 Then scrollRatio = 0
	If scrollRatio > 1 Then scrollRatio = 1

	Local trackTop# = l\Y + CONSOLE_SCROLLBAR_PADDINGTOP_PX
	Local trackBottom# = l\Y + l\Height - CONSOLE_SCROLLBAR_PADDINGBOTTOM_PX - l\ScrollbarHeight
	Local trackRange# = trackBottom - trackTop

	l\ScrollbarY = Int(trackTop + scrollRatio * trackRange)
	
	; Calculate the inputbox height
	l\InputBoxHeight = Int(CONSOLE_INPUTBOX_HEIGHT * MenuScale)

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

Function Console_HandleScrollbarDrag(layout.ConsoleLayout)

	If MouseHit1 Then
		isHighlighted% = MouseOn(layout\ScrollbarX,layout\ScrollbarY,layout\ScrollbarWidth,layout\ScrollbarHeight)
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

Function Console_HandleMouseWheel()
	Local mouseZSpeed = MouseZSpeed()

	If mouseZSpeed <> 0 Then
		Console\Scroll = Console\Scroll - CONSOLE_MOUSESCROLL_SPEED * MenuScale * mouseZSpeed
	EndIf
End Function

Function Console_ClampScroll(layout.ConsoleLayout)

	If layout\ContentHeight <= layout\Height Then
        Console\Scroll = 0
        Return
    EndIf

	If Console\Scroll > 0 Then Console\Scroll = 0

	Local minScroll# = layout\Height - layout\ContentHeight
    If Console\Scroll < minScroll Then
        Console\Scroll = minScroll
    EndIf

End Function

; ---------------------------------------------------------------------------

Function Console_UpdateHistory()

	If KeyHit(200) Then ; up
		If Console\HistoryCursor = Null Then
			Console\HistoryCursor = ConsoleHistoryTail
		ElseIf Console\HistoryCursor\PrevEntry <> Null Then
			Console\HistoryCursor = Console\HistoryCursor\PrevEntry
		EndIf

		If Console\HistoryCursor <> Null Then
			Console\Input = Console\HistoryCursor\Text
		EndIf
	EndIf

	If KeyHit(208) Then ; down
		If Console\HistoryCursor <> Null And Console\HistoryCursor\NextEntry <> Null Then
			Console\HistoryCursor = Console\HistoryCursor\NextEntry
			Console\Input = Console\HistoryCursor\Text
		Else
			Console\HistoryCursor = Null
			Console\Input = ""
		EndIf
	EndIf

End Function

; ---------------------------------------------------------------------------

Function Console_UpdateSubmission()

	If Not KeyHit(28) Then Return
	If Console\Input = "" Then Return

	Console_Submit(Console\Input)
	Console\Input = ""
	Console\HistoryCursor = Null

End Function

Function Console_Submit(text$)

	CreateConsoleMsg("> " + text, 255,255,0)
	Console_AddHistory(text)

	ExecuteConsoleCommand(text)

	Console\Scroll = 0

End Function

Function CreateConsoleMsg(txt$,r%=-1,g%=-1,b%=-1)
	Local c.ConsoleMsg = New ConsoleMsg
	Insert c Before First ConsoleMsg
	
	c\txt = txt
	
	c\r = r
	c\g = g
	c\b = b
	
	If (c\r<0) Then c\r = ConsoleR
	If (c\g<0) Then c\g = ConsoleG
	If (c\b<0) Then c\b = ConsoleB
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
    	CreateConsoleMsg("Unknown command: " + cmd$, 255, 0 , 0)
		CreateConsoleMsg("Type 'help' for a list of available commands.")
	Else
		Console_DispatchCommand(commandID, Lower(args$))
	End If

End Function

; ---------------------------------------------------------------------------

Function UpdateConsole()
	
	If Not Options\ConsoleEnabled Then Return

	;; ToDo:: use Console\IsOpen when we've integrated the functions to control opening & closing of the console
	;If Not Console\IsOpen Then Return
	If Not ConsoleOpen Then Return

	Console_TrimMessages(1000)

	Local layout.ConsoleLayout = Console_CalculateLayout()

	Console_UpdateScroll(layout)
	Console_UpdateHistory()
	Console_UpdateSubmission()

	Console_Render(layout)
	
End Function
;---------------------------------------------------------------------------------------------------