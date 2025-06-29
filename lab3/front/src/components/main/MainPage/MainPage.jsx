import {useAuth} from "../../start/Authorization/AuthContext.jsx";
import Canvas from "../Canvas/Canvas.jsx";
import {Controls} from "../Controls.jsx";
import "./MainPage.css"
import {Button} from "../../core/Button/Button.jsx";
import Table from "../Table/Table.jsx";
import {addPoint, clearPoints, getPoints} from "../../../data/Repository.js";
import {useEffect, useState} from "react";
import {ERROR, POINTS_LOADING_ERROR} from "../../../data/Strings.js";

export function MainPage() {
    const auth = useAuth();
    const [points, setPoints] = useState([]);
    const [xNow, setXNow] = useState(0);
    const [yNow, setYNow] = useState(0);
    const [rNow, setRNow] = useState(2);

    useEffect(() => {
        async function fetchPoints() {
            try {
                const data = await getPoints(auth.user, auth.password);
                if (data === ERROR) {
                } else {
                    setPoints(data);
                }
            } catch (error) {
                console.error(POINTS_LOADING_ERROR);
            }
        }

        fetchPoints();
    }, [auth.user, auth.password]);

    const clearAll = async () => {
        await clearPoints(auth.user, auth.password)
        setPoints([])
    }

    const addPointUser = async (x, y, r, time, result) => {
        await addPoint(auth.user, auth.password, x, y, r, time, result);
        setPoints(prevPoints => [...prevPoints, {x, y, r, time, result}]);
    };

    const addPointBySubmitButton = async () => {
        const time = Date.now()
        const result = false
        await addPoint(auth.user, auth.password, xNow, yNow, rNow, time, result);
        setPoints(prevPoints => [...prevPoints, {x: xNow, y: yNow, r: rNow, time, result}]);
    };


    const handleRUpdate = async (r) => {
        setRNow(r)
        await fetch(`http://localhost:8080/points/r?r=${r}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': ' Basic YXJla2Fsb3Y6cXdlcnR5MTIzNA=='
            }

        })
    }

    return (
        <div className="main-page">
            <h2>Добро пожаловать, {auth.user}!</h2>
            <div className="content-wrapper">
                <div className="canvas-column">
                    <Canvas existedPoints={points} radius={rNow} savePoint={addPointUser}/>
                </div>
                <div className="controls-column">
                    <Controls x={xNow} y={yNow} r={rNow} xChange={setXNow} yChange={setYNow} rChange={handleRUpdate}
                              onSubmitClick={addPointBySubmitButton}/>
                </div>
            </div>
            <Button id="clear" onClick={clearAll}>Почистить точки</Button>
            <Table points={points}/>
            <Button id="exit" onClick={auth.logout}>Выйти</Button>
        </div>
    );
}
