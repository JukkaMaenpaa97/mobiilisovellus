<script>
	import UIkit from 'uikit';
	import Icons from 'uikit/dist/js/uikit-icons';
	UIkit.use(Icons);

	import Menu from './components/menu.svelte';
	import Login from './components/login.svelte';
	import ServiceList from './components/servicelist.svelte';

	function onLogin(e){
		UIkit.notification('<span uk-icon="icon: check"></span> Apikey retrieved', {pos: 'top-right'});
	}
</script>

<main>
<Menu />
<div class="uk-section">
<div class="uk-container uk-container-small">
	<ServiceList />
</div>
	<!--<Login on:login={onLogin}/>-->
</div>
</main>
