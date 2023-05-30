namespace java genjava
namespace py genpython

struct BookOptional {
  1: string title,
  2: string author,
  3: optional i32 year,
}

struct BookNoOptional {
  1: string title,
  2: string author,
  3: i32 year,
}
 
service BookstoreService {
   bool addBookOptional(1:string title, 2: string author, 3: optional i32 year);
   bool addBookNoOptional(1:string title, 2: string author, 3: i32 year);
   bool addBookStructOptional(1: BookOptional book);
   bool addBookStructNoOptional(1: BookNoOptional book);
}
