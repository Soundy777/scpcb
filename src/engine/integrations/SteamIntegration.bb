Function SteamIntegration_Init%()

    If Not Config\Gameplay\EnableSteam Then Return False
    If HasCLIFlag("nosteam") Then Return False

    ;; ToDO:: investigate what the actual fuck is going on with this & how we need to handle it
    If Steam_RestartAppIfNecessary(2178380) Then Return False

    If Steam_Init() <> 0 Then RuntimeErrorExt("Steam failed to initialize")
    return True

End Function