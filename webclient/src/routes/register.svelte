<script>
    import { navigate } from 'svelte-routing';

    let username;
    let password;
    let name;

    let loading = false;

    function registerAccount(e){
        e.preventDefault();
        loading = true;
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
                    <input type="text" class="uk-input" name="name" placeholder="Koko nimi" bind:value={name} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <label class="uk-form-label">Sähköpostiosoite</label>
                    <input type="text" class="uk-input" name="username" placeholder="Sähköpostiosoite" bind:value={username} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <label class="uk-form-label">Salasana</label>
                    <input type="password" class="uk-input" name="password" placeholder="Salasana" bind:value={password} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <label class="uk-form-label">Vahvista</label>
                    <input type="password" class="uk-input" name="password" placeholder="Vahvista salasana" bind:value={password} disabled={loading}>
                </div>
                <div class="uk-margin">
                    <input type="submit" class="uk-button uk-button-primary" value="Luo Jobster-tili" disabled={loading}>
                    <button class="uk-button uk-button-secondary" on:click={() => {navigate("/login");}}>Peruuta</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>
