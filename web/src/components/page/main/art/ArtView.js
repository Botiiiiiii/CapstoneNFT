import React from "react";
// src/pages/index.js를 통해서 한번에 import 할 수 있도록 함
import MainTab from "../../../common/MainTab";

const ArtView = (props) => {
    return (
        <div>
            <MainTab history={props} tabValue="0"/>
            <div style={{marginTop:"100px"}}>
                <p>art View</p>
            </div>
        </div>
    );
}

export default ArtView;
