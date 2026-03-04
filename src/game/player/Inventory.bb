Function CanUseItem(canUseWithHazmat%, canUseWithGasMask%, canUseWithEyewear%)
	If (canUseWithHazmat = False And WearingHazmat) Then
		Msg = I_Loc\MessageItem_HazmatNouse
		MsgTimer = 70*5
		Return False
	ElseIf (canUseWithGasMask = False And (WearingGasMask Or Wearing1499))
		Msg = I_Loc\MessageItem_GasmaskNouse
		MsgTimer = 70*5
		Return False
	ElseIf (canUseWithEyewear = False And (WearingNightVision))
		Msg = I_Loc\MessageItem_NvgNouse
		MsgTimer = 70*5
		Return False
	EndIf
	
	Return True
End Function