;--------------------------------------- math -------------------------------------------------------

Function GenerateSeedNumber(seed$)
 	Local temp% = 0
 	Local shift% = 0
 	For i = 1 To Len(seed)
 		temp = temp Xor (Asc(Mid(seed,i,1)) Shl shift)
 		shift=(shift+1) Mod 24
	Next
 	Return temp
End Function

Function Distance#(x1#, y1#, x2#, y2#)
	Local x# = x2 - x1, y# = y2 - y1
	Return(Sqr(x*x + y*y))
End Function

Function CurveValue#(number#, old#, smooth#)
	If DeltaTime = 0 Then Return old
	
	If number < old Then
		Return Max(old + (number - old) * (1.0 / smooth * DeltaTime), number)
	Else
		Return Min(old + (number - old) * (1.0 / smooth * DeltaTime), number)
	EndIf
End Function

Function CurveAngle#(val#, old#, smooth#)
	If DeltaTime = 0 Then Return old
	
   Local diff# = WrapAngle(val) - WrapAngle(old)
   If diff > 180 Then diff = diff - 360
   If diff < - 180 Then diff = diff + 360
   Return WrapAngle(old + diff * (1.0 / smooth * DeltaTime))
End Function

Function WrapAngle#(angle#)
	If angle = INFINITY Then Return 0.0
	While angle < 0
		angle = angle + 360
	Wend 
	While angle >= 360
		angle = angle - 360
	Wend
	Return angle
End Function

Function point_direction#(x1#,z1#,x2#,z2#)
	Local dx#, dz#
	dx = x1 - x2
	dz = z1 - z2
	Return ATan2(dz,dx)
End Function

Function point_distance#(x1#,z1#,x2#,z2#)
	Local dx#,dy#
	dx = x1 - x2
	dy = z1 - z2
	Return Sqr((dx*dx)+(dy*dy)) 
End Function

Function angleDist#(a0#,a1#)
	Local b# = a0-a1
	Local bb#
	If b<-180.0 Then
		bb = b+360.0
	Else If b>180.0 Then
		bb = b-360.0
	Else
		bb = b
	EndIf
	Return bb
End Function