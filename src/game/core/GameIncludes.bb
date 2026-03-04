Include "src/game/resources/Game_Sounds.bb"
Include "src/game/resources/Game_Textures.bb"
Include "src/game/resources/Game_Fonts.bb"
Include "src/game/resources/Game_Preload.bb"

Include "src/game/systems/loading/LoadingSystem.bb"
Include "src/game/systems/loading/LoadingRenderer.bb"

;; ToDo:: Fix this jank
; Player movement depends upon some aspect of Items.bb which itself depends upon some SFX variables
;Include "Items.bb"
;Include "src/game/player/Player.bb"