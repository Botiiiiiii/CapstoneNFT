import React, {useState} from "react";
import ArtView from './view/ArtView'
import CreateView from './view/CreateView'
import MyView from './view/MyView'
import SupportView from './view/SupportView'
import '../../../App.css'

// src/pages/index.js를 통해서 한번에 import 할 수 있도록 함

function MainPage() {
    const [activeTab, setActiveTab] = useState(0)
    const clickHandler = (id) => {
        setActiveTab(id)
    }

    const tabMenu = {
        0: <ArtView/>,
        1: <MyView/>,
        2: <CreateView/>,
        3: <SupportView/>
    }

    return (
        <div>
            <div align='right'>

                <ul className="tabMenu">
                    <li className="submenu" onClick={() => clickHandler(0)}>Art</li>
                    <li className="submenu" onClick={() => clickHandler(1)}>My</li>
                    <li className="submenu" onClick={() => clickHandler(2)}>Create</li>
                    <li className="submenu" onClick={() => clickHandler(3)}>Support</li>
                </ul>

            </div>
            <div className="contents">
                {tabMenu[activeTab]}
            </div>
        </div>
    );
}

export default MainPage;
