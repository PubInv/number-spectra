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
        var obj = {};
        obj.value = continuedFractionClassic(curr);
        obj.expression_text = '['+k+';'+arr.toString()+']';
        process.stdout.write(util.inspect(obj)+',\n');
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
    genall([],max);
}

function toContinuedFraction(p, q) {
    var curr = [];
    while(q != 1) {
        curr.push(Math.floor(p / q));
        var nq = p % q;
        p = q;
        q = nq;
    }
    curr.push(p);
    return curr;
}

genallCF([10,10,10,10,10,10]);
/* process.stdout.write(toContinuedFraction(314,100)+'\n'); */
