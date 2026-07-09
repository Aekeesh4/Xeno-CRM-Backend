import {
  BrowserRouter,
  Routes,
  Route,
  Link,
} from "react-router-dom";

import Dashboard from "./pages/Dashboard.jsx";
import Customers from "./pages/Customers.jsx";
import Leads from "./pages/Leads.jsx";
import AddLead from "./pages/AddLead.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/RegisterPage.jsx";
import Profile from "./pages/Profile.jsx";
import AICopilot from "./pages/AICopilot.jsx";
import AIAssistant from "./pages/AIAssistant";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import ResetPassword from "./pages/ResetPassword";
import ForgotPassword from "./pages/ForgotPassword";
function App() {

  const token = localStorage.getItem("token");

  const user =
    JSON.parse(
      localStorage.getItem("user")
    );



  const logout = () => {

    localStorage.removeItem("token");

    localStorage.removeItem("user");

    window.location.href = "/login";

  };



  return (

    <BrowserRouter>

      <nav

        className="navbar navbar-expand-lg shadow"

        style={{

          background:

            "linear-gradient(90deg,#0f172a,#1e293b)",

          padding: "15px",

        }}

      >

        <div className="container">

          <Link

            className="navbar-brand fw-bold text-white"

            to="/"

          >

            Xeno CRM

          </Link>





          <div className="navbar-nav d-flex flex-row gap-3">

            {

              token

              &&

              <>

                <Link

                  className="nav-link text-white"

                  to="/"

                >

                  Dashboard

                </Link>



                <Link

                  className="nav-link text-white"

                  to="/customers"

                >

                  Customers

                </Link>



                <Link

                  className="nav-link text-white"

                  to="/leads"

                >

                  Leads

                </Link>



                {

                  user?.role === "ADMIN"

                  &&

                  <Link

                    className="nav-link text-white"

                    to="/add-lead"

                  >

                    Add Lead

                  </Link>

                }



                <Link

                  className="nav-link text-white"

                  to="/profile"

                >

                  Profile

                </Link>
                <Link

                  className="nav-link text-white"

                  to="/ai-assistant"

                >

                  🤖 AI Assistant

                </Link>
                <Link
                  className="nav-link text-white"
                  to="/ai-copilot"
                >
                  🤖 AI Copilot
                </Link>

              </>

            }





            {

              !token

              &&

              <>

                <Link

                  className="nav-link text-white"

                  to="/register"

                >

                  Register

                </Link>



                <Link

                  className="nav-link text-white"

                  to="/login"

                >

                  Login

                </Link>

              </>

            }

          </div>







          {

            token

            &&

            <div className="d-flex align-items-center gap-3">

              <div className="text-end">

                <div

                  className="text-white fw-bold"

                >

                  {user?.name}

                </div>



                <span

                  className="badge bg-primary"

                >

                  {user?.role}

                </span>

              </div>





              <button

                className="btn btn-danger"

                onClick={logout}

              >

                Logout

              </button>

            </div>

          }



        </div>

      </nav>








      <div className="container mt-5">

        <Routes>


          <Route

            path="/"

            element={

              <ProtectedRoute>

                <Dashboard />

              </ProtectedRoute>

            }

          />





          <Route

            path="/customers"

            element={

              <ProtectedRoute>

                <Customers />

              </ProtectedRoute>

            }

          />






          <Route

            path="/leads"

            element={

              <ProtectedRoute>

                <Leads />

              </ProtectedRoute>

            }

          />







          <Route

            path="/add-lead"

            element={

              <ProtectedRoute>

                <AddLead />

              </ProtectedRoute>

            }

          />







          <Route

            path="/profile"

            element={

              <ProtectedRoute>

                <Profile />

              </ProtectedRoute>

            }

          />
          <Route

            path="/ai-assistant"

            element={

              <ProtectedRoute>

                <AIAssistant />

              </ProtectedRoute>

            }

          />
          <Route
            path="/ai-copilot"
            element={
              <ProtectedRoute>
                <AICopilot />
              </ProtectedRoute>
            }
          />






          <Route

            path="/register"

            element={<Register />}

          />






          <Route

            path="/login"

            element={<Login />}

          />
          <Route
              path="/reset-password"
              element={<ResetPassword />}
          />
          <Route
            path="/forgot-password"
            element={<ForgotPassword />}
          />


        </Routes>

      </div>

    </BrowserRouter>

  );

}

export default App;