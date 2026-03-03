Type LoadingScreens
	Field imgpath$
	Field img%
	Field ID%
	Field title$
	Field alignx%, aligny%
	Field disablebackground%
	Field txt$[5], txtamount%
	; Inclusive prefix sum
	Field txtDelay%[5], totalTxtDelay%
End Type

Const LOADING_SCREENS_DATA_PATH$ = "Loadingscreens\loadingscreens.ini"

Global CurrentLoadingScreen.LoadingScreens
Global LoadingScreenCount% = 0
Global LoadingBackgroundImage%
Global LoadingScreenTextHandle%
Global LoadingScreenStartTime%
Global LoadingScreenTimePerCharacter%

Function Loading_Init()

    LoadingBackgroundImage = LoadImage_Strict("Loadingscreens\loadingback.jpg")

    Delete Each LoadingScreens
	LoadingScreenTimePerCharacter% = GetOptionInt("general", "loading screen cycle per char ms")
	Local hasOverride%
	For m.ActiveMods = Each ActiveMods
		Local modPath$ = m\Path + LOADING_SCREENS_DATA_PATH
		If FileType(modPath) = 1 Then
			LoadLoadingScreens(modPath)
			If FileType(modPath + ".OVERRIDE") = 1 Then
				hasOverride = True
				Exit
			EndIf
		EndIf
	Next
	If Not hasOverride Then LoadLoadingScreens(LOADING_SCREENS_DATA_PATH)

End Function

Function LoadLoadingScreens(file$)
	Local TemporaryString$, i%
	Local ls.LoadingScreens
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			ls.LoadingScreens = New LoadingScreens
			LoadingScreenCount=LoadingScreenCount+1
			ls\ID = LoadingScreenCount
			
			ls\title = TemporaryString
			ls\imgpath = GetINIString(file, TemporaryString, "image path")
			
			ls\totalTxtDelay = 0
			For i = 0 To 4
				ls\txt[i] = GetINIString(file, TemporaryString, "text"+(i+1))
				ls\totalTxtDelay = ls\totalTxtDelay + Len(ls\txt[i]) * LoadingScreenTimePerCharacter
				ls\txtDelay[i] = ls\totalTxtDelay
				If ls\txt[i]<> "" Then ls\txtamount=ls\txtamount+1
			Next
			
			ls\disablebackground = GetINIInt(file, TemporaryString, "disablebackground")
			
			Select Lower(GetINIString(file, TemporaryString, "align x"))
				Case "left"
					ls\alignx = -1
				Case "middle", "center"
					ls\alignx = 0
				Case "right" 
					ls\alignx = 1
			End Select 
			
			Select Lower(GetINIString(file, TemporaryString, "align y"))
				Case "top", "up"
					ls\aligny = -1
				Case "middle", "center"
					ls\aligny = 0
				Case "bottom", "down"
					ls\aligny = 1
			End Select 			
			
		EndIf
	Wend
	
	CloseFile f
End Function