import React, {useState} from "react";
import {withRouter} from "react-router-dom";
import {AppBar, Tabs, Tab} from "@material-ui/core";

const SubTab = (props) => {
    console.log((props.tabValue))
    const [value, setValue] = useState(Number(props.tabValue))
    const HandleChange = (event, newValue) => {
        setValue(newValue)
    };

    const MouseOver = (value) => {
        return (
            <div>
                <p>
                    aaa
                </p>
            </div>
        )
    }

    return (
        <div>
            <Tabs className="submenu" value={value} onChange={HandleChange} onMouseOver={MouseOver}>
                <Tab label="Art" href="/main/art" onMouseOver={MouseOver(0)}
                     style={{marginLeft: "auto", padding: "0"}}/>
                <Tab label="My" href="/main/my" onMouseOver={MouseOver(1)}/>
                <Tab label="Create" href="/main/create" onMouseOver={MouseOver(2)}/>
                <Tab label="Support" href="/main/support" onMouseOver={MouseOver(3)}/>
            </Tabs>
        </div>
    );
}

export default withRouter(SubTab);
