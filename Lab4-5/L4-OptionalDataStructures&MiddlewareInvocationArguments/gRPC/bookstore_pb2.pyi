from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class AddBookNoOptionalRequest(_message.Message):
    __slots__ = ["author", "title", "year"]
    AUTHOR_FIELD_NUMBER: _ClassVar[int]
    TITLE_FIELD_NUMBER: _ClassVar[int]
    YEAR_FIELD_NUMBER: _ClassVar[int]
    author: str
    title: str
    year: int
    def __init__(self, title: _Optional[str] = ..., author: _Optional[str] = ..., year: _Optional[int] = ...) -> None: ...

class AddBookOptionalRequest(_message.Message):
    __slots__ = ["author", "title", "year"]
    AUTHOR_FIELD_NUMBER: _ClassVar[int]
    TITLE_FIELD_NUMBER: _ClassVar[int]
    YEAR_FIELD_NUMBER: _ClassVar[int]
    author: str
    title: str
    year: int
    def __init__(self, title: _Optional[str] = ..., author: _Optional[str] = ..., year: _Optional[int] = ...) -> None: ...

class AddBookResponse(_message.Message):
    __slots__ = ["response"]
    RESPONSE_FIELD_NUMBER: _ClassVar[int]
    response: bool
    def __init__(self, response: bool = ...) -> None: ...
