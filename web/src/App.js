import './App.css';
import {
    BrowserRouter,
    Routes,
    Route
} from "react-router-dom";
import Login from './components/page/login/LoginPage'
import Main from './components/page/main/MainPage'

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login/>}/>
                <Route path="/main" element={<Main/>}/>
                {/*<Route exact path="/info" render={() => <Info userInfo={userInfo} />} />*/}
            </Routes>
        </BrowserRouter>
    );
}

export default App;
