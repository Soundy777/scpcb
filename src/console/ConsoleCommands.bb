; ===========================================================================
; ConsoleCommands.bb
; ===========================================================================

Const CMD_HELP = 1
Const CMD_CLEAR = 2
Const CMD_GODMODE = 3

; ---------------------------------------------------------------------------

Function InitConsoleCommands()

    RegisterConsoleCommand("help", CMD_HELP, "Lists commands", "Lists all available console commands with a short description of each.")
    RegisterConsoleCommand("clear", CMD_CLEAR, "Clears console", "Clears the console output.")
    RegisterConsoleCommand("godmode", CMD_GODMODE, "Toggles god mode", "ToDo:: long description")

End Function

; ---------------------------------------------------------------------------

Function Console_DispatchCommand(commandID%, args$)
    Select commandID
        Case CMD_HELP
            Cmd_Help(args)
        Case CMD_CLEAR
            Cmd_Clear(args)
        Case CMD_GODMODE
            Cmd_Godmode(args)
    End Select
End Function

; ---------------------------------------------------------------------------

Function Cmd_Help(args$)

    ;AddConsoleLine("Available Commands:")

    ;For c.ConsoleCommand = Each ConsoleCommand
        ;AddConsoleLine(c\Name + " - " + c\Description)
    ;Next

End Function

Function Cmd_Clear(args$)

    ;Console_LineCount = 0

End Function

Function Cmd_Godmode(args$)

    ; Example placeholder
    ;AddConsoleLine("Godmode enabled.")
    ; Player\GodMode = True
    DebugLog "Console:: God Mode Command invoked."
    DebugLog "Args" + args$

End Function

; ---------------------------------------------------------------------------