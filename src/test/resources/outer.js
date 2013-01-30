inner = require('inner');

var Calculator = function() {
  this.quadruple = function( base ) {
    return inner.doubler(base) + inner.doubler(base);
  }
}

module.exports = new Calculator()
