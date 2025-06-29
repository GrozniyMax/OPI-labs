
export const drawCircle = (ctx, width, height, radius, dynamicScalingFactor) => {
    ctx.fillStyle = "#39FF1410";
    ctx.beginPath();
    ctx.arc(width / 2, height / 2, radius * dynamicScalingFactor, 0, 0.5 * Math.PI);
    ctx.lineTo(width / 2, height / 2);
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "#39FF14";
    ctx.stroke();
}

export const drawRectangle = (ctx, width, height, radius, dynamicScalingFactor) => {
    ctx.fillStyle = "#0000FF10";
    ctx.fillRect(width / 2, height / 2, radius * dynamicScalingFactor, -radius / 2 * dynamicScalingFactor);
    ctx.strokeStyle = "#0000FF";
    ctx.strokeRect(width / 2, height / 2, radius * dynamicScalingFactor, -radius / 2 * dynamicScalingFactor);
}

export const drawTriangle = (ctx, width, height, radius, dynamicScalingFactor) => {
    ctx.fillStyle = "#FFFF0010";
    ctx.beginPath();
    ctx.moveTo(width / 2, height / 2);
    ctx.lineTo(width / 2, height / 2 + radius / 2 * dynamicScalingFactor);
    ctx.lineTo(width / 2 - radius * dynamicScalingFactor, height / 2);
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "#FFFF00";
    ctx.stroke();
}

export const drawAxis = (ctx, width, height) => {
    ctx.beginPath();
    ctx.moveTo(0, height / 2);
    ctx.lineTo(width, height / 2);

    ctx.moveTo(width / 2, 0);
    ctx.lineTo(width / 2, height);
    ctx.strokeStyle = 'white';
    ctx.stroke();
}

export const drawAxisLabels = (ctx, width, height, dynamicScalingFactor, step = 0.5, range = [-2, 2]) => {
    ctx.font = '8px Arial';
    ctx.fillStyle = 'white';
    const centerX = width / 2;
    const centerY = height / 2;

    for (let i = range[0]; i <= range[1]; i += step) {
        const x = centerX + i * dynamicScalingFactor;
        ctx.fillText(i.toFixed(1), x, centerY + 15);
    }

    for (let i = range[0]; i <= range[1]; i += step) {
        const y = centerY - i * dynamicScalingFactor;
        ctx.fillText(i.toFixed(1), centerX + 5, y);
    }
};
