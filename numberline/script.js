// This code was originally copied from code under MIT-licences
// by Larry Botha: https://codepen.io/larrybotha/pen/ZLQYrz

const log = (...args) => console.log(args);

const COLORS = {
  PRIMARY: '#000000',
  SECONDARY: '#ffffff' };


const numberlineFactory = (mountElem, config = {}) => {
  if (!mountElem || !('nodeType' in mountElem)) {
    throw new Error('no mount element provided');
  }

  const eventMap = {
    mousedown: handleCursorDown,
    touchstart: handleCursorDown,
    mouseleave: handleCursorLeave,
    touchleave: handleCursorLeave,
    mousemove: handleCursorMove,
    touchmove: handleCursorMove,
    mouseup: handleCursorUp,
    touchend: handleCursorUp,
    touchcancel: handleCursorUp };

  const handleHeight = 20;
  const padding = {
    bottom: 40,
    top: 60 };

  const tickRadii = {
    large: 4,
    medium: 2,
    small: 1 };

  // eslint-disable-next-line no-undef
  const two = new Two({
    // eslint-disable-next-line no-undef
    type: Two.Types[config.type || 'svg'],
    width: config.width || 100,
    height: config.height || 720 });

  const paths = {};
  let handleActive = false;
  let tickData = [];
  let handleTimer;
  let options;

  function init() {
    two.appendTo(mountElem).bind('resize', handleResize);

    drawShape();
    initEvents();

    setViewBox(two.width, two.height);
    two.renderer.domElement.setAttribute(
    'style',
    `
        cursor: pointer;
        -moz-user-select:none;
        -ms-user-select:none;
        -webkit-user-select:none;
        user-select:none;
        -webkit-tap-highlight-color: rgba(0,0,0,0);
      `);

  }

  function setViewBox(width, height) {
    two.renderer.domElement.setAttribute('viewBox', `0 0 ${width} ${height}`);
  }

  function drawShape() {
    drawBg();
    drawHandle();
  }

  function initEvents() {
    const domElement = two.renderer.domElement;

    Object.keys(eventMap).map((type) =>
    domElement.addEventListener(type, eventMap[type]));

  }

  function drawBg() {
    if (paths.bg) destroyBg();

    const bg = two.makeRectangle(
    two.width / 2,
    two.height / 2,
    two.width,
    two.height);

    bg.noStroke();
    bg.fill = COLORS.PRIMARY;

    paths.bg = bg;
  }

  function drawHandle() {
    if (paths.handle) destroyHandle();

    const handle = two.makeRectangle(
    two.width / 2,
    getActionAreaHeight() + padding.top,
    two.width,
    handleHeight);

    handle.opacity = 0.7;
    handle.noStroke();

    paths.handle = handle;

    two.update();
    // eslint-disable-next-line no-underscore-dangle
    handle._renderer.elem.setAttribute('style', 'cursor: move');
  }

  function drawNumberline() {
    const canDraw = Object.keys(options).every(
    (key) =>
    [
    'max',
    'min',
    'labelIncrement',
    'numTicksBetweenLabels',
    'unit'].
    indexOf(key) > -1);


    if (canDraw) {
      prepareTicks();
      prepareLabels();
      drawSpecials(SPECTRA0);
    }
  }


  function prepareLabels() {
    if (paths.labels && paths.labels.length) destroyLabels();

    const { min, max, labelIncrement, unit } = options;
    const numLabels = getNumLabels(min, max, labelIncrement);
    const labels = Array.apply(null, Array(numLabels)).map(
    (_, i) => min + i * labelIncrement);

    const labelsBottom = two.height - padding.bottom;
    const labelHeight = getActionAreaHeight() / (numLabels - 1);

    paths.labels = labels.map((num, i) => {
      const textX = two.width / 2 + 2;
      const textY = labelsBottom - labelHeight * i;
      const text = two.makeText(i === 0 ? `${num}${unit}` : num, textX, textY);
      text.fill = COLORS.SECONDARY;
      text.size = 14;
      text.alignment = 'left';
      text.baseline = 'middle';

      return text;
    });
  }

  function drawSpecials(specials) {
    if (paths.labels && paths.labels.length) destroySpecials();

    const { min, labelIncrement, numTicksBetweenLabels } = options;
    const numLabels = getNumLabels();
    const numSpecials = specials.length;
    const numTicks = numLabels + numTicksBetweenLabels * (numLabels - 1);
    const ticksBottom = two.height - padding.bottom;
    const tickYInc = getActionAreaHeight() / (numTicks - 1);

    paths.specials = specials.map((s, i) => {
      console.log("in draw",s);
      let radius = tickRadii.small;

      const lineY = ticksBottom - tickYInc * s.value;

      var m = two.width / 2 - 10
      var margin = 10;
      var line = two.makeLine(margin, lineY, 2*m+margin , lineY);
      line.linewidth = 1;
      line.stroke = "rgba(255, 0, 0, 1.0)";

      tickData[i] = {
        y: lineY,
        val: min + labelIncrement / (numTicksBetweenLabels + 1) * s.value };
      return line;
    });
  }

  function prepareTicks() {
    tickData = [];

    if (paths.ticks && paths.ticks.length) destroyTicks();

    const { min, labelIncrement, numTicksBetweenLabels } = options;
    const numLabels = getNumLabels();
    const numTicks = numLabels + numTicksBetweenLabels * (numLabels - 1);
    const ticksBottom = two.height - padding.bottom;
    const tickYInc = getActionAreaHeight() / (numTicks - 1);

    paths.ticks = Array.apply(null, Array(numTicks)).map((_, i) => {
      let radius =
      numTicksBetweenLabels % 2 === 1 &&
      i % Math.ceil(numTicksBetweenLabels / 2) === 0 ?
      tickRadii.medium :
      tickRadii.small;
      radius = i % (numTicksBetweenLabels + 1) === 0 ? tickRadii.large : radius;
      const circleY = ticksBottom - tickYInc * i;
      const circle = two.makeCircle(two.width / 2 - 10, circleY, radius);
      circle.noStroke();
      circle.fill = COLORS.SECONDARY;

      tickData[i] = {
        y: circleY,
        val: min + labelIncrement / (numTicksBetweenLabels + 1) * i };


      return circle;
    });
  }

  function getActionAreaHeight() {
    return two.height - (padding.top + padding.bottom);
  }

  function getNumLabels() {
    const { min, max, labelIncrement } = options;
    const range = Math.abs(max - min);

    return Math.floor(range / labelIncrement + 1);
  }

  function getNearestTickAtVal(val) {
    return tickData.reduce((acc, _, i, arr) => {
      const currTick = arr[i];
      const nextTick = arr[(i + 1) % arr.length];
      const valIsBetweenTicks = currTick.val <= val && nextTick.val >= val;

      if (valIsBetweenTicks) {
        return (currTick.val + nextTick.val) / 2 >= val ? currTick : nextTick;
      }

      return acc === -1 && i === arr.length - 1 ?
      val < arr[0].val ? arr[0] : arr[arr.length - 1] :
      acc;
    }, -1);
  }

  function getNearestTickAtY(y) {
    return tickData.reduce((acc, _, i, arr) => {
      const currTick = arr[i];
      const nextTick = arr[(i + 1) % arr.length];
      const yIsBetweenTicks = currTick.y >= y && nextTick.y <= y;

      if (yIsBetweenTicks) {
        return (currTick.y + nextTick.y) / 2 >= y ? nextTick : currTick;
      }

      return acc === -1 && i === arr.length - 1 ?
      y < arr[0].y ? arr[arr.length - 1] : arr[0] :
      acc;
    }, -1);
  }

  function handleCursorDown(e) {
    const eventPos = getEventPos(e);
    handleActive = true;

    if (handleTimer) clearInterval(handleTimer);

    setHandlePosition(eventPos.y);
  }

  function handleCursorMove(e) {
    if (handleActive) {
      const eventPos = getEventPos(e);
      e.preventDefault();

      setHandlePosition(eventPos.y);
    }
  }

  function handleCursorUp(e) {
    const eventPos = getEventPos(e);
    const nearestTick = getNearestTickAtY(eventPos.y);

    if (handleActive) {
      handleActive = false;
      animateHandle(nearestTick.y, () => {
        if (typeof config.onSelect === 'function') {
          config.onSelect(nearestTick.val);
        }
      });
    }
  }

  function handleCursorLeave() {
    if (handleActive) handleActive = false;
  }

  function animateHandle(y, cb) {
    const { handle } = paths;
    const incDec = handle.translation.y < y ? 1 : -1;
    const increment = 2 * incDec;
    let remaining = Math.abs(y - Math.abs(handle.translation.y));

    if (handleTimer) clearInterval(handleTimer);

    handleTimer = setInterval(() => {
      if (remaining < Math.abs(increment)) {
        clearInterval(handleTimer);
        setHandlePosition(parseInt(y, 10));

        if (typeof cb === 'function') cb();
      } else {
        setHandlePosition(parseInt(handle.translation.y + increment, 10));
        remaining = remaining - Math.abs(increment);
      }
    }, 1);
  }

  function animateHandleToValue(val, cb) {
    const nearestTick = getNearestTickAtVal(val);

    animateHandle(nearestTick.y, cb);
  }

  function setHandlePosition(y) {
    const { handle } = paths;
    const handleX = handle.translation.x;
    let handleY = y;

    if (y <= padding.top) {
      handleY = padding.top;
    }

    if (y >= two.height - padding.bottom) {
      handleY = two.height - padding.bottom;
    }

    if (typeof config.onSetHandlePosition === 'function') {
      const nearestTick = getNearestTickAtY(handleY);

      config.onSetHandlePosition(nearestTick.val);
    }

    handle.translation.set(handleX, handleY);
    two.update();
  }

  function getEventPos(e) {
    const touches = ['targetTouches', 'changedTouches'].reduce(
    (acc, targetType) =>
    e[targetType] && e[targetType].length ? e[targetType] : acc,
    null);

    const event = touches ? touches[0] : e;
    const svgBB = e.currentTarget.getBoundingClientRect();

    return {
      x: event.clientX - svgBB.left,
      y: event.clientY - svgBB.top };

  }

  function handleResize() {
    setViewBox(two.width, two.height);
    destroyPaths();
    drawBg();
    drawHandle();
    drawNumberline();
    two.update();
  }

  function updateDims({ height, width }) {
    two.width = parseInt(width, 10);
    two.height = parseInt(height, 10);
    two.trigger('resize');
  }

  function setOptions(opts = {}, cb) {
    options = Object.assign({}, options, opts);

    Object.keys(options).map(key => {
      const val = parseInt(options[key], 10);

      options[key] = isNaN(val) ? options[key] : val;
    });

    drawNumberline();
    animateHandleToValue(options.min, cb);
  }

  function destroySpecials() {
    if (paths.specials) {
      paths.specials.map(p => two.remove(p));
      delete paths.specials;
    }
  }


  function destroyTicks() {
    if (paths.ticks) {
      paths.ticks.map(p => two.remove(p));
      delete paths.ticks;
    }
  }

  function destroyLabels() {
    if (paths.labels) {
      paths.labels.map(p => two.remove(p));
      delete paths.labels;
    }
  }

  function destroyBg() {
    if (paths.bg) {
      two.remove(paths.bg);
      delete paths.bg;
    }
  }

  function destroyHandle() {
    if (paths.handle) {
      two.remove(paths.handle);
      delete paths.handle;
    }
  }

  function destroyPaths() {
    Object.keys(paths).map(key => {
      const ps = Array.isArray(paths[key]) ? paths[key] : [paths[key]];

      ps.map(path => two.remove(path));

      return delete paths[key];
    });
  }

  function destroy() {
    const domElement = two.renderer.domElement;

    Object.keys(eventMap).map((type) =>
    domElement.removeEventListener(type, eventMap[type], { capture: true }));

    destroyPaths();

    return true;
  }

  init();

  return {
    animateHandleToValue,
    destroy,
    setOptions,
    updateDims };

};

let opts = {};
const controls = document.querySelector('.js-controls');
const mount = document.querySelector('.js-mount');
const outputs = [].slice.apply(document.querySelectorAll('.js-range-output'));
const inputs = [].slice.apply(document.querySelectorAll('input'));

inputs.filter((elem, i) => elem.name.indexOf('nl-') > -1).
map(elem => {
  opts[elem.name.replace('nl-', '')] = elem.value;

  return elem;
});

const numberline = numberlineFactory(mount, {
  height: controls.offsetHeight,
  width: mount.offsetWidth,
  onSelect: val => {
    log(`onSelect callback value:`, val);
  } });

numberline.setOptions(opts);

function handleHandleChange(e) {
  const { target } = e;

  if (target.tagName.toLowerCase() === 'input' && target.value) {
    const { value } = target;
    const name = target.name.replace('nl-', '');

    switch (name) {
      case 'handle-value':
        numberline.animateHandleToValue(value);
        break;
      default:
        opts[target.name.replace('nl-', '')] = value;
        numberline.setOptions(opts);
        break;}

  }
}

const updateOutputs = () => {
  outputs.map(o => {
    const type = o.getAttribute('data-type');
    const input = inputs.find(i => i.name === type);

    o.textContent = `: ${input.value}`;
  });
};

controls.addEventListener('input', handleHandleChange);
controls.addEventListener('input', updateOutputs);

updateOutputs();

window.addEventListener('resize', () => {
  const { offsetWidth } = mount;
  const { offsetHeight } = controls;

  numberline.updateDims({
    height: offsetHeight,
    width: offsetWidth });

});
