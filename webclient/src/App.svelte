<script>
	import UIkit from 'uikit';
	import Icons from 'uikit/dist/js/uikit-icons';
	UIkit.use(Icons);

	import { Router, Link, Route } from "svelte-routing";
	import Home from './routes/home.svelte';
	import Login from './routes/login.svelte';
	import Register from './routes/register.svelte';
	import RegisterSuccess from './routes/registersuccess.svelte';
	import Profile from './routes/profile.svelte';
	import Menu from './components/menu.svelte';

	export let url = "";
</script>

<Router url="{url}">
	<Menu />
	<div class="uk-section">
		<div class="uk-container">
			<Route path="register" component="{Register}" />
			<Route path="registersuccess" component="{RegisterSuccess}" />
			<Route path="login" component="{Login}" />
			<Route path="profile" component="{Profile}" />
			<Route path="/"><Home /></Route>
		</div>
	</div>
</Router>
