package sr.thrift.server.DeviceHandlers;
import generated.rpc.*;
import org.apache.thrift.TException;
import sr.thrift.server.DeviceStore.DeviceStore;

import java.util.ArrayList;
import java.util.List;


public class DeviceHandler implements Device.Iface {
    DeviceStore deviceStore;
    public DeviceHandler(DeviceStore deviceStore) {
        this.deviceStore = deviceStore;
    }
    @Override
    public ResultStatus getState(String id) throws TException {
        System.out.println("Called GET STATE id: " + id);

        ResultStatus resultStatus = new ResultStatus();

        FridgeObject fridgeObject = deviceStore.getFridgeById(id);
        if (fridgeObject != null) {
            if (fridgeObject.getDevice().getState().equals(DeviceState.ON)) {
                resultStatus.setMessage("Fridge " + id + " is turned on.");
            } else {
                resultStatus.setMessage("Fridge " + id + " is turned off.");
            }

            return resultStatus;
        }

        LampObject lampObject = deviceStore.getLampById(id);
        if (lampObject != null) {
            if (lampObject.getDevice().getState().equals(DeviceState.ON)) {
                resultStatus.setMessage("Lamp " + id + " is turned on.");
            } else {
                resultStatus.setMessage("Lamp " + id + " is turned off.");
            }

            return resultStatus;
        }

        VacuumCleanerObject vacuumCleanerObject = deviceStore.getVacuumCleanerById(id);
        if (vacuumCleanerObject != null) {
            if (vacuumCleanerObject.getDevice().getState().equals(DeviceState.ON)) {
                resultStatus.setMessage("Vacuum cleaner " + id + " is turned on.");
            } else {
                resultStatus.setMessage("Vacuum cleaner " + id + " is turned off.");
            }

            return resultStatus;
        }

        TelevisionObject televisionObject = deviceStore.getTelevisionById(id);
        if (televisionObject != null) {
            if (televisionObject.getDevice().getState().equals(DeviceState.ON)) {
                resultStatus.setMessage("Television " + id + " is turned on.");
            } else {
                resultStatus.setMessage("Television " + id + " is turned off.");
            }

            return resultStatus;
        }

        throw new CustomException("Get State", "Device " + id + " doesn't exist.");
    }

    @Override
    public ResultStatus turnOn(String id) throws TException {
        System.out.println("Called TURN ON id: " + id);

        ResultStatus resultStatus = new ResultStatus();

        FridgeObject fridgeObject= deviceStore.getFridgeById(id);
        if (fridgeObject != null) {
            if (fridgeObject.getDevice().getState() == DeviceState.ON) {
                resultStatus.setMessage("Fridge " + id + " is already turned on.");
            } else {
                fridgeObject.getDevice().setState(DeviceState.ON);
                resultStatus.setMessage("Fridge " + id + " has been turned on.");
                System.out.println("Fridge " + id + " has been turned on.");
            }

            return resultStatus;
        }

        LampObject lampObject= deviceStore.getLampById(id);
        if (lampObject != null) {
            if (lampObject.getDevice().getState() == DeviceState.ON) {
                resultStatus.setMessage("Lamp " + id + " is already turned on.");
            } else {
                lampObject.getDevice().setState(DeviceState.ON);
                resultStatus.setMessage("Lamp " + id + " has been turned on.");
                System.out.println("Lamp " + id + " has been turned on.");
            }

            return resultStatus;
        }

        VacuumCleanerObject vacuumCleanerObject= deviceStore.getVacuumCleanerById(id);
        if (vacuumCleanerObject != null) {
            if (vacuumCleanerObject.getDevice().getState() == DeviceState.ON) {
                resultStatus.setMessage("Vacuum cleaner " + id + " is already turned on.");
            } else {
                vacuumCleanerObject.getDevice().setState(DeviceState.ON);
                resultStatus.setMessage("Vacuum cleaner " + id + " has been turned on.");
                System.out.println("Vacuum cleaner " + id + " has been turned on.");
            }

            return resultStatus;
        }

        TelevisionObject televisionObject = deviceStore.getTelevisionById(id);
        if (televisionObject != null) {
            if (televisionObject.getDevice().getState() == DeviceState.ON) {
                resultStatus.setMessage("Television " + id + " is already turned on.");
            } else {
                televisionObject.getDevice().setState(DeviceState.ON);
                resultStatus.setMessage("Television " + id + " has been turned on.");
                System.out.println("Television " + id + " has been turned on.");
            }

            return resultStatus;
        }

        throw new CustomException("Turn On", "Device " + id + " doesn't exist.");
    }

    @Override
    public ResultStatus turnOff(String id) throws TException {
        System.out.println("Called TURN OFF id: " + id);

        ResultStatus resultStatus = new ResultStatus();

        FridgeObject fridgeObject= deviceStore.getFridgeById(id);
        if (fridgeObject != null) {
            if (fridgeObject.getDevice().getState() == DeviceState.OFF) {
                resultStatus.setMessage("Fridge " + id + " is already turned off.");
            } else {
                fridgeObject.getDevice().setState(DeviceState.OFF);
                resultStatus.setMessage("Fridge " + id + " has been turned off.");
                System.out.println("Fridge " + id + " has been turned off.");
            }

            return resultStatus;
        }

        LampObject lampObject = deviceStore.getLampById(id);
        if (lampObject != null) {
            if (lampObject.getDevice().getState() == DeviceState.OFF) {
                resultStatus.setMessage("Lamp " + id + " is already turned off.");
            } else {
                lampObject.getDevice().setState(DeviceState.OFF);
                resultStatus.setMessage("Lamp " + id + " has been turned off.");
                System.out.println("Lamp " + id + " has been turned off.");
            }

            return resultStatus;
        }

        VacuumCleanerObject vacuumCleanerObject = deviceStore.getVacuumCleanerById(id);
        if (vacuumCleanerObject != null) {
            if (vacuumCleanerObject.getDevice().getState() == DeviceState.OFF) {
                resultStatus.setMessage("Vacuum Cleaner " + id + " is already turned off.");
            } else {
                vacuumCleanerObject.getDevice().setState(DeviceState.OFF);
                resultStatus.setMessage("Vacuum Cleaner " + id + " has been turned off.");
                System.out.println("Vacuum Cleaner " + id + " has been turned off.");
            }

            return resultStatus;
        }

        TelevisionObject televisionObject = deviceStore.getTelevisionById(id);
        if(televisionObject != null) {
            if (televisionObject.getDevice().getState() == DeviceState.OFF) {
                resultStatus.setMessage("Television " + id + " is already turned off.");
            } else {
                televisionObject.getDevice().setState(DeviceState.OFF);
                resultStatus.setMessage("Television " + id + " has been turned off.");
                System.out.println("Television " + id + " has been turned off.");
            }

            return resultStatus;
        }

        throw new CustomException("Turn Off", "Device " + id + " doesn't exist.");
    }

    @Override
    public List<String> listAllDevices() {
        System.out.println("Called LIST ALL DEVICES");

        List<String> devices = new ArrayList<>();

        deviceStore.getFridges().values().stream().map(fridge -> "Fridge" +" | id: " + fridge.getDevice().getId() + " | Temperature: " + fridge.getTemperature() + " | State: " + fridge.getDevice().getState())
                .forEach(devices::add);

        deviceStore.getLamps().values().stream().map(lamp -> "Lamp" + " | id: " + lamp.getDevice().getId() + " | Color: " + lamp.getColor() + " | Intensity: " + lamp.getIntensity() + " | State: " + lamp.getDevice().getState())
                .forEach(devices::add);

        for(VacuumCleanerObject vecuumCleaner : deviceStore.getVacuumCleaners().values()) {
            String str = "Vacuum Cleaner";
            str += " | id: " + vecuumCleaner.getDevice().getId();
            str += " | Battery: "+ vecuumCleaner.getBattery() + "%";

            String containerString = "";

            int containerValue = vecuumCleaner.getCapacity().getValue();
            switch (containerValue) {
                case 0:
                    containerString = "Empty";
                    break;
                case 1:
                    containerString = "Half Empty";
                    break;
                case 2:
                    containerString = "Full";
                    break;
                default:
                    containerString = "Unknown";
            }

            str += " | Capacity: " + containerString;
            str += " | State: " + vecuumCleaner.getDevice().getState();

            devices.add(str);
        }

        for(TelevisionObject televisionObject : deviceStore.getTelevisions().values()) {
            String str = "Television";
            str += " | id: " + televisionObject.getDevice().getId();
            str += " | Channel: "+ televisionObject.getChannel();

            String typeString = "";

            int typeValue = televisionObject.getType().getValue();
            switch (typeValue) {
                case 0:
                    typeString = "Smart";
                    break;
                case 1:
                    typeString = "Non Smart";
                    break;
                default:
                    typeString = "Unknown";
            }

            str += " | Type: " + typeString;
            str += " | State: " + televisionObject.getDevice().getState();

            devices.add(str);
        }

        return devices;
    }
}
