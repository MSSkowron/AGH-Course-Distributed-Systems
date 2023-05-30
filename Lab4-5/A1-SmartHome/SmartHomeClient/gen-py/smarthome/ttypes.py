#
# Autogenerated by Thrift Compiler (0.18.1)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TFrozenDict, TException, TApplicationException
from thrift.protocol.TProtocol import TProtocolException
from thrift.TRecursive import fix_spec

import sys

from thrift.transport import TTransport
all_structs = []


class DeviceType(object):
    FRIDGE = 0
    LAMP = 1
    VACUUM_CLEANER = 2
    TELEVISION = 3

    _VALUES_TO_NAMES = {
        0: "FRIDGE",
        1: "LAMP",
        2: "VACUUM_CLEANER",
        3: "TELEVISION",
    }

    _NAMES_TO_VALUES = {
        "FRIDGE": 0,
        "LAMP": 1,
        "VACUUM_CLEANER": 2,
        "TELEVISION": 3,
    }


class DeviceState(object):
    ON = 0
    OFF = 1

    _VALUES_TO_NAMES = {
        0: "ON",
        1: "OFF",
    }

    _NAMES_TO_VALUES = {
        "ON": 0,
        "OFF": 1,
    }


class LightColor(object):
    BLUE = 0
    GREEN = 1
    YELLOW = 2
    RED = 3

    _VALUES_TO_NAMES = {
        0: "BLUE",
        1: "GREEN",
        2: "YELLOW",
        3: "RED",
    }

    _NAMES_TO_VALUES = {
        "BLUE": 0,
        "GREEN": 1,
        "YELLOW": 2,
        "RED": 3,
    }


class Capacity(object):
    EMPTY = 0
    HALF_EMPTY = 1
    FULL = 2

    _VALUES_TO_NAMES = {
        0: "EMPTY",
        1: "HALF_EMPTY",
        2: "FULL",
    }

    _NAMES_TO_VALUES = {
        "EMPTY": 0,
        "HALF_EMPTY": 1,
        "FULL": 2,
    }


class TelevisionType(object):
    SMART = 0
    NON_SMART = 1

    _VALUES_TO_NAMES = {
        0: "SMART",
        1: "NON_SMART",
    }

    _NAMES_TO_VALUES = {
        "SMART": 0,
        "NON_SMART": 1,
    }


class CustomException(TException):
    """
    Attributes:
     - operation
     - message

    """


    def __init__(self, operation=None, message=None,):
        super(CustomException, self).__setattr__('operation', operation)
        super(CustomException, self).__setattr__('message', message)

    def __setattr__(self, *args):
        raise TypeError("can't modify immutable instance")

    def __delattr__(self, *args):
        raise TypeError("can't modify immutable instance")

    def __hash__(self):
        return hash(self.__class__) ^ hash((self.operation, self.message, ))

    @classmethod
    def read(cls, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and cls.thrift_spec is not None:
            return iprot._fast_decode(None, iprot, [cls, cls.thrift_spec])
        iprot.readStructBegin()
        operation = None
        message = None
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    operation = iprot.readString().decode('utf-8', errors='replace') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    message = iprot.readString().decode('utf-8', errors='replace') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()
        return cls(
            operation=operation,
            message=message,
        )

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('CustomException')
        if self.operation is not None:
            oprot.writeFieldBegin('operation', TType.STRING, 1)
            oprot.writeString(self.operation.encode('utf-8') if sys.version_info[0] == 2 else self.operation)
            oprot.writeFieldEnd()
        if self.message is not None:
            oprot.writeFieldBegin('message', TType.STRING, 2)
            oprot.writeString(self.message.encode('utf-8') if sys.version_info[0] == 2 else self.message)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __str__(self):
        return repr(self)

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class ResultStatus(object):
    """
    Attributes:
     - message

    """


    def __init__(self, message=None,):
        self.message = message

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    self.message = iprot.readString().decode('utf-8', errors='replace') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('ResultStatus')
        if self.message is not None:
            oprot.writeFieldBegin('message', TType.STRING, 1)
            oprot.writeString(self.message.encode('utf-8') if sys.version_info[0] == 2 else self.message)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class DeviceObject(object):
    """
    Attributes:
     - id
     - type
     - state

    """


    def __init__(self, id=None, type=None, state=None,):
        self.id = id
        self.type = type
        self.state = state

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    self.id = iprot.readString().decode('utf-8', errors='replace') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I32:
                    self.type = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.I32:
                    self.state = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('DeviceObject')
        if self.id is not None:
            oprot.writeFieldBegin('id', TType.STRING, 1)
            oprot.writeString(self.id.encode('utf-8') if sys.version_info[0] == 2 else self.id)
            oprot.writeFieldEnd()
        if self.type is not None:
            oprot.writeFieldBegin('type', TType.I32, 2)
            oprot.writeI32(self.type)
            oprot.writeFieldEnd()
        if self.state is not None:
            oprot.writeFieldBegin('state', TType.I32, 3)
            oprot.writeI32(self.state)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class FridgeObject(object):
    """
    Attributes:
     - device
     - temperature

    """


    def __init__(self, device=None, temperature=None,):
        self.device = device
        self.temperature = temperature

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.device = DeviceObject()
                    self.device.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I32:
                    self.temperature = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('FridgeObject')
        if self.device is not None:
            oprot.writeFieldBegin('device', TType.STRUCT, 1)
            self.device.write(oprot)
            oprot.writeFieldEnd()
        if self.temperature is not None:
            oprot.writeFieldBegin('temperature', TType.I32, 2)
            oprot.writeI32(self.temperature)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class LampObject(object):
    """
    Attributes:
     - device
     - color
     - intensity

    """


    def __init__(self, device=None, color=None, intensity=None,):
        self.device = device
        self.color = color
        self.intensity = intensity

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.device = DeviceObject()
                    self.device.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I32:
                    self.color = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.I32:
                    self.intensity = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('LampObject')
        if self.device is not None:
            oprot.writeFieldBegin('device', TType.STRUCT, 1)
            self.device.write(oprot)
            oprot.writeFieldEnd()
        if self.color is not None:
            oprot.writeFieldBegin('color', TType.I32, 2)
            oprot.writeI32(self.color)
            oprot.writeFieldEnd()
        if self.intensity is not None:
            oprot.writeFieldBegin('intensity', TType.I32, 3)
            oprot.writeI32(self.intensity)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class VacuumCleanerObject(object):
    """
    Attributes:
     - device
     - capacity
     - battery

    """


    def __init__(self, device=None, capacity=None, battery=None,):
        self.device = device
        self.capacity = capacity
        self.battery = battery

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.device = DeviceObject()
                    self.device.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I32:
                    self.capacity = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.I32:
                    self.battery = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('VacuumCleanerObject')
        if self.device is not None:
            oprot.writeFieldBegin('device', TType.STRUCT, 1)
            self.device.write(oprot)
            oprot.writeFieldEnd()
        if self.capacity is not None:
            oprot.writeFieldBegin('capacity', TType.I32, 2)
            oprot.writeI32(self.capacity)
            oprot.writeFieldEnd()
        if self.battery is not None:
            oprot.writeFieldBegin('battery', TType.I32, 3)
            oprot.writeI32(self.battery)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class TelevisionObject(object):
    """
    Attributes:
     - device
     - type
     - channel

    """


    def __init__(self, device=None, type=None, channel=None,):
        self.device = device
        self.type = type
        self.channel = channel

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.device = DeviceObject()
                    self.device.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I32:
                    self.type = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.I32:
                    self.channel = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('TelevisionObject')
        if self.device is not None:
            oprot.writeFieldBegin('device', TType.STRUCT, 1)
            self.device.write(oprot)
            oprot.writeFieldEnd()
        if self.type is not None:
            oprot.writeFieldBegin('type', TType.I32, 2)
            oprot.writeI32(self.type)
            oprot.writeFieldEnd()
        if self.channel is not None:
            oprot.writeFieldBegin('channel', TType.I32, 3)
            oprot.writeI32(self.channel)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(CustomException)
CustomException.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'operation', 'UTF8', None, ),  # 1
    (2, TType.STRING, 'message', 'UTF8', None, ),  # 2
)
all_structs.append(ResultStatus)
ResultStatus.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'message', 'UTF8', None, ),  # 1
)
all_structs.append(DeviceObject)
DeviceObject.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'id', 'UTF8', None, ),  # 1
    (2, TType.I32, 'type', None, None, ),  # 2
    (3, TType.I32, 'state', None, None, ),  # 3
)
all_structs.append(FridgeObject)
FridgeObject.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'device', [DeviceObject, None], None, ),  # 1
    (2, TType.I32, 'temperature', None, None, ),  # 2
)
all_structs.append(LampObject)
LampObject.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'device', [DeviceObject, None], None, ),  # 1
    (2, TType.I32, 'color', None, None, ),  # 2
    (3, TType.I32, 'intensity', None, None, ),  # 3
)
all_structs.append(VacuumCleanerObject)
VacuumCleanerObject.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'device', [DeviceObject, None], None, ),  # 1
    (2, TType.I32, 'capacity', None, None, ),  # 2
    (3, TType.I32, 'battery', None, None, ),  # 3
)
all_structs.append(TelevisionObject)
TelevisionObject.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'device', [DeviceObject, None], None, ),  # 1
    (2, TType.I32, 'type', None, None, ),  # 2
    (3, TType.I32, 'channel', None, None, ),  # 3
)
fix_spec(all_structs)
del all_structs
