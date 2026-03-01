Function EnsureDirectory(path$)
    If FileType(path$) <> 2 Then CreateDir(path$)
End Function

Function EnsureFileExists(path$)
    If FileType(path$) <> 1 Then
        Local f% = WriteFile(path$)
        CloseFile(f)
    EndIf
End Function

Function GetOSBits%()
    If GetEnv("ProgramFiles(X86)") <> 0 Then
        Return 64
    Else
        Return 32
    EndIf
End Function