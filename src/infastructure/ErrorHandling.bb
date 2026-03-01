Function ErrorHandling_Init(versionNumber$)

    InitErrorMsgs(11, True)

    SetErrorMsg(0, "An error occured in SCP - Containment Breach v" + versionNumber)
    SetErrorMsg(1, "Please send us the generated minidump along with a screenshot of this window!")
    SetErrorMsg(2, "---------------------------------------------------")
    SetErrorMsg(3, "OS: " + SystemProperty("os") + " " + GetOSBits() + " Bit (Build: " + SystemProperty("osbuild") + ")")
    SetErrorMsg(4, "CPU: " + Trim(SystemProperty("cpuname")) + " (Arch: " + SystemProperty("cpuarch") + ", " + GetEnv("NUMBER_OF_PROCESSORS") + " Threads)")

    SetErrorMsg(8, "Caught exception: " + "_CaughtError_")

End Function

Function CatchErrors(location$)
	SetErrorMsg(9, location)
End Function

Function RuntimeErrorExt%(Message$)
	SetErrorMsg(8, "Caught exception: " + Message)
	MemoryAccessViolation()
End Function