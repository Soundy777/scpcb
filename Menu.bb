Global MenuBack% = LoadImage_Strict("GFX\menu\back.jpg")
Global MenuText% = LoadImage_Strict("GFX\menu\scptext.jpg")
Global Menu173% = LoadImage_Strict("GFX\menu\173back.jpg")
Global MenuWhite%, MenuBlack%
MenuWhite = LoadImage_Strict("GFX\menu\menuwhite.jpg")
MenuBlack = LoadImage_Strict("GFX\menu\menublack.jpg")
MaskImage MenuBlack, 255,255,0

ResizeImage(MenuBack, ImageWidth(MenuBack) * Gfx\MenuScale, ImageHeight(MenuBack) * Gfx\MenuScale)
ResizeImage(MenuText, ImageWidth(MenuText) * Gfx\MenuScale, ImageHeight(MenuText) * Gfx\MenuScale)
ResizeImage(Menu173, ImageWidth(Menu173) * Gfx\MenuScale, ImageHeight(Menu173) * Gfx\MenuScale)

For i = 0 To 3
	ArrowIMG(i) = LoadImage_Strict("GFX\menu\arrow.png")
	ScaleImage(ArrowIMG(i), Viewport_GetScale(), Viewport_GetScale())
	RotateImage(ArrowIMG(i), 90 * i)
	HandleImage(ArrowIMG(i), 0, 0)
Next

Global RandomSeed$, RandomSeedNumeric%, HasNumericSeed%
Function GetRandomSeed%()
	If HasNumericSeed Then Return RandomSeedNumeric Else Return GenerateSeedNumber(RandomSeed)
End Function

Dim MenuBlinkTimer%(2), MenuBlinkDuration%(2)
MenuBlinkTimer%(0) = 1
MenuBlinkTimer%(1) = 1

Global MenuStr$, MenuStrX%, MenuStrY%

Global MainMenuTab%

Global EnableSFXRelease_Prev% = Config\Audio\EnableSFXRelease%

Global IntroEnabled% = GetOptionInt("general", "intro enabled")

Global SelectedInputBox%

Global SavePath$ = "Saves\"
Global SaveMSG$

;nykyisen tallennuksen nimi ja samalla missä kansiossa tallennustiedosto sijaitsee saves-kansiossa
Global PrevSave$, CurrSave$

Global SaveGameAmount%
Dim SaveGames$(SaveGameAmount+1) 
Dim SaveGameTime$(SaveGameAmount + 1)
Dim SaveGameDate$(SaveGameAmount + 1)
Dim SaveGameVersion$(SaveGameAmount + 1)
Dim SaveGamePlayTime$(SaveGameAmount + 1)

Global SavedMapsAmount% = 0
Dim SavedMaps$(SavedMapsAmount+1)
Dim SavedMapsPath$(SavedMapsAmount+1)
Dim SavedMapsAuthor$(SavedMapsAmount+1)

Global SelectedMap% = -1

LoadSaveGames()

Global CurrLoadGamePage%
Global EntriesPerPage%
Global PagingFrameHeight%

Function CalculatePagingVariables()
	Local y = 286+70+10
	Local availableHeight# = Config\Graphics\ScreenHeight - ((y + 55 + 30) * Gfx\MenuScale)
	EntriesPerPage% = Min(availableHeight / (100 * Gfx\MenuScale), 6)
	; height = 510 * Gfx\MenuScale
	PagingFrameHeight = EntriesPerPage * 80 * Gfx\MenuScale + 30 * Gfx\MenuScale
End Function
CalculatePagingVariables()

; 0 is idle; 1 is upload confirmation; 2 is update confirmation 
Global ModUIState%
Global ModChangelog$
Global ShouldKeepModDescription% = True
Global ModsDirty% = False
Global SelectedMod.Mods
Global NewModBlink% = LoadImage_Strict("GFX\newmod.png")
ResizeImage(NewModBlink, 24 * Gfx\MenuScale, 24 * Gfx\MenuScale)

Function EllipsisLeft$(txt$, maxLen%)
	If Len(txt) > maxLen Then Return Left(txt, maxLen-3) + "…"
	Return txt
End Function

Function UpdateMainMenu()
	Local x%, y%, width%, height%, temp%
	
	Color 0,0,0
	Rect 0,0,Config\Graphics\ScreenWidth,Config\Graphics\ScreenHeight,True
	
	ShowPointer()
	
	DrawImage(MenuBack, 0, 0)
	
	If (MilliSecs() Mod MenuBlinkTimer(0)) >= Rand(MenuBlinkDuration(0)) Then
		DrawImage(Menu173, Config\Graphics\ScreenWidth - ImageWidth(Menu173), Config\Graphics\ScreenHeight - ImageHeight(Menu173))
	EndIf
	
	If Rand(300) = 1 Then
		MenuBlinkTimer(0) = Rand(4000, 8000)
		MenuBlinkDuration(0) = Rand(200, 500)
	End If
	
	SetFont Font1
	
	MenuBlinkTimer(1)=MenuBlinkTimer(1)-DeltaTime
	If MenuBlinkTimer(1) < MenuBlinkDuration(1) Then
		Color(50, 50, 50)
		Text(MenuStrX + Rand(-5, 5), MenuStrY + Rand(-5, 5), MenuStr, True)
		If MenuBlinkTimer(1) < 0 Then
			MenuBlinkTimer(1) = Rand(700, 800)
			MenuBlinkDuration(1) = Rand(10, 35)
			MenuStrX = Rand(700, 1000) * Gfx\MenuScale
			MenuStrY = Rand(100, 600) * Gfx\MenuScale
			
			Select Rand(0, 22)
				Case 0, 2, 3
					MenuStr = I_Loc\Menu_Random[1]
				Case 4, 5
					MenuStr = I_Loc\Menu_Random[2]
				Case 6, 7, 8
					MenuStr = I_Loc\Menu_Random[3]
				Case 9, 10, 11
					MenuStr = I_Loc\Menu_Random[4]
				Case 12, 19
					MenuStr = I_Loc\Menu_Random[5]
				Case 13
					MenuStr = I_Loc\Menu_Random[6]
				Case 14
					MenuStr = I_Loc\Menu_Random[7]
				Case 15
					MenuStr = I_Loc\Menu_Random[8]
				Case 16
					MenuStr = I_Loc\Menu_Random[9]
				Case 17
					MenuStr = I_Loc\Menu_Random[10]
				Case 18
					MenuStr = I_Loc\Menu_Random[11]
				Case 20
					MenuStr = I_Loc\Menu_Random[12]
				Case 21
					MenuStr = I_Loc\Menu_Random[13]
				Case 22
					MenuStr = I_Loc\Menu_Random[14]
			End Select
		EndIf
	EndIf
	
	SetFont Font2
	
	DrawImage(MenuText, Config\Graphics\ScreenWidth / 2 - ImageWidth(MenuText) / 2, Config\Graphics\ScreenHeight - 20 * Gfx\MenuScale - ImageHeight(MenuText))
	
	If Config\Graphics\ScreenWidth > 1240 * Gfx\MenuScale Then
		DrawTiledImageRect(MenuWhite, 0, 5, 512, 7 * Gfx\MenuScale, 985.0 * Gfx\MenuScale, 407.0 * Gfx\MenuScale, (Config\Graphics\ScreenWidth - 1240 * Gfx\MenuScale) + 300, 7 * Gfx\MenuScale)
	EndIf
	
	If (Not MouseDown1)
		OnSliderID = 0
	EndIf
	
	If MainMenuTab = 0 Then
		x = 159 * Gfx\MenuScale
		y = 286 * Gfx\MenuScale

		width = 400 * Gfx\MenuScale
		height = 70 * Gfx\MenuScale

		Local spacing% = height + 10 * Gfx\MenuScale

		If DrawButton(x, y, width, height, I_Loc\Menu_NewUpper) Then
			HasNumericSeed = Config\Gameplay\UseNumericSeeds
			If HasNumericSeed Then
				RandomSeedNumeric = MilliSecs()
			Else
				RandomSeed = ""
				If Rand(15)=1 Then 
					Select Rand(13)
						Case 1 
							RandomSeed = "NIL"
						Case 2
							RandomSeed = "NO"
						Case 3
							RandomSeed = "d9341"
						Case 4
							RandomSeed = "5CP_I73"
						Case 5
							RandomSeed = "DONTBLINK"
						Case 6
							RandomSeed = "CRUNCH"
						Case 7
							RandomSeed = "die"
						Case 8
							RandomSeed = "HTAED"
						Case 9
							RandomSeed = "rustledjim"
						Case 10
							RandomSeed = "larry"
						Case 11
							RandomSeed = "JORGE"
						Case 12
							RandomSeed = "dirtymetal"
						Case 13
							RandomSeed = "whatpumpkin"
					End Select
				Else
					n = Rand(4,8)
					For i = 1 To n
						If Rand(3)=1 Then
							RandomSeed = RandomSeed + Rand(0,9)
						Else
							RandomSeed = RandomSeed + Chr(Rand(97,122))
						EndIf
					Next							
				EndIf
			EndIf
			
			MainMenuTab = 1
		EndIf

		y = y + spacing

		If DrawButton(x, y, width, height, I_Loc\Menu_LoadUpper) Then
			LoadSaveGames()
			MainMenuTab = 2
		EndIf

		y = y + spacing

		If DrawButton(x, y, width, height, I_Loc\Menu_ModsUpper, True, False, Not Flags\ModsEnabled) Then
			MainMenuTab = 8
		EndIf

		y = y + spacing

		If DrawButton(x, y, width, height, I_Loc\Menu_OptionsUpper) Then
			MainMenuTab = 3 : OnSliderID = 66
		EndiF

		y = y + spacing

		If DrawButton(x, y, width, height, I_Loc\Menu_QuitUpper) Then
			StopChannel(CurrMusicStream)
			End
		EndIf
		
	Else
		
		x = 159 * Gfx\MenuScale
		y = 286 * Gfx\MenuScale
		
		width = 400 * Gfx\MenuScale
		height = 70 * Gfx\MenuScale
		
		DrawFrame(x, y, width, height)
		
		If DrawButton(x + width + 10 * Gfx\MenuScale, y, 580 * Gfx\MenuScale - width - 10 * Gfx\MenuScale, height, I_Loc\Menu_BackUpper, False, False, UpdatingMod<>Null) Then 
			Select MainMenuTab
				Case 1
					PutINIValue(Paths\OptionsFile, "general", "intro enabled", IntroEnabled%)
					MainMenuTab = 0
				Case 2
					CurrLoadGamePage = 0
					MainMenuTab = 0
				Case 3,5,6,7 ;save the options
					SaveOptionsINI()
					
					UserTrackCheck% = 0
					UserTrackCheck2% = 0
					
					AntiAlias Config\Graphics\AntiAliasing
					Viewport_Recalculate()
					MainMenuTab = 0
				Case 4 ;move back to the "new game" tab
					MainMenuTab = 1
					CurrLoadGamePage = 0
					MouseHit1 = False
				Case 8
					CurrLoadGamePage = 0
					MainMenuTab = 0
					SerializeMods()
					If ModsDirty Then
						ModsDirty = False
						;; ToDo:: implement a more elegant reloading function to deal with the case where we need to fully reload all/some mods
					Else
						UpdateActiveMods()
					EndIf
				Default
					MainMenuTab = 0
			End Select
		EndIf
		
		Select MainMenuTab
			Case 1 ; New game
				;[Block]
				
				x = 159 * Gfx\MenuScale
				y = 286 * Gfx\MenuScale
				
				width = 400 * Gfx\MenuScale
				height = 70 * Gfx\MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, I_Loc\Menu_NewUpper, True, True)
				
				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				height = 330 * Gfx\MenuScale
				
				DrawFrame(x, y, width, height)				
				
				SetFont Font1
				
				Text (x + 20 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, I_Loc\NewGame_Name)
				CurrSave = InputBox(x + 150 * Gfx\MenuScale, y + 15 * Gfx\MenuScale, 200 * Gfx\MenuScale, 30 * Gfx\MenuScale, CurrSave, 1)
				CurrSave = Left(CurrSave, 15)
				CurrSave = Replace(CurrSave,":","")
				CurrSave = Replace(CurrSave,".","")
				CurrSave = Replace(CurrSave,"/","")
				CurrSave = Replace(CurrSave,"\","")
				CurrSave = Replace(CurrSave,"<","")
				CurrSave = Replace(CurrSave,">","")
				CurrSave = Replace(CurrSave,"|","")
				CurrSave = Replace(CurrSave,"?","")
				CurrSave = Replace(CurrSave,Chr(34),"")
				CurrSave = Replace(CurrSave,"*","")
				
				Color 255,255,255
				If SelectedMap = -1 Then
					Text (x + 20 * Gfx\MenuScale, y + 60 * Gfx\MenuScale, I_Loc\Menu_Seed)
					If HasNumericSeed Then
						Local inputBoxSeed$ = InputBox(x+150*Gfx\MenuScale, y+55*Gfx\MenuScale, 200*Gfx\MenuScale, 30*Gfx\MenuScale, Str(RandomSeedNumeric), 3, 3)
						If Instr(inputBoxSeed, "-", 2) <> 0 Then
							RandomSeedNumeric = -RandomSeedNumeric
						Else
							RandomSeedNumeric = Int(inputBoxSeed)
						EndIf
					Else
						RandomSeed = Left(InputBox(x+150*Gfx\MenuScale, y+55*Gfx\MenuScale, 200*Gfx\MenuScale, 30*Gfx\MenuScale, RandomSeed, 3),15)
					EndIf
				Else
					Text (x + 20 * Gfx\MenuScale, y + 60 * Gfx\MenuScale, I_Loc\Menu_Map)
					Color (255, 255, 255)
					Rect(x+150*Gfx\MenuScale, y+55*Gfx\MenuScale, 200*Gfx\MenuScale, 30*Gfx\MenuScale)
					Color (0, 0, 0)
					Rect(x+150*Gfx\MenuScale+2, y+55*Gfx\MenuScale+2, 200*Gfx\MenuScale-4, 30*Gfx\MenuScale-4)
					
					Color (255, 0,0)
					Local mapName$ = SavedMaps(SelectedMap)
					If Len(mapName)>15 Then
						Text(x+150*Gfx\MenuScale + 100*Gfx\MenuScale, y+55*Gfx\MenuScale + 15*Gfx\MenuScale, Left(mapName,14)+"...", True, True)
					Else
						Text(x+150*Gfx\MenuScale + 100*Gfx\MenuScale, y+55*Gfx\MenuScale + 15*Gfx\MenuScale, mapName, True, True)
					EndIf
					
					If DrawButton(x+370*Gfx\MenuScale, y+55*Gfx\MenuScale, 120*Gfx\MenuScale, 30*Gfx\MenuScale, I_Loc\NewGame_Deselect, False) Then
						SelectedMap=-1
					EndIf
				EndIf	
				
				Text(x + 20 * Gfx\MenuScale, y + 110 * Gfx\MenuScale, I_Loc\NewGame_Enableintro)
				IntroEnabled = DrawTick(x + 280 * Gfx\MenuScale, y + 110 * Gfx\MenuScale, IntroEnabled)	
				
				;Local modeName$, modeDescription$, selectedDescription$
				Text (x + 20 * Gfx\MenuScale, y + 150 * Gfx\MenuScale, I_Loc\Menu_Difficulty)				
				For i = SAFE To CUSTOM
					Local dif.Difficulty = Difficulties[i]
					If DrawTick(x + 20 * Gfx\MenuScale, y + (180+30*i) * Gfx\MenuScale, (SelectedDifficulty = dif)) Then SelectedDifficulty = dif
					Color(dif\r,dif\g,dif\b)
					Text(x + 50 * Gfx\MenuScale, y + (185+30*i) * Gfx\MenuScale, dif\localName)
				Next
				
				Color(255, 255, 255)
				DrawFrame(x + 150 * Gfx\MenuScale,y + 155 * Gfx\MenuScale, 410*Gfx\MenuScale, 150*Gfx\MenuScale)
				
				If SelectedDifficulty\customizable Then
					SelectedDifficulty\permaDeath =  DrawTick(x + 160 * Gfx\MenuScale, y + 165 * Gfx\MenuScale, SelectedDifficulty\permaDeath)
					Text(x + 200 * Gfx\MenuScale, y + 165 * Gfx\MenuScale, I_Loc\Difficulty_Permadeath)
					
					If DrawTick(x + 160 * Gfx\MenuScale, y + 195 * Gfx\MenuScale, SelectedDifficulty\saveType = SAVEANYWHERE And (Not SelectedDifficulty\permaDeath)) Then 
						SelectedDifficulty\saveType = SAVEANYWHERE
						SelectedDifficulty\permaDeath = False
					Else
						SelectedDifficulty\saveType = SAVEONSCREENS
					EndIf
					
					Text(x + 200 * Gfx\MenuScale, y + 195 * Gfx\MenuScale, I_Loc\Difficulty_Saveanywhere)	
					
					SelectedDifficulty\aggressiveNPCs =  DrawTick(x + 160 * Gfx\MenuScale, y + 225 * Gfx\MenuScale, SelectedDifficulty\aggressiveNPCs)
					Text(x + 200 * Gfx\MenuScale, y + 225 * Gfx\MenuScale, I_Loc\Difficulty_Aggressivenpcs)
					
					;Other factor's difficulty
					Color 255,255,255
					DrawImage ArrowIMG(1),x + 155 * Gfx\MenuScale, y+251*Gfx\MenuScale
					If MouseHit1
						If ImageRectOverlap(ArrowIMG(1),x + 155 * Gfx\MenuScale, y+251*Gfx\MenuScale, ScaledMouseX(),ScaledMouseY(),0,0)
							If SelectedDifficulty\otherFactors < HARD
								SelectedDifficulty\otherFactors = SelectedDifficulty\otherFactors + 1
							Else
								SelectedDifficulty\otherFactors = EASY
							EndIf
							PlaySFX(SFX_INTERACT_BUTTON_1)
						EndIf
					EndIf
					Color 255,255,255
					Select SelectedDifficulty\otherFactors
						Case EASY
							Text(x + 200 * Gfx\MenuScale, y + 255 * Gfx\MenuScale, Format(I_Loc\Difficulty_Other, I_Loc\Difficulty_OtherEasy))
						Case NORMAL
							Text(x + 200 * Gfx\MenuScale, y + 255 * Gfx\MenuScale, Format(I_Loc\Difficulty_Other, I_Loc\Difficulty_OtherNormal))
						Case HARD
							Text(x + 200 * Gfx\MenuScale, y + 255 * Gfx\MenuScale, Format(I_Loc\Difficulty_Other, I_Loc\Difficulty_OtherHard))
					End Select
				Else
					RowText(SelectedDifficulty\description, x+160*Gfx\MenuScale, y+165*Gfx\MenuScale, (410-20)*Gfx\MenuScale, 140*Gfx\MenuScale)					
				EndIf
				
				If DrawButton(x, y + height + 10 * Gfx\MenuScale, 160 * Gfx\MenuScale, 70 * Gfx\MenuScale, I_Loc\NewGame_Loadmap, False) Then
					MainMenuTab = 4
					LoadSavedMaps()
				EndIf
				
				SetFont Font2
				
				If DrawButton(x + 420 * Gfx\MenuScale, y + height + 10 * Gfx\MenuScale, 160 * Gfx\MenuScale, 70 * Gfx\MenuScale, I_Loc\NewGame_Start, False) Then
					TimerStopped = True

					If CurrSave = "" Then CurrSave = I_Loc\NewGame_Untitled
					Local SaveName$ = CurrSave
					
					Local SameFound% = 1

					LoadSaveGames()
					For i% = 1 To SaveGameAmount
						If SaveGames(i - 1) = CurrSave Then
							SameFound = SameFound + 1
							i = 0
							CurrSave = SaveName + " (" + Str(SameFound) + ")"
						EndIf
					Next
					
					If (Not HasNumericSeed) And RandomSeed = "" Then
						RandomSeed = Abs(MilliSecs())
					EndIf

					SeedRnd GetRandomSeed()

					SetErrorMsg(7, GetSeedString(False))

					LoadEntities()
					LoadAllSounds()
					InitRoomTemplates()
					InitNewGame()
					MainMenuOpen = False
					FlushKeys()
					FlushMouse()
					
					PutINIValue(Paths\OptionsFile, "general", "intro enabled", IntroEnabled%)
					
				EndIf
				
				;[End Block]
			Case 2 ;load game
				;[Block]
				
				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				;height = 300 * Gfx\MenuScale
				;height = 510 * Gfx\MenuScale
				height = PagingFrameHeight
				
				DrawFrame(x, y, width, height)
				
				x = 159 * Gfx\MenuScale
				y = 286 * Gfx\MenuScale
				
				width = 400 * Gfx\MenuScale
				height = 70 * Gfx\MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, I_Loc\Menu_LoadUpper, True, True)
				
				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				height = 296 * Gfx\MenuScale
				
				;SetFont Font1	
				
				SetFont Font2
				
				If CurrLoadGamePage < Ceil(Float(SaveGameAmount)/EntriesPerPage)-1 And SaveMSG = "" Then 
					If DrawButton(x+530*Gfx\MenuScale, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale, ">") Then
						CurrLoadGamePage = CurrLoadGamePage+1
					EndIf
				Else
					DrawFrame(x+530*Gfx\MenuScale, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale)
					Color(100, 100, 100)
					Text(x+555*Gfx\MenuScale, y + PagingFrameHeight + 27.5*Gfx\MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 And SaveMSG = "" Then
					If DrawButton(x, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale, "<") Then
						CurrLoadGamePage = CurrLoadGamePage-1
					EndIf
				Else
					DrawFrame(x, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale)
					Color(100, 100, 100)
					Text(x+25*Gfx\MenuScale, y + PagingFrameHeight + 27.5*Gfx\MenuScale, "<", True, True)
				EndIf
				
				DrawFrame(x+50*Gfx\MenuScale,y+PagingFrameHeight,width-100*Gfx\MenuScale,55*Gfx\MenuScale)
				
				Text(x+(width/2.0),y+PagingFrameHeight+26*Gfx\MenuScale,Format(I_Loc\Menu_Page, Int(Max((CurrLoadGamePage+1),1)), Int(Max((Int(Ceil(Float(SaveGameAmount)/EntriesPerPage))),1))),True,True)
				
				SetFont Font1
				
				If CurrLoadGamePage > Ceil(Float(SaveGameAmount)/EntriesPerPage)-1 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
				EndIf
				
				If SaveGameAmount = 0 Then
					Text (x + 20 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, I_Loc\LoadGame_Nosaved)
				Else
					x = x + 20 * Gfx\MenuScale
					y = y + 20 * Gfx\MenuScale
					
					For i% = (1+(EntriesPerPage*CurrLoadGamePage)) To EntriesPerPage+(EntriesPerPage*CurrLoadGamePage)
						If i <= SaveGameAmount Then
							DrawFrame(x,y,540* Gfx\MenuScale, 70* Gfx\MenuScale)
							
							If SaveGameVersion(i - 1) <> CompatibleNumber Then
								Color 255,0,0
							Else
								Color 255,255,255
							EndIf
							
							Text(x + 20 * Gfx\MenuScale, y + 10 * Gfx\MenuScale, SaveGames(i - 1))
							Text(x + 20 * Gfx\MenuScale, y + (10+18) * Gfx\MenuScale, SaveGameTime(i - 1) + RSet(SaveGameDate(i - 1), 21 - Len(SaveGameTime(i - 1))))
							Text(x + 20 * Gfx\MenuScale, y + (10+36) * Gfx\MenuScale, SaveGameVersion(i - 1) + RSet(FormatDuration(SaveGamePlayTime(i - 1), False), 21 - Len(SaveGameVersion(i - 1))))
							
							If SaveMSG = "" Then
								If SaveGameVersion(i - 1) <> CompatibleNumber Then
									DrawFrame(x + 300 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale)
									Color(255, 0, 0)
									Text(x + 350 * Gfx\MenuScale, y + 35 * Gfx\MenuScale, I_Loc\LoadGame_Load, True, True)
								Else
									If DrawButton(x + 300 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\LoadGame_Load, False) Then
										LoadEntities()
										LoadAllSounds()
										InitRoomTemplates()
										LoadGame(SaveGames(i - 1))
										CurrSave = SaveGames(i - 1)
										InitLoadGame()
										MainMenuOpen = False
										Return
									EndIf
								EndIf
								
								If DrawButton(x + 420 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\LoadGame_Delete, False) Then
									SaveMSG = SaveGames(i - 1)
									DebugLog SaveMSG
									Exit
								EndIf
							Else
								DrawFrame(x + 300 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale)
								If SaveGameVersion(i - 1) <> CompatibleNumber Then
									Color(255, 0, 0)
								Else
									Color(100, 100, 100)
								EndIf
								Text(x + 350 * Gfx\MenuScale, y + 35 * Gfx\MenuScale, I_Loc\LoadGame_Load, True, True)
								
								DrawFrame(x + 420 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale)
								Color(100, 100, 100)
								Text(x + 470 * Gfx\MenuScale, y + 35 * Gfx\MenuScale, I_Loc\LoadGame_Delete, True, True)
							EndIf
							
							y = y + 80 * Gfx\MenuScale
						Else
							Exit
						EndIf
					Next
					
					If SaveMSG <> ""
						x = 740 * Gfx\MenuScale
						y = 366 * Gfx\MenuScale
						height = 100 * Gfx\MenuScale
						DrawFrame(x, y, 420 * Gfx\MenuScale, height)
						RowText(I_Loc\LoadGame_DeleteConfirm, x + 20 * Gfx\MenuScale, y + 15 * Gfx\MenuScale, 400 * Gfx\MenuScale, height)
						y = y + height - (30 + 15) * Gfx\MenuScale
						If DrawButton(x + 50 * Gfx\MenuScale, y, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Menu_Yes, False) Then
							DeleteFile(CurrentDir() + SavePath + SaveMSG + ".cbsav")
							SaveMSG = ""
							LoadSaveGames()
						EndIf
						If DrawButton(x + 250 * Gfx\MenuScale, y, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Menu_No, False) Then
							SaveMSG = ""
						EndIf
					EndIf
				EndIf
				
				
				
				;[End Block]
			Case 3,5,6,7 ;options
				;[Block]
				
				x = 159 * Gfx\MenuScale
				y = 286 * Gfx\MenuScale
				
				width = 400 * Gfx\MenuScale
				height = 70 * Gfx\MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, I_Loc\Menu_OptionsUpper, True, True)
				
				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				height = 60 * Gfx\MenuScale
				DrawFrame(x, y, width, height)
				
				Color 0,255,0
				If MainMenuTab = 3
					Rect(x+15*Gfx\MenuScale,y+10*Gfx\MenuScale,(width/5)+10*Gfx\MenuScale,(height/2)+10*Gfx\MenuScale,True)
				ElseIf MainMenuTab = 5
					Rect(x+155*Gfx\MenuScale,y+10*Gfx\MenuScale,(width/5)+10*Gfx\MenuScale,(height/2)+10*Gfx\MenuScale,True)
				ElseIf MainMenuTab = 6
					Rect(x+295*Gfx\MenuScale,y+10*Gfx\MenuScale,(width/5)+10*Gfx\MenuScale,(height/2)+10*Gfx\MenuScale,True)
				ElseIf MainMenuTab = 7
					Rect(x+435*Gfx\MenuScale,y+10*Gfx\MenuScale,(width/5)+10*Gfx\MenuScale,(height/2)+10*Gfx\MenuScale,True)
				EndIf
				
				Color 255,255,255
				If DrawButton(x+20*Gfx\MenuScale,y+15*Gfx\MenuScale,width/5,height/2, I_Loc\Option_Graphics, False) Then MainMenuTab = 3
				If DrawButton(x+160*Gfx\MenuScale,y+15*Gfx\MenuScale,width/5,height/2, I_Loc\Option_Audio, False) Then MainMenuTab = 5
				If DrawButton(x+300*Gfx\MenuScale,y+15*Gfx\MenuScale,width/5,height/2, I_Loc\Option_Controls, False) Then MainMenuTab = 6
				If DrawButton(x+440*Gfx\MenuScale,y+15*Gfx\MenuScale,width/5,height/2, I_Loc\Option_Advanced, False) Then MainMenuTab = 7
				
				SetFont Font1
				y = y + 70 * Gfx\MenuScale
				
				If MainMenuTab <> 5
					UserTrackCheck% = 0
					UserTrackCheck2% = 0
				EndIf
				
				Local tx# = x+width
				Local ty# = y
				Local tw# = 440*Gfx\MenuScale
				Local th# = 160*Gfx\MenuScale
				
				;DrawOptionsTooltip(tx,ty,tw,th,"")
				
				If MainMenuTab = 3 ;Graphics
					;[Block]
					;height = 380 * Gfx\MenuScale
					height = 330 * Gfx\MenuScale
					DrawFrame(x, y, width, height)
					
					y=y+20*Gfx\MenuScale
					
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Vsync)
					Config\Graphics\Vsync% = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, Config\Graphics\Vsync%)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"vsync")
					EndIf
					
					y=y+30*Gfx\MenuScale
					
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Antialias)
					Config\Graphics\AntiAliasing = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, Config\Graphics\AntiAliasing%)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"antialias")
					EndIf
					
					y=y+30*Gfx\MenuScale
					
					;Local prevGamma# = Config\Graphics\ScreenGamma
					Config\Graphics\ScreenGamma = (SlideBar(x + 310*Gfx\MenuScale, y+6*Gfx\MenuScale, 150*Gfx\MenuScale, Config\Graphics\ScreenGamma*50.0, 1)/50.0)
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Gamma)
					If (MouseOn(x+310*Gfx\MenuScale,y+6*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=1
						DrawOptionsTooltip(tx,ty,tw,th,"gamma",Config\Graphics\ScreenGamma)
					EndIf

					y=y+50*Gfx\MenuScale
					
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Texlod)
					Config\Graphics\TextureDetails = Slider5(x+310*Gfx\MenuScale,y+6*Gfx\MenuScale,150*Gfx\MenuScale,Config\Graphics\TextureDetails,3,"0.8","0.4","0.0","-0.4","-0.8")
					Select Config\Graphics\TextureDetails%
						Case 0
							Gfx\TextureLODBias# = 0.8
						Case 1
							Gfx\TextureLODBias# = 0.4
						Case 2
							Gfx\TextureLODBias# = 0.0
						Case 3
							Gfx\TextureLODBias# = -0.4
						Case 4
							Gfx\TextureLODBias# = -0.8
					End Select
					TextureLodBias Gfx\TextureLODBias
					If (MouseOn(x+310*Gfx\MenuScale,y-6*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Or OnSliderID=3
						DrawOptionsTooltip(tx,ty,tw,th+100*Gfx\MenuScale,"texquality")
					EndIf
					
					y=y+50*Gfx\MenuScale
					
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Vram)
					EnableVRam = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, EnableVRam)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"vram")
					EndIf

					y=y+50*Gfx\MenuScale

					Config\Graphics\HUDOffset = SlideBar(x + 310*Gfx\MenuScale, y+6*Gfx\MenuScale,150*Gfx\MenuScale, Config\Graphics\HUDOffset*100, 5)/100
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Hudoffset)
					If (MouseOn(x+310*Gfx\MenuScale,y+6*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=5
						DrawOptionsTooltip(tx,ty,tw,th,"hudoffset")
					EndIf

					y=y+50*Gfx\MenuScale
					
					Local SlideBarFOV# = Config\Graphics\FOV-40
					SlideBarFOV = (SlideBar(x + 310*Gfx\MenuScale, y+6*Gfx\MenuScale,150*Gfx\MenuScale, SlideBarFOV*2.0, 4)/2.0)
					Config\Graphics\FOV = Int(SlideBarFOV+40)
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_fov)
					Color 255,255,0
					Text(x + 25 * Gfx\MenuScale, y + 25 * Gfx\MenuScale, Config\Graphics\FOV+"°")
					If (MouseOn(x+310*Gfx\MenuScale,y+6*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=4
						DrawOptionsTooltip(tx,ty,tw,th,"fov")
					EndIf

					;[End Block]
				ElseIf MainMenuTab = 5 ;Audio
					;[Block]
					height = 220 * Gfx\MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 20*Gfx\MenuScale
					
					MusicVolume = (SlideBar(x + 310*Gfx\MenuScale, y-4*Gfx\MenuScale, 150*Gfx\MenuScale, MusicVolume*100.0, 1)/100.0)
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Musicvol)
					If (MouseOn(x+310*Gfx\MenuScale,y-4*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=1
						DrawOptionsTooltip(tx,ty,tw,th,"musicvol",MusicVolume)
					EndIf
					
					y = y + 40*Gfx\MenuScale
					
					;Config\Audio\SFXVolume = (SlideBar(x + 310*Gfx\MenuScale, y-4*Gfx\MenuScale, 150*Gfx\MenuScale, Config\Audio\SFXVolume*100.0)/100.0)
					PrevSFXVolume = (SlideBar(x + 310*Gfx\MenuScale, y-4*Gfx\MenuScale, 150*Gfx\MenuScale, Config\Audio\SFXVolume*100.0, 2)/100.0)
					Config\Audio\SFXVolume = PrevSFXVolume
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Soundvol)
					If (MouseOn(x+310*Gfx\MenuScale,y-4*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=2
						DrawOptionsTooltip(tx,ty,tw,th,"soundvol",PrevSFXVolume)
					EndIf
					;If MouseDown1 Then
					;	If MouseX() >= x And MouseX() <= x + width + 14 And MouseY() >= y And MouseY() <= y + 20 Then
					;		PlayTestSound(True)
					;	Else
					;		PlayTestSound(False)
					;	EndIf
					;Else
					;	PlayTestSound(False)
					;EndIf
					
					y = y + 30*Gfx\MenuScale
					
					Color 255,255,255
					Text x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Sfxautorelease
					Config\Audio\EnableSFXRelease = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, Config\Audio\EnableSFXRelease)
					If EnableSFXRelease_Prev% <> Config\Audio\EnableSFXRelease
						If Config\Audio\EnableSFXRelease%
							For snd.Sound = Each Sound
								For i=0 To 31
									If snd\channels[i]<>0 Then
										If ChannelPlaying(snd\channels[i]) Then
											StopChannel(snd\channels[i])
										EndIf
									EndIf
								Next
								If snd\internalHandle<>0 Then
									FreeSound snd\internalHandle
									snd\internalHandle = 0
								EndIf
								snd\releaseTime = 0
							Next
						Else
							For snd.Sound = Each Sound
								If snd\internalHandle = 0 Then snd\internalHandle = LoadSound_Strict(snd\name)
							Next
						EndIf
						EnableSFXRelease_Prev% = Config\Audio\EnableSFXRelease
					EndIf
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th+220*Gfx\MenuScale,"sfxautorelease")
					EndIf
					y = y + 30*Gfx\MenuScale
					
					Color 255,255,255
					Text x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Usertrack
					EnableUserTracks = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, EnableUserTracks)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"usertrack")
					EndIf
					
					If EnableUserTracks
						y = y + 30 * Gfx\MenuScale
						Color 255,255,255
						Text x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Usertrackmode
						UserTrackMode = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, UserTrackMode)
						If UserTrackMode
							Text x + 350 * Gfx\MenuScale, y + Gfx\MenuScale, I_Loc\OptionName_UsertrackmodeRepeat
						Else
							Text x + 350 * Gfx\MenuScale, y + Gfx\MenuScale, I_Loc\OptionName_UsertrackmodeRandom
						EndIf
						If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
							DrawOptionsTooltip(tx,ty,tw,th,"usertrackmode")
						EndIf
						If DrawButton(x + 20 * Gfx\MenuScale, y + 30 * Gfx\MenuScale, 250 * Gfx\MenuScale, 25 * Gfx\MenuScale, I_Loc\OptionName_Usertrackscan,False)
							DebugLog "User Tracks Check Started"
							
							UserTrackCheck% = 0
							UserTrackCheck2% = 0
							
							Dir=ReadDir("SFX\Radio\UserTracks\")
							Repeat
								file$=NextFile(Dir)
								If file$="" Then Exit
								If FileType("SFX\Radio\UserTracks\"+file$) = 1 Then
									UserTrackCheck = UserTrackCheck + 1
									test = LoadSound("SFX\Radio\UserTracks\"+file$)
									If test<>0
										UserTrackCheck2 = UserTrackCheck2 + 1
									EndIf
									FreeSound test
								EndIf
							Forever
							CloseDir Dir
							
							DebugLog "User Tracks Check Ended"
						EndIf
						If MouseOn(x+20*Gfx\MenuScale,y+30*Gfx\MenuScale,190*Gfx\MenuScale,25*Gfx\MenuScale) And OnSliderID=0
							DrawOptionsTooltip(tx,ty,tw,th,"usertrackscan")
						EndIf
						If UserTrackCheck%>0
							Text x + 20 * Gfx\MenuScale, y + 100 * Gfx\MenuScale, Format(I_Loc\OptionName_UsertrackscanFound, UserTrackCheck2, UserTrackCheck)
						EndIf
					Else
						UserTrackCheck%=0
					EndIf
					;[End Block]
				ElseIf MainMenuTab = 6 ;Controls
					;[Block]
					height = 270 * Gfx\MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 20*Gfx\MenuScale
					
					MouseSens = (SlideBar(x + 310*Gfx\MenuScale, y-4*Gfx\MenuScale, 150*Gfx\MenuScale, (MouseSens+0.5)*100.0, 1)/100.0)-0.5
					Color(255, 255, 255)
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Mousesensitivity)
					If (MouseOn(x+310*Gfx\MenuScale,y-4*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=1
						DrawOptionsTooltip(tx,ty,tw,th,"mousesensitivity",MouseSens)
					EndIf
					
					y = y + 40*Gfx\MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Mouseinvert)
					InvertMouse = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, InvertMouse)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"mouseinvert")
					EndIf
					
					y = y + 40*Gfx\MenuScale
					
					MouseSmooth = (SlideBar(x + 310*Gfx\MenuScale, y-4*Gfx\MenuScale, 150*Gfx\MenuScale, (MouseSmooth)*50.0, 2)/50.0)
					Color(255, 255, 255)
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Mousesmoothing)
					If (MouseOn(x+310*Gfx\MenuScale,y-4*Gfx\MenuScale,150*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=2
						DrawOptionsTooltip(tx,ty,tw,th,"mousesmoothing",MouseSmooth)
					EndIf
					
					Color(255, 255, 255)
					
					y = y + 30*Gfx\MenuScale
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Binds)
					y = y + 10*Gfx\MenuScale
					
					Text(x + 20 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, I_Loc\OptionName_BindMoveForward)
					InputBox(x + 170 * Gfx\MenuScale, y + 20 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_UP,210)),5,-1)
					Text(x + 20 * Gfx\MenuScale, y + 40 * Gfx\MenuScale, I_Loc\OptionName_BindMoveLeft)
					InputBox(x + 170 * Gfx\MenuScale, y + 40 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_LEFT,210)),3,-1)
					Text(x + 20 * Gfx\MenuScale, y + 60 * Gfx\MenuScale, I_Loc\OptionName_BindMoveBack)
					InputBox(x + 170 * Gfx\MenuScale, y + 60 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_DOWN,210)),6,-1)
					Text(x + 20 * Gfx\MenuScale, y + 80 * Gfx\MenuScale, I_Loc\OptionName_BindMoveRight)
					InputBox(x + 170 * Gfx\MenuScale, y + 80 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_RIGHT,210)),4,-1)
					Text(x + 20 * Gfx\MenuScale, y + 100 * Gfx\MenuScale, I_Loc\OptionName_BindSave)
					InputBox(x + 170 * Gfx\MenuScale, y + 100 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_SAVE,210)),11,-1)

					Text(x + 280 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, I_Loc\OptionName_BindBlink)
					InputBox(x + 470 * Gfx\MenuScale, y + 20 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_BLINK,210)),7,-1)
					Text(x + 280 * Gfx\MenuScale, y + 40 * Gfx\MenuScale, I_Loc\OptionName_BindSprint)
					InputBox(x + 470 * Gfx\MenuScale, y + 40 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_SPRINT,210)),8,-1)
					Text(x + 280 * Gfx\MenuScale, y + 60 * Gfx\MenuScale, I_Loc\OptionName_BindInv)
					InputBox(x + 470 * Gfx\MenuScale, y + 60 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_INV,210)),9,-1)
					Text(x + 280 * Gfx\MenuScale, y + 80 * Gfx\MenuScale, I_Loc\OptionName_BindCrouch)
					InputBox(x + 470 * Gfx\MenuScale, y + 80 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_CROUCH,210)),10,-1)
					Text(x + 280 * Gfx\MenuScale, y + 100 * Gfx\MenuScale, I_Loc\OptionName_BindConsole)
					InputBox(x + 470 * Gfx\MenuScale, y + 100 * Gfx\MenuScale,100*Gfx\MenuScale,20*Gfx\MenuScale,KeyName(Min(KEY_CONSOLE,210)),12,-1)
					
					If MouseOn(x+20*Gfx\MenuScale,y,width-40*Gfx\MenuScale,120*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"controls")
					EndIf
					
					For i = 0 To 227
						If KeyHit(i) Then key = i : Exit
					Next
					If key<>0 Then
						Select SelectedInputBox
							Case 3
								KEY_LEFT = key
							Case 4
								KEY_RIGHT = key
							Case 5
								KEY_UP = key
							Case 6
								KEY_DOWN = key
							Case 7
								KEY_BLINK = key
							Case 8
								KEY_SPRINT = key
							Case 9
								KEY_INV = key
							Case 10
								KEY_CROUCH = key
							Case 11
								KEY_SAVE = key
							Case 12
								KEY_CONSOLE = key
						End Select
						SelectedInputBox = 0
					EndIf
					;[End Block]
				ElseIf MainMenuTab = 7 ;Advanced
					;[Block]
					height = (325 + (Config\Graphics\Framelimit > 0.0) * 30) * Gfx\MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 20*Gfx\MenuScale
					
					Color 255,255,255				
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Showhud)	
					HUDenabled = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, HUDenabled)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"hud")
					EndIf
					
					y=y+30*Gfx\MenuScale
					
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Console)
					ConsoleEnabled = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, ConsoleEnabled)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"consoleenable")
					EndIf

					y = y + 30*Gfx\MenuScale

					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Speedrunmode)
					SpeedRunMode = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, SpeedRunMode)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"speedrunmode")
					EndIf

					y = y + 30*Gfx\MenuScale

					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Numericseeds)
					Config\Gameplay\UseNumericSeeds = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, Config\Gameplay\UseNumericSeeds)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"numericseeds")
					EndIf
					
					y = y + 50*Gfx\MenuScale
					
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Achpopup)
					AchvMSGenabled% = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, AchvMSGenabled%)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"achpopup")
					EndIf

					y = y + 50*Gfx\MenuScale

					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Launcher)
					Config\Launcher\LauncherEnabled% = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, Config\Launcher\LauncherEnabled%)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"launcher")
					EndIf
					
					y = y + 50*Gfx\MenuScale
					
					Color 255,255,255
					Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Showfps)
					Config\Graphics\ShowFPS% = DrawTick(x + 310 * Gfx\MenuScale, y + Gfx\MenuScale, Config\Graphics\ShowFPS%)
					If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						DrawOptionsTooltip(tx,ty,tw,th,"showfps")
					EndIf
					
					; ============================================================
					; Removed Option for framelimit
					; Reason: the slider is a normalized range from 0-100%.
					; framelimit is an int and this is unintuitive to use
					; This same code (buy with adjusted x,y offsets) appears in main.bb
					; ============================================================
					;y = y + 30*Gfx\MenuScale
					
					;Color 255,255,255
					;Text(x + 20 * Gfx\MenuScale, y, I_Loc\OptionName_Framelimit)
					;Color 255,255,255
					;If DrawTick(x + 310 * Gfx\MenuScale, y, Config\Graphics\Framelimit > 0) Then
						;Config\Graphics\Framelimit = Int(SlideBar(x + 150*Gfx\MenuScale, y+30*Gfx\MenuScale, 100*Gfx\MenuScale, Config\Graphics\Framelimit, 1))
						;Config\Graphics\Framelimit = Min(Config\Graphics\Framelimit, 0)
						;Color 255,255,0
						;Text(x + 25 * Gfx\MenuScale, y + 25 * Gfx\MenuScale, Format(I_Loc\OptionName_FramelimitFps, Config\Graphics\Framelimit%))
						;If (MouseOn(x+150*Gfx\MenuScale,y+30*Gfx\MenuScale,100*Gfx\MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=1
							;DrawOptionsTooltip(tx,ty,tw,th,"framelimit",Config\Graphics\Framelimit)
						;EndIf
					;Else
						;Config\Graphics\Framelimit = 0
					;EndIf
					;If MouseOn(x+310*Gfx\MenuScale,y+Gfx\MenuScale,20*Gfx\MenuScale,20*Gfx\MenuScale) And OnSliderID=0
						;DrawOptionsTooltip(tx,ty,tw,th,"framelimit",Config\Graphics\Framelimit)
					;EndIf
					; ============================================================
					;[End Block]
				EndIf
				;[End Block]
			Case 4 ; load map
				;[Block]
				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				
				DrawFrame(x, y, width, PagingFrameHeight)
				
				x = 159 * Gfx\MenuScale
				y = 286 * Gfx\MenuScale
				
				width = 400 * Gfx\MenuScale
				height = 70 * Gfx\MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, I_Loc\NewGame_LoadmapUpper, True, True)
				
				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				height = 350 * Gfx\MenuScale
				
				SetFont Font2
				
				tx# = x+width+1*Gfx\MenuScale
				ty# = y
				tw# = 400*Gfx\MenuScale
				th# = 150*Gfx\MenuScale
				
				If CurrLoadGamePage < Ceil(Float(SavedMapsAmount)/EntriesPerPage)-1 Then 
					If DrawButton(x+530*Gfx\MenuScale, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale, ">") Then
						CurrLoadGamePage = CurrLoadGamePage+1
					EndIf
				Else
					DrawFrame(x+530*Gfx\MenuScale, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale)
					Color(100, 100, 100)
					Text(x+555*Gfx\MenuScale, y + PagingFrameHeight + 27.5*Gfx\MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 Then
					If DrawButton(x, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale, "<") Then
						CurrLoadGamePage = CurrLoadGamePage-1
					EndIf
				Else
					DrawFrame(x, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale)
					Color(100, 100, 100)
					Text(x+25*Gfx\MenuScale, y + PagingFrameHeight + 27.5*Gfx\MenuScale, "<", True, True)
				EndIf
				
				DrawFrame(x+50*Gfx\MenuScale,y+PagingFrameHeight,width-100*Gfx\MenuScale,55*Gfx\MenuScale)
				
				Text(x+(width/2.0),y+PagingFrameHeight+26*Gfx\MenuScale,Format(I_Loc\Menu_Page, Int(Max((CurrLoadGamePage+1),1)), Int(Max((Int(Ceil(Float(SavedMapsAmount)/EntriesPerPage))),1))),True,True)
				
				SetFont Font1
				
				If CurrLoadGamePage > Ceil(Float(SavedMapsAmount)/EntriesPerPage)-1 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
				EndIf
				
				SetFont Font1
				
				If SavedMaps(0)="" Then 
					Text (x + 20 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, I_Loc\LoadMap_Nomaps)
				Else
					x = x + 20 * Gfx\MenuScale
					y = y + 20 * Gfx\MenuScale
					For i = (1+(EntriesPerPage*CurrLoadGamePage)) To EntriesPerPage+(EntriesPerPage*CurrLoadGamePage)
						If i <= SavedMapsAmount Then
							DrawFrame(x,y,540* Gfx\MenuScale, 70* Gfx\MenuScale)
							
							Text(x + 20 * Gfx\MenuScale, y + 15 * Gfx\MenuScale, SavedMaps(i - 1))
							Text(x + 20 * Gfx\MenuScale, y + (15+27) * Gfx\MenuScale, SavedMapsAuthor(i - 1))
							
							If DrawButton(x + 400 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\LoadGame_Load, False) Then
								SelectedMap=i - 1
								MainMenuTab = 1
							EndIf
							If MouseOn(x + 400 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100*Gfx\MenuScale,30*Gfx\MenuScale)
								DrawMapCreatorTooltip(tx,ty,tw,th,SavedMapsPath(i-1))
							EndIf
							
							y = y + 80 * Gfx\MenuScale
						Else
							Exit
						EndIf
					Next
				EndIf
				;[End Block]
			Case 8 ;Mods

				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				height = PagingFrameHeight

				DrawFrame(x, y, width, height)
				
				x = 159 * Gfx\MenuScale
				y = 286 * Gfx\MenuScale
				
				width = 400 * Gfx\MenuScale
				height = 70 * Gfx\MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, I_Loc\Menu_ModsUpper, True, True)
				
				y = y + height + 10 * Gfx\MenuScale
				width = 580 * Gfx\MenuScale
				height = 296 * Gfx\MenuScale
				
				SetFont Font2

				If CurrLoadGamePage < Ceil(Float(ModCount)/EntriesPerPage)-1 And SaveMSG = "" Then 
					If DrawButton(x+530*Gfx\MenuScale, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale, ">") Then
						CurrLoadGamePage = CurrLoadGamePage+1
					EndIf
				Else
					DrawFrame(x+530*Gfx\MenuScale, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale)
					Color(100, 100, 100)
					Text(x+555*Gfx\MenuScale, y + PagingFrameHeight + 27.5*Gfx\MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 And SaveMSG = "" Then
					If DrawButton(x, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale, "<") Then
						CurrLoadGamePage = CurrLoadGamePage-1
					EndIf
				Else
					DrawFrame(x, y + PagingFrameHeight, 50*Gfx\MenuScale, 55*Gfx\MenuScale)
					Color(100, 100, 100)
					Text(x+25*Gfx\MenuScale, y + PagingFrameHeight + 27.5*Gfx\MenuScale, "<", True, True)
				EndIf
				
				DrawFrame(x+50*Gfx\MenuScale,y+PagingFrameHeight,width-100*Gfx\MenuScale,55*Gfx\MenuScale)
				
				Text(x+(width/2.0),y+PagingFrameHeight+26*Gfx\MenuScale,Format(I_Loc\Menu_Page, Int(Max((CurrLoadGamePage+1),1)), Int(Max((Int(Ceil(Float(ModCount)/entriesPerPage))),1))),True,True)
				
				SetFont Font1
				
				If CurrLoadGamePage > Ceil(Float(ModCount)/EntriesPerPage)-1 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
				EndIf

				If ModCount = 0 Then
					Text (x + 20 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, I_Loc\Mods_Nomods)
				Else
					x = x + 10 * Gfx\MenuScale
					y = y + (20 + (EntriesPerPage-1) * 80) * Gfx\MenuScale
					
					UpdateUpdatingMod()

					Local xStart = x

					Local milis% = MilliSecs()
					i% = ModCount
					If (i Mod EntriesPerPage) <> 0 Then i = i - (i Mod EntriesPerPage) + EntriesPerPage
					Local drawn% = 0
					Local m.Mods = Last Mods
					Repeat
						If i <= (EntriesPerPage+(EntriesPerPage*CurrLoadGamePage)) Then
							If i <= ModCount Then
								x = xStart

								Local mActive = DrawTick(x, y + 25 * Gfx\MenuScale, m\IsActive)
								If mActive <> m\IsActive Then
									m\IsActive = mActive
									m\IsNew = False
									If m\RequiresReload Then ModsDirty = True
								EndIf

								x = x + 25 * Gfx\MenuScale

								DrawFrame(x,y,490* Gfx\MenuScale, 70 * Gfx\MenuScale)
								If m\Icon = 0 And m\Iconpath <> "" Then
									m\Icon = LoadImage_Strict(m\IconPath)
									m\DisabledIcon = CreateGrayScaleImage(m\Icon)
									ResizeImage(m\Icon, 64 * Gfx\MenuScale, 64 * Gfx\MenuScale)
									ResizeImage(m\DisabledIcon, 64 * Gfx\MenuScale, 64 * Gfx\MenuScale)
								EndIf

								If m\Icon <> 0 Then
									Local ico%
									If m\IsActive Then ico = m\Icon Else ico = m\DisabledIcon
									DrawImage(ico, x + 3 * Gfx\MenuScale, y + 3 * Gfx\MenuScale)
								EndIf

								If m\IsNew And milis Mod 1200 >= 600 Then DrawImage(NewModBlink, x + 2 * Gfx\MenuScale, y + 2 * Gfx\MenuScale)

								If m\IsActive Then
									Color 255, 255, 255
								Else
									Color 150, 150, 150
								EndIf

								Text(x + 85 * Gfx\MenuScale, y + 10 * Gfx\MenuScale, EllipsisLeft(m\Name, 24))
								Text(x + 85 * Gfx\MenuScale, y + (10+18) * Gfx\MenuScale, EllipsisLeft(m\Description, 24))
								Text(x + 85 * Gfx\MenuScale, y + (10+18*2) * Gfx\MenuScale, EllipsisLeft(m\Author, 24))

								If DrawButton(x + 500 * Gfx\MenuScale, y + 10 * Gfx\MenuScale, 30 * Gfx\MenuScale, 20 * Gfx\MenuScale, "▲", False, False, i = 1) Then
									Insert m Before Before m
									m = After m
									If m\IsActive And m\RequiresReload Then ModsDirty = True
								EndIf
								
								If DrawButton(x + 500 * Gfx\MenuScale, y + (70 - 30) * Gfx\MenuScale, 30 * Gfx\MenuScale, 20 * Gfx\MenuScale, "▼", False, False, i = ModCount) Then
									Insert m After After m
									m = Before m
									If m\IsActive And m\RequiresReload Then ModsDirty = True
								EndIf

								If UpdatingMod = m Then
									Local strr$ = ""
									Local slice% = (milis Mod 1200) / 200
									Select slice
										Case 0
											strr = "   "
										Case 1
											strr = ".  "
										Case 2
											strr = ".. "
										Case 3
											strr = "..."
										Case 4
											strr = " .."
										Case 5
											strr = "  ."
									End Select
									DrawButton(x + 370 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, strr, False, False, True)
								Else
									Local buttonsInactive% = UpdateModErrorCode <> 0 Or ModUIState <> 0 Or UpdatingMod <> Null Or (Not Flags\SteamActive)
									If m\SteamWorkshopId = "" Then
										If DrawButton(x + 370 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Mods_Upload, False, False, buttonsInactive) Then
											ModUIState = 1
											SelectedMod = m
											ReadTagsFromMod(m)
										EndIf
									Else
										If m\IsUserOwner Then
											If DrawButton(x + 370 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Mods_Update, False, False, buttonsInactive) Then
												ModUIState = 2
												SelectedMod = m
												ReadTagsFromMod(m)
											EndIf
										Else
											If DrawButton(x + 370 * Gfx\MenuScale, y + 20 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Mods_Visit, False, False, buttonsInactive) Then
												VisitModPage(m)
											EndIf
										EndIf
									EndIf
								EndIf

								If Len(m\Name) > 24 And MouseX() > x + 85 * Gfx\MenuScale And MouseY() > y + 10 * Gfx\MenuScale And MouseX() < x + 340 * Gfx\MenuScale And MouseY() < y + (10 + 15) * Gfx\MenuScale Then
									DrawFramedRowText(m\Name, MouseX(), MouseY(), 400 * Gfx\MenuScale)
								EndIf

								If Len(m\Description) > 24 And MouseX() > x + 85 * Gfx\MenuScale And MouseY() > y + (10 + 18) * Gfx\MenuScale And MouseX() < x + 340 * Gfx\MenuScale And MouseY() < y + (10 + 15 + 18) * Gfx\MenuScale Then
									DrawFramedRowText(m\Description, MouseX(), MouseY(), 400 * Gfx\MenuScale)
								EndIf

								If Len(m\Author) > 24 And MouseX() > x + 85 * Gfx\MenuScale And MouseY() > y + (10 + 2*18) * Gfx\MenuScale And MouseX() < x + 340 * Gfx\MenuScale And MouseY() < y + (10 + 15 + 2*18) * Gfx\MenuScale Then
									DrawFramedRowText(m\Author, MouseX(), MouseY(), 400 * Gfx\MenuScale)
								EndIf
							EndIf

							y = y - 80 * Gfx\MenuScale
							drawn = drawn + 1
						EndIf
						If i <= ModCount Then m = Before m
						i = i - 1
					Until i <= 0 Lor drawn => EntriesPerPage

					x = 740 * Gfx\MenuScale
					y = 366 * Gfx\MenuScale
					If UpdateModErrorCode <> 0
						DrawFrame(x, y, 420 * Gfx\MenuScale, 200 * Gfx\MenuScale)
						Color(255, 0, 0)
						RowText(Format(I_Loc\Mods_UpdateFailed, GetWorkshopErrorCodeStr(UpdateModErrorCode)), x + 20 * Gfx\MenuScale, y + 15 * Gfx\MenuScale, 380 * Gfx\MenuScale, 200 * Gfx\MenuScale)
						If DrawButton(x + 150 * Gfx\MenuScale, y + 150 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Menu_Ok, False) Then
							UpdateModErrorCode = 0
						EndIf
					Else If ModUIState = 1 Then
						DrawFrame(x, y, 420 * Gfx\MenuScale, 285 * Gfx\MenuScale)
						RowText(I_Loc\Mods_UploadConfirm, x + 20 * Gfx\MenuScale, y + 15 * Gfx\MenuScale, 380 * Gfx\MenuScale, 200 * Gfx\MenuScale)
						If DrawButton(x + 22.5 * Gfx\MenuScale, y + 100 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Menu_Yes, False) Then
							WriteTagsToMod(SelectedMod)
							SetSteamTags()
							UploadMod(SelectedMod)
							ModUIState = 0
							SelectedMod = Null
						EndIf
						If DrawButton(x + 150 * Gfx\MenuScale, y + 100 * Gfx\MenuScale, 125 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Mods_Viewterms, False) Then
							ExecFile("https://steamcommunity.com/sharedfiles/workshoplegalagreement")
						EndIf
						If DrawButton(x + (320 - 22.5) * Gfx\MenuScale, y + 100 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Menu_No, False) Then
							WriteTagsToMod(SelectedMod)
							ModUIState = 0
							SelectedMod = Null
						EndIf
						DrawTagSelection(x + 10 * Gfx\MenuScale, y + 160 * Gfx\MenuScale, 380 * Gfx\MenuScale)
					Else If ModUIState = 2 Then
						DrawFrame(x, y, 420 * Gfx\MenuScale, 335 * Gfx\MenuScale)
						RowText(I_Loc\Mods_UpdateConfirm, x + 20 * Gfx\MenuScale, y + 15 * Gfx\MenuScale, 400 * Gfx\MenuScale, 200 * Gfx\MenuScale)
						ModChangelog = InputBox(x + 20 * Gfx\MenuScale, y + 80 * Gfx\MenuScale, 380 * Gfx\MenuScale, 30 * Gfx\MenuScale, ModChangelog, 99)
						Text(x + 20 * Gfx\MenuScale, y + 125 * Gfx\MenuScale, I_Loc\Mods_Keepdesc)
						ShouldKeepModDescription = DrawTick(x + 325 * Gfx\MenuScale, y + 121 * Gfx\MenuScale, ShouldKeepModDescription)
						If DrawButton(x + 50 * Gfx\MenuScale, y + 155 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Menu_Yes, False) Then
							WriteTagsToMod(SelectedMod)
							SetSteamTags()
							UpdateMod(SelectedMod, ModChangelog)
							ModChangelog = ""
							ModUIState = 0
							SelectedMod = Null
						EndIf
						If DrawButton(x + 250 * Gfx\MenuScale, y + 155 * Gfx\MenuScale, 100 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Menu_No, False) Then
							WriteTagsToMod(SelectedMod)
							ModChangelog = ""
							ModUIState = 0
							SelectedMod = Null
						EndIf
						DrawTagSelection(x + 10 * Gfx\MenuScale, y + 210 * Gfx\MenuScale, 400 * Gfx\MenuScale)
					Else
						If DrawButton(x + 10 * Gfx\MenuScale, y, 150 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Mods_Reloadmods, False, False, UpdatingMod<>Null) Then
							SerializeMods()
							ReloadMods()
						EndIf

						If DrawButton(x + 10 * Gfx\MenuScale, y + 40 * Gfx\MenuScale, 150 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\Mods_Reloadgame, False, False, UpdatingMod<>Null) Then
							SerializeMods()
							;; ToDo:: Implement a more robust restarting logic to handle reloading all/some mods
							Return
						EndIf

						If DrawButton(x + 10 * Gfx\MenuScale, y + 80 * Gfx\MenuScale, 150 * Gfx\MenuScale, 50 * Gfx\MenuScale, "", False, False, UpdatingMod<>Null) Then
							ExecFile("Mods")
						EndIf
						RowText(I_Loc\Mods_Openlocal, x + 10 * Gfx\MenuScale, y + (80 + 10) * Gfx\MenuScale, 150 * Gfx\MenuScale, (50 - 15) * Gfx\MenuScale, True)
					EndIf
				EndIf

		End Select
		
	End If
	
	If SpeedRunMode And (Not TimerStopped) Then
		DrawTimer()
		If MainMenuOpen Then
			If DrawButton(Config\Graphics\ScreenWidth - 150 * Gfx\MenuScale - 24, 60 * Gfx\MenuScale + 24, 150 * Gfx\MenuScale, 30 * Gfx\MenuScale, I_Loc\HUD_SpeedrunStoptimer, False) Then
				TimerStopped = True
			EndIf
		EndIf
	EndIf

	Color 255,255,255
	SetFont GameFonts\Console
	Text 20,Config\Graphics\ScreenHeight-30,"v"+VersionNumber
	
	;DrawTiledImageRect(MenuBack, 985 * Gfx\MenuScale, 860 * Gfx\MenuScale, 200 * Gfx\MenuScale, 20 * Gfx\MenuScale, 1200 * Gfx\MenuScale, 866 * Gfx\MenuScale, 300, 20 * Gfx\MenuScale)
	
	If Config\Graphics\Fullscreen Then DrawImage Resource_GetTexture(TEX_CURSOR), ScaledMouseX(),ScaledMouseY()
	
	SetFont Font1
End Function

Const TAG_COUNT = 8
Global TagActive%[TAG_COUNT]
Global TagSteamName$[TAG_COUNT]
TagSteamName[0] = "Textures"
TagSteamName[1] = "Sounds"
TagSteamName[2] = "Models"
TagSteamName[3] = "Rooms"
TagSteamName[4] = "Items"
TagSteamName[5] = "SCP-294 Drinks"
TagSteamName[6] = "Loading Screens"
TagSteamName[7] = "Custom Maps"

Function ReadTagsFromMod(m.Mods)
	For i = 0 To TAG_COUNT-1
		TagActive[i] = False
	Next
	Local tags$ = GetINIString2(m\Path + "info.ini", 1, "tags")
	Local tag$, tagIdx%
	DebugLog(tags)
	Repeat
		tag = Trim(Piece(tags, tagIdx, ","))
		DebugLog(tag)
		tagIdx = tagIdx + 1
		For i = 0 To TAG_COUNT-1
			If tag = TagSteamName[i] Then TagActive[i] = True : Exit
		Next
	Until tag = ""
End Function

Function WriteTagsToMod(m.Mods)
	Local txt$, comma%
	For i = 0 To TAG_COUNT-1
		If TagActive[i] Then
			If comma Then txt = txt + ", "
			txt = txt + TagSteamName[i]
			comma = True
		EndIf
	Next
	PutINIValue(m\Path + "info.ini", "", "tags", txt)
End Function

Function SetSteamTags()
	Steam_ClearItemTags()
	For i = 0 To TAG_COUNT-1
		If TagActive[i] Then Steam_AddItemTag(TagSteamName[i])
	Next
End Function

Function DrawTagSelection(x%, y%, width%)
	Text x + 10 * Gfx\MenuScale, y, I_Loc\Mods_Tags
	y = y + 25 * Gfx\MenuScale

	Local xStep% = (width - 20 * Gfx\MenuScale) / 3
	Local xBegin% = x + width - 40 * Gfx\MenuScale - xStep * 2
	For i = 0 To TAG_COUNT-1
		Local myX% = xBegin + (i Mod 3) * xStep
		Local row% = i / 3
		Local myY% = y + row * 30 * Gfx\MenuScale
		If i = 6 Then myX = x + StringWidth(TagSteamName[i]) + 20 * Gfx\MenuScale
		If i = 7 Then myX = myX + xStep
		Text(myX - StringWidth(I_Loc\Mods_Tag[i+1]) - 5, myY, I_Loc\Mods_Tag[i+1])
		TagActive[i] = DrawTick(myX, myY, TagActive[i])
	Next
End Function

Function CreateGrayScaleImage%(img%)
	Local ret% = CreateImage(ImageWidth(img), ImageHeight(img))
	Local rbuf% = ImageBuffer(img)
	Local buf% = ImageBuffer(ret)
	LockBuffer(rbuf)
	LockBuffer(buf)
	For x = 0 To ImageWidth(img)-1
		For y = 0 To ImageHeight(img)-1
			Local color% = ReadPixelFast(x, y, rbuf)
			Local g% = ((color Shr 16) And 255) * 0.21 + ((color Shr 8) And 255) * 0.72 + (color And 255) * 0.07
			WritePixelFast(x, y, (color And $FF000000) + (g Shl 16) + (g Shl 8) + g, buf)
		Next
	Next
	UnlockBuffer(rbuf)
	UnlockBuffer(buf)
	Return ret
End Function

Function GreatestCommonDivsior(u%, v%)
	If u <= 0 Lor v <= 0 Then Return 1

	Local k% = 0, t% = u Or v, d

	While (t And 1) = 0
		k = k + 1
		t = t Shr 1
	Wend

	v = v Shr k
	u = u Shr k

	If (u And 1) = 0 Then d = (u Shr 1) Else If (v And 1) = 0 Then d = -(v Shr 1) Else d = (u Shr 1) - (v Shr 1)
	While d <> 0
		While (d And 1) = 0 d = d / 2 Wend
		If d > 0 Then u = d Else v = -d
		d = (u Shr 1) - (v Shr 1)
	Wend
	
	Return u Shl k
End Function


Function DrawBar(img%, x%, y%, width%, filled#, centerX% = False)
	Local spacing = ImageWidth(img) + 2
	width = Int(width / spacing) * spacing + 3
	Local height = ImageHeight(img) + 6
	If centerX Then x = x - width / 2
	Color 255, 255, 255
	Rect (x, y, width, height, False)
	For i = 1 To Int(((width - 6) * filled) / spacing)
		DrawImage(img, x + 3 + spacing * (i - 1), y + 3)
	Next
End Function

Function DrawTiledImageRect(img%, srcX%, srcY%, srcwidth#, srcheight#, x%, y%, width%, height%)
	
	Local x2% = x
	While x2 < x+width
		If x2 + srcwidth > x + width Then srcwidth = (x + width) - x2
		Local y2% = y
		While y2 < y+height
			DrawImageRect(img, x2, y2, srcX, srcY, srcwidth, Min((y + height) - y2, srcheight))
			y2 = y2 + srcheight
		Wend
		x2 = x2 + srcwidth
	Wend
	
End Function

Function InputBox$(x%, y%, width%, height%, Txt$, ID% = 0, virtualKeyboardMode=0)
	;TextBox(x,y,width,height,Txt$)
	Color (255, 255, 255)
	DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	Color (0, 0, 0)
	
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		Color(50, 50, 50)
		MouseOnBox = True
		If MouseHit1 Then
			SelectedInputBox = ID
			If Flags\SteamActive And virtualKeyboardMode >= 0 Then Steam_OpenOnScreenKeyboard(virtualKeyboardMode, x, y, width, height)
			FlushKeys
		EndIf
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	Color (255, 255, 255)	
	
	If (Not MouseOnBox) And MouseHit1 And SelectedInputBox = ID Then
		SelectedInputBox = 0
		If Flags\SteamActive And virtualKeyboardMode >= 0 Then Steam_CloseOnScreenKeyboard()
	EndIf
	
	Text(x + width / 2, y + height / 2, Txt, True, True)

	If SelectedInputBox = ID Then
		If (MilliSecs() Mod 800) < 400 Then Rect (x + width / 2 + StringWidth(Txt) / 2 + 2, y + height / 2 - 17 * Gfx\MenuScale / 2, 2, 17 * Gfx\MenuScale)
		If KeyDown(29) And KeyHit(47) Then
			Txt = Txt + GetClipboardContents()
		Else If KeyDown(29) And KeyHit(46) Then
			SetClipboardContents(Txt)
		Else
			Txt = TextInput(Txt)
		EndIf
	EndIf

	Return Txt
End Function

Function DrawFrame(x%, y%, width%, height%, xoffset%=0, yoffset%=0, scrollY%=True)
	Local srcY%
	If scrollY Then srcY = y Mod 256
	Color 255, 255, 255
	DrawTiledImageRect(MenuWhite, xoffset, srcY, 512, 512, x, y, width, height)
	
	DrawTiledImageRect(MenuBlack, yoffset, srcY, 512, 512, x+3*Gfx\MenuScale, y+3*Gfx\MenuScale, width-6*Gfx\MenuScale, height-6*Gfx\MenuScale)	
End Function

Function DrawButton%(x%, y%, width%, height%, txt$, bigfont% = True, waitForMouseUp%=False, disabled%=False)
	Local clicked% = False
	
	DrawFrame (x, y, width, height)
	If (Not disabled) And MouseOn(x, y, width, height) Then
		Color(30, 30, 30)
		If (MouseHit1 And (Not waitForMouseUp)) Or (MouseUp1 And waitForMouseUp) Then 
			clicked = True
			PlaySFX(SFX_INTERACT_BUTTON_1)
		EndIf
		Rect(x + 4, y + 4, width - 8, height - 8)
	EndIf
	
	If disabled Then
		Color(100, 100, 100)
	Else
		Color (255, 255, 255)
	EndIf
	If bigfont Then SetFont Font2 Else SetFont Font1
	Text(x + width / 2, y + height / 2, txt, True, True)
	
	Return clicked
End Function

Function DrawButton2%(x%, y%, width%, height%, txt$, bigfont% = True)
	Local clicked% = False
	
	DrawFrame (x, y, width, height)
	Local hit% = MouseHit(1)
	If MouseOn(x, y, width, height) Then
		Color(30, 30, 30)
		If hit Then clicked = True : PlaySFX(SFX_INTERACT_BUTTON_1)
		Rect(x + 4, y + 4, width - 8, height - 8)	
	Else
		Color(0, 0, 0)
	EndIf
	
	Color (255, 255, 255)
	If bigfont Then SetFont Font2 Else SetFont Font1
	Text(x + width / 2, y + height / 2, txt, True, True)
	
	Return clicked
End Function

Function DrawTick%(x%, y%, selected%, locked% = False)
	Local width% = 20 * Gfx\MenuScale, height% = 20 * Gfx\MenuScale
	
	Color (255, 255, 255)
	DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	
	Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	
	If Highlight Then
		Color(50, 50, 50)
		If MouseHit1 Then selected = (Not selected) : PlaySFX(SFX_INTERACT_BUTTON_1)
	Else
		Color(0, 0, 0)		
	End If
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	
	If selected Then
		If Highlight Then
			Color 255,255,255
		Else
			Color 200,200,200
		EndIf
		DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x + 4, y + 4, width - 8, height - 8)
		;Rect(x + 4, y + 4, width - 8, height - 8)
	EndIf
	
	Color 255, 255, 255
	
	Return selected
End Function

Function SlideBar#(x%, y%, width%, value#, ID%)
	
	If MouseDown1 And OnSliderID=0 Then
		If ScaledMouseX() >= x And ScaledMouseX() <= x + width + 14 And ScaledMouseY() >= y And ScaledMouseY() <= y + 20 Then
			OnSliderID = ID
		EndIf
	EndIf

	If ID = OnSliderID Then
		value = Min(Max((ScaledMouseX() - x) * 100 / width, 0), 100)
	EndIf

	Local height% = ImageHeight(BlinkMeterIMG) + 6

	Color 255,255,255
	Rect(x, y, width + 14, height,False)

	DrawImage(BlinkMeterIMG, x + width * value / 100.0 +3, y+3)
	
	Color 170,170,170 
	Text (x - 20 * Gfx\MenuScale - StringWidth(I_Loc\Option_SliderLow), y + 3*Gfx\MenuScale, I_Loc\Option_SliderLow)					
	Text (x + width + 20 * Gfx\MenuScale + 14, y+3*Gfx\MenuScale, I_Loc\Option_SliderHigh)	
	
	Return value
	
End Function




Function RowText(A$, X, Y, W, H, align% = 0, Leading#=1)
	;Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	
	If H<1 Then H=2048
	
	Local LinesShown = 0
	Local Height = StringHeight(A$) + Leading
	Local b$
	
	While Len(A) > 0
		Local space = Instr(A$, " ")
		If space = 0 Then space = Len(A$) + 1
		Local temp$ = Left(A$, space - 1)
		
		If StringWidth (b$ + temp$) > W Then ;too big, so Print what will fit
			If align Then
				Text(X + W / 2, LinesShown * Height + Y, b, True)
			Else
				Text(X, LinesShown * Height + Y, b)
			EndIf			
			
			LinesShown = LinesShown + 1
			b$=""
		Else ;append it To b$ (which will eventually be printed) And remove it from A$
			If b <> "" Then b = b + " "
			b$ = b$ + temp$
			A$ = Right(A$, Max(0, Len(A$) - Len(temp$) - 1))
		EndIf
		
		If ((LinesShown + 1) * Height) > H Then Exit ;the Next Line would be too tall, so leave
	Wend
	
	If (b$ <> "") And((LinesShown + 1) <= H) Then
		If align Then
			Text(X + W / 2, LinesShown * Height + Y, b, True) ;Print any remaining Text If it'll fit vertically
		Else
			Text(X, LinesShown * Height + Y, b) ;Print any remaining Text If it'll fit vertically
		EndIf
	EndIf
	
End Function

Function GetLineAmount(A$, W, H, Leading#=1)
	;Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	
	If H<1 Then H=2048
	
	Local LinesShown = 0
	Local Height = StringHeight(A$) + Leading
	Local b$

	While Len(A) > 0
		Local space = Instr(A$, " ")
		If space = 0 Then space = Len(A$) + 1
		Local temp$ = Left(A$, space - 1)
		
		If StringWidth (b$ + temp$) > W Then ;too big, so Print what will fit
			LinesShown = LinesShown + 1
			b$=""
		Else ;append it To b$ (which will eventually be printed) And remove it from A$
			If b <> "" Then b = b + " "
			b$ = b$ + temp$
			A$ = Right(A$, Max(0, Len(A$) - Len(temp$) - 1))
		EndIf
		
		If ((LinesShown + 1) * Height) > H Then Exit ;the Next Line would be too tall, so leave
	Wend

	Return LinesShown+1
	
End Function

Function DrawTooltip(message$)
	Local scale# = Config\Graphics\ScreenHeight/768.0
	
	Local width = (StringWidth(message$))+20*Gfx\MenuScale
	
	Color 25,25,25
	Rect(ScaledMouseX()+20,ScaledMouseY(),width,19*scale,True)
	Color 150,150,150
	Rect(ScaledMouseX()+20,ScaledMouseY(),width,19*scale,False)
	SetFont Font1
	Text(ScaledMouseX()+(20*Gfx\MenuScale)+(width/2),ScaledMouseY()+(12*Gfx\MenuScale), message$, True, True)
End Function

Global QuickLoadPercent% = -1
Global QuickLoad_CurrEvent.Events

Function DrawQuickLoading()
	
	If QuickLoadPercent > -1
		If QuickLoadPercent > 99
			QuickLoadPercent = -1
		EndIf
		QuickLoadEvents()
	Else
		QuickLoadPercent = -1
		QuickLoad_CurrEvent = Null
	EndIf
	
End Function

Function DrawOptionsTooltip(x%,y%,width%,height%,option$,value#=0,ingame%=False)
	Local fx# = x+10*Gfx\MenuScale
	Local fy# = y+10*Gfx\MenuScale
	Local fw# = width-20*Gfx\MenuScale
	Local fh# = height-20*Gfx\MenuScale
	Local lines% = 0, lines2% = 0
	Local txt$ = ""
	Local txt2$ = "", R% = 0, G% = 0, B% = 0
	Local extraspace% = 0
	
	SetFont Font1
	Color 255,255,255
	Select Lower(option$)
		;Graphic options
			;[Block]
		Case "vsync"
			txt = I_Loc\OptionTooltip_Vsync
		Case "antialias"
			txt = I_Loc\OptionTooltip_Antialias
			txt2 = I_Loc\Option_HintFullscreen
			R = 255
		Case "gamma"
			txt = I_Loc\OptionTooltip_Gamma
			R = 255
			G = 255
			B = 255
			txt2 = Format(I_Loc\Option_HintDefault, "%", Str(Int(value*100)), "100")
		Case "texquality"
			txt = I_Loc\OptionTooltip_Texlod
		Case "vram"
			txt = I_Loc\OptionTooltip_Vram
			txt2 = I_Loc\Option_HintMenuonly
			R = 255
		Case "hudoffset"
			txt = I_Loc\OptionTooltip_Hudoffset
			R = 255
			G = 255
			B = 255
			txt2 = Format(I_Loc\Option_HintDefault, "%", Str(Int(Config\Graphics\HUDOffset*100)), "0")
		Case "fov"
			txt = I_Loc\OptionTooltip_Fov
			R = 255
			G = 255
			B = 255
			txt2 = Format(I_Loc\Option_HintDefault, "°", Str(Config\Graphics\FOV), Str(Config\Graphics\DefaultFOV))
			;[End Block]
		;Sound options
			;[Block]
		Case "musicvol"
			txt = I_Loc\OptionTooltip_Musicvol
			R = 255
			G = 255
			B = 255
			txt2 = Format(I_Loc\Option_HintDefault, "%", Str(Int(value*100)), "50")
		Case "soundvol"
			txt = I_Loc\OptionTooltip_Soundvol
			R = 255
			G = 255
			B = 255
			txt2 = Format(I_Loc\Option_HintDefault, "%", Str(Int(value*100)), "100")
		Case "sfxautorelease"
			txt = I_Loc\OptionTooltip_Sfxautorelease
			R = 255
			txt2 = I_Loc\Option_HintMenuonly
		Case "usertrack"
			txt = I_Loc\OptionTooltip_Usertrack
			R = 255
			txt2 = I_Loc\Option_HintMenuonly
		Case "usertrackmode"
			txt = I_Loc\OptionTooltip_Usertrackmode
			R = 255
			G = 255
			txt2 = I_Loc\OptionTooltip_UsertrackmodeNote
		Case "usertrackscan"
			txt = I_Loc\OptionTooltip_Usertrackscan
			;[End Block]
		;Control options	
			;[Block]
		Case "mousesensitivity"
			txt = I_Loc\OptionTooltip_Mousesensitivty
			R = 255
			G = 255
			B = 255
			txt2 = Format(I_Loc\Option_HintDefault, "%", Str(Int((0.5+value)*100)), "50")
		Case "mouseinvert"
			txt = Format(I_Loc\Option_HintSelfexplanatory, I_Loc\OptionName_Mouseinvert)
		Case "mousesmoothing"
			txt = I_Loc\OptionTooltip_Mousesmoothing
			R = 255
			G = 255
			B = 255
			txt2 = Format(I_Loc\Option_HintDefault, "%", Str(Int(value*100)), "100")
		Case "controls"
			txt = I_Loc\OptionTooltip_Binds
			;[End Block]
		;Advanced options	
			;[Block]
		Case "hud"
			txt = I_Loc\OptionTooltip_Showhud
		Case "consoleenable"
			txt = Format(I_Loc\OptionTooltip_Console, KeyName(KEY_CONSOLE))
			R = 255
			txt2 = I_Loc\OptionTooltip_ConsoleNote
		Case "speedrunmode"
			txt = I_Loc\OptionTooltip_Speedrunmode
		Case "numericseeds"
			txt = I_Loc\OptionTooltip_Numericseeds
		Case "achpopup"
			txt = I_Loc\OptionTooltip_Achpopup
		Case "launcher"
			txt = I_Loc\OptionTooltip_Launcher
		Case "showfps"
			txt = I_Loc\OptionTooltip_Showfps
		Case "framelimit"
			txt = I_Loc\OptionTooltip_Framelimit
			If value > 0 And value < 60
				R = 255
				G = 255
				txt2 = I_Loc\OptionTooltip_FramelimitNote
			EndIf
			;[End Block]
	End Select
	
	lines% = GetLineAmount(txt,fw,fh)
	If txt2$ = ""
		DrawFrame(x,y,width,((StringHeight(txt)*lines)+(15+lines)*Gfx\MenuScale)+extraspace)
	Else
		lines2% = GetLineAmount(txt2,fw,fh)
		DrawFrame(x,y,width,(((StringHeight(txt)*lines)+(15+lines)*Gfx\MenuScale)+(StringHeight(txt2)*lines2)+(10+lines2)*Gfx\MenuScale)+extraspace)
	EndIf
	RowText(txt,fx,fy,fw,fh)
	If txt2$ <> ""
		Color R,G,B
		RowText(txt2,fx,(fy+(StringHeight(txt)*lines)+(5+lines)*Gfx\MenuScale),fw,fh)
	EndIf
End Function

Function DrawFramedRowText(txt$, x%, y%, width%)
	Local fw% = width - 12*Gfx\MenuScale
	Local lines% = GetLineAmount(txt, fw, 0)
	DrawFrame(x, y, width, ((StringHeight(txt)*lines)+(10+lines)*Gfx\MenuScale), 0, 0, False)
	RowText(txt, x + 6*Gfx\MenuScale, y + 6*Gfx\MenuScale, fw, 0)
End Function

Function DrawMapCreatorTooltip(x%,y%,width%,height%,mapname$)
	Local fx# = x+20*Gfx\MenuScale
	Local fy# = y+20*Gfx\MenuScale
	Local fw# = width-40*Gfx\MenuScale
	Local fh# = height-40*Gfx\MenuScale
	Local lines% = 0
	
	SetFont Font1
	Color 255,255,255
	
	Local txt$[6]
	txt[0] = File_GetFileName(mapname)
	If Right(mapname,6)="cbmap2" Then
		Local f% = OpenFile(mapname$)
		
		Local author$ = ReadLine(f)
		Local descr$ = ReadLine(f)
		ReadByte(f)
		ReadByte(f)
		Local ramount% = ReadInt(f)
		If ReadInt(f) > 0 Then
			Local hasForest% = True
		Else
			hasForest% = False
		EndIf
		If ReadInt(f) > 0 Then
			Local hasMT% = True
		Else
			hasMT% = False
		EndIf
		
		CloseFile f%
	Else
		author$ = I_Loc\LoadMap_Unknown
		descr$ = I_Loc\LoadMap_Nodesc
		ramount% = 0
		hasForest% = False
		hasMT% = False
	EndIf
	txt[1] = Format(I_Loc\LoadMap_Madeby, author)
	txt[2] = Format(I_Loc\LoadMap_Desc, descr)
	If ramount > 0 Then
		txt[3] = Format(I_Loc\LoadMap_Roomcount, ramount)
	Else
		txt[3] = Format(I_Loc\LoadMap_Madeby, I_Loc\LoadMap_Unknown)
	EndIf
	If hasForest Then
		txt[4] = Format(I_Loc\LoadMap_Customforest, I_Loc\Menu_Yes)
	Else
		txt[4] = Format(I_Loc\LoadMap_Customforest, I_Loc\Menu_No)
	EndIf
	If hasMT Then
		txt[5] = Format(I_Loc\LoadMap_Custommaintenance, I_Loc\Menu_Yes)
	Else
		txt[5] = Format(I_Loc\LoadMap_Custommaintenance, I_Loc\Menu_No)
	EndIf
	
	lines% = GetLineAmount(txt[2],fw,fh)
	DrawFrame(x,y,width,(StringHeight(txt[0])*6)+StringHeight(txt[2])*lines+25*Gfx\MenuScale)
	
	Color 255,255,255
	Text(fx,fy,txt[0])
	Text(fx,fy+StringHeight(txt[0]),txt[1])
	RowText(txt[2],fx,fy+(StringHeight(txt[0])*2),fw,height)
	Text(fx,fy+((StringHeight(txt[0])*2)+StringHeight(txt[2])*lines+5*Gfx\MenuScale),txt[3])
	Text(fx,fy+((StringHeight(txt[0])*3)+StringHeight(txt[2])*lines+5*Gfx\MenuScale),txt[4])
	Text(fx,fy+((StringHeight(txt[0])*4)+StringHeight(txt[2])*lines+5*Gfx\MenuScale),txt[5])
	
End Function

Global OnSliderID% = 0

Function Slider3(x%,y%,width%,value%,ID%,val1$,val2$,val3$)
	
	If MouseDown1 And OnSliderID = 0 Then
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			OnSliderID = ID
		EndIf
	EndIf
	
	Color 200,200,200
	Rect(x,y,width+14,10,True)
	Rect(x,y-8,4,14,True)
	Rect(x+(width/2)+5,y-8,4,14,True)
	Rect(x+width+10,y-8,4,14,True)
	
	If ID = OnSliderID
		If (ScaledMouseX() <= x+8)
			value = 0
		ElseIf (ScaledMouseX() >= x+width/2) And (ScaledMouseX() <= x+(width/2)+8)
			value = 1
		ElseIf (ScaledMouseX() >= x+width)
			value = 2
		EndIf
		Color 0,255,0
		Rect(x,y,width+14,10,True)
	Else
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			Color 0,200,0
			Rect(x,y,width+14,10,False)
		EndIf
	EndIf
	
	If value = 0
		DrawImage(BlinkMeterIMG,x,y-8)
	ElseIf value = 1
		DrawImage(BlinkMeterIMG,x+(width/2)+3,y-8)
	Else
		DrawImage(BlinkMeterIMG,x+width+6,y-8)
	EndIf
	
	Color 170,170,170
	If value = 0
		Text(x+2,y+10+Gfx\MenuScale,val1,True)
	ElseIf value = 1
		Text(x+(width/2)+7,y+10+Gfx\MenuScale,val2,True)
	Else
		Text(x+width+12,y+10+Gfx\MenuScale,val3,True)
	EndIf
	
	Return value
	
End Function

Function Slider4(x%,y%,width%,value%,ID%,val1$,val2$,val3$,val4$)
	
	If MouseDown1 And OnSliderID = 0 Then
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			OnSliderID = ID
		EndIf
	EndIf
	
	Color 200,200,200
	Rect(x,y,width+14,10,True)
	Rect(x,y-8,4,14,True) ;1
	Rect(x+(width*(1.0/3.0))+(10.0/3.0),y-8,4,14,True) ;2
	Rect(x+(width*(2.0/3.0))+(20.0/3.0),y-8,4,14,True) ;3
	Rect(x+width+10,y-8,4,14,True) ;4
	
	If ID = OnSliderID
		If (ScaledMouseX() <= x+8)
			value = 0
		ElseIf (ScaledMouseX() >= x+width*(1.0/3.0)) And (ScaledMouseX() <= x+width*(1.0/3.0)+8)
			value = 1
		ElseIf (ScaledMouseX() >= x+width*(2.0/3.0)) And (ScaledMouseX() <= x+width*(2.0/3.0)+8)
			value = 2
		ElseIf (ScaledMouseX() >= x+width)
			value = 3
		EndIf
		Color 0,255,0
		Rect(x,y,width+14,10,True)
	Else
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			Color 0,200,0
			Rect(x,y,width+14,10,False)
		EndIf
	EndIf
	
	If value = 0
		DrawImage(BlinkMeterIMG,x,y-8)
	ElseIf value = 1
		DrawImage(BlinkMeterIMG,x+width*(1.0/3.0)+2,y-8)
	ElseIf value = 2
		DrawImage(BlinkMeterIMG,x+width*(2.0/3.0)+4,y-8)
	Else
		DrawImage(BlinkMeterIMG,x+width+6,y-8)
	EndIf
	
	Color 170,170,170
	If value = 0
		Text(x+2,y+10+Gfx\MenuScale,val1,True)
	ElseIf value = 1
		Text(x+width*(1.0/3.0)+2+(10.0/3.0),y+10+Gfx\MenuScale,val2,True)
	ElseIf value = 2
		Text(x+width*(2.0/3.0)+2+((10.0/3.0)*2),y+10+Gfx\MenuScale,val3,True)
	Else
		Text(x+width+12,y+10+Gfx\MenuScale,val4,True)
	EndIf
	
	Return value
	
End Function

Function Slider5(x%,y%,width%,value%,ID%,val1$,val2$,val3$,val4$,val5$)
	
	If MouseDown1 And OnSliderID = 0 Then
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			OnSliderID = ID
		EndIf
	EndIf
	
	Color 200,200,200
	Rect(x,y,width+14,10,True)
	Rect(x,y-8,4,14,True) ;1
	Rect(x+(width/4)+2.5,y-8,4,14,True) ;2
	Rect(x+(width/2)+5,y-8,4,14,True) ;3
	Rect(x+(width*0.75)+7.5,y-8,4,14,True) ;4
	Rect(x+width+10,y-8,4,14,True) ;5
	
	If ID = OnSliderID
		If (ScaledMouseX() <= x+8)
			value = 0
		ElseIf (ScaledMouseX() >= x+width/4) And (ScaledMouseX() <= x+(width/4)+8)
			value = 1
		ElseIf (ScaledMouseX() >= x+width/2) And (ScaledMouseX() <= x+(width/2)+8)
			value = 2
		ElseIf (ScaledMouseX() >= x+width*0.75) And (ScaledMouseX() <= x+(width*0.75)+8)
			value = 3
		ElseIf (ScaledMouseX() >= x+width)
			value = 4
		EndIf
		Color 0,255,0
		Rect(x,y,width+14,10,True)
	Else
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			Color 0,200,0
			Rect(x,y,width+14,10,False)
		EndIf
	EndIf
	
	If value = 0
		DrawImage(BlinkMeterIMG,x,y-8)
	ElseIf value = 1
		DrawImage(BlinkMeterIMG,x+(width/4)+1.5,y-8)
	ElseIf value = 2
		DrawImage(BlinkMeterIMG,x+(width/2)+3,y-8)
	ElseIf value = 3
		DrawImage(BlinkMeterIMG,x+(width*0.75)+4.5,y-8)
	Else
		DrawImage(BlinkMeterIMG,x+width+6,y-8)
	EndIf
	
	Color 170,170,170
	If value = 0
		Text(x+2,y+10+Gfx\MenuScale,val1,True)
	ElseIf value = 1
		Text(x+(width/4)+4.5,y+10+Gfx\MenuScale,val2,True)
	ElseIf value = 2
		Text(x+(width/2)+7,y+10+Gfx\MenuScale,val3,True)
	ElseIf value = 3
		Text(x+(width*0.75)+9.5,y+10+Gfx\MenuScale,val4,True)
	Else
		Text(x+width+12,y+10+Gfx\MenuScale,val5,True)
	EndIf
	
	Return value
	
End Function

Function Slider7(x%,y%,width%,value%,ID%,val1$,val2$,val3$,val4$,val5$,val6$,val7$)
	
	If MouseDown1 And OnSliderID = 0 Then
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			OnSliderID = ID
		EndIf
	EndIf
	
	Color 200,200,200
	Rect(x,y,width+14,10,True)
	Rect(x,y-8,4,14,True) ;1
	Rect(x+(width*(1.0/6.0))+(10.0/6.0),y-8,4,14,True) ;2
	Rect(x+(width*(2.0/6.0))+(20.0/6.0),y-8,4,14,True) ;3
	Rect(x+(width*(3.0/6.0))+(30.0/6.0),y-8,4,14,True) ;4
	Rect(x+(width*(4.0/6.0))+(40.0/6.0),y-8,4,14,True) ;5
	Rect(x+(width*(5.0/6.0))+(50.0/6.0),y-8,4,14,True) ;6
	Rect(x+width+10,y-8,4,14,True) ;7
	
	If ID = OnSliderID
		If (ScaledMouseX() <= x+8)
			value = 0
		ElseIf (ScaledMouseX() >= x+(width*(1.0/6.0))) And (ScaledMouseX() <= x+(width*(1.0/6.0))+8)
			value = 1
		ElseIf (ScaledMouseX() >= x+(width*(2.0/6.0))) And (ScaledMouseX() <= x+(width*(2.0/6.0))+8)
			value = 2
		ElseIf (ScaledMouseX() >= x+(width*(3.0/6.0))) And (ScaledMouseX() <= x+(width*(3.0/6.0))+8)
			value = 3
		ElseIf (ScaledMouseX() >= x+(width*(4.0/6.0))) And (ScaledMouseX() <= x+(width*(4.0/6.0))+8)
			value = 4
		ElseIf (ScaledMouseX() >= x+(width*(5.0/6.0))) And (ScaledMouseX() <= x+(width*(5.0/6.0))+8)
			value = 5
		ElseIf (ScaledMouseX() >= x+width)
			value = 6
		EndIf
		Color 0,255,0
		Rect(x,y,width+14,10,True)
	Else
		If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
			Color 0,200,0
			Rect(x,y,width+14,10,False)
		EndIf
	EndIf
	
	If value = 0
		DrawImage(BlinkMeterIMG,x,y-8)
	ElseIf value = 1
		DrawImage(BlinkMeterIMG,x+(width*(1.0/6.0))+1,y-8)
	ElseIf value = 2
		DrawImage(BlinkMeterIMG,x+(width*(2.0/6.0))+2,y-8)
	ElseIf value = 3
		DrawImage(BlinkMeterIMG,x+(width*(3.0/6.0))+3,y-8)
	ElseIf value = 4
		DrawImage(BlinkMeterIMG,x+(width*(4.0/6.0))+4,y-8)
	ElseIf value = 5
		DrawImage(BlinkMeterIMG,x+(width*(5.0/6.0))+5,y-8)
	Else
		DrawImage(BlinkMeterIMG,x+width+6,y-8)
	EndIf
	
	Color 170,170,170
	If value = 0
		Text(x+2,y+10+Gfx\MenuScale,val1,True)
	ElseIf value = 1
		Text(x+(width*(1.0/6.0))+2+(10.0/6.0),y+10+Gfx\MenuScale,val2,True)
	ElseIf value = 2
		Text(x+(width*(2.0/6.0))+2+((10.0/6.0)*2),y+10+Gfx\MenuScale,val3,True)
	ElseIf value = 3
		Text(x+(width*(3.0/6.0))+2+((10.0/6.0)*3),y+10+Gfx\MenuScale,val4,True)
	ElseIf value = 4
		Text(x+(width*(4.0/6.0))+2+((10.0/6.0)*4),y+10+Gfx\MenuScale,val5,True)
	ElseIf value = 5
		Text(x+(width*(5.0/6.0))+2+((10.0/6.0)*5),y+10+Gfx\MenuScale,val6,True)
	Else
		Text(x+width+12,y+10+Gfx\MenuScale,val7,True)
	EndIf
	
	Return value
	
End Function

Global OnBar%
Global ScrollBarY# = 0.0
Global ScrollMenuHeight# = 0.0

Function DrawScrollBar#(x, y, width, height, barx, bary, barwidth, barheight, bar#, dir = 0)
	;0 = vaakasuuntainen, 1 = pystysuuntainen
	
	Local MouseSpeedX = MouseXSpeed()
	Local MouseSpeedY = MouseYSpeed()
	
	Color(0, 0, 0)
	;Rect(x, y, width, height)
	Button(barx, bary, barwidth, barheight, "")
	
	If dir = 0 Then ;vaakasuunnassa
		If height > 10 Then
			Color 250,250,250
			Rect(barx + barwidth / 2, bary + 5*Gfx\MenuScale, 2*Gfx\MenuScale, barheight - 10)
			Rect(barx + barwidth / 2 - 3*Gfx\MenuScale, bary + 5*Gfx\MenuScale, 2*Gfx\MenuScale, barheight - 10)
			Rect(barx + barwidth / 2 + 3*Gfx\MenuScale, bary + 5*Gfx\MenuScale, 2*Gfx\MenuScale, barheight - 10)
		EndIf
	Else ;pystysuunnassa
		If width > 10 Then
			Color 250,250,250
			Rect(barx + 4*Gfx\MenuScale, bary + barheight / 2, barwidth - 10*Gfx\MenuScale, 2*Gfx\MenuScale)
			Rect(barx + 4*Gfx\MenuScale, bary + barheight / 2 - 3*Gfx\MenuScale, barwidth - 10*Gfx\MenuScale, 2*Gfx\MenuScale)
			Rect(barx + 4*Gfx\MenuScale, bary + barheight / 2 + 3*Gfx\MenuScale, barwidth - 10*Gfx\MenuScale, 2*Gfx\MenuScale)
		EndIf
	EndIf
	
	If MouseX()>barx And MouseX()<barx+barwidth
		If MouseY()>bary And MouseY()<bary+barheight
			OnBar = True
		Else
			If (Not MouseDown1)
				OnBar = False
			EndIf
		EndIf
	Else
		If (Not MouseDown1)
			OnBar = False
		EndIf
	EndIf
	
	If MouseDown1
		If OnBar
			If dir = 0
				Return Min(Max(bar + MouseSpeedX / Float(width - barwidth), 0), 1)
			Else
				Return Min(Max(bar + MouseSpeedY / Float(height - barheight), 0), 1)
			EndIf
		EndIf
	EndIf
	
	Return bar
	
End Function

Function Button%(x,y,width,height,txt$, disabled%=False)
	Local Pushed = False
	
	Color 50, 50, 50
	If Not disabled Then 
		If MouseX() > x And MouseX() < x+width Then
			If MouseY() > y And MouseY() < y+height Then
				If MouseDown1 Then
					Pushed = True
					Color 50*0.6, 50*0.6, 50*0.6
				Else
					Color Min(50*1.2,255),Min(50*1.2,255),Min(50*1.2,255)
				EndIf
			EndIf
		EndIf
	EndIf
	
	If Pushed Then 
		Rect x,y,width,height
		Color 133,130,125
		Rect x+1*Gfx\MenuScale,y+1*Gfx\MenuScale,width-1*Gfx\MenuScale,height-1*Gfx\MenuScale,False	
		Color 10,10,10
		Rect x,y,width,height,False
		Color 250,250,250
		Line x,y+height-1*Gfx\MenuScale,x+width-1*Gfx\MenuScale,y+height-1*Gfx\MenuScale
		Line x+width-1*Gfx\MenuScale,y,x+width-1*Gfx\MenuScale,y+height-1*Gfx\MenuScale
	Else
		Rect x,y,width,height
		Color 133,130,125
		Rect x,y,width-1*Gfx\MenuScale,height-1*Gfx\MenuScale,False	
		Color 250,250,250
		Rect x,y,width,height,False
		Color 10,10,10
		Line x,y+height-1,x+width-1,y+height-1
		Line x+width-1,y,x+width-1,y+height-1		
	EndIf
	
	Color 255,255,255
	If disabled Then Color 70,70,70
	Text x+width/2, y+height/2-1*Gfx\MenuScale, txt, True, True
	
	Color 0,0,0
	
	If Pushed And MouseHit1 Then PlaySFX(SFX_INTERACT_BUTTON_1) : Return True
End Function






;~IDEal Editor Parameters:
;~F#33#499#4AB#4B5#4E8#5C3#5D6#5F3#5FA#615#629#64A#662#693#6C4#6EA#710#72D#73E#756
;~F#764#787#79F#7A8#7D9#7ED#821#867#8A9
;~C#Blitz3D