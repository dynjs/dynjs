
describe("modules", function() {

  var userDir   = java.lang.System.getProperty('user.dir');

  require.addLoadPath( userDir + "/src/test/javascript/org/dynjs/test-modules" );

  it("should provide non-leakage of vars into sub-required modules", function() {
    require('module-a.js');
  });

/*
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
  */

});
