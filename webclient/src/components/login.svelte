<script>
    import { createEventDispatcher } from 'svelte';
    const dispatch = createEventDispatcher();

    import Api from "../services/api.js";

    let username;
    let password;

    async function doLogin(e){
        e.preventDefault();
        Api.login(username, password);
        let apikey = "oasijfoaiejfoei291024j01294j0";
        dispatch("login", {apikey: apikey});
    }
</script>

<div class="uk-section">
    <div class="uk-container uk-container-xsmall">
        <h1>Jobster webclient 0.1b</h1>
        <form method="post" on:submit={doLogin}>
            <fieldset class="uk-fieldset">
                <legend class="uk-legend">Kirjaudu sisään</legend>
                <div class="uk-margin">
                    <input type="text" class="uk-input" name="username" placeholder="Sähköpostiosoite" bind:value={username}>
                </div>
                <div class="uk-margin">
                    <input type="password" class="uk-input" name="password" placeholder="Salasana" bind:value={password}>
                </div>
                <div class="uk-margin">
                    <input type="submit" class="uk-button uk-button-primary" value="Kirjaudu">
                    tai
                    <button class="uk-button uk-button-secondary">Rekisteröidy</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>
