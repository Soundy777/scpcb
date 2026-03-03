Function Game_Preload()

    Resource_PreloadTexture(TEX_CURSOR)

    Resource_PreloadFont(FONT_UI_SMALL)
    Resource_PreloadFont(FONT_UI_LARGE)
    Resource_PreloadFont(FONT_DIGITAL_SMALL)
    Resource_PreloadFont(FONT_DIGITAL_LARGE)
    Resource_PreloadFont(FONT_JOURNAL)

    GameFonts\Console = Resource_GetFont(FONT_UI_SMALL)
    ;; ToDo:: GameFonts\Credits_Main
    ;; ToDo:: GameFonts\Credits_Secondary
    GameFonts\UI_Small = Resource_GetFont(FONT_UI_SMALL)
    GameFonts\UI_Large = Resource_GetFont(FONT_UI_LARGE)
    GameFonts\Digital_Small = Resource_GetFont(FONT_DIGITAL_SMALL)
    GameFonts\Digital_Large = Resource_GetFont(FONT_DIGITAL_LARGE)
    GameFonts\Handwritten = Resource_GetFont(FONT_JOURNAL)

End Function