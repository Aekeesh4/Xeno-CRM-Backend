import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function ForgotPassword() {

  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async () => {

    if (!email) {
      alert("Please enter your email.");
      return;
    }

    try {

      setLoading(true);

      const response = await axios.post(
        "http://localhost:9090/api/auth/forgot-password",
        {
          email: email
        }
      );

      alert(response.data.message);

      navigate("/login");

    } catch (error) {

      console.log(error);

      alert(
        error.response?.data?.message ||
        "Something went wrong."
      );

    } finally {

      setLoading(false);

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
          color: "white"
        }}
      >

        <h2 className="text-center mb-4">
          Forgot Password
        </h2>

        <input
          type="email"
          className="form-control mb-4"
          placeholder="Enter your registered email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <button
          className="btn btn-warning w-100"
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? "Sending..." : "Send Reset Link"}
        </button>

      </div>

    </div>

  );

}

export default ForgotPassword;