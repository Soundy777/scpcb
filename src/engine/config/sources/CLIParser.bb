Function HasCLIFlag%(name$)

    name = "-" + name
    Local cmd$ = CommandLine()
    Local pos% = Instr(cmd, " " + name + " ")
    If pos <> 0 Then Return True
    pos = Instr(cmd, name + " ")
    If pos = 1 Then Return True
    pos = Instr(cmd, " " + name)
    If pos = Len(cmd) - Len(name) Then Return True
    Return cmd = name

End Function

Function GetCLIInt%(name$, def%=0)

	Local txt$ = GetCLIString(name)
	If txt <> "" Then Return Int(txt)
	return def

End Function

Function GetCLIString$(name$, def$="")

	name = "-" + name + " "
	Local cmd$ = CommandLine()
	Local begin% = Instr(cmd, " " + name)
	If begin = 0 Then
		begin = Instr(cmd, name)
		If begin <> 1 Return def
	Else
		begin = begin + 1
	EndIf
	begin = begin + Len(name)
	Local end% = Instr(cmd, " ", begin)
	If end = 0 Then end = Len(cmd) + 1
	Return Trim(Mid(cmd, begin, end - begin))

End Function