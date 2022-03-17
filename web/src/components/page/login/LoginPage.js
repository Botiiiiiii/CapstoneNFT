import {Link} from "react-router-dom";

function LoginPage() {
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
                        Login Page
                    </a>

                </Link>

            </header>
        </div>
    );
}

export default LoginPage;
