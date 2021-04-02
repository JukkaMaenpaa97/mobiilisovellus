<script>
	import UIkit from 'uikit';
	import Icons from 'uikit/dist/js/uikit-icons';
	UIkit.use(Icons);

	import Login from './components/login.svelte'

	function onLogin(e){
		UIkit.notification('<span uk-icon="icon: check"></span> Apikey retrieved', {pos: 'top-right'});
	}
</script>

<main>
	<Login on:login={onLogin}/>
</main>
