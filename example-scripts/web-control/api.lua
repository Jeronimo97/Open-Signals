-- api.lua: HTTP and JSON helpers for signal control
local api = {}

function api.jsonDecode(str)
  if not str then return nil end
  local ok, result = pcall(textutils.unserializeJSON, str)
  return ok and result or nil
end

function api.get(config, path)
  local url = config.API_HOST .. path
  if config.HTTP_TIMEOUT then
    http.request({ url = url, method = "GET", timeout = config.HTTP_TIMEOUT })
  end
  local ok, resp = pcall(http.get, url)
  if not ok or not resp then
    return nil, "HTTP GET failed: " .. tostring(resp)
  end
  local body = resp.readAll()
  resp.close()
  return body
end

function api.post(config, path, payloadTable)
  local url = config.API_HOST .. path
  local headers = { ["Content-Type"] = "application/json" }
  local bodyText = textutils.serializeJSON(payloadTable or {})
  local ok, resp = pcall(http.post, url, bodyText, headers)
  if not ok or not resp then
    return nil, "HTTP POST failed: " .. tostring(resp)
  end
  local body = resp.readAll()
  resp.close()
  return body
end

--[[
  Register a new signal with the API
  @param config: table with API_HOST etc
  @param id: string, signal id
  @param state: string, initial state
  @param possible_states: table (optional)
  @return response body or nil, error message
]]
function api.register_signal(config, id, state, possible_states)
  local payload = { id = id, state = state }
  if possible_states then payload.possible_states = possible_states end
  return api.post(config, "/api/signals/register", payload)
end

--[[
  Get all signals from the API
  @param config: table with API_HOST etc
  @return response body or nil, error message
]]
function api.get_signals(config)
  return api.get(config, "/api/signals")
end

function api.get_signal(config, id)
  return api.get(config, "/api/signals/" .. id)
end

--[[
  Update the state of a signal by its ID
  @param config: table with API_HOST etc
  @param id: string, signal id
  @param state: string, new state
  @return response body or nil, error message
]]
function api.update_signal(config, id, state)
  local payload = { state = state }
  return api.post(config, "/api/signals/" .. id, payload)
end

return api 