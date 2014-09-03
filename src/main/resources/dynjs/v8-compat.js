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

  Error.prepareStackTrace = function(err, structuredStackTrace) {
    return structuredStackTrace.toString();
  };

  function __captureStackTrace(err, func) {
    var __v8Stack = __v8StackGetter(err, Error.stackTraceLimit, func), __stackStr;

    Object.defineProperty(err, 'stack', {
      enumerable: true,
      configurable: true,
      get: function() {
        if (!__stackStr) {
          __stackStr = Error.prepareStackTrace(err, __v8Stack);
        }
        return __stackStr;
      }.bind(this),
      set: function(val) {
        __stackStr = val;
      }
    });
  }
})();
