; ===========================================================================
; ConsoleRenderer.bb
; ===========================================================================

Function Console_Render(layout.ConsoleLayout)

	SetFont GameFonts\Console
	ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255

    Console_RenderBackground(layout)
	Console_RenderMessages(layout)
	Console_Render_Scrollbar_Background(layout)
	Console_RenderScrollbar(layout)
	Console_RenderInputBox(layout)

	If Config\Graphics\Fullscreen Then DrawImage Resource_GetTexture(TEX_CURSOR), ScaledMouseX(),ScaledMouseY()

	Color 255,255,255
	SetFont Font1

End Function

; ---------------------------------------------------------------------------

Function Console_RenderBackground(layout.ConsoleLayout)

    DrawFrame layout\X, layout\Y, layout\Width, layout\Height + CONSOLE_PADDING_PX * Gfx\MenuScale

End Function

Function Console_RenderMessages(layout.ConsoleLayout)

	Local consoleTop# = layout\Y + 5 * Gfx\MenuScale
	Local consoleBottom# = layout\Y + layout\Height - 20 * Gfx\MenuScale
	Local y# = consoleBottom# - Console\Scroll

	For cm.ConsoleMsg = Each ConsoleMsg
		If (y > consoleTop# And y <= consoleBottom#) Then
			Color cm\r, cm\g, cm\b
			Text layout\X + CONSOLE_LINE_LEFTPADDING_PX * Gfx\MenuScale, y, cm\txt
		EndIf
		y = y - CONSOLE_LINE_HEIGHT_PX * Gfx\MenuScale
	Next

End Function

Function Console_Render_Scrollbar_Background(layout.ConsoleLayout)

	Color 60,60,60
	Rect layout\ScrollbarX - 3, layout\Y + 5, layout\ScrollbarWidth + 6, layout\Height - 5

End Function

Function Console_RenderScrollbar(layout.ConsoleLayout)

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