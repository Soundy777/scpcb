Const FONT_UI_SMALL%        = 1 ; Previously Font1
Const FONT_UI_LARGE%        = 2 ; Previously Font2
Const FONT_DIGITAL_SMALL%   = 3 ; Previously Font3
Const FONT_DIGITAL_LARGE%   = 4 ; Previously Font4
Const FONT_JOURNAL%         = 5 ; Previously Font5

Type GameFonts
    Field Console%
    Field Credits_Main%
    Field Credits_Secondary%
    Field UI_Small%
    Field UI_Large%
    Field Digital_Small%
    Field Digital_Large%
    Field Handwritten%
End Type

Global GameFonts.GameFonts = New GameFonts

;; Note:: Fonts must match their internal name due to some blitz bug
Function Game_RegisterFonts()

    Resource_RegisterFont(FONT_UI_SMALL, 19, "GFX\font\cour\Courier New.ttf")
    Resource_RegisterFont(FONT_UI_LARGE, 52, "GFX\font\cour\Courier New.ttf")
    Resource_RegisterFont(FONT_DIGITAL_SMALL, 22, "GFX\font\DS-DIGI\DS-Digital.ttf")
    Resource_RegisterFont(FONT_DIGITAL_LARGE, 60, "GFX\font\DS-DIGI\DS-Digital.ttf")
    Resource_RegisterFont(FONT_JOURNAL, 58, "GFX\font\Journal\Journal.ttf")

End Function

