import {useEffect, useState} from "react";
import "./Header.module.css"

export default function Header() {
    const [nowTime, setNow] = useState(new Date())
    useEffect(() => {
        const interval = setInterval(() => setNow(new Date()), 1000)
        return () => {
            clearInterval(interval)
        }
    }, [])
    return (
        <header>
            <h3>Тараненко Максим | P3211 </h3>
            <span>Текущее время: {nowTime.toLocaleTimeString()}</span>
        </header>
    )
}
