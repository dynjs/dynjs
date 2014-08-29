// JSHint directive: do not warn that Error is read-only //
/* global Error: true */

(function() {

  Error = function Error(msg) {
    this.name = 'Error';
    this.message = msg;
    __captureStackTrace(this);
  };

  Error.prototype.toString = function() {
    return [this.name, this.message].join(': ');
  };

  Error.stackTraceLimit = 10;

  Error.captureStackTrace = __captureStackTrace;

  Error.prepareStackTrace = function(){};

  function __captureStackTrace(err, func) {
    var __javaStack = __v8StackGetter(), __stack = [], __stackStr;

    // ignore the first 3 elements of the stack, since those will be the 
    // Java builtin function, the __captureStackTrace function, and the
    // Error ctor
    for (var i = 3; i < __javaStack.size(); i++) {
      var elem = __javaStack.get(i);
      __stack.push(elem);
    }
    
    Object.defineProperty(err, 'stack', {
      enumerable: true,
      configurable: true,
      get: function() {
        if (!__stackStr) {
          print(">>>>> ERR NAME " + err.name);
          __stackStr = err.name ? err.name : '<unknown>';

          if (err.message) __stackStr = [_stackStr, err.message].join(': ');
          __stackStr += "\n";

          __stackStr = __stack.reduce(function(memo, elem) { 
            return [memo, "  at ", elem, "\n"].join('');
          }, __stackStr);
        }
        return __stackStr;
      },
      set: function(val) {
        __stackStr = val;
      }
    });
  }
})();
