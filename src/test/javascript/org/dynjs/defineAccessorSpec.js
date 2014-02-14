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

describe("Object.create", function() {
  xit("should create an object with a null prototype if null is passed", function() {
    var o = Object.create(null);
    expect(o.prototype).toBe(null);
  });
});

describe("the __proto__ property", function() {
  it("should be Object.prototype for Objects", function() {
    var a = new Object();
    a.__proto__
    validateProto(a, Object.prototype);
    a = {};
    a.__proto__
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

  function validateProto(obj, proto) {
    expect(obj.__proto__).toNotBe(null);
    expect(obj.__proto__).toNotBe(undefined);
    expect(obj.__proto__).toBe(proto);
  }
});

