import {Link} from "react-router-dom";
// src/pages/index.js를 통해서 한번에 import 할 수 있도록 함

function CreateView() {
    return (
        <div className="App">
            <header className="App-header">

                <Link to="/Register">

                    <a
                        className="App-link"
                        href="https://reactjs.org"
                        target="_blank"
                        rel="noopener noreferrer"
                    >
                        Create View
                    </a>

                </Link>

            </header>
        </div>
    );
}

export default CreateView;
