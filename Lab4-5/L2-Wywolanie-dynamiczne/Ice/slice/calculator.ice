
#ifndef CALC_ICE
#define CALC_ICE

module Demo
{
  sequence<long> numbers;

  enum operation { MIN, MAX, AVG };

  exception NoInput {};

  struct A
  {
    short a;
    long b;
    float c;
    string d;
  };

  interface Calc
  {
    long add(int a, int b);
    long subtract(int a, int b);
    void op(A a1, short b1);
    float avg(numbers n) throws NoInput;
  };
};

#endif
