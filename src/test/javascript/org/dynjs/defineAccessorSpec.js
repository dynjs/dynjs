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
