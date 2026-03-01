Function GetOptionString$(section$, key$)

	Local opt$ = GetINIString(Paths\OptionsFile, section, key)
	If opt = "" Then Return GetINIString(Paths\DefaultsFile, section, key)
	Return opt
    
End Function

Function GetOptionInt%(section$, key$)
	Return ParseINIInt(GetOptionString(section, key))
End Function

Function GetOptionFloat#(section$, key$)
	Return Float(GetOptionString(section, key))
End Function