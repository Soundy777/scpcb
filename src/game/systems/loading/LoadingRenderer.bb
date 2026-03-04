Function Loading_Render(percent%, shortloading=False)
	
	Local x%, y%
	
	If percent = 0 Then
		LoadingScreenStartTime = MilliSecs()
		
		temp = Rand(1,LoadingScreenCount)
		For ls.loadingscreens = Each LoadingScreens
			If ls\id = temp Then
				If ls\img=0 Then
					ls\img = LoadImage_Strict("Loadingscreens\"+ls\imgpath)
					ScaleImage(ls\img, Gfx\MenuScale, Gfx\MenuScale)
					MaskImage(ls\img, 0, 0, 0)
				EndIf
				CurrentLoadingScreen = ls 
				Exit
			EndIf
		Next
	EndIf	
	
	firstloop = True
	Repeat 
		
		ClsColor 0,0,0
		Cls
		
		If percent > 20 Then
			UpdateMusic()
		EndIf

		Local LoadingScreenTextHandle = 0
		If CurrentLoadingScreen\totalTxtDelay <> 0 Then
			Local elapsedTime = (MilliSecs() - LoadingScreenStartTime) Mod CurrentLoadingScreen\totalTxtDelay
			While elapsedTime >= CurrentLoadingScreen\txtDelay[LoadingScreenTextHandle]
				LoadingScreenTextHandle = LoadingScreenTextHandle + 1
			Wend
		EndIf
		
		If (Not CurrentLoadingScreen\disablebackground) Then
			DrawImage LoadingBackgroundImage, Config\Graphics\ScreenWidth/2 - ImageWidth(LoadingBackgroundImage)/2, Config\Graphics\ScreenHeight/2 - ImageHeight(LoadingBackgroundImage)/2
		EndIf	
		
		If CurrentLoadingScreen\alignx = 0 Then
			x = Config\Graphics\ScreenWidth/2 - ImageWidth(CurrentLoadingScreen\img)/2 
		ElseIf  CurrentLoadingScreen\alignx = 1
			x = Config\Graphics\ScreenWidth - ImageWidth(CurrentLoadingScreen\img)
		Else
			x = 0
		EndIf
		
		If CurrentLoadingScreen\aligny = 0 Then
			y = Config\Graphics\ScreenHeight/2 - ImageHeight(CurrentLoadingScreen\img)/2 
		ElseIf  CurrentLoadingScreen\aligny = 1
			y = Config\Graphics\ScreenHeight - ImageHeight(CurrentLoadingScreen\img)
		Else
			y = 0
		EndIf	
		
		DrawImage CurrentLoadingScreen\img, x, y
		
		DrawBar(BlinkMeterIMG, Config\Graphics\ScreenWidth / 2, Config\Graphics\ScreenHeight / 2 - 70 * Gfx\MenuScale, 300 * Viewport_GetScale(), percent / 100.0, True)
		
		If CurrentLoadingScreen\title = "CWM" Then
			
			If Not shortloading Then 
				If firstloop Then 
					If percent = 0 Then
						PlaySound_Strict LoadTempSound("SFX\SCP\990\cwm1.cwm")
					ElseIf percent = 100
						PlaySound_Strict LoadTempSound("SFX\SCP\990\cwm2.cwm")
					EndIf
				EndIf
			EndIf
			
			SetFont GameFonts\UI_Large
			strtemp$ = ""
			temp = Rand(2,9)
			For i = 0 To temp
				strtemp$ = STRTEMP + RandomDefaultWidthChar(48,122,"?")
			Next
			Text(Config\Graphics\ScreenWidth / 2, Config\Graphics\ScreenHeight / 2 + 80*Gfx\MenuScale, strtemp, True, True)
			
			If percent = 0 Then 
				If Rand(5)=1 Then
					Select Rand(2)
						Case 1
							CurrentLoadingScreen\txt[0] = Format(I_Loc\Menu_LoadingCwm[1], CurrentDate())
						Case 2
							CurrentLoadingScreen\txt[0] = CurrentTime()
					End Select
				Else
					Select Rand(13)
						Case 1
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[2]
						Case 2
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[3]
						Case 3
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[4]
						Case 4
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[5]
						Case 5
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[6]
						Case 6 
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[7]
						Case 7
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[8]
						Case 8, 9
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[9]
						Case 10
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[10]
						Case 11
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[11]
						Case 12
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[12]
						Case 13
							CurrentLoadingScreen\txt[0] = I_Loc\Menu_LoadingCwm[13]
					End Select
				EndIf
			EndIf
			
			strtemp$ = CurrentLoadingScreen\txt[0]
			temp = Int(Len(CurrentLoadingScreen\txt[0])-Rand(5))
			For i = 0 To Rand(10,15);temp
				strtemp$ = Replace(CurrentLoadingScreen\txt[0],Mid(CurrentLoadingScreen\txt[0],Rand(1,Len(strtemp)-1),1),RandomDefaultWidthChar(130,250,"?"))
			Next		
			SetFont GameFonts\UI_Small
			RowText(strtemp, Config\Graphics\ScreenWidth / 2-200*Gfx\MenuScale, Config\Graphics\ScreenHeight / 2 +120*Gfx\MenuScale,400*Gfx\MenuScale,300*Gfx\MenuScale,True)		
		Else
			
			Color 0,0,0
			SetFont GameFonts\UI_Large
			Text(Config\Graphics\ScreenWidth / 2 + Max(1, Gfx\MenuScale), Config\Graphics\ScreenHeight / 2 + 80*Gfx\MenuScale+Max(1, Gfx\MenuScale), CurrentLoadingScreen\title, True, True)
			SetFont GameFonts\UI_Small
			RowText(CurrentLoadingScreen\txt[LoadingScreenTextHandle], Config\Graphics\ScreenWidth / 2-200*Gfx\MenuScale+Max(1, Gfx\MenuScale), Config\Graphics\ScreenHeight / 2 +120*Gfx\MenuScale+Max(1, Gfx\MenuScale),400*Gfx\MenuScale,300*Gfx\MenuScale,True)
			
			Color 255,255,255
			SetFont GameFonts\UI_Large
			Text(Config\Graphics\ScreenWidth / 2, Config\Graphics\ScreenHeight / 2 +80*Gfx\MenuScale, CurrentLoadingScreen\title, True, True)
			SetFont GameFonts\UI_Small
			RowText(CurrentLoadingScreen\txt[LoadingScreenTextHandle], Config\Graphics\ScreenWidth / 2-200*Gfx\MenuScale, Config\Graphics\ScreenHeight / 2 +120*Gfx\MenuScale,400*Gfx\MenuScale,300*Gfx\MenuScale,True)
			
		EndIf

		If SpeedRunMode And (Not TimerStopped) And PlayTime > 0 Then
			DrawTimer()
		EndIf
		
		Color 0,0,0
		Text(Config\Graphics\ScreenWidth / 2 + Max(1, enuScale), Config\Graphics\ScreenHeight / 2 - 100 * Gfx\MenuScale + Max(1, Gfx\MenuScale), Format(I_Loc\Menu_Loading, percent), True, True)
		Color 255,255,255
		Text(Config\Graphics\ScreenWidth / 2, Config\Graphics\ScreenHeight / 2 - 100 * Gfx\MenuScale, Format(I_Loc\Menu_Loading, percent), True, True)
		
		If percent = 100 Then 
			If firstloop And CurrentLoadingScreen\title <> "CWM" Then PlaySound_Strict LoadTempSound(("SFX\Horror\Horror8.ogg"))
			Text(Config\Graphics\ScreenWidth / 2, Config\Graphics\ScreenHeight - 50 * Gfx\MenuScale, I_Loc\Menu_Pressany, True, True)
		Else
			FlushKeys()
			FlushMouse()
		EndIf
		
		FrameCompositor_Apply(Config\Graphics\BorderlessWindowed, Config\Graphics\ScreenWidth, Config\Graphics\ScreenHeight, Config\Graphics\ScreenGamma)
		
		Flip False
		
		firstloop = False
		If percent <> 100 Then Exit
		
	Until (GetKey()<>0 Or MouseHit(1))
	Cls
End Function

Function RandomDefaultWidthChar$(min%, max%, def$)
	Local c$ = Chr(Rand(min%, max%))
	If StringWidth(c) <> StringWidth("L") Then Return def Else Return c
End Function