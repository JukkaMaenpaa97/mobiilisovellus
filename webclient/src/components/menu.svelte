<script>
	import { Router, Link, Route } from "svelte-routing";
</script>

<div uk-sticky="sel-target: .uk-navbar-container; cls-active: uk-navbar-sticky">
    <div class="uk-navbar-container jobster-navbar-container uk-light">
        <div class="uk-container uk-container-expand">
            <div class="uk-navbar">
                <div class="uk-navbar-left">
                    <a class="uk-navbar-item uk-logo" href="./">JOBSTER</a>
                    <ul class="uk-navbar-nav">
                        <li><Link to="/">Selaa palveluita</Link></li>
                        <li><a href="#">Paikkakunnat</a></li>
                        <li><a href="#">Ryhdy yrittäjäksi</a></li>
                    </ul>
                </div>
                <div class="uk-navbar-right">
                    <ul class="uk-navbar-nav">
                        <li><Link to="login">Kirjaudu</Link></li>
                        <li><Link to="register">Rekisteröidy</Link></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    .jobster-navbar-container{
        background-color:#222222;
    }
</style>
