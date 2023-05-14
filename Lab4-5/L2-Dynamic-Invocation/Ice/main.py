import sys, Ice

Ice.loadSlice('slice/calculator.ice')
import Demo

class CalcI(Demo.Calc):
    def add(self, a, b, current=None):
        print("add called with {} and {}".format(a, b))
              
        return a+b

    def subtract(self, a, b, current=None):
        print("substract called with {} and {}".format(a, b))
                  
        return a-b

    def op(self, a1, b1, current=None):
        print("op called with {} and {}".format(a1, b1))

        return

    def avg(self, n, current=None):
        print("avg called with {}".format(n))

        if not n:
            raise Demo.NoInput()

        return sum(n) / len(n)

with Ice.initialize(sys.argv) as communicator:
    adapter = communicator.createObjectAdapterWithEndpoints("Adapter", "tcp -h localhost -p 10000 -z : udp -h localhost -p 10000 -z")

    servant = CalcI()

    adapter.add(servant, communicator.stringToIdentity("calc"))

    adapter.activate()

    print("listening on port 10000...")

    communicator.waitForShutdown()
