import { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import axios from "axios";

function ResetPassword() {

    const [searchParams] = useSearchParams();

    const token = searchParams.get("token");

    const navigate = useNavigate();

    const [password, setPassword] = useState("");

    const resetPassword = async (e) => {

        e.preventDefault();

        try {

            const res = await axios.post(
                "http://localhost:9090/api/auth/reset-password",
                {
                    token: token,
                    password: password
                }
            );

            alert(res.data.message);

            navigate("/login");

        } catch (err) {

            alert(
                err.response?.data?.message ||
                "Something went wrong"
            );

        }

    };

    return (

        <div className="container">

            <div
                className="card shadow p-4 mx-auto"
                style={{
                    maxWidth: "500px",
                    marginTop: "80px"
                }}
            >

                <h2 className="text-center mb-4">
                    Reset Password
                </h2>

                <form onSubmit={resetPassword}>

                    <input
                        type="password"
                        className="form-control mb-3"
                        placeholder="Enter New Password"
                        value={password}
                        onChange={(e) =>
                            setPassword(e.target.value)
                        }
                    />

                    <button
                        className="btn btn-primary w-100"
                    >
                        Reset Password
                    </button>

                </form>

            </div>

        </div>

    );

}

export default ResetPassword;