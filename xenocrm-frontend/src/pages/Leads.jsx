import { useEffect, useState } from "react";
import { toast } from "react-toastify";

function Leads() {

  const [leads, setLeads] = useState([]);

  const [filter, setFilter] = useState("ALL");

  const [search, setSearch] = useState("");

  const [currentPage, setCurrentPage] = useState(1);

  const leadsPerPage = 5;


  // AI EMAIL

  const [generatedEmail, setGeneratedEmail] = useState("");

  const [showEmail, setShowEmail] = useState(false);
const [emailTo, setEmailTo] = useState("");
const [editingLead, setEditingLead] = useState(null);

const [formData, setFormData] = useState({

  customerName: "",

  email: "",

  phone: "",

  company: "",

  source: "",

  assignedTo: "",

  status: "NEW",

  notes: ""

});


  const user =

    JSON.parse(

      localStorage.getItem("user")

    );


  const isAdmin = user?.role === "ADMIN";







  const fetchLeads = async () => {

    try {

      const response = await fetch(

        "https://xeno-crm-production-1dfc.up.railway.app/api/lead/all"

      );


      const data = await response.json();

      setLeads(data);

    }

    catch {

      toast.error(

        "Failed To Load Leads"

      );

    }

  };






  useEffect(() => {

    fetchLeads();

  }, []);







  // AI EMAIL GENERATOR

  const generateEmail = async (id) => {

    try {

      const response = await fetch(

        `https://xeno-crm-production-1dfc.up.railway.app/api/ai/email/${id}`

      );


      const data = await response.text();


      setGeneratedEmail(data);
      const lead = leads.find(

        l => l.id === id

      );

      setEmailTo(

        lead.email

      );

      setShowEmail(true);


      toast.success(

        "AI Email Generated"

      );

    }

    catch {

      toast.error(

        "Failed To Generate Email"

      );

    }

  };







  const copyEmail = () => {

    navigator.clipboard.writeText(

      generatedEmail

    );


    toast.success(

      "Email Copied"

    );

  };
const sendEmail = async () => {

  try {

    await fetch(

      "http://localhost:9090/api/email/send",

      {

        method:"POST",

        headers:{

          "Content-Type":"application/json"

        },

        body:JSON.stringify({

          to: emailTo,

          subject:"Xeno CRM Offer",

          body: generatedEmail

        })

      }

    );

    toast.success(

      "Email Sent Successfully"

    );

  }

  catch {

    toast.error(

      "Email Sending Failed"

    );

  }

}







  const exportCSV = () => {

    const headers = [

      "ID",

      "Name",

      "Company",

      "AI Score",

      "Priority"

    ];



    const rows = leads.map(

      (lead) => [

        lead.id,

        lead.customerName,

        lead.company,

        lead.aiScore,

        lead.priority

      ]

    );



    const csv =

      [headers, ...rows]

      .map(

        row => row.join(",")

      )

      .join("\n");



    const blob = new Blob(

      [csv],

      {

        type:

        "text/csv;charset=utf-8;"

      }

    );



    const url =

      URL.createObjectURL(blob);



    const link =

      document.createElement("a");



    link.href = url;

    link.download = "leads.csv";

    link.click();



    toast.success(

      "CSV Exported"

    );

  };








  const updateStatus = async (id) => {

    try {

      await fetch(

        `https://xeno-crm-production-1dfc.up.railway.app/api/lead/status/${id}`,

        {

          method: "PUT",

          headers: {

            "Content-Type":

            "application/json"

          },

          body:

          JSON.stringify({

            status:

            "CONTACTED"

          })

        }

      );


      toast.success(

        "Lead Contacted"

      );


      fetchLeads();

    }

    catch {

      toast.error(

        "Operation Failed"

      );

    }

  };








  const convertLead = async (id) => {

    try {

      await fetch(

        `https://xeno-crm-production-1dfc.up.railway.app/api/lead/convert/${id}`,

        {

          method: "POST"

        }

      );


      toast.success(

        "Lead Converted"

      );


      fetchLeads();

    }

    catch {

      toast.error(

        "Conversion Failed"

      );

    }

  };








  const deleteLead = async (id) => {

    const confirmDelete =

      window.confirm(

        "Delete this lead?"

      );


    if (!confirmDelete)

      return;



    try {

      await fetch(

        `https://xeno-crm-production-1dfc.up.railway.app/api/lead/delete/${id}`,

        {

          method: "DELETE"

        }

      );


      toast.warning(

        "Lead Deleted"

      );


      fetchLeads();

    }

    catch {

      toast.error(

        "Delete Failed"

      );

    }

  };
  const startEdit = (lead) => {

    setEditingLead(lead.id);

    setFormData({

      customerName: lead.customerName,

      email: lead.email,

      phone: lead.phone,

      company: lead.company,

      source: lead.source,

      assignedTo: lead.assignedTo,

      status: lead.status,

      notes: lead.notes

    });

  };
  const updateLead = async () => {

    try {

      await fetch(

        `https://xeno-crm-production-1dfc.up.railway.app/api/lead/update/${editingLead}`,

        {

          method: "PUT",

          headers: {

            "Content-Type": "application/json"

          },

          body: JSON.stringify(formData)

        }

      );

      toast.success("Lead Updated");

      setEditingLead(null);

      fetchLeads();

    }

    catch {

      toast.error("Update Failed");

    }

  };
    const filteredLeads =

      leads.filter(

        (lead) =>

          (

            filter === "ALL"

            ||

            lead.status === filter

          )

          &&

          (

            lead.customerName

              ?.toLowerCase()

              .includes(

                search.toLowerCase()

              )

          )

      );





    const indexOfLastLead =

      currentPage * leadsPerPage;


    const indexOfFirstLead =

      indexOfLastLead - leadsPerPage;



    const currentLeads =

      filteredLeads.slice(

        indexOfFirstLead,

        indexOfLastLead

      );



    const totalPages =

      Math.ceil(

        filteredLeads.length

        /

        leadsPerPage

      );







    return (

      <div className="container mt-4">

        <div className="d-flex justify-content-between mb-4">

          <h1>

            🤖 AI Leads Management

          </h1>



          <button

            className="btn btn-success"

            onClick={exportCSV}

          >

            Export CSV

          </button>

        </div>



{
editingLead &&

<div className="card p-4 mb-4 shadow">

<h3 className="mb-3">

Edit Lead

</h3>


<input

className="form-control mb-3"

placeholder="Customer Name"

value={formData.customerName}

onChange={(e)=>

setFormData({

...formData,

customerName:e.target.value

})

}

/>



<input

className="form-control mb-3"

placeholder="Email"

value={formData.email}

onChange={(e)=>

setFormData({

...formData,

email:e.target.value

})

}

/>



<input

className="form-control mb-3"

placeholder="Phone"

value={formData.phone}

onChange={(e)=>

setFormData({

...formData,

phone:e.target.value

})

}

/>



<input

className="form-control mb-3"

placeholder="Company"

value={formData.company}

onChange={(e)=>

setFormData({

...formData,

company:e.target.value

})

}

/>



<select

className="form-select mb-3"

value={formData.status}

onChange={(e)=>

setFormData({

...formData,

status:e.target.value

})

}

>

<option value="NEW">

NEW

</option>

<option value="CONTACTED">

CONTACTED

</option>

<option value="CONVERTED">

CONVERTED

</option>

</select>




<button

className="btn btn-warning me-3"

onClick={updateLead}

>

Update Lead

</button>




<button

className="btn btn-secondary"

onClick={()=>

setEditingLead(null)

}

>

Cancel

</button>

</div>

}



        <div className="row mb-4">

          <div className="col-md-6">

            <input

              className="form-control"

              placeholder="Search Lead"

              value={search}

              onChange={(e) =>

                setSearch(

                  e.target.value

                )

              }

            />

          </div>





          <div className="col-md-3">

            <select

              className="form-select"

              value={filter}

              onChange={(e) =>

                setFilter(

                  e.target.value

                )

              }

            >

              <option value="ALL">

                ALL

              </option>

              <option value="NEW">

                NEW

              </option>

              <option value="CONTACTED">

                CONTACTED

              </option>

              <option value="CONVERTED">

                CONVERTED

              </option>

            </select>

          </div>

        </div>








        <div className="table-responsive">

          <table className="table table-bordered table-hover align-middle">

            <thead className="table-dark">

              <tr>

                <th>ID</th>

                <th>Name</th>

                <th>Company</th>

                <th>Status</th>

                <th>AI Score</th>

                <th>Priority</th>

                <th>AI Summary</th>

                {

                  isAdmin

                  &&

                  <th>Actions</th>

                }

              </tr>

            </thead>






            <tbody>

              {

                currentLeads.map(

                  (lead) => (

                  <tr key={lead.id}>


                    <td>

                      {lead.id}

                    </td>


                    <td>

                      {lead.customerName}

                    </td>


                    <td>

                      {lead.company}

                    </td>


                    <td>

                      {lead.status}

                    </td>






                    <td>

                      <div

                        className="progress"

                        style={{

                          height: "25px"

                        }}

                      >

                        <div

                          className="progress-bar"

                          style={{

                            width:

                            `${lead.aiScore || 0}%`

                          }}

                        >

                          {

                            lead.aiScore || 0

                          }

                        </div>

                      </div>

                    </td>






                    <td>

                      <span

                        className={

                          lead.priority ===

                          "HOT 🔥"

                          ?

                          "badge bg-danger"

                          :

                          lead.priority ===

                          "MEDIUM"

                          ?

                          "badge bg-warning text-dark"

                          :

                          "badge bg-success"

                        }

                      >

                        {

                          lead.priority

                        }

                      </span>

                    </td>







                    <td

                      style={{

                        maxWidth: "300px"

                      }}

                    >

                      <small>

                        {

                          lead.aiSummary

                        }

                      </small>

                    </td>








                    {

                      isAdmin

                      &&

                      <td>
<button

className="btn btn-primary btn-sm me-2"

onClick={() => startEdit(lead)}

>

Edit

</button>

                        <button

                          className="btn btn-info btn-sm me-2"

                          onClick={() =>

                            generateEmail(

                              lead.id

                            )

                          }

                        >

                          ✉ Email

                        </button>





                        {

                          lead.status ===

                          "NEW"

                          &&

                          <button

                            className="btn btn-warning btn-sm me-2"

                            onClick={() =>

                              updateStatus(

                                lead.id

                              )

                            }

                          >

                            Contact

                          </button>

                        }






                        {

                          lead.status !==

                          "CONVERTED"

                          &&

                          <button

                            className="btn btn-success btn-sm me-2"

                            onClick={() =>

                              convertLead(

                                lead.id

                              )

                            }

                          >

                            Convert

                          </button>

                        }







                        <button

                          className="btn btn-danger btn-sm"

                          onClick={() =>

                            deleteLead(

                              lead.id

                            )

                          }

                        >

                          Delete

                        </button>

                      </td>

                    }

                  </tr>

                ))

              }

            </tbody>

          </table>

        </div>








        {

          showEmail

          &&

          <div

            className="modal d-block"

            style={{

              background:

              "rgba(0,0,0,0.5)"

            }}

          >

            <div className="modal-dialog modal-lg">

              <div className="modal-content">


                <div className="modal-header">

                  <h5>

                    AI Generated Email

                  </h5>



                  <button

                    className="btn-close"

                    onClick={() =>

                      setShowEmail(false)

                    }

                  />

                </div>





                <div className="modal-body">

                  <pre

                    style={{

                      whiteSpace:

                      "pre-wrap"

                    }}

                  >

                    {

                      generatedEmail

                    }

                  </pre>

                </div>





                <div className="modal-footer">

                  <button

                    className="btn btn-primary"

                    onClick={copyEmail}

                  >

                    Copy Email

                  </button>
<button

className="btn btn-success"

onClick={sendEmail}

>

Send Email

</button>
                  <button

                    className="btn btn-secondary"

                    onClick={() =>

                      setShowEmail(false)

                    }

                  >

                    Close

                  </button>

                </div>

              </div>

            </div>

          </div>

        }








        <div className="d-flex justify-content-center mt-4">

          <button

            className="btn btn-outline-primary me-3"

            disabled={

              currentPage === 1

            }

            onClick={() =>

              setCurrentPage(

                currentPage - 1

              )

            }

          >

            Previous

          </button>





          <span className="mt-2">

            Page

            {" "}

            {currentPage}

            {" "}

            of

            {" "}

            {totalPages}

          </span>





          <button

            className="btn btn-outline-primary ms-3"

            disabled={

              currentPage === totalPages

              ||

              totalPages === 0

            }

            onClick={() =>

              setCurrentPage(

                currentPage + 1

              )

            }

          >

            Next

          </button>

        </div>

      </div>

    );

  }

  export default Leads;