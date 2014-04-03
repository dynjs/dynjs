
describe("modules", function() {

  var userDir   = java.lang.System.getProperty('user.dir');

  require.addLoadPath( userDir + "/src/test/javascript/org/dynjs/test-modules" );

  it("should provide non-leakage of vars into sub-required modules", function() {
    require('module-a.js');
  });

});
