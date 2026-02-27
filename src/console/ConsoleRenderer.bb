; ===========================================================================
; ConsoleRenderer.bb
; ===========================================================================

Function Console_Render(layout.ConsoleLayout)

	SetFont ConsoleFont
	ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255
	Color 255,255,255

    Console_RenderBackground(layout)
	Console_RenderMessages(layout)
	Console_RenderScrollbar(layout)
	Console_RenderInputBox(layout)

	If Fullscreen Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()

	Color 255,255,255
	SetFont Font1

End Function

; ---------------------------------------------------------------------------

Function Console_RenderBackground(layout.ConsoleLayout)
    DrawFrame layout\X, layout\Y, layout\Width, layout\Height + CONSOLE_PADDING_PX * MenuScale
End Function

Function Console_RenderMessages(layout.ConsoleLayout)

	Local y# = layout\Y + layout\Height - 25 * MenuScale - Console\Scroll

	For cm.ConsoleMsg = Each ConsoleMsg
		If (y >= layout\y And y < layout\y + layout\Height - 20 * MenuScale) Then
			Color cm\r, cm\g, cm\b
			Text layout\X + 20*MenuScale, y + 5*MenuScale, cm\txt
		EndIf
		y = y - 15*MenuScale
	Next

End Function

Function Console_RenderScrollbar(layout.ConsoleLayout)

	;; ToDo:: use the layout versions of scrollbar x,y,width & height
	;Local scrollbarX = layout\X + layout\Width - 23 * MenuScale
    ;Local scrollbarY# = layout\Y + layout\Height - layout\ScrollbarHeight + (Console\Scroll * layout\ScrollbarHeight / layout\Height)

    isHighlighted% = MouseOn(layout\ScrollbarX, layout\ScrollbarY, layout\ScrollbarWidth, layout\ScrollbarHeight)
	If isHighlighted Then 
        Color 200,200,200
    Else 
        Color 120,120,120
    EndIf

	Rect layout\ScrollbarX, layout\ScrollbarY, layout\ScrollbarWidth, layout\ScrollbarHeight, True

End Function

Function Console_RenderInputBox(layout.ConsoleLayout)

	SelectedInputBox = 2
	Console\Input = InputBox(layout\X, layout\Y + layout\Height, layout\Width, layout\InputBoxHeight, Console\Input, 2, -1)

End Function