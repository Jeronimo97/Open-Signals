package com.troblecodings.signals.cctweaked;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.troblecodings.signals.OpenSignalsMain;
import com.troblecodings.signals.SEProperty;
import com.troblecodings.signals.blocks.Signal;
import com.troblecodings.signals.enums.ChangeableStage;
import com.troblecodings.signals.tileentitys.ComputerinterfaceEntity;
import com.troblecodings.signals.tileentitys.SignalTileEntity;

public class ComputerInterfacePeripheral implements IPeripheral, Driver {

    private final ComputerinterfaceEntity tileEntity;
    private final World world;
    private final BlockPos pos;

    private List<SEProperty> properties = new ArrayList<>();
    private final Map<Integer, SEProperty> intToSEProperty = new HashMap<>();
    private final Map<String, List<String>> propertyAllowedValues = new HashMap<>();

    public ComputerInterfacePeripheral(ComputerinterfaceEntity tileEntity) {
        this.tileEntity = tileEntity;
        this.world = tileEntity.getWorld();
        this.pos = tileEntity.getPos();
        initDriver();
    }

    @Override
    @Nonnull
    public String getType() {
        return "os_interface";
    }

    @Override
    @Nullable
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException, InterruptedException {
        LuaFunction f = functions.get(method);

        try {
            return new Object[] {
                f.method.invoke(this, (Object) arguments)
            };
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new Object[] {};
        }
    }

    @Override
    @Nonnull
    public String[] getMethodNames() {
        return functionNames.toArray(new String[0]);
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if (other == this) return true;
        if (!(other instanceof ComputerInterfacePeripheral)) return false;
        ComputerInterfacePeripheral otherPeripheral = (ComputerInterfacePeripheral) other;
        return otherPeripheral.pos.equals(this.pos) && otherPeripheral.world.equals(this.world);
    }

    @CCFunction
    public Object[] hasLink(Object[] arguments) {
        return new Object[] { true, 
            new Object[] {
                tileEntity.hasLink()
            }
        };
    }

    @CCFunction
    public Object[] getLinkedSignal(Object[] arguments) throws LuaException {
        BlockPos pos = tileEntity.getPos();
        return new Object[] { 
            true,
            new Object[] {
                pos.getX(), pos.getY(), pos.getZ()
            }
        };
    }

    @CCFunction
    public Object[] getSupportedSignalState(Object[] arguments) throws LuaException {
        if (!tileEntity.hasLink()) {
            return new Object[] {
                false
            };
        }

        Signal signal = tileEntity.getLinkedSignal();
        if (signal == null) {
            return new Object[] { false, "No valid signal found" };
        }

        // Initialize properties from the signal
        properties = signal.getProperties();
        if (properties.isEmpty()) {
            return new Object[] { false, "No properties available for this signal" };
        }

        // Get current signal configuration from the signal tile entity
        Map<SEProperty, String> currentConfig = ((SignalTileEntity)world.getTileEntity(tileEntity.getLinkedPosition())).getProperties();
        
        // Filter and sort properties based on their stage, dependencies, and current configuration
        List<Map<String, Object>> signalStates = properties.stream()
            .filter(property -> {
                // First check if the property can be changed at the appropriate stage
                boolean validStage = property.isChangabelAtStage(ChangeableStage.APISTAGE) || 
                                   property.isChangabelAtStage(ChangeableStage.APISTAGE_NONE_CONFIG);
                
                if (!validStage) return false;

                // Then check if the property is active based on current configuration
                boolean validConfig = property.testMap(currentConfig);
                
                // For properties that depend on other properties, check if their dependencies are met
                if (property.getParent() != null) {
                    // Check if the property's parent enum is active in the current configuration
                    String parentValue = currentConfig.get(property);
                    if (parentValue == null || !property.getParent().getAllowedValues().contains(parentValue)) {
                        return false;
                    }
                }

                return validConfig;
            })
            .sorted(Comparator.comparing(SEProperty::getName))
            .map(property -> {
                Map<String, Object> stateInfo = new HashMap<>();
                stateInfo.put("name", property.getName());
                stateInfo.put("default", property.getDefault());
                stateInfo.put("allowedValues", property.getParent().getAllowedValues());
                stateInfo.put("stage", property.isChangabelAtStage(ChangeableStage.APISTAGE) ? 
                    "APISTAGE" : "APISTAGE_NONE_CONFIG");
                return stateInfo;
            })
            .collect(Collectors.toList());

        return new Object[] {
            true,
            signalStates.toArray()
        };
    }

    @CCFunction
    public Object[] setSignalState(Object[] arguments) throws LuaException {
        if (arguments.length < 2) {
            throw new LuaException("Expected property name and value");
        }

        String propertyName = (String) arguments[0];
        String value = (String) arguments[1];

        if (!tileEntity.hasLink()) {
            return new Object[] { false, "No signal linked" };
        }

        Signal signal = tileEntity.getLinkedSignal();
        if (signal == null) {
            return new Object[] { false, "No valid signal found" };
        }

        // Initialize properties from the signal
        properties = signal.getProperties();
        if (properties.isEmpty()) {
            return new Object[] { false, "No properties available for this signal" };
        }

        SignalTileEntity signalTile = (SignalTileEntity)world.getTileEntity(tileEntity.getLinkedPosition());
        
        // Get current properties and create a mutable copy
        Map<SEProperty, String> currentConfig = new HashMap<>(signalTile.getProperties());
        
        // Find the property by name
        SEProperty property = properties.stream()
            .filter(p -> p.getName().equals(propertyName))
            .findFirst()
            .orElseThrow(() -> new LuaException("Invalid property name: " + propertyName + 
                ". Available properties: " + properties.stream()
                    .map(SEProperty::getName)
                    .collect(Collectors.joining(", "))));

        // Validate the value
        if (!property.isValid(value)) {
            return new Object[] { false, "Invalid value for property " + propertyName + 
                ". Allowed values: " + property.getParent().getAllowedValues() };
        }

        // Check if property can be changed at current stage
        if (!property.isChangabelAtStage(ChangeableStage.APISTAGE) && 
            !property.isChangabelAtStage(ChangeableStage.APISTAGE_NONE_CONFIG)) {
            return new Object[] { false, "Property cannot be changed at current stage" };
        }

        // Update the property in our mutable copy
        currentConfig.put(property, value);
        
        // Update all properties at once
        signalTile.setProperties(currentConfig);

        return new Object[] { true };
    }

    @CCFunction
    public Object[] getPropertyInfo(Object[] arguments) throws LuaException {
        if (arguments.length < 1) {
            throw new LuaException("Expected property name");
        }

        String propertyName = (String) arguments[0];
        
        SEProperty property = properties.stream()
            .filter(p -> p.getName().equals(propertyName))
            .findFirst()
            .orElseThrow(() -> new LuaException("Invalid property name: " + propertyName));

        Map<String, Object> info = new HashMap<>();
        info.put("name", property.getName());
        info.put("default", property.getDefault());
        info.put("allowedValues", property.getParent().getAllowedValues());
        info.put("stage", property.isChangabelAtStage(ChangeableStage.APISTAGE) ? 
            "APISTAGE" : "APISTAGE_NONE_CONFIG");

        return new Object[] { true, info };
    }
}
