import React, { useEffect, useState } from "react";
import "./Table.css";

export default function Table({points}) {
    return (
        <div className="table-container">
                <table className="styled-table">
                    <thead>
                    <tr>
                        <th>Время</th>
                        <th>X</th>
                        <th>Y</th>
                        <th>R</th>
                        <th>Результат</th>
                    </tr>
                    </thead>
                    <tbody>
                    {points.map((point, index) => (
                        <tr key={index}>
                            <td>{point.time}</td>
                            <td>{point.x}</td>
                            <td>{point.y}</td>
                            <td>{point.r}</td>
                            <td className={point.result ? "success" : "failure"}>
                                {point.result ? "V" : "X"}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
        </div>
    );
}
