package sr.thrift.server.DeviceHandlers;

import sr.thrift.server.DeviceStore.DeviceStore;
import generated.rpc.*;
import org.apache.thrift.TException;

public class FridgeHandler extends DeviceHandler implements Fridge.Iface{

    public FridgeHandler(DeviceStore container){
        super(container);
    }
    @Override
    public ResultStatus getTemperature(String id) throws TException {
        System.out.println("Called GET TEMPERATURE id: " + id);

        FridgeObject fridgeObject = deviceStore.getFridgeById(id);
        if(fridgeObject != null) {
            ResultStatus resultStatus=new ResultStatus();
            resultStatus.setMessage("Fridge "+ "| id: " + id + " | Temperature: " + fridgeObject.getTemperature() + " Celsius degrees");
            return resultStatus;
        } else {
            throw new CustomException("Get Temperature", "Fridge id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus setTemperature(String id, int value) throws TException {
        System.out.println("Called SET TEMPERATURE id: " + id + " value: " + value);

        FridgeObject fridgeObject = deviceStore.getFridgeById(id);
        if (fridgeObject != null) {
            if (fridgeObject.getDevice().getState() == DeviceState.OFF) {
                throw new CustomException("Change Temperature","Fridge id: " + id +" is turned off. Temperature cannot be changed!");
            } else {
                if (value >= -10 && value <= 10){
                    ResultStatus resultStatus=new ResultStatus();
                    resultStatus.setMessage("Temperature set to " + value + " Celsius degrees");
                    fridgeObject.setTemperature(value);
                    System.out.println("Fridge " + id + " temperature set to " + value + " Celsius degrees.");
                    return resultStatus;
                } else {
                    throw new CustomException("Set Temperature","Fridge id: " + id +". Temperature is out of range. You can set a value only in range -10 to 10!");
                }
            }
        } else {
            throw new CustomException("Set Temperature","Fridge id: " + id +" has not been found.");
        }
    }
}