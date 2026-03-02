Global EH_TotalVidMem = TotalVidMem()
Global EH_TotalPhysMeme = TotalPhys()
Global EH_UpdateTimer# = 0.0
Global EH_UpdateInterval# = 2 * FPS_BASE_FACTOR ; seconds (we must multiply by FPS_BASE_FACTOR as our deltatime system is scaled by it - for now)

Function ErrorHandling_Init(versionNumber$)

    InitErrorMsgs(11, True)

    SetErrorMsg(0, "An error occured in SCP - Containment Breach v" + versionNumber)
    SetErrorMsg(1, "Please send us the generated minidump along with a screenshot of this window!")
    SetErrorMsg(2, "---------------------------------------------------")
    SetErrorMsg(3, "OS: " + SystemProperty("os") + " " + GetOSBits() + " Bit (Build: " + SystemProperty("osbuild") + ")")
    SetErrorMsg(4, "CPU: " + Trim(SystemProperty("cpuname")) + " (Arch: " + SystemProperty("cpuarch") + ", " + GetEnv("NUMBER_OF_PROCESSORS") + " Threads)")

    SetErrorMsg(8, "Caught exception: " + "_CaughtError_")

End Function

Function ErrorHandling_Update()
    EH_UpdateTimer = EH_UpdateTimer + DeltaTime
    
    If EH_UpdateTimer >= EH_UpdateInterval Then
        EH_UpdateTimer = 0
        
        Local usedVid% = EH_TotalVidMem - (AvailVidMem() / 1024)
        Local usedPhys% = EH_TotalPhysMeme - (AvailPhys() / 1024)

        SetErrorMsg(5, "GPU: " + Gfx\DriverName + " (" + usedVid + "MB/" + EH_TotalVidMem + "MB)")
        SetErrorMsg(6, "RAM: (" + usedPhys + "MB/" + EH_TotalPhysMeme + "MB)")
    EndIf
End Function

Function CatchErrors(location$)
	SetErrorMsg(9, location)
End Function

Function RuntimeErrorExt%(Message$)
	SetErrorMsg(8, "Caught exception: " + Message)
	MemoryAccessViolation()
End Function