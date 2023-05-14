package sr.thrift.server.DeviceHandlers;

import generated.rpc.*;
import org.apache.thrift.TException;
import sr.thrift.server.DeviceStore.DeviceStore;

public class LampHandler extends DeviceHandler implements Lamp.Iface {

    public LampHandler(DeviceStore container) {
        super(container);
    }

    @Override
    public ResultStatus setColor(String id, LightColor color) throws TException {
        System.out.println("Called SET COLOR id: " + id + " color: " + color);

        LampObject lampObject = deviceStore.getLampById(id);
        if(lampObject != null) {
            if (lampObject.getDevice().getState() == DeviceState.OFF) {
                throw new CustomException("Set Color","Lamp id: " + id +" is turned off. Color cannot be changed!");
            } else {
                ResultStatus resultStatus = new ResultStatus();
                resultStatus.setMessage("Lamp "+ "| id: " + id + " | Color set to: " + color);
                lampObject.setColor(color);
                System.out.println("Lamp " + id + " color set to " + color);
                return resultStatus;
            }
        } else {
            throw new CustomException("Set Color", "Lamp "+ " | id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus getColor(String id) throws TException {
        System.out.println("Called GET COLOR id: " + id);

        LampObject lampObject = deviceStore.getLampById(id);
        if(lampObject != null) {
            ResultStatus resultStatus = new ResultStatus();
            resultStatus.setMessage("Lamp "+ "| id: " + id + " | Color: " + lampObject.getColor());
            return resultStatus;
        } else {
            throw new CustomException("Get Color", "Lamp "+ " | id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus setIntensity(String id, int value) throws TException {
        System.out.println("Called SET INTENSITY id: " + id + " value: " + value);

        LampObject lampObject = deviceStore.getLampById(id);
        if(lampObject != null){
            if (lampObject.getDevice().getState() == DeviceState.OFF) {
                throw new CustomException("Set Intensity","Lamp "+ " | id: " + id +" is turned off. Intensity cannot be set!");
            } else {
                if (value >= 0 && value <= 100) {
                    ResultStatus resultStatus = new ResultStatus();
                    resultStatus.setMessage("Lamp "+ "| id: " + id + " | Intensity set to: " + value);
                    lampObject.setIntensity(value);
                    System.out.println("Lamp " + id + " intensity set to " + value);
                    return resultStatus;
                } else {
                    throw new CustomException("Set Intensity","Lamp "+ " | id: " + id +". Intensity is out of range. You can set a value only in range 0 to 100!");
                }
            }
        } else {
            throw new CustomException("Set Intensity", "Lamp "+ " | id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus getIntensity(String id) throws TException {
        System.out.println("Called GET INTENSITY id: " + id);

        LampObject lampObject = deviceStore.getLampById(id);
        if(lampObject != null) {
            ResultStatus resultStatus = new ResultStatus();
            resultStatus.setMessage("Lamp "+ "| id: " + id + " | Intensity: " + lampObject.getIntensity());
            return resultStatus;
        } else {
            throw new CustomException("Get Intensity", "Lamp "+ " | id: " + id +" has not been found.");
        }
    }
}
