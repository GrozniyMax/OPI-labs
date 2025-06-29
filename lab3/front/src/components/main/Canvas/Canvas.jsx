import React, { useRef, useState, useEffect } from 'react';
import { drawAxis, drawAxisLabels, drawCircle, drawRectangle, drawTriangle } from "./DrawUtils.js";
import {isPointInShape} from "../../../data/Util.js";

export default function Canvas({ existedPoints, savePoint, radius }) {
    const canvasRef = useRef(null);
    const [points, setPoints] = useState(existedPoints || []);

    const width = 650;
    const height = 650;

    const dynamicScalingFactor = width / 4;

    const pointRadius =  5;

    const drawElements = (ctx) => {
        drawAxis(ctx, width, height);
        drawCircle(ctx, width, height, radius, dynamicScalingFactor);
        drawRectangle(ctx, width, height, radius, dynamicScalingFactor);
        drawTriangle(ctx, width, height, radius, dynamicScalingFactor);
        drawAxisLabels(ctx, width, height, dynamicScalingFactor);
    };

    const drawPoints = (ctx, points) => {
        points.forEach(point => {
            ctx.beginPath();
            ctx.arc(point.x * dynamicScalingFactor + width / 2,
                height / 2 - point.y * dynamicScalingFactor,
                pointRadius, 0, 2 * Math.PI);
            ctx.fillStyle = isPointInShape(point.x, point.y, radius, dynamicScalingFactor) ? 'green' : 'red';

            ctx.fill();
        });
    };

    useEffect(() => {
        setPoints(existedPoints);
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawElements(ctx);
        drawPoints(ctx, points);
    }, [points, existedPoints, radius]); // Добавлен radius в зависимость от useEffect

    const handleClick = (e) => {
        const canvas = canvasRef.current;
        const rect = canvas.getBoundingClientRect();
        const x = (e.clientX - rect.left - width / 2) / dynamicScalingFactor;
        const y = (height / 2 - (e.clientY - rect.top)) / dynamicScalingFactor;  // инвертируем Y

        const time = Date.now();
        const result = isPointInShape(x, y, radius, dynamicScalingFactor);
        savePoint(x, y, radius, time, result);
    };

    return (
        <div>
            <canvas
                id="canvas"
                ref={canvasRef}
                width={650}
                height={650}
                style={{ border: '1px solid black' }}
                onClick={handleClick}
            />
        </div>
    );
}
