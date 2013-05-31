function AnObject() {
  return "functor"
}

var myfunc = function() {
  return "hello!"
}
module.exports = new AnObject();
module.exports.AnObject = AnObject;
module.exports.a_function = myfunc;

