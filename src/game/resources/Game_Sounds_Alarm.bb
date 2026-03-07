Global SFX_Alarm_1%
Global SFX_Alarm_2%
Global SFX_Alarm_3%
Global SFX_Alarm_4%

Const SFX_ALARM_AMBIANCE_COUNT_UPPERBOUND = 10
Dim SFX_Alarm_Ambiance(11)

Function PlayRandomAlarmAmbianceSFX()
    PlaySFX(SFX_Alarm_Ambiance(Rand(0,SFX_ALARM_AMBIANCE_COUNT_UPPERBOUND)))
End Function

Function Register_AlarmSounds()
    SFX_Alarm_1 = Resource_RegisterSound("SFX\Alarm\Alarm.ogg")
    SFX_Alarm_2 = Resource_RegisterSound("SFX\Alarm\Alarm3.ogg")
    SFX_Alarm_3 = Resource_RegisterSound("SFX\Alarm\Alarm4.ogg")
    SFX_Alarm_4 = Resource_RegisterSound("SFX\Alarm\Alarm5.ogg")

    SFX_Alarm_Ambiance(0) = Resource_RegisterSound("SFX\Alarm\Alarm2_1.ogg")
    SFX_Alarm_Ambiance(1) = Resource_RegisterSound("SFX\Alarm\Alarm2_2.ogg")
    SFX_Alarm_Ambiance(2) = Resource_RegisterSound("SFX\Alarm\Alarm2_3.ogg")
    SFX_Alarm_Ambiance(3) = Resource_RegisterSound("SFX\Alarm\Alarm2_4.ogg")
    SFX_Alarm_Ambiance(4) = Resource_RegisterSound("SFX\Alarm\Alarm2_5.ogg")
    SFX_Alarm_Ambiance(5) = Resource_RegisterSound("SFX\Alarm\Alarm2_6.ogg")
    SFX_Alarm_Ambiance(6) = Resource_RegisterSound("SFX\Alarm\Alarm2_7.ogg")
    SFX_Alarm_Ambiance(7) = Resource_RegisterSound("SFX\Alarm\Alarm2_8.ogg")
    SFX_Alarm_Ambiance(8) = Resource_RegisterSound("SFX\Alarm\Alarm2_9.ogg")
    SFX_Alarm_Ambiance(9) = Resource_RegisterSound("SFX\Alarm\Alarm2_10.ogg")
    SFX_Alarm_Ambiance(10) = Resource_RegisterSound("SFX\Alarm\Alarm2_11.ogg")
End Function

;; ToDo:: Determine if LoopSFX works correct as intended
;LoopSFX(SFX_Alarm_4, 4, Camera, e\room\Objects[0], 5.0)
;; ToDo:: replace all usages of array "AlarmSFX"
;; ToDo:: replace the calls to "Alarm2_x" call buried somewhere