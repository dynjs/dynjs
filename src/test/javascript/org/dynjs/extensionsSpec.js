/* jshint nonew: true, proto: true  */

describe("defineGetter", function() {
  it("should be an enumerable property", function() {
    var a = {};
    a.__defineGetter__("foo", function() { return 1; });

    var properties = [];
    for(var property in a) {
      properties.push(property);
    }

    expect(properties).toEqual(["foo"]);
  });
});

describe("defineSetter", function() {
  it("should be an enumerable property", function() {
    var a = {};
    a.__defineSetter__("foo", function(value) { a.bar = value; });

    var properties = [];
    for(var property in a) {
      properties.push(property);
    }

    expect(properties).toEqual(["foo"]);
  });
});

describe("Object.defineProperty", function() {
  it("should allow setting a getter function", function() {
    var a = {};
    var b = 'bar';
    Object.defineProperty(a, 'foo', {
      get: function() { return b; },
      set: function() {},
      enumerable: true,
      configurable: true});
    expect(a.foo).toBe(b);
  });
});

describe("Object.create", function() {
  xit("should create an object with a null prototype if null is passed", function() {
    var o = Object.create(null);
    expect(o.prototype).toBe(null);
  });
});

describe("the __proto__ property", function() {
  it("should be Object.prototype for Objects", function() {
    var a = new Object();
    validateProto(a, Object.prototype);
    a = {};
    validateProto(a, Object.prototype);
  });

  it("should be Array.prototype for Array objects", function() {
    var a = new Array();
    validateProto(a, Array.prototype);
    a = [];
    validateProto(a, Array.prototype);
  });

  it("should be Boolean.prototype for Boolean objects", function() {
    var a = new Boolean(true);
    validateProto(a, Boolean.prototype);
    a = true;
    validateProto(a, Boolean.prototype);
  });

  it("should be Date.prototype for Date objects", function() {
    var a = new Date();
    validateProto(a, Date.prototype);
  });

  it("should be Number.prototype for Number objects", function() {
    var a = new Number();
    validateProto(a, Number.prototype);
    a = 100;
    validateProto(a, Number.prototype);
  });

  it("should be String.prototype for String objects", function() {
    var a = new String();
    validateProto(a, String.prototype);
    a = "Foobar!";
    validateProto(a, String.prototype);
  });

  it("should be Function.prototype for functions", function() {
    var a = function() { return "FOO"; };
    validateProto(a, Function.__proto__);
  });

  it("should be Foo.prototype for Foos", function() {
    var Foo = function() {
    };
    var a = new Foo();
    expect(Object.getPrototypeOf(a)).toBe(Foo.prototype);
    validateProto(a, Foo.prototype);
  });

  it("should be undefined for an object created with Object.create(null)", function() {
    var a = Object.create(null);
    expect(a.__proto__).toBe(undefined);
    expect(Object.getPrototypeOf(a)).toBe(null);
  });

  it("should allow shadowing when __proto__ is undefined", function() {
    var a = Object.create(null);
    a.__proto__ = 17;
    expect(a.__proto__).toBe(17);
    expect(Object.getPrototypeOf(a)).toBe(null);
  });

  it("should allow shadowing when __proto__ is defined", function() {
    var a = {};
    Object.defineProperty(a, "__proto__",
      { value:17, writable: true, configurable: true, enumerable: true });
    expect(a.__proto__).toBe(17);
    expect(Object.getPrototypeOf(a)).toBe(Object.prototype);
  });

  it("should throw a TypeError when setting __proto__ if the object is not extensible",
      function() {
        var a = {};
        Object.preventExtensions(a);
        try {
          a.__proto__ = undefined;
          expect(false).toBe(true);
        } catch(e) {
          // success
          expect(a.__proto__).toBe(Object.prototype);
        }
  });

  it("should not treat __proto__ as special when parsing JSON", function() {
    var x = JSON.parse('{"__proto__":[]}');
    expect(Object.getPrototypeOf(x)).toBe(Object.prototype);
    expect(Array.isArray(x.__proto__)).toBe(true);
    expect(x.hasOwnProperty('__proto__')).toBe(true);
  });

  it("should update prototype for object literals with __proto__", function() {
    var proto = { foo: 'bar' };
    var a = { __proto__: proto };
    expect(Object.getPrototypeOf(a)).toBe(proto);
  });

  xit("should allow Object.prototype.__proto__ to be set", function() {
    // TODO: This test should pass, and replicating it in the REPL
    // shows that it works as expected. However, the Jasmine test
    // runner hangs when we set Object.prototype.__proto__ to anything
    // unexpected, even if we repair the damage later.
    var a = {};
    Object.prototype.__proto__ = {
          foo: function() {print("HERE"); return 'bar';},
        __proto__: null
    };
    Object.prototype.__proto__ = null;
    expect(a.foo()).toBe('bar');
  });

  function validateProto(obj, proto) {
    expect(obj.__proto__).toNotBe(null);
    expect(obj.__proto__).toNotBe(undefined);
    expect(obj.__proto__).toBe(proto);
  }
});


describe("JSAdapter", function() {
  it("should provide a __get__ override", function() {
    var x = new JSAdapter({
      __get__: function(name) { return name; }
    });
    expect(x.foo).toBe("foo");
    expect(x['bar']).toBe("bar");
  });

  it("should provide a __set__ override", function() {
    var z = "foo";
    var x = new JSAdapter({
      __set__: function(name, value) { z = value; }
    });
    x.foo = "bar";
    expect(z).toBe("bar");
  });

  it("should behave like a regular JS object", function() {
    var x = new JSAdapter({});
    expect(x.__proto__).toNotBe(null);
    expect(x.__proto__).toBe(Object.prototype);
    x.foo = "foo";
    expect(x.foo).toBe("foo");
    x.bar = function() {
      return "foobar";
    };
    expect(x.bar()).toBe("foobar");
  });

  it("should allow for overrides", function() {
    var x = new JSAdapter(
      { tacos: 'big' },
      {
        __get__: function(name) { return name; }
      }
    );

    expect(x.foo).toBe("foo");
    expect(x.tacos).toBe("big");
    x.tacos = 'small';
    expect(x.tacos).toBe("small");
  });

  it("should allow for a prototype", function() {
    var p = {
      tacos: 'big',
      burgers: function() {
        return 'double';
      }
    };

    var x = new JSAdapter(
      p,
      { cheese: 'swiss',
        data: {}},
      {
        __get__: function(name) {
          return this.data[name];
        },
        __set__: function(name, value) {
          this.data[name] = value;
        }
      }
    );

    expect(x.foo).toBe(undefined);
    x.foo = 'bar';
    expect(x.foo).toBe('bar');
    expect(x.data.foo).toBe('bar');
    expect(x.tacos).toBe("big");
    expect(x.cheese).toBe("swiss");
    expect(x.burgers()).toBe("double");

  });
});

describe("V8 Error API", function() {
  it("should have an Error.stackTraceLimit property defaulting to 10", function() {
    expect(Error.stackTraceLimit).toBe(10);
  });

  it("should have an Error.prepareStackTrace function", function() {
    expect(typeof Error.prepareStackTrace).toBe('function');
  });

  describe("Error.captureStackTrace", function() {
    it("should be a function", function() {
      expect(typeof Error.captureStackTrace).toBe('function');
    });

    it("should accept two parameters", function() {
      expect(Error.captureStackTrace.length).toBe(2);
    });

    it("should add a writable 'stack' property to the given object", function() {
      var err = {};
      Error.captureStackTrace(err);
      expect(err.stack).not.toBeFalsy();
      err.stack = 'foobar';
      expect(err.stack).toBe('foobar');
    });

    it("should cap the stack at constructorOpt if provided", function() {
      var e = {};
      var expected = "<unknown>\n  at foo";
      function foo() {
        function bar() {
          function foobar() {
            Error.captureStackTrace(e, bar);
          }
          foobar();
        }
        bar();
      }
      foo();
      expect(e.stack).toMatch(expected);
    });

    it("should respect Error.stackTraceLimit", function() {
      var e = {};
      function foo() {
        function bar() {
          function foobar() {
            Error.captureStackTrace(e);
          }
          foobar();
        }
        bar();
      }
      foo();
      // This is 12 because the first line is the error message
      // and there's an additional \n at the end of the stack
      expect(e.stack.split("\n").length).toBe(12);
    });

    it("should allow Error.stackTraceLimit to be customized", function() {
      var e = {};
      Error.stackTraceLimit = 3;
      function foo() {
        function bar() {
          function foobar() {
            Error.captureStackTrace(e);
          }
          foobar();
        }
        bar();
      }
      foo();
      // This is 5 because the first line is the error message
      // and there's an additional \n at the end of the stack
      expect(e.stack.split("\n").length).toBe(5);
      Error.stackTraceLimit = 10;
    });
  });

  describe("Error.prepareStackTrace", function() {
    it("should be a function that takes two parameters", function() {
      expect(typeof Error.prepareStackTrace).toBe('function');
      expect(Error.prepareStackTrace.length).toBe(2);
    });

    it("should be customizable", function() {
      var e = {}, __prepareStackTrace = Error.prepareStackTrace;
      Error.prepareStackTrace = function(e,s) {
        return 'pumpkins';
      };
      Error.captureStackTrace(e);
      expect(e.stack).toBe('pumpkins');
      Error.prepareStackTrace = __prepareStackTrace;
    });
  });

  describe("CallSite objects in a structured stack trace", function() {
    // make err.stack return the structuredStackTrace instead
    // of converting it to a string.
    var __prepareStackTrace = Error.prepareStackTrace;
    beforeEach(function() {
      Error.prepareStackTrace = function(e,s) { 
        return s; 
      };
    });
    afterEach(function() {
      Error.prepareStackTrace = __prepareStackTrace;
    });

    function errorGenerator(str) {
      return new Error(str);
    }

    it("should have a getFunctionName property", function() {
      var e = errorGenerator();
      expect(e.stack[0].getFunctionName()).toBe('errorGenerator');
    });

    it("should have a getFunction property", function() {
      var e = errorGenerator();
      expect(e.stack[0].getFunction()).toBe(errorGenerator);
    });

    it("should have a getThis property", function() {
      var e = errorGenerator();
      expect(typeof e.stack[0].getThis()).toBe('object');
    });

    it("should have a getLineNumber property", function() {
      var e = errorGenerator();
      expect(e.stack[0].getLineNumber()).toBe(361);
    });

    it("should have a getTypeName property", function() {
      function Thing() {
      }
    
      Thing.prototype.createError = function() {
        return new Error("Created by a Thing's createError function property");
      };

      var thing = new Thing();
      var error = thing.createError();
      expect(error.stack[0].getTypeName()).toBe('Thing');

      error = (function f(){return new Error('created by a func');})();
      expect(error.stack[0].getTypeName()).toBe('Function');
    });

    it("should have a getMethodName property", function() {
      function Thing() {
      }
    
      Thing.prototype.createError = function() {
        return new Error("Created by a Thing's createError function property");
      };

      var thing = new Thing();
      var error = thing.createError();
      expect(error.stack[0].getMethodName()).toBe('createError');
    });

    it("should have a getColumnNumber property", function() {
      var error = errorGenerator();
      expect(error.stack[0].getColumnNumber()).toBe(7);
    });

    it("should have an isNative property", function() {
      var error = errorGenerator();
      expect(error.stack[0].isNative()).toBe(false);

      try {
        require('not-exist.js');
      } catch(e) {
        expect(e.stack[0].isNative()).toBe(false);
        expect(e.stack[1].isNative()).toBe(true);
      }
    });

  });
});
