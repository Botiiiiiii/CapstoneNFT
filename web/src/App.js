import './App.css';
import {
    BrowserRouter,
    Switch,
    Route
} from "react-router-dom";
import Login from './components/page/login/LoginPage'
import ArtView from "./components/page/main/art/ArtView";
import CreateView from "./components/page/main/create/CreateView";
import SupportView from "./components/page/main/support/SupportView";
import MyView from "./components/page/main/my/MyView";

function App() {
    return (
        <BrowserRouter>
                <Route path="/login" component={Login}/>
                <Route path="/main/art" component={ArtView}/>
                <Route path="/main/create" component={CreateView}/>
                <Route path="/main/my" component={MyView}/>
                <Route path="/main/support" component={SupportView}/>
                {/*<Route exact path="/info" render={() => <Info userInfo={userInfo} />} />*/}
        </BrowserRouter>
    );
}

export default App;
