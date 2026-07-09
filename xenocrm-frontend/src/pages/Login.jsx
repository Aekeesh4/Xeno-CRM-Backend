import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleLogin = async () => {

    try {

      const response = await fetch(
        "http://localhost:9090/api/auth/login",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email,
            password,
          }),
        }
      );

      const data = await response.json();

      console.log(data);

      if (data.token) {

        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data.user));

        alert("Login Successful");

        navigate("/");

      } else {

        alert(data.message || "Invalid Credentials");

      }

    } catch (error) {

      console.log(error);

      alert("Server Error");

    }

  };

  return (
    <div
      className="container mt-5"
      style={{ maxWidth: "600px" }}
    >
      <div
        className="p-5 rounded shadow"
        style={{
          background: "#1e293b",
          color: "white",
        }}
      >

        <h1 className="text-center mb-4">
          Login
        </h1>

        <input
          type="email"
          className="form-control mb-3"
          placeholder="Enter your email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          className="form-control mb-3"
          placeholder="Enter your password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button
          className="btn btn-primary w-100"
          onClick={handleLogin}
        >
          Login
        </button>

        <div className="text-end mt-3">
          <button
            type="button"
            className="btn btn-link text-info text-decoration-none p-0"
            onClick={() => navigate("/forgot-password")}
          >
            Forgot Password?
          </button>
        </div>

      </div>
    </div>
  );

}

export default Login;