// Tell jshint that we're using ES6 features (moz? seriously?)
/*jshint moz:true */

describe("ECMAScript 6 features", function() {
  // ES6 for loops
  // =============
  // Arrays are iterable by default.
  it("should support for var of loops on Arrays", function() {
    var fruits = ['Apple', 'Banana', 'Grape'];
    for (var fruit of fruits) {
      expect(typeof fruit).toBe('string');
      expect(fruits.indexOf(fruit)).toBeGreaterThan(-1);
    }
  });

  it("should support for of loops on Arrays", function() {
    var fruits = ['Apple', 'Banana', 'Grape'];
    for (fruit of fruits) {
      expect(typeof fruit).toBe('string');
      expect(fruits.indexOf(fruit)).toBeGreaterThan(-1);
    }
  });
});
