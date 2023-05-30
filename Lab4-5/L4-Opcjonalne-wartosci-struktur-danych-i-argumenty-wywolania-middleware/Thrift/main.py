import sys

sys.path.append('gen-py')

from genpython import BookstoreService
from genpython.ttypes import BookOptional
from genpython.ttypes import BookNoOptional

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer


class BookstoreServiceHandler:
    def addBookOptional(self, title, string, year):
        print("called addBookOptional")
        print(title)
        print(string)
        print(year)

        return True

    def addBookNoOptional(self, title, string, year):
        print("called addBookNoOptional")
        print(title)
        print(string)
        print(year)

        return True

    def addBookStructOptional(self, book):
        print("called addBookStructOptional")
        print(book)

        return True

    def addBookStructNoOptional(self, book):
        print("called addBookStructNoOptional")
        print(book)

        return True

handler = BookstoreServiceHandler()
processor = BookstoreService.Processor(handler)
transport = TSocket.TServerSocket("127.0.0.2", 9090)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print("Starting thrift server in Python...")
server.serve()
