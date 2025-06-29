import Slider from "../core/Slider/Slider.jsx";
import TextField from "../core/TextField/TextField.jsx";
import {Button} from "../core/Button/Button.jsx";

export function Controls({x, y, r, xChange, yChange, rChange, onSubmitClick}) {


    return (
        <div>
            <Slider onChange={xChange} current={x} title={"Значение по X:"} values={[-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2]}/>
            <TextField onChange={yChange} min={-2} max={2} title={"Значение по Y:"}/>
            <Slider onChange={rChange} current={r} title={"Значение R:"} values={[0.5, 1, 1.5, 2]}/>
            <Button onClick={onSubmitClick}>Добавить точку</Button>
        </div>
    )
}
