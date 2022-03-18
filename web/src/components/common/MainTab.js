import React, {useState} from "react";
import {useHistory, withRouter} from "react-router-dom";
import {AppBar, Tabs, Tab} from "@material-ui/core";

const MainTab = (props) => {
    console.log((props.tabValue))
    const [value, setValue] = useState(Number(props.tabValue))
    const history = useHistory();
    const HandleChange = (event, newValue) => {
        setValue(newValue)
        history.replace(newValue === 0 ? "/main/art" : newValue === 1 ? "/main/my" : newValue === 2 ? "/main/create" : newValue === 3 ? "/main/support" : "/main/art");
    };

    return (

        <AppBar title='aaaaa' position='fixed' style={{ background: '#2E3B55'}}>
            <Tabs value={value} onChange={HandleChange} style={{backgroundColor:'#000000'}}>
                <Tab label="Art" style={{marginLeft:'auto', padding:'0'}}/>
                <Tab label="My"/>
                <Tab label="Create"/>
                <Tab label="Support"/>
            </Tabs>
        </AppBar>
    );
}

export default withRouter(MainTab);
