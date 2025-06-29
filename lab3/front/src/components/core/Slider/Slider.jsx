import React, {useEffect, useState} from "react";
import "./Slider.css";


const Slider = ({title, current, values, onChange}) => {
    const [currentIndex, setCurrentIndex] = useState(current);

    useEffect(() => {
        handleSlide(values.indexOf(current))
    }, [current])

    const handleSlide = (index) => {
        setCurrentIndex(index);
        if (onChange) {
            onChange(values[index]);
        }
    };

    return (
        <div>
            <h3>{title}</h3>
            <div className="value-szlider">
                <div className="slider-track">
                    {values.map((value, index) => (
                        <div
                            key={index}
                            className={`slider-point ${
                                index === currentIndex ? "active" : ""
                            }`}
                            onClick={() => handleSlide(index)}
                        >
                            {value}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Slider;
