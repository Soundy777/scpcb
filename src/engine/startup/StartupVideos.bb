; ==================================================
; StartupVideos.bb
; Responsible for playing intro / splash videos
; ==================================================

Function Startup_PlayVideos()

    if (Config\Gameplay\PlayStartupVideo) Then

	    Startup_PlayMovieFile("GFX\menu\startup_Undertow")
	    Startup_PlayMovieFile("GFX\menu\startup_TSS")

    End If

End Function


Function Startup_PlayMovieFile(movieFile$)

	Local scaledHeight%
	Local aspect# = Float(Gfx\RealWidth) / Float(Gfx\RealHeight)

	; Maintain 16:9 scaling behaviour from original
	If aspect > 1.76 And aspect < 1.78
		scaledHeight = Gfx\RealHeight
		;DebugLog "Startup: Native 16:9 - Not Scaled"
	Else
		scaledHeight = Int(Float(Gfx\RealWidth) / (16.0 / 9.0))
		;DebugLog "Startup: Scaled Height = " + scaledHeight
	EndIf

	Local movie = OpenMovie(movieFile$ + ".avi")
	If movie = 0 Then
		DebugLog "Startup: Failed to open movie: " + movieFile$
		Return
	EndIf

	Local audio = StreamSound_Strict(movieFile$ + ".ogg", Config\Audio\SFXVolume, 0)

	Repeat
		Cls
		
		DrawMovie(movie, 0, (Gfx\RealHeight / 2 - scaledHeight / 2), Gfx\RealWidth, scaledHeight)
		
		Flip
		
	Until GetKey() Or (Not IsStreamPlaying_Strict(audio))

	StopStream_Strict(audio)
	CloseMovie(movie)

	Cls
	Flip

End Function