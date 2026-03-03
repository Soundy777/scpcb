Include "src/engine/core/BuildInfo.bb"
Include "src/engine/core/FeatureFlags.bb"

Include "src/engine/infastructure/FileSystem.bb"
Include "src/engine/infastructure/IniParser.bb"
Include "src/engine/infastructure/Blitz_File_FileName.bb"
Include "src/engine/infastructure/ErrorHandling.bb"

include "src/engine/core/AppPaths.bb"
include "src/engine/startup/StartupVideos.bb"

Include "KeyName.bb"        ;; ToDo:: this will become part of our input management system

Include "src/engine/config/ConfigManager.bb"
Include "src/engine/core/Time.bb"
Include "src/engine/graphics/Graphics.bb"
Include "src/engine/graphics/Viewport.bb"

Include "src/engine/resources/ResourcesManager.bb"
Include "src/engine/audio/Audio.bb"

Include "src/engine/physics/Physics.bb"

Include "src/engine/integrations/SteamIntegration.bb"
Include "src/engine/integrations/DiscordIntegration.bb"

Include "src/engine/modding/ModManager.bb"
Include "src/engine/localization/Localization.bb"

Include "src/engine/console/ConsoleCore.bb"

Include "src/engine/effects/DevilParticleSystem.bb"