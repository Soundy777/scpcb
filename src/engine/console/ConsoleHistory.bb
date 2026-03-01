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