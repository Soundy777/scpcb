; ===========================================================================
; SCP CB Launcher
; ===========================================================================
; A standalone app to config the game before launching it
; ===========================================================================
;; ToDos
;; Migrate over all GFX, SFX & Fonts into this folder structure
;; Bring in the code dependencies (copy them) strictloads.bb && iniparser.bb
;; The launcher is also dependant upon the Localization system (because ofc it is) so work that shit out when we come to it too
;; Setup a dedicated launcher_options.ini to hold any launcher specific config such as width/height
;; Create a UI_Utils.bb which goes about drawing the button (copy it from menu.bb)
;; Develop a proper app init, update_loop & shutdown procedure
;; Within init, pull the config from the games actual option.ini
;; Within shutdown save the adjusted config back to options.ini
;; rig it up to actually launch the game (provided that option is selected)
; ===========================================================================

; Origonal if which determined if the launcher should launch
; If (Config\Launcher\LauncherEnabled Lor HasCLIFlag("launcher")) And (Not IsRestart) And (Not HasCLIFlag("nolauncher")) Lor Config\Graphics\Fullscreen And (Not GfxMode3DExists(Config\Graphics\ScreenWidth, Config\Graphics\ScreenHeight, 32-16*Config\Graphics\Bit16Mode)) Then

Dim GfxDrivers$(0)
Dim ArrowIMG(4)
Dim AspectRatioWidths%(0), AspectRatioHeights%(0)
Dim GfxModeCountPerAspectRatio%(0)
Dim GfxModeWidthsByAspectRatio%(0, 0), GfxModeHeightsByAspectRatio%(0, 0)

Function UpdateLauncher()
	AspectRatioRatio = 1.0

	MenuScale = 1
	
	Graphics3DExt(Config\Launcher\LauncherWidth, Config\Launcher\LauncherHeight, 0, 2)

	;InitExt
	
	SetBuffer BackBuffer()
	
	RealGraphicWidth = Config\Graphics\ScreenWidth
	RealGraphicHeight = Config\Graphics\ScreenHeight

	Local TotalGfxModes% = CountGfxModes3D()

	Local selectedGdc% = GreatestCommonDivsior(Config\Graphics\ScreenWidth, Config\Graphics\ScreenHeight)
	Local SelectedAspectRatioWidth% = Config\Graphics\ScreenWidth / selectedGdc, SelectedAspectRatioHeight% = Config\Graphics\ScreenHeight / selectedGdc
	
	Local SelectedGfxMode% = -1, AspectRatioCount%
	Local SelectedAspectRatio% = -1
	Local nativeGdc% = GreatestCommonDivsior(DesktopWidth(), DesktopHeight())
	Local NativeAspectRatioWidth = DesktopWidth() / nativeGdc : NativeAspectRatioHeight = DesktopHeight() / nativeGdc
	Local nativeAspectRatio%, nativeGfxMode

	Dim AspectRatioWidths%(TotalGfxModes), AspectRatioHeights%(TotalGfxModes)
	Dim GfxModeCountPerAspectRatio%(TotalGfxModes)
	Dim GfxModeWidthsByAspectRatio%(TotalGfxModes, TotalGfxModes), GfxModeHeightsByAspectRatio%(TotalGfxModes, TotalGfxModes)

	Font1 = LoadFont_Strict("GFX\font\cour\Courier New.ttf", 18)
	SetFont Font1
	MenuWhite = LoadImage_Strict("GFX\menu\menuwhite.jpg")
	MenuBlack = LoadImage_Strict("GFX\menu\menublack.jpg")	
	MaskImage MenuBlack, 255,255,0
	Local LauncherIMG% = LoadImage_Strict("GFX\menu\launcher.png")
	Local i%	
	
	For i = 0 To 3
		ArrowIMG(i) = LoadImage_Strict("GFX\menu\arrow.png")
		RotateImage(ArrowIMG(i), 90 * i)
		HandleImage(ArrowIMG(i), 0, 0)
	Next
	
	For i% = 1 To TotalGfxModes
		Local w% = GfxModeWidth(i), h% = GfxModeHeight(i)
		Local gdc% = GreatestCommonDivsior(w, h)
		Local aw% = w / gdc, ah% = h / gdc
		If (aw < 50 And ah < 50) Lor (aw = NativeAspectRatioWidth And ah = NativeAspectRatioHeight) Lor (aw = SelectedAspectRatioWidth And ah = SelectedAspectRatioHeight) Then
			Local ai% = -1
			For n% = 0 To AspectRatioCount - 1
				If AspectRatioWidths(n) = aw And AspectRatioHeights(n) = ah Then ai = n : Exit
			Next
			If ai = -1 Then
				ai = AspectRatioCount
				AspectRatioWidths(ai) = aw : AspectRatioHeights(ai) = ah
				AspectRatioCount = AspectRatioCount + 1
			EndIf
			Local sameFound% = False
			Local lai% = GfxModeCountPerAspectRatio(ai)
			For n = 0 To lai-1
				If GfxModeWidthsByAspectRatio(ai, n) = w And GfxModeHeightsByAspectRatio(ai, n) = h Then sameFound = True : Exit
			Next
			If Not sameFound
				GfxModeWidthsByAspectRatio(ai, lai) = w : GfxModeHeightsByAspectRatio(ai, lai) = h
				GfxModeCountPerAspectRatio(ai) = GfxModeCountPerAspectRatio(ai) + 1

				If Config\Graphics\ScreenWidth = w And Config\Graphics\ScreenHeight = h Then SelectedGfxMode = lai : SelectedAspectRatio = ai
				If DesktopWidth() = w And DesktopHeight() = h Then nativeGfxMode = lai : nativeAspectRatio = ai
			EndIf
		EndIf
	Next

	If SelectedGfxMode = -1 Then SelectedGfxMode = nativeGfxMode : SelectedAspectRatio = nativeAspectRatio

	Local gfxDriverCount = CountGfxDrivers()
	Dim GfxDrivers$(gfxDriverCount + 1)
	For i = 1 To gfxDriverCount
		GfxDrivers(i) = GfxDriverName(i)
	Next
	
	BlinkMeterIMG% = LoadImage_Strict("GFX\blinkmeter.jpg")
	
	AppTitle "SCP - Containment Breach Launcher"

	Local quit% = False

	Local height% = 18
	
	Repeat
		;Cls
		Color 0,0,0
		Rect 0,0,Config\Launcher\LauncherWidth,Config\Launcher\LauncherHeight,True
		
		MouseHit1 = MouseHit(1)
		MouseDown1 = MouseDown(1)
		If Not MouseDown1 Then OnSliderID = 0
		
		Color 255, 255, 255
		DrawImage(LauncherIMG, 0, 0)
		
		Local x% = 20
		Local y% = 240 - 65

		Text(x, y, I_Loc\Launcher_Resolution)

		x = x + 130
		y = y - 5

		Color 255, 255, 255
		Rect(x, y, 400, 20)
		For i = 0 To (AspectRatioCount - 1)
			Color 0, 0, 0
			Local txt$ = Str(AspectRatioWidths(i)) + ":" + Str(AspectRatioHeights(i))
			Local txtW% = StringWidth(txt)
			Text(x + 5, y + 5, txt)
			Local temp% = False
			If SelectedAspectRatio = i Then temp = True
			If MouseOn(x + 1, y + 1, txtW + 8, height) Then
				Color 100, 100, 100
				temp = True
				If MouseHit1 Then SelectedAspectRatio = i : SelectedGfxMode = Min(GfxModeCountPerAspectRatio(i) - 1, SelectedGfxMode)
			EndIf
			If temp Then Rect(x + 1, y + 1, txtW + 8, height, False)
			If nativeAspectRatio = i Then
				Color 0, 255, 0
				Rect(x + 0, y + 0, txtW + 10, height + 2, False)
			EndIf
			x = x + txtW + 15
		Next

		x% = 40
		y% = 270 - 65

		For i = 0 To (GfxModeCountPerAspectRatio(SelectedAspectRatio) - 1)
			Color 0, 0, 0

			Local gfxWidth% = GfxModeWidthsByAspectRatio(SelectedAspectRatio, i), gfxHeight% = GfxModeHeightsByAspectRatio(SelectedAspectRatio, i)
			txt$ = gfxWidth + "x" + gfxHeight
			txtW% = StringWidth(txt)

			If SelectedGfxMode = i Then Rect(x - 4, y - 4, txtW + 8, height, False)

			Text(x, y, txt)

			If gfxWidth = DesktopWidth() And gfxHeight = DesktopHeight() Then
				Color 0, 255, 0
				Rect(x - 5, y - 5, txtW + 10, height + 2, False)
			EndIf

			If MouseOn(x - 4, y - 4, txtW + 8, height) Then
				Color 100, 100, 100
				Rect(x - 4, y - 4, txtW + 8, height, False)
				If MouseHit1 Then SelectedGfxMode = i
			EndIf
			
			y=y+20
			If y >= 250 - 65 + (Config\Launcher\LauncherHeight - 80 - 260) Then y = 270 - 65 : x=x+105
		Next
		
		;-----------------------------------------------------------------
		Color 255, 255, 255
		x = 30
		y = 369
		Rect(x - 10, y, 340, 95)
		Text(x - 10, y - 25, I_Loc\Launcher_Graphics)
		
		y=y+10
		For i = 1 To gfxDriverCount
			Color 0, 0, 0
			txt$ = EllipsisLeft(GfxDrivers(i), 30)
			txtW% = StringWidth(txt)
			If Config\Graphics\SelectedGFXDriver = i Then Rect(x - 4, y - 4, txtW + 8, height, False)
			Text(x, y, txt)
			If MouseOn(x - 4, y - 4, txtW + 8, height) Then
				Color 100, 100, 100
				Rect(x - 4, y - 4, txtW + 8, height, False)
				If MouseHit1 Then Config\Graphics\SelectedGFXDriver = i
			EndIf
			
			y=y+20
		Next
		
		Config\Graphics\Fullscreen = DrawTick(40 + 430 - 15, 260 - 55 + 5 - 8, Config\Graphics\Fullscreen)
		If Config\Graphics\Fullscreen Then Config\Graphics\BorderlessWindowed = False
		Config\Graphics\BorderlessWindowed = DrawTick(40 + 430 - 15, 260 - 55 + 35, Config\Graphics\BorderlessWindowed)
		If Config\Graphics\BorderlessWindowed Then Config\Graphics\Fullscreen = False

		lock% = False

		If Config\Graphics\BorderlessWindowed Or (Not Config\Graphics\Fullscreen) Then lock% = True
		Config\Graphics\Bit16Mode = DrawTick(40 + 430 - 15, 260 - 55 + 65 + 8, Config\Graphics\Bit16Mode,lock%)
		Config\Launcher\LauncherEnabled = DrawTick(40 + 430 - 15, 260 - 55 + 95 + 8, Config\Launcher\LauncherEnabled)

		Text(40 + 430 + 15, 262 - 55 + 5 - 8, I_Loc\Launcher_FullscreenExclusive)
		Color 255, 255, 255
		RowText(I_Loc\Launcher_Fullscreen, 40 + 430 + 15, 262 - 55 + 35 - 6, 150, 50)

		If Config\Graphics\BorderlessWindowed Or (Not Config\Graphics\Fullscreen)
 		   Color 255, 0, 0
 		   Config\Graphics\Bit16Mode = False
		Else
		    Color 255, 255, 255
		EndIf

		Text(40 + 430 + 15, 262 - 55 + 65 + 8, I_Loc\Launcher_16bit)
		Color 255, 255, 255
		Text(40 + 430 + 15, 262 - 55 + 95 + 8, I_Loc\Launcher_Launcher)
		
		gfxWidth% = GfxModeWidthsByAspectRatio(SelectedAspectRatio, SelectedGfxMode) : gfxHeight% = GfxModeHeightsByAspectRatio(SelectedAspectRatio, SelectedGfxMode)

		If Config\Graphics\Fullscreen
			Text(260 + 15, 262 - 55 + 140, I_Loc\Launcher_ResolutionCurrent+" "+gfxWidth + "x" + gfxHeight + "," + (16+(16*(Not Config\Graphics\Bit16Mode))))
		Else
			Text(260 + 15, 262 - 55 + 140, I_Loc\Launcher_ResolutionCurrent+" "+gfxWidth + "x" + gfxHeight + ",32")
		EndIf

		If DrawButton(Config\Launcher\LauncherWidth - 30 - 90 - 130 - 15, Config\Launcher\LauncherHeight - 50 - 55, 130, 30, I_Loc\Launcher_Mapcreator, False, False) Then
			ExecFile(Chr(34)+"Map Creator\StartMapCreator.bat"+Chr(34))
			quit = True
			Exit
		EndIf

		If DrawButton(Config\Launcher\LauncherWidth - 30 - 90 - 130 - 15, Config\Launcher\LauncherHeight - 50, 130, 30, I_Loc\Launcher_Discord, False, False) Then
			ExecFile("https://discord.gg/guqwRtQPdq")
		EndIf
		
		If DrawButton(Config\Launcher\LauncherWidth - 30 - 90, Config\Launcher\LauncherHeight - 50 - 55, 100, 30, I_Loc\Launcher_Launch, False, False) Then
			Config\Graphics\ScreenWidth = gfxWidth
			Config\Graphics\ScreenHeight = gfxHeight
			RealGraphicWidth = Config\Graphics\ScreenWidth
			RealGraphicHeight = Config\Graphics\ScreenHeight
			Exit
		EndIf
		
		If DrawButton(Config\Launcher\LauncherWidth - 30 - 90, Config\Launcher\LauncherHeight - 50, 100, 30, I_Loc\Launcher_Exit, False, False) Then quit = True : Exit
		Flip
	Forever
	
	PutINIValue(Paths\OptionsFile, "graphics", "width", GfxModeWidthsByAspectRatio(SelectedAspectRatio, SelectedGfxMode))
	PutINIValue(Paths\OptionsFile, "graphics", "height", GfxModeHeightsByAspectRatio(SelectedAspectRatio, SelectedGfxMode))
	If Config\Graphics\Fullscreen Then
		PutINIValue(Paths\OptionsFile, "graphics", "fullscreen", "true")
	Else
		PutINIValue(Paths\OptionsFile, "graphics", "fullscreen", "false")
	EndIf
	If Config\Launcher\LauncherEnabled Then
		PutINIValue(Paths\OptionsFile, "launcher", "launcher enabled", "true")
	Else
		PutINIValue(Paths\OptionsFile, "launcher", "launcher enabled", "false")
	EndIf
	If Config\Graphics\BorderlessWindowed Then
		PutINIValue(Paths\OptionsFile, "graphics", "borderless windowed", "true")
	Else
		PutINIValue(Paths\OptionsFile, "graphics", "borderless windowed", "false")
	EndIf
	If Config\Graphics\Bit16Mode Then
		PutINIValue(Paths\OptionsFile, "graphics", "16bit", "true")
	Else
		PutINIValue(Paths\OptionsFile, "graphics", "16bit", "false")
	EndIf
	PutINIValue(Paths\OptionsFile, "graphics", "gfx driver", Config\Graphics\SelectedGFXDriver)
	
	FreeImage(LauncherIMG) : LauncherIMG = 0
	
	If quit Then End

	Dim AspectRatioWidths%(0), AspectRatioHeights%(0)
	Dim GfxModeCountPerAspectRatio%(0)
	Dim GfxModeWidthsByAspectRatio%(0, 0), GfxModeHeightsByAspectRatio%(0, 0)
End Function
