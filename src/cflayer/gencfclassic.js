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
        expression_text: '['+k+';'+arr.toString()+']'
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

function genallCF(arr,max,n) {
  const layers = {};
  const fractions = genall([],arr);
  layers["spectracf-"+max+"-"+n] = fractions;
  return layers;
}

function toContinuedFraction(p, q) {
    var curr = [];
    while(q != 0) {
        curr.push(Math.floor(p / q));
        var nq = p % q;
        p = q;
        q = nq;
    }
    return curr;
}

var max = parseInt(process.argv[2]);
var n = parseInt(process.argv[3]);

var arr = [];
for(var i = 0; i < n; i++) {
    arr.push(max);
}
var layers = genallCF(arr,max,n);
process.stdout.write(JSON.stringify(layers,null,'  '));
process.stdout.write('\n');
