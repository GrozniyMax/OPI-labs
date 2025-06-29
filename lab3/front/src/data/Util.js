const formatTime = (timeInSeconds) => {
    const hours = Math.floor(timeInSeconds / 3600);
    const minutes = Math.floor((timeInSeconds % 3600) / 60);
    const seconds = timeInSeconds % 60;

    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
};

export const transformPoints = (points) => {
    return points.map(point => ({
        time: formatTime(point.time),
        x: point.x,
        y: point.y,
        r: point.r,
        result: point.result
    }));
};


export const isPointInShape = (x, y, r) => {
    if (x >= 0 && y <= 0) {
        return (x * x + y * y) <= r * r;
    }
    // Нижний левый угол (треугольник)
    if (x <= 0 && y <= 0) {
        return (x >= -r) && (y >= -x * 0.5 - r * 0.5);
    }
    // Нижний правый угол (прямоугольник)
    if (x >= 0 && y >= 0) {
        return (x <= r) && (y < r * 0.5);
    }
    // Верхний правый угол (пусто)
    return false;
};

