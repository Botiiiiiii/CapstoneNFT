import {Link} from "react-router-dom";
import MainTab from "../../../common/MainTab";
import React from "react";

// src/pages/index.js를 통해서 한번에 import 할 수 있도록 함

const MyView = (props) => {
    return (
        <div>
            {<MainTab history={props} tabValue = "1"/>}
            <div>
                <Link to="/Register">

                    <a
                        className="App-link"
                        href="https://reactjs.org"
                        target="_blank"
                        rel="noopener noreferrer"
                    >
                        My View
                    </a>

                </Link>

            </div>

        </div>
    );
}

export default MyView;
