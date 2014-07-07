// Some simple test for the parser

var obj = {
  false: 'a false thing',
  foo: {
    false: 'foo false'
  },
  bar: function() {
         return {
           false: 'bar false'
         };
       }
};

describe("DynJS Parser", function() {
  it("should handle properties named 'false'", function() {
    expect(obj.false).toBe('a false thing');
    expect(obj[false]).toBe('a false thing');
    expect(obj.foo.false).toBe('foo false');
    expect(obj.foo[false]).toBe('foo false');
    var foobar = obj.bar();
    expect(foobar.false).toBe('bar false');
    expect(foobar[false]).toBe('bar false');
    expect(obj.bar().false).toBe('bar false');
  });
});

describe("named function expressions", function () {
  describe("in strict mode", function () {
    "use strict";

    it("should compile in function scope", function () {
      var f = function ident(x) {
        return x;
      };

      expect(f('foo')).toBe('foo');
    });

    it("should compile in block scope", function () {
      if (true) {
        var f = function ident(x) {
          return x;
        };

        expect(f('foo')).toBe('foo');
      }
    });
  });

  describe("in sloppy mode", function () {
    it("should compile in function scope", function () {
      var f = function ident(x) {
        return x;
      };

      expect(f('foo')).toBe('foo');
    });

    it("should compile in block scope", function () {
      if (true) {
        var f = function ident(x) {
          return x;
        };

        expect(f('foo')).toBe('foo');
      }
    });
  });
});

