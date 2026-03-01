;achievement menu & messages by InnocentSam

Const MAXACHIEVEMENTS=38
Dim Achievements%(MAXACHIEVEMENTS)

Const Achv008%=0, Achv012%=1, Achv035%=2, Achv049%=3, Achv055%=4, Achv066%=5, Achv079%=6, Achv096%=7, Achv106%=8, Achv148%=9
Const Achv205%=10, Achv294%=11, Achv372%=12, Achv420%=13, Achv427%=14, Achv500%=15, Achv513%=16, Achv714%=17, Achv789%=18, Achv860%=19
Const Achv895%=20, Achv914%=21, Achv939%=22, Achv966%=23, Achv970%=24, Achv1025%=25, Achv1048%=26, Achv1123%=27, Achv1162%=28, Achv1499%=29

Const AchvMaynard%=30, AchvHarp%=31, AchvSNAV%=32, AchvOmni%=33, AchvTesla%=34, AchvPD%=35

Const AchvConsole%=36, AchvKeter%=37

Global AchvNames$[MAXACHIEVEMENTS]
AchvNames[Achv008] = "008"
AchvNames[Achv012] = "012"
AchvNames[Achv035] = "035"
AchvNames[Achv049] = "049"
AchvNames[Achv055] = "055"
AchvNames[Achv066] = "066"
AchvNames[Achv079] = "079"
AchvNames[Achv096] = "096"
AchvNames[Achv106] = "106"
AchvNames[Achv148] = "148"
AchvNames[Achv205] = "205"
AchvNames[Achv294] = "294"
AchvNames[Achv372] = "372"
AchvNames[Achv420] = "420J"
AchvNames[Achv427] = "427"
AchvNames[Achv500] = "500"
AchvNames[Achv513] = "513"
AchvNames[Achv714] = "714"
AchvNames[Achv789] = "789J"
AchvNames[Achv860] = "860"
AchvNames[Achv895] = "895"
AchvNames[Achv914] = "914"
AchvNames[Achv939] = "939"
AchvNames[Achv966] = "966"
AchvNames[Achv970] = "970"
AchvNames[Achv1025] = "1025"
AchvNames[Achv1048] = "1048"
AchvNames[Achv1123] = "1123"
AchvNames[Achv1162] = "1162"
AchvNames[Achv1499] = "1499"
AchvNames[AchvMaynard] = "Maynard"
AchvNames[AchvHarp] = "Harp"
AchvNames[AchvSNAV] = "SNAV"
AchvNames[AchvOmni] = "Omni"
AchvNames[AchvTesla] = "Tesla"
AchvNames[AchvPD] = "PD"
AchvNames[AchvConsole] = "Console"
AchvNames[AchvKeter] = "Keter"

Global UsedConsole

Global AchievementsMenu%
Global AchvMSGenabled% = GetOptionInt("general", "achievement popup enabled")
Dim AchievementStrings$(MAXACHIEVEMENTS)
Dim AchievementDescs$(MAXACHIEVEMENTS)
Dim AchvIMG%(MAXACHIEVEMENTS)
For i = 0 To MAXACHIEVEMENTS-1
	AchievementStrings(i) = GetModdedINIString(StringsFile, "Achievement", AchvNames[i])
	AchievementDescs(i) = GetModdedINIString(StringsFile, "Achievement Desc", AchvNames[i])
	
	AchvIMG(i) = LoadImage_Strict("GFX\menu\achievements\Achv"+AchvNames[i]+".jpg")
	AchvIMG(i) = ResizeImage2(AchvIMG(i),ImageWidth(AchvIMG(i))*Config\Graphics\ScreenHeight/768.0,ImageHeight(AchvIMG(i))*Config\Graphics\ScreenHeight/768.0)
Next

Global AchvLocked = LoadImage_Strict("GFX\menu\achievements\achvlocked.jpg")
AchvLocked = ResizeImage2(AchvLocked,ImageWidth(AchvLocked)*Config\Graphics\ScreenHeight/768.0,ImageHeight(AchvLocked)*Config\Graphics\ScreenHeight/768.0)

Function GiveAchievement(achvname%, showMessage%=True)
	If Achievements(achvname)<>True Then
		Achievements(achvname)=True
		Local loc2% = -1
		If AchvMSGenabled And showMessage Then
			CreateAchievementMsg(achvname,AchievementStrings(achvname))
		EndIf
		; The "Fair Play" achievement cannot be found on Steam because every achievement there requires the console to not be used.
		If Flags\SteamActive And (Not UsedConsole) And achvname<>AchvConsole Then
			Steam_Achieve("Achv" + AchvNames[achvname])
		End If
	EndIf
End Function

Function AchievementTooltip(achvno%)
    Local scale# = Config\Graphics\ScreenHeight/768.0
    
    SetFont Font3
    Local width = StringWidth(AchievementStrings(achvno))
    SetFont Font1
    If (StringWidth(AchievementDescs(achvno))>width) Then
        width = StringWidth(AchievementDescs(achvno))
    EndIf
    width = width+20*MenuScale
    
    Local height = 38*scale
    
    Color 25,25,25
    Rect(ScaledMouseX()+(20*MenuScale),ScaledMouseY()+(20*MenuScale),width,height,True)
    Color 150,150,150
    Rect(ScaledMouseX()+(20*MenuScale),ScaledMouseY()+(20*MenuScale),width,height,False)
    SetFont Font3
    Text(ScaledMouseX()+(20*MenuScale)+(width/2),ScaledMouseY()+(35*MenuScale), AchievementStrings(achvno), True, True)
    SetFont Font1
    Text(ScaledMouseX()+(20*MenuScale)+(width/2),ScaledMouseY()+(55*MenuScale), AchievementDescs(achvno), True, True)
End Function

Function DrawAchvIMG(x%, y%, achvno%)
	Local row%
	Local scale# = Config\Graphics\ScreenHeight/768.0
	Local SeparationConst2 = 76 * scale
;	If achvno >= 0 And achvno < 4 Then 
;		row = achvno
;	ElseIf achvno >= 3 And achvno <= 6 Then
;		row = achvno-3
;	ElseIf achvno >= 7 And achvno <= 10 Then
;		row = achvno-7
;	ElseIf achvno >= 11 And achvno <= 14 Then
;		row = achvno-11
;	ElseIf achvno >= 15 And achvno <= 18 Then
;		row = achvno-15
;	ElseIf achvno >= 19 And achvno <= 22 Then
;		row = achvno-19
;	ElseIf achvno >= 24 And achvno <= 26 Then
;		row = achvno-24
;	EndIf
	row = achvno Mod 4
	Color 0,0,0
	Rect((x+((row)*SeparationConst2)), y, 64*scale, 64*scale, True)
	If Achievements(achvno) = True Then
		DrawImage(AchvIMG(achvno),(x+(row*SeparationConst2)),y)
	Else
		DrawImage(AchvLocked,(x+(row*SeparationConst2)),y)
	EndIf
	Color 50,50,50
	
	Rect((x+(row*SeparationConst2)), y, 64*scale, 64*scale, False)
End Function

Global CurrAchvMSGID% = 0

Type AchievementMsg
	Field achvID%
	Field txt$
	Field msgx#
	Field msgtime#
	Field msgID%
End Type

Function CreateAchievementMsg.AchievementMsg(id%,txt$)
	Local amsg.AchievementMsg = New AchievementMsg
	
	amsg\achvID = id
	amsg\txt = txt
	amsg\msgx = 0.0
	amsg\msgtime = FPSfactor2
	amsg\msgID = CurrAchvMSGID
	CurrAchvMSGID = CurrAchvMSGID + 1
	
	Return amsg
End Function

Function UpdateAchievementMsg()
	Local amsg.AchievementMsg,amsg2.AchievementMsg
	Local scale# = Config\Graphics\ScreenHeight/768.0
	Local width% = 264*scale
	Local height% = 84*scale
	Local x%,y%
	
	For amsg = Each AchievementMsg
		If amsg\msgtime <> 0
			x=Config\Graphics\ScreenWidth+amsg\msgx
			y=(Config\Graphics\ScreenHeight-height)
			For amsg2 = Each AchievementMsg
				If amsg2 <> amsg
					If amsg2\msgID > amsg\msgID
						y=y-height
					EndIf
				EndIf
			Next
			DrawFrame(x,y,width,height)
			Color 0,0,0
			Rect(x+10*scale,y+10*scale,64*scale,64*scale,True)
			DrawImage(AchvIMG(amsg\achvID),x+10*scale,y+10*scale)
			Color 50,50,50
			Rect(x+10*scale,y+10*scale,64*scale,64*scale,False)
			Color 255,255,255
			SetFont Font1
			RowText(Format(I_Loc\HUD_AchvUnlocked, amsg\txt),x+84*scale,y+10*scale,width-94*scale,y-20*scale)
			If amsg\msgtime > 0.0 And amsg\msgtime < 70*7
				amsg\msgtime = amsg\msgtime + FPSfactor2
				If amsg\msgx > -width%
					amsg\msgx = Max(amsg\msgx-4*FPSfactor2,-width%)
				EndIf
			ElseIf amsg\msgtime >= 70*7
				amsg\msgtime = -1
			ElseIf amsg\msgtime = -1
				If amsg\msgx < 0.0
					amsg\msgx = Min(amsg\msgx+4*FPSfactor2,0.0)
				Else
					amsg\msgtime = 0.0
				EndIf
			EndIf
		Else
			Delete amsg
		EndIf
	Next
	
End Function




;~IDEal Editor Parameters:
;~F#31#48
;~C#Blitz3D