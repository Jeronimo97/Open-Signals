local signal = {}

-- return false if something went wrong
-- return true and supportedStates if everything is fine
function signal.init(interface)
    local res = interface.getSupportedSignalState()

    local success = res[1]
    local supportedStates = res[2]

    if not success then
        return flase
    end

    signal.supportedStates = supportedStates
    signal.currentState = nil
    signal.lastState = nil

    return true and supportedStates
end

return signal