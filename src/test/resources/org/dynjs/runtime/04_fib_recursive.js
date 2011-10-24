function fib(n) {
    return n < 2 ? n : fib(n - 1) + fib(n - 2);
}

var result = fib(6);