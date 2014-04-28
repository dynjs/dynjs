// howdy
// whatup
// is this good?
// these shouldn't break

function a() {
  b(); // anywhere
}

// the spacing of the line-number

function c() {
  throw new java.lang.Exception( "damnit" );  // this is line 13
}

function b() {
  c();
}

a();
