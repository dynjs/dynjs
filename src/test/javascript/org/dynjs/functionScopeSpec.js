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

