<script>

    import { navigate, Link } from 'svelte-routing';
    import { createEventDispatcher } from 'svelte';
    const dispatch = createEventDispatcher();

    import Api from "../services/api.js";

    let user_email;
    let user_password;

    let loading = false;
    let field_class = "";
    let error_message = "";

    function doLogin(e){
        e.preventDefault();
        loading = true;

        Api.login(user_email, user_password,
            (response) => {
                loading = false;
                field_class = "uk-form-success";
                error_message = "";
            },
            (error) => {
                loading = false;
                error_message = "Virheelliset kirjautumistiedot!";
                field_class="uk-form-danger";
            }
        );
    }
</script>

<div class="uk-section">
    <div class="uk-container uk-container-xsmall">
        <h1>Jobster - Töille tekijöitä ja tekijöille töitä</h1>
        <form method="post" on:submit={doLogin}>
            <fieldset class="uk-fieldset">
                <legend class="uk-legend">Kirjaudu sisään {#if loading}<div uk-spinner></div>{/if}</legend>
                <div class="uk-margin">
                    <input type="text" class="uk-input { field_class }" name="user_email" placeholder="Sähköpostiosoite" bind:value={user_email} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <input type="password" class="uk-input { field_class }" name="user_password" placeholder="Salasana" bind:value={user_password} disabled={loading}>
                </div>
                {#if error_message !== ""}
                <div class="uk-alert uk-alert-danger" uk-alert>
                    {error_message}
                </div>
                {/if}
                <div class="uk-margin">
                    <input type="submit" class="uk-button uk-button-primary" value="Kirjaudu" disabled={loading}>
                    tai
                    <button class="uk-button uk-button-secondary" on:click={ () => {navigate("/register");}}>Rekisteröidy</button>
                </div>
            </fieldset>
        </form>
        <Link to="">Unohditko salasanasi?</Link>
    </div>
</div>
