; ===========================================================================
; ConsoleRenderer.bb
; ===========================================================================

Function Console_Render(layout.ConsoleLayout)

    Console_RenderBackground(layout)
	Console_RenderMessages(layout)
	Console_RenderScrollbar(layout)

End Function

; ---------------------------------------------------------------------------

Function Console_RenderBackground(layout.ConsoleLayout)
    DrawFrame layout\X, layout\Y, layout\Width, layout\Height + CONSOLE_PADDING_PX * MenuScale
End Function

Function Console_RenderMessages(layout.ConsoleLayout)

	Local y# = layout\Y + Console\Scroll

	For cm.ConsoleMsg = Each ConsoleMsg
		Color cm\r, cm\g, cm\b
		Text layout\X + 10*MenuScale, y, cm\txt
		y = y + 20*MenuScale
	Next

End Function

Function Console_RenderScrollbar(layout.ConsoleLayout)

	;; ToDo:: use the layout versions of scrollbar x,y,width & height

	Local scrollbarX = layout\X + layout\Width - 23 * MenuScale
    Local scrollbarY# = layout\Y + layout\Height - layout\ScrollbarHeight + (Console\Scroll * layout\ScrollbarHeight / layout\Height)

    isHighlighted% = MouseOn(scrollbarX,scrollbarY,20*MenuScale,layout\ScrollbarHeight)
	If isHighlighted Then 
        Color 200,200,200
    Else 
        Color 120,120,120
    EndIf

	Rect scrollbarX, scrollbarY, 20*MenuScale, layout\ScrollbarHeight, True

End Function