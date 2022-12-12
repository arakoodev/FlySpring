import React, { useState, useEffect } from "react";
import '../App.css';
import { useAuth0 } from "@auth0/auth0-react";
export default function Comp() {
    const { getAccessTokenSilently } = useAuth0();
    const [token, setToken] = useState('not loaded yet');
    //const [msg, setMsg] = useState('no msgs yet');
    useEffect(()=>{
        getAccessTokenSilently().then(t => setToken(t));
    },[getAccessTokenSilently]);
    return (
        <div>
            <p>{token}</p>
            {/*<button onClick={()=>{
                fetch('http://localhost:3010/api/public')
                    .then(res => res.json())
                    .then(res => {
                        console.log(res)
                        setMsg(res.message)
                    })
            }}>
                call public
            </button>
            <button onClick={()=>{
                fetch('http://localhost:3010/api/private', {
                    method:'GET',
                    mode:'no-cors',
                    headers:{
                        'Authorization':`Bearer ${token}`
                    }
                })
                .then(res => res.json())
                    .then(res => {
                        console.log(res)
                        setMsg(res.message)
                    })
            }}>
                call private
            </button>
        <h1>{msg}</h1>*/}
        </div>
    )
}