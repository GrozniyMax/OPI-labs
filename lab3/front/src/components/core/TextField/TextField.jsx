import React, { useEffect, useState } from "react";
import "./TextField.css";

const TextField = ({ min, current, max, onChange, title }) => {
    const [value, setValue] = useState(current || "");  // Инициализируем значением по умолчанию пустой строкой
    const [error, setError] = useState("");

    useEffect(() => {
        if (current !== undefined) {  // Проверяем, существует ли current
            handleChange(current.toString());
        }
    }, [current]);

    const handleChange = (inputValue) => {
        // Проверка на числовое значение
        if (!/^-?\d*\.?\d*$/.test(inputValue) && inputValue !== "") {
            setError("Введите числовое значение");
            return;
        }

        setValue(inputValue);
        setError("");

        const numericValue = parseFloat(inputValue);

        // Проверка на диапазон
        if (numericValue < min || numericValue > max) {
            setError(`Число в диапазоне [${min}, ${max}]`);
            return;
        }

        if (onChange && !isNaN(numericValue)) {
            onChange(numericValue); // Отправляем значение через onChange
        }
    };

    return (
        <div>
            <h3>{title}</h3>
            <div className="text-field-wrapper">
                <input
                    id="x-input-field"
                    type="text"
                    className={`text-field ${error ? "text-field-error" : ""}`}
                    value={value}
                    onChange={(e) => handleChange(e.target.value)} // Обработчик события
                    placeholder={`Введите значение [${min}, ${max}]`}
                />
                {error && <div className="text-field-error-message">{error}</div>}
            </div>
        </div>
    );
};

export default TextField;
