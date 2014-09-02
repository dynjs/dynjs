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
    var __v8Stack = __v8StackGetter(err), __stackStr;

    Object.defineProperty(err, 'stack', {
      enumerable: true,
      configurable: true,
      get: function() {
        if (!__stackStr) {
          __stackStr = __v8Stack.toString();
        }
        return __stackStr;
      },
      set: function(val) {
        __stackStr = val;
      }
    });
  }
})();
