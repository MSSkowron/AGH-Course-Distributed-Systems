package sr.thrift.server.DeviceHandlers;

import generated.rpc.*;
import org.apache.thrift.TException;
import sr.thrift.server.DeviceStore.DeviceStore;

public class VacuumCleanerHandler extends DeviceHandler implements VacuumCleaner.Iface {

    public VacuumCleanerHandler(DeviceStore container) {
        super(container);
    }

    @Override
    public ResultStatus getBattery(String id) throws TException {
        System.out.println("Called GET BATTERY id: " + id);

        VacuumCleanerObject vacuumCleanerObject = deviceStore.getVacuumCleanerById(id);
        if(vacuumCleanerObject != null) {
            ResultStatus resultStatus = new ResultStatus();
            resultStatus.setMessage("Vacuum cleaner "+ "| id: " + id + " | Battery: " + vacuumCleanerObject.getBattery() + "%");
            return resultStatus;
        } else {
            throw new CustomException("Get Battery","Vacuum cleaner id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus chargeUp(String id) throws TException {
        System.out.println("Called CHARGE UP id: " + id);

        VacuumCleanerObject vacuumCleanerObject = deviceStore.getVacuumCleanerById(id);
        if(vacuumCleanerObject != null) {
            ResultStatus resultStatus = new ResultStatus();
            resultStatus.setMessage("Vacuum cleaner "+ "| id: " + id + " | Charged up to 100%.");
            vacuumCleanerObject.setBattery(100);
            System.out.println("Vacuum cleaner " + id + " has been charged up");
            return resultStatus;
        } else {
            throw new CustomException("Check Battery","Vacuum cleaner id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus getCapacity(String id) throws TException {
        System.out.println("Called GET CAPACITY id: " + id);

        VacuumCleanerObject vacuumCleaner = deviceStore.getVacuumCleanerById(id);
        String containerState;
        if(vacuumCleaner != null){
            switch (vacuumCleaner.getCapacity().getValue()){
                case 0:
                    containerState="Empty";
                    break;
                case 1:
                    containerState="Half Empty";
                    break;
                case 2:
                    containerState="Full";
                    break;
                default:
                    containerState="Unknown";
                    break;
            }

            ResultStatus resultStatus = new ResultStatus();
            resultStatus.setMessage("Vacuum cleaner "+ "| id: " + id + " | Capacity: " + containerState);
            return resultStatus;
        } else {
            throw new CustomException("Check Capacity","Vacuum cleaner id: " + id +" has not been found.");
        }
    }

    @Override
    public ResultStatus empty(String id) throws TException {
        System.out.println("Called EMPTY id: " + id);

        VacuumCleanerObject vacuumCleanerObject = deviceStore.getVacuumCleanerById(id);
        if(vacuumCleanerObject != null) {
            ResultStatus resultStatus = new ResultStatus();
            resultStatus.setMessage("Vacuum cleaner "+ "| id: " + id + " | Emptied.");
            vacuumCleanerObject.setCapacity(Capacity.EMPTY);
            System.out.println("Vacuum cleaner " + id + " has been emptied");
            return resultStatus;
        } else {
            throw new CustomException("Check Battery","Vacuum cleaner id: " + id +" has not been found.");
        }
    }
}