// JSHint directive: do not warn that Error is read-only //
/* global Error: true */

(function() {

  Error = function Error(msg) {
    this.name = 'Error';
    this.message = msg;
    __captureStackTrace(this, Error);
  };

  Error.prototype.toString = function() {
    return [this.name, this.message].join(': ');
  };

  Error.stackTraceLimit = 10;

  Error.captureStackTrace = __captureStackTrace;

  Error.prepareStackTrace = function(err, structuredStackTrace) {
    var str = (err.name) ? err.name : '<unknown>';
    if (err.message) str += ': ' + err.message;
    str += "\n";

    if (structuredStackTrace) {
      for(var i = 0; i < structuredStackTrace.length; i++) {
        str += "  at " + structuredStackTrace[i].toString() + "\n";
      }
    }
    return str;
  };

  function __captureStackTrace(err, func) {
    var __v8Stack = __v8StackGetter(err, Error.stackTraceLimit, func), __stackStr;

    Object.defineProperty(err, 'stack', {
      enumerable: true,
      configurable: true,
      get: function() {
        if (!__stackStr) {
          var jstack = __v8Stack.getStackArray(), stack = [];
          for(var i=0; i<jstack.length; i++) {
            // TODO: Convert the stack objects into JS CallSite objects
            stack.push(jstack[i]);
          }

          __stackStr = Error.prepareStackTrace(err, stack);
        }
        return __stackStr;
      }.bind(this),
      set: function(val) {
        __stackStr = val;
      }
    });
  }
})();
