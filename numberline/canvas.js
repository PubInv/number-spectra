// GLOBAL
var W = 700;
var H = 500;
const WINDOW_HEIGHT_FACTOR = 0.5;


function draw_number_line(ctx,starti,endi) {
  with(ctx) {

    var w = W;
    var h = H;
    fillStyle = '#FFFFFF';
    fillRect(0, 0, w, h);
    fill();
    beginPath();
    lineWidth = 1;
    strokeStyle = '#fff00';
    moveTo(w/7, h/2);
    lineTo(6*w/7, h/2);
    stroke();
    moveTo(w/7, h/2);
    lineTo(w/7+10, h/2+10);
    stroke();
    moveTo(w/7, h/2);
    lineTo(w/7+10, h/2-10);
    stroke();
    moveTo(6*w/7, h/2);
    lineTo(6*w/7-10, h/2+10);
    stroke();
    moveTo(6*w/7, h/2);
    lineTo(6*w/7-10, h/2-10);
    stroke();

    for(var i = starti;i <= endi; i++) {
      beginPath();
      strokeStyle = '#000';
      lineWidth = 1;
      moveTo(w/2 + i * interval, h/2 - 10);
      lineTo(w/2 + i * interval, h/2 + 10);
      fillStyle = '#000';
      fillText(i, (w/2 + i * interval )- 5, h/2 + 25);
      if(!i) {

        strokeStyle = '#f0f';
      }
      fill();
      stroke();
    }
  }
}

function redraw(ctx) {
  draw_number_line(ctx);
}

$(function() {
  var canvas = $('canvas')[0];
  var ctx = canvas.getContext('2d');

  var w = canvas.width = 700;
  var h = canvas.height = 400;
  var x0 = w/2;
  var starti=-10;
  var endi=10;
  var interval = 20;
  var step = 2;
  var steps = 5;


  function drawOperation(x, step, steps){
    drawPoint(x);
    for(var i = 0; i < steps; i++){
      drawCurve(x+i*step*interval,x+(i+1)*step*interval);
    }
    drawPoint(x+steps*step*interval);
  }

  canvas.addEventListener("mousedown", getPosition, false);
  function drawPoint(x1){
    ctx.beginPath();
    ctx.arc(x1,h/2,2,0,2*Math.PI);
    ctx.fill();
    ctx.stroke();
  }
  function drawArc(p1,dir){

  }

  function getPosition(e){
    var x = e.clientX;
    var xe = x-x%interval+10;
    if(x0===0){
      drawPoint(xe);
      x0=xe;
    }
    else{
      drawPoint(xe);
      drawCurve(x0,xe);
      x0=xe;
    }

  }
  function drawCurve(x1,x2){
    ctx.moveTo(x1,h/2);
    ctx.quadraticCurveTo(x1+(x2-x1)/2,h/2-Math.abs(x2-x1),x2,h/2);
    var text;
    var s = (x2 - x1)/interval;
    if(s>0){
      text="+" +s.toString();
    }
    else {
      text = (x2 - x1)/interval;
    }
    ctx.fillText(text,x1+(x2-x1)/2,h/2-Math.abs(x2-x1)*0.5-10);

    ctx.stroke();
  }




  function onWindowResize() {
    W = window.innerWidth;
    H = window.innerHeight * WINDOW_HEIGHT_FACTOR;
    console.log(W,H);
    redraw(ctx,starti,endi);
  }
  window.addEventListener('resize', onWindowResize, false);

  draw_number_line(ctx);
  //  drawOperation(x0,step, steps);
});
