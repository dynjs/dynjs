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
    var x = new JSAdapter({})
    expect(x.__proto__).toNotBe(null);
    expect(x.__proto__).toBe(Object.prototype);
    x.foo = "foo";
    expect(x.foo).toBe("foo");
    x.bar = function() {
      return "foobar";
    };
    expect(x.bar()).toBe("foobar");
  });
});


