describe("function scope", function() {
  it("should work", function() {

    var f1 = function(array, func) {
      array[0] = 'foo';
      func(array);
    }
    var f2 = function(arr2) {
      expect(arr2[0]).toEqual('foo');
    }

    f1(['morning'], f2);
  });
});

describe("named function expressions", function() {
  it("should not define identifier in parent scope", function() {
    var f = function ident(x) {
      return x;
    };

    expect(function() { ident }).toThrow("unable to reference: ident");
  });

  it("should define identifier inside function scope", function() {
    var f = function ident() {
      return ident;
    };

    expect(f()).toBe(f);
  });
});