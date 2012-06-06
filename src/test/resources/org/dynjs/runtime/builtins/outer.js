var INNER = require('inner');

exports.quadruple = function( base ) {
  return INNER.doubler(base) + INNER.doubler(base);
}
