local signal = peripheral.wrap("left")

while true do
    signal.setSignalState("hpblock", "HP0")
    sleep(1)
    signal.setSignalState("hpblock", "HP1")
    sleep(1)
end
