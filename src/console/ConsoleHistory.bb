; ===========================================================================
; ConsoleHistory.bb
; ===========================================================================

Type ConsoleHistoryEntry
	Field Text$
	Field PrevEntry.ConsoleHistoryEntry
	Field NextEntry.ConsoleHistoryEntry
End Type

; ---------------------------------------------------------------------------

Global ConsoleHistoryTail.ConsoleHistoryEntry

; ---------------------------------------------------------------------------

Function Console_AddHistory(text$)
	Local entry.ConsoleHistoryEntry = New ConsoleHistoryEntry
	entry\Text = text$

	If ConsoleHistoryTail <> Null Then
		ConsoleHistoryTail\NextEntry = entry
		entry\PrevEntry = ConsoleHistoryTail
	EndIf

	ConsoleHistoryTail = entry
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