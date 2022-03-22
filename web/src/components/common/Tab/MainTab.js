import React, {useState} from "react";
import {withRouter} from "react-router-dom";
import {AppBar, Tabs, Tab} from "@material-ui/core";

const MainTab = (props) => {
    console.log((props.tabValue))
    const [value, setValue] = useState(Number(props.tabValue))
    const HandleChange = (event, newValue) => {
        setValue(newValue)
    };

    return (

        <AppBar className="tabMenu" position='fixed'>
            <Tabs className="submenu" value={value} onChange={HandleChange}>
                <Tab label="Art" href="/main/art" style={{marginLeft: "auto", padding: "0"}}/>
                <Tab label="My" href="/main/my"/>
                <Tab label="Create" href="/main/create"/>
                <Tab label="Support" href="/main/support"/>
            </Tabs>
        </AppBar>
    );
}

export default withRouter(MainTab);
