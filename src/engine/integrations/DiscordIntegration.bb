Global DiscordLastStatus%, DiscordCooldown%

Function DiscordIntegration_Init%()

    If Not Config\Gameplay\EnableDiscord Then Return False
    If HasCLIFlag("nodiscord") Then Return False

    local success% = (BlitzcordCreateCore("1465275739342377014") = 0)
    If success Then 
        BlitzcordSetLargeImage("logo")
    End If
    
    Return success

End Function