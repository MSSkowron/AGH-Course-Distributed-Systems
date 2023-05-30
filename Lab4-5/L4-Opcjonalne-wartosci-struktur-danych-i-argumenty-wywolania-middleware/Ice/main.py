import sys, Ice

Ice.loadSlice('slice/bookstore.ice')
import Bookstore


class BookstoreServiceI(Bookstore.BookstoreService):
    def addBookOptional(self, title, author, year, current=None):
        print("called addBookOptional")
        if(year):
            print(title + author + str(year))
        else:
            print(title + author)

        return True

    def addBookNoOptional(self, title, author, year, current=None):
        print("called addBookNoOptional")
        print(title + author + str(year))

        return True
    def addBookStructOptional(self, book, current=None):
        print("called addBookStructOptional")
        print(book)

        return True

    def addBookStructNoOptional(self, book, current=None):
        print("called addBookStructNoOptional")
        print(book)

        return True

with Ice.initialize(sys.argv) as communicator:
    adapter = communicator.createObjectAdapterWithEndpoints("Adapter", "tcp -h 127.0.0.2 -p 10000 -z : udp -h localhost -p 10000 -z")

    servant = BookstoreServiceI()

    adapter.add(servant, communicator.stringToIdentity("bookstore"))

    adapter.activate()

    print("listening on port 10000...")

    communicator.waitForShutdown()
