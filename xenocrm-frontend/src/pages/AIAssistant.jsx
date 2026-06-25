import { useState } from "react";

const API = "https://xeno-crm-production-1dfc.up.railway.app";

function AIAssistant() {

  const [question, setQuestion] = useState("");

  const [response, setResponse] = useState("");

  const [loading, setLoading] = useState(false);

  const askAI = async () => {

    if (question.trim() === "") {

      alert("Please ask something.");

      return;

    }

    setLoading(true);

    try {

      const res = await fetch(

        `${API}/api/ai/chat`,

        {

          method: "POST",

          headers: {

            "Content-Type": "application/json"

          },

          body: JSON.stringify({

            question: question

          })

        }

      );

      const data = await res.text();

      setResponse(data);

    }

    catch (err) {

      console.log(err);

      alert("Unable to connect AI");

    }

    finally {

      setLoading(false);

    }

  };

  return (

    <div className="container mt-4">

      <div className="card shadow-lg border-0">

        <div className="card-body p-5">

          <h2 className="fw-bold">

            🤖 AI CRM Assistant

          </h2>

          <p className="text-muted">

            Ask anything about your CRM.

          </p>

          <textarea

            className="form-control"

            rows="6"

            placeholder="Example:
Which leads should I contact today?
Who are my VIP customers?
Generate follow-up strategy."

            value={question}

            onChange={(e) =>{

              setQuestion(e.target.value);

            }}

          />

          <button

            className="btn btn-success mt-4"

            onClick={askAI}

            disabled={loading}

          >

            {

              loading

              ?

              <>

                <span

                  className="spinner-border spinner-border-sm me-2"

                ></span>

                Thinking...

              </>

              :

              "Ask AI"

            }

          </button>

          {

            response &&

            <div className="card mt-5">

              <div className="card-body">

                <h3>

                  🤖 AI Response

                </h3>

                <hr/>

                <pre

                  style={{

                    whiteSpace:"pre-wrap",

                    fontSize:"16px",

                    background:"#fff",

                    color:"#000",

                    padding:"20px",

                    borderRadius:"12px"

                  }}

                >

                  {response}

                </pre>

              </div>

            </div>

          }

        </div>

      </div>

    </div>

  );

}

export default AIAssistant;