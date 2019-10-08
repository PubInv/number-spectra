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

function genall(curr,max) {
    if (max.length == 0) {
        var k = curr[0];
        var arr = curr.slice(1,curr.length);
        process.stdout.write('{value: '+continuedFractionClassic(curr)+' ,expression_text:\'['+k+';'+arr.toString()+']\'},\n');
    } else {
        var n = max[0];
        var next = max.slice(1,max.length);
        var i;
        for(i = 1; i <= n; i++) {
            curr.push(i);
            genall(curr,next);
            curr.pop();
        }
    }
}

function genallCF(max) {
    process.stdout.write('const SPECTRA0 = [\n');
    genall([],max);
    process.stdout.write('];\n');
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

/* process.stdout.write(toContinuedFraction(31415926536,10000000000)+'\n'); */
var max = parseInt(process.argv[2]);
var n = parseInt(process.argv[3]);

var arr = [];
for(var i = 0; i < n; i++) {
    arr.push(max);
}
genallCF(arr);
