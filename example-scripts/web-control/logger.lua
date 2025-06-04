-- Simple Logger for ComputerCraft
-- Usage: local logger = require("logger")
-- logger.info("Hello")

local logger = {}

local LEVELS = {
  DEBUG = 1,
  INFO  = 2,
  WARN  = 3,
  ERROR = 4
}

local LEVEL_NAMES = {"DEBUG", "INFO", "WARN", "ERROR"}

-- Set default log level here (can be changed)
logger.level = LEVELS.INFO

local function log(level, ...)
  if level < logger.level then return end
  local msg = table.concat({ ... }, " ")
  local time = os and os.clock and string.format("[%8.2fs]", os.clock()) or ""
  print(string.format("%s [%s] %s", time, LEVEL_NAMES[level], msg))
end

function logger.debug(...)
  log(LEVELS.DEBUG, ...)
end

function logger.info(...)
  log(LEVELS.INFO, ...)
end

function logger.warn(...)
  log(LEVELS.WARN, ...)
end

function logger.error(...)
  log(LEVELS.ERROR, ...)
end

function logger.setLevel(levelName)
  local lvl = LEVELS[string.upper(levelName or "")]
  if lvl then logger.level = lvl end
end

return logger
