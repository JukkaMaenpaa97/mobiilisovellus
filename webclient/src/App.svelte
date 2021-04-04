<script>
	import UIkit from 'uikit';
	import Icons from 'uikit/dist/js/uikit-icons';
	UIkit.use(Icons);

	import { Router, Link, Route } from "svelte-routing";
	import Home from './routes/home.svelte';
	import Login from './routes/login.svelte';
	import Register from './routes/register.svelte';
	import Profile from './routes/profile.svelte';

	export let url = "";
</script>

<div class="uk-section">
	<div class="uk-container">
		<Router url="{url}">
			<nav>
				<Link to="/">Home</Link>
				<Link to="login">Login</Link>
				<Link to="register">Register</Link>
				<Link to="profile">Profile</Link>
			</nav>
			<div>
				<Route path="register" component="{Register}" />
				<Route path="login" component="{Login}" />
				<Route path="profile" component="{Profile}" />
				<Route path="/"><Home /></Route>
			</div>
		</Router>
	</div>
</div>
