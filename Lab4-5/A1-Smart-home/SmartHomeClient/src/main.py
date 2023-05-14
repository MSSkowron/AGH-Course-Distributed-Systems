import sys
import time
import os

from thrift.transport import TTransport
from thrift.transport import TSocket
from thrift.protocol import TBinaryProtocol
from thrift.Thrift import TApplicationException
from thrift.protocol.TMultiplexedProtocol import TMultiplexedProtocol
from thrift.transport.TTransport import TTransportException

sys.path.append('gen-py')

from smarthome import Fridge, Device, Lamp, VacuumCleaner, Television
from smarthome.ttypes import CustomException


def connect_to_home_server(home_name: str, address: str, port: int) -> None:
    trans = TSocket.TSocket(address, port)
    trans = TTransport.TBufferedTransport(trans)
    protocol = TBinaryProtocol.TBinaryProtocol(trans)

    device = Device.Client(TMultiplexedProtocol(protocol, "Device"))
    fridge = Fridge.Client(TMultiplexedProtocol(protocol, "Fridge"))
    lamp = Lamp.Client(TMultiplexedProtocol(protocol, "Lamp"))
    vacuum_cleaner = VacuumCleaner.Client(TMultiplexedProtocol(protocol, "VacuumCleaner"))
    television = Television.Client(TMultiplexedProtocol(protocol, "Television"))

    print(f"Trying to connect to {home_name} ({address}:{port})...")
    time.sleep(1)

    try:
        trans.open()
        print(f"Connected successfully to {home_name} ({address}:{port})!")
        time.sleep(1)

        while True:
            os.system("cls")

            print(f"-- SMART HOME MANAGER [{home_name} - {address}:{port}]")
            print("-- Choose number to run a command")
            print("1: List all devices")
            print("2: Run a fridge command")
            print("3: Run a lamp command")
            print("4: Run a vacuum cleaner command")
            print("5: Run a television command")
            print("6: Go to menu")
            print("7: Exit")

            command_number = input()
            print()

            if command_number == '1':
                for dev in device.listAllDevices():
                    print(dev)

            elif command_number == '2':
                print("-- Run a fridge command")
                print("-- Please use the format <command_number>#<arg1>|<arg2>|...|<argN>")
                print("1: Get state          | arg1 = id")
                print("2: Turn on            | arg1 = id")
                print("3: Turn off           | arg1 = id")
                print("4: Set temperature    | arg1 = id | arg2 = value [INTEGER from -10 to 10]")
                print("5: Get temperature    | arg1 = id")

                try:
                    target = input()
                    fridge_command = target.split("#")
                    fridge_command_number, fridge_command_arguments = fridge_command[0], fridge_command[1]

                    if fridge_command_number == '1':
                        print(fridge.getState(fridge_command_arguments))
                    elif fridge_command_number == '2':
                        print(fridge.turnOn(fridge_command_arguments))
                    elif fridge_command_number == '3':
                        print(fridge.turnOff(fridge_command_arguments))
                    elif fridge_command_number == '4':
                        fridge_id, fridge_value = fridge_command_arguments.split('|')
                        fridge_value = int(fridge_value)
                        print(fridge.setTemperature(fridge_id, fridge_value))
                    elif fridge_command_number == '5':
                        print(fridge.getTemperature(fridge_command_arguments))
                    else:
                        print("Invalid fridge command number! Try again...")

                except (IndexError, ValueError):
                    print("Invalid input format. Please use the format <command_number>#<arg1>|<arg2>|...|<argN>")
                except CustomException as customException:
                    print("An error occurred: " + str(customException))
                except TApplicationException as tApplicationException:
                    print("An error occurred: " + str(tApplicationException))

            elif command_number == '3':
                print("-- Run a lamp command")
                print("-- Run a command like this -> <command_number>#<arg1>|<arg2>|...|<argN>")
                print("1: Get state         | arg1 = id")
                print("2: Turn on           | arg1 = id")
                print("3: Turn off          | arg1 = id")
                print("4: Set color         | arg1 = id | arg2 = color [0=BLUE, 1=GREEN, 2=YELLOW, 3=RED]")
                print("5: Get color         | arg1 = id")
                print("6: Set intensity     | arg1 = id | arg2 = value [INTEGER from 0 to 100]")
                print("7: Get intensity     | arg1 = id")

                try:
                    target = input()
                    lamp_command = target.split("#")
                    lamp_command_number, lamp_command_arguments = lamp_command[0], lamp_command[1]

                    if lamp_command_number == '1':
                        print(lamp.getState(lamp_command_arguments))
                    elif lamp_command_number == '2':
                        print(lamp.turnOn(lamp_command_arguments))
                    elif lamp_command_number == '3':
                        print(lamp.turnOff(lamp_command_arguments))
                    elif lamp_command_number == '4':
                        args = lamp_command_arguments.split("|")
                        lamp_id = args[0]
                        lamp_color = int(args[1])
                        if lamp_color == 0:
                            print(lamp.setColor(lamp_id, Lamp.LightColor.BLUE))
                        elif lamp_color == 1:
                            print(lamp.setColor(lamp_id, Lamp.LightColor.GREEN))
                        elif lamp_color == 2:
                            print(lamp.setColor(lamp_id, Lamp.LightColor.YELLOW))
                        elif lamp_color == 3:
                            print(lamp.setColor(lamp_id, Lamp.LightColor.RED))
                        else:
                            print("Invalid lamp color number! Try again...")
                    elif lamp_command_number == '5':
                        print(lamp.getColor(lamp_command_arguments))
                    elif lamp_command_number == '6':
                        args = lamp_command_arguments.split("|")
                        lamp_id = args[0]
                        lamp_intensity = int(args[1])
                        print(lamp.setIntensity(lamp_id, lamp_intensity))
                    elif lamp_command_number == '7':
                        print(lamp.getIntensity(lamp_command_arguments))
                    else:
                        print("Invalid lamp command number! Try again...")

                except (IndexError, ValueError):
                    print("Invalid input format. Please use the format <command_number>#<arg1>|<arg2>|...|<argN>")
                except CustomException as customException:
                    print("An error occurred: " + str(customException))
                except TApplicationException as tApplicationException:
                    print("An error occurred: " + str(tApplicationException))

            elif command_number == '4':
                print("-- Run a vacuum cleaner command")
                print("-- Run a command like this -> <command_number>#<arg1>|<arg2>|...|<argN>")
                print("1: Get state             | arg1 = id")
                print("2: Turn on               | arg1 = id")
                print("3: Turn off              | arg1 = id")
                print("4: Get battery level     | arg1 = id")
                print("5: Charge up             | arg1 = id")
                print("6: Get capacity          | arg1 = id")
                print("7: Empty                 | arg1 = id")

                try:
                    target = input()
                    vacuum_cleaner_command = target.split("#")
                    vacuum_cleaner_command_number, vacuum_cleaner_command_arguments = vacuum_cleaner_command[0], \
                                                                                      vacuum_cleaner_command[1]

                    if vacuum_cleaner_command_number == '1':
                        print(vacuum_cleaner.getState(vacuum_cleaner_command_arguments))
                    elif vacuum_cleaner_command_number == '2':
                        print(vacuum_cleaner.turnOn(vacuum_cleaner_command_arguments))
                    elif vacuum_cleaner_command_number == '3':
                        print(vacuum_cleaner.turnOff(vacuum_cleaner_command_arguments))
                    elif vacuum_cleaner_command_number == '4':
                        print(vacuum_cleaner.getBattery(vacuum_cleaner_command_arguments))
                    elif vacuum_cleaner_command_number == '5':
                        print(vacuum_cleaner.chargeUp(vacuum_cleaner_command_arguments))
                    elif vacuum_cleaner_command_number == '6':
                        print(vacuum_cleaner.getCapacity(vacuum_cleaner_command_arguments))
                    elif vacuum_cleaner_command_number == '7':
                        print(vacuum_cleaner.empty(vacuum_cleaner_command_arguments))
                    else:
                        print("Invalid vacuum cleaner command number! Try again...")

                except (IndexError, ValueError):
                    print("Invalid input format. Please use the format <command_number>#<arg1>|<arg2>|...|<argN>")
                except CustomException as customException:
                    print("An error occurred: " + str(customException))
                except TApplicationException as tApplicationException:
                    print("An error occurred: " + str(tApplicationException))

            elif command_number == '5':
                print("-- Run a television command")
                print("-- Run a command like this -> <command_number>#<arg1>|<arg2>|...|<argN>")

                print("1: Get state             | arg1 = id")
                print("2: Turn on               | arg1 = id")
                print("3: Turn off              | arg1 = id")
                print("4: Set channel           | arg1 = id | arg2 = channel [INTEGER from 0 to 500]")
                print("5: Get channel           | arg1 = id")

                try:
                    target = input()
                    television_command = target.split("#")
                    television_command_number, television_command_arguments = television_command[0], television_command[1]

                    if television_command_number == '1':
                        print(television.getState(television_command_arguments))
                    elif television_command_number == '2':
                        print(television.turnOn(television_command_arguments))
                    elif television_command_number == '3':
                        print(television.turnOff(television_command_arguments))
                    elif television_command_number == '4':
                        args = television_command_arguments.split("|")
                        television_id = args[0]
                        television_channel = int(args[1])
                        print(television.setChannel(television_id, television_channel))
                    elif television_command_number == '5':
                        print(television.getChannel(television_command_arguments))
                    else:
                        print("Invalid television command number! Try again...")

                except (IndexError, ValueError):
                    print("Invalid input. Please use the format <command_number>#<arg1>|<arg2>|...|<argN>")
                except CustomException as customException:
                    print("An error occurred: " + str(customException))
                except TApplicationException as tApplicationException:
                    print("An error occurred: " + str(tApplicationException))

            elif command_number == '6':
                trans.close()
                break

            elif command_number == '7':
                trans.close()
                sys.exit()

            else:
                print("Invalid command number! Try again...")

            print()
            print("Press any key to continue...")
            input()

        main()

    except TTransportException as tTransportException:
        print("There was a problem with the network communication with the home server.")
        print("Error: " + str(tTransportException))
        print("Please check your network connection and try again.")
        print("Do you want to try to reconnect? Type 'Y' to try again or 'N' to go back to the menu.")

        target = input()
        if target == 'Y':
            connect_to_home_server(home_name, address, port)
        elif target == 'N':
            main()
        else:
            print("Invalid command!")
            print()
            print("Press any key to continue...")
            input()
            main()

    except ConnectionResetError as connectionResetError:
        print("Sorry, there was a problem with the network communication with the home server.")
        print("Error: " + str(connectionResetError))
        print("The connection has been reset. Please check your network connection and try again later.")
        print("Do you want to try to reconnect? Type 'Y' to try again or 'N' to go back to the menu.")

        trans.close()

        target = input()
        if target == 'Y':
            connect_to_home_server(home_name, address, port)
        elif target == 'N':
            main()
        else:
            print("Invalid command!")
            print()
            print("Press any key to continue...")
            input()
            main()


def main():
    os.system("cls")

    print("-- SMART HOME MANAGER")
    print("-- Choose home to connect")
    print("1: Home 1")
    print("2: Home 2")
    print("3: Exit")

    home_number = input()

    if home_number == '1':
        connect_to_home_server("Home 1", "localhost", 9010)
    elif home_number == '2':
        connect_to_home_server("Home 2", "localhost", 9020)
    elif home_number == '3':
        os.system("cls")
        print("Thank you for using SMART HOME MANAGER!")
        sys.exit()
    else:
        print("Invalid argument! Try again...")
        print()
        print("Press any key to continue...")
        input()
        main()


main()
