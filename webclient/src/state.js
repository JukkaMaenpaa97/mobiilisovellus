import { writable } from 'svelte/store';

export const SessionStore = writable({
    "session_apikey": "",
    "session_logged_in": false,
    "session_expired": false
})
