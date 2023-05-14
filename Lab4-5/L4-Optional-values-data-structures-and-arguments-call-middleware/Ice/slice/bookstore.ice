module Bookstore
{
    class BookOptional
    {
        string title;
        string author;
        optional(1) int year;
    };

    class BookNoOptional
    {
        string title;
        string author;
        int year;
    };

    interface BookstoreService
    {
        bool addBookOptional(string title, string author, optional(1) int year);
        bool addBookNoOptional(string title, string author, int year);
        bool addBookStructOptional(BookOptional book);
        bool addBookStructNoOptional(BookNoOptional book);
    };
};

