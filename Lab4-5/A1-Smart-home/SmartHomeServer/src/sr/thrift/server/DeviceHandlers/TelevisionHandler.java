package sr.thrift.server.DeviceHandlers;

import generated.rpc.*;
import org.apache.thrift.TException;
import sr.thrift.server.DeviceStore.DeviceStore;

public class TelevisionHandler extends DeviceHandler implements Television.Iface{
    public TelevisionHandler(DeviceStore container){
        super(container);
    }

    @Override
    public ResultStatus getChannel(String id) throws TException {
        System.out.println("Called GET CHANNEL id: " + id);

        TelevisionObject televisionObject = deviceStore.getTelevisionById(id);
        if(televisionObject != null) {
            ResultStatus resultStatus = new ResultStatus();
            resultStatus.setMessage("Television "+ "| id: " + id + " | Channel: " + televisionObject.getChannel());
            return resultStatus;
        } else {
            throw new CustomException("Get Channel", "Television id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus setChannel(String id, int value) throws TException {
        System.out.println("Called SET CHANNEL id: " + id + " value: " + value);

        TelevisionObject televisionObject = deviceStore.getTelevisionById(id);
        if (televisionObject != null) {
            if (televisionObject.getDevice().getState() == DeviceState.OFF) {
                throw new CustomException("Set Channel","Television id: " + id +" is turned off. Channel cannot be changed!");
            } else if (televisionObject.getType().equals(TelevisionType.NON_SMART)) {
                throw new CustomException("Set Channel","Television id: " + id +" is not smart. Channel cannot be changed!");
            } else {
                if (value >= 0 && value <= 500){
                    ResultStatus resultStatus = new ResultStatus();
                    resultStatus.setMessage("Channel set to "+ value);
                    televisionObject.setChannel(value);
                    System.out.println("Television " + id + " channel set to " + value);
                    return resultStatus;
                } else {
                    throw new CustomException("Set Channel","Television id: " + id +". Channel is out of range. You can set a value only in range 0 to 500!");
                }
            }
        } else {
            throw new CustomException("Set Channel","Television id: " + id +" has not been found.");
        }
    }
}
