const util = require('util')

function continuedFractionGeneral(f, g, i, n) {
       if (i < n - 1) {
           return f(i) + g(i) / continuedFractionGeneral(f, g, i + 1, n);
       }
       return f(i);
}

function continuedFractionClassic(arr) {
   return continuedFractionGeneral(i => arr[i],i => 1.0,0,arr.length);
}

// I'm modifying this to return objects so we can use JSON
// directly to make layers easier to incorporate into the
// browser.
function genall(curr,max) {
  var fractions = [];
    if (max.length == 0) {
        var k = curr[0];
      var arr = curr.slice(1,curr.length);
      var fraction = {
        value: continuedFractionClassic(curr),
        expression_text: '\'['+k+';'+arr.toString()+']\''
      };
      fractions.push(fraction);
//        process.stdout.write('{value: '+continuedFractionClassic(curr)+' ,expression_text:\'['+k+';'+arr.toString()+']\'},\n');
    } else {
        var n = max[0];
        var next = max.slice(1,max.length);
        var i;
        for(i = 1; i <= n; i++) {
            curr.push(i);
          var nextgen = genall(curr,next);
          fractions = fractions.concat(nextgen);
            curr.pop();
        }
    }
  return fractions;
}

function genallCF(max) {
//    process.stdout.write('const SPECTRA0 = [\n');
  const fractions = genall([],max);
  process.stdout.write(JSON.stringify(fractions,null,'  '));
      process.stdout.write('\n');
//    process.stdout.write('];\n');
}

function toContinuedFraction(p, q) {
    var curr = [];
    while(q != 1) {
        process.stdout.write(p+'/'+q);
        curr.push(Math.floor(p / q));
        var nq = p % q;
        p = q;
        q = nq;
    }
    curr.push(p);
    return curr;
}

/* process.stdout.write(toContinuedFraction(314,100)+'\n'); */
genallCF([4]);
