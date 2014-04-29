describe("calling a function on an undefined property of an object", function(){
  it("should include the function name in the error message", function(){
    try {
      foo = {};
      foo.bar.foobar();
    } catch(e) {
      expect(e.toString()).toMatch(/foobar/);
    }
  });
});

describe('instanceof', function() {
  it('should throw a TypeError when RHS is undefined', function() {
    try {
      var x = {} instanceof undefined;
      throw "instanceof should have thrown TypeError";
    } catch (e) {
      expect(e instanceof TypeError).toBeTruthy();
    }
  });
});

