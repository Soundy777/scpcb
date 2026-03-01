Function SteamIntegration_Init(isSteamActive%)
    If isSteamActive Then
        If Steam_RestartAppIfNecessary(2178380) Then Return
	    If Steam_Init() <> 0 Then RuntimeErrorExt("Steam failed to initialize")
    End If
End Function