import classes from "./Button.module.css"
export function Button({id, children, onClick, transparent}) {
    return (
        <button id={id} onClick={onClick} className={transparent ? `${classes.button} ${classes.transparent}` : classes.button}>
            {children}
        </button>
    )
}
