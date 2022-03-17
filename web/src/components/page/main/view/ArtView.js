import { Link} from "react-router-dom";
// src/pages/index.js를 통해서 한번에 import 할 수 있도록 함

function ArtView() {
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
                        Art View
                    </a>

                </Link>

            </header>
        </div>
    );
}

export default ArtView;
