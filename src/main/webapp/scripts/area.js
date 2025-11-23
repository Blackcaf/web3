function getCanvas() {
    return document.getElementById('area');
}

function parseLocaleFloat(str) {
    if (!str) return NaN;
    const normalized = String(str).trim().replace(',', '.');
    const result = parseFloat(normalized);
    return isNaN(result) ? NaN : result;
}

function getCtxAndResize() {
    const canvas = getCanvas();
    if (!canvas) return null;
    const dpr = window.devicePixelRatio || 1;
    const cssSize = canvas.parentElement ? canvas.parentElement.clientWidth : 400;
    canvas.style.width = cssSize + 'px';
    canvas.style.height = cssSize + 'px';
    canvas.width = Math.round(cssSize * dpr);
    canvas.height = Math.round(cssSize * dpr);
    const ctx = canvas.getContext('2d');
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    return ctx;
}

function getScale() {
    const rInput = document.getElementById('mainForm:rValue_input');
    const rVal = rInput ? parseLocaleFloat(rInput.value) : 1.0;
    const r = isNaN(rVal) || rVal <= 0 ? 1.0 : rVal;
    const canvas = getCanvas();
    const size = canvas ? canvas.clientWidth : 400;
    const domainMax = Math.max(5, r + 1);
    const unit = (size / (domainMax * 2));
    const cx = size / 2;
    const cy = size / 2;
    return { r, unit, cx, cy, size, domainMax };
}

function drawAxes(ctx, scale) {
    const { unit, cx, cy, size, r } = scale;
    ctx.save();
    ctx.clearRect(0, 0, size, size);

    ctx.strokeStyle = '#e5e7eb';
    ctx.lineWidth = 1;
    for (let k = -scale.domainMax; k <= scale.domainMax; k++) {
        const gx = cx + k * unit;
        const gy = cy - k * unit;
        ctx.beginPath();
        ctx.moveTo(gx, 0); ctx.lineTo(gx, size);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(0, gy); ctx.lineTo(size, gy);
        ctx.stroke();
    }

    ctx.strokeStyle = '#333';
    ctx.beginPath();
    ctx.moveTo(0, cy); ctx.lineTo(size, cy);
    ctx.moveTo(cx, 0); ctx.lineTo(cx, size);
    ctx.stroke();

    ctx.fillStyle = '#333';
    ctx.beginPath();
    ctx.moveTo(size - 10, cy - 4); ctx.lineTo(size, cy); ctx.lineTo(size - 10, cy + 4);
    ctx.moveTo(cx - 4, 10); ctx.lineTo(cx, 0); ctx.lineTo(cx + 4, 10);
    ctx.fill();

    ctx.font = '12px sans-serif';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'top';
    
    const labels = [-r, -r/2, r/2, r];
    for (let v of labels) {
        const x = cx + v * unit;
        if (x >= 0 && x <= size) {
            ctx.beginPath(); ctx.moveTo(x, cy - 5); ctx.lineTo(x, cy + 5); ctx.stroke();
            if (v !== 0) ctx.fillText(String(v), x, cy + 6);
        }
    }
    ctx.textAlign = 'right';
    ctx.textBaseline = 'middle';
    for (let v of labels) {
        const y = cy - v * unit;
        if (y >= 0 && y <= size) {
            ctx.beginPath(); ctx.moveTo(cx - 5, y); ctx.lineTo(cx + 5, y); ctx.stroke();
            if (v !== 0) ctx.fillText(String(v), cx - 6, y);
        }
    }
    ctx.restore();
}

function drawRegion(ctx, scale) {
    const { r, unit, cx, cy } = scale;
    ctx.save();
    
    ctx.lineWidth = 2;
    
    ctx.fillStyle = 'rgba(30, 144, 255, 0.5)';
    ctx.strokeStyle = 'rgba(30, 144, 255, 0.9)';
    ctx.beginPath();
    ctx.moveTo(cx, cy);
    ctx.lineTo(cx + r * unit, cy);
    ctx.lineTo(cx + r * unit, cy - r * unit);
    ctx.lineTo(cx, cy - r * unit);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    ctx.fillStyle = 'rgba(0, 191, 255, 0.5)';
    ctx.strokeStyle = 'rgba(0, 191, 255, 0.9)';
    ctx.beginPath();
    ctx.moveTo(cx, cy);
    ctx.lineTo(cx - (r/2) * unit, cy);
    ctx.lineTo(cx, cy - r * unit);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
    
    ctx.fillStyle = 'rgba(70, 130, 180, 0.5)';
    ctx.strokeStyle = 'rgba(70, 130, 180, 0.9)';
    ctx.beginPath();
    ctx.moveTo(cx, cy);
    ctx.arc(cx, cy, r * unit, 0, Math.PI/2, false);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
    
    ctx.restore();
}

function drawPoints(ctx, scale) {
    const { unit, cx, cy } = scale;
    
    const rows = document.querySelectorAll('.results-table tbody tr');
    rows.forEach(function(tr) {
        const tds = tr.querySelectorAll('td');
        if (tds.length < 5) return;
        const x = parseLocaleFloat(tds[1].textContent);
        const y = parseLocaleFloat(tds[2].textContent);
        const r = parseLocaleFloat(tds[3].textContent);
        const hitText = (tds[4].textContent || '').trim();
        const hit = hitText === 'Да' || hitText.toLowerCase() === 'yes' || hitText === 'true';
        
        const rInput = document.getElementById('mainForm:rValue_input');
        const currentR = rInput ? parseLocaleFloat(rInput.value) : 1.0;
        if (Math.abs(r - currentR) > 0.01) return;
        
        const px = cx + x * unit;
        const py = cy - y * unit;
        ctx.beginPath();
        ctx.arc(px, py, 4, 0, Math.PI * 2);
        ctx.fillStyle = hit ? '#2e7d32' : '#b00020';
        ctx.fill();
    });
    
    if (previewPoint) {
        const px = cx + previewPoint.x * unit;
        const py = cy - previewPoint.y * unit;
        ctx.save();
        ctx.beginPath();
        ctx.arc(px, py, 6, 0, Math.PI * 2);
        ctx.fillStyle = '#2563eb';
        ctx.fill();
        ctx.strokeStyle = '#ffffff';
        ctx.lineWidth = 2;
        ctx.stroke();
        ctx.restore();
    }
}

function drawAll() {
    const ctx = getCtxAndResize();
    if (!ctx) return;
    const scale = getScale();
    drawAxes(ctx, scale);
    drawRegion(ctx, scale);
    drawPoints(ctx, scale);
}

let previewPoint = null;

function mouseMoveCanvas(ev) {
    const rInput = document.getElementById('mainForm:rValue_input');
    const rVal = rInput ? parseLocaleFloat(rInput.value) : null;
    if (!rInput || isNaN(rVal)) return; 

    const canvas = getCanvas();
    const rect = canvas.getBoundingClientRect();
    const cx = ev.clientX - rect.left;
    const cy = ev.clientY - rect.top;
    const scale = getScale();
    
    let x = Math.round((cx - scale.cx) / scale.unit);
    const validX = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
    let closestX = validX[0];
    let minDist = Math.abs(x - closestX);
    for (let vx of validX) {
        const dist = Math.abs(x - vx);
        if (dist < minDist) {
            minDist = dist;
            closestX = vx;
        }
    }
    x = closestX;

    const y = -((cy - scale.cy) / scale.unit);
    const clampedY = Math.max(-5, Math.min(5, y));
    
    if (typeof window.setXValue === 'function') {
        window.setXValue(x);
    } else if (typeof setXValue === 'function') {
        setXValue(x);
    } else {
        if (typeof window.selectXValue === 'function') {
            window.selectXValue(x);
        } else if (typeof selectXValue === 'function') {
            selectXValue(x);
        } else {
            var hiddenInput = document.getElementById('mainForm:xValue') ||
                             document.querySelector('[id$=":xValue"]') ||
                             document.querySelector('input[type="hidden"][id*="xValue"]');
            if (hiddenInput) {
                hiddenInput.value = x;
                var changeEvent = new Event('change', { bubbles: true });
                hiddenInput.dispatchEvent(changeEvent);
            }
        }
    }
    
    const yInput = document.getElementById('mainForm:yValue') ||
                   document.querySelector('[id$=":yValue"]') ||
                   document.querySelector('input[type="text"][id*="yValue"]');
    if (yInput) {
        yInput.value = clampedY;
        var yChangeEvent = new Event('change', { bubbles: true });
        yInput.dispatchEvent(yChangeEvent);
    }
    
    previewPoint = { x: x, y: clampedY };
    drawAll(); 
}

function clickCanvas(ev) {
    const rInput = document.getElementById('mainForm:rValue_input');
    const rVal = rInput ? parseLocaleFloat(rInput.value) : null;
    if (!rInput || isNaN(rVal)) return;
    
    const canvas = getCanvas();
    const rect = canvas.getBoundingClientRect();
    const cx = ev.clientX - rect.left;
    const cy = ev.clientY - rect.top;
    const scale = getScale();
    
    let x = Math.round((cx - scale.cx) / scale.unit);
    const validX = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
    let closestX = validX[0];
    let minDist = Math.abs(x - closestX);
    for (let vx of validX) {
        const dist = Math.abs(x - vx);
        if (dist < minDist) {
            minDist = dist;
            closestX = vx;
        }
    }
    x = closestX;
    
    const y = -((cy - scale.cy) / scale.unit);
    const clampedY = Math.max(-5, Math.min(5, y));
    
    if (typeof window.setXValue === 'function') {
        window.setXValue(x);
    } else if (typeof setXValue === 'function') {
        setXValue(x);
    } else {
        if (typeof window.selectXValue === 'function') {
            window.selectXValue(x);
        } else if (typeof selectXValue === 'function') {
            selectXValue(x);
        } else {
            var hiddenInput = document.getElementById('mainForm:xValue') ||
                             document.querySelector('[id$=":xValue"]') ||
                             document.querySelector('input[type="hidden"][id*="xValue"]');
            if (hiddenInput) {
                hiddenInput.value = x;
                var changeEvent = new Event('change', { bubbles: true });
                hiddenInput.dispatchEvent(changeEvent);
            }
        }
    }
    const yInput = document.getElementById('mainForm:yValue') ||
                   document.querySelector('[id$=":yValue"]') ||
                   document.querySelector('input[type="text"][id*="yValue"]');
    if (yInput) {
        yInput.value = clampedY;
        var yChangeEvent = new Event('change', { bubbles: true, cancelable: true });
        yInput.dispatchEvent(yChangeEvent);
        var yInputEvent = new Event('input', { bubbles: true, cancelable: true });
        yInput.dispatchEvent(yInputEvent);
        yInput.focus();
        setTimeout(function() {
            yInput.blur();
        }, 10);
    }
    
    setTimeout(function() {
        const form = document.getElementById('mainForm');
        if (form) {
            const checkButton = document.getElementById('mainForm:checkButton');
            if (checkButton) {
                checkButton.click();
            } else {
                const submitButton = form.querySelector('input[value="Проверить"], button[value="Проверить"]');
                if (submitButton) {
                    submitButton.click();
                } else {
                    form.submit();
                }
            }
        }
    }, 50);
}

window.addEventListener('load', function() {
    drawAll();
    
    const canvas = getCanvas();
    if (canvas) {
        canvas.addEventListener('mousemove', mouseMoveCanvas);
        canvas.addEventListener('click', clickCanvas);
    }
});

window.addEventListener('resize', drawAll);

window.addEventListener('load', function() {
    function setupRWatcher() {
        const rInput = document.getElementById('mainForm:rValue_input');
        if (rInput) {
            let lastRValue = rInput.value;
            setInterval(function() {
                if (rInput && rInput.value !== lastRValue) {
                    lastRValue = rInput.value;
                    previewPoint = null;
                    if (typeof drawAll === 'function') {
                        drawAll();
                    }
                }
            }, 50);
        } else {
            setTimeout(setupRWatcher, 100);
        }
    }
    setupRWatcher();
});
