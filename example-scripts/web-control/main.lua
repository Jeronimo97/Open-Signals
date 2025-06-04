--[[
  CC:Tweaked‐Lua‐Skript für Signalsteuerung (modularisiert)
  -------------------------------------------------------------------------------
  Dieses Skript nutzt mehrere Module:
    • api.lua: HTTP- und JSON-Hilfsfunktionen
    • signal_poll.lua: Polling- und Redstone-Logik
    • logger.lua: Logging
  Die main.lua enthält nur noch Konfiguration und den Start der Hauptschleife.
--]]

-- === 1. MODULE ===
local logger = require("logger")
local api    = require("api")
local signal = require("signal")

local interface = peripheral.find("os_interface")

-- === 2. KONFIGURATION ===
local CONFIG = {
  API_HOST      = "http://127.0.0.1:3000",    -- Basis‐URL ohne „/api“
  SIGNAL_ID     = os.computerID(),              -- ID dieses Signals
  POLL_INTERVAL = 2,                            -- Intervall in Sekunden
  RS_SIDE_INPUT = "right",                      -- Seite für Redstone‐Input
  RS_SIDE_OUTPUT= "left",                       -- Seite für Redstone‐Output
  HTTP_TIMEOUT  = 5                             -- Timeout für HTTP‐Requests (Sek.)
}

-- === 3. START ===
logger.info("Starte Signalsteuerung...")

if interface == nil then
    logger.error("Keine OS-Interface gefunden!")
    sleep(5)
    shell.exit()
end

if not # interface == 1 then
    logger.error("Kein oder mehere OS-Interfaces gefunden!")
    sleep(5)
    shell.exit()
end

if not interface.hasLink() then
    logger.error("Kein Signal Verbunden!")
    sleep(5)
    shell.exit()
end
logger.info("Signal verbunden!")

logger.info("Registriere Signal mit ID: " .. CONFIG.SIGNAL_ID)

local success, supportedStates = signal.init(interface)

if not success then
    logger.error("Fehler beim Initialisieren des Signals!")
end

while true do
    local _, k, _ = os.pullEvent("key")

    if k == keys.q then
        logger.info("Beende Programm...")
        break
    end

    -- poll signal state from api
    local response = api.get_signal(CONFIG, CONFIG.SIGNAL_ID)
    if response == nil then
        logger.error("Fehler beim Abrufen des Signals!")
    end

    local signalState = response.state
    
    interface.setSignalState(signalState)
    
end
