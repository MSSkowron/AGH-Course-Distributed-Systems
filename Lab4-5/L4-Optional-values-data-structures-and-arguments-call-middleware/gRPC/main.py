from concurrent import futures
import grpc
import bookstore_pb2
import bookstore_pb2_grpc

class BookstoreServicer(bookstore_pb2_grpc.BookstoreServiceServicer):
    def AddBookOptional(self, request, context):
        print("called AddBookOptional")
        print(request)
        response = bookstore_pb2.AddBookResponse(response=True)
        return response

    def AddBookNoOptional(self, request, context):
        print("called AddBookNoOptional")
        print(request)
        response = bookstore_pb2.AddBookResponse(response=True)
        return response

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    bookstore_pb2_grpc.add_BookstoreServiceServicer_to_server(BookstoreServicer(), server)
    server.add_insecure_port('127.0.0.2:9090')
    server.start()
    print("Listening on 127.0.0.2:9090...")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()