<script>
    import UIkit from 'uikit';
    import Icons from 'uikit/dist/js/uikit-icons';
    UIkit.use(Icons);

    import { navigate } from 'svelte-routing';
    import Api from '../services/api.js';
    let user_email;
    let user_password;
    let user_password_again;
    let user_name;
    let field_classes = {

    }

    let loading = false;
    let register_success = false;
    let error_message = "";

    function registerAccount(e){
        e.preventDefault();
        loading = true;

        Api.register(user_email, user_name, user_password, user_password_again,
            (response) => {
                loading = false;
                navigate('/registersuccess');
            },
            (error) => {

                if(error.response.status==500){
                    UIkit.notification('<span uk-icon="icon: warning"></span> Palvelin kohtasi ongelman, eikä pyyntöä voitu suorittaa.', {pos: 'top-right', status: 'danger'});
                }else if(error.response.status==400){
                    error_message = "Tarkista tiedot ja yritä uudelleen.";
                    field_classes = {};
                    error.response.data.invalid_fields.forEach((item) => {
                        field_classes[item] = "uk-form-danger";
                    });
                }
                loading = false;
            }
        );
    }
</script>

<div class="uk-section">
    <div class="uk-container uk-container-xsmall">
        <h1>Luo uusi tili Jobsteriin</h1>

        <form method="post" on:submit={registerAccount}>
            <fieldset class="uk-fieldset">
                <legend class="uk-legend">Rekisteröidy {#if loading}<div uk-spinner></div>{/if}</legend>

                <div class="uk-margin">
                    <label class="uk-form-label">Koko nimi</label>
                    <input type="text" class="uk-input { field_classes['user_name']}" name="user_name" placeholder="Koko nimi" bind:value={user_name} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <label class="uk-form-label">Sähköpostiosoite</label>
                    <input type="text" class="uk-input { field_classes['user_email'] }" name="user_email" placeholder="Sähköpostiosoite" bind:value={user_email} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <label class="uk-form-label">Salasana</label>
                    <input type="password" class="uk-input  { field_classes['user_password']}" name="user_password" placeholder="Salasana" bind:value={user_password} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <label class="uk-form-label">Vahvista salasana</label>
                    <input type="password" class="uk-input { field_classes['user_password_again']}" name="user_password_again" placeholder="Vahvista salasana" bind:value={user_password_again} disabled={loading}>
                </div>
                {#if error_message !== ""}
                <div class="uk-alert uk-alert-danger" uk-alert>
                    {error_message}
                </div>
                {/if}
                <div class="uk-margin">
                    <input type="submit" class="uk-button uk-button-primary" value="Luo Jobster-tili" disabled={loading}>
                    <button class="uk-button uk-button-secondary" on:click={() => {navigate("/login");}}>Peruuta</button>
                </div>

            </fieldset>
        </form>
    </div>
</div>
